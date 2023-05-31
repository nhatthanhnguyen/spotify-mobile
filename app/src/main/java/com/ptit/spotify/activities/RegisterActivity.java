package com.ptit.spotify.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ptit.spotify.R;
import com.ptit.spotify.helper.Helper;
import com.ptit.spotify.helper.SessionManager;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.utils.HttpUtils;
import com.ptit.spotify.utils.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String KEY_USER_TO_MAIN = "KEY_USER_TO_MAIN";
    public static final String KEY_PASSWORD_TO_MAIN = "KEY_PASSWORD_TO_MAIN";
    public static final String KEY_USER_FROM_REGISTER = "KEY_USER_FROM_REGISTER";
    public static final int REQUEST_CODE_REGISTER = 1;

    private EditText editEmail;
    private EditText editUsername;
    private EditText editPassword;
    private SessionManager session;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setControl();
        session = new SessionManager(this);

        init();

    }

    void setControl() {
        btnBack = findViewById(R.id.btnBack);
    }

    private void init() {
        editEmail = (EditText) findViewById(R.id.edit_register_mail);
        editUsername = (EditText) findViewById(R.id.edit_register_username);
        editPassword = (EditText) findViewById(R.id.edit_register_password);
        findViewById(R.id.btn_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_register:
                register();
                break;
            case R.id.btnBack:
                finish();
                break;
        }
    }

    private void register() {
        String email = editEmail.getText().toString().trim();
        String username = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        if (validateInput(email, username, password)) {
//            Bundle bundle = new Bundle();
//            bundle.putString("USERNAME", username);
//            Intent intent = new Intent(RegisterActivity.this, VerifyOtpActivity.class);
//            intent.putExtras(bundle);
//            startActivity(intent);
            registerProcess(username, email, password, new VolleyCallback() {
                @Override
                public void handleCallback() {
                    Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("USERNAME", username);
                    Intent intent = new Intent(RegisterActivity.this, VerifyOtpActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    session.setLogin(false);
                }
            }, new VolleyCallback() {
                @Override
                public void handleCallback() {
                    Toast.makeText(RegisterActivity.this, "Register Fail", Toast.LENGTH_SHORT).show();
                }
            });

//            Toast.makeText(context, "Register Success", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(context, StartActivity.class);
//            startActivity(intent);
//            session.setLogin(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REGISTER && resultCode == Activity.RESULT_OK) {
            String userName = data.getStringExtra(KEY_USER_FROM_REGISTER);
            editEmail.setText(userName);
            editUsername.requestFocus();
        }
    }

    private boolean validateInput(String email, String username, String password) {
        if (TextUtils.isEmpty(email)) {
            editEmail.requestFocus();
            editEmail.setError(this.getResources().getString(R.string.error_field_required));
            return false;
        }
        if (!Helper.isValidEmailAddress(email)) {
            editEmail.requestFocus();
            editEmail.setError(this.getResources().getString(R.string.error_email_wrong_format));
            return false;
        }
        if (TextUtils.isEmpty(username)) {
            editUsername.requestFocus();
            editUsername.setError(this.getResources().getString(R.string.error_field_required));
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            editPassword.requestFocus();
            editPassword.setError(this.getResources().getString(R.string.error_field_required));
            return false;
        }
        return true;
    }

    private void registerProcess(String username, String email, String password, final VolleyCallback success, final VolleyCallback err) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", username);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            final String mRequestBody = jsonBody.toString();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    Constants.getRegisterEndpoint(),
                    new JSONObject(),
                    response -> {
                        Log.i("LOG_RESPONSE", String.valueOf(response));
                        success.handleCallback();
                    },
                    error -> {
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