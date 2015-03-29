package com.appacitive.khelkund.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.model.Player;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PlayerDetailsActivity extends ActionBarActivity {

    @InjectView(R.id.player_toolbar)
    public Toolbar mToolbar;

    private String playerId;

    private Player mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_details);
        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);
        Intent intent = getIntent();
        playerId = intent.getStringExtra("player_id");
        fetchAndDisplayPlayerDetails();

    }

    private void fetchAndDisplayPlayerDetails()
    {
        Http http = new Http();
        http.get(Urls.PlayerUrls.getPlayerDetailsUrl(playerId), new HashMap<String, String>(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                if (result.optJSONObject("Error") != null)
                    return;
                mPlayer = new Player(result);
                DisplayPlayerDetails();
            }

            @Override
            public void failure(Exception e) {

            }
        });

    }

    private void DisplayPlayerDetails()
    {
        getSupportActionBar().setTitle(String.format("%s %s", mPlayer.getFirstName(), mPlayer.getLastName()));
        getSupportActionBar().setElevation(5);
        getSupportActionBar().setSubtitle("Subtitle");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.drawable.demo);

        mToolbar.setBackgroundColor(getResources().getColor(R.color.DD));
        mToolbar.setCollapsible(true);
        mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }


}
