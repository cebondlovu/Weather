package com.softcrypt.weather.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.softcrypt.weather.base.BaseApplication;
import com.softcrypt.weather.database.LocationsDatabaseHelper;
import com.softcrypt.weather.models.ItemLocation;
import com.softcrypt.weather.R;

import java.util.UUID;

public class ViewPopup {
    private LocationsDatabaseHelper dbHelper;

    public void confirmPopup(final String lon, final String lat, final BaseApplication context) {
        Button save;
        Button cancel;
        final TextView txt_error,txt_latitude,txt_longitude;
        final EditText edt_name;
        ImageView icon;

        final Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.fragment_locations);

        icon = dialog.findViewById(R.id.imageView7);
        txt_error = dialog.findViewById(R.id.txt_error_dg);
        txt_latitude = dialog.findViewById(R.id.txt_lat_dg);
        txt_latitude.setText(lat);
        txt_longitude = dialog.findViewById(R.id.txt_lon_dg);
        txt_longitude.setText(lon);
        edt_name = dialog.findViewById(R.id.edt_name_dg);
        save = dialog.findViewById(R.id.btn_save_dg);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper = new LocationsDatabaseHelper(context);

                if (dbHelper.getAllUserLocations().size() != 0) {
                    for (int i = 0; i < dbHelper.getAllUserLocations().size(); i++) {
                        String compared = dbHelper.getAllUserLocations().get(i).getName();

                        if(compared.toLowerCase().equals(edt_name.getText().toString().toLowerCase())) {
                            txt_error.setVisibility(View.VISIBLE);
                            return;
                        }
                    }
                }

                dbHelper.insertNewPlace(new ItemLocation(
                        UUID.randomUUID().toString(),
                        edt_name.getText().toString(),
                        lon,
                        lat
                ));
                dialog.hide();
                Toast.makeText(context,"Stored Successfully",Toast.LENGTH_LONG).show();

            }
        });

        cancel = dialog.findViewById(R.id.btn_cancel_dg);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void deleteItem(final String uniqueID,final String name, final  BaseApplication context){
        Button yes;
        Button no;
        ImageView icon;
        TextView message;

        final Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.decide_popup);
        icon = dialog.findViewById(R.id.imageView3);
        message = dialog.findViewById(R.id.txt_msg_dg);
        message.setText(new StringBuilder("Do You Wish To Remove ").append(name).append(" From Locations?"));
        no = dialog.findViewById(R.id.btn_no_dg);
        yes = dialog.findViewById(R.id.btn_yes_dg);
        yes.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                dbHelper = new LocationsDatabaseHelper(context);
                dbHelper.deletePlace(uniqueID);
                dialog.hide();
                Toast.makeText(context,"Removed "+ name, Toast.LENGTH_LONG).show();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
