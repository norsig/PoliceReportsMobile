package com.keniobyte.bruino.minsegapp.features.section_police_stations;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.kml.KmlLayer;

import java.util.ArrayList;

/**
 * @author bruino
 * @version 17/01/17.
 */

public interface IPoliceStationsView {
    KmlLayer getJurisdictionKmlLayer();
    ArrayList<Marker> showPoliceStations(ArrayList<MarkerOptions> markerOptionsArrayList);
    ArrayList<Marker> getMarkers();

    void setMarkersPoliceStations(ArrayList<Marker> markers);
    void warningGpsMessage();
    void notFoundRangeMessage();
    void notPermissionLocationMessage();
    void permissionLocationMessage();
    void setMyLocation(Marker marker);
    void navigationToListPoliceStations();
}
