package com.keniobyte.bruino.minsegapp.features.section_list_missing.missing_report;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author bruino
 * @version 11/01/17.
 */

public interface IMissingReportInteractor {
    void sendMissingReport(JSONObject missingReport, final OnSendReportFinishedListener listener) throws JSONException;

    interface OnSendReportFinishedListener {
        void sendMissingReportError();
        //TODO: To implement in MinSegBe.
        //void missingReportCoolDown();
        void onSuccess();
    }
}
