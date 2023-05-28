package com.ptit.spotify.data;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ptit.spotify.dto.model.Song;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.utils.HttpUtils;
import com.ptit.spotify.utils.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SongData {
    public static void GetAllSong(Context context, final VolleyCallback success, final VolleyCallback err) {
        JSONObject jsonBody = new JSONObject();
        final String mRequestBody = jsonBody.toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.getAddSongEndpoint(), new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("LOG_RESPONSE", String.valueOf(response));
                Gson gson = new Gson();
                JSONArray items = response.optJSONArray("songs");
                if(items != null) {
                    for(int i = 0; i < items.length(); i++) {
                        allSong.add(gson.fromJson(items.toString(), Song.class));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_RESPONSE", error.toString());
                err.handleCallback();
            }
        });
        HttpUtils.getInstance(context).getRequestQueue().add(jsonObjectRequest);
    }
}
