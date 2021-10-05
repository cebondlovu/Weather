package com.softcrypt.weather.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.ContentLoadingProgressBar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.softcrypt.weather.R;
import com.softcrypt.weather.base.BaseApplication;
import com.softcrypt.weather.common.Common;
import com.softcrypt.weather.viewModels.WelcomeViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class WelcomeAct extends AppCompatActivity {
    private ConstraintLayout constraintLayout;
    private ImageView logo;
    private TextView title;
    public static TextView text;
    ProgressBar loading;
    private WelcomeViewModel welcomeViewModel;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    @Override
    protected void onStart() {
        super.onStart();
        buildLocationRequest();
        buildLocationCallBack();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ((BaseApplication) getApplication()).getAppComponent().injectWelcomeAct(this);
        welcomeViewModel = new WelcomeViewModel((BaseApplication) this.getApplication(), Realm.getDefaultInstance());
        constraintLayout = findViewById(R.id.root_view);
        logo = findViewById(R.id.logo);
        title = findViewById(R.id.title);
        loading = findViewById(R.id.progress);
        text = findViewById(R.id.text);

        getNetworkProviderState();

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            buildLocationRequest();
                            buildLocationCallBack();

                            if (ActivityCompat.checkSelfPermission(
                                    WelcomeAct.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                                    PackageManager.PERMISSION_GRANTED &&
                                    ActivityCompat.checkSelfPermission(WelcomeAct.this,
                                            Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                            PackageManager.PERMISSION_GRANTED) {

                                return;
                            }

                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(WelcomeAct.this);
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback,
                                    Looper.myLooper());
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Snackbar.make(constraintLayout, "Permissions Denied", Snackbar.LENGTH_LONG)
                                .show();
                    }
                }).check();

        try {
            text.setText("Setting Up!!");
            setupApp();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupApp() throws IOException {
        welcomeViewModel.getDoneResult().observe(this, cList -> {
            if(cList != null) {
                Common.globalCityList = cList;
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getNetworkProviderState() {
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    }

    private void buildLocationCallBack() {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Common.current_location = locationResult.getLastLocation();
                Log.d("Location", locationResult.getLastLocation().getLongitude()+"/"+
                        locationResult.getLastLocation().getLatitude());
            }

            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10.0f);
    }
}