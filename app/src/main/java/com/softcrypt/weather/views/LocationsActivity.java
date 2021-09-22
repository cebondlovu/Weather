package com.softcrypt.weather.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.softcrypt.weather.adapters.LocationsAdapter;
import com.softcrypt.weather.base.BaseApplication;
import com.softcrypt.weather.database.LocationsDatabaseHelper;
import com.softcrypt.weather.models.ItemLocation;
import com.softcrypt.weather.R;
import com.softcrypt.weather.viewModels.LocationsViewModel;
import com.softcrypt.weather.viewModels.MainViewModel;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

public class LocationsActivity extends AppCompatActivity {

    RecyclerView locations_recycler;
    Button delete_all_btn, add_location_btn;
    TextView txt_no_data;
    LocationsDatabaseHelper dbHelper;
    Toolbar toolbar;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private LocationsViewModel locationsViewModel;

/*    @Override
    protected void onStart() {
        super.onStart();
        locationsViewModel = new ViewModelProvider(this, viewModelFactory).get(LocationsViewModel.class);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        ((BaseApplication) getApplication()).getAppComponent().injectLocationsAct(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        locations_recycler = findViewById(R.id.locations_recycler);
        delete_all_btn = findViewById(R.id.delete_all_btn);
        txt_no_data = findViewById(R.id.txt_no_data);
        delete_all_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.deleteAllPlaces();
                getLocations();
                showProcedure();
                Toast.makeText(view.getContext(), "Locations Removed", Toast.LENGTH_LONG).show();
            }
        });

        add_location_btn = findViewById(R.id.add_location_btn);
        add_location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (dbHelper.getAllUserLocations().size() < 4) {
                    Intent intent = new Intent(view.getContext(), MapsActivity.class);
                    startActivity(intent);
                //}
            }
        });

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

    @Override
    protected void onResume() {
        getLocations();
        showProcedure();
        super.onResume();
    }

    public void getLocations(){
        dbHelper = new LocationsDatabaseHelper((BaseApplication) this.getApplication());
        ArrayList<ItemLocation> list;
        list = dbHelper.getAllUserLocations();
        LocationsAdapter adapter = new LocationsAdapter((BaseApplication) this.getApplication(),list);
        locations_recycler.setAdapter(adapter);
    }

    public void showProcedure(){
        dbHelper = new LocationsDatabaseHelper((BaseApplication) this.getApplication());

        if (dbHelper.getAllUserLocations().size() == 4) {
            add_location_btn.setVisibility(View.GONE);
            delete_all_btn.setVisibility(View.VISIBLE);
            locations_recycler.setVisibility(View.VISIBLE);
        }else if (dbHelper.getAllUserLocations().size() == 0){
            txt_no_data.setVisibility(View.VISIBLE);
            locations_recycler.setVisibility(View.INVISIBLE);
            add_location_btn.setVisibility(View.VISIBLE);
            delete_all_btn.setVisibility(View.GONE);
        }else{
            delete_all_btn.setVisibility(View.VISIBLE);
            add_location_btn.setVisibility(View.VISIBLE);
            txt_no_data.setVisibility(View.INVISIBLE);
            locations_recycler.setVisibility(View.VISIBLE);
        }
    }

    public void closeAct(View view) {
    }
}