package com.appacitive.khelkund.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.widget.EditText;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.ConnectionManager;
import com.appacitive.khelkund.infra.Http;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        ConnectionManager.checkNetworkConnectivity(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick(R.id.rl_login)
    public void onLoginCLick()
    {
        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
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

        Http http = new Http();


    }

}
