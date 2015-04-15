package com.appacitive.khelkund.activities.privateleague;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.adapters.LeaderboardAdapter;
import com.appacitive.khelkund.adapters.PrivateLeagueLeaderboardAdapter;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.model.PrivateLeague;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.wasabeef.recyclerview.animators.adapters.SlideInLeftAnimationAdapter;

public class PrivateLeagueLeaderboardActivity extends ActionBarActivity {

    @InjectView(R.id.tv_private_league_leaderboard_header)
    public TextView mHeader;

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
        mLeague = manager.GetPrivateLeague(leagueId);

        mHeader.setText(mLeague.getName());

        
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager)mLayoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SlideInLeftAnimationAdapter(new PrivateLeagueLeaderboardAdapter(mLeague, SharedPreferencesManager.ReadUserId()));
        mRecyclerView.setAdapter(mAdapter);
    }
}
