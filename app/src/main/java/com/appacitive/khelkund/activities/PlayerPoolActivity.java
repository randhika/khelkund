package com.appacitive.khelkund.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.fragments.PlayerPoolFragment;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.model.PlayerType;
import com.appacitive.khelkund.model.TeamHelper;
import com.appacitive.khelkund.model.events.FilledPlayerCardClickedEvent;
import com.appacitive.khelkund.model.events.PlayerChosenEvent;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.squareup.otto.Subscribe;

public class PlayerPoolActivity extends ActionBarActivity implements ActionBar.TabListener {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    private PlayerType mPlayerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_pool);
        final ActionBar actionBar = getSupportActionBar();
        setupActionBarTitle();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_player_pool, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void setupActionBarTitle()
    {
        final ActionBar actionBar = getSupportActionBar();
        String playerType = getIntent().getStringExtra("type");
        mPlayerType = PlayerType.valueOf(playerType.toUpperCase());
        actionBar.setTitle("Choose " + playerType);
    }

    private void showSearchFilterTutorialOverlay() {
        new ShowcaseView.Builder(this)
                .setTarget(new ViewTarget(findViewById(R.id.menu_action_filter)))
                .setContentTitle("You can filter on teams based on upcoming matches")
                .hideOnTouchOutside()
                .singleShot(44)
                .build().hideButton();
    }

    @Override
    public void onBackPressed()
    {
        setResult(RESULT_CANCELED, null);
        finish();
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
            return PlayerPoolFragment.newInstance(position + 1, mPlayerType);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ALL";
                case 1:
                    return "TOP PERFORMERS";
                case 2:
                    return "BARGAINS";
            }
            return null;
        }
    }

    @Subscribe
    public void onPlayerChosen(PlayerChosenEvent event)
    {
        Intent intent = new Intent();
        intent.putExtra("player_id", event.PlayerId);
        intent.putExtra("Type", mPlayerType.toString());
        if (getParent() == null) {
            setResult(Activity.RESULT_OK, intent);
        } else {
            getParent().setResult(Activity.RESULT_OK, intent);
        }
        setResult(RESULT_OK, intent);
        finish();
    }
}
