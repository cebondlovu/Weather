package com.softcrypt.weather.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.softcrypt.weather.models.ItemLocation;
import com.softcrypt.weather.repository.LocationsDatabaseHelperRepository;

import java.util.ArrayList;

import javax.inject.Inject;

public class MapsViewModel extends ViewModel {
    private LocationsDatabaseHelperRepository locationsDatabaseHelperRepository;
    private final MutableLiveData<ArrayList<ItemLocation>> modelLocationListMutableLivData = new MutableLiveData<>();

    @Inject
    public MapsViewModel(LocationsDatabaseHelperRepository locationsDatabaseHelperRepository){
        this.locationsDatabaseHelperRepository = locationsDatabaseHelperRepository;
    }
    public MutableLiveData<ArrayList<ItemLocation>> getLocationListModelData(String name) {
        getUserLocations();
        return modelLocationListMutableLivData;
    }

    private void getUserLocations() {
        locationsDatabaseHelperRepository.deleteAllPlaces();
    }
}
