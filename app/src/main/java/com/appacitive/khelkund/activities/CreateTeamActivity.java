package com.appacitive.khelkund.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.adapters.TeamLogoAdapter;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.infra.ConnectionManager;
import com.appacitive.khelkund.infra.KhelkundApplication;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.model.Player;
import com.appacitive.khelkund.model.Team;
import com.appacitive.khelkund.model.KhelkundUser;
import com.appacitive.khelkund.model.events.LogoSelectedEvent;
import com.appacitive.khelkund.model.viewholders.TeamLogoViewHolder;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmList;

public class CreateTeamActivity extends ActionBarActivity {

    private static List<String> mLogos = new ArrayList<String>()
    {{
            add("l1");
            add("l2");
            add("l3");
            add("l4");
            add("l5");
            add("l6");
            add("l7");
            add("l8");
            add("l9");
            add("l10");
            add("l11");
            add("l12");
            add("l13");
            add("l14");
            add("l15");
            add("l16");
            add("l17");
        }};

    private int selectedPosition = 0;

    @InjectView(R.id.rv_pick_team_logo)
    public RecyclerView mLogoRecyclerView;

    @InjectView(R.id.et_pick_team_name)
    public EditText mTeamName;

    @InjectView(R.id.btn_create_team)
    public Button mCreateTeam;

    private String mUserId;
    private KhelkundUser mUser;
    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        ButterKnife.inject(this);

        ConnectionManager.checkNetworkConnectivity(this);
        mUserId = SharedPreferencesManager.ReadUserId();
        StorageManager manager = new StorageManager();
        mUser = manager.GetUser(mUserId);
        if(mUser.getFirstName() != null && mUser.getFirstName() != "null")
        {
            mTeamName.setHint(String.format("Your team name eg %s XI", mUser.getFirstName()));
        }
        mLogoRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 3);
        mLogoRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TeamLogoAdapter(mLogos, this.selectedPosition);
        mLogoRecyclerView.setAdapter(mAdapter);

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
                createTeam(mTeamName.getText().toString(), mLogos.get(selectedPosition));
            }
        });

    }

    @Subscribe
    public void onLogoSelected(LogoSelectedEvent event)
    {
        TeamLogoViewHolder holder = (TeamLogoViewHolder) mLogoRecyclerView.findViewHolderForAdapterPosition(this.selectedPosition);
        if(holder != null)
            holder.logo.setCardBackgroundColor(Color.WHITE);

        this.selectedPosition = event.Position;

        holder = (TeamLogoViewHolder) mLogoRecyclerView.findViewHolderForAdapterPosition(this.selectedPosition);
        if(holder != null)
            holder.logo.setCardBackgroundColor(KhelkundApplication.getAppContext().getResources().getColor(R.color.accent));

        ((TeamLogoAdapter) mAdapter).setmSelectedPosition(this.selectedPosition);
    }

    private void createTeam(String teamName, String logoId) {
        Team team = new Team();
        team.setUserId(mUserId);
        team.setFormation("C_4_3_3_1");
        team.setPlayers(new RealmList<Player>());
        team.setImageName(logoId);
        team.setName(teamName);
        team.setTotalPoints(0);

        team.setTransfersRemaining(80);
        team.setBalance(10000000);

        StorageManager storageManager = new StorageManager();
        storageManager.SaveTeam(team);
        Intent editTeamIntent = new Intent(CreateTeamActivity.this, EditTeamActivity.class);
        startActivity(editTeamIntent);
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
}
