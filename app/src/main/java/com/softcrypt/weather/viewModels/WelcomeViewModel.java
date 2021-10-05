package com.softcrypt.weather.viewModels;

import android.net.wifi.WifiConfiguration;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.label305.asynctask.SimpleAsyncTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.softcrypt.weather.R;
import com.softcrypt.weather.base.BaseApplication;
import com.softcrypt.weather.models.LocalCity;
import com.softcrypt.weather.repository.LocationDatabaseRepository;
import com.softcrypt.weather.views.WelcomeAct;
import com.softcrypt.weather.views.fragments.CitiesFrag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class WelcomeViewModel extends AndroidViewModel {

    private final LocationDatabaseRepository locationDatabaseRepository;
    private MutableLiveData<List<String>> result = new MutableLiveData<>();
    private MutableLiveData<String> progressResult = new MutableLiveData<>();

    public WelcomeViewModel(@NonNull BaseApplication application, Realm realm) {
        super(application);
        this.locationDatabaseRepository = new LocationDatabaseRepository(application, realm);
    }

    public LiveData<List<String>> getDoneResult() throws IOException {
        new LoadCities().execute();
        return result;
    }

    private class LoadCities extends SimpleAsyncTask<List<String>> {
        @Override
        protected List<String> doInBackground() {
            List<String> lstCities = new ArrayList<>();
            try {
                StringBuilder builder = new StringBuilder();
                InputStream is = getApplication().getResources().openRawResource(R.raw.city_list);
                GZIPInputStream gzipInputStream = new GZIPInputStream(is);

                InputStreamReader reader = new InputStreamReader(gzipInputStream);
                BufferedReader in = new BufferedReader(reader);

                String read;
                while ((read = in.readLine()) != null)
                    builder.append(read);
                lstCities = new Gson().fromJson(builder.toString(),
                        new TypeToken<List<String>>(){}.getType());
            }catch (IOException e){
                e.printStackTrace();
            }

            return lstCities;
        }

        @Override
        protected void onSuccess(final List<String> listCity) {
            super.onSuccess(listCity);
            result.setValue(listCity);
        }
    }
}
