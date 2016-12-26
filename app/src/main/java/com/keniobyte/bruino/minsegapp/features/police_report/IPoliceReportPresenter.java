package com.keniobyte.bruino.minsegapp.features.police_report;

/**
 * Created by bruino on 03/12/16.
 */

public interface IPoliceReportPresenter {
    void captcha();
    void reloadCaptcha();
    void sendPoliceReport();
    void onDatetimeInput();
    void onSetDatetime();
    //void attachFile();
}
