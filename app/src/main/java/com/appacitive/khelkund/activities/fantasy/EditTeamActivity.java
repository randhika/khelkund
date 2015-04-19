package com.appacitive.khelkund.activities.fantasy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.adapters.PlayerCardAdapter;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.infra.ConnectionManager;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.infra.widgets.VerticallyWrappedGridLayoutManager;
import com.appacitive.khelkund.model.Formation;
import com.appacitive.khelkund.model.KhelkundUser;
import com.appacitive.khelkund.model.Player;
import com.appacitive.khelkund.model.PlayerType;
import com.appacitive.khelkund.model.Team;
import com.appacitive.khelkund.infra.TeamHelper;
import com.appacitive.khelkund.model.events.AlreadyOwnedPlayerClickedEvent;
import com.appacitive.khelkund.model.events.CardErrorEvent;
import com.appacitive.khelkund.model.events.EmptyPlayerCardClickedEvent;
import com.appacitive.khelkund.model.events.FilledPlayerCardClickedEvent;
import com.appacitive.khelkund.model.events.NewPlayerAddedEvent;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.squareup.otto.Subscribe;

import org.codechimp.apprater.AppRater;
import org.json.JSONObject;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmList;

public class EditTeamActivity extends ActionBarActivity {

    private KhelkundUser mUser;
    private Team mTeamOriginal;
    private Team mTeamMutated;
    private StorageManager mStorageManager;

    private ProgressDialog mProgressDialog;

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
    public PlayerCardAdapter mBatsmenAdapter;

    public RecyclerView.LayoutManager mBowlersLayoutManager = null;
    public PlayerCardAdapter mBowlersAdapter;

    public RecyclerView.LayoutManager mAllRoundersLayoutManager = null;
    public PlayerCardAdapter mAllRoundersAdapter;

    public RecyclerView.LayoutManager mWicketKeepersLayoutManager = null;
    public PlayerCardAdapter mWicketKeepersAdapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_team);
        ButterKnife.inject(this);
        ConnectionManager.checkNetworkConnectivity(this);
        mStorageManager = new StorageManager();
        String userId = SharedPreferencesManager.ReadUserId();
        mUser = mStorageManager.GetUser(userId);

        mTeamOriginal = mStorageManager.GetTeam(userId);
        mTeamMutated = TeamHelper.clone(mTeamOriginal);
        getSupportActionBar().setTitle(mTeamOriginal.getName());
        Formation formation = TeamHelper.getFormation(mTeamMutated);

        updateStats(mTeamMutated);

        mBatsmanRecyclerView.setHasFixedSize(false);
        mBatsmenLayoutManager = new VerticallyWrappedGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mBatsmanRecyclerView.setLayoutManager(mBatsmenLayoutManager);
        mBatsmenAdapter = new PlayerCardAdapter(TeamHelper.getBatsmen(mTeamMutated), formation.BatsmenCount, R.drawable.batsman, PlayerType.BATSMAN, mTeamMutated.getCaptainId());
        mBatsmanRecyclerView.setAdapter(mBatsmenAdapter);


        mBowlerRecyclerView.setHasFixedSize(false);
        mBowlersLayoutManager = new VerticallyWrappedGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mBowlerRecyclerView.setLayoutManager(mBowlersLayoutManager);
        mBowlersAdapter = new PlayerCardAdapter(TeamHelper.getBowlers(mTeamMutated), formation.BowlersCount, R.drawable.bowler, PlayerType.BOWLER, mTeamMutated.getCaptainId());
        mBowlerRecyclerView.setAdapter(mBowlersAdapter);

        mAllRounderRecyclerView.setHasFixedSize(false);
        mAllRoundersLayoutManager = new VerticallyWrappedGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mAllRounderRecyclerView.setLayoutManager(mAllRoundersLayoutManager);
        mAllRoundersAdapter = new PlayerCardAdapter(TeamHelper.getAllRounders(mTeamMutated), formation.AllRoundersCount, R.drawable.allrounder, PlayerType.ALLROUNDER, mTeamMutated.getCaptainId());
        mAllRounderRecyclerView.setAdapter(mAllRoundersAdapter);

        mWicketKeeperRecyclerView.setHasFixedSize(false);
        mWicketKeepersLayoutManager = new VerticallyWrappedGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mWicketKeeperRecyclerView.setLayoutManager(mWicketKeepersLayoutManager);
        mWicketKeepersAdapter = new PlayerCardAdapter(TeamHelper.getWicketKeepers(mTeamMutated), formation.WicketKeepersCount, R.drawable.wicketkeeper, PlayerType.WICKETKEEPER, mTeamMutated.getCaptainId());
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
        if (item.getItemId() == R.id.action_save) {
            saveTeam(mTeamMutated);
            return true;
        }
        if (item.getItemId() == R.id.action_reset) {
            mTeamMutated = TeamHelper.clone(mTeamOriginal);
            updateStats(mTeamMutated);
            resetAdapters(mTeamMutated, false);
            showSuccess("Your changes have been reset");
            return true;
        }
        if (item.getItemId() == R.id.action_share) {
            shareTeam();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareTeam() {

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Ready to share?");
        mProgressDialog.show();
        Bitmap bitmap = getScreenBitmap();

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_TEXT, String.format("Hey! Checkout my team %s on Khelkund. Get the app here %s", mTeamMutated.getName(), getResources().getString(R.string.SHORT_APP_URL)));
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, mTeamOriginal.getName());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);

        OutputStream outstream;
        try {
            outstream = getContentResolver().openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
            outstream.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        share.putExtra(Intent.EXTRA_STREAM, uri);
        mProgressDialog.dismiss();
        startActivity(Intent.createChooser(share, String.format("Share %s using :", mTeamMutated.getName())));
    }

    private Bitmap getScreenBitmap() {

        View view = findViewById(R.id.rl_edit_team_header);
        Bitmap b = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas1 = new Canvas(b);
        view.draw(canvas1);

        ScrollView iv = (ScrollView) findViewById(R.id.edit_team_parent_scroll);
        Bitmap c = Bitmap.createBitmap(iv.getChildAt(0).getWidth(), iv.getChildAt(0).getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(c);
        iv.getChildAt(0).draw(canvas2);

        int width, height = 0;

        if(b.getWidth() > c.getWidth()) {
            width = b.getWidth();

        } else {
            width = c.getWidth();
        }

        height = b.getHeight() + c.getHeight();

        Bitmap cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs);
        comboImage.drawColor(getResources().getColor(R.color.background_material_light));
        comboImage.drawBitmap(b, 0f, 0f, null);
        comboImage.drawBitmap(c, 0f, b.getHeight(), null);
        return cs;

    }

    private void resetAdapters(Team team, boolean animate) {
        Formation formation = TeamHelper.getFormation(team);

        mBatsmenAdapter.resetDetails(TeamHelper.getBatsmen(team), formation.BatsmenCount, R.drawable.batsman, PlayerType.BATSMAN, team.getCaptainId());
        mBatsmenAdapter.notifyDataSetChanged();

        mBowlersAdapter.resetDetails(TeamHelper.getBowlers(team), formation.BowlersCount, R.drawable.bowler, PlayerType.BOWLER, team.getCaptainId());
        mBowlersAdapter.notifyDataSetChanged();

        mAllRoundersAdapter.resetDetails(TeamHelper.getAllRounders(team), formation.AllRoundersCount, R.drawable.allrounder, PlayerType.ALLROUNDER, team.getCaptainId());
        mAllRoundersAdapter.notifyDataSetChanged();

        mWicketKeepersAdapter.resetDetails(TeamHelper.getWicketKeepers(team), formation.WicketKeepersCount, R.drawable.wicketkeeper, PlayerType.WICKETKEEPER, team.getCaptainId());
        mWicketKeepersAdapter.notifyDataSetChanged();
    }

    private PlayerCardAdapter getAdapterByType(PlayerType type) {
        if (type == PlayerType.BATSMAN)
            return mBatsmenAdapter;
        if (type == PlayerType.ALLROUNDER)
            return mAllRoundersAdapter;
        if (type == PlayerType.BOWLER)
            return mBowlersAdapter;
        return mWicketKeepersAdapter;
    }

    private int userSelected = 0;

    final Map<String, String> formations = new LinkedHashMap<String, String>() {{
        put("C_6_4_0_1", "6 BTSM, 4 BWLR, 0 AR, 1 WK");
        put("C_5_3_2_1", "5 BTSM, 3 BWLR, 2 AR, 1 WK");
        put("C_4_3_3_1", "4 BTSM, 3 BWLR, 3 AR, 1 WK");
        put("C_4_4_2_1", "4 BTSM, 4 BWLR, 2 AR, 1 WK");
        put("C_5_4_1_1", "5 BTSM, 4 BWLR, 1 AR, 1 WK");
    }};

    String[] formationsStrings = new String[]{
            "6 BTSM, 4 BWLR, 0 AR, 1 WK",
            "5 BTSM, 3 BWLR, 2 AR, 1 WK",
            "4 BTSM, 3 BWLR, 3 AR, 1 WK",
            "4 BTSM, 4 BWLR, 2 AR, 1 WK",
            "5 BTSM, 4 BWLR, 1 AR, 1 WK"

    };

    private View.OnClickListener formationChangeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            List<String> indexes = new ArrayList<String>(formations.keySet());
            final int selectionId = indexes.indexOf(mTeamMutated.getFormation());
            userSelected = selectionId;
            AlertDialog.Builder builder = new AlertDialog.Builder(EditTeamActivity.this);
            builder.setTitle("CHANGE TEAM FORMATION")
                    .setSingleChoiceItems(formationsStrings, selectionId, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            userSelected = i;
                        }
                    })
                    .setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            tryChangeFormation(getFormation(new ArrayList<String>(formations.keySet()).get(userSelected)));
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).create().show();

        }
    };

    private Formation getFormation(String formation) {
        String[] parts = formation.split("_");
        return new Formation(Integer.valueOf(parts[1]), Integer.valueOf(parts[2]), Integer.valueOf(parts[3]), Integer.valueOf(parts[4]));
    }

    private String toFormationString(Formation formation) {
        return "C_" + String.valueOf(formation.BatsmenCount) + "_" + String.valueOf(formation.BowlersCount) + "_" + String.valueOf(formation.AllRoundersCount) + "_" + String.valueOf(formation.WicketKeepersCount);
    }

    private void tryChangeFormation(Formation formation) {
        if (formation.BatsmenCount < TeamHelper.getBatsmen(mTeamMutated).size()) {
            showError(String.format("You need to remove %s batsmen to change formation", String.valueOf(TeamHelper.getBatsmen(mTeamMutated).size() - formation.BatsmenCount)));
            return;
        }

        if (formation.BowlersCount < TeamHelper.getBowlers(mTeamMutated).size()) {
            showError(String.format("You need to remove %s bowlers to change formation", String.valueOf(TeamHelper.getBowlers(mTeamMutated).size() - formation.BowlersCount)));
            return;
        }

        if (formation.AllRoundersCount < TeamHelper.getAllRounders(mTeamMutated).size()) {
            showError(String.format("You need to remove %s all rounders to change formation", String.valueOf(TeamHelper.getAllRounders(mTeamMutated).size() - formation.AllRoundersCount)));
            return;
        }

        if (formation.WicketKeepersCount < TeamHelper.getWicketKeepers(mTeamMutated).size()) {
            showError(String.format("You need to remove %s wicket keepers to change formation", String.valueOf(TeamHelper.getWicketKeepers(mTeamMutated).size() - formation.WicketKeepersCount)));
            return;
        }

        // if all validations pass, change the formation
        mTeamMutated.setFormation(toFormationString(formation));
        showSuccess(String.format("Formation changed to %s", new ArrayList<String>(formations.values()).get(userSelected)));
        updateStats(mTeamMutated);
        resetAdapters(mTeamMutated, false);
    }

    private View.OnClickListener autoSelectClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //  reset fields
            mTeamMutated.setCaptainId(null);
//            mTeamMutated.setBalance(mTeamOriginal.getBalance());
            mTeamMutated.setBalance(mTeamOriginal.getBalanceLimit());
            mTeamMutated.setPlayers(new RealmList<Player>());

            Random random = new Random();
            Formation formation = TeamHelper.getFormation(mTeamMutated);
            // Fetch batsmen
            List<Player> batsmen = mStorageManager.GetBargainPlayersByType("Batsman");
            if (batsmen.size() == 0) {
                showError("Check your internet connectivity and try again");
                return;
            }
            int i = 0;
            while (i < formation.BatsmenCount) {
                Player randomPlayer = batsmen.get(random.nextInt(batsmen.size()));
                boolean isPlayerTaken = false;
                for (Player p : mTeamMutated.getPlayers()) {
                    if (p.getId().equals(randomPlayer.getId())) {
                        isPlayerTaken = true;
                        break;
                    }
                }
                if (isPlayerTaken == false) {
                    mTeamMutated.getPlayers().add(randomPlayer);
                    mTeamMutated.setBalance(mTeamMutated.getBalance() - randomPlayer.getPrice());
                    i++;
                }
            }


            // Fetch wicketkeepers
            List<Player> wicketKeepers = mStorageManager.GetBargainPlayersByType("WicketKeeper");
            i = 0;
            while (i < formation.WicketKeepersCount) {
                Player randomPlayer = wicketKeepers.get(random.nextInt(wicketKeepers.size()));
                boolean isPlayerTaken = false;
                for (Player p : mTeamMutated.getPlayers()) {
                    if (p.getId().equals(randomPlayer.getId())) {
                        isPlayerTaken = true;
                        break;
                    }
                }
                if (isPlayerTaken == false) {
                    mTeamMutated.getPlayers().add(randomPlayer);
                    mTeamMutated.setBalance(mTeamMutated.getBalance() - randomPlayer.getPrice());
                    i++;
                }
            }

            // Fetch bowlers
            List<Player> bowlers = mStorageManager.GetBargainPlayersByType("Bowler");
            i = 0;
            while (i < formation.BowlersCount) {
                Player randomPlayer = bowlers.get(random.nextInt(bowlers.size()));
                boolean isPlayerTaken = false;
                for (Player p : mTeamMutated.getPlayers()) {
                    if (p.getId().equals(randomPlayer.getId())) {
                        isPlayerTaken = true;
                        break;
                    }
                }
                if (isPlayerTaken == false) {
                    mTeamMutated.getPlayers().add(randomPlayer);
                    mTeamMutated.setBalance(mTeamMutated.getBalance() - randomPlayer.getPrice());
                    i++;
                }
            }

            // Fetch all rounders
            List<Player> allRounders = mStorageManager.GetBargainPlayersByType("AllRounder");
            i = 0;
            while (i < formation.AllRoundersCount) {
                Player randomPlayer = allRounders.get(random.nextInt(allRounders.size()));
                boolean isPlayerTaken = false;
                for (Player p : mTeamMutated.getPlayers()) {
                    if (p.getId().equals(randomPlayer.getId())) {
                        isPlayerTaken = true;
                        break;
                    }
                }
                if (isPlayerTaken == false) {
                    mTeamMutated.getPlayers().add(randomPlayer);
                    mTeamMutated.setBalance(mTeamMutated.getBalance() - randomPlayer.getPrice());
                    i++;
                }
            }

            updateStats(mTeamMutated);
            resetAdapters(mTeamMutated, false);
            showSuccess("Auto selected team for you");
        }
    };

    private void updateStats(Team team) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_top);
        animation.setStartOffset(500);

        if (mBalance.getText().toString().equals(String.valueOf(team.getBalance())) == false) {
            mBalance.setText(String.valueOf(team.getBalance()));
            mBalance.startAnimation(animation);
        }

        if (mTransfers.getText().toString().equals(String.valueOf(team.getTransfersRemaining() + " Transfers")) == false) {
            mTransfers.setText(String.valueOf(team.getTransfersRemaining() + " Transfers"));
            mTransfers.startAnimation(animation);
        }

        mPoints.setText(String.valueOf(team.getTotalPoints()) + " Pts");
        Formation formation = TeamHelper.getFormation(mTeamMutated);
        mFormation.setText(String.format("Your team formation is set to %s BTSM, %s BWLR, %s AR and %s WK.", formation.BatsmenCount, formation.BowlersCount, formation.AllRoundersCount, formation.WicketKeepersCount));
        mBatsmanCount.setText(String.format("%sX", String.valueOf(formation.BatsmenCount)));
        mBowlerCount.setText(String.format("%sX", String.valueOf(formation.BowlersCount)));
        mAllRounderCount.setText(String.format("%sX", String.valueOf(formation.AllRoundersCount)));
        mWicketKeeperCount.setText(String.format("%sX", String.valueOf(formation.WicketKeepersCount)));
    }

    @Subscribe
    public void onFilledCardClick(FilledPlayerCardClickedEvent event) {
        Intent viewPlayerDetailsIntent = new Intent(EditTeamActivity.this, PlayerDetailsActivity.class);
        viewPlayerDetailsIntent.putExtra("player_id", event.playerId);
        if (mTeamMutated.getCaptainId() != null && mTeamMutated.getCaptainId().equals(event.playerId))
            viewPlayerDetailsIntent.putExtra("is_captain", true);
        else
            viewPlayerDetailsIntent.putExtra("is_captain", false);
        startActivityForResult(viewPlayerDetailsIntent, VIEW_PLAYER_DETAILS_REQUEST);
    }

    @Subscribe
    public void onEmptyCardClick(EmptyPlayerCardClickedEvent event) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        showSaveTeamTutorialOverlay();
        // check if the request code is same as what is passed
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == VIEW_PLAYER_DETAILS_REQUEST) {

            final String captainId = data.getStringExtra("captain_id");
            if (captainId != null) {
                mTeamMutated.setCaptainId(captainId);
                updateStats(mTeamMutated);
                resetAdapters(mTeamMutated, false);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        BusProvider.getInstance().post(new AlreadyOwnedPlayerClickedEvent(captainId));
                    }
                };
                new Handler().postDelayed(runnable, 300);

            }

            String removeId = data.getStringExtra("remove_id");
            if (removeId != null) {
                for (int i = 0; i < mTeamMutated.getPlayers().size(); i++) {
                    if (mTeamMutated.getPlayers().get(i).getId().equals(removeId)) {
                        int price = mTeamMutated.getPlayers().get(i).getPrice();
                        mTeamMutated.getPlayers().remove(i);
                        mTeamMutated.setBalance(mTeamMutated.getBalance() + price);
                        if (mTeamMutated.getCaptainId() != null && mTeamMutated.getCaptainId().equals(removeId))
                            mTeamMutated.setCaptainId(null);
                        updateStats(mTeamMutated);
                        resetAdapters(mTeamMutated, false);
                    }
                }
            }
        }
        if (requestCode == CHOOSE_PLAYER_REQUEST) {
            String playerId = data.getStringExtra("player_id");
            tryAddPlayer(playerId);
        }
    }

    private void showSaveTeamTutorialOverlay() {
        new ShowcaseView.Builder(this)
                .setTarget(new ViewTarget(findViewById(R.id.action_save)))
                .setContentText("Don't forget to SAVE your team once you have chosen your players")
                .hideOnTouchOutside()
                .singleShot(33)
                .build().hideButton();
    }

    private void tryAddPlayer(final String playerId) {
        StorageManager storageManager = new StorageManager();
        Player player = storageManager.GetPlayer(playerId);


        // check player already exists in your team
        for (Player p : mTeamMutated.getPlayers()) {
            if (playerId.equals(p.getId())) {
                showError("You already own this player");
                BusProvider.getInstance().post(new AlreadyOwnedPlayerClickedEvent(playerId));
                return;
            }
        }

        // check if user has sufficient balance
        if (mTeamMutated.getBalance() - player.getPrice() < 0) {
            showError("You have insufficient funds to buy this player");
            YoYo.with(Techniques.Wobble).duration(700).playOn(mBalance);
            return;
        }

        // check if user hits maximum number of players from same team count
        int sameTeamCount = 0;
        for (Player p : mTeamMutated.getPlayers()) {
            if (p.getShortTeamName().equals(player.getShortTeamName()))
                sameTeamCount++;
        }
        if (sameTeamCount >= 6) {
            showError("You cannot pick more than 6 players from the same team");
            return;
        }

        // if validations pass, add him to team
        mTeamMutated.getPlayers().add(player);
        int oldBalance = mTeamMutated.getBalance();
        mTeamMutated.setBalance(oldBalance - player.getPrice());
        resetAdapters(mTeamMutated, false);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                BusProvider.getInstance().post(new NewPlayerAddedEvent(playerId));
            }
        };
        new Handler().postDelayed(runnable, 200);

        updateStats(mTeamMutated);
    }

    private void saveTeam(final Team team) {

        // make sure there are 11 players
        if (mTeamMutated.getPlayers().size() != 11) {
            showError("Your team is missing players. Pick all 11 players to continue");
            BusProvider.getInstance().post(new CardErrorEvent());
            return;
        }

        // check captain exists
        if (mTeamMutated.getCaptainId() == null || TextUtils.isEmpty(mTeamMutated.getCaptainId())) {
            showError("Select a captain for your team");
            BusProvider.getInstance().post(new CardErrorEvent());
            return;
        }

        // calculate transfers
        List<String> originalPlayerIds = new ArrayList<String>();
        List<String> mutatedPlayerIds = new ArrayList<String>();
        for (Player p : mTeamOriginal.getPlayers()) {
            originalPlayerIds.add(p.getId());
        }

        for (Player p : mTeamMutated.getPlayers()) {
            mutatedPlayerIds.add(p.getId());
        }

        originalPlayerIds.retainAll(mutatedPlayerIds);
        int transfersMade = 11 - originalPlayerIds.size();
        if (transfersMade > mTeamOriginal.getTransfersRemaining()) {
            showError("You don't have sufficient transfers remaining to make these trades");
            YoYo.with(Techniques.Wobble).duration(700).playOn(mTransfers);
            return;
        }
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Saving your team");
        mProgressDialog.show();
        Http http = new Http();
        JSONObject jsonObject = TeamHelper.getJson(team);
        http.post(Urls.TeamUrls.saveTeamUrl(), new HashMap<String, String>(), jsonObject, new APCallback() {
            @Override
            public void success(JSONObject result) {
                mProgressDialog.dismiss();
                if (result.optJSONObject("Error") != null) {
                    showError(result.optJSONObject("Error").optString("ErrorMessage"));
                    BusProvider.getInstance().post(new CardErrorEvent());
                    return;
                }
                showSuccess("Team saved successfully!");
                StorageManager manager = new StorageManager();
                Team savedTeam = new Team(result);
                savedTeam.setUserId(team.getUserId());
                manager.SaveTeam(savedTeam);
                mTeamOriginal = savedTeam;
                mTeamMutated = TeamHelper.clone(mTeamOriginal);
                resetAdapters(mTeamMutated, false);
                updateStats(mTeamMutated);
                AppRater.setLightTheme();
                AppRater.app_launched(EditTeamActivity.this, 2, 2, 2, 2);
            }

            @Override
            public void failure(Exception e) {
                mProgressDialog.dismiss();
                BusProvider.getInstance().post(new CardErrorEvent());
                showError("Something went wrong");
            }
        });
    }

    private void showError(String message) {

        SnackbarManager.show(
                Snackbar.with(getApplicationContext()) // context
                        .type(SnackbarType.MULTI_LINE)
                        .color(Color.parseColor("#F44336"))
                        .textColor(Color.WHITE)
                        .text(message) // text to be displayed
                        .duration(Snackbar.SnackbarDuration.LENGTH_LONG) // make it shorter
                , this);
    }

    private void showSuccess(String message) {
        SnackbarManager.show(
                Snackbar.with(getApplicationContext()) // context
                        .type(SnackbarType.MULTI_LINE)
                        .color(Color.parseColor("#4CAF50"))
                        .textColor(Color.WHITE)
                        .text(message) // text to be displayed
                        .duration(Snackbar.SnackbarDuration.LENGTH_LONG) // make it shorter
                , this);
    }
}
