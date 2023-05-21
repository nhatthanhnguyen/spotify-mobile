package com.ptit.spotify.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
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

    private Context context;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editConfirmPassword;
    private SessionManager session;
    private ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setControl();
        context = this;
        session = new SessionManager(this);

        init();

    }

    void setControl() {
        btnBack = findViewById(R.id.btnBack);
    }

    private void init() {
        editEmail = (EditText) findViewById(R.id.edit_register_mail);
        editPassword = (EditText) findViewById(R.id.edit_register_password);
        editConfirmPassword = (EditText) findViewById(R.id.edit_register_confirm_password);
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
        //TODO: in backend send register request using json body, now using form-data => isHandle = false
        boolean isHandle = false;
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();
        if (validateInput(email, password, confirmPassword)) {
            if(isHandle) {
                registerProcess(email, password, confirmPassword, new VolleyCallback() {
                    @Override
                    public void handleCallback() {
                        Toast.makeText(context, "Register Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, StartActivity.class);
                        startActivity(intent);
                        session.setLogin(true);
                    }
                }, new VolleyCallback() {
                    @Override
                    public void handleCallback() {
                        Toast.makeText(context, "Register Fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            Toast.makeText(context, "Register Success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, StartActivity.class);
            startActivity(intent);
            session.setLogin(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REGISTER && resultCode == Activity.RESULT_OK) {
            String userName = data.getStringExtra(KEY_USER_FROM_REGISTER);
            editEmail.setText(userName);
            editPassword.requestFocus();
        }
    }
    private boolean validateInput(String userName, String password, String confirmPassword) {
        if (TextUtils.isEmpty(userName)) {
            editEmail.requestFocus();
            editEmail.setError(context.getResources().getString(R.string.error_field_required));
            return false;
        }
        if (!Helper.isValidEmailAddress(userName)) {
            editEmail.requestFocus();
            editEmail.setError(context.getResources().getString(R.string.error_email_wrong_format));
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            editPassword.requestFocus();
            editPassword.setError(context.getResources().getString(R.string.error_field_required));
            return false;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            editConfirmPassword.requestFocus();
            editConfirmPassword.setError(context.getResources().getString(R.string.error_field_required));
            return false;
        }
        if(!password.equals(confirmPassword)) {
            editConfirmPassword.requestFocus();
            editConfirmPassword.setError(context.getResources().getString(R.string.error_password_not_match));
            return false;
        }
        return true;
    }

    private void registerProcess(String email, String password, String confirmPassword, final VolleyCallback success, final VolleyCallback err) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", email);
            jsonBody.put("password", password);
            jsonBody.put("confirm_password", confirmPassword);
            final String mRequestBody = jsonBody.toString();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.getRegisterEndpoint(), new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("LOG_RESPONSE", String.valueOf(response));
                    success.handleCallback();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_RESPONSE", error.toString());
                    err.handleCallback();
                }
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