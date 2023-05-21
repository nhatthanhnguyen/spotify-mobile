package com.ptit.spotify.utils;


import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class HttpUtils {

    private static final String TAG = "HttpUtils";
    private static HttpUtils sInstance;
    private RequestQueue mRequestQueue;

    private HttpUtils(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public static synchronized HttpUtils getInstance(Context context) {
        if (sInstance == null)
            sInstance = new HttpUtils(context);
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

}
