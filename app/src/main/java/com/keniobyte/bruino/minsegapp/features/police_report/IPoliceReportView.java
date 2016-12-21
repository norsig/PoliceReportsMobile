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
    String getIncidentDescriptor();
    ArrayList<Uri> getArrayUri();
    Double getLatitude();
    Double getLongitude();
    String getAddress();
    String getReportType();

    void showDatePicker();
    void showCaptcha();
    void updateLabelTime();
    void sendReportPoliceMessageError(int r_string_message);
    void sendReportPoliceMessageSuccess();
    void showProgress();
    void hideProgress();
    void navigationToHome();
}