package com.keniobyte.bruino.minsegapp.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Volley singleton pattern.
 */
public class MinSegAppSingleton {

    private static MinSegAppSingleton singleton;
    private RequestQueue requestQueue;
    private static Context context;
    public static final String URL_BASE = "https://keniobyte.com/minsegbe";

    private MinSegAppSingleton(Context context){
        MinSegAppSingleton.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized MinSegAppSingleton getInstance(Context context){

        if (singleton == null){
            singleton = new MinSegAppSingleton(context);
        }

        return singleton;
    }

    public RequestQueue getRequestQueue() {

        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        return requestQueue;
    }

    public void addToRequestQueue(Request req){
        getRequestQueue().add(req);
    }
}
