package com.keniobyte.bruino.minsegapp.features.section_list_wanted.wanted_report;

import org.json.JSONException;

/**
 * @author bruino
 * @version 11/01/17.
 */

public interface IWantedReportPresenter {
    void captcha();
    void reloadCaptcha();
    void sendWantedReport() throws JSONException;
}
