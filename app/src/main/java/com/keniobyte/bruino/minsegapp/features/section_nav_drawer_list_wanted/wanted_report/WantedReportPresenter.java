package com.keniobyte.bruino.minsegapp.features.section_nav_drawer_list_wanted.wanted_report;

import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.ui.base.BasePresenter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author bruino
 * @version 12/01/17.
 */

public class WantedReportPresenter extends BasePresenter<WantedReportActivity> implements IWantedReportPresenter, IWantedReportInteractor.OnSendReportFinishedListener {
    private IWantedReportView wantedReportView;
    private IWantedReportInteractor wantedReportInteractor;

    public WantedReportPresenter(IWantedReportInteractor wantedReportInteractor) {
        this.wantedReportInteractor = wantedReportInteractor;
    }

    public void addView(IWantedReportView missingReportView){
        this.wantedReportView = missingReportView;
    }

    public void captcha() {
        wantedReportView.showCaptcha();
    }

    @Override
    public void reloadCaptcha() {
        wantedReportView.hideCaptcha();
        wantedReportView.showCaptcha();
    }

    @Override
    public void sendWantedReport() throws JSONException {
        wantedReportView.showProgress();
        JSONObject wantedReport = new JSONObject();
        wantedReport.put("wanted_data_id", wantedReportView.getId());
        wantedReport.put("description", wantedReportView.getDescription());
        wantedReportInteractor.sendWantedReport(wantedReport, this);
    }

    @Override
    public void sendWantedReportError() {
        wantedReportView.hideProgress();
        wantedReportView.sendWantedReportMessageError(R.string.send_failure);
    }

    //TODO: To implement in MinSegBe.
    /*@Override
    public void wantedReportCoolDown() {
        wantedReportView.hideProgress();
        wantedReportView.sendWantedReportMessageError(R.string.msj_cool_down);
    }*/

    @Override
    public void onSuccess() {
        wantedReportView.hideProgress();
        wantedReportView.sendWantedReportMessageSuccess();
    }
}
