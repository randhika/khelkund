package com.appacitive.khelkund.activities.misc;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.adapters.LeaderboardAdapter;
import com.appacitive.khelkund.adapters.LeaderboardTeamAdapter;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.infra.ConnectionManager;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.SnackBarManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.infra.widgets.carousel.Carousel;
import com.appacitive.khelkund.model.LeaderboardScore;
import com.appacitive.khelkund.model.Team;
import com.appacitive.khelkund.model.events.LeaderboardItemClickedEvent;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_leaderboard);

        ConnectionManager.checkNetworkConnectivity(this);

        mContext = this;
        mScores = new ArrayList<LeaderboardScore>();
        FetchLeaderBoard();
    }

    private void FetchLeaderBoard() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Fetching Global Leaderboard");
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
        mAdapter = new AlphaInAnimationAdapter(new LeaderboardAdapter(mScores, mUserId));
        mRecyclerView.setAdapter(mAdapter);
    }



    @Subscribe
    public void onLeaderboardItemClicked(LeaderboardItemClickedEvent event)
    {
        fetchTeamAndDisplay(event.UserId);
    }

    private void fetchTeamAndDisplay(final String userId) {
        Http http = new Http();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Fetching team");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        http.get(Urls.TeamUrls.getMyTeamUrl(userId), new HashMap<String, String>(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                mProgressDialog.dismiss();
                if (result.optJSONObject("Error") != null) {
                    SnackBarManager.showError(result.optJSONObject("Error").optString("ErrorMessage"), LeaderboardActivity.this);
                    return;
                }
                if (result.optString("Id") != null) {

                    Team mTeam = new Team(result);
                    mTeam.setUserId(userId);
                    Dialog mDialog = new Dialog(LeaderboardActivity.this, R.style.Base_Theme_AppCompat_Dialog);
                    mDialog.setContentView(R.layout.layout_leaderboard_team_dialog);
                    TextView mName = (TextView) mDialog.findViewById(R.id.tv_leaderboard_team_name);
                    mName.setText("Team " + mTeam.getName());
                    Carousel carousel = (Carousel) mDialog.findViewById(R.id.carousel);
                    carousel.setSpacing(1.1f);
                    final LeaderboardTeamAdapter adapter = new LeaderboardTeamAdapter(LeaderboardActivity.this, mTeam.getPlayers());
                    carousel.setAdapter(adapter);
                    mDialog.show();
                }
            }

            @Override
            public void failure(Exception e) {
                mProgressDialog.dismiss();
                SnackBarManager.showError("Something went wrong", LeaderboardActivity.this);
            }
        });
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left_fast, R.anim.slide_out_right_fast);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_left_fast, R.anim.slide_out_right_fast);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
