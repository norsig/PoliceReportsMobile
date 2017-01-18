package com.keniobyte.bruino.minsegapp.features.section_police_report;

import org.json.JSONException;

/**
 * @author bruino
 * @version 05/01/17.
 */

public interface ISectionPoliceReportPresenter {
    void isCurrentVersion(int version) throws JSONException;
    void onClickPoliceReportAircraft();
    void onClickPoliceReportDrugs();
    void onClickUpdate();
    void onClickCancelUpdate();
}
