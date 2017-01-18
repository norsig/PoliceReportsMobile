package com.keniobyte.bruino.minsegapp.features.section_home;


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

import com.keniobyte.bruino.minsegapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(SectionHomePresenter.class)
public class SectionHomeFragment extends Fragment implements ISectionHomeView{

    @BindView(R.id.emergencyButton)
    FloatingActionButton emergencyButton;

    private SectionHomePresenter presenter;

    public SectionHomeFragment() {
        presenter = new SectionHomePresenter();
        presenter.addView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_init, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.emergencyButton)
    public void onClick() {
        presenter.callEmergency();
    }

    @Override
    public void callEmergency() {
        if (PackageManager.PERMISSION_GRANTED !=  ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)) {

            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getContext(), Manifest.permission.CALL_PHONE)) {
                ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.CALL_PHONE}, 6);
            } else {
                ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.CALL_PHONE}, 6);
            }
        } else {
            getContext().startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "911")));
        }
    }
}
