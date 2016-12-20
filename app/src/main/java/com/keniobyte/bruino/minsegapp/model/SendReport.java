package com.keniobyte.bruino.minsegapp.model;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.keniobyte.bruino.minsegapp.MainActivity;
import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.requestHandler.MinSegAppRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 *  Send reports about:
 *      - Wanted
 *      - Missing
 *      - Police reports
 *      - Anonymous drug reports
 *      - Anonymous corruption reports
 */
public class SendReport {

    private Context context;
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;
    private RequestParams requestParams;

    public static final String URL_REPORT_MISSING = "/endpoint/call/json/missing_data";
    public static final String URL_REPORT_WANTED = "/endpoint/call/json/wanted_data";
    public static final String URL_REPORT = "/endpoint/call/json/anonymous_report";
    public static final String URL_BASE = "https://keniobyte.com/minsegbe";

    public SendReport(RequestParams requestParams, Context context){
        this.requestParams = requestParams;
        this.context = context;
    }

    public void sendPoliceReport(){
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        MinSegAppRestClient.post(URL_REPORT, requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.i(TAG, "Response Success : "+response.toString());
                progressDialog.dismiss();
                try {
                    switch (response.getString("status")){
                        case "ok":
                            Log.i("send_report: ", "ok");
                            // If an id is returned, it means is the first time the user sends and alert.
                            // id is stored to keep track of user reports without exposing his identity.
                            if (!response.isNull("anonymous_id")){
                                SharedPreferences preferences = context.getSharedPreferences("minsegapp", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt("anonymous_id", response.getInt("anonymous_id"));
                                editor.apply();
                            }

                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getResources().getString(R.string.accept)
                                    , new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            alertDialog.dismiss();
                                            Intent intent = new Intent(context, MainActivity.class);
                                            // Deletes activities history and returns to home screen
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            context.startActivity(intent);
                                        }
                                    });
                            alertDialog.setMessage(context.getResources().getString(R.string.send_success));
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.show();
                            break;

                        case "cool_down":
                            Log.i("send_report: ", "cool_down");
                            alertDialog.setCancelable(true);
                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // :(
                                }
                            });
                            alertDialog.setMessage(context.getResources().getString(R.string.msj_cool_down));
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.show();
                            break;

                        case "error":
                            Log.i("send_report: ", "error");
                            alertDialog.setCancelable(true);
                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // :(
                                }
                            });
                            alertDialog.setMessage(context.getResources().getString(R.string.send_failure));
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.show();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressDialog.dismiss();
                alertDialog.setMessage(context.getResources().getString(R.string.send_failure));
                alertDialog.setCancelable(true);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressDialog.dismiss();
                alertDialog.setMessage(context.getResources().getString(R.string.send_failure));
                alertDialog.setCancelable(true);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        });
    }

    public void setProgressDialog(ProgressDialog progressDialog){
        this.progressDialog = progressDialog;
    }

    public void setAlertDialog(final AlertDialog alertDialog){
        this.alertDialog = alertDialog;
    }
}