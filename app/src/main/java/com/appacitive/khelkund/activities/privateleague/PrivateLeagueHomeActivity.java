package com.appacitive.khelkund.activities.privateleague;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.activities.misc.LoginActivity;
import com.appacitive.khelkund.adapters.PrivateLeagueAdapter;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.infra.ConnectionManager;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.SnackBarManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.model.PrivateLeague;
import com.appacitive.khelkund.model.events.privateleague.PrivateLeagueDeleteEvent;
import com.appacitive.khelkund.model.events.privateleague.PrivateLeagueSelectedEvent;
import com.appacitive.khelkund.model.events.privateleague.PrivateLeagueShareEvent;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.FlipInBottomXAnimator;

public class PrivateLeagueHomeActivity extends ActionBarActivity {

    @InjectView(R.id.et_join_private_league_code)
    public EditText mCode;

    @InjectView(R.id.rl_private_league_empty)
    public RelativeLayout mEmpty;

    @InjectView(R.id.fab_create_private_league)
    public FloatingActionButton mCreate;

    @InjectView(R.id.rv_privateleague)
    public RecyclerView mRecyclerView;

    public PrivateLeagueAdapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;

    private List<PrivateLeague> mPrivateLeagues;
    private String mUserId;
    private ProgressDialog mProgress;
    private StorageManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_league_home);
        ButterKnife.inject(this);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mUserId = SharedPreferencesManager.ReadUserId();

        if (mUserId == null) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            PrivateLeagueHomeActivity.this.finish();
            return;
        }
        mPrivateLeagues = new ArrayList<PrivateLeague>();
        mManager = new StorageManager();
        mPrivateLeagues = (mManager.GetAllPrivateLeaguesForUser(mUserId));
        ConnectionManager.checkNetworkConnectivity(this);
        if (mPrivateLeagues == null || mPrivateLeagues.size() == 0)
            fetchAndDisplayPrivateLeague();
        else displayPrivateLeagues();
        mCode.addTextChangedListener(watcher);
        showTutorialOverlay();
    }

    private void showTutorialOverlay() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                new ShowcaseView.Builder(PrivateLeagueHomeActivity.this)
                        .setTarget(new ViewTarget(mCreate))
                        .setContentText("Or join an existing one")
                        .setContentTitle("Create a new Private League")
                        .hideOnTouchOutside()
                        .singleShot(998)
                        .build().hideButton();
            }
        };
        new Handler().postDelayed(runnable, 1000);

    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.length() == 5) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                joinPrivateLeague(charSequence.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void joinPrivateLeague(String code) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Joining private league " + code);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Http http = new Http();
        JSONObject request = new JSONObject();
        try {
            request.put("Code", code);
            request.put("UserId", mUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        http.post(Urls.PrivateLeagueUrls.getJoinPrivateLeaguesUrl(), new HashMap<String, String>(), request, new APCallback() {
            @Override
            public void success(JSONObject result) {
                dialog.dismiss();
                mCode.setText("");
                if (result.optJSONObject("Error") != null) {
                    SnackBarManager.showError(result.optJSONObject("Error").optString("ErrorMessage"), PrivateLeagueHomeActivity.this);
                    YoYo.with(Techniques.Shake).duration(700).playOn(mCode);
                    return;
                }
                JSONArray privateLeaguesArray = result.optJSONArray("PrivateLeagues");
                if (privateLeaguesArray != null && privateLeaguesArray.length() > 0) {
                    PrivateLeague league = new PrivateLeague(privateLeaguesArray.optJSONObject(0));
                    if (league == null)
                        return;
                    league.setUserId(mUserId);
                    StorageManager manager = new StorageManager();
                    manager.SavePrivateLeague(league);
                    mPrivateLeagues.add(0, league);
                    mAdapter.ResetLeagues(mPrivateLeagues);
                    mAdapter.notifyDataSetChanged();
                    ItemCountChanged();
                    mLayoutManager.scrollToPosition(0);
                }

            }

            @Override
            public void failure(Exception e) {
                mCode.setText("");
                dialog.dismiss();
                SnackBarManager.showError("Something went wrong", PrivateLeagueHomeActivity.this);
            }
        });
    }

    private void displayPrivateLeagues() {

        mAdapter = new PrivateLeagueAdapter(mPrivateLeagues, mUserId);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new FlipInBottomXAnimator());
        ItemCountChanged();
    }

    private void fetchAndDisplayPrivateLeague() {
        Http http = new Http();
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Fetching private leagues");
        mProgress.show();
        http.get(Urls.PrivateLeagueUrls.getPrivateLeaguesUrl(mUserId), new HashMap<String, String>(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                mProgress.dismiss();
                if (result.optJSONObject("Error") != null) {
                    SnackBarManager.showError(result.optJSONObject("Error").optString("ErrorMessage"), PrivateLeagueHomeActivity.this);
                } else {
                    List<PrivateLeague> privateLeagues = new ArrayList<PrivateLeague>();
                    JSONArray leaguesArray = result.optJSONArray("PrivateLeagues");
                    if (leaguesArray != null) {
                        for (int i = 0; i < leaguesArray.length(); i++) {
                            PrivateLeague league = new PrivateLeague(leaguesArray.optJSONObject(i));
                            league.setUserId(mUserId);
                            privateLeagues.add(league);
                        }
                    }
                    StorageManager mManager = new StorageManager();
                    mManager.SavePrivateLeagues(privateLeagues);
                    mPrivateLeagues = privateLeagues;
                }
                displayPrivateLeagues();
            }

            @Override
            public void failure(Exception e) {
                mProgress.dismiss();
                SnackBarManager.showError("Your private leagues could not be fetched from Khelkund at the moment. Try again later.", PrivateLeagueHomeActivity.this);
            }
        });
    }

    @OnClick(R.id.fab_create_private_league)
    public void onClickCreateLeagueButton() {

        View view = getLayoutInflater().inflate(R.layout.layout_dialog_create_privateleague, null);

        final EditText mName = (EditText) view.findViewById(R.id.et_private_league_create_name);
        TextView mCreate = (TextView) view.findViewById(R.id.tv_create_league);
        TextView mCancel = (TextView) view.findViewById(R.id.tv_createleague_cancel);

        final Dialog dialog = new Dialog(this, R.style.MyDialog);
        dialog.setContentView(view);
        dialog.show();
        mName.requestFocus();
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    YoYo.with(Techniques.Shake).duration(700).playOn(mName);
                    return;
                }
                dialog.dismiss();
                createNewPrivateLeague(name);
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void createNewPrivateLeague(String name) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Creating Private League");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Http http = new Http();
        JSONObject request = new JSONObject();
        try {
            request.put("UserId", mUserId);
            request.put("Name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        http.post(Urls.PrivateLeagueUrls.getCreatePrivateLeaguesUrl(), new HashMap<String, String>(), request, new APCallback() {
            @Override
            public void success(JSONObject result) {
                dialog.dismiss();
                if (result.optJSONObject("Error") != null) {
                    SnackBarManager.showError(result.optJSONObject("Error").optString("ErrorMessage"), PrivateLeagueHomeActivity.this);
                    return;
                }
                JSONArray privateLeaguesArray = result.optJSONArray("PrivateLeagues");
                if (privateLeaguesArray != null && privateLeaguesArray.length() > 0) {
                    PrivateLeague league = new PrivateLeague(privateLeaguesArray.optJSONObject(0));
                    league.setUserId(mUserId);
                    StorageManager manager = new StorageManager();
                    manager.SavePrivateLeague(league);
                    mPrivateLeagues.add(0, league);
                    mAdapter.ResetLeagues(mPrivateLeagues);
                    mAdapter.notifyDataSetChanged();
                    ItemCountChanged();
                    showShareTutorial(league);
                }
            }

            @Override
            public void failure(Exception e) {
                dialog.dismiss();
                SnackBarManager.showError("Unable to create Private League.", PrivateLeagueHomeActivity.this);
            }
        });
    }

    public void showShareTutorial(final PrivateLeague league) {
        mLayoutManager.scrollToPosition(0);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                RecyclerView.ViewHolder cardViewHolder = mRecyclerView.findViewHolderForAdapterPosition(0);
                if (cardViewHolder != null && cardViewHolder.itemView != null) {
                    ImageView share = (ImageView) cardViewHolder.itemView.findViewById(R.id.iv_privateleague_share);
                    new ShowcaseView.Builder(PrivateLeagueHomeActivity.this)
                            .setTarget(new ViewTarget(share))
                            .setContentTitle("Invite your friends")
                            .setContentText(String.format("Share the Private League code %s with your friends and invite them to join %s", league.getCode(), league.getName()))
                            .hideOnTouchOutside()
                            .singleShot(997)
                            .build().hideButton();
                }
            }
        };
        new Handler().postDelayed(runnable, 500);

    }

    @Subscribe
    public void onPrivateLeagueClicked(PrivateLeagueSelectedEvent event) {
        Intent intent = new Intent(this, PrivateLeagueLeaderboardActivity.class);
        intent.putExtra("league_id", event.leagueId);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right_fast, R.anim.slide_out_left_fast);
    }

    @Subscribe
    public void onPrivateLeagueShareClicked(PrivateLeagueShareEvent event) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Invite your friends");
        dialog.show();
        StorageManager manager = new StorageManager();
        PrivateLeague league = manager.GetPrivateLeague(event.LeagueId, mUserId);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, String.format("Hey! Join my private IPL fantasy league on Khelkund using the code %s. Get the app here %s", league.getCode(), getResources().getString(R.string.SHORT_APP_URL)));
        sendIntent.setType("text/plain");

        dialog.dismiss();
        startActivity(Intent.createChooser(sendIntent, String.format("Invite friends to %s using", league.getName())));
    }

    @Subscribe
    public void onPrivateLeagueDeleteClicked(PrivateLeagueDeleteEvent event) {
        final PrivateLeague league = new StorageManager().GetPrivateLeague(event.LeagueId, mUserId);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Are you sure you want to leave Private League " + league.getName())
                .setMessage("You can always re-enter the Private League code and join again.")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("LEAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deletePrivateLeague(league.getId());
                    }
                });
        builder.show();

    }

    private void deletePrivateLeague(final String leagueId) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Removing Private League");
        dialog.show();
        Http http = new Http();
        JSONObject request = new JSONObject();

        try {
            request.put("UserId", mUserId);
            request.put("LeagueId", leagueId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        http.post(Urls.PrivateLeagueUrls.getLeavePrivateLeaguesUrl(), new HashMap<String, String>(), request, new APCallback() {
            @Override
            public void success(JSONObject result) {
                dialog.dismiss();
                if (result.optJSONObject("Error") != null) {
                    SnackBarManager.showError(result.optJSONObject("Error").optString("ErrorMessage"), PrivateLeagueHomeActivity.this);
                    return;
                }
                SnackBarManager.showSuccess("Private League removed.", PrivateLeagueHomeActivity.this);
                StorageManager manager = new StorageManager();
                manager.RemovePrivateLeague(leagueId, mUserId);
                mPrivateLeagues = manager.GetAllPrivateLeaguesForUser(mUserId);
                mAdapter.ResetLeagues(mPrivateLeagues);
                mAdapter.notifyDataSetChanged();
                ItemCountChanged();
            }

            @Override
            public void failure(Exception e) {
                dialog.dismiss();
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_private_league_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_left_fast, R.anim.slide_out_right_fast);
                return true;
            case R.id.action_refresh:
                fetchAndDisplayPrivateLeague();
                return true;

        }


        return super.onOptionsItemSelected(item);
    }

    public void ItemCountChanged() {
        if (mAdapter.getItemCount() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mEmpty.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left_fast, R.anim.slide_out_right_fast);
    }

}
