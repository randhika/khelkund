package com.appacitive.khelkund.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.widget.EditText;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.fragments.LoginFragment;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.ConnectionManager;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.SnackBarManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.model.User;
import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RegisterActivity extends ActionBarActivity {


    @InjectView(R.id.et_register_name)
    public EditText mName;

    @InjectView(R.id.et_register_email)
    public EditText mEmail;

    @InjectView(R.id.et_register_password)
    public EditText mPassword;


    public ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        ConnectionManager.checkNetworkConnectivity(this);
    }

    @OnClick(R.id.rl_login)
    public void onLoginCLick()
    {
        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    @OnClick(R.id.btn_register)
    public void onRegister()
    {
        mName.setError(null);
        mEmail.setError(null);
        mPassword.setError(null);
        String name = mName.getText().toString();
        String email = mEmail.getText().toString();
        String password = mEmail.getText().toString();


        if(TextUtils.isEmpty(name))
        {
            mName.setError("Provide a proper name.");
            mName.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(email) || !isEmailValid(email))
        {
            mEmail.setError("Provide a proper email address.");
            mEmail.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(password) || !isPasswordValid(password))
        {
            mPassword.setError("Passwords should be at least 4 characters.");
            mPassword.requestFocus();
            return;
        }
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Registering new user");
        mProgressDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Email", mEmail.getText().toString());
            jsonObject.put("Password", mPassword.getText().toString());
            jsonObject.put("FirstName", mName.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Http http = new Http();
        http.post(Urls.UserUrls.getSignupUrl(), new HashMap<String, String>(), jsonObject, new APCallback() {
            @Override
            public void success(JSONObject result) {
                mProgressDialog.dismiss();
                if(result.optJSONObject("Error") != null)
                {
                    SnackBarManager.showError(result.optJSONObject("Error").optString("ErrorMessage"), RegisterActivity.this);
                    return;
                }
                User user = new User(result);
                SharedPreferencesManager.WriteUserId(user.getId());
                StorageManager manager = new StorageManager();
                manager.SaveUser(user);

                Intent homeIntent = new Intent(RegisterActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                finish();
            }

            @Override
            public void failure(Exception e) {
                mProgressDialog.dismiss();
                SnackBarManager.showError("Something went wrong", RegisterActivity.this);
            }
        });

    }

}
