package com.keniobyte.bruino.minsegapp.ui.reportPolices;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.lib.recaptcha.ReCaptcha;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.keniobyte.bruino.minsegapp.model.SendReport;
import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.utils.RealPathUtil;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.squareup.picasso.Picasso;
import static android.content.ContentResolver.SCHEME_CONTENT;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 *       Reports police corruption anonymously:
 *       - Crime location (required)
 *       - Perpetrator's name (optional)
 *       - Description(required)
 *       - Picture attachment/evidence (optional)
 *       - Crime date and time(optional)
 */

public class ReportPoliceAnonymusActivity extends ActionBarActivity {

    private  final String TAG = getClass().getSimpleName();

    private int PICK_IMAGE_REQUEST = 1;
    private static int MY_PERMISSIONS_REQUEST_READ_EXTERNAL = 7;

    private Uri uriImage = null;
    private int calendarFlag = 0;
    private boolean captchaFlag = false;

    private Bundle bundle;
    private Context mContext = this;
    private SharedPreferences preferences = null;

    @NotEmpty(message = "Complete la Descripción")
    private EditText editTextDescription;
    private EditText editTextNamePerpetrator;
    private Calendar myCalendar = Calendar.getInstance();
    protected Button buttonTimePicker;
    private Button buttonAddPhoto;
    private ImageView imageViewEvidence;
    private TextView labelDate, labelTime;

    //Date popup method
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            //Hour popup method
            final TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, time, myCalendar
                    .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true);
            timePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.accept)
                    , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            calendarFlag = 1;
                            updateLabel();
                        }
                    });
            timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel)
                    , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    });
            timePickerDialog.show();
        }
    };

    //Hour popup method
    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_police);
        preferences = getSharedPreferences("minsegapp", Context.MODE_PRIVATE);

        bundle = this.getIntent().getExtras();

        setToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        editTextNamePerpetrator = (EditText) findViewById(R.id.editTextNamePerpetrator);
        labelDate = (TextView) findViewById(R.id.labelDate);
        labelTime = (TextView) findViewById(R.id.labelClock);

        buttonTimePicker = (Button) findViewById(R.id.btnTimerPicker);
        buttonTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
            }
        });

        final Validator validator = new Validator(mContext);
        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                //Check for empty spaces
                if (!editTextDescription.getText().toString().replaceAll("\\s", "").equals("")){
                    verifyCaptcha();
                } else {
                    editTextDescription.setError(getResources().getString(R.string.messageDescription));
                }
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

        Button buttonSendPoliceReport = (Button) findViewById(R.id.buttonSendReportPolice);
        buttonSendPoliceReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        imageViewEvidence = (ImageView) findViewById(R.id.imageViewEvidence);
        buttonAddPhoto = (Button) findViewById(R.id.buttonAddPhoto);
        buttonAddPhoto.setClickable(true);
        buttonAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PackageManager.PERMISSION_GRANTED !=  ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL);
                    } else {
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL);
                    }

                } else {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, PICK_IMAGE_REQUEST);
                    /*Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Selected Picture"), PICK_IMAGE_REQUEST);*/
                }
            }
        });
    }

    private void verifyCaptcha(){
        final AlertDialog.Builder alertdialog = new AlertDialog.Builder(ReportPoliceAnonymusActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogCaptcha = inflater.inflate(R.layout.dialog_recaptcha, null);
        final ReCaptcha reCaptcha = (ReCaptcha) dialogCaptcha.findViewById(R.id.captcha);
        final EditText editTextCaptcha = (EditText) dialogCaptcha.findViewById(R.id.edit_text_captcha);
        //ImageButton buttonChangedCaptcha = (ImageButton) dialogCaptcha.findViewById(R.id.buttonChangedCaptcha);

        /*buttonChangedCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reCaptcha.showChallengeAsync(getString(R.string.API_KEY_CAPCHTA), new ReCaptcha.OnShowChallengeListener() {
                    @Override
                    public void onChallengeShown(boolean shown) {
                        editTextCaptcha.setText("");
                    }
                });
            }
        });*/

        reCaptcha.setLanguageCode("es");
        reCaptcha.showChallengeAsync(getString(R.string.API_KEY_CAPCHTA), new ReCaptcha.OnShowChallengeListener() {
            @Override
            public void onChallengeShown(boolean shown) {
                Log.i(TAG, "SHOWN : " + shown);
                captchaFlag = shown;
            }
        });

        alertdialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                if (captchaFlag && !editTextCaptcha.getText().toString().isEmpty()){
                    reCaptcha.verifyAnswerAsync(getString(R.string.API_KEY_CAPCHTA_VERIFY)
                            , editTextCaptcha.getText().toString(), new ReCaptcha.OnVerifyAnswerListener() {
                                @Override
                                public void onAnswerVerified(boolean success) {
                                    Log.i(TAG, String.valueOf(success));
                                    if (success){
                                        try {
                                            sendReport();
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        verifyCaptcha();
                                    }
                                }
                            });
                } else {
                    verifyCaptcha();
                }
            }
        });

        alertdialog.setView(dialogCaptcha);
        AlertDialog alertDialogCaptcha = alertdialog.create();
        alertDialogCaptcha.setCanceledOnTouchOutside(false);
        alertDialogCaptcha.show();
        // Show keyboard input.
        alertDialogCaptcha.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void sendReport() throws FileNotFoundException{
        RequestParams requestParams = new RequestParams();

        //Send user's anonymous id if it exists.
        if (preferences.getInt("anonymous_id", 0) != 0){
            requestParams.put("anonymous_id", preferences.getInt("anonymous_id", 0));
        }

        //Send date(optional)
        if (calendarFlag == 1){
            requestParams.put("incident_date", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US).format(myCalendar.getTime()));
        }

        //Send picture(optional)
        if(uriImage != null){
            if(uriImage.getScheme().equals(SCHEME_CONTENT)){
                requestParams.put("file_name", uriImage.getLastPathSegment());
                requestParams.put("picture", getContentResolver().openInputStream(uriImage));
            } else {
                File file = new File(getRealPathImage(uriImage));
                requestParams.put("picture", file);
                requestParams.put("file_name", file.getName());
            }
        }

        //Send perpetrator(optional)
        String namePerpetrator = editTextNamePerpetrator.getText().toString().replaceAll("\\s", "");
        if (!namePerpetrator.equals("")){
            requestParams.put("perpetrator", editTextNamePerpetrator.getText().toString());
        }

        //Send report(required)
        requestParams.put("incident_description", editTextDescription.getText().toString());

        //Send coordinates(required)
        requestParams.put("lat", bundle.getDouble("latitude"));
        requestParams.put("lng", bundle.getDouble("longitude"));

        //Send address(required)
        requestParams.put("address", bundle.getString("address"));

        //Report type
        requestParams.put("report_type", "drugs");

        SendReport sendReport = new SendReport(requestParams, mContext);
        sendReport.setProgressDialog(createProgressDialog());
        sendReport.setAlertDialog(createAlertDialog());
        sendReport.sendPoliceReport();
    }

    //Updates date on UI
    private void updateLabel() {
        labelDate.setText(new SimpleDateFormat("dd/MM/yy", Locale.US).format(myCalendar.getTime()));
        StringBuilder string = new StringBuilder();
        string.append(myCalendar.get(Calendar.HOUR_OF_DAY))
                .append(":")
                .append(myCalendar.get(Calendar.MINUTE));
        labelTime.setText(string);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //Picture path
            uriImage = data.getData();
            if(uriImage.getScheme().equals(SCHEME_CONTENT)){
                Log.i(TAG, "File Scheme: content");
                String path = getRealPathImage(uriImage);
                if (path != null){
                    Picasso.with(mContext).load(new File(path)).fit().centerCrop().into(imageViewEvidence);
                }
            } else {
                Log.i(TAG, "File Scheme: file");
                Picasso.with(mContext).load(uriImage).fit().centerCrop().into(imageViewEvidence);
            }
        }
    }

    private String getRealPathImage(Uri uri){
        // SDK < API11
        if (Build.VERSION.SDK_INT < 11)
            return RealPathUtil.getRealPathFromURI_BelowAPI11(this, uri);
            // SDK >= 11 && SDK < 19
        else if (Build.VERSION.SDK_INT < 19)
            return RealPathUtil.getRealPathFromURI_API11to18(this, uri);
            // SDK > 19 (Android 4.4)
        else
            return RealPathUtil.getRealPathFromURI_API19(this, uri);
    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle(getResources().getString(R.string.section_anonymousReport));
        setSupportActionBar(toolbar);
    }

    private ProgressDialog createProgressDialog(){
        ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle(mContext.getResources().getString(R.string.sending));
        progressDialog.setMessage(mContext.getApplicationContext().getResources().getString(R.string.wait_please));
        return progressDialog;
    }

    private AlertDialog createAlertDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ReportPoliceAnonymusActivity.this);
        alertDialog.setTitle(getResources().getString(R.string.section_police_report));
        return alertDialog.create();
    }
}