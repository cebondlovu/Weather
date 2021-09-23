package com.softcrypt.weather.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.folderselector.FileChooserDialog;
import com.softcrypt.weather.base.BaseApplication;
import com.softcrypt.weather.common.MarshMellowPermission;
import com.softcrypt.weather.database.LocationsDatabaseHelper;
import com.softcrypt.weather.models.ItemLocation;
import com.softcrypt.weather.R;
import com.softcrypt.weather.viewModels.ViewPopupViewModel;

import java.io.File;
import java.util.UUID;

import io.realm.Realm;

public class ViewPopup extends AppCompatActivity implements FileChooserDialog.FileCallback {

    public static final String $CALL_TYPE = "CALL_TYPE";

    public static final String LA_T = "LAT";
    public static final String LN_G = "LNG";
    public static final String NAME = "NAME";
    public static final String UNIQUE_ID = "UNIQUE_ID";

    private ViewPopupViewModel viewPopupViewModel;
    private MarshMellowPermission permission;


    @Override
    protected void onStart() {
        super.onStart();
        viewPopupViewModel = new ViewPopupViewModel((BaseApplication) this.getApplication(),
                Realm.getDefaultInstance());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseApplication) getApplication()).getAppComponent().injectViewPopup(this);
        permission = new MarshMellowPermission(this);
        viewPopupViewModel = new ViewPopupViewModel((BaseApplication) this.getApplication(),
                Realm.getDefaultInstance());

        final Intent intent = getIntent();

        switch(intent.getStringExtra($CALL_TYPE)) {
            case "CONFIRM":
                confirmPopup(intent.getStringExtra(LN_G), intent.getStringExtra(LA_T));
                break;
            case "DELETE":
                deleteItem(intent.getStringExtra(UNIQUE_ID), intent.getStringExtra(NAME));
                break;
            case "BACKUP":
                backupRealmFile();
                break;
            case "IMPORT":
                importRealmFile();
        }
    }

    public void confirmPopup(final String lon, final String lat) {
        Button save, cancel;
        final TextView txt_error,txt_latitude,txt_longitude;
        final EditText edt_name;
        ImageView icon;

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.fragment_locations);

        icon = dialog.findViewById(R.id.imageView7);
        txt_error = dialog.findViewById(R.id.txt_error_dg);
        txt_latitude = dialog.findViewById(R.id.txt_lat_dg);
        txt_latitude.setText(lat);
        txt_longitude = dialog.findViewById(R.id.txt_lon_dg);
        txt_longitude.setText(lon);
        edt_name = dialog.findViewById(R.id.edt_name_dg);
        save = dialog.findViewById(R.id.btn_save_dg);

        save.setOnClickListener(view ->
                viewPopupViewModel.getAllLocationsMutableLivData().observe(this, itemLocations -> {
                    if (itemLocations.size() != 0) {
                        for (int i = 0; i < itemLocations.size(); i++) {
                            String compared = itemLocations.get(i).getName();
                            if (compared.equalsIgnoreCase(edt_name.getText().toString())) {
                                txt_error.setVisibility(View.VISIBLE);
                                return;
                            }
                        }
                    } else {
                        viewPopupViewModel.insertLocationMutableLivData(new ItemLocation(
                                1,
                                UUID.randomUUID().toString(),
                                edt_name.getText().toString(),
                                lon,
                                lat
                        ));
                        dialog.hide();
                        Toast.makeText(this, "Stored Successfully", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }));

        cancel = dialog.findViewById(R.id.btn_cancel_dg);
        cancel.setOnClickListener(view -> dialog.hide());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void deleteItem(final String uniqueID,final String name){
        Button yes, no;
        ImageView icon;
        TextView message;

        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.decide_popup);
        icon = dialog.findViewById(R.id.imageView3);
        message = dialog.findViewById(R.id.txt_msg_dg);
        message.setText(new StringBuilder("Do You Wish To Remove ").append(name).append(" From Locations?"));
        no = dialog.findViewById(R.id.btn_no_dg);
        yes = dialog.findViewById(R.id.btn_yes_dg);

        yes.setOnClickListener(view -> {
            viewPopupViewModel.deleteLocationMutableLivData(uniqueID);
            dialog.hide();
            Toast.makeText(this, "Removed " + name, Toast.LENGTH_LONG).show();
            finish();
        });

        no.setOnClickListener(view -> dialog.hide());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void importRealmFile() {
        if (!permission.checkPermissionForExternalStorage())
            permission.requestPermissionForExternalStorage();
        if (permission.checkPermissionForExternalStorage())
            new FileChooserDialog.Builder(ViewPopup.this)
                    .initialPath("/sdcard/Download")
                    .extensionsFilter(".realm")
                    .tag("Select Data File")
                    .goUpLabel("Up")
                    .show();
    }

    public void backupRealmFile() {
        if (!permission.checkPermissionForExternalStorage())
            permission.requestPermissionForExternalStorage();
        if (permission.checkPermissionForExternalStorage()) {
            viewPopupViewModel.backupRealFileMutableLivData();
            MaterialDialog dialog = new MaterialDialog.Builder(ViewPopup.this)
                    .title("Done")
                    .content("backup-Up to Downloads Folder")
                    .positiveText("Ok").onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    }).show();
        }

    }

    @Override
    public void onFileSelection(@NonNull FileChooserDialog dialog, @NonNull File file) {
        viewPopupViewModel.importRealFileMutableLivData(file.getPath());
        finish();
    }
}
