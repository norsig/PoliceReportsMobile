package com.keniobyte.bruino.minsegapp.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.ui.navegationDrawer.ItemPoliceReportActivity;
import com.keniobyte.bruino.minsegapp.ui.reportPolices.PoliceReportAircraftActivity;
import com.keniobyte.bruino.minsegapp.ui.reportPolices.PoliceReportActivity;
import com.keniobyte.bruino.minsegapp.ui.reportPolices.ReportPoliceAnonymusActivity;
import com.keniobyte.bruino.minsegapp.ui.reportPolices.PoliceReportInternalAffairActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import io.nlopez.smartlocation.OnGeocodingListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.geocoding.utils.LocationAddress;

public class PoliceReportMapActivity extends ActionBarActivity implements OnMapReadyCallback {

    private final String TAG = getClass().getSimpleName();
    private Context context = this;
    private Bundle bundle;

    private Boolean isRunning = false;
    private Button btnNextStep;

    private Double lat = -26.8167;
    private Double lng = -65.2167;

    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyBijObeslhFveev-UHt4AZ-UAcSO_ix59k";

    private GoogleMap mMap;
    private MapFragment mapFragment;
    private LatLng mLatLng = null;

    private AutoCompleteTextView mAutoCompleteLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_map_report);

        bundle = this.getIntent().getExtras();

        setToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAutoCompleteLocation = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextAddress);
        mAutoCompleteLocation.setAdapter(new GoogleAutocompleteAdapter(this, R.layout.list_item));
        mAutoCompleteLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String string = (String) parent.getItemAtPosition(position);
                mAutoCompleteLocation.setText("");
                mAutoCompleteLocation.setText(string);
                mMap.clear();
                SmartLocation.with(getApplicationContext()).geocoding().direct(string, new OnGeocodingListener() {
                    @Override
                    public void onLocationResolved(String s, List<LocationAddress> list) {
                        if (list.size() > 0) {
                            mLatLng = new LatLng(list.get(0).getAddress().getLatitude(), list.get(0).getAddress().getLongitude());
                            mMap.addMarker(new MarkerOptions().position(mLatLng));
                            btnNextStep.setEnabled(true);
                        }
                    }
                });
            }
        });

        btnNextStep = (Button) findViewById(R.id.btnNextStep);
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLatLng != null) {
                    Intent intent = null;
                    switch (bundle.getInt("report_type")) {
                        case ItemPoliceReportActivity.TYPE_ONLINE_REPORT:
                            intent = new Intent(getApplicationContext(), PoliceReportActivity.class);
                            break;
                        case ItemPoliceReportActivity.TYPE_INTERNAL_AFFAIRS_REPORT:
                            intent = new Intent(getApplicationContext(), PoliceReportInternalAffairActivity.class);
                            intent.putExtra("isLocation", true);
                            break;
                        case ItemPoliceReportActivity.TYPE_ANONYMOUS_REPORT:
                            intent = new Intent(getApplicationContext(), ReportPoliceAnonymusActivity.class);
                            break;
                        case ItemPoliceReportActivity.TYPE_AIRCRAFT_POLICE_REPORT:
                            intent = new Intent(getApplicationContext(), PoliceReportAircraftActivity.class);
                            break;
                    }
                    if (intent != null) {
                        intent.putExtra("latitude", mLatLng.latitude);
                        intent.putExtra("longitude", mLatLng.longitude);
                        intent.putExtra("address", mAutoCompleteLocation.getText().toString());
                        startActivity(intent);
                    }
                } else {
                    if (bundle.getInt("report_type") == PoliceReportActivity.TYPE_INTERNAL_AFFAIRS_REPORT) {
                        Intent intent = new Intent(getApplicationContext(), PoliceReportInternalAffairActivity.class);
                        intent.putExtra("isLocation", false);
                        startActivity(intent);
                    }
                }
            }
        });

        switch (bundle.getInt("report_type")) {
            case PoliceReportActivity.TYPE_ONLINE_REPORT:
                btnNextStep.setEnabled(true);
                break;
            case PoliceReportActivity.TYPE_INTERNAL_AFFAIRS_REPORT:
                btnNextStep.setEnabled(true);
                break;
            case PoliceReportActivity.TYPE_ANONYMUS_REPORT:
                btnNextStep.setEnabled(false);
                break;
            case ItemPoliceReportActivity.TYPE_AIRCRAFT_POLICE_REPORT:
                btnNextStep.setEnabled(false);
                break;
        }

        setUpMap();
    }

    private void setUpMap() {
        if (mMap == null) {
            mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        LatLng myLocation = new LatLng(lat, lng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 11));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(latLng));

                Location location = new Location(LocationManager.GPS_PROVIDER);
                location.setLatitude(latLng.latitude);
                location.setLongitude(latLng.longitude);

                SmartLocation.with(getApplicationContext()).geocoding().reverse(location, new OnReverseGeocodingListener() {
                    @Override
                    public void onAddressResolved(Location location, List<Address> list) {
                        if (list.size() > 0) {
                            mAutoCompleteLocation.setText(list.get(0).getAddressLine(0));
                            mLatLng = latLng;
                            btnNextStep.setEnabled(true);
                        } else {
                            if (isRunning) {
                                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                                alertDialog.setTitle(getResources().getString(R.string.title_attention));
                                alertDialog.setMessage(getResources().getString(R.string.text_reverse_geocode));
                                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.accept)
                                        , new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        });
                                alertDialog.show();
                            }
                        }
                    }
                });
            }
        });
    }

    public static ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&location=-26.8167,-65.2167&radius=100&types=address");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GoogleAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList resultList;

        public GoogleAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public Object getItem(int position) {
            return resultList.get(position);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        TextView description = (TextView) findViewById(R.id.desriptionLocationMap);
        switch (bundle.getInt("report_type")) {
            case ItemPoliceReportActivity.TYPE_ONLINE_REPORT:
                toolbar.setTitle(getResources().getString(R.string.section_police_report));
                description.setText(R.string.incident_location);
                break;
            case ItemPoliceReportActivity.TYPE_INTERNAL_AFFAIRS_REPORT:
                toolbar.setTitle(getResources().getString(R.string.section_internal_affairs));
                description.setText(R.string.incident_location);
                break;
            case ItemPoliceReportActivity.TYPE_ANONYMOUS_REPORT:
                toolbar.setTitle(getResources().getString(R.string.section_anonymousReport));
                description.setText(R.string.selected_map_drug);
                break;
            case ItemPoliceReportActivity.TYPE_AIRCRAFT_POLICE_REPORT:
                toolbar.setTitle(getResources().getString(R.string.section_short_aircraft));
                description.setText(R.string.selected_map_aircraft);
                break;
        }

        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunning = false;
    }
}