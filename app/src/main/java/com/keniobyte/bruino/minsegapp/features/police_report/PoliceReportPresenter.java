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
    private IPoliceReportInteractor policeReportInteractor;
    private IPoliceReportView policeReportView;

    public PoliceReportPresenter(IPoliceReportInteractor policeReportInteractor) {
        this.policeReportInteractor = policeReportInteractor;
    }

    public void addView(IPoliceReportView policeReportView) {
        this.policeReportView = policeReportView;
    }

    @Override
    public void captcha() {
        policeReportView.showCaptcha();
    }

    @Override
    public void reloadCaptcha() {
        policeReportView.hideCaptcha();
        policeReportView.showCaptcha();
    }

    @Override
    public void sendPoliceReport() {
        policeReportView.showProgress();

        PoliceReport policeReport = new PoliceReport();
        policeReport.setNamePerpetrator(policeReportView.getPerpetrator());
        policeReport.setIncidentDate(calendarToDateFormat(policeReportView.getIncidentDate()));
        policeReport.setIncidentDescription(policeReportView.getIncidentDescription());
        policeReport.setListPathsAttachments(policeReportView.getArrayPathsAtachments());
        policeReport.setLatitude(policeReportView.getLatitude());
        policeReport.setLongitude(policeReportView.getLongitude());
        policeReport.setIncidentAddress(policeReportView.getAddress());
        policeReport.setTypePoliceReport(policeReportView.getTypePoliceReport());

        policeReportInteractor.sendReportPolice(policeReport, this);
    }

    @Override
    public void onDatetimeInput(){
        policeReportView.showDatetimePicker();
    }

    @Override
    public void onSetDatetime() {
        policeReportView.setTextDatetimeButton(new SimpleDateFormat("dd/MMM/yyyy - HH:mm", new Locale("es", "ES"))
                .format(policeReportView.getIncidentDate().getTime()));
    }

    @Override
    public void showAttachFile(List<ChosenImage> images) {
        policeReportView.setImages(images);
        policeReportView.setAdapter(policeReportView.createMediaResultAdapter(policeReportView.getImages()));
        policeReportView.createListAttachments();
        //TODO: implemented multiple files
        /*if (policeReportView.getImages() == null) {
            policeReportView.setImages(images);
            policeReportView.setAdapter(policeReportView.createMediaResultAdapter(policeReportView.getImages()));
            policeReportView.createListAttachments();
        } else {
            policeReportView.addItemAttachments(images);
            policeReportView.getAdapter().notifyDataSetChanged();
        }*/
    }

    @Override
    public void onProgress(int progress) {
        policeReportView.setProgressDialog(progress);
    }

    @Override
    public void sendReportPoliceError() {
        policeReportView.hideProgress();
        policeReportView.sendPoliceReportMessageError(R.string.send_failure);
    }

    @Override
    public void reportPoliceCoolDown() {
        policeReportView.hideProgress();
        policeReportView.sendPoliceReportMessageError(R.string.msj_cool_down);
    }

    @Override
    public void onSuccess() {
        policeReportView.hideProgress();
        policeReportView.sendPoliceReportMessageSuccess();
    }

    private String calendarToDateFormat(Calendar calendar){
        String dateFormat = null;
        if (calendar != null){
            dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US).format(calendar.getTime());
        }
        return dateFormat;
    }
}
