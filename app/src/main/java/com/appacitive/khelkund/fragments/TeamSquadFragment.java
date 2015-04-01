package com.appacitive.khelkund.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.activities.EditTeamActivity;
import com.appacitive.khelkund.adapters.SquadAdapter;
import com.appacitive.khelkund.infra.DividerItemDecoration;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.model.Team;
import com.appacitive.khelkund.model.KhelkundUser;
import com.getbase.floatingactionbutton.FloatingActionButton;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
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
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(mTeam.getName());
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SquadAdapter(mTeam.getPlayers());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.abc_list_divider_mtrl_alpha)));

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editTeamIntent = new Intent(getActivity(), EditTeamActivity.class);
                startActivity(editTeamIntent);
            }
        });

        return rootView;
    }


}
