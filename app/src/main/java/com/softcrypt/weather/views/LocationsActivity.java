package com.softcrypt.weather.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.folderselector.FileChooserDialog;
import com.softcrypt.weather.adapters.LocationsAdapter;
import com.softcrypt.weather.base.BaseApplication;
import com.softcrypt.weather.R;
import com.softcrypt.weather.common.MarshMellowPermission;
import com.softcrypt.weather.viewModels.LocationsViewModel;

import java.io.File;
import java.util.Objects;

import io.realm.Realm;

public class LocationsActivity extends AppCompatActivity {

    RecyclerView locations_recycler;
    Button delete_all_btn, add_location_btn;
    TextView txt_no_data;
    Toolbar toolbar;

    private LocationsViewModel locationsViewModel;

    @Override
    protected void onStart() {
        super.onStart();
        locationsViewModel =   new LocationsViewModel((BaseApplication) this.getApplication(), Realm.getDefaultInstance());
    }

    @Override
    protected void onResume() {
        locationsViewModel =   new LocationsViewModel((BaseApplication) this.getApplication(), Realm.getDefaultInstance());
        getLocations();
        showProcedure();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationsViewModel.clearDisposable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationsViewModel.clearDisposable();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        ((BaseApplication) getApplication()).getAppComponent().injectLocationsAct(this);
        locationsViewModel = new LocationsViewModel((BaseApplication) this.getApplication(), Realm.getDefaultInstance());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        locations_recycler = findViewById(R.id.locations_recycler);
        delete_all_btn = findViewById(R.id.delete_all_btn);
        txt_no_data = findViewById(R.id.txt_no_data);

        add_location_btn = findViewById(R.id.add_location_btn);

        locations_recycler.setHasFixedSize(true);
        locations_recycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false));

        showProcedure();
        getLocations();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void getLocations(){
        locationsViewModel.getAllLocationsMutableLivData().observe(this, itemLocations -> {
            if(itemLocations != null){
                LocationsAdapter adapter = new LocationsAdapter(this,itemLocations);
                locations_recycler.setAdapter(adapter);
            }
        });
    }

    public void showProcedure(){
        locationsViewModel.getAllLocationsMutableLivData().observe( this, itemLocations -> {
            if(itemLocations.size() >= 8) {
                add_location_btn.setVisibility(View.GONE);
                delete_all_btn.setVisibility(View.VISIBLE);
                locations_recycler.setVisibility(View.VISIBLE);
            } else if(itemLocations.size() == 0){
                txt_no_data.setVisibility(View.VISIBLE);
                locations_recycler.setVisibility(View.INVISIBLE);
                add_location_btn.setVisibility(View.VISIBLE);
                delete_all_btn.setVisibility(View.GONE);
            } else {
                delete_all_btn.setVisibility(View.VISIBLE);
                add_location_btn.setVisibility(View.VISIBLE);
                txt_no_data.setVisibility(View.INVISIBLE);
                locations_recycler.setVisibility(View.VISIBLE);
            }
        });
    }

    public void deleteAllLocations(View view) {
        locationsViewModel.deleteAllMutableLivData();
                getLocations();
                showProcedure();
                Toast.makeText(view.getContext(), "Locations Removed", Toast.LENGTH_LONG).show();
    }

    public void addLocation(View view) {
        locationsViewModel.getAllLocationsMutableLivData().observe(this, itemLocations -> {
            if(itemLocations.size() <= 8){
                Intent intent = new Intent(view.getContext(), MapsActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(view.getContext(), "Too Many Locations", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void importStart(View view) {
        Intent intent = new Intent(this, ViewPopup.class);
        intent.putExtra(ViewPopup.$CALL_TYPE, "IMPORT");
        startActivity(intent);
    }

    public void backupStart(View view) {
        Intent intent = new Intent(this, ViewPopup.class);
        intent.putExtra(ViewPopup.$CALL_TYPE, "BACKUP");
        startActivity(intent);
    }
}