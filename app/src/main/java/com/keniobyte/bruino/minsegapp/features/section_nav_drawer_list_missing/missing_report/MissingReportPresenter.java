package com.keniobyte.bruino.minsegapp.features.section_nav_drawer_list_missing.missing_report;

import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.ui.base.BasePresenter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author bruino
 * @version 12/01/17.
 */

public class MissingReportPresenter extends BasePresenter<MissingReportActivity> implements IMissingReportPresenter, IMissingReportInteractor.OnSendReportFinishedListener {
    private IMissingReportView missingReportView;
    private IMissingReportInteractor missingReportInteractor;

    public MissingReportPresenter(IMissingReportInteractor missingReportInteractor) {
        this.missingReportInteractor = missingReportInteractor;
    }

    public void addView(IMissingReportView missingReportView){
        this.missingReportView = missingReportView;
    }

    public void captcha() {
        missingReportView.showCaptcha();
    }

    @Override
    public void reloadCaptcha() {
        missingReportView.hideCaptcha();
        missingReportView.showCaptcha();
    }

    @Override
    public void sendMissingReport() throws JSONException {
        missingReportView.showProgress();
        JSONObject missingReport = new JSONObject();
        missingReport.put("missing_data_id", missingReportView.getId());
        missingReport.put("description", missingReportView.getDescription());
        missingReportInteractor.sendMissingReport(missingReport, this);
    }

    @Override
    public void sendMissingReportError() {
        missingReportView.hideProgress();
        missingReportView.sendMissingReportMessageError(R.string.send_failure);
    }

    //TODO: To implement in MinSegBe.
    /*@Override
    public void missingReportCoolDown() {
        missingReportView.hideProgress();
        missingReportView.sendMissingReportMessageError(R.string.msj_cool_down);
    }*/

    @Override
    public void onSuccess() {
        missingReportView.hideProgress();
        missingReportView.sendMissingReportMessageSuccess();
    }
}
