package com.keniobyte.bruino.minsegapp.features.police_report;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by bruino on 01/12/16.
 */

public interface IPoliceReportView {
    String getPerpetrator();
    Calendar getIncidentDate();
    String getIncidentDescription();
    ArrayList<Uri> getArrayUriAttachFile();
    Double getLatitude();
    Double getLongitude();
    String getAddress();
    String getTypePoliceReport();

    void sendPoliceReportMessageError(int rStringMessage);
    void sendPoliceReportMessageSuccess();
    void showDatetimePicker();
    void updateDatetimeLabel(String datetime);
    void showCaptcha();
    void hideCaptcha();
    void showProgress();
    void hideProgress();
}