package com.keniobyte.bruino.minsegapp.ui.navegationDrawer;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.keniobyte.bruino.minsegapp.features.location_police_report.LocationPoliceReportActivity;
import com.keniobyte.bruino.minsegapp.model.PoliceReport;
import com.keniobyte.bruino.minsegapp.requestHandler.MinSegAppSingleton;
import com.keniobyte.bruino.minsegapp.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Police reports
 *
 *      Police report: it aims to improve or completely replace citizen's reports. Once the user sends a report,
 *      he has to concur to the corresponding police station within 72hs to validate its statement. Fields:
 *       - Crime location (required)
 *       - Perpetrator's name (required)
 *       - Description(required)
 *       - Picture attachment/evidence (optional)
 *       - Crime date and time(required)
 *
 *      Internal Affairs Report: Report police corruption anonymously. Fields:
 *       - Crime location (required)
 *       - Perpetrator's name (required)
 *       - Description(required)
 *       - Picture attachment/evidence (optional)
 *       - Crime date and time(required)
 *
 *      Anonymous Drug Report. Fields:
 *       - Crime location (required)
 *       - Perpetrator's name (optional)
 *       - Description(required)
 *       - Picture attachment/evidence (optional)
 *       - Crime date and time(optional)
 */
public class ItemPoliceReportActivity extends ActionBarActivity {

    private final String TAG = getClass().getSimpleName();

    public static final int TYPE_ONLINE_REPORT = 1;
    public static final int TYPE_ANONYMOUS_REPORT = 2;
    public static final int TYPE_INTERNAL_AFFAIRS_REPORT = 3;
    public static final int TYPE_AIRCRAFT_POLICE_REPORT = 4;

    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private Context mContext = this;

    public ItemPoliceReportActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_police_report);
        setToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progressIndeterminateCurrentVersion);
        progressBar.setVisibility(View.VISIBLE);

        linearLayout = (LinearLayout) findViewById(R.id.viewListPoliceReport);
        linearLayout.setVisibility(View.INVISIBLE);


        isCurrentVersion();

        Button buttonAircraft  = (Button) findViewById(R.id.btnAnonymousPoliceReport);
        buttonAircraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LocationPoliceReportActivity.class);
                intent.putExtra("type_report", PoliceReport.TYPE_POLICE_REPORT_DRUGS);
                startActivity(intent);
            }
        });

        Button mButtonAnonymusDrugReport = (Button) findViewById(R.id.buttonAircraft);
        mButtonAnonymusDrugReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LocationPoliceReportActivity.class);
                intent.putExtra("type_report", PoliceReport.TYPE_POLICE_REPORT_AIRCRAFT);
                startActivity(intent);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle(getResources().getString(R.string.section_police_report));
        setSupportActionBar(toolbar);
    }

    private void isCurrentVersion() {
        try {
            Log.i(TAG, String.valueOf(getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0).versionCode));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        try {
            MinSegAppSingleton.getInstance(mContext).addToRequestQueue(
                    new JsonObjectRequest(Request.Method.POST,
                            MinSegAppSingleton.URL_BASE+"/endpoint/call/json/check_update",
                            new JSONObject().put("version", getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0).versionCode),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i(TAG, response.toString());
                                    try {
                                        if (response.getString("status").equals("outdated")){
                                            AlertDialog dialogUpdateApp = new AlertDialog.Builder(mContext)
                                                    .setTitle(getResources().getString(R.string.title_attention))
                                                    .setMessage(getResources().getString(R.string.message_not_current_version))
                                                    .setPositiveButton(getResources().getString(R.string.button_update), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            try {
                                                                startActivity(new Intent("android.intent.action.VIEW",
                                                                        Uri.parse("https://play.google.com/store/apps/details?id=com.keniobyte.bruino.minsegapp")));
                                                                ((Activity) mContext).finish();
                                                            }catch(Exception e) {
                                                                Toast.makeText(getApplicationContext(),"Unable to Connect Try Again...",
                                                                        Toast.LENGTH_LONG).show();
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    })
                                                    .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            ((Activity) mContext).finish();
                                                        }
                                                    })
                                                    .create();
                                            dialogUpdateApp.show();
                                        } else {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            linearLayout.setVisibility(View.VISIBLE);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    AlertDialog alertDialogError = new AlertDialog.Builder(mContext)
                                            .setTitle(getResources().getString(R.string.title_error))
                                            .setMessage(getResources().getString(R.string.message_error_current_version))
                                            .setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    ((Activity) mContext).finish();
                                                }
                                            }).create();
                                    alertDialogError.show();
                                }
                            }));
        } catch (JSONException | PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}