package com.keniobyte.bruino.minsegapp.ui.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.keniobyte.bruino.minsegapp.model.PoliceStation;
import com.keniobyte.bruino.minsegapp.R;

/**
 * Adapter Police Station.
 */
public class ItemStationPoliceAdapter extends ArrayAdapter<PoliceStation> {

    private int MY_PERMISSIONS_REQUEST_CALL_PHONE = 8;

    public ItemStationPoliceAdapter(Context context, int resource, PoliceStation[] objects) {
        super(context, R.layout.listitem_station, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View item = convertView;

        final ViewHolder holder;

        if (item == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            item = inflater.inflate(R.layout.listitem_station, null);

            holder = new ViewHolder();
            holder.name = (TextView)item.findViewById(R.id.tNameStation);
            holder.locality = (TextView)item.findViewById(R.id.tLocality);
            holder.address = (TextView)item.findViewById(R.id.tAddress);
            holder.phone = (TextView)item.findViewById(R.id.tPhone);
            holder.buttonPhone = (ImageButton)item.findViewById(R.id.buttonPhone);

            item.setTag(holder);
        } else {
            holder = (ViewHolder) item.getTag();
        }

        holder.name.setText(getItem(position).getName());
        holder.locality.setText(getItem(position).getCity());
        holder.address.setText(getItem(position).getAddress());
        holder.phone.setText(getItem(position).getPhone());
        holder.buttonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (PackageManager.PERMISSION_GRANTED !=  ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getContext(), Manifest.permission.CALL_PHONE)) {
                        ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);

                    } else {
                        ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    }

                } else {
                    getContext().startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + holder.phone.getText().toString())));
                }
            }
        });

        return(item);
    }

    static class ViewHolder{
        TextView name, locality, address, phone;
        ImageButton buttonPhone;
    }
}