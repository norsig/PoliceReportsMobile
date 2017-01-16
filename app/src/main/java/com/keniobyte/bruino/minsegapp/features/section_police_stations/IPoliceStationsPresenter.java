package com.keniobyte.bruino.minsegapp.features.section_police_stations;

import org.json.JSONException;

import java.io.IOException;

/**
 * @author bruino
 * @version 16/01/17.
 */

public interface IPoliceStationsPresenter {
    void showPoliceStation() throws JSONException, IOException;

    void onClickMyPoliceStations();
    void onClickListPoliceStations();
}
