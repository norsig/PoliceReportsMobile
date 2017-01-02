package com.keniobyte.bruino.minsegapp.features.police_report;

import android.net.Uri;

import com.keniobyte.bruino.minsegapp.model.PoliceReport;

import java.util.ArrayList;

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