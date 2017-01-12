package com.keniobyte.bruino.minsegapp.features.section_nav_drawer_list_wanted.wanted_report;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author bruino
 * @version 12/01/17.
 */

public interface IWantedReportInteractor {
    void sendWantedReport(JSONObject wantedReport, final OnSendReportFinishedListener listener) throws JSONException;

    interface OnSendReportFinishedListener {
        void sendWantedReportError();
        //TODO: To implement in MinSegBe.
        //void wantedReportCoolDown();
        void onSuccess();
    }
}
