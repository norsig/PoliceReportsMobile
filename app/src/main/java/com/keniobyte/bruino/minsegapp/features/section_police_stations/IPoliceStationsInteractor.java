package com.keniobyte.bruino.minsegapp.features.section_police_stations;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @author bruino
 * @version 16/01/17.
 */

public interface IPoliceStationsInteractor {
    JSONObject getPoliceStations() throws IOException, JSONException;
}
