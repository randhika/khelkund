package com.appacitive.khelkund.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.activities.PlayerPoolActivity;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_action_filter:
                showFilterDialog();
                return true;
            default:
                break;
        }
        return false;
    }



    List<Integer> selectedItemsIndexes;
    private void showFilterDialog() {
        selectedItemsIndexes = new ArrayList<Integer>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        boolean[] isSelectedArray = {false, false, false, false, false, false, false, false};
        builder.setTitle("Choose teams");
        List<String> teams = ((PlayerPoolActivity) getActivity()).allTeams;
        String[] teamsToShow = teams.toArray(new String[teams.size()]);
        builder.setMultiChoiceItems(teamsToShow, isSelectedArray, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                if (isChecked)
                    selectedItemsIndexes.add(which);
                else if (selectedItemsIndexes.contains(which))
                    selectedItemsIndexes.remove(Integer.valueOf(which));
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("FILTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                List<String> selectedTeams = new ArrayList<String>();
                for (int index = 0; index < selectedItemsIndexes.size(); index++) {
                    selectedTeams.add(((PlayerPoolActivity)getActivity()).allTeams.get(selectedItemsIndexes.get(index)));
                }
                ((PlayerPoolActivity) getActivity()).chosenTeams = selectedTeams;
            }
        });
        builder.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player_pool, container, false);
        ButterKnife.inject(this, rootView);
        setHasOptionsMenu(true);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PlayerPoolAdapter(mPlayers);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }
}
