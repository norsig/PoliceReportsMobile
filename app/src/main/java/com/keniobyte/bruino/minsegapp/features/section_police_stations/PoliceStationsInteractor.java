package com.keniobyte.bruino.minsegapp.features.section_police_stations;

import android.content.Context;
import com.keniobyte.bruino.minsegapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author bruino
 * @version 16/01/17.
 */

public class PoliceStationsInteractor implements IPoliceStationsInteractor {
    private Context context;

    public PoliceStationsInteractor(Context context) {
        this.context = context;
    }

    @Override
    public JSONObject getPoliceStations() throws IOException, JSONException {
        InputStream openRawResource = context.getResources().openRawResource(R.raw.police_stations_json);
        byte[] b = new byte[openRawResource.available()];
        openRawResource.read(b);
        String string = new String(b);
        return new JSONObject(string);
    }
}
