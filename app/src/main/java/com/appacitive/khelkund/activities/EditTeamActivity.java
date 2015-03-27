package com.appacitive.khelkund.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.adapters.PlayerCardAdapter;
import com.appacitive.khelkund.infra.VerticallyWrappedGridLayoutManager;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.model.Team;
import com.appacitive.khelkund.model.User;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EditTeamActivity extends ActionBarActivity {

    private User mUser;
    private Team mTeam;
    private StorageManager mStorageManager;

    @InjectView(R.id.tv_formation)
    public TextView mFormation;

    @InjectView(R.id.tv_autoselect)
    public TextView mAutoSelect;

    @InjectView(R.id.tv_change_formation)
    public TextView mChangeFormation;

    @InjectView(R.id.tv_balance)
    public TextView mBalance;

    @InjectView(R.id.tv_transfers)
    public TextView mTransfers;

    @InjectView(R.id.tv_points)
    public TextView mPoints;

    @InjectView(R.id.tv_batsman_count)
    public TextView mBatsmanCount;

    @InjectView(R.id.tv_bowler_count)
    public TextView mBowlerCount;

    @InjectView(R.id.tv_allrounder_count)
    public TextView mAllRounderCount;

    @InjectView(R.id.tv_wicketkeeper_count)
    public TextView mWicketKeeperCount;

    @InjectView(R.id.rv_batsman)
    public RecyclerView mBatsmanRecyclerView;

    @InjectView(R.id.rv_bowler)
    public RecyclerView mBowlerRecyclerView;

    @InjectView(R.id.rv_allrounder)
    public RecyclerView mAllRounderRecyclerView;

    @InjectView(R.id.rv_wicketkeeper)
    public RecyclerView mWicketKeeperRecyclerView;

    public RecyclerView.LayoutManager mBatsmenLayoutManager = null;
    public RecyclerView.Adapter mBatsmenAdapter;

    public RecyclerView.LayoutManager mBowlersLayoutManager = null;
    public RecyclerView.Adapter mBowlersAdapter;

    public RecyclerView.LayoutManager mAllRoundersLayoutManager = null;
    public RecyclerView.Adapter mAllRoundersAdapter;

    public RecyclerView.LayoutManager mWicketKeepersLayoutManager = null;
    public RecyclerView.Adapter mWicketKeepersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_team);
        ButterKnife.inject(this);
        mStorageManager = new StorageManager();
        String userId = SharedPreferencesManager.ReadUserId();
        mUser = mStorageManager.GetUser(userId);
        mTeam = mStorageManager.GetTeam(userId);
        updateStats();

        String[] urls = new String[]{
                "http://iplstatic.s3.amazonaws.com/players/210/1.png",
                "http://iplstatic.s3.amazonaws.com/players/210/1.png",
                "http://iplstatic.s3.amazonaws.com/players/210/94.png",
                "http://iplstatic.s3.amazonaws.com/players/210/227.png",
                "http://iplstatic.s3.amazonaws.com/players/210/96.png"
        };
        mBatsmanRecyclerView.setHasFixedSize(true);
        mBatsmenLayoutManager = new VerticallyWrappedGridLayoutManager(this, 3);
        mBatsmanRecyclerView.setLayoutManager(mBatsmenLayoutManager);
        mBatsmenAdapter = new PlayerCardAdapter(urls);
        mBatsmanRecyclerView.setAdapter(mBatsmenAdapter);

        mBowlerRecyclerView.setHasFixedSize(true);
        mBowlersLayoutManager = new VerticallyWrappedGridLayoutManager(this, 3);
        mBowlerRecyclerView.setLayoutManager(mBowlersLayoutManager);
        mBowlersAdapter = new PlayerCardAdapter(urls);
        mBowlerRecyclerView.setAdapter(mBowlersAdapter);

        mAllRounderRecyclerView.setHasFixedSize(true);
        mAllRoundersLayoutManager = new VerticallyWrappedGridLayoutManager(this, 3);
        mAllRounderRecyclerView.setLayoutManager(mAllRoundersLayoutManager);
        mAllRoundersAdapter = new PlayerCardAdapter(urls);
        mAllRounderRecyclerView.setAdapter(mAllRoundersAdapter);

        mWicketKeeperRecyclerView.setHasFixedSize(true);
        mWicketKeepersLayoutManager = new VerticallyWrappedGridLayoutManager(this, 3);
        mWicketKeeperRecyclerView.setLayoutManager(mWicketKeepersLayoutManager);
        mWicketKeepersAdapter = new PlayerCardAdapter(urls);
        mWicketKeeperRecyclerView.setAdapter(mWicketKeepersAdapter);

//        mChangeFormation.setOnClickListener(formationChangeClickListener());
//        mAutoSelect.setOnClickListener(autoSelectClickListener());

    }



    private void updateStats()
    {
        mBalance.setText(String.valueOf(mTeam.getBalance()));
        mTransfers.setText(String.valueOf(mTeam.getTransfersRemaining()));
        mPoints.setText(String.valueOf(mTeam.getTotalPoints()));
        String[] formation = mTeam.getFormation().split("_");
        mFormation.setText(String.format("Your team formation is set to %s BTSM, %s BWLR, %s AR and %s WK.", formation[1], formation[2], formation[3], formation[4]));
    }


}
