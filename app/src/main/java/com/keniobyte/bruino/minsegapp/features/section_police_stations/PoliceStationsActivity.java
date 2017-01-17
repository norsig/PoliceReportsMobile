package com.keniobyte.bruino.minsegapp.features.section_police_stations;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.kml.KmlLayer;
import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.features.section_police_stations.listPoliceStations.PoliceStationsInfoActivity;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(PoliceStationsPresenter.class)
public class PoliceStationsActivity extends AppCompatActivity implements IPoliceStationsView, OnMapReadyCallback {
    private final String TAG = getClass().getSimpleName();
    private final double LAT = -26.8167;
    private final double LNG = -65.2167;

    @BindView(R.id.myToolbar) Toolbar toolbar;
    @BindView(R.id.myPoliceStationButton) Button myPoliceStationButton;
    @BindView(R.id.listPoliceStationsButton) Button listPoliceStationsButton;

    @BindString(R.string.section_police_stations) String title;
    @BindString(R.string.accept) String ok;
    @BindString(R.string.title_attention) String titleWarning;
    @BindString(R.string.text_permissions_location) String messagePermissionLocation;
    @BindString(R.string.text_gps) String messageGps;
    @BindString(R.string.not_permission_location) String messageNotPermissionLocation;
    @BindString(R.string.not_found_range) String messageNotFoundRange;

    @BindDrawable(R.drawable.ic_arrow_back_white_24dp) Drawable arrowBack;

    private Context context = this;
    private PoliceStationsPresenter presenter;
    private PoliceStationsInteractor interactor;

    private String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private GoogleMap googleMap;
    private KmlLayer jurisdictionKmlLayer;
    private ArrayList<Marker> markersPoliceStation = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_police_station);
        ButterKnife.bind(this);

        toolbar.setTitle(title);
        toolbar.setNavigationIcon(arrowBack);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (googleMap == null) {
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.policeStationsMap);
            if (mapFragment != null){
                mapFragment.getMapAsync(this);
            }
        }

        interactor = new PoliceStationsInteractor(context);
        presenter = new PoliceStationsPresenter(context, interactor);
        presenter.addView(this);
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
    public void onMapReady(GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LAT, LNG), 11));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap = googleMap;
        try {
            jurisdictionKmlLayer = new KmlLayer(googleMap, R.raw.map_v2, context);
            jurisdictionKmlLayer.addLayerToMap();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        try {
            presenter.showPoliceStation();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public KmlLayer getJurisdictionKmlLayer() {
        return jurisdictionKmlLayer;
    }

    @Override
    public ArrayList<Marker> showPoliceStations(ArrayList<MarkerOptions> markerOptionsArrayList) {
        ArrayList<Marker> markers = new ArrayList<>();
        for (MarkerOptions markerOptions : markerOptionsArrayList) {
            Marker marker = googleMap.addMarker(markerOptions);
            if (marker != null){
                markers.add(marker);
            }
        }
        return markers;
    }

    @Override
    public ArrayList<Marker> getMarkers() {
        return markersPoliceStation;
    }

    @Override
    public void setMarkersPoliceStations(ArrayList<Marker> markers) {
        markersPoliceStation = markers;
    }

    @Override
    public void warningGpsMessage() {
        new AlertDialog.Builder(context)
                .setTitle(titleWarning)
                .setMessage(messageGps)
                .setCancelable(false)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .show();
    }

    @Override
    public void notFoundRangeMessage() {
        new AlertDialog.Builder(context)
                .setTitle(titleWarning)
                .setMessage(messageNotFoundRange)
                .setCancelable(false)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }

    @Override
    public void notPermissionLocationMessage() {
        new AlertDialog.Builder(context)
                .setTitle(titleWarning)
                .setMessage(messageNotPermissionLocation)
                .setCancelable(false)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }

    @Override
    public void permissionLocationMessage() {
        new AlertDialog.Builder(context)
                .setTitle(titleWarning)
                .setMessage(messagePermissionLocation)
                .setCancelable(false)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions((Activity) context, permissions, 4);
                    }
                })
                .show();
    }

    @Override
    public void setMyLocation(Marker marker) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 14));
    }

    @Override
    public void navigationToListPoliceStations() {
        startActivity(new Intent(context, PoliceStationsInfoActivity.class));
    }

    @OnClick({R.id.myPoliceStationButton, R.id.listPoliceStationsButton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.myPoliceStationButton:
                if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        && PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    permissionLocationMessage();
                } else {
                    presenter.onClickMyPoliceStations();
                }
                break;
            case R.id.listPoliceStationsButton:
                presenter.onClickListPoliceStations();
                break;
        }
    }
}
