package com.appacitive.khelkund.activities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.appacitive.khelkund.R;

import org.w3c.dom.Text;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LicencesActivity extends ActionBarActivity {

    @InjectView(R.id.tv_licences_app_version)
    public TextView mAppVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licences);
        ButterKnife.inject(this);
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        mAppVersion.setText(version);
    }


}
