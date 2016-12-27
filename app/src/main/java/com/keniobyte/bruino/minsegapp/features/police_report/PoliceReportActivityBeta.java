package com.keniobyte.bruino.minsegapp.features.police_report;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.lib.recaptcha.ReCaptcha;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.keniobyte.bruino.minsegapp.MainActivity;
import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.model.PoliceReport;
import com.keniobyte.bruino.minsegapp.utils.CaptchaDialog;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(PoliceReportPresenter.class)
public class PoliceReportActivityBeta extends AppCompatActivity implements IPoliceReportView{
    public String TAG = getClass().getSimpleName();

    @BindView(R.id.myToolbar) Toolbar toolbar;
    @BindView(R.id.editTextNamePerpetrator) EditText namePerpetratorEditText;
    @NotEmpty(trim = true) @BindView(R.id.editTextDescription) EditText descriptionEditText;
    @BindView(R.id.labelClock) TextView clockTextView;
    @BindView(R.id.labelDate) TextView dateTextView;
    @BindView(R.id.btnTimerPicker) Button datetimeButton;
    @BindView(R.id.buttonSendReportPolice) Button sendButton;

    @BindString(R.string.section_anonymousReport) String titlePoliceReportDrugs;
    @BindString(R.string.section_aircraft) String titlePoliceReportAircraft;
    @BindString(R.string.section_internal_affairs) String titlePoliceReportAffairs;
    @BindString(R.string.section_police_report) String titlePoliceReportOnline;

    @BindString(R.string.accept) String ok;
    @BindString(R.string.recharge) String recharge;
    @BindString(R.string.sending) String progressDialogTitle;
    @BindString(R.string.wait_please) String progressDialogMessage;
    @BindString(R.string.send_success) String messageSucces;
    @BindString(R.string.messageDescription) String messageErrorDescription;
    @BindString(R.string.API_KEY_CAPCHTA) String apiKeyCaptcha;
    @BindString(R.string.API_KEY_CAPCHTA_VERIFY) String apiKeyCaptchaVerify;

    private Context mContext = this;
    private PoliceReportInteractor interactor;
    private PoliceReportPresenter presenter;

    private Validator validator;
    private Calendar calendar;
    private ProgressDialog progressDialog;
    private CaptchaDialog captchaDialog;
    final ReCaptcha.OnVerifyAnswerListener onVerifyAnswerListener = new ReCaptcha.OnVerifyAnswerListener() {
        @Override
        public void onAnswerVerified(boolean success) {
            if (success){
                presenter.sendPoliceReport();
            } else {
                presenter.reloadCaptcha();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_police);
        ButterKnife.bind(this);
        setToolbar();

        calendar = Calendar.getInstance();

        validator = new Validator(this);
        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                presenter.captcha();
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                for (ValidationError error : errors) {
                    View view = error.getView();
                    String message = error.getCollatedErrorMessage(mContext);

                    // Display error messages ;)
                    if (view instanceof EditText) {
                        ((EditText) view).setError(message);
                    } else {
                        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle(progressDialogTitle);
        progressDialog.setMessage(progressDialogMessage);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        captchaDialog = new CaptchaDialog();
        captchaDialog.setOnVerifyAnswerListener(onVerifyAnswerListener);

        interactor = new PoliceReportInteractor(this);
        presenter = new PoliceReportPresenter(interactor);
        presenter.addView(this);
    }

    @OnClick(R.id.buttonSendReportPolice)
    public void submit(){
        validator.validate();
    }

    @Override
    public void updateDatetimeLabel(String datetime) {
        dateTextView.setText(datetime);
    }

    @OnClick(R.id.btnTimerPicker)
    public void datetimeInput(){
        presenter.onDatetimeInput();
    }

    final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            calendar.set(year, month, day);

            TimePickerDialog timePickerDialog = new TimePickerDialog(mContext
                    , onTimeSetListener
                    , calendar.get(Calendar.HOUR_OF_DAY)
                    , calendar.get(Calendar.MINUTE)
                    ,true);
            timePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE
                    , ok
                    , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            presenter.onSetDatetime();
                        }
                    });
            timePickerDialog.setCancelable(true);
            timePickerDialog.show();
        }
    };

    final TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
        }
    };

    @Override
    public void showDatetimePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext
                , onDateSetListener
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        datePickerDialog.show();
    }

    @Override
    public String getPerpetrator() {
        return namePerpetratorEditText.getText().toString();
    }

    @Override
    public Calendar getIncidentDate() {
        return calendar;
    }

    @Override
    public String getIncidentDescription() {
        return descriptionEditText.getText().toString();
    }

    //TODO: implement send multiple files
    @Override
    public ArrayList<Uri> getArrayUriAttachFile() {
        return null;
    }

    @Override
    public Double getLatitude() {
        return this.getIntent().getExtras().getDouble("latitude");
    }

    @Override
    public Double getLongitude() {
        return this.getIntent().getExtras().getDouble("longitude");
    }

    @Override
    public String getAddress() {
        return this.getIntent().getExtras().getString("address");
    }

    @Override
    public String getTypePoliceReport() {
        return this.getIntent().getExtras().getString("type_report");
    }

    @Override
    public void sendPoliceReportMessageError(int rStringMessage) {
        new AlertDialog.Builder(this)
                .setMessage(rStringMessage)
                .setCancelable(true)
                .show();
    }


    @Override
    public void sendPoliceReportMessageSuccess() {
        new AlertDialog.Builder(this)
                .setMessage(messageSucces)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        navigationToHome();
                    }
                })
                .show();
    }

    @Override
    public void showCaptcha() {
        captchaDialog.show(getFragmentManager(), "Captcha");
    }

    @Override
    public void hideCaptcha() {
        captchaDialog.dismiss();
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void navigationToHome() {
        startActivity(new Intent(mContext, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    private void setToolbar(){
        String title = null;
        switch (getTypePoliceReport()){
            case PoliceReport.TYPE_POLICE_REPORT_AFFAIR:
                title = titlePoliceReportAffairs;
                break;
            case PoliceReport.TYPE_POLICE_REPORT_AIRCRAFT:
                ((ViewGroup) namePerpetratorEditText.getParent()).removeView(namePerpetratorEditText);
                title = titlePoliceReportAircraft;
                break;
            case PoliceReport.TYPE_POLICE_REPORT_DRUGS:
                title = titlePoliceReportDrugs;
                break;
            case PoliceReport.TYPE_POLICE_REPORT_ONLINE:
                title = titlePoliceReportOnline;
                break;
        }
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
    }
}
