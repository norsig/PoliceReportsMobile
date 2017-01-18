package com.keniobyte.bruino.minsegapp.features.section_list_missing.missing_report;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.lib.recaptcha.ReCaptcha;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.keniobyte.bruino.minsegapp.MainActivity;
import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.utils.CaptchaDialog;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONException;

import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MissingReportActivity extends AppCompatActivity implements IMissingReportView{

    @BindView(R.id.myToolbar) Toolbar toolbar;
    @NotEmpty @BindView(R.id.descriptionPersonEditText) EditText descriptionPersonEditText;

    @BindString(R.string.section_provideData) String titleActivity;
    @BindDrawable(R.drawable.ic_arrow_back_white_24dp) Drawable arrowBack;

    @BindString(R.string.accept) String ok;
    @BindString(R.string.sending) String progressDialogTitle;
    @BindString(R.string.wait_please) String progressDialogMessage;
    @BindString(R.string.send_success) String messageSuccess;
    @BindString(R.string.messageComplete) String message;

    private Context context = this;
    private MissingReportPresenter presenter;
    private MissingReportInteractor interactor;

    private Validator validator;
    private ProgressDialog progressDialog;
    private CaptchaDialog captchaDialog;
    final ReCaptcha.OnVerifyAnswerListener onVerifyAnswerListener = new ReCaptcha.OnVerifyAnswerListener() {
        @Override
        public void onAnswerVerified(boolean success) {
            if (success) {
                try {
                    presenter.sendMissingReport();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                presenter.reloadCaptcha();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_activity_report);
        ButterKnife.bind(this);

        toolbar.setTitle(titleActivity);
        toolbar.setNavigationIcon(arrowBack);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setSupportActionBar(toolbar);

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

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(progressDialogTitle);
        progressDialog.setMessage(progressDialogMessage);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        captchaDialog = new CaptchaDialog();
        captchaDialog.setOnVerifyAnswerListener(onVerifyAnswerListener);

        interactor = new MissingReportInteractor(context);
        presenter = new MissingReportPresenter(interactor);
        presenter.addView(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_person_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send_person_report:
                validator.validate();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getDescription() {
        return descriptionPersonEditText.getText().toString();
    }

    @Override
    public int getId() {
        return getIntent().getExtras().getInt("id");
    }

    @Override
    public void sendMissingReportMessageError(int rStringMessage) {
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
    public void sendMissingReportMessageSuccess() {
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

    private void navigationToHome() {
        startActivity(new Intent(context, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
