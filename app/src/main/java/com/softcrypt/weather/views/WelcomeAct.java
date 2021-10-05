package com.softcrypt.weather.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.softcrypt.weather.R;
import com.softcrypt.weather.base.BaseApplication;
import com.softcrypt.weather.common.Common;
import com.softcrypt.weather.viewModels.WelcomeViewModel;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.Realm;

public class WelcomeAct extends AppCompatActivity {

    private ImageView logo;
    private TextView title;
    public static TextView text;
    ProgressBar loading;
    private WelcomeViewModel welcomeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ((BaseApplication) getApplication()).getAppComponent().injectWelcomeAct(this);
        welcomeViewModel = new WelcomeViewModel((BaseApplication) this.getApplication(), Realm.getDefaultInstance());

        logo = findViewById(R.id.logo);
        title = findViewById(R.id.title);
        loading = findViewById(R.id.progress);
        text = findViewById(R.id.text);

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
}