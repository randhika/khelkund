package com.appacitive.khelkund.activities.pick5;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.adapters.Pick5Adapter;
import com.appacitive.khelkund.adapters.SquadAdapter;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.model.Match;
import com.appacitive.khelkund.model.events.MatchSelectedEvent;
import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Pick5HomeActivity extends ActionBarActivity {

    @InjectView(R.id.rv_pick5)
    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;

    private List<Match> mMatches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick5_home);
        ButterKnife.inject(this);
        StorageManager storageManager = new StorageManager();
        mMatches = storageManager.GetAllMatches();

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new Pick5Adapter(mMatches);
        mRecyclerView.setAdapter(mAdapter);
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

    @Subscribe
    public void onMatchSelected(MatchSelectedEvent event)
    {
        Intent intent = new Intent(this, Pick5MatchActivity.class);
        intent.putExtra("match_id", event.MatchId);
        startActivity(intent);
    }
}
