package com.keniobyte.bruino.minsegapp.features.section_nav_drawer_police_report;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.support.v7.widget.Toolbar;

import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.features.location_police_report.LocationPoliceReportActivity;

import org.json.JSONException;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(SectionPoliceReportPresenter.class)
public class SectionPoliceReportActivity extends AppCompatActivity implements ISectionPoliceReportView {

    @BindView(R.id.myToolbar) Toolbar toolbar;
    @BindView(R.id.checkingVersionProgressBar) ProgressBar checkingVersionProgressBar;
    @BindView(R.id.policeReportDrugsButton) Button policeReportDrugsButton;
    @BindView(R.id.policeReportAircraftButton) Button policeReportAircraftButton;
    @BindView(R.id.policeReportLinearLayout) LinearLayout policeReportLinearLayout;

    @BindString(R.string.section_police_report) String titlePoliceReport;
    @BindString(R.string.title_attention) String titleWarning;
    @BindString(R.string.message_not_current_version) String messageUpdateApp;
    @BindString(R.string.button_update) String update;
    @BindString(R.string.cancel) String cancel;
    @BindString(R.string.title_error) String titleError;
    @BindString(R.string.message_error_current_version) String messageConnectionError;
    @BindString(R.string.accept) String ok;

    @BindDrawable(R.drawable.ic_arrow_back_white_24dp) Drawable arrowBack;


    private Context context = this;
    private SectionPoliceReportPresenter presenter;

    private final static String URL_APP_STORE = "https://play.google.com/store/apps/details?id=com.keniobyte.bruino.minsegapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.section_police_report);
        ButterKnife.bind(this);

        toolbar.setTitle(titlePoliceReport);
        toolbar.setNavigationIcon(arrowBack);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        presenter = new SectionPoliceReportPresenter(context);
        presenter.addView(this);
        try {
            presenter.isCurrentVersion(getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgressBar() {
        checkingVersionProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        checkingVersionProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showListTypePoliceReport() {
        policeReportLinearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideListTypePoliceReport() {
        policeReportLinearLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void updateAppMessage() {
        new AlertDialog.Builder(context)
                .setTitle(titleWarning)
                .setMessage(messageUpdateApp)
                .setCancelable(false)
                .setPositiveButton(update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.onClickUpdate();
                    }
                })
                .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.onClickCancelUpdate();
                    }
                })
                .show();
    }

    @Override
    public void connectionErrorMessage() {
        new AlertDialog.Builder(context)
                .setTitle(titleError)
                .setMessage(messageConnectionError)
                .setCancelable(false)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void navigationToLocationPoliceReport(String typeReport) {
        startActivity(new Intent(context, LocationPoliceReportActivity.class)
                .putExtra("type_report", typeReport));
    }

    @Override
    public void navigationToAppStore() {
        startActivity(new Intent("android.intent.action.VIEW",
                Uri.parse(URL_APP_STORE)));
    }

    @OnClick({R.id.policeReportDrugsButton, R.id.policeReportAircraftButton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.policeReportDrugsButton:
                presenter.onClickPoliceReportDrugs();
                break;
            case R.id.policeReportAircraftButton:
                presenter.onClickPoliceReportAircraft();
                break;
        }
    }
}
