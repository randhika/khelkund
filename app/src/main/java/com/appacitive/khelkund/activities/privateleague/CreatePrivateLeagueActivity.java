package com.appacitive.khelkund.activities.privateleague;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.ConnectionManager;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SnackBarManager;
import com.appacitive.khelkund.infra.Urls;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CreatePrivateLeagueActivity extends ActionBarActivity {

    private EditText mName;
    private EditText mPassword;
    private Button mConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_private_league);
        ConnectionManager.checkNetworkConnectivity(this);
        mName = (EditText) findViewById(R.id.create_name);
        mPassword = (EditText) findViewById(R.id.create_password);
        mConfirm = (Button) findViewById(R.id.create_confirm);
        mConfirm.setOnClickListener(confirmCreateLeagueClick);
    }

    View.OnClickListener confirmCreateLeagueClick = new View.OnClickListener() {
        public void onClick(View v) {
            if(mName.getText().toString().isEmpty())
            {
                mName.setError("Private League should have a name.");
                YoYo.with(Techniques.Shake).playOn(mName);
                return;
            }
            if(mPassword.getText().toString().isEmpty())
            {
                mPassword.setError("Private League should have a password.");
                YoYo.with(Techniques.Shake).playOn(mPassword);
                return;
            }
            CreateLeague();
        }
    };

    private void CreateLeague()
    {
        JSONObject payload = new JSONObject();
        try {
            payload.put("PrivateLeagueName", mName.getText().toString());
            payload.put("Password", mPassword.getText().toString());
        }
        catch (JSONException e)
        {
            throw new RuntimeException(e.getMessage());
        }
        Http http = new Http();
        http.post(Urls.PrivateLeagueUrls.getCreatePrivateLeaguesUrl(), new HashMap<String, String>(), payload, new APCallback() {
            @Override
            public void success(JSONObject result) {

                Intent privateLeagueHomeIntent = new Intent(CreatePrivateLeagueActivity.this, PrivateLeagueHomeActivity.class);
                startActivity(privateLeagueHomeIntent);
            }

            @Override
            public void failure(Exception e) {
                SnackBarManager.showError("Unable to create private league at the moment.", CreatePrivateLeagueActivity.this);
            }
        });
    }
}
