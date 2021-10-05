package com.softcrypt.weather.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.softcrypt.weather.adapters.ViewPagerAdapter;
import com.softcrypt.weather.base.BaseApplication;
import com.softcrypt.weather.common.Common;
import com.softcrypt.weather.common.MarshMellowPermission;
import com.softcrypt.weather.viewModels.MainViewModel;
import com.softcrypt.weather.viewModels.MapsViewModel;
import com.softcrypt.weather.views.fragments.CitiesFrag;
import com.softcrypt.weather.views.fragments.ForecastFrag;
import com.softcrypt.weather.views.fragments.TodayWeatherFrag;
import com.softcrypt.weather.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private BottomNavigationView bottomNavigationView;
    private Fragment selectorFragment;

    public static String GLOBAL_LIST = "gList";
    public static List<String> list;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private MainViewModel mainViewModel;
    private MapsViewModel mapsViewModel;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainViewModel.clearDisposable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainViewModel = new ViewModelProvider(this, viewModelFactory)
                .get(MainViewModel.class);
        mapsViewModel = new MapsViewModel((BaseApplication) this.getApplication(), Realm.getDefaultInstance());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mainViewModel = new ViewModelProvider(this, viewModelFactory)
                .get(MainViewModel.class);
        mapsViewModel = new MapsViewModel((BaseApplication) this.getApplication(), Realm.getDefaultInstance());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((BaseApplication) getApplication()).getAppComponent().injectMainAct(this);

        mainViewModel = new ViewModelProvider(this, viewModelFactory)
                .get(MainViewModel.class);
        mapsViewModel = new MapsViewModel((BaseApplication) this.getApplication(), Realm.getDefaultInstance());



        list = new ArrayList<>();
        list = Common.globalCityList;
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_today:
                    selectorFragment = new TodayWeatherFrag(mainViewModel, mapsViewModel);
                    break;
                case R.id.nav_days:
                    selectorFragment = new ForecastFrag(mainViewModel);
                    break;
                case R.id.nav_city:
                    selectorFragment = new CitiesFrag(mainViewModel);
                    break;
                case R.id.nav_add:
                    selectorFragment = null;
                    startActivity(new Intent(this, LocationsActivity.class));
                    break;
            }

            if (selectorFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectorFragment).commit();
            }

            return true;
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new TodayWeatherFrag(mainViewModel, mapsViewModel)).commit();
        bottomNavigationView.setSelectedItemId(R.id.nav_today);
    }


}