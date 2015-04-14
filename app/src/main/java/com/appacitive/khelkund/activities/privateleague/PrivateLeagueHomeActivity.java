package com.appacitive.khelkund.activities.privateleague;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.adapters.PrivateLeagueAdapter;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.ConnectionManager;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.SnackBarManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.model.PrivateLeague;
import com.appacitive.khelkund.model.TeamHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;

public class PrivateLeagueHomeActivity extends ActionBarActivity {

    @InjectView(R.id.rv_privateleague)
    public RecyclerView mRecyclerView;

    public RecyclerView.Adapter mAdapter;
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

        mUserId = SharedPreferencesManager.ReadUserId();
        mManager = new StorageManager();
        mPrivateLeagues = TeamHelper.clone(mManager.GetAllPrivateLeaguesForUser(mUserId));
        ConnectionManager.checkNetworkConnectivity(this);
        if (mPrivateLeagues == null || mPrivateLeagues.size() == 0)
            fetchAndDisplayPrivateLeague();
        else displayPrivateLeagues();
    }

    private void displayPrivateLeagues() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SlideInBottomAnimationAdapter(new PrivateLeagueAdapter(mPrivateLeagues));
        mRecyclerView.setAdapter(mAdapter);
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
                    return;
                }
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

        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mName.getText().toString();
                if (TextUtils.isEmpty(name)) {
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
                    mAdapter.notifyItemInserted(0);
                    showShareDialog(league);
                }
            }

            @Override
            public void failure(Exception e) {
                SnackBarManager.showError("Unable to create Private League", PrivateLeagueHomeActivity.this);
            }
        });
    }

    public void showShareDialog(PrivateLeague league) {
        Dialog dialog = new Dialog(this, R.style.MyDialog);
        
    }


}
