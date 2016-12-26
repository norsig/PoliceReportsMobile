package com.keniobyte.bruino.minsegapp.features.police_report;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.keniobyte.bruino.minsegapp.requestHandler.MinSegAppRestClient;
import com.keniobyte.bruino.minsegapp.utils.RealPathUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.content.ContentResolver.SCHEME_CONTENT;

/**
 * Created by bruino on 12/12/16.
 */

public class PoliceReportInteractor implements IPoliceReportInteractor {
    private String TAG = getClass().getSimpleName();
    private Context context;

    public PoliceReportInteractor(Context context) {
        this.context = context;
    }

    @Override
    public void sendReportPolice(String perpetrator, String incidentDate, String incidentDescriptor, ArrayList<Uri> arrayUri
            , Double latitude, Double longitude, String address, String typeReport, final OnSendReportFinishedListener listener) {
        RequestParams report = new RequestParams();
        //Send user's anonymous id if it exists.
        if (getAnonymousId() != 0){
            report.put("anonymous_id", getAnonymousId());
        }
        //Send date(optional)
        if (incidentDate != null){
            report.put("incident_date", incidentDate);
        }
        //Send picture(optional)
        if(arrayUri != null){//TODO: changed to multiple attachedment
            if(arrayUri.get(0).getScheme().equals(SCHEME_CONTENT)){
                try {
                    report.put("file_name", context.getContentResolver().openInputStream(arrayUri.get(0)));
                    report.put("picture", arrayUri.get(0).getLastPathSegment());
                } catch (FileNotFoundException e) {
                    Log.e(TAG, e.toString());
                }
            } else {
                try {
                    File file = new File(getRealPathImage(arrayUri.get(0)));
                    report.put("picture", file);
                    report.put("file_name", file.getName());
                } catch (FileNotFoundException e) {
                    Log.e(TAG, e.toString());
                }
            }
        }
        //Send perpetrator(optional)
        if (perpetrator != null){
            report.put("perpetrator", perpetrator);
        }
        //Send report(required)
        report.put("incident_description", incidentDate);
        //Send coordinates(required)
        report.put("lat", latitude);
        report.put("lng", longitude);
        //Send address(required)
        report.put("address", address);
        //Report type
        report.put("report_type", typeReport);

        MinSegAppRestClient.post( "/endpoint/call/json/anonymous_report", report, new JsonHttpResponseHandler(){
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

    private String getRealPathImage(Uri uri){
        // SDK < API11
        if (Build.VERSION.SDK_INT < 11)
            return RealPathUtil.getRealPathFromURI_BelowAPI11(context, uri);
            // SDK >= 11 && SDK < 19
        else if (Build.VERSION.SDK_INT < 19)
            return RealPathUtil.getRealPathFromURI_API11to18(context, uri);
            // SDK > 19 (Android 4.4)
        else
            return RealPathUtil.getRealPathFromURI_API19(context, uri);
    }
}
