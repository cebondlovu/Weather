package com.softcrypt.weather.viewModels;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.afollestad.materialdialogs.folderselector.FileChooserDialog;
import com.softcrypt.weather.base.BaseApplication;
import com.softcrypt.weather.models.ItemLocation;
import com.softcrypt.weather.repository.LocationDatabaseRepository;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import io.realm.Realm;

public class LocationsViewModel extends AndroidViewModel {

    private final LocationDatabaseRepository locationDatabaseRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MutableLiveData<ArrayList<ItemLocation>> modelAllLocMutableLivData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> modelDeleteAllMutableLivData = new MutableLiveData<>();

    public LocationsViewModel(BaseApplication application, Realm realm){
        super(application);
        this.locationDatabaseRepository = new LocationDatabaseRepository(application,realm);
    }

    public MutableLiveData<ArrayList<ItemLocation>> getAllLocationsMutableLivData() {
        getAllUserLocations();
        return modelAllLocMutableLivData;
    }

    public void deleteAllMutableLivData() {
        deleteAllLocations();
    }

    private void getAllUserLocations() {
        modelAllLocMutableLivData.setValue(locationDatabaseRepository.modelGetAllLocations());
    }

    private void deleteAllLocations(){
        locationDatabaseRepository.modelDeleteAllLocations();
    }



    public void clearDisposable(){
        if(disposable != null)
            disposable.clear();
    }
}
