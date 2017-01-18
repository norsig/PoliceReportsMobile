package com.keniobyte.bruino.minsegapp.features.section_list_wanted.wanted_report;

/**
 * @author bruino
 * @version 11/01/17.
 */

public interface IWantedReportView {
    String getDescription();
    int getId();

    void sendWantedReportMessageError(int rStringMessage);
    void sendWantedReportMessageSuccess();
    void showCaptcha();
    void hideCaptcha();
    void showProgress();
    void hideProgress();
}
