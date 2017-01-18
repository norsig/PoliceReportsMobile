package com.keniobyte.bruino.minsegapp.features.section_police_stations;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.kml.KmlContainer;
import com.google.maps.android.kml.KmlPlacemark;
import com.google.maps.android.kml.KmlPolygon;
import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.model.PoliceStation;
import com.keniobyte.bruino.minsegapp.ui.base.BasePresenter;
import com.keniobyte.bruino.minsegapp.utils.Polygon.Point;
import com.keniobyte.bruino.minsegapp.utils.Polygon.Polygon;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

/**
 * @author bruino
 * @version 18/01/17.
 */

public class PoliceStationsPresenter extends BasePresenter<PoliceStationsActivity> implements IPoliceStationsPresenter {
    private IPoliceStationsView policeStationsView;
    private IPoliceStationsInteractor policeStationsInteractor;
    private Context context;

    private List<PoliceStation> policeStations;

    public PoliceStationsPresenter(Context context, IPoliceStationsInteractor policeStationsInteractor) {
        this.context = context;
        this.policeStationsInteractor = policeStationsInteractor;
    }

    public void addView(IPoliceStationsView policeStationsView){
        this.policeStationsView = policeStationsView;
    }

    @Override
    public void showPoliceStation() throws JSONException, IOException {
        policeStations = policeStationsInteractor.getPoliceStations();
        ArrayList<MarkerOptions> markerOptionsArrayList = new ArrayList<>();

        for (int i = 0; i < policeStations.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(policeStations.get(i).getLatitude(), policeStations.get(i).getLongitude()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title(policeStations.get(i).getName())
                    .snippet(context.getResources().getString(R.string.jurisdiction) + " " + policeStations.get(i).getId());
            markerOptionsArrayList.add(markerOptions);
        }
        policeStationsView.setMarkersPoliceStations(policeStationsView.showPoliceStations(markerOptionsArrayList));
    }

    @Override
    public void onClickMyPoliceStations() {
        if (SmartLocation.with(context).location().state().isGpsAvailable() || SmartLocation.with(context).location().state().isAnyProviderAvailable()){
            SmartLocation.with(context).location().oneFix().start(new OnLocationUpdatedListener() {
                @Override
                public void onLocationUpdated(Location location) {
                    int jurisdiction = containsJurisdiction(location);

                    if (jurisdiction != 0){
                        try {
                            String police_station_name = getPoliceStation(jurisdiction);

                            if (!showInfoMarker(police_station_name) || police_station_name == null){
                                policeStationsView.notFoundRangeMessage();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            policeStationsView.warningGpsMessage();
        }
    }

    @Override
    public void onClickListPoliceStations() {
        policeStationsView.navigationToListPoliceStations();
    }

    private int containsJurisdiction(Location location){
        int nameJurisdiction = 0;

        for (KmlContainer container : policeStationsView.getJurisdictionKmlLayer().getContainers()){

            for (KmlPlacemark placemark : container.getPlacemarks()){
                final Polygon.Builder polygon = Polygon.Builder();

                for (LatLng latLng : ((KmlPolygon) placemark.getGeometry()).getOuterBoundaryCoordinates()){
                    Point point = new Point((float) latLng.latitude, (float) latLng.longitude);
                    polygon.addVertex(point);
                }
                Point pointLocation = new Point((float) location.getLatitude(), (float) location.getLongitude());

                if (polygon.build().contains(pointLocation)){
                    nameJurisdiction = Integer.valueOf(placemark.getProperty("name").replaceAll("[^0-9]+", ""));
                    break;
                }
            }
        }
        return nameJurisdiction;
    }

    private String getPoliceStation(int num_jurisdiction) throws JSONException {
        String station_name = null;

        for (int i = 0; i < policeStations.size(); i++) {

            if (policeStations.get(i).getJurisdiction() == num_jurisdiction) {
                station_name = policeStations.get(i).getName();
            }
        }
        return station_name;
    }

    private Boolean showInfoMarker(String police_station_name) {
        Boolean isShowMarker = false;

        for (Marker marker : policeStationsView.getMarkers()) {

            if (marker.getTitle().equals(police_station_name)) {
                marker.showInfoWindow();
                policeStationsView.setMyLocation(marker);
                isShowMarker = true;
                break;
            }
        }
        return isShowMarker;
    }
}
