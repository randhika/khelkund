package com.appacitive.khelkund.activities.privateleague;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.adapters.PrivateLeagueAdapter;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SnackBarManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.model.PrivateLeague;
import com.facebook.applinks.AppLinkData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bolts.AppLinks;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;

public class PrivateLeagueHomeActivity extends ActionBarActivity {

    @InjectView(R.id.rv_privateleague)
    public RecyclerView mRecyclerView;

    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;

    private List<PrivateLeague> mPrivateLeagues;
    private String mUserId;
    private ProgressDialog mProgress;
    private StorageManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_league_home);
        ButterKnife.inject(this);
        Uri targetUrl =
                AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Toast.makeText(this, targetUrl.toString(), Toast.LENGTH_LONG).show();
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        } else {
            AppLinkData.fetchDeferredAppLinkData(
                    this,
                    new AppLinkData.CompletionHandler() {
                        @Override
                        public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
                            //process applink data

                        }
                    });
        }
//        mUserId = SharedPreferencesManager.ReadUserId();
//        mManager = new StorageManager();
//        mPrivateLeagues = mManager.GetPrivateLeague(mUserId);
//        ConnectionManager.checkNetworkConnectivity(this);
//        if(mPrivateLeagues == null)
//            fetchAndDisplayPrivateLeague();
//        else displayPrivateLeagues();
    }

    private void displayPrivateLeagues() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SlideInBottomAnimationAdapter(new PrivateLeagueAdapter(mPrivateLeagues));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void fetchAndDisplayPrivateLeague() {
        Http http = new Http();
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Fetching private leagues");
        mProgress.show();
        http.get(Urls.PrivateLeagueUrls.getPrivateLeaguesUrl(mUserId), new HashMap<String, String>(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                mProgress.dismiss();
                if (result.optJSONObject("Error") != null) {
                    SnackBarManager.showError(result.optJSONObject("Error").optString("ErrorMessage"), PrivateLeagueHomeActivity.this);
                    return;
                }
                List<PrivateLeague> privateLeagues = new ArrayList<PrivateLeague>();
                JSONArray leaguesArray = result.optJSONArray("PrivateLeagues");
                if (leaguesArray != null) {
                    for (int i = 0; i < leaguesArray.length(); i++)
                        privateLeagues.add(new PrivateLeague(leaguesArray.optJSONObject(i)));
                }
                StorageManager mManager = new StorageManager();
                mManager.SavePrivateLeagues(privateLeagues);
                displayPrivateLeagues();
            }

            @Override
            public void failure(Exception e) {
                mProgress.dismiss();
                SnackBarManager.showError("Your private leagues could not be fetched from Khelkund at the moment. Try again later.", PrivateLeagueHomeActivity.this);
            }
        });
    }

    @OnClick(R.id.fab_create_private_league)
    public void onClickCreateLeagueButton() {
        Intent createPrivateLeagueIntent = new Intent(this, CreatePrivateLeagueActivity.class);
        startActivity(createPrivateLeagueIntent);
    }


}
