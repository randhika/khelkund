package com.appacitive.khelkund.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.KhelkundApplication;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.model.History;
import com.appacitive.khelkund.model.Team;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamOverviewFragment extends Fragment {

    @InjectView(R.id.chart)
    public LineChart mChart;

    @InjectView(R.id.tv_overview_total_points)
    public TextView mTotalPoints;

    @InjectView(R.id.tv_overview_previouspoints)
    public TextView mPreviousMatchPoints;

    @InjectView(R.id.tv_overview_against)
    public TextView mPreviousMatchAgainst;

    @InjectView(R.id.iv_overview_emblem)
    public ImageView mEmblem;

    private Team mTeam;

    public TeamOverviewFragment() {
        // Required empty public constructor
    }

    public static TeamOverviewFragment newInstance() {
        return new TeamOverviewFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_team_overview, container, false);
        ButterKnife.inject(this, rootView);
        String userId = SharedPreferencesManager.ReadUserId();
        StorageManager storageManager = new StorageManager();
        mTeam = storageManager.GetTeam(userId);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(mTeam.getName());
        int bitmapId  = KhelkundApplication.getAppContext().getResources().getIdentifier(mTeam.getImageName(), "drawable", KhelkundApplication.getAppContext().getPackageName());
        if(bitmapId > 0)
            Picasso.with(getActivity()).load(bitmapId).into(mEmblem);
        mTotalPoints.setText(String.valueOf(mTeam.getTotalPoints()));
        if(mTeam.getTeamHistory().size() != 0)
        {
            mPreviousMatchPoints.setText(String.valueOf(mTeam.getTeamHistory().last().getPoints()));
            mPreviousMatchAgainst.setText(" in " + mTeam.getTeamHistory().last().getOpposition());
        }
        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setEnabled(false);
        YAxis leftAxis = mChart.getAxisLeft();leftAxis.setLabelCount(4);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        ArrayList<String> oppositions = new ArrayList<String>();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        for(int i = 0 ; i < mTeam.getTeamHistory().size(); i++)
        {
            oppositions.add(mTeam.getTeamHistory().get(i).getOpposition());
            entries.add(new Entry(Float.valueOf(mTeam.getTeamHistory().get(i).getPoints()), i));
        }

        final LineDataSet dataSet = new LineDataSet(entries, "points");
        dataSet.setColor(getResources().getColor(R.color.accent));
        dataSet.setCircleSize(5);
        dataSet.setCircleColorHole(getResources().getColor(R.color.accent));
        dataSet.setCircleColor(getResources().getColor(R.color.accent));
        dataSet.setLineWidth(3);
        LineData lineData = new LineData(oppositions, new ArrayList<LineDataSet>(){{add(dataSet);}});
        mChart.setData(lineData);
        showOverlayTutorial();
        return rootView;
    }

    private void showOverlayTutorial() {


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                new ShowcaseView.Builder(getActivity())
                        .setTarget(ViewTarget.NONE)
                        .setContentText("Swipe left to view your squad")
                        .setContentTitle("Your team details appear here")
                        .hideOnTouchOutside()
                        .singleShot(11)
                        .build().hideButton();
            }
        };
        new Handler().postDelayed(runnable, 500);


    }


}
