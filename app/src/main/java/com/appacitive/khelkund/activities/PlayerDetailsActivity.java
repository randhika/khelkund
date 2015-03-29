package com.appacitive.khelkund.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.appacitive.khelkund.model.TeamHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            getSupportActionBar().setLogo(new BitmapDrawable(bitmap));
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    private void DisplayPlayerDetails()
    {
        getSupportActionBar().setTitle(String.format("%s %s", mPlayer.getFirstName(), mPlayer.getLastName()));
        getSupportActionBar().setElevation(5);
        getSupportActionBar().setSubtitle(mPlayer.getShortTeamName());
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setLogo(drawableFromUrl(mPlayer.getImageUrl()));
        Picasso.with(this).load(mPlayer.getImageUrl()).into(target);
        mToolbar.setBackgroundColor(getResources().getColor(TeamHelper.getTeamColor(mPlayer.getShortTeamName())));
        mToolbar.setCollapsible(false);
        mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    public static Drawable drawableFromUrl(String url) {
        Bitmap x;
        InputStream input = null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            connection.connect();

        input = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(x);
    }


}
