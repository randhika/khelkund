package com.appacitive.khelkund.activities.pick5;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.adapters.Pick5Adapter;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.infra.SnackBarManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.services.FetchAllPick5MatchesIntentService;
import com.appacitive.khelkund.model.Match;
import com.appacitive.khelkund.model.events.MatchSelectedEvent;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public class Pick5ListActivity extends ActionBarActivity implements ActionBar.TabListener {

    SectionsPagerAdapter mSectionsPagerAdapter;

    ViewPager mViewPager;

    private List<Match> openMatches;
    private List<Match> completedMatches;
    private List<Match> upcomingMatches;

    private List<Match> mMatches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick5_list);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        fetchMatches();

    }

    private void fetchMatches() {
        mMatches = new ArrayList<Match>();
        openMatches = new ArrayList<Match>();
        completedMatches = new ArrayList<Match>();
        upcomingMatches = new ArrayList<Match>();

        StorageManager storageManager = new StorageManager();
        mMatches = storageManager.GetAllMatches();

        if (mMatches.size() == 0) {
            SnackBarManager.showMessage("Match data could not be made available at the moment. Please try again later", this);
            Intent mPick5ServiceIntent = new Intent(this, FetchAllPick5MatchesIntentService.class);
            startService(mPick5ServiceIntent);
            return;
        }

        for (Match match : mMatches) {
            if (match.getMatchStatus() == 2 || match.getMatchStatus() == 1) {
                completedMatches.add(match);
                continue;
            }
            int diffInDays = (int) ((match.getStartDate().getTime() - (new Date()).getTime()) / (1000 * 60 * 60 * 24));

            if(match.isOpen() == true)
                openMatches.add(match);
            else upcomingMatches.add(match);
//            if (diffInDays > 2)
//            {
//                upcomingMatches.add(match);
//            }
//            else openMatches.add(match);
        }
    }

    @Subscribe
    public void onMatchSelected(MatchSelectedEvent event) {
        Intent intent = new Intent(this, Pick5MatchActivity.class);
        intent.putExtra("match_id", event.MatchId);
        startActivity(intent);
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                {
                    return Pick5ListFragment.newInstance(openMatches);
                }
                case 1:
                {
                    return Pick5ListFragment.newInstance(upcomingMatches);
                }
                default :
                {
                    return Pick5ListFragment.newInstance(completedMatches);
                }
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "OPEN";
                case 1:
                    return "UPCOMING";
                case 2:
                    return "COMPLETED";
            }
            return null;
        }
    }

    public static class Pick5ListFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        @InjectView(R.id.rv_pick5)
        public RecyclerView mRecyclerView;
        public RecyclerView.Adapter mAdapter;
        public RecyclerView.LayoutManager mLayoutManager;

        private List<Match> matches;

        public static Pick5ListFragment newInstance(List<Match> matches) {
            Pick5ListFragment fragment = new Pick5ListFragment();
            fragment.matches = matches;
            return fragment;
        }

        public Pick5ListFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pick5_list, container, false);
            ButterKnife.inject(this, rootView);

            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);

            Pick5Adapter scheduleAdapter = new Pick5Adapter(matches);
            mAdapter = new ScaleInAnimationAdapter(scheduleAdapter);
            mRecyclerView.setAdapter(mAdapter);

            return rootView;
        }
    }

}
