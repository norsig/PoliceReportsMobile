package com.keniobyte.bruino.minsegapp.features.section_nav_drawer_list_missing.missing_report;

/**
 * @author bruino
 * @version 12/01/17.
 */

public interface IMissingReportView {
    String getDescription();
    int getId();

    void sendMissingReportMessageError(int rStringMessage);
    void sendMissingReportMessageSuccess();
    void showCaptcha();
    void hideCaptcha();
    void showProgress();
    void hideProgress();
}
