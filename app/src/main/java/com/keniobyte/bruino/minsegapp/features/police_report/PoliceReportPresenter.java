package com.keniobyte.bruino.minsegapp.features.police_report;

import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.model.PoliceReport;
import com.keniobyte.bruino.minsegapp.ui.base.BasePresenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * @author bruino
 * @version 02/01/17.
 */

public class PoliceReportPresenter extends BasePresenter<PoliceReportActivityBeta2> implements IPoliceReportPresenter, IPoliceReportInteractor.OnSendReportFinishedListener {
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
        policeReport.setListPathsAttachments(reportPoliceView.getArrayPathsAtachments());
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
        reportPoliceView.setTextDatetimeButton(new SimpleDateFormat("dd/MMM/yyyy - HH:mm", new Locale("es", "ES"))
                .format(reportPoliceView.getIncidentDate().getTime()));
    }

    @Override
    public void showAttachFile(List<ChosenImage> images) {
        reportPoliceView.setImages(images);
        reportPoliceView.setAdapter(reportPoliceView.createMediaResultAdapter(reportPoliceView.getImages()));
        reportPoliceView.createListAttachments();
        //TODO: implemented multiple files
        /*if (reportPoliceView.getImages() == null) {
            reportPoliceView.setImages(images);
            reportPoliceView.setAdapter(reportPoliceView.createMediaResultAdapter(reportPoliceView.getImages()));
            reportPoliceView.createListAttachments();
        } else {
            reportPoliceView.addItemAttachments(images);
            reportPoliceView.getAdapter().notifyDataSetChanged();
        }*/
    }

    @Override
    public void onProgress(int progress) {
        reportPoliceView.setProgressDialog(progress);
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
