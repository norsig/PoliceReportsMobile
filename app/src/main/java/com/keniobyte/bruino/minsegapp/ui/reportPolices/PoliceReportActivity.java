package com.keniobyte.bruino.minsegapp.ui.reportPolices;

import android.Manifest;
import android.app.Activity;
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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.app.AlertDialog;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 *      Police  report Online (No implemented): Report with personal user information included, law compliance.
 *       - Crime location (required)
 *       - Perpetrator's name (required)
 *       - Description(required)
 *       - Picture attachment/evidence (optional)
 *       - Crime date and time(required)
*/

public class PoliceReportActivity extends ActionBarActivity {

    private  final String TAG = getClass().getSimpleName();

    private int PICK_IMAGE_REQUEST = 1;
    private static int MY_PERMISSIONS_REQUEST_READ_EXTERNAL = 7;

    public static final int TYPE_ONLINE_REPORT = 1;
    public static final int TYPE_ANONYMUS_REPORT = 2;
    public static final int TYPE_INTERNAL_AFFAIRS_REPORT = 3;

    private String realPathImage = null;
    private int flagCalendar = 0;

    private Bundle bundle;
    private Context mContext = this;
    private SharedPreferences preferences = null;

    @NotEmpty(message = "Complete la Descripción")
    private EditText editTextDescription;
    @NotEmpty(message = "Complete con el nombre del acusado")
    private EditText editTextNamePerpetrator;

    private Calendar myCalendar = Calendar.getInstance();
    protected Button buttonTimePicker;
    private Button buttonAddPhoto;
    private ImageView imageViewEvidence;
    private TextView labelDate, labelTime;

    //Metodo para popup de Fecha.
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            //Metodo para popup de hora del hecho
            final TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, time, myCalendar
                    .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true);
            timePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.accept)
                    , new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    flagCalendar = 1;
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

    //Metodo para popup de Hora.
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
                //Verifica si hay espacios vacios solamente.
                if (!editTextNamePerpetrator.getText().toString().replaceAll("\\s", "").equals("")){
                    //Verifica si hay espacios vacios solamente.
                    if (!editTextDescription.getText().toString().replaceAll("\\s", "").equals("")){
                        captchaVerify();
                    } else {
                        editTextDescription.setError(getResources().getString(R.string.messageDescription));
                    }
                } else {
                    editTextNamePerpetrator.setError(getResources().getString(R.string.messagePerpetrator));
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

        Button buttonSendReportPolice = (Button) findViewById(R.id.buttonSendReportPolice);
        buttonSendReportPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        imageViewEvidence = (ImageView) findViewById(R.id.imageViewEvidence);
        buttonAddPhoto = (Button) findViewById(R.id.buttonAddPhoto);
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
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Selected Picture"), PICK_IMAGE_REQUEST);
                }
            }
        });
    }

    private void captchaVerify(){
        final AlertDialog.Builder alertdialog = new AlertDialog.Builder(PoliceReportActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogCaptcha = inflater.inflate(R.layout.dialog_recaptcha, null);
        final ReCaptcha reCaptcha = (ReCaptcha) dialogCaptcha.findViewById(R.id.captcha);
        final EditText editTextCaptcha = (EditText) dialogCaptcha.findViewById(R.id.edit_text_captcha);
        /*ImageButton buttonChangedCaptcha = (ImageButton) dialogCaptcha.findViewById(R.id.buttonChangedCaptcha);

        buttonChangedCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reCaptcha.showChallengeAsync(getString(R.string.API_KEY_CAPCHTA), new ReCaptcha.OnShowChallengeListener() {
                    @Override
                    public void onChallengeShown(boolean shown) {
                        Log.i(TAG, "SHOWN : " + shown);
                    }
                });
            }
        });*/

        reCaptcha.setLanguageCode("es");
        reCaptcha.showChallengeAsync(getString(R.string.API_KEY_CAPCHTA), new ReCaptcha.OnShowChallengeListener() {
            @Override
            public void onChallengeShown(boolean shown) {
                Log.i(TAG, "SHOWN : " + shown);
            }
        });

        alertdialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                reCaptcha.verifyAnswerAsync(getString(R.string.API_KEY_CAPCHTA_VERIFY)
                        , editTextCaptcha.getText().toString(), new ReCaptcha.OnVerifyAnswerListener() {
                            @Override
                            public void onAnswerVerified(boolean success) {
                                if (success){
                                    try {
                                        sendReport();
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    captchaVerify();
                                }
                            }
                        });
            }
        }).create();

        alertdialog.setView(dialogCaptcha);
        AlertDialog a = alertdialog.create();
        a.show();
        a.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }

    private void sendReport() throws FileNotFoundException{
        RequestParams requestParams = new RequestParams();

        //Send date(required)
        if (flagCalendar == 1){
            requestParams.put("incident_date", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US).format(myCalendar.getTime()));
        } else {
            Toast.makeText(getApplicationContext(), "Coloque horario del suceso", Toast.LENGTH_LONG).show();
            return;
        }

        //Send picture(optional)
        if(realPathImage != null){
            File file = new File(realPathImage);
            requestParams.put("picture", file);
            requestParams.put("file_name", file.getName());
        }

        //Send perpetrator(required)
        requestParams.put("perpetrator", editTextNamePerpetrator.getText().toString());

        //Send report(required)
        requestParams.put("incident_description", editTextDescription.getText().toString());

        //Send coordinates(required)
        requestParams.put("lat", bundle.getDouble("latitude"));
        requestParams.put("lng", bundle.getDouble("longitude"));

        //Send address(required)
        requestParams.put("address", bundle.getString("address"));

        //TODO: Send SSN(Required)
        requestParams.put("ssn", preferences.getInt("ssn", 12345678));

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
            //Image path
            Uri uri = data.getData();

            // SDK < API11
            if (Build.VERSION.SDK_INT < 11)
                realPathImage = RealPathUtil.getRealPathFromURI_BelowAPI11(this, uri);

            // SDK >= 11 && SDK < 19
            else if (Build.VERSION.SDK_INT < 19)
                realPathImage = RealPathUtil.getRealPathFromURI_API11to18(this, uri);

            // SDK > 19 (Android 4.4)
            else
                realPathImage = RealPathUtil.getRealPathFromURI_API19(this, uri);

            Picasso.with(mContext).load(uri).fit().centerCrop().into(imageViewEvidence);
        }
    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle(getResources().getString(R.string.section_police_report));
        setSupportActionBar(toolbar);
    }

    private ProgressDialog createProgressDialog(){
        ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle(mContext.getResources().getString(R.string.sending));
        progressDialog.setMessage(mContext.getApplicationContext().getResources().getString(R.string.wait_please));
        return progressDialog;
    }

    private AlertDialog createAlertDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PoliceReportActivity.this);
        alertDialog.setTitle(getResources().getString(R.string.section_police_report));
        return alertDialog.create();
    }
}