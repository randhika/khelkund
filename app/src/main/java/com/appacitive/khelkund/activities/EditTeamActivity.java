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
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmList;

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
        mStorageManager = new StorageManager();
        String userId = SharedPreferencesManager.ReadUserId();
        mUser = mStorageManager.GetUser(userId);

        mTeamOriginal = mStorageManager.GetTeam(userId);
        mTeamMutated = TeamHelper.clone(mTeamOriginal);

        mTeamMutated.setTransfersRemaining(50);
//        mTeamMutated.getPlayers().remove(0);
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
        Formation formation = TeamHelper.getFormation(team);
        mBatsmenAdapter = new PlayerCardAdapter(TeamHelper.getBatsmen(team), formation.BatsmenCount, R.drawable.batsman, PlayerType.BATSMAN);
        mBatsmanRecyclerView.setAdapter(mBatsmenAdapter);
        mBowlersAdapter = new PlayerCardAdapter(TeamHelper.getBowlers(team), formation.BowlersCount, R.drawable.bowler, PlayerType.BOWLER);
        mBowlerRecyclerView.setAdapter(mBowlersAdapter);
        mAllRoundersAdapter = new PlayerCardAdapter(TeamHelper.getAllRounders(team), formation.AllRoundersCount, R.drawable.allrounder, PlayerType.ALLROUNDER);
        mAllRounderRecyclerView.setAdapter(mAllRoundersAdapter);
        mWicketKeepersAdapter = new PlayerCardAdapter(TeamHelper.getWicketKeepers(team), formation.WicketKeepersCount, R.drawable.wicketkeeper, PlayerType.WICKETKEEPER);
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
            //  reset fields
            mTeamMutated.setCaptainId(null);
            mTeamMutated.setBalance(10000000);
            mTeamMutated.setPlayers(new RealmList<Player>());

            Random random = new Random();
            Formation formation = TeamHelper.getFormation(mTeamMutated);
            // Fetch batsmen
            List<Player> batsmen = mStorageManager.GetBargainPlayersByType("Batsman");
            int i = 0;
            while (i < formation.BatsmenCount)
            {
                Player randomPlayer = batsmen.get(random.nextInt(batsmen.size()));
                boolean isPlayerTaken = false;
                for(Player p : mTeamMutated.getPlayers())
                {
                    if(p.getId().equals(randomPlayer.getId()))
                    {
                        isPlayerTaken = true;
                        break;
                    }
                }
                if(isPlayerTaken == false)
                {
                    mTeamMutated.getPlayers().add(randomPlayer);
                    mTeamMutated.setBalance(mTeamMutated.getBalance() - randomPlayer.getPrice());
                    i++;
                }
            }


            // Fetch wicketkeepers
            List<Player> wicketKeepers = mStorageManager.GetBargainPlayersByType("WicketKeeper");
            i = 0;
            while (i < formation.WicketKeepersCount)
            {
                Player randomPlayer = wicketKeepers.get(random.nextInt(wicketKeepers.size()));
                boolean isPlayerTaken = false;
                for(Player p : mTeamMutated.getPlayers())
                {
                    if(p.getId().equals(randomPlayer.getId()))
                    {
                        isPlayerTaken = true;
                        break;
                    }
                }
                if(isPlayerTaken == false)
                {
                    mTeamMutated.getPlayers().add(randomPlayer);
                    mTeamMutated.setBalance(mTeamMutated.getBalance() - randomPlayer.getPrice());
                    i++;
                }
            }

            // Fetch bowlers
            List<Player> bowlers = mStorageManager.GetBargainPlayersByType("Bowler");
            i = 0;
            while (i < formation.BowlersCount)
            {
                Player randomPlayer = bowlers.get(random.nextInt(bowlers.size()));
                boolean isPlayerTaken = false;
                for(Player p : mTeamMutated.getPlayers())
                {
                    if(p.getId().equals(randomPlayer.getId()))
                    {
                        isPlayerTaken = true;
                        break;
                    }
                }
                if(isPlayerTaken == false)
                {
                    mTeamMutated.getPlayers().add(randomPlayer);
                    mTeamMutated.setBalance(mTeamMutated.getBalance() - randomPlayer.getPrice());
                    i++;
                }
            }

            // Fetch all rounders
            List<Player> allRounders = mStorageManager.GetBargainPlayersByType("AllRounder");
            i = 0;
            while (i < formation.AllRoundersCount)
            {
                Player randomPlayer = allRounders.get(random.nextInt(allRounders.size()));
                boolean isPlayerTaken = false;
                for(Player p : mTeamMutated.getPlayers())
                {
                    if(p.getId().equals(randomPlayer.getId()))
                    {
                        isPlayerTaken = true;
                        break;
                    }
                }
                if(isPlayerTaken == false)
                {
                    mTeamMutated.getPlayers().add(randomPlayer);
                    mTeamMutated.setBalance(mTeamMutated.getBalance() - randomPlayer.getPrice());
                    i++;
                }
            }

            updateStats(mTeamMutated);
            resetAdapters(mTeamMutated);
            showMessage("Auto Selected team for you");

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
        if(mTeamMutated.getCaptainId() != null && mTeamMutated.getCaptainId().equals(event.playerId))
            viewPlayerDetailsIntent.putExtra("is_captain", true);
        else
            viewPlayerDetailsIntent.putExtra("is_captain", false);
        startActivityForResult(viewPlayerDetailsIntent, VIEW_PLAYER_DETAILS_REQUEST);
    }

    @Subscribe
    public void onEmptyCardClick(EmptyPlayerCardClickedEvent event)
    {
        Intent choosePlayerIntent = new Intent(EditTeamActivity.this, PlayerPoolActivity.class);
        choosePlayerIntent.putExtra("type", event.playerType.toString());
        startActivityForResult(choosePlayerIntent, CHOOSE_PLAYER_REQUEST);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed
        if(resultCode != RESULT_OK)
            return;
        if(requestCode == VIEW_PLAYER_DETAILS_REQUEST)
        {
            String captainId = data.getStringExtra("captain_id");
            if(captainId != null) {
                mTeamMutated.setCaptainId(captainId);
                updateStats(mTeamMutated);
            }

            String removeId = data.getStringExtra("remove_id");
            if(removeId != null)
            {
                for(int i = 0; i < mTeamMutated.getPlayers().size(); i++)
                {
                    if(mTeamMutated.getPlayers().get(i).getId().equals(removeId)) {
                        mTeamMutated.getPlayers().remove(i);
                        if(mTeamMutated.getCaptainId().equals(removeId))
                            mTeamMutated.setCaptainId(null);
                        resetAdapters(mTeamMutated);
                    }
                }
            }
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

        // make sure there are 11 players
        if(mTeamMutated.getPlayers().size() != 11)
        {
            showMessage("Your team is missing players. Pick all 11 players to continue");
            return;
        }

        // check captain exists
        if(mTeamMutated.getCaptainId() == null || TextUtils.isEmpty(mTeamMutated.getCaptainId()))
        {
            showMessage("Select a captain for your team");
            return;
        }
        Http http = new Http();
        JSONObject jsonObject = TeamHelper.getJson(team);
        http.post(Urls.TeamUrls.saveTeamUrl(), new HashMap<String, String>(), jsonObject, new APCallback() {
            @Override
            public void success(JSONObject result) {
                if(result.optJSONObject("Error") != null)
                {
                    showMessage(result.optJSONObject("Error").optString("ErrorMessage"));
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
