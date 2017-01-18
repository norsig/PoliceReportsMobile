package com.keniobyte.bruino.minsegapp.features.section_police_stations;

import android.content.Context;
import android.util.Log;

import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.model.PoliceStation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bruino
 * @version 18/01/17.
 */

public class PoliceStationsInteractor implements IPoliceStationsInteractor {
    private Context context;

    public PoliceStationsInteractor(Context context) {
        this.context = context;
    }

    @Override
    public List<PoliceStation> getPoliceStations() throws IOException, JSONException {
        JSONObject policeStationJson = getPoliceStationsJson();
        List<PoliceStation> policeStations = new ArrayList<>();

        for (int i = 0; i < policeStationJson.getJSONArray("police_stations").length(); i++){
            policeStations.add(new PoliceStation(policeStationJson.getJSONArray("police_stations").getJSONObject(i).getInt("id")
                    , policeStationJson.getJSONArray("police_stations").getJSONObject(i).getString("name")
                    , policeStationJson.getJSONArray("police_stations").getJSONObject(i).getString("city")
                    , policeStationJson.getJSONArray("police_stations").getJSONObject(i).getString("address")
                    , policeStationJson.getJSONArray("police_stations").getJSONObject(i).getString("phone")
                    , (float) policeStationJson.getJSONArray("police_stations").getJSONObject(i).getDouble("lat")
                    , (float) policeStationJson.getJSONArray("police_stations").getJSONObject(i).getDouble("lng")
                    , policeStationJson.getJSONArray("police_stations").getJSONObject(i).getInt("jurisdiction_id")
            ));
        }
        return policeStations;
    }

    private JSONObject getPoliceStationsJson() throws IOException, JSONException {
        InputStream openRawResource = context.getResources().openRawResource(R.raw.police_stations_json);
        byte[] b = new byte[openRawResource.available()];
        openRawResource.read(b);
        String string = new String(b);
        return new JSONObject(string);
    }
}
