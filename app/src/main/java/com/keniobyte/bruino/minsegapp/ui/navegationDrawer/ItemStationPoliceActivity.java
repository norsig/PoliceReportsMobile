package com.keniobyte.bruino.minsegapp.ui.navegationDrawer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.kml.KmlContainer;
import com.google.maps.android.kml.KmlLayer;
import com.google.maps.android.kml.KmlPlacemark;
import com.google.maps.android.kml.KmlPolygon;
import com.keniobyte.bruino.minsegapp.utils.Polygon.Point;
import com.keniobyte.bruino.minsegapp.utils.Polygon.Polygon;
import com.keniobyte.bruino.minsegapp.ui.ListViewStationPoliceActivity;
import com.keniobyte.bruino.minsegapp.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

/**
 * Activity Police Station
 */
public class ItemStationPoliceActivity extends ActionBarActivity implements OnMapReadyCallback {

    private final String TAG = getClass().getSimpleName();

    private Context mContext = this;
    private SharedPreferences preferences = null;
    private static int REQUEST_CODE_PERMISSION_LOCATION = 9;
    private String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    //Lat and lng corresponds to San Miguel de Tucumán, Tucumán, Argentina.
    private JSONObject listPoliceStation = null;
    private LatLng myLocation = new LatLng(-26.8167, -65.2167);
    private ArrayList<Marker> markersPoliceStation = new ArrayList<>();
    private GoogleMap mMap;
    private KmlLayer kmlLayerJurisdiction;

    public ItemStationPoliceActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_police_station);
        setToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;
        setUpMap();

        Button buttonListPoliceStation = (Button) findViewById(R.id.btnListStation);
        buttonListPoliceStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ListViewStationPoliceActivity.class);
                startActivity(intent);
            }
        });

        Button buttonMyPoliceStation = (Button) findViewById(R.id.btnMyStationPolice);
        buttonMyPoliceStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                        && PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    AlertDialog alertDialogPermission = new AlertDialog.Builder(mContext).create();
                    alertDialogPermission.setTitle(getResources().getString(R.string.title_attention));
                    alertDialogPermission.setMessage(getResources().getString(R.string.text_permissions_location));
                    alertDialogPermission.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.accept), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions((Activity) mContext, permissions, REQUEST_CODE_PERMISSION_LOCATION);
                        }
                    });
                    alertDialogPermission.show();
                } else {
                    setMyLocation();
                }
            }
        });

        preferences = getSharedPreferences("minsegapp", Context.MODE_PRIVATE);

        // if there's no police stations, "my police station" gets disabled.
        if (preferences.getString("police_station", "").isEmpty()) {
            buttonMyPoliceStation.setEnabled(false);
            Toast.makeText(mContext, getResources().getString(R.string.not_internet_kml)
                    , Toast.LENGTH_LONG).show();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpMap() {
        if (mMap == null) {
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapStationPolice);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        }
    }

    private void setMyLocation() {
        if (SmartLocation.with(mContext).location().state().isGpsAvailable()) {
            SmartLocation.with(mContext).location().oneFix().start(new OnLocationUpdatedListener() {
                @Override
                public void onLocationUpdated(Location location) {
                    myStationPolice(location);
                }
            });
        } else {
            AlertDialog alertDialogGps = new AlertDialog.Builder(mContext).create();
            alertDialogGps.setCancelable(false);
            alertDialogGps.setTitle(getResources().getString(R.string.title_attention));
            alertDialogGps.setMessage(getResources().getString(R.string.text_gps));
            alertDialogGps.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.actived), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            alertDialogGps.setButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(mContext, getResources().getString(R.string.not_permission_location)
                            , Toast.LENGTH_LONG).show();
                }
            });
            alertDialogGps.show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 11));
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        /* Police station and jurisdictions corresponds to the following JSON structure:
        {
            "jurisdiction":[
            {
                "id":"1",
                "stations":[
                    {"id":"1", "name":"Comisaría 1", "lat":"-26.8305333", "long":"-65.2003491"}
                 ]
            }, ...
            ]
        }
        */
        try {

            if (listPoliceStation == null) {
                listPoliceStation = new JSONObject(preferences.getString("police_station", ""));
            }

            if (!listPoliceStation.toString().isEmpty()) {

                for (int i = 0; i < listPoliceStation.getJSONArray("police_stations").length(); i++) {
                    markersPoliceStation.add(mMap.addMarker(
                            new MarkerOptions()
                                    .position(new LatLng(listPoliceStation.getJSONArray("police_stations").getJSONObject(i).getDouble("lat"),
                                            listPoliceStation.getJSONArray("police_stations").getJSONObject(i).getDouble("lng")))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                    .title(listPoliceStation.getJSONArray("police_stations").getJSONObject(i).getString("name"))
                                    .snippet(getResources().getString(R.string.jurisdiction) + " " + listPoliceStation.getJSONArray("police_stations").getJSONObject(i).getString("jurisdiction_id"))));
                }

            } else {
                Log.i(TAG, "Empty information Station Police");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            setKmlLayer();
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    private void setKmlLayer() throws IOException, XmlPullParserException {
        kmlLayerJurisdiction = new KmlLayer(mMap, R.raw.map_v2, getApplicationContext());
        kmlLayerJurisdiction.addLayerToMap();
    }

    public GoogleMap getmMap() {
        return mMap;
    }

    //Gets corresponding police station from current location and jurisdiction from KML polygons
    public void myStationPolice(Location location) {
        String jurisdiction_number = "";
        String station_name = null;

        boolean flag = false;

        for (KmlContainer container : kmlLayerJurisdiction.getContainers()) {
            KmlPlacemark placemark;

            for (KmlPlacemark kmlPlacemark : container.getPlacemarks()) {
                placemark = kmlPlacemark;
                KmlPolygon kmlPolygon = (KmlPolygon) placemark.getGeometry();
                final Polygon.Builder polygon = Polygon.Builder();

                for (LatLng latLng : kmlPolygon.getOuterBoundaryCoordinates()) {
                    float d = (float) latLng.latitude;
                    float e = (float) latLng.longitude;
                    Point pointPolygon = new Point(d, e);
                    polygon.addVertex(pointPolygon);
                }

                Polygon polygon1 = polygon.build();

                Point point = new Point((float) location.getLatitude(), (float) location.getLongitude());
                if (polygon1.contains(point)) {
                    flag = true;
                    jurisdiction_number = placemark.getProperty("name").replaceAll("[^0-9]+", "");
                    break;
                }
            }
        }

        if (flag) {

            try {
                if (listPoliceStation == null) {
                    listPoliceStation = new JSONObject(preferences.getString("police_station", ""));
                }

                if (!listPoliceStation.toString().isEmpty() && !jurisdiction_number.isEmpty()) {

                    for (int i = 0; i < listPoliceStation.getJSONArray("police_stations").length(); i++) {

                        if (listPoliceStation.getJSONArray("police_stations").getJSONObject(i).getInt("jurisdiction_id")
                                == Integer.valueOf(jurisdiction_number)) {
                            station_name = listPoliceStation.getJSONArray("police_stations").getJSONObject(i).getString("name");
                        }
                    }

                } else {
                    Log.i(TAG, "Json Police Stations Empty");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for (Marker marker : markersPoliceStation) {
                if (marker.getTitle().equals(station_name)) {
                    marker.showInfoWindow();

                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    getmMap().setMyLocationEnabled(true);
                    getmMap().moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 14));
                }
            }

        } else {
            Toast.makeText(mContext, getResources().getString(R.string.not_found_range), Toast.
                    LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 9: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Request Code: " + requestCode + " - GrantResults: " + Arrays.toString(grantResults));
                }
            }
        }
    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle(R.string.section_police_report);
        setSupportActionBar(toolbar);
    }
}
