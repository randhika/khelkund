package com.appacitive.khelkund.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.adapters.PlayerPoolAdapter;
import com.appacitive.khelkund.adapters.SquadAdapter;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.model.Player;
import com.appacitive.khelkund.model.PlayerType;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by sathley on 3/29/2015.
 */
public class PlayerPoolFragment extends Fragment {

    @InjectView(R.id.rv_pool)
    public RecyclerView mRecyclerView;

    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;

    private RealmResults<Player> mPlayers;
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static PlayerPoolFragment newInstance(int sectionNumber, PlayerType playerType) {
        PlayerPoolFragment fragment = new PlayerPoolFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        StorageManager storageManager = new StorageManager();
        switch (sectionNumber)
        {
            case 1 : {
                fragment.mPlayers = storageManager.GetAllPlayers(playerType.toString());
                break;
            }

            case 2 : {
                fragment.mPlayers = storageManager.GetTopPerformingPlayersByType(playerType.toString());
                break;
            }

            case 3 : {
                fragment.mPlayers = storageManager.GetBargainPlayersByType(playerType.toString());
                break;
            }
        }
        return fragment;
    }

    public PlayerPoolFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player_pool, container, false);
        ButterKnife.inject(this, rootView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PlayerPoolAdapter(mPlayers);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }
}
