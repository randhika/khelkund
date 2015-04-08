package com.appacitive.khelkund.navigationdrawer;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.adapters.Pick5Adapter;
import com.appacitive.khelkund.adapters.ScheduleAdapter;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.model.Match;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.wasabeef.recyclerview.animators.adapters.SlideInLeftAnimationAdapter;

public class ScheduleActivity extends ActionBarActivity {

    @InjectView(R.id.rv_schedule)
    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;

    private List<Match> mMatches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.inject(this);
        StorageManager storageManager = new StorageManager();
        mMatches = storageManager.GetAllMatches();

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(mMatches);
        mAdapter = new SlideInLeftAnimationAdapter(scheduleAdapter);
        mRecyclerView.setAdapter(mAdapter);
        int position = 0;

        for(int i = 1 ; i < mMatches.size(); i++)
        {
            if(mMatches.get(i).getStartDate().after(new Date()) && mMatches.get(i-1).getStartDate().before(new Date()))
                position = i;


        }
        mRecyclerView.smoothScrollToPosition(position);
    }



}
