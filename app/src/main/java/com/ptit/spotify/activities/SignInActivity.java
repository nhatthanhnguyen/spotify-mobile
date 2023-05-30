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

import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ptit.spotify.R;
import com.ptit.spotify.dto.model.Account;
import com.ptit.spotify.helper.SessionManager;
import com.ptit.spotify.utils.Constants;
import com.ptit.spotify.utils.HttpUtils;
import com.ptit.spotify.utils.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String KEY_USER_TO_MAIN = "KEY_USER_TO_MAIN";
    public static final String KEY_PASSWORD_TO_MAIN = "KEY_PASSWORD_TO_MAIN";
    public static final String KEY_USER_FROM_REGISTER = "KEY_USER_FROM_REGISTER";
    public static final int REQUEST_CODE_REGISTER = 1;
    private Context context;
    private EditText editUserName;
    private EditText editPassword;
    private SessionManager session;
    private ImageButton btnBack;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setControl();
//        setAction();
        context = this;
        session = new SessionManager(this);

        if (session.isLoggedIn()) {
            Intent intent = new Intent(context, ContentActivity.class);
            startActivity(intent);
            finish();
        }
        init();
    }

    private void setControl() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            finish();
        });
        btnSignIn = findViewById(R.id.buttonSignIn);
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
        editUserName = findViewById(R.id.edit_login_username);
        editPassword = findViewById(R.id.edit_login_password);
        findViewById(R.id.buttonSignIn).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.buttonSignIn:
                login();
                break;
            case R.id.btnBack:
                finish();
        }
    }

    private void login() {
        String userName = editUserName.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        if (validateInput(userName, password)) {
            loginProcess(userName, password, () -> {
                Toast.makeText(SignInActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                session.setLogin(true);
            }, () -> Toast.makeText(SignInActivity.this, "Login Fail", Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REGISTER && resultCode == Activity.RESULT_OK) {
            String userName = data.getStringExtra(KEY_USER_FROM_REGISTER);
            editUserName.setText(userName);
            editPassword.requestFocus();
        }
    }

    private boolean validateInput(String userName, String password) {
        if (TextUtils.isEmpty(password)) {
            editPassword.requestFocus();
            editPassword.setError(context.getResources().getString(R.string.error_field_required));
            return false;
        }
        if (TextUtils.isEmpty(userName)) {
            editUserName.requestFocus();
            editUserName.setError(context.getResources().getString(R.string.error_field_required));
            return false;
        }
        return true;
    }

    private void loginProcess(String userName, String password, final VolleyCallback success, final VolleyCallback err) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", userName);
            jsonBody.put("password", password);
            final String mRequestBody = jsonBody.toString();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.getLoginEndpoint(),
                    new JSONObject(), response -> {
                try {
                    Gson gson = new Gson();
                    Account account = gson.fromJson(response.getString("account"), Account.class);
                    session.setUserId(account.getUser_id());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Log.i("LOG_RESPONSE", String.valueOf(response));
                success.handleCallback();
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