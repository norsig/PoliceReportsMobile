package com.keniobyte.bruino.minsegapp.features.section_police_stations;

import com.keniobyte.bruino.minsegapp.models.PoliceStation;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * @author bruino
 * @version 16/01/17.
 */

public interface IPoliceStationsInteractor {
    List<PoliceStation> getPoliceStations() throws IOException, JSONException;
}
