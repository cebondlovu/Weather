package com.softcrypt.weather.views;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.softcrypt.weather.R;
import com.softcrypt.weather.base.BaseApplication;

import com.softcrypt.weather.viewModels.MapsViewModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Marker currentUser;
    private final List<LatLng> other_locations = new ArrayList<>();

    private MapsViewModel mapsViewModel;

    @Override
    protected void onStart() {
        super.onStart();
        mapsViewModel = new MapsViewModel((BaseApplication) this.getApplication(), Realm.getDefaultInstance());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ((BaseApplication) getApplication()).getAppComponent().injectMapsAct(this);
        mapsViewModel = new MapsViewModel((BaseApplication) this.getApplication(), Realm.getDefaultInstance());
        buildLocationRequest();
        buildLocationCallBack();
        getLocations();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    private void getLocations() {
        mapsViewModel.getLocCountMutableLivData().observe(this, count -> {
            mapsViewModel.getAllLocationsMutableLivData().observe(this, itemLocations -> {
                if (itemLocations != null)
                    if (Integer.parseInt(count) > 0) {
                        for (int i = 0; i < itemLocations.size(); i++) {
                            other_locations.add(new LatLng(Double.parseDouble(itemLocations.get(i).getLat()),
                                    Double.parseDouble(itemLocations.get(i).getLng())));
                        }
                    }
            });
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.setOnMarkerDragListener(this);

        if (fusedLocationProviderClient != null)
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

        assert fusedLocationProviderClient != null;
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

        //Adding Other Stored locations
        if(other_locations != null)
            for (LatLng latLng : other_locations){
                //you can also use mMap.addMarker
                mMap.addCircle(new CircleOptions().center(latLng)
                        .radius(50) //50m
                        .strokeColor(Color.GREEN)
                        .fillColor(0x220000FF) //transparent
                        .strokeWidth(5.0f));


            }
    }

    @Override
    protected void onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }

    private void buildLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10.0f);
    }

    private void buildLocationCallBack() {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if(mMap != null){
                    if(currentUser != null)
                        currentUser.remove();

                    currentUser = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(locationResult.getLastLocation().getLatitude(),
                                    locationResult.getLastLocation().getLongitude())).title("You").draggable(true));

                    assert currentUser != null;
                    mMap.animateCamera(CameraUpdateFactory
                            .newLatLngZoom(currentUser.getPosition(), 12.0f));
                }
            }

            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        mapsViewModel.getAllLocationsMutableLivData().observe(this, itemLocations -> {
            if(itemLocations.size() >= 8)
                Toast.makeText(this,"You Have Reached Limit of 8", Toast.LENGTH_LONG).show();
            else {
                Intent intent = new Intent(this, ViewPopup.class);
                intent.putExtra(ViewPopup.$CALL_TYPE, "CONFIRM");
                intent.putExtra(ViewPopup.LA_T, String.valueOf(marker.getPosition().latitude));
                intent.putExtra(ViewPopup.LN_G, String.valueOf(marker.getPosition().longitude));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}