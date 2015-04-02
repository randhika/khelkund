package com.appacitive.khelkund.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.adapters.LeaderboardAdapter;
import com.appacitive.khelkund.adapters.SquadAdapter;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.ConnectionManager;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.SnackBarManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.model.LeaderboardScore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LeaderboardActivity extends ActionBarActivity {

    private Context mContext;
    private List<LeaderboardScore> mScores;
    private ProgressDialog mProgressDialog;

    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        ConnectionManager.checkNetworkConnectivity(this);

        mContext = this;
        mScores = new ArrayList<LeaderboardScore>();
        FetchLeaderBoard();
    }

    private void FetchLeaderBoard() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Fetching Leaderboard");
        mProgressDialog.show();
        Http http = new Http();
        mUserId = SharedPreferencesManager.ReadUserId();
        http.get(Urls.LeaderboardUrls.getLeaderboardUrl(mUserId), new HashMap<String, String>(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                mProgressDialog.dismiss();
                if (result.optJSONObject("Error") != null) {
                    SnackBarManager.showError(result.optJSONObject("Error").optString("ErrorMessage"), LeaderboardActivity.this);
                    return;
                }

                JSONArray scores = result.optJSONArray("TeamScores");
                if (scores != null) {
                    for (int i = 0; i < scores.length(); i++) {
                        LeaderboardScore s = new LeaderboardScore(scores.optJSONObject(i));
                        LeaderboardActivity.this.mScores.add(s);
                    }
                    PopulateView();
                }
            }

            @Override
            public void failure(Exception e) {
                mProgressDialog.dismiss();
                SnackBarManager.showError("Unable to fetch leaderboard at the moment", LeaderboardActivity.this);
            }
        });
    }

    private void PopulateView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_leaderboard);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(LeaderboardActivity.this);
        ((LinearLayoutManager)mLayoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new LeaderboardAdapter(mScores, mUserId);
        mRecyclerView.setAdapter(mAdapter);
    }


}
