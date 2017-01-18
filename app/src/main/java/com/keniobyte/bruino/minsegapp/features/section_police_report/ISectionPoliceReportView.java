package com.keniobyte.bruino.minsegapp.features.section_police_report;

/**
 * @author bruino
 * @version 05/01/17.
 */

public interface ISectionPoliceReportView {
    void showProgressBar();
    void hideProgressBar();
    void showListTypePoliceReport();
    void hideListTypePoliceReport();
    void updateAppMessage();
    void connectionErrorMessage();
    void navigationToLocationPoliceReport(String typeReport);
    void navigationToAppStore();
}
