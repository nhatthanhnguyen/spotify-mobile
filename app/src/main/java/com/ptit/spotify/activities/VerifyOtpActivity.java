package com.ptit.spotify.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ptit.spotify.R;
import com.ptit.spotify.helper.SessionManager;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.utils.HttpUtils;
import com.ptit.spotify.utils.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class VerifyOtpActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String KEY_USER_TO_MAIN = "KEY_USER_TO_MAIN";
    public static final String KEY_PASSWORD_TO_MAIN = "KEY_PASSWORD_TO_MAIN";
    public static final String KEY_USER_FROM_REGISTER = "KEY_USER_FROM_REGISTER";
    public static final int REQUEST_CODE_REGISTER = 1;
    private EditText editOtp;
    private SessionManager session;
    private ImageButton btnBack;
    private Button btnVerify;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        setControl();
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        if (bundle != null) {
            username = bundle.getString("USERNAME");
        }
//        setAction();
        session = new SessionManager(this);

        if(session.isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        init();
    }

    private void setControl() {
        btnBack = findViewById(R.id.btnBack);
        btnVerify = findViewById(R.id.buttonSignIn);
    }

//    private void setAction() {
//        btnBack.setOnClickListener(view -> {
//            finish();
//        });
//        btnSignIn.setOnClickListener(view -> {
//            Intent intent = new Intent(this, ContentActivity.class);
//            startActivity(intent);
//            finish();
//        });
//    }

    private void init() {
        editOtp = findViewById(R.id.edit_login_username);
        findViewById(R.id.buttonVerify).setOnClickListener(this);
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.buttonVerify:
                verify();
                break;
            case R.id.btnBack:
                finish();
        }
    }
    private void verify() {
        String otp = editOtp.getText().toString().trim();
        if (validateInput(otp)) {
            verifyProcess(username, otp,  () -> {
                Toast.makeText(VerifyOtpActivity.this, "Verify Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, StartActivity.class);
                startActivity(intent);
                session.setLogin(false);
            }, () -> Toast.makeText(VerifyOtpActivity.this, "Verify Fail", Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REGISTER && resultCode == Activity.RESULT_OK) {
            String userName = data.getStringExtra(KEY_USER_FROM_REGISTER);
            editOtp.setText(userName);
        }
    }
    private boolean validateInput(String otp) {
        if (TextUtils.isEmpty(otp)) {
            editOtp.requestFocus();
            editOtp.setError(this.getResources().getString(R.string.error_field_required));
            return false;
        }
        return true;
    }

    private void verifyProcess(String userName, String otp, final VolleyCallback success, final VolleyCallback err) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", userName);
            jsonBody.put("otp", otp);
            final String mRequestBody = jsonBody.toString();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.getVerifyEndpoint(), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("LOG_RESPONSE", String.valueOf(response));
                    success.handleCallback();
                }
            }, error -> {
                Log.e("LOG_RESPONSE", error.toString());
                err.handleCallback();
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public byte[] getBody() {
                    return mRequestBody.getBytes(StandardCharsets.UTF_8);
                }
            };
            HttpUtils.getInstance(this).getRequestQueue().add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
            err.handleCallback();
        }
    }
}