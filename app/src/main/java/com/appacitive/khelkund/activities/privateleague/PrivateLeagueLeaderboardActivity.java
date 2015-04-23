package com.appacitive.khelkund.activities.privateleague;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.adapters.LeaderboardAdapter;
import com.appacitive.khelkund.adapters.LeaderboardTeamAdapter;
import com.appacitive.khelkund.adapters.PrivateLeagueLeaderboardAdapter;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.SnackBarManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.infra.widgets.carousel.CoverFlowCarousel;
import com.appacitive.khelkund.model.PrivateLeague;
import com.appacitive.khelkund.model.Team;
import com.appacitive.khelkund.model.events.LeaderboardItemClickedEvent;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.adapters.SlideInLeftAnimationAdapter;

public class PrivateLeagueLeaderboardActivity extends ActionBarActivity {

    @InjectView(R.id.tv_private_league_leaderboard_header)
    public TextView mHeader;

    @InjectView(R.id.fab_share)
    public FloatingActionButton mShare;

    @InjectView(R.id.rv_privateleague_leaderboard)
    public RecyclerView mRecyclerView;

    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;

    private PrivateLeague mLeague;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_league_leaderboard);
        ButterKnife.inject(this);
        String leagueId = getIntent().getExtras().getString("league_id");
        StorageManager manager = new StorageManager();
        mLeague = manager.GetPrivateLeague(leagueId, SharedPreferencesManager.ReadUserId());

        mHeader.setText(mLeague.getName());

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager)mLayoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SlideInLeftAnimationAdapter(new PrivateLeagueLeaderboardAdapter(mLeague, SharedPreferencesManager.ReadUserId()));
        mRecyclerView.setAdapter(mAdapter);

        showTutorialOverlay();
    }

    private void showTutorialOverlay() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                new ShowcaseView.Builder(PrivateLeagueLeaderboardActivity.this)
                        .setTarget(new ViewTarget(mShare))
                        .setContentText("Share the invite code with your friends and compete with them.")
                        .setContentTitle("Invite your friends")
                        .hideOnTouchOutside()
                        .singleShot(997)
                        .build().hideButton();
            }
        };
        new Handler().postDelayed(runnable, 1000);

    }

    @OnClick(R.id.fab_share)
    public void onShareClick()
    {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Invite your friends");
        dialog.show();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, String.format("Hey! Join my private IPL fantasy league on Khelkund. Use the code %s. Get the app here %s", mLeague.getCode(), getResources().getString(R.string.SHORT_APP_URL)));
        sendIntent.setType("text/plain");
        dialog.dismiss();
        startActivity(Intent.createChooser(sendIntent, String.format("Invite friends to %s using", mLeague.getName())));
    }

    @Subscribe
    public void onLeaderboardItemClicked(LeaderboardItemClickedEvent event)
    {
        fetchTeamAndDisplay(event.UserId);
    }

    private void fetchTeamAndDisplay(final String userId) {
        Http http = new Http();
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Fetching team");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        http.get(Urls.TeamUrls.getMyTeamUrl(userId), new HashMap<String, String>(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                mProgressDialog.dismiss();
                if (result.optJSONObject("Error") != null) {
                    SnackBarManager.showError(result.optJSONObject("Error").optString("ErrorMessage"), PrivateLeagueLeaderboardActivity.this);
                    return;
                }
                if (result.optString("Id") != null) {

                    Team mTeam = new Team(result);
                    mTeam.setUserId(userId);
                    Dialog mDialog = new Dialog(PrivateLeagueLeaderboardActivity.this, R.style.Base_Theme_AppCompat_Dialog);
                    mDialog.setContentView(R.layout.layout_leaderboard_team_dialog);
                    TextView mName = (TextView) mDialog.findViewById(R.id.tv_leaderboard_team_name);
                    mName.setText(mTeam.getName());
                    CoverFlowCarousel carousel = (CoverFlowCarousel) mDialog.findViewById(R.id.carousel);
                    final LeaderboardTeamAdapter adapter = new LeaderboardTeamAdapter(PrivateLeagueLeaderboardActivity.this, mTeam.getPlayers());
                    carousel.setAdapter(adapter);
                    mDialog.show();
                }
            }

            @Override
            public void failure(Exception e) {
                mProgressDialog.dismiss();
                SnackBarManager.showError("Something went wrong", PrivateLeagueLeaderboardActivity.this);
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
}
