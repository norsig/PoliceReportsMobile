package com.keniobyte.bruino.minsegapp.ui.navegationDrawer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.keniobyte.bruino.minsegapp.R;

/**
 * Home screen that shows ministry of security's logo and a quick access to 911 dial.
 */
public class ItemInitial extends Fragment {

    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 13;

    public ItemInitial() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_init, container, false);

        FloatingActionButton buttonPhoneEmergency = (FloatingActionButton) view.findViewById(R.id.buttonPhoneEmergency);
        buttonPhoneEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PackageManager.PERMISSION_GRANTED !=  ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getContext(), Manifest.permission.CALL_PHONE)) {
                        ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    } else {
                        ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    }

                } else {
                    getContext().startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "911")));
                }
            }
        });

        return view;
    }
}
