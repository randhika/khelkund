package com.appacitive.khelkund.activities.navigationdrawer;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.adapters.ShareViaListAdapter;
import com.appacitive.khelkund.infra.BusProvider;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ShareReferralCodeActivity extends ActionBarActivity {

    private List<SharePackageDetails> mPackageDetails;

    @InjectView(R.id.tv_share_code)
    public TextView mCode;

    @InjectView(R.id.rv_share)
    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_referral_code);
        ButterKnife.inject(this);
        mPackageDetails = new ArrayList<SharePackageDetails>();
        getPackageNames();
        mLayoutManager = new GridLayoutManager(this, 3);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ShareViaListAdapter(mPackageDetails, this);
        mRecyclerView.setAdapter(mAdapter);
    }


    private void getPackageNames() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "test");
        sendIntent.setType("text/plain");
        List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(sendIntent, 0);

        PackageManager packageManager = getPackageManager();
        for (ResolveInfo resolveInfo : resolveInfoList) {
            {
                try {
                    SharePackageDetails details = new SharePackageDetails();
                    String packageName = resolveInfo.activityInfo.packageName;
                    details.packageName = packageName;
                    details.logo = packageManager.getApplicationIcon(details.packageName);
                    ApplicationInfo info = packageManager.getApplicationInfo(packageName, 0);
                    details.appName = packageManager.getApplicationLabel(info).toString();


                    mPackageDetails.add(details);
                } catch (Exception e) {
                    continue;
                }
            }
        }
    }

    @Subscribe
    public void onShareItemClick(String packageName)
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, String.format("Hey! Come join me play Khelkund and win attractive prizes. Use the code %s. Get the app here %s", mCode.getText(), getResources().getString(R.string.SHORT_APP_URL)));
        sendIntent.setType("text/plain");
        sendIntent.setPackage(packageName);
        startActivity(sendIntent);
    }

    public class SharePackageDetails {
        public String packageName;
        public String appName;
        public Drawable logo;
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }
}
