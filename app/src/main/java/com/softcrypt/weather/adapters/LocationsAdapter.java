package com.softcrypt.weather.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softcrypt.weather.models.ItemLocation;
import com.softcrypt.weather.R;
import com.softcrypt.weather.views.ViewPopup;

import java.util.ArrayList;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.MyViewHolder> {

    Context context;
    ArrayList<ItemLocation> list;

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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.txt_name.setText(new StringBuilder(list.get(position).getName()));
        holder.txt_lat.setText(new StringBuilder("[ ").append(list.get(position).getLat()).append(" ]").toString());
        holder.txt_lon.setText(new StringBuilder("[ ").append(list.get(position).getLng()).append(" ]").toString());
        holder.del_img.setImageResource(R.drawable.ic_baseline_delete_forever_24);
        holder.del_img.setOnClickListener(view -> {
            Intent intent = new Intent(context, ViewPopup.class);
            intent.putExtra(ViewPopup.$CALL_TYPE, "DELETE");
            intent.putExtra(ViewPopup.UNIQUE_ID, list.get(position).getUniqueId());
            intent.putExtra(ViewPopup.NAME, list.get(position).getName());
            context.startActivity(intent);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name,txt_lat,txt_lon;
        ImageView del_img, loc_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_name = itemView.findViewById(R.id.txt_name);
            txt_lat = itemView.findViewById(R.id.txt_lat);
            txt_lon = itemView.findViewById(R.id.txt_lon);
            del_img = itemView.findViewById(R.id.del_img);
            loc_img = itemView.findViewById(R.id.imageView);
        }
    }
}
