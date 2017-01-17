package com.keniobyte.bruino.minsegapp.features.section_police_stations.listPoliceStations;

import android.util.Log;

import com.keniobyte.bruino.minsegapp.features.section_police_stations.IPoliceStationsInteractor;
import com.keniobyte.bruino.minsegapp.model.PoliceStation;
import com.keniobyte.bruino.minsegapp.ui.base.BasePresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bruino
 * @version 17/01/17.
 */

public class PoliceStationInfoPresenter extends BasePresenter<PoliceStationsInfoActivity> implements IPoliceStationsInfoPresenter {

    private IPoliceStationInfoView policeStationInfoView;
    private IPoliceStationsInteractor policeStationsInteractor;

    public PoliceStationInfoPresenter(IPoliceStationsInteractor policeStationsInteractor) {
        this.policeStationsInteractor = policeStationsInteractor;
    }

    public void addView(IPoliceStationInfoView policeStationInfoView){
        this.policeStationInfoView = policeStationInfoView;
    }

    @Override
    public void setPoliceStationsInList() throws IOException, JSONException {
        JSONObject policeStationJson = policeStationsInteractor.getPoliceStations();
        Log.i(policeStationInfoView.getClass().getSimpleName(), policeStationJson.toString());
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

        policeStationInfoView.setPoliceStationAdapter(policeStationInfoView.createPoliceStationAdapter(policeStations));
    }

    @Override
    public void callPoliceStation(PoliceStation policeStation) {
        policeStationInfoView.callPhone(policeStation.getPhone());
    }
}
