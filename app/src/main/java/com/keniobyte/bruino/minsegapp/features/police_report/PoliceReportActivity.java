package com.keniobyte.bruino.minsegapp.features.police_report;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.lib.recaptcha.ReCaptcha;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.keniobyte.bruino.minsegapp.MainActivity;
import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.features.police_report.adapter.MediaResultAdapter;
import com.keniobyte.bruino.minsegapp.models.PoliceReport;
import com.keniobyte.bruino.minsegapp.views.components.NestedListView;
import com.keniobyte.bruino.minsegapp.utils.CaptchaDialog;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;

/**
 * @author bruino
 * @version 02/01/17.
 */
@RequiresPresenter(PoliceReportPresenter.class)
public class PoliceReportActivity extends AppCompatActivity implements IPoliceReportView, IPoliceReportPresenter.onListAttachmentsListener {
    public String TAG = getClass().getSimpleName();

    @BindView(R.id.myToolbar) Toolbar toolbar;
    @BindView(R.id.namePerpetratorEditText) EditText namePerpetratorEditText;
    @BindView(R.id.datetimeButton) Button datetimeButton;
    @BindView(R.id.descriptionEditText) @NotEmpty(trim = true) EditText descriptionEditText;
    @BindView(R.id.attachmentsListView) NestedListView attachmentsListView;

    @BindDrawable(R.drawable.ic_arrow_back_white_24dp) Drawable arrowBack;

    @BindString(R.string.police_report_drugs) String titlePoliceReportDrugs;
    @BindString(R.string.police_report_aircraft) String titlePoliceReportAircraft;
    @BindString(R.string.police_report_affairs) String titlePoliceReportAffairs;
    @BindString(R.string.police_report_online) String titlePoliceReportOnline;
    @BindString(R.string.anonymous) String anonymous;

    @BindString(R.string.accept) String ok;
    @BindString(R.string.sending) String progressDialogTitle;
    @BindString(R.string.wait_please) String progressDialogMessage;
    @BindString(R.string.send_success) String messageSuccess;
    @BindString(R.string.messageComplete) String message;

    private Context context = this;
    private PoliceReportInteractor interactor;
    private PoliceReportPresenter presenter;

    private Validator validator;
    private Calendar calendar;
    private ImagePicker imagePicker;
    private List<ChosenImage> images;
    private MediaResultAdapter adapter;
    private ProgressDialog progressDialog;
    private CaptchaDialog captchaDialog;
    final ReCaptcha.OnVerifyAnswerListener onVerifyAnswerListener = new ReCaptcha.OnVerifyAnswerListener() {
        @Override
        public void onAnswerVerified(boolean success) {
            if (success) {
                presenter.sendPoliceReport();
            } else {
                presenter.reloadCaptcha();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.police_report_activity_form);
        ButterKnife.bind(this);

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
                    // Display error messages ;)
                    if (view instanceof EditText) {
                        ((EditText) view).setError(message);
                    }
                }
            }
        });

        setTitleToolbar();
        toolbar.setNavigationIcon(arrowBack);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(progressDialogTitle);
        progressDialog.setMessage(progressDialogMessage);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);

        captchaDialog = new CaptchaDialog();
        captchaDialog.setOnVerifyAnswerListener(onVerifyAnswerListener);

        interactor = new PoliceReportInteractor(this);
        presenter = new PoliceReportPresenter(interactor);
        presenter.addView(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_police_report_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_attachedment:
                pickImageSingle();
                return true;
            case R.id.action_send_police_report:
                validator.validate();
                break;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public List<String> getArrayPathsAtachments() {
        List<String> paths = new ArrayList<String>();
        if (images != null) {
            for (ChosenImage image : images) {
                paths.add(image.getOriginalPath());
            }
        }
        return paths;
    }

    @Override
    public Double getLatitude() {
        return getIntent().getExtras().getDouble("latitude");
    }

    @Override
    public Double getLongitude() {
        return getIntent().getExtras().getDouble("longitude");
    }

    @Override
    public String getAddress() {
        return getIntent().getExtras().getString("address");
    }

    @Override
    public String getTypePoliceReport() {
        return getIntent().getExtras().getString("type_report");
    }

    @Override
    public void createListAttachments() {
        attachmentsListView.setAdapter(adapter);
    }

    @Override
    public void onItemDelete(int position) {
        images.remove(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public MediaResultAdapter createMediaResultAdapter(List<ChosenImage> images) {
        return new MediaResultAdapter(images, context, this);
    }

    @Override
    public void sendPoliceReportMessageError(int rStringMessage) {
        new AlertDialog.Builder(this)
                .setMessage(rStringMessage)
                .setCancelable(true)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }

    @Override
    public void sendPoliceReportMessageSuccess() {
        new AlertDialog.Builder(this)
                .setMessage(messageSuccess)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        navigationToHome();
                    }
                })
                .show();
    }

    final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            calendar.set(year, month, day);

            TimePickerDialog timePickerDialog = new TimePickerDialog(context
                    , onTimeSetListener
                    , calendar.get(Calendar.HOUR_OF_DAY)
                    , calendar.get(Calendar.MINUTE)
                    , true);
            timePickerDialog.setCancelable(true);
            timePickerDialog.show();
        }
    };

    final TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            presenter.onSetDatetime();
        }
    };

    @Override
    public void showDatetimePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context
                , onDateSetListener
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        datePickerDialog.show();
    }

    @Override
    public void setTextDatetimeButton(String datetime) {
        datetimeButton.setText(datetime);
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
        progressDialog.dismiss();
    }

    @Override
    public void setProgressDialog(int progress) {
        progressDialog.setProgress(progress);
    }

    @OnClick(R.id.datetimeButton)
    public void onClickDatetimeButton() {
        presenter.onDatetimeInput();
    }

    @Override
    public void addItemAttachments(List<ChosenImage> images) {
        for (ChosenImage image : images) {
            this.images.add(image);
        }
    }

    @Override
    public void setAdapter(MediaResultAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public List<ChosenImage> getImages() {
        return images;
    }

    @Override
    public void setImages(List<ChosenImage> images) {
        this.images = images;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                if (imagePicker == null) {
                    imagePicker = new ImagePicker(this);
                    imagePicker.setImagePickerCallback(imagePickerCallback);
                }
                imagePicker.submit(data);
            }
        }
    }

    private void navigationToHome() {
        startActivity(new Intent(context, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    private void setTitleToolbar() {
        switch (getTypePoliceReport()) {
            case PoliceReport.TYPE_POLICE_REPORT_AFFAIR:
                toolbar.setTitle(titlePoliceReportAffairs);
                toolbar.setSubtitle(anonymous);
                break;
            case PoliceReport.TYPE_POLICE_REPORT_AIRCRAFT:
                ((ViewGroup) namePerpetratorEditText.getParent()).removeView(namePerpetratorEditText);
                toolbar.setTitle(titlePoliceReportAircraft);
                toolbar.setSubtitle(anonymous);
                break;
            case PoliceReport.TYPE_POLICE_REPORT_DRUGS:
                toolbar.setTitle(titlePoliceReportDrugs);
                toolbar.setSubtitle(anonymous);
                break;
            case PoliceReport.TYPE_POLICE_REPORT_ONLINE:
                toolbar.setTitle(titlePoliceReportOnline);
                break;
        }
    }

    private ImagePickerCallback imagePickerCallback = new ImagePickerCallback() {
        @Override
        public void onImagesChosen(List<ChosenImage> list) {
            presenter.showAttachFile(list);
        }

        @Override
        public void onError(String message) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    };

    public void pickImageSingle() {
        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 7);
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 7);
            }

        } else {
            imagePicker = new ImagePicker(this);
            //imagePicker.allowMultiple();
            imagePicker.setImagePickerCallback(imagePickerCallback);
            imagePicker.pickImage();
        }
    }
}
