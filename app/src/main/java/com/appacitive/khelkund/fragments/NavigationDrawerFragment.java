package com.appacitive.khelkund.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.activities.LoginActivity;
import com.appacitive.khelkund.activities.navigationdrawer.FeedbackActivity;
import com.appacitive.khelkund.activities.navigationdrawer.HowToPlayActivity;
import com.appacitive.khelkund.activities.navigationdrawer.LeaderboardActivity;
import com.appacitive.khelkund.activities.navigationdrawer.LicencesActivity;
import com.appacitive.khelkund.activities.navigationdrawer.PrizesActivity;
import com.appacitive.khelkund.activities.navigationdrawer.ScheduleActivity;
import com.appacitive.khelkund.activities.navigationdrawer.ScoringChartActivity;
import com.appacitive.khelkund.infra.KhelkundApplication;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.model.KhelkundUser;
import com.appacitive.khelkund.model.Team;
import com.digits.sdk.android.Digits;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class NavigationDrawerFragment extends Fragment {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    private NavigationDrawerCallbacks mCallbacks;

    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    public NavigationDrawerFragment() {
    }

    @InjectView(R.id.tv_nav_logout)
    public TextView mLogout;

    @InjectView(R.id.tv_nav_leaderboard)
    public TextView mLeaderboard;

    @InjectView(R.id.tv_nav_terms)
    public TextView mTerms;

    @InjectView(R.id.tv_nav_name)
    public TextView mName;

    @InjectView(R.id.iv_nav_logo)
    public ImageView mLogo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        ButterKnife.inject(this, view);

        String userId = SharedPreferencesManager.ReadUserId();
        StorageManager manager = new StorageManager();
        KhelkundUser user = manager.GetUser(userId);

        if (user != null) {
            String name = user.getFirstName();
            if (user.getLastName() != null && user.getLastName().equals("null") == false)
                name += " " + user.getLastName();
            mName.setText(name);

        }

        Team team = manager.GetTeam(userId);
        if (team != null) {
            int bitmapId = KhelkundApplication.getAppContext().getResources().getIdentifier(team.getImageName(), "drawable", KhelkundApplication.getAppContext().getPackageName());
            if (bitmapId > 0)
                Picasso.with(getActivity()).load(bitmapId).into(mLogo);
        }

        return view;
    }

    @OnClick(R.id.rl_leaderboard)
    public void onLeaderBoardClick() {
        Intent intent = new Intent(getActivity(), LeaderboardActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.rl_prizes)
    public void onPrizesCLick() {
        Intent intent = new Intent(getActivity(), PrizesActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.rl_howtoplay)
    public void onHowToPlayClick() {
        Intent intent = new Intent(getActivity(), HowToPlayActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.rl_scoring)
    public void onScoringChartClick() {
        Intent intent = new Intent(getActivity(), ScoringChartActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.rl_terms)
    public void onTermsClick() {
        Uri uri = Uri.parse("http://www.khelkund.com/ipl/terms.aspx");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @OnClick(R.id.rl_licenses)
    public void onLicensesClick() {
        Intent intent = new Intent(getActivity(), LicencesActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.rl_schedule)
    public void onScheduleClick() {
        Intent intent = new Intent(getActivity(), ScheduleActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.rl_feedback)
    public void onFeedbackClick() {
        Intent feedBackIntent = new Intent(getActivity(), FeedbackActivity.class);
        startActivity(feedBackIntent);
    }

    @OnClick(R.id.rl_logout)
    public void onLogoutClick() {
        LoginManager.getInstance().logOut();
        Twitter.logOut();
        Digits.getSessionManager().clearActiveSession();
        String userId = SharedPreferencesManager.ReadUserId();
        if (userId == null)
            return;
        StorageManager manager = new StorageManager();
        manager.deleteUser(userId);
        SharedPreferencesManager.RemoveUserId();
        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        startActivity(loginIntent);
        getActivity().finish();
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu();
            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mDrawerLayout != null && isDrawerOpen()) {
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    public static interface NavigationDrawerCallbacks {
        void onNavigationDrawerItemSelected(int position);
    }
}
