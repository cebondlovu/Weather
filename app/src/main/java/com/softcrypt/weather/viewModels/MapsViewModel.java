package com.softcrypt.weather.viewModels;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.softcrypt.weather.base.BaseApplication;
import com.softcrypt.weather.models.ItemLocation;
import com.softcrypt.weather.repository.LocationDatabaseRepository;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import io.realm.Realm;

public class MapsViewModel extends AndroidViewModel {
    private LocationDatabaseRepository locationDatabaseRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MutableLiveData<ArrayList<ItemLocation>> modelAllLocMutableLivData = new MutableLiveData<>();
    private final MutableLiveData<String> modelCountMutableLivData = new MutableLiveData<>();

    public MapsViewModel(BaseApplication application, Realm realm){
        super(application);
        this.locationDatabaseRepository = new LocationDatabaseRepository(application, realm);
    }
    public MutableLiveData<ArrayList<ItemLocation>> getAllLocationsMutableLivData() {
        getAllUserLocations();
        return modelAllLocMutableLivData;
    }

    public void insertLocationMutableLivData(ItemLocation itemLocation) {
        insertLocation(itemLocation);
    }

    public MutableLiveData<String> getLocCountMutableLivData() {
        getLocationsCount();
        return modelCountMutableLivData;
    }

    private void getLocationsCount() {
        modelCountMutableLivData.setValue(String.valueOf(locationDatabaseRepository.modelGetLocationsCount()));
    }

    private void getAllUserLocations() {
        modelAllLocMutableLivData.setValue(locationDatabaseRepository.modelGetAllLocations());
    }

    private void insertLocation(ItemLocation itemLocation) {
        locationDatabaseRepository.modelInsertLocation(itemLocation);
    }
}
