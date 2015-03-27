package com.appacitive.khelkund.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.ConnectionManager;

public class RegisterActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ConnectionManager.checkNetworkConnectivity(this);
    }


}
