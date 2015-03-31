package com.appacitive.khelkund.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.adapters.TeamLogoAdapter;
import com.appacitive.khelkund.infra.ConnectionManager;
import com.appacitive.khelkund.infra.RecyclerItemClickListener;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.model.Player;
import com.appacitive.khelkund.model.Team;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmList;

public class CreateTeamActivity extends ActionBarActivity {

    private static List<String> mLogos = new ArrayList<String>()
    {{
            add("ic_logo_1");
            add("ic_logo_2");
            add("ic_logo_3");
            add("ic_logo_4");
            add("ic_logo_5");
        }};

    private static int selectedId = 0;

    @InjectView(R.id.rv_pick_team_logo)
    public RecyclerView mLogoRecyclerView;

    @InjectView(R.id.et_pick_team_name)
    public EditText mTeamName;

    @InjectView(R.id.btn_create_team)
    public Button mCreateTeam;

    private String mUserId;
    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        ButterKnife.inject(this);
        ConnectionManager.checkNetworkConnectivity(this);
        mUserId = SharedPreferencesManager.ReadUserId();

        mLogoRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 3);
        mLogoRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TeamLogoAdapter(mLogos);
        mLogoRecyclerView.setAdapter(mAdapter);

        mLogoRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {

                // 1. Remove selection from previously selected item
                TeamLogoAdapter.TeamLogoViewHolder holder =  (TeamLogoAdapter.TeamLogoViewHolder)mLogoRecyclerView.findViewHolderForAdapterPosition(CreateTeamActivity.selectedId);
                holder.logo.setBackgroundColor(Color.WHITE);

                // 2. Add selection to newly selected item
                CardView cardView = (CardView) view.findViewById(R.id.card_view_pick_team_logo);
                cardView.setBackgroundColor(getResources().getColor(R.color.accent));

                // 3. Update selected position in static variable
                CreateTeamActivity.selectedId = position;
            }
        }));

        mCreateTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(mTeamName.getText()))
                {
                    SnackbarManager.show(
                            Snackbar.with(getApplicationContext())
                                    .text("You did not provide a name for your team"), CreateTeamActivity.this);
                    return;
                }
                createTeam(mTeamName.getText().toString(), mLogos.get(selectedId));
            }
        });

    }

    private void createTeam(String teamName, String logoId) {
        Team team = new Team();
        team.setUserId(mUserId);
        team.setFormation("C_4_3_3_1");
        team.setPlayers(new RealmList<Player>());
        team.setImageName(logoId);
        team.setName(teamName);
        team.setTotalPoints(0);

        team.setTransfersRemaining(18);
        team.setBalance(10000000);

        StorageManager storageManager = new StorageManager();
        storageManager.SaveTeam(team);
        Intent editTeamIntent = new Intent(CreateTeamActivity.this, EditTeamActivity.class);
        startActivity(editTeamIntent);
        finish();
    }


}
