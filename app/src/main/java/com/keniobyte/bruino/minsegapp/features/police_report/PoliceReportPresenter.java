package com.keniobyte.bruino.minsegapp.features.police_report;

import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.model.PoliceReport;
import com.keniobyte.bruino.minsegapp.ui.base.BasePresenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by bruino on 13/12/16.
 */

public class PoliceReportPresenter extends BasePresenter<PoliceReportActivityBeta> implements IPoliceReportPresenter, IPoliceReportInteractor.OnSendReportFinishedListener {
    private IPoliceReportInteractor reportPoliceInteractor;
    private IPoliceReportView reportPoliceView;

    public PoliceReportPresenter(IPoliceReportInteractor reportPoliceInteractor) {
        this.reportPoliceInteractor = reportPoliceInteractor;
    }

    public void addView(IPoliceReportView reportPoliceView) {
        this.reportPoliceView = reportPoliceView;
    }

    @Override
    public void captcha() {
        reportPoliceView.showCaptcha();
    }

    @Override
    public void reloadCaptcha() {
        reportPoliceView.hideCaptcha();
        reportPoliceView.showCaptcha();
    }

    @Override
    public void sendPoliceReport() {
        reportPoliceView.showProgress();

        PoliceReport policeReport = new PoliceReport();
        policeReport.setNamePerpetrator(reportPoliceView.getPerpetrator());
        policeReport.setIncidentDate(calendarToDateFormat(reportPoliceView.getIncidentDate()));
        policeReport.setIncidentDescription(reportPoliceView.getIncidentDescription());
        policeReport.setArrayListUriAttachFile(reportPoliceView.getArrayUriAttachFile());
        policeReport.setLatitude(reportPoliceView.getLatitude());
        policeReport.setLongitude(reportPoliceView.getLongitude());
        policeReport.setIncidentAddress(reportPoliceView.getAddress());
        policeReport.setTypePoliceReport(reportPoliceView.getTypePoliceReport());

        reportPoliceInteractor.sendReportPolice(policeReport, this);
    }

    @Override
    public void onDatetimeInput(){
        reportPoliceView.showDatetimePicker();
    }

    @Override
    public void onSetDatetime() {
        reportPoliceView.updateDatetimeLabel(new SimpleDateFormat("dd/MM/yy", Locale.US)
                .format(reportPoliceView.getIncidentDate().getTime()));
    }

    @Override
    public void sendReportPoliceError() {
        reportPoliceView.hideProgress();
        reportPoliceView.sendPoliceReportMessageError(R.string.send_failure);
    }

    @Override
    public void reportPoliceCoolDown() {
        reportPoliceView.hideProgress();
        reportPoliceView.sendPoliceReportMessageError(R.string.msj_cool_down);
    }

    @Override
    public void onSuccess() {
        reportPoliceView.hideProgress();
        reportPoliceView.sendPoliceReportMessageSuccess();
    }

    private String calendarToDateFormat(Calendar calendar){
        String dateFormat = null;
        if (calendar != null){
            dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US).format(calendar.getTime());
        }
        return dateFormat;
    }
}
