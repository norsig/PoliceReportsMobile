package com.keniobyte.bruino.minsegapp.features.section_list_wanted.wanted_report;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.network.MinSegAppSingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author bruino
 * @version 12/01/17.
 */

public class WantedReportInteractor implements IWantedReportInteractor {
    private final String TAG = getClass().getSimpleName();
    private Context context;

    public WantedReportInteractor(Context context) {
        this.context = context;
    }

    @Override
    public void sendWantedReport(JSONObject wantedReport, final OnSendReportFinishedListener listener) throws JSONException {
        /*if (getAnonymousId() != 0){
            wantedReport.put("anonymous_id", getAnonymousId());
        }*/

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, MinSegAppSingleton.URL_BASE + context.getResources().getString(R.string.url_wanted_report)
                , wantedReport
                , new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            switch (response.getString("status")){
                                case "ok":
                                    //TODO: To Implement in MinSegBe.
                                    // If an id is returned, it means is the first time the user sends and alert.
                                    // id is stored to keep track of user reports without exposing his identity.
                                    /*if (!response.isNull("anonymous_id")){
                                        context.getSharedPreferences("minsegapp", Context.MODE_PRIVATE)
                                                .edit()
                                                .putInt("anonymous_id", response.getInt("anonymous_id"))
                                                .apply();
                                    }*/
                                    listener.onSuccess();
                                    break;

                                /*case "cool_down":
                                    listener.wantedReportCoolDown();
                                    break;*/
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        listener.sendWantedReportError();
                    }
                });
        MinSegAppSingleton.getInstance(context).addToRequestQueue(request);
    }

    /*private int getAnonymousId(){
        return context.getSharedPreferences("minsegapp", Context.MODE_PRIVATE).getInt("anonymous_id", 0);
    }*/
}
