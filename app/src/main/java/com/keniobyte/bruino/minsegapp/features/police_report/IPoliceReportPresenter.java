package com.keniobyte.bruino.minsegapp.features.police_report;

import com.kbeanie.multipicker.api.entity.ChosenImage;

import java.util.List;

/**
 * @author bruino
 * @version 03/12/16.
 */

public interface IPoliceReportPresenter {
    void captcha();
    void reloadCaptcha();
    void sendPoliceReport();
    void onDatetimeInput();
    void onSetDatetime();
    void showAttachFile(List<ChosenImage> images);

    interface onListAttachmentsListener {
        void onItemDelete(int position);
    }
}
