package com.ptit.spotify.data;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.ptit.spotify.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Volley {
    private void volleyImageRequest(Context context) {
        ImageRequest imageRequest = new ImageRequest("http://2dev4u.com/wp-content/uploads/2016/10/take-a-selfie-with-js.png", new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                Log.e(TAG, "ImageRequest onResponse: " + response.toString());
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "ImageRequest onErrorResponse: " + error.getMessage());
            }
        });
        HttpUtils.getInstance(context).getRequestQueue().add(imageRequest);
    }

    private void volleyJsonArrayRequest(Context context) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://dev.2dev4u.com/news/api.php", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e(TAG, "JsonArrayRequest onResponse: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "JsonArrayRequest onErrorResponse: " + error.getMessage());
            }
        });
        HttpUtils.getInstance(context).getRequestQueue().add(jsonArrayRequest);
    }

    private void volleyJsonObjectRequest(Context context) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://api.ipify.org/?format=json", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "JsonObjectRequest onResponse: " + response.getString("ip").toString());
                            Gson gson = new Gson();
                            Person person = gson.fromJson(response.toString(), Person.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "JsonObjectRequest onErrorResponse: " + error.getMessage());
            }
        });
        HttpUtils.getInstance(context).getRequestQueue().add(jsonObjectRequest);
    }

    private void volleyStringRequest(Context context, final String name, final String email, final String avatar) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "http://dev.2dev4u.com/news/api.php?email=" + email,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "StringRequest onResponse: " + response.toString());
                        if (!response.equals("User doesn't exist")) {
                            // When user exists will return id_user
                            Log.e(TAG, "StringRequest onResponse: User is exists: id= " + response);
                        } else {
                            volleyPostStringRequest(context, name, email, avatar);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.getMessage());
            }
        });

        HttpUtils.getInstance(context).getRequestQueue().add(stringRequest);
    }

    //String request Method.POST
    private void volleyPostStringRequest(Context context, final String name, final String email, final String avatar) {
        StringRequest insertRequest = new StringRequest(Request.Method.POST, "http://dev.2dev4u.com/news/api.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "StringRequest Post" + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("name", name);
                params.put("avatar", avatar);

                return params;
            }
        };
        HttpUtils.getInstance(context).getRequestQueue().add(insertRequest);
    }
}
