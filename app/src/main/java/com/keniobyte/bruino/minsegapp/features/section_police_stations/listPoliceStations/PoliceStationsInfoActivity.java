package com.keniobyte.bruino.minsegapp.features.section_police_stations.listPoliceStations;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.features.section_police_stations.PoliceStationsInteractor;
import com.keniobyte.bruino.minsegapp.features.section_police_stations.listPoliceStations.adapter.PoliceStationAdapter;
import com.keniobyte.bruino.minsegapp.model.PoliceStation;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(PoliceStationInfoPresenter.class)
public class PoliceStationsInfoActivity extends AppCompatActivity implements IPoliceStationInfoView {

    @BindView(R.id.myToolbar) Toolbar toolbar;
    @BindView(R.id.policeStationsListView) ListView policeStationsListView;

    @BindString(R.string.list_station) String title;

    @BindDrawable(R.drawable.ic_arrow_back_white_24dp) Drawable arrowBack;

    private Context context = this;
    private PoliceStationInfoPresenter presenter;
    private PoliceStationsInteractor interactor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_police_station);
        ButterKnife.bind(this);

        toolbar.setTitle(title);
        toolbar.setNavigationIcon(arrowBack);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        interactor = new PoliceStationsInteractor(context);
        presenter = new PoliceStationInfoPresenter(interactor);
        presenter.addView(this);

        try {
            presenter.setPoliceStationsInList();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public PoliceStationAdapter createPoliceStationAdapter(List<PoliceStation> policeStations) {
        return new PoliceStationAdapter(context, policeStations);
    }

    @Override
    public void setPoliceStationAdapter(PoliceStationAdapter adapter) {
        policeStationsListView.setAdapter(adapter);
        policeStationsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        policeStationsListView.setItemChecked(9, true);
    }

    @Override
    public void callPhone(String number_phone) {
        if (PackageManager.PERMISSION_GRANTED !=  ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)) {

            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CALL_PHONE)) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 3);
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 3);
            }
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number_phone)));
        }
    }

    @Override
    public void onListCallPhoneInteraction(PoliceStation policeStation) {
        presenter.callPoliceStation(policeStation);
    }
}
