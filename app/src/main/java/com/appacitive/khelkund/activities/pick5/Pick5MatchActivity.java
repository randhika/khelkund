package com.appacitive.khelkund.activities.pick5;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.fragments.Pick5FinishedFragment;
import com.appacitive.khelkund.fragments.Pick5PlayFragment;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.ConnectionManager;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.SnackBarManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.model.Match;
import com.appacitive.khelkund.model.Pick5MatchDetails;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;

public class Pick5MatchActivity extends ActionBarActivity {

    private ProgressDialog mDialog;
    private Pick5MatchDetails mDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick5_match);
        ButterKnife.inject(this);
        ConnectionManager.checkNetworkConnectivity(this);
        String matchId = getIntent().getStringExtra("match_id");
        String userId = SharedPreferencesManager.ReadUserId();

            Pick5PlayFragment playPick6Fragment = Pick5PlayFragment.newInstance(mDetails);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, playPick6Fragment).commit();


//        fetchMatchDetails(userId, matchId);

        //  if match is over and user did not play
//        if(mDetails.getMatchDetails().getMatchStatus() == 2 && mDetails.getPlayers().size() == 0)
//        {
//            Pick5FinishedFragment emptyPick5Fragment = new Pick5FinishedFragment();
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.fragment_container, emptyPick5Fragment).commit();
//        }
//
//        //  any other circumstance
//        if (mDetails.getMatchDetails().getMatchStatus() == 0) {
//            Pick5PlayFragment playPick6Fragment = Pick5PlayFragment.newInstance(mDetails);
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.fragment_container, playPick6Fragment).commit();
//
//        }
    }

    private void fetchMatchDetails(String userId, String matchId)
    {
        Http http = new Http();
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Fetching match details");
        mDialog.show();
        http.get(Urls.Pick5Urls.getMatchesDetailsUrl(userId, matchId), new HashMap<String, String>(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                mDialog.dismiss();
                if (result.optJSONObject("Error") != null) {
                    SnackBarManager.showError(result.optJSONObject("Error").optString("ErrorMessage"), Pick5MatchActivity.this);
                    return;
                }
                mDetails = new Pick5MatchDetails(result);
            }

            @Override
            public void failure(Exception e) {
                mDialog.dismiss();
                SnackBarManager.showError("Something went wrong.", Pick5MatchActivity.this);
            }
        });
    }


}
