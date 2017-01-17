package com.keniobyte.bruino.minsegapp.features.section_police_stations.listPoliceStations;

import com.keniobyte.bruino.minsegapp.features.section_police_stations.listPoliceStations.adapter.PoliceStationAdapter;
import com.keniobyte.bruino.minsegapp.model.PoliceStation;

import java.util.List;

/**
 * @author bruino
 * @version 17/01/17.
 */

public interface IPoliceStationInfoView {
    PoliceStationAdapter createPoliceStationAdapter(List<PoliceStation> policeStations);

    void setPoliceStationAdapter(PoliceStationAdapter adapter);
    void callPhone(String number_phone);
    void onListCallPhoneInteraction(PoliceStation policeStation);
}
