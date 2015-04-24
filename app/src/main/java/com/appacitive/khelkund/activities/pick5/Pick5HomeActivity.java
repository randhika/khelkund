package com.appacitive.khelkund.activities.pick5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.adapters.Pick5Adapter;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.infra.ConnectionManager;
import com.appacitive.khelkund.infra.SnackBarManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.services.FetchAllPick5MatchesIntentService;
import com.appacitive.khelkund.model.Match;
import com.appacitive.khelkund.model.events.MatchSelectedEvent;
import com.squareup.otto.Subscribe;

import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.wasabeef.recyclerview.animators.adapters.SlideInLeftAnimationAdapter;

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
        ConnectionManager.checkNetworkConnectivity(this);
        StorageManager storageManager = new StorageManager();
        mMatches = storageManager.GetAllMatches();

        if(mMatches.size() == 0) {
            SnackBarManager.showMessage("Match data could not be made available at the moment. Please try again later", this);
            Intent mPick5ServiceIntent = new Intent(this, FetchAllPick5MatchesIntentService.class);
            startService(mPick5ServiceIntent);
        }

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Pick5Adapter scheduleAdapter = new Pick5Adapter(mMatches);
        mAdapter = new SlideInLeftAnimationAdapter(scheduleAdapter);
        mRecyclerView.setAdapter(mAdapter);
        int position = mMatches.size();

        for (int i = 1; i < mMatches.size(); i++) {
            if (mMatches.get(i).getStartDate().after(new Date())) {
                position = i;
                break;
            }
        }
        mRecyclerView.scrollToPosition(position);
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
    public void onMatchSelected(MatchSelectedEvent event) {
        Intent intent = new Intent(this, Pick5MatchActivity.class);
        intent.putExtra("match_id", event.MatchId);
        startActivity(intent);
    }
}
