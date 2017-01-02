package com.keniobyte.bruino.minsegapp.features.police_report;

import android.net.Uri;

import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.keniobyte.bruino.minsegapp.features.police_report.adapter.MediaResultAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author bruino
 * @version 01/12/16.
 */

public interface IPoliceReportView {
    String getPerpetrator();
    Calendar getIncidentDate();
    String getIncidentDescription();
    List<String> getArrayPathsAtachments();
    Double getLatitude();
    Double getLongitude();
    String getAddress();
    String getTypePoliceReport();
    List<ChosenImage> getImages();
    MediaResultAdapter createMediaResultAdapter(List<ChosenImage> images);

    void createListAttachments();
    void addItemAttachments(List<ChosenImage> images);
    void setAdapter(MediaResultAdapter adapter);
    void setImages(List<ChosenImage> images);
    void sendPoliceReportMessageError(int rStringMessage);
    void sendPoliceReportMessageSuccess();
    void showDatetimePicker();
    void setTextDatetimeButton(String datetime);
    void showCaptcha();
    void hideCaptcha();
    void showProgress();
    void hideProgress();
    void setProgressDialog(int progress);
}