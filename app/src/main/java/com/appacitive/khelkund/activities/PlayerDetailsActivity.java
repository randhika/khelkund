package com.appacitive.khelkund.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.ConnectionManager;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.model.Player;
import com.appacitive.khelkund.model.Statistics;
import com.appacitive.khelkund.model.TeamHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PlayerDetailsActivity extends ActionBarActivity {

    @InjectView(R.id.player_toolbar)
    public Toolbar mToolbar;

    private String playerId;

    private Player mPlayerFromServer;
    private Player mPlayerFromDb;

    @InjectView(R.id.iv_details_photo)
    public ImageView mPlayerPhoto;

    @InjectView(R.id.tv_details_1)
    public TextView mDetails1;

    @InjectView(R.id.tv_label_details_1)
    public TextView mLabelDetail1;

    @InjectView(R.id.tv_details_2)
    public TextView mDetails2;

    @InjectView(R.id.tv_label_details_2)
    public TextView mLabelDetail2;

    @InjectView(R.id.tv_details_3)
    public TextView mDetails3;

    @InjectView(R.id.tv_label_details_3)
    public TextView mLabelDetail3;

    @InjectView(R.id.btn_details_remove)
    public Button mRemove;

    @InjectView(R.id.btn_make_captain)
    public Button mMakeCaptain;

    @InjectView(R.id.tv_details_player_name)
    public TextView mName;

    @InjectView(R.id.chart)
    public LineChart mChart;

    @InjectView(R.id.tv_details_price)
    public TextView mPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player_details);
        ButterKnife.inject(this);
        ConnectionManager.checkNetworkConnectivity(this);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.abc_ic_clear_mtrl_alpha);
        mToolbar.setCollapsible(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        Intent intent = getIntent();
        playerId = intent.getStringExtra("player_id");
        boolean isCaptain = intent.getBooleanExtra("is_captain", false);
        if(isCaptain == true)
            mMakeCaptain.setEnabled(false);

        StorageManager manager = new StorageManager();
        mPlayerFromDb = manager.GetPlayer(playerId);
        DisplayPlayerBasicDetails();
        InitializeChart();
        FetchAndDisplayPlayerStatistics();
        mMakeCaptain.setOnClickListener(makeCaptainListener);
        mRemove.setOnClickListener(removePlayerListener);
    }

    public View.OnClickListener makeCaptainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("captain_id", mPlayerFromDb.getId());
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    };

    public View.OnClickListener removePlayerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("remove_id", mPlayerFromDb.getId());
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    };

    private void FetchAndDisplayPlayerStatistics() {
        Http http = new Http();
        http.get(Urls.PlayerUrls.getPlayerDetailsUrl(playerId), new HashMap<String, String>(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                if (result.optJSONObject("Error") != null)
                    return;
                mPlayerFromServer = new Player(result);
                DisplayStatistics();
            }

            @Override
            public void failure(Exception e) {

            }
        });

    }

    private void InitializeChart() {

        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setEnabled(false);
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(4);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        ArrayList<String> oppositions = new ArrayList<String>();
        ArrayList<Entry> entries = new ArrayList<Entry>();

        oppositions.add("");
        entries.add(new Entry(Float.valueOf(mPlayerFromDb.getPointsHistory1()), 0));

        oppositions.add("");
        entries.add(new Entry(Float.valueOf(mPlayerFromDb.getPointsHistory2()), 1));

        oppositions.add("");
        entries.add(new Entry(Float.valueOf(mPlayerFromDb.getPointsHistory3()), 2));

        oppositions.add("");
        entries.add(new Entry(Float.valueOf(mPlayerFromDb.getPointsHistory4()), 3));

        oppositions.add("");
        entries.add(new Entry(Float.valueOf(mPlayerFromDb.getPointsHistory5()), 4));

        final LineDataSet dataSet = new LineDataSet(entries, "points");
        dataSet.setColor(getResources().getColor(R.color.primary));
        dataSet.setCircleSize(5);
        dataSet.setCircleColorHole(getResources().getColor(R.color.primary));
        dataSet.setCircleColor(getResources().getColor(R.color.primary));
        dataSet.setLineWidth(3);
        LineData lineData = new LineData(oppositions, new ArrayList<LineDataSet>() {{
            add(dataSet);
        }});
        mChart.setData(lineData);

    }

    private void DisplayPlayerBasicDetails() {

        mName.setText(mPlayerFromDb.getDisplayName());
        mToolbar.setBackgroundResource(TeamHelper.getTeamColor(mPlayerFromDb.getShortTeamName()));
        Picasso.with(this).load(mPlayerFromDb.getImageUrl()).fit().centerInside().into(mPlayerPhoto);
        mPrice.setText("$ " + String.valueOf(mPlayerFromDb.getPrice()));
    }

    private void DisplayStatistics()
    {
        List<String> statistics = new ArrayList<String>();
        Statistics playerStats = mPlayerFromServer.getStatistics();
        statistics.add("Matches");
        statistics.add(String.valueOf(playerStats.getMatchesPlayed()));

        if (mPlayerFromServer.getType().equals("AllRounder")) {
            statistics.add("Runs");
            statistics.add(String.valueOf(playerStats.getRunsScored()));
            statistics.add("Wickets");
            statistics.add(String.valueOf(playerStats.getWickets()));

        }

        if (mPlayerFromServer.getType().equals("Batsman") || mPlayerFromServer.getType().equals("WicketKeeper")) {
            statistics.add("Runs");
            statistics.add(String.valueOf(playerStats.getRunsScored()));
            statistics.add("Strike Rate");
            statistics.add(String.valueOf(playerStats.getStrikeRate()));

        }

        if (mPlayerFromServer.getType().equals("Bowler")) {
            statistics.add("Wickets");
            statistics.add(String.valueOf(playerStats.getWickets()));
            statistics.add("Economy");
            statistics.add(String.valueOf(playerStats.getEconomy()));

        }

        mLabelDetail1.setText(statistics.get(0));
        mLabelDetail2.setText(statistics.get(2));
        mLabelDetail3.setText(statistics.get(4));

        mDetails1.setText(statistics.get(1));
        mDetails2.setText(statistics.get(3));
        mDetails3.setText(statistics.get(5));
    }
}
