package com.keniobyte.bruino.minsegapp.features.police_report;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by bruino on 12/12/16.
 */

public interface IPoliceReportInteractor {
    void sendReportPolice(String perpetrator, String incidentDate, String incidentDescriptor, ArrayList<Uri> arrayUri
            , Double latitude, Double longitude, String address, String typeReport, final OnSendReportFinishedListener listener);
    
    interface OnSendReportFinishedListener {
        void sendReportPoliceError();
        void reportPoliceCoolDown();
        void onSuccess();

    }
}