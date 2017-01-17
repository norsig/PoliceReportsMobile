package com.keniobyte.bruino.minsegapp.features.section_police_stations.listPoliceStations.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.features.section_police_stations.listPoliceStations.PoliceStationsInfoActivity;
import com.keniobyte.bruino.minsegapp.model.PoliceStation;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author bruino
 * @version 17/01/17.
 */

public class PoliceStationAdapter extends ArrayAdapter<PoliceStation> {

    public PoliceStationAdapter(Context context, List<PoliceStation> objects) {
        super(context, R.layout.item_police_station, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView != null){
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_police_station, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        if (getItem(position) != null){
            holder.name.setText(getItem(position).getName());
            holder.locality.setText(getItem(position).getCity());
            holder.address.setText(getItem(position).getAddress());
            holder.phone.setText(getItem(position).getPhone());
            holder.callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((PoliceStationsInfoActivity) getContext()).onListCallPhoneInteraction(getItem(position));
                }
            });
        }

        return convertView;
    }

    static class ViewHolder{
        @BindView(R.id.namePoliceStationTextView) TextView name;
        @BindView(R.id.localityPoliceStationTextView) TextView locality;
        @BindView(R.id.addressPoliceStationTextView) TextView address;
        @BindView(R.id.phonePoliceStationsTextView) TextView phone;
        @BindView(R.id.callPoliceStationButton) ImageButton callButton;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
