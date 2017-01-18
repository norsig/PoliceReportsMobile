package com.keniobyte.bruino.minsegapp.features.location_police_report;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.AdapterView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;
import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.network.MinSegAppSingleton;
import com.keniobyte.bruino.minsegapp.views.base.BasePresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import io.nlopez.smartlocation.OnGeocodingListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.geocoding.utils.LocationAddress;

/**
 * @author bruino
 * @version 02/01/17.
 */

public class LocationPoliceReportPresenter extends BasePresenter<LocationPoliceReportActivity> implements ILocationPoliceReportPresenter {
    private ILocationPoliceReportView locationPoliceReportView;
    private Context context;

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private String keyGoogleMapsAddress;
    private String location;

    public LocationPoliceReportPresenter(Context context) {
        this.context = context;
        this.keyGoogleMapsAddress = context.getResources().getString(R.string.API_KEY_MAPS_AUTOCOMPLETE);
        this.location = context.getResources().getString(R.string.LOCATION);
    }

    public void addView(ILocationPoliceReportView locationPoliceReportView){
        this.locationPoliceReportView = locationPoliceReportView;
    }

    @Override
    public void resultAddressList(String input) throws UnsupportedEncodingException {
        locationPoliceReportView.showProgressBar();
        String url = PLACES_API_BASE + TYPE_AUTOCOMPLETE
                + OUT_JSON
                + "?key=" + keyGoogleMapsAddress
                + "&location=" + location
                + "&radius=200&types=address&input="
                + URLEncoder.encode(input, "utf8");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET
                , url
                , null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    publishResult(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(locationPoliceReportView.getClass().getSimpleName(), error.toString());
                locationPoliceReportView.geocodingReverseMessageError();
            }
        });
        MinSegAppSingleton.getInstance(context).addToRequestQueue(request);
    }

    private void publishResult(JSONObject response) throws JSONException {
        JSONArray predsJsonArray = response.getJSONArray("predictions");
        ArrayList resultList = new ArrayList(predsJsonArray.length());
        for (int i = 0; i < predsJsonArray.length(); i++) {
            resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
        }
        locationPoliceReportView.setEnableNextStepButton(false);
        locationPoliceReportView.setResultAddressList(resultList);
        locationPoliceReportView.hideProgressBar();
        locationPoliceReportView.setAdapterAutoCompleteTextView(locationPoliceReportView.createArrayAdapter());
    }

    @Override
    public void onItemSelectedAddressAutoCompleteTextView(AdapterView<?> parent, int position) {
        String address = (String) parent.getItemAtPosition(position);
        locationPoliceReportView.setTextAutoCompleteTextView(address);
        SmartLocation.with(context).geocoding().direct(address, new OnGeocodingListener() {
            @Override
            public void onLocationResolved(String s, List<LocationAddress> list) {
                if (list.size() > 0){
                    LatLng location = new LatLng(list.get(0).getAddress().getLatitude()
                            , list.get(0).getAddress().getLongitude());
                    locationPoliceReportView.addMarkerInGoogleMap(location);
                    locationPoliceReportView.setEnableNextStepButton(true);
                    locationPoliceReportView.setLocationPoliceReport(location);
                }
            }
        });
    }

    @Override
    public void nextStep() {
        locationPoliceReportView.navigationToPoliceReportActivity();
    }

    @Override
    public void onMapClick(final LatLng latLng) {
        locationPoliceReportView.addMarkerInGoogleMap(latLng);
        locationPoliceReportView.setEnableNextStepButton(false);
        locationPoliceReportView.showProgressBar();
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        SmartLocation.with(context).geocoding().reverse(location
                , new OnReverseGeocodingListener() {
                    @Override
                    public void onAddressResolved(Location location, List<Address> list) {
                        if (list.size() > 0){
                            String address = list.get(0).getAddressLine(0);
                            locationPoliceReportView.hideProgressBar();
                            locationPoliceReportView.setTextAutoCompleteTextView("");
                            locationPoliceReportView.setHintAutoCompleteTextView(address);
                            locationPoliceReportView.setLocationPoliceReport(latLng);
                            locationPoliceReportView.setEnableNextStepButton(true);
                        } else {
                            locationPoliceReportView.geocodingReverseMessageError();
                            locationPoliceReportView.hideProgressBar();
                        }
                    }
                });
    }

    @Override
    public void unregisterReceiver() {
        SmartLocation.with(context).geocoding().stop();
    }

    @Override
    public void onClickInAutoCompleteTextView() {
        locationPoliceReportView.hintToTextAutoCompleteTextView();
    }
}
