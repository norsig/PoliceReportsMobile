package com.keniobyte.bruino.minsegapp.features.location_police_report;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

/**
 * @author bruino
 * @version 02/01/17.
 */

public interface ILocationPoliceReportView {
    ArrayAdapter createArrayAdapter();

    void setEnableNextStepButton(boolean i);
    void addMarkerInGoogleMap(LatLng latLng);
    void hintToTextAutoCompleteTextView();
    void showProgressBar();
    void hideProgressBar();
    void setTextAutoCompleteTextView(String string);
    void setHintAutoCompleteTextView(String string);
    void setResultAddressList(ArrayList resultAddressList);
    void setLocationPoliceReport(LatLng location);
    void setAdapterAutoCompleteTextView(ArrayAdapter adapter);
    void navigationToPoliceReportActivity();
    void geocodingReverseMessageError();
}
