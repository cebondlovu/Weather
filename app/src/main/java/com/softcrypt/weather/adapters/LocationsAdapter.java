package com.softcrypt.weather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softcrypt.weather.common.LocationsDatabaseHelper;
import com.softcrypt.weather.models.ItemLocation;
import com.softcrypt.weather.R;
import com.softcrypt.weather.utils.ViewPopup;

import java.util.ArrayList;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.MyViewHolder> {

    Context context;
    ArrayList<ItemLocation> list;
    LocationsDatabaseHelper dbHelper;
    ViewPopup popup = new ViewPopup();

    public LocationsAdapter(Context context, ArrayList<ItemLocation> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_locations,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.txt_name.setText(new StringBuilder(list.get(position).getName()));
        holder.txt_lat.setText(new StringBuilder("[ ").append(list.get(position).getLat()).append(" ]").toString());
        holder.txt_lon.setText(new StringBuilder("[ ").append(list.get(position).getLng()).append(" ]").toString());
        holder.del_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.deleteItem(list.get(position).getUniqueID(),list.get(position).getName(),
                        view.getContext());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name,txt_lat,txt_lon;
        ImageView del_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_name = itemView.findViewById(R.id.txt_name);
            txt_lat = itemView.findViewById(R.id.txt_lat);
            txt_lon = itemView.findViewById(R.id.txt_lon);
            del_img = itemView.findViewById(R.id.del_img);
        }
    }
}
