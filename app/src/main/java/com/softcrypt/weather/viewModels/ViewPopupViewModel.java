package com.softcrypt.weather.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.softcrypt.weather.base.BaseApplication;
import com.softcrypt.weather.database.LocationsDatabaseHelper;
import com.softcrypt.weather.models.ItemLocation;
import com.softcrypt.weather.repository.LocationDatabaseRepository;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class ViewPopupViewModel  extends AndroidViewModel {

    private LocationDatabaseRepository locationDatabaseRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MutableLiveData<ArrayList<ItemLocation>> modelAllLocMutableLivData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> modelInsertLocMutableLivData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> modelDeletePlaceMutableLivData = new MutableLiveData<>();

    public ViewPopupViewModel(BaseApplication application, Realm realm) {
        super(application);
        this.locationDatabaseRepository = new LocationDatabaseRepository(application, realm);
    }

    public MutableLiveData<ArrayList<ItemLocation>> getAllLocationsMutableLivData() {
        getAllUserLocations();
        return modelAllLocMutableLivData;
    }

    public void importRealFileMutableLivData(String filePath){
        importRealmFile(filePath);
    }

    public void backupRealFileMutableLivData() {
        backupRealmFile();
    }

    public void insertLocationMutableLivData(ItemLocation itemLocation) {
        insertLocation(itemLocation);
    }

    public void deleteLocationMutableLivData(String uniqueId) {
        deleteLocation(uniqueId);
    }

    private void deleteLocation(String uniqueId) {
        locationDatabaseRepository.modelDeleteLocation(uniqueId);
    }

    private void getAllUserLocations() {
        locationDatabaseRepository.modelGetAllLocations();
    }

    private void insertLocation(ItemLocation itemLocation) {
        locationDatabaseRepository.modelInsertLocation(itemLocation);
    }

    private void importRealmFile(String filePath) {
        locationDatabaseRepository.modelRestore(filePath);
    }

    private void backupRealmFile() {
        locationDatabaseRepository.modelBackup();
    }
}
