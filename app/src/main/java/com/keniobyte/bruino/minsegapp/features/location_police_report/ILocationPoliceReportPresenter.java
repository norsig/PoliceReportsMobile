package com.keniobyte.bruino.minsegapp.features.location_police_report;

import android.widget.AdapterView;

import com.google.android.gms.maps.model.LatLng;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * @author bruino
 * @version 02/01/17.
 */

public interface ILocationPoliceReportPresenter {
    void resultAddressList(String input) throws UnsupportedEncodingException;
    void onItemSelectedAddressAutoCompleteTextView(AdapterView<?> parent, int position);
    void nextStep();
    void onMapClick(LatLng latLng);

    void unregisterReceiver();
}
