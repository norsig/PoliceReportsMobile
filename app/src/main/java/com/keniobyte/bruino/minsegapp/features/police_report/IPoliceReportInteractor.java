package com.keniobyte.bruino.minsegapp.features.police_report;

import com.keniobyte.bruino.minsegapp.models.PoliceReport;

/**
 * @author bruino
 * @version 12/12/16.
 */

public interface IPoliceReportInteractor {
    void sendReportPolice(PoliceReport policeReport, final OnSendReportFinishedListener listener);

    interface OnSendReportFinishedListener {
        void onProgress(int progress);
        void sendReportPoliceError();
        void reportPoliceCoolDown();
        void onSuccess();
    }
}