package com.keniobyte.bruino.minsegapp.features.police_report;

import android.content.Context;
import android.util.Log;

import com.keniobyte.bruino.minsegapp.model.PoliceReport;
import com.keniobyte.bruino.minsegapp.requestHandler.MinSegAppRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

/**
 * @author bruino
 * @version 02/01/17.
 */

public class PoliceReportInteractor implements IPoliceReportInteractor {
    private String TAG = getClass().getSimpleName();
    private Context context;

    public PoliceReportInteractor(Context context) {
        this.context = context;
    }

    @Override
    public void sendReportPolice(PoliceReport policeReport, final OnSendReportFinishedListener listener) {
        RequestParams report = new RequestParams();
        //Send user's anonymous id if it exists.
        if (getAnonymousId() != 0){
            report.put("anonymous_id", getAnonymousId());
        }
        //Send date(optional)
        if (policeReport.getIncidentDate() != null){
            report.put("incident_date", policeReport.getIncidentDate());
        }
        //Send picture(optional)
        if(policeReport.getListPathsAttachments().size() > 0){//TODO: changed to multiple attachedment
            try {
                File file = new File(policeReport.getListPathsAttachments().get(0));
                report.put("picture", file);
                report.put("file_name", file.getName());
            } catch (FileNotFoundException e) {
                Log.e(TAG, e.toString());
            }
        }
        //Send perpetrator(optional)
        if (policeReport.getNamePerpetrator()!= null){
            report.put("perpetrator", policeReport.getNamePerpetrator());
        }
        //Send report(required)
        report.put("incident_description", policeReport.getIncidentDescription());
        //Send coordinates(required)
        report.put("lat", policeReport.getLatitude());
        report.put("lng", policeReport.getLongitude());
        //Send address(required)
        report.put("address", policeReport.getIncidentAddress());//TODO: verify word
        //Report type
        report.put("report_type", policeReport.getTypePoliceReport());

        MinSegAppRestClient.post( "/endpoint/call/json/anonymous_report", report, new JsonHttpResponseHandler(){
            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                int progress = (int) ((bytesWritten * 100) / totalSize);
                listener.onProgress(progress);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    switch (response.getString("status")){
                        case "ok":
                            // If an id is returned, it means is the first time the user sends and alert.
                            // id is stored to keep track of user reports without exposing his identity.
                            if (!response.isNull("anonymous_id")){
                                context.getSharedPreferences("minsegapp", Context.MODE_PRIVATE)
                                        .edit()
                                        .putInt("anonymous_id", response.getInt("anonymous_id"))
                                        .apply();
                            }
                            listener.onSuccess();
                            break;

                        case "cool_down":
                            listener.reportPoliceCoolDown();
                            break;
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG, errorResponse.toString());
                listener.sendReportPoliceError();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, responseString);
                listener.sendReportPoliceError();
            }
        });
    }

    private int getAnonymousId(){
        return context.getSharedPreferences("minsegapp", Context.MODE_PRIVATE).getInt("anonymous_id", 0);
    }
}
