package com.appacitive.khelkund.activities.pick5;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.appacitive.khelkund.R;
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



    }


}
