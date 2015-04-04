package com.appacitive.khelkund.activities.pick5;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.fragments.Pick5FinishedFragment;
import com.appacitive.khelkund.fragments.Pick5PlayFragment;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.model.Match;

import butterknife.ButterKnife;

public class Pick5MatchActivity extends ActionBarActivity {

    private Match mMatch;
    private StorageManager storageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick5_match);
        ButterKnife.inject(this);
        String matchId = getIntent().getStringExtra("match_id");
        storageManager = new StorageManager();
        mMatch = storageManager.GetMatch(matchId);
        if (mMatch.getMatchStatus() == 0) {
            Pick5PlayFragment playPick6Fragment = new Pick5PlayFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, playPick6Fragment).commit();

        }
        if(mMatch.getMatchStatus() == 1)
        {
            Pick5FinishedFragment emptyPick5Fragment = new Pick5FinishedFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, emptyPick5Fragment).commit();
        }


    }


}
