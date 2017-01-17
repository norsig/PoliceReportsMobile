package com.keniobyte.bruino.minsegapp.features.section_police_stations.listPoliceStations;

import com.keniobyte.bruino.minsegapp.model.PoliceStation;

import org.json.JSONException;

import java.io.IOException;

/**
 * @author bruino
 * @version 17/01/17.
 */

public interface IPoliceStationsInfoPresenter {
    void setPoliceStationsInList() throws IOException, JSONException;
    void callPoliceStation(PoliceStation policeStation);
}
