package com.keniobyte.bruino.minsegapp.features.police_report;

import android.net.Uri;

import com.keniobyte.bruino.minsegapp.model.PoliceReport;

import java.util.ArrayList;

/**
 * Created by bruino on 12/12/16.
 */

public interface IPoliceReportInteractor {
    void sendReportPolice(PoliceReport policeReport, final OnSendReportFinishedListener listener);

    interface OnSendReportFinishedListener {
        void sendReportPoliceError();
        void reportPoliceCoolDown();
        void onSuccess();

    }
}