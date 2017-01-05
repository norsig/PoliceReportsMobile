package com.keniobyte.bruino.minsegapp.features.section_nav_drawer_police_report;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.keniobyte.bruino.minsegapp.model.PoliceReport;
import com.keniobyte.bruino.minsegapp.requestHandler.MinSegAppSingleton;
import com.keniobyte.bruino.minsegapp.ui.base.BasePresenter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author bruino
 * @version 05/01/17.
 */

public class SectionPoliceReportPresenter extends BasePresenter<SectionPoliceReportActivity> implements ISectionPoliceReportPresenter {
    private SectionPoliceReportActivity sectionPoliceReportView;
    private Context context;

    public SectionPoliceReportPresenter(Context context) {
        this.context = context;
    }

    public void addView(SectionPoliceReportActivity sectionPoliceReportView){
        this.sectionPoliceReportView = sectionPoliceReportView;
    }

    @Override
    public void isCurrentVersion(int version) throws JSONException {
        sectionPoliceReportView.showProgressBar();
        sectionPoliceReportView.hideListTypePoliceReport();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST
                , MinSegAppSingleton.URL_BASE + "/endpoint/call/json/check_update"
                , new JSONObject().put("version", version)
                , new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i(sectionPoliceReportView.getClass().getSimpleName(), "status version: " + response.getString("status"));
                            if (response.getString("status").equals("outdated")){
                                sectionPoliceReportView.updateAppMessage();
                            } else {
                                sectionPoliceReportView.hideProgressBar();
                                sectionPoliceReportView.showListTypePoliceReport();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(sectionPoliceReportView.getClass().getSimpleName(), error.toString());
                        sectionPoliceReportView.connectionErrorMessage();
                    }
                });
        MinSegAppSingleton.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public void onClickPoliceReportAircraft() {
        sectionPoliceReportView.navigationToLocationPoliceReport(PoliceReport.TYPE_POLICE_REPORT_AIRCRAFT);
    }

    @Override
    public void onClickPoliceReportDrugs() {
        sectionPoliceReportView.navigationToLocationPoliceReport(PoliceReport.TYPE_POLICE_REPORT_DRUGS);
    }

    @Override
    public void onClickUpdate() {
        sectionPoliceReportView.navigationToAppStore();
    }

    @Override
    public void onClickCancelUpdate() {
        sectionPoliceReportView.finish();
    }
}
