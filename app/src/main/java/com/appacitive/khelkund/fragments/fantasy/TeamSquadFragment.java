package com.appacitive.khelkund.fragments.fantasy;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.activities.fantasy.EditTeamActivity;
import com.appacitive.khelkund.adapters.SquadAdapter;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.model.KhelkundUser;
import com.appacitive.khelkund.model.Team;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;

public class TeamSquadFragment extends Fragment {

    @InjectView(R.id.rv_squad)
    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;

    @InjectView(R.id.fab_edit)
    public FloatingActionButton mFab;

    private KhelkundUser mUser;
    private Team mTeam;

    public TeamSquadFragment() {
        // Required empty public constructor
    }

    public static TeamSquadFragment newInstance() {
        return new TeamSquadFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_team_squad, container, false);
        ButterKnife.inject(this, rootView);
        String userId = SharedPreferencesManager.ReadUserId();
        StorageManager storageManager = new StorageManager();
        mTeam = storageManager.GetTeam(userId);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(mTeam.getName());
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AlphaInAnimationAdapter(new SquadAdapter(mTeam.getPlayers(), mTeam.getCaptainId()));
        mRecyclerView.setAdapter(mAdapter);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editTeamIntent = new Intent(getActivity(), EditTeamActivity.class);
                startActivity(editTeamIntent);
                getActivity().overridePendingTransition(R.anim.slide_in_right_fast, R.anim.slide_out_left_fast);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (isVisibleToUser && mFab != null) {
                    new ShowcaseView.Builder(getActivity())
                            .setTarget(new ViewTarget(mFab))
                            .setContentTitle("Click here to edit your team")
                            .hideOnTouchOutside()
                            .singleShot(22)
                            .build().hideButton();
                }
            }
        };
        new Handler().postDelayed(runnable, 500);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
