package com.appacitive.khelkund.activities.navigationdrawer;

import android.app.ProgressDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.adapters.ShareViaListAdapter;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.SnackBarManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.model.KhelkundUser;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ShareReferralCodeActivity extends ActionBarActivity {

    private List<SharePackageDetails> mPackageDetails;
    private Intent mIntent;

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

        String userId = SharedPreferencesManager.ReadUserId();
        StorageManager manager = new StorageManager();
        KhelkundUser user = manager.GetUser(userId);


        //  for existing users without referral code in db
        if(user != null && TextUtils.isEmpty(user.getReferralCode()))
        {
            fetchUserFromServer(userId);
        }

        else if(user != null && TextUtils.isEmpty(user.getReferralCode()) == false)
            mCode.setText(user.getReferralCode());

    }

    private void fetchUserFromServer(final String userId) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching referral code");
        dialog.show();
        Http http = new Http();
        http.get(Urls.UserUrls.getUserUrl(userId), new HashMap<String, String>(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                if (result.optJSONObject("Error") != null) {
                    SnackBarManager.showError(result.optJSONObject("Error").optString("ErrorMessage"), ShareReferralCodeActivity.this);
                    return;
                }
                KhelkundUser user = new KhelkundUser(result.optJSONObject("User"));
                StorageManager storageManager = new StorageManager();
                storageManager.SaveUser(user);

                mCode.setText(user.getReferralCode());
            }

            @Override
            public void failure(Exception e) {
                SnackBarManager.showError("Unable to fetch your referral code at the moment.", ShareReferralCodeActivity.this);
            }
        });
    }

    private void getPackageNames() {
        mIntent = new Intent();
        mIntent.setAction(Intent.ACTION_SEND);
        mIntent.putExtra(Intent.EXTRA_TEXT, String.format("Hey! Come join me play Khelkund and win attractive prizes. Use my referral code %s. Get the app here %s", mCode.getText(), getResources().getString(R.string.SHORT_APP_URL)));
        mIntent.setType("text/plain");
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(mIntent, PackageManager.GET_RESOLVED_FILTER);

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
    public void onShareItemClick(String packageName) {
        mIntent.setPackage(packageName);
        mIntent.putExtra(Intent.EXTRA_TEXT, String.format("Hey! Come join me play Khelkund and win attractive prizes. Use the code %s. Get the app here %s", mCode.getText(), getResources().getString(R.string.SHORT_APP_URL)));
        startActivity(mIntent);
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
