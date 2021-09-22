package com.softcrypt.weather.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.softcrypt.weather.base.BaseApplication;
import com.softcrypt.weather.models.ItemLocation;
import com.softcrypt.weather.repository.LocationsDatabaseHelperRepository;

import java.util.ArrayList;

import javax.inject.Inject;

public class LocationsViewModel extends AndroidViewModel {
    private final LocationsDatabaseHelperRepository locationsDatabaseHelperRepository;
    private final MutableLiveData<ArrayList<ItemLocation>> modelLocationListMutableLivData = new MutableLiveData<>();
    private final MutableLiveData<ItemLocation> modelLocationMutableLivData = new MutableLiveData<>();

    @Inject
    public LocationsViewModel(BaseApplication application){
        super(application);
        this.locationsDatabaseHelperRepository = new LocationsDatabaseHelperRepository(application);
    }

    public MutableLiveData<ArrayList<ItemLocation>> getLocationListModelData(String name) {
        getUserLocations();
        return modelLocationListMutableLivData;
    }

    public MutableLiveData<ItemLocation> getLocationModelData(String name) {
        getSelectedLocation(name);
        return modelLocationMutableLivData;
    }

    public void deleteAllModel() {
        deleteAllPlaces();
    }

    public void deleteSelectedModel(String uniqueId){
        deletePlace(uniqueId);
    }

    public void storeLocationModel(ItemLocation itemLocation) {
        insertNewPlace(itemLocation);
    }

    private void insertNewPlace(ItemLocation itemLocation) {

    }

    private void getUserLocations() {
        locationsDatabaseHelperRepository.getAllUserLocations();
    }


    private void getSelectedLocation(String name) {

    }

    private void deleteAllPlaces(){

    }

    private void deletePlace(String uniqueId) {

    }
}
