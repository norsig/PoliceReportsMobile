package com.keniobyte.bruino.minsegapp.features.section_nav_drawer_list_missing.missing_report;

import org.json.JSONException;

/**
 * @author bruino
 * @version 12/01/17.
 */

public interface IMissingReportPresenter {
    void captcha();
    void reloadCaptcha();
    void sendMissingReport() throws JSONException;
}
