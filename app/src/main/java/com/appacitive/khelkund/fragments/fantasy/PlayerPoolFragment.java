package com.appacitive.khelkund.fragments.fantasy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.appacitive.khelkund.adapters.PlayerPoolAdapter;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.infra.KhelkundApplication;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.services.FetchAllPlayersIntentService;
import com.appacitive.khelkund.model.Player;
import com.appacitive.khelkund.model.PlayerType;
import com.appacitive.khelkund.model.events.FilterAppliedEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;

/**
 * Created by sathley on 3/29/2015.
 */
public class PlayerPoolFragment extends Fragment {

    @InjectView(R.id.rv_pool)
    public RecyclerView mRecyclerView;

    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;

    private List<Player> mPlayers;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int sectionNumber = 0;

    public static List<String> allTeams = new ArrayList<String>() {{
        add("KXIP");
        add("RR");
        add("DD");
        add("CSK");
        add("RCB");
        add("SRH");
        add("KKR");
        add("MI");
    }};

    public static List<String> chosenTeams = new ArrayList<String>(allTeams);

    public static boolean[] isSelectedArray = {true, true, true, true, true, true, true, true};

    public static PlayerPoolFragment newInstance(int sectionNumber, PlayerType playerType) {
        PlayerPoolFragment fragment = new PlayerPoolFragment();
        fragment.sectionNumber = sectionNumber;
        StorageManager storageManager = new StorageManager();
        switch (sectionNumber) {
            case 1: {
                fragment.mPlayers = storageManager.GetAllPlayers(playerType.toString());
                if(fragment.mPlayers == null || fragment.mPlayers.size() == 0)
                {
                    Intent mServiceIntent = new Intent(KhelkundApplication.getAppContext(), FetchAllPlayersIntentService.class);
                    KhelkundApplication.getAppContext().startService(mServiceIntent);
                }
                break;
            }

            case 2: {
                fragment.mPlayers = storageManager.GetTopPerformingPlayersByType(playerType.toString());
                break;
            }

            case 3: {
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

    private void showFilterDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose teams");

        List<String> teams = allTeams;
        String[] teamsToShow = teams.toArray(new String[teams.size()]);

        builder.setMultiChoiceItems(teamsToShow, isSelectedArray, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                if (isChecked) {
                    if (chosenTeams.contains(allTeams.get(which)) == false)
                        chosenTeams.add(allTeams.get(which));
                } else if (chosenTeams.contains(allTeams.get(which))) {
                    chosenTeams.remove(allTeams.get(which));
                }
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
                BusProvider.getInstance().post(new FilterAppliedEvent());
            }
        });
        builder.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_player_pool, container, false);
        ButterKnife.inject(this, rootView);
        setHasOptionsMenu(true);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        List<Player> filteredList = filter(mPlayers, chosenTeams);
        mAdapter = new AlphaInAnimationAdapter(new PlayerPoolAdapter(filteredList));
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Subscribe
    public void onFilterApplied(FilterAppliedEvent event) {
        mAdapter = new AlphaInAnimationAdapter(new PlayerPoolAdapter(filter(mPlayers, chosenTeams)));
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<Player> filter(List<Player> players, List<String> teams) {
        List<Player> filteredList = new ArrayList<Player>();
        for (Player p : players) {
            if (teams.contains(p.getShortTeamName())) {
                filteredList.add(p);
            }
        }
        return filteredList;
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
