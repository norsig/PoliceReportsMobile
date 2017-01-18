package com.keniobyte.bruino.minsegapp.features.section_police_stations.listPoliceStations;

import com.keniobyte.bruino.minsegapp.features.section_police_stations.IPoliceStationsInteractor;
import com.keniobyte.bruino.minsegapp.models.PoliceStation;
import com.keniobyte.bruino.minsegapp.views.base.BasePresenter;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * @author bruino
 * @version 18/01/17.
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
        List<PoliceStation> policeStations = policeStationsInteractor.getPoliceStations();;
        policeStationInfoView.setPoliceStationAdapter(policeStationInfoView.createPoliceStationAdapter(policeStations));
    }

    @Override
    public void callPoliceStation(PoliceStation policeStation) {
        policeStationInfoView.callPhone(policeStation.getPhone());
    }
}
