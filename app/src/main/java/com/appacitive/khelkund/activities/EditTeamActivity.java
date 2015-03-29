package com.appacitive.khelkund.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.adapters.PlayerCardAdapter;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.infra.VerticallyWrappedGridLayoutManager;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.model.Formation;
import com.appacitive.khelkund.model.Player;
import com.appacitive.khelkund.model.PlayerType;
import com.appacitive.khelkund.model.Team;
import com.appacitive.khelkund.model.TeamHelper;
import com.appacitive.khelkund.model.User;
import com.appacitive.khelkund.model.events.EmptyPlayerCardClickedEvent;
import com.appacitive.khelkund.model.events.FilledPlayerCardClickedEvent;
import com.nispok.snackbar.Snackbar;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EditTeamActivity extends ActionBarActivity {

    private User mUser;
    private Team mTeamOriginal;
    private Team mTeamMutated;
    private StorageManager mStorageManager;

    private static int VIEW_PLAYER_DETAILS_REQUEST = 111;
    private static int CHOOSE_PLAYER_REQUEST = 222;

    @InjectView(R.id.tv_formation)
    public TextView mFormation;

    @InjectView(R.id.tv_autoselect)
    public TextView mAutoSelect;

    @InjectView(R.id.tv_change_formation)
    public TextView mChangeFormation;

    @InjectView(R.id.tv_balance)
    public TextView mBalance;

    @InjectView(R.id.tv_transfers)
    public TextView mTransfers;

    @InjectView(R.id.tv_points)
    public TextView mPoints;

    @InjectView(R.id.tv_batsman_count)
    public TextView mBatsmanCount;

    @InjectView(R.id.tv_bowler_count)
    public TextView mBowlerCount;

    @InjectView(R.id.tv_allrounder_count)
    public TextView mAllRounderCount;

    @InjectView(R.id.tv_wicketkeeper_count)
    public TextView mWicketKeeperCount;

    @InjectView(R.id.rv_batsman)
    public RecyclerView mBatsmanRecyclerView;

    @InjectView(R.id.rv_bowler)
    public RecyclerView mBowlerRecyclerView;

    @InjectView(R.id.rv_allrounder)
    public RecyclerView mAllRounderRecyclerView;

    @InjectView(R.id.rv_wicketkeeper)
    public RecyclerView mWicketKeeperRecyclerView;

    public RecyclerView.LayoutManager mBatsmenLayoutManager = null;
    public RecyclerView.Adapter mBatsmenAdapter;

    public RecyclerView.LayoutManager mBowlersLayoutManager = null;
    public RecyclerView.Adapter mBowlersAdapter;

    public RecyclerView.LayoutManager mAllRoundersLayoutManager = null;
    public RecyclerView.Adapter mAllRoundersAdapter;

    public RecyclerView.LayoutManager mWicketKeepersLayoutManager = null;
    public RecyclerView.Adapter mWicketKeepersAdapter;

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_team);
        ButterKnife.inject(this);
        BusProvider.getInstance().register(this);
        mStorageManager = new StorageManager();
        String userId = SharedPreferencesManager.ReadUserId();
        mUser = mStorageManager.GetUser(userId);

        mTeamOriginal = mStorageManager.GetTeam(userId);
        mTeamMutated = TeamHelper.clone(mTeamOriginal);

        mTeamMutated.setTransfersRemaining(5);
        mTeamMutated.getPlayers().remove(0);
        Formation formation = TeamHelper.getFormation(mTeamMutated);

        updateStats(mTeamMutated);

        mBatsmanRecyclerView.setHasFixedSize(false);
        mBatsmenLayoutManager = new VerticallyWrappedGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mBatsmanRecyclerView.setLayoutManager(mBatsmenLayoutManager);
        mBatsmenAdapter = new PlayerCardAdapter(TeamHelper.getBatsmen(mTeamMutated), formation.BatsmenCount, R.drawable.batsman, PlayerType.BATSMAN);
        mBatsmanRecyclerView.setAdapter(mBatsmenAdapter);

        mBowlerRecyclerView.setHasFixedSize(false);
        mBowlersLayoutManager = new VerticallyWrappedGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mBowlerRecyclerView.setLayoutManager(mBowlersLayoutManager);
        mBowlersAdapter = new PlayerCardAdapter(TeamHelper.getBowlers(mTeamMutated), formation.BowlersCount, R.drawable.bowler, PlayerType.BOWLER);
        mBowlerRecyclerView.setAdapter(mBowlersAdapter);

        mAllRounderRecyclerView.setHasFixedSize(false);
        mAllRoundersLayoutManager = new VerticallyWrappedGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mAllRounderRecyclerView.setLayoutManager(mAllRoundersLayoutManager);
        mAllRoundersAdapter = new PlayerCardAdapter(TeamHelper.getAllRounders(mTeamMutated), formation.AllRoundersCount, R.drawable.allrounder, PlayerType.ALLROUNDER);
        mAllRounderRecyclerView.setAdapter(mAllRoundersAdapter);

        mWicketKeeperRecyclerView.setHasFixedSize(false);
        mWicketKeepersLayoutManager = new VerticallyWrappedGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mWicketKeeperRecyclerView.setLayoutManager(mWicketKeepersLayoutManager);
        mWicketKeepersAdapter = new PlayerCardAdapter(TeamHelper.getWicketKeepers(mTeamMutated), formation.WicketKeepersCount, R.drawable.wicketkeeper, PlayerType.WICKETKEEPER);
        mWicketKeeperRecyclerView.setAdapter(mWicketKeepersAdapter);

        mChangeFormation.setOnClickListener(formationChangeClickListener);
        mAutoSelect.setOnClickListener(autoSelectClickListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_team, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save)
        {
            saveTeam(mTeamMutated);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetAdapters(Team team)
    {
        Formation formation = TeamHelper.getFormation(mTeamMutated);
        mBatsmenAdapter = new PlayerCardAdapter(TeamHelper.getBatsmen(mTeamMutated), formation.BatsmenCount, R.drawable.batsman, PlayerType.BATSMAN);
        mBatsmanRecyclerView.setAdapter(mBatsmenAdapter);
        mBowlersAdapter = new PlayerCardAdapter(TeamHelper.getBowlers(mTeamMutated), formation.BowlersCount, R.drawable.bowler, PlayerType.BOWLER);
        mBowlerRecyclerView.setAdapter(mBowlersAdapter);
        mAllRoundersAdapter = new PlayerCardAdapter(TeamHelper.getAllRounders(mTeamMutated), formation.AllRoundersCount, R.drawable.allrounder, PlayerType.ALLROUNDER);
        mAllRounderRecyclerView.setAdapter(mAllRoundersAdapter);
        mWicketKeepersAdapter = new PlayerCardAdapter(TeamHelper.getWicketKeepers(mTeamMutated), formation.WicketKeepersCount, R.drawable.wicketkeeper, PlayerType.WICKETKEEPER);
        mWicketKeeperRecyclerView.setAdapter(mWicketKeepersAdapter);
    }

    private RecyclerView.Adapter getAdapterByType(PlayerType type)
    {
        if(type == PlayerType.BATSMAN)
            return mBatsmenAdapter;
        if (type == PlayerType.ALLROUNDER)
            return mAllRoundersAdapter;
        if(type == PlayerType.BOWLER)
            return mBowlersAdapter;
        return mWicketKeepersAdapter;
    }

    private View.OnClickListener formationChangeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener autoSelectClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };



    private void updateStats(Team team)
    {
        mBalance.setText(String.valueOf(team.getBalance()));
        mTransfers.setText(String.valueOf(team.getTransfersRemaining()) + " Transfers");
        mPoints.setText(String.valueOf(team.getTotalPoints()) + " Pt");
        Formation formation = TeamHelper.getFormation(mTeamMutated);
        mFormation.setText(String.format("Your team formation is set to %s BTSM, %s BWLR, %s AR and %s WK.", formation.BatsmenCount, formation.BowlersCount, formation.AllRoundersCount, formation.WicketKeepersCount));
        mBatsmanCount.setText(String.format("%sX", String.valueOf(formation.BatsmenCount)));
        mBowlerCount.setText(String.format("%sX", String.valueOf(formation.BowlersCount)));
        mAllRounderCount.setText(String.format("%sX", String.valueOf(formation.AllRoundersCount)));
        mWicketKeeperCount.setText(String.format("%sX", String.valueOf(formation.WicketKeepersCount)));
    }

    @Subscribe
    public void onFilledCardClick(FilledPlayerCardClickedEvent event)
    {
        Intent viewPlayerDetailsIntent = new Intent(EditTeamActivity.this, PlayerDetailsActivity.class);
        viewPlayerDetailsIntent.putExtra("player_id", event.playerId);
        viewPlayerDetailsIntent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivityForResult(viewPlayerDetailsIntent, VIEW_PLAYER_DETAILS_REQUEST);
    }

    @Subscribe
    public void onEmptyCardClick(EmptyPlayerCardClickedEvent event)
    {
        Intent choosePlayerIntent = new Intent(EditTeamActivity.this, PlayerPoolActivity.class);
        choosePlayerIntent.putExtra("type", event.playerType.toString());
        choosePlayerIntent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivityForResult(choosePlayerIntent, CHOOSE_PLAYER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed
        if(resultCode != RESULT_OK)
            return;
        if(requestCode == VIEW_PLAYER_DETAILS_REQUEST)
        {

        }
        if(requestCode == CHOOSE_PLAYER_REQUEST)
        {
            String playerId = data.getStringExtra("player_id");
            tryAddPlayer(playerId);
        }
    }

    private void tryAddPlayer(String playerId)
    {
        StorageManager storageManager = new StorageManager();
        Player player = storageManager.GetPlayer(playerId);

        // check if user has sufficient balance
        if(mTeamMutated.getBalance() - player.getPrice() < 0)
        {
            showMessage("You have insufficient funds to buy this player");
            return;
        }
        // check if user has sufficient transfers remaining
        boolean isOriginalPlayer = false;
        for(Player originalPlayer : mTeamOriginal.getPlayers())
        {
            if(playerId.equals(originalPlayer.getId()))
                isOriginalPlayer = true;
        }
        if(isOriginalPlayer == false && mTeamMutated.getTransfersRemaining() < 1)
        {
            showMessage("Sorry! You have run out of transfers");
            return;
        }
        // check player already exists in your team
        for(Player p : mTeamMutated.getPlayers())
        {
            if(playerId.equals(p.getId()))
            {
                showMessage("You already own this player");
                return;
            }
        }
        // check if user hits maximum number of players from same team count
        int sameTeamCount = 0;
        for (Player p : mTeamMutated.getPlayers())
        {
            if(p.getShortTeamName().equals(player.getShortTeamName()))
                sameTeamCount++;
        }
        if(sameTeamCount >=6)
        {
            showMessage("You cannot pick more than 6 players from the same team");
            return;
        }

        // if validations pass, add him to team
        mTeamMutated.getPlayers().add(player);
        int oldBalance = mTeamMutated.getBalance();
        mTeamMutated.setBalance(oldBalance - player.getPrice());
        int oldTransfersRemaining = mTeamMutated.getTransfersRemaining();
        mTeamMutated.setTransfersRemaining(oldTransfersRemaining - 1);
        resetAdapters(mTeamMutated);
        updateStats(mTeamMutated);
    }

    private void saveTeam(final Team team)
    {
        // check captain exists
        if(mTeamMutated.getCaptainId() == null || TextUtils.isEmpty(mTeamMutated.getCaptainId()))
        {
            showMessage("Select a captain for your team");
            return;
        }
        // make sure there are 11 players
        if(mTeamMutated.getPlayers().size() != 11)
        {
            showMessage("Your team is missing players. Pick all 11 players to continue");
            return;
        }
        Http http = new Http();
        JSONObject jsonObject = TeamHelper.getJson(team);
        http.post(Urls.TeamUrls.saveTeamUrl(), new HashMap<String, String>(), jsonObject, new APCallback() {
            @Override
            public void success(JSONObject result) {
                if(result.optJSONObject("Error") != null)
                {
                    showMessage("Something wrong happened");
                    return;
                }
                showMessage("Team created successfully");
                StorageManager manager = new StorageManager();
                Team savedTeam = new Team(result);
                savedTeam.setUserId(team.getUserId());
                manager.Save(savedTeam);
            }

            @Override
            public void failure(Exception e) {
                showMessage("Something wrong happened");
            }
        });
    }

    private void showMessage(String message)
    {
        Snackbar.with(getApplicationContext()) // context
                .text(message) // text to display
                .show(this);
    }
}
