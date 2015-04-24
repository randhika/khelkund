package com.appacitive.khelkund.activities.navigationdrawer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.appacitive.khelkund.R;

import java.util.Locale;

public class HowToPlayActivity extends ActionBarActivity implements ActionBar.TabListener {

    SectionsPagerAdapter mSectionsPagerAdapter;

    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);

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
            return WebViewFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "FANTASY CRICKET";
                case 1:
                    return "PICK'EM 5";
                case 2:
                    return "PRIVATE LEAGUE";
            }
            return null;
        }
    }

    public static class WebViewFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        public int sectionNumber = 0;

        public static WebViewFragment newInstance(int sectionNumber) {
            WebViewFragment fragment = new WebViewFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            fragment.sectionNumber = sectionNumber;
            return fragment;
        }

        public WebViewFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_how_to_play_activity, container, false);
            WebView mWebView = (WebView) rootView.findViewById(R.id.web_howtoplay);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setClickable(true);

            String path = "file:///android_asset/fantasy_league.html";
            if(sectionNumber == 1)
                path = "file:///android_asset/fantasy_league.html";
            if(sectionNumber == 2)
                path = "file:///android_asset/pick_5.html";
            if(sectionNumber == 3)
                path = "file:///android_asset/private_league.html";
            mWebView.loadUrl(path);
            mWebView.setBackgroundColor(Color.TRANSPARENT);
            return rootView;
        }
    }

}
