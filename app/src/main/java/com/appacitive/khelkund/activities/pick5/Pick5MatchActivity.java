package com.appacitive.khelkund.activities.pick5;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.RelativeLayout;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.fragments.Pick5EmptyFragment;
import com.appacitive.khelkund.fragments.Pick5FinishedReadonlyFragment;
import com.appacitive.khelkund.fragments.Pick5PlayFragment;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.ConnectionManager;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.SnackBarManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.model.Pick5MatchDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Pick5MatchActivity extends ActionBarActivity {

    private ProgressDialog mDialog;

    private Pick5MatchDetails mDetails;

    @InjectView(R.id.pick5_next_match)
    public RelativeLayout mNext;

    @InjectView(R.id.pick5_previous_match)
    public RelativeLayout mPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick5_match);
        ButterKnife.inject(this);
        ConnectionManager.checkNetworkConnectivity(this);
        String matchId = getIntent().getStringExtra("match_id");
        String userId = SharedPreferencesManager.ReadUserId();

        fetchMatchDetails(userId, matchId);
    }

    private void setupFragment() {

        //  if match is currently in progress or finished and user did not create a team
        if ((mDetails.getMatchDetails().getMatchStatus() == 2 || mDetails.getMatchDetails().getMatchStatus() == 1) && mDetails.getPlayers().size() == 0) {
            Pick5EmptyFragment emptyPick5Fragment = new Pick5EmptyFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, emptyPick5Fragment).commit();
        }

        //  if user created a team for this match and the match has either started or is finished
        if (mDetails.getPlayers().size() > 0 && (mDetails.getMatchDetails().getMatchStatus() == 1 || mDetails.getMatchDetails().getMatchStatus() == 2)) {
            Pick5FinishedReadonlyFragment readonlyFragment = Pick5FinishedReadonlyFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, readonlyFragment).commit();
        }

        //  if match has not yet started
        if (mDetails.getMatchDetails().getMatchStatus() == 0) {
            Pick5PlayFragment playPick5Fragment = Pick5PlayFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, playPick5Fragment).commit();

        }
    }

    public Pick5MatchDetails getMatchDetails() {
        Pick5MatchDetails details = null;
        try {
            details = new Pick5MatchDetails(new JSONObject("{\n" +
                    "    \"Error\": null,\n" +
                    "    \"Status\": 0,\n" +
                    "    \"AppPlayers\": null,\n" +
                    "    \"AppTeamPoints\": 20,\n" +
                    "    \"Id\": null,\n" +
                    "    \"MatchDetails\": {\n" +
                    "        \"AwayTeamName\": \"Mumbai Indians\",\n" +
                    "        \"AwayTeamShortName\": \"MI\",\n" +
                    "        \"HomeTeamName\": \"Kolkata Knight Riders\",\n" +
                    "        \"HomeTeamShortName\": \"KKR\",\n" +
                    "        \"Id\": \"88663888708633095\",\n" +
                    "        \"MatchStatus\": 0,\n" +
                    "        \"StartDate\": \"2015-04-08T14:30:00.0000000Z\",\n" +
                    "        \"Venue\": \"Eden Gardens, Kolkata\"\n" +
                    "    },\n" +
                    "    \"PlayerMapping\": [\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664645986025969\",\n" +
                    "            \"UserPlayerId\": \"88664679846642055\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664639720784334\",\n" +
                    "            \"UserPlayerId\": \"88664677801918830\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664633373753761\",\n" +
                    "            \"UserPlayerId\": \"88664648848638213\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664665582862676\",\n" +
                    "            \"UserPlayerId\": \"88664638362878404\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664644121657826\",\n" +
                    "            \"UserPlayerId\": \"88664675724689769\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664679846642055\",\n" +
                    "            \"UserPlayerId\": \"88664645986025969\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664633946276262\",\n" +
                    "            \"UserPlayerId\": \"88664650026189068\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664633946276262\",\n" +
                    "            \"UserPlayerId\": \"88664651433378070\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664679175553409\",\n" +
                    "            \"UserPlayerId\": \"88664646607831542\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664662621684037\",\n" +
                    "            \"UserPlayerId\": \"88664637316399551\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664670244831577\",\n" +
                    "            \"UserPlayerId\": \"88664639033967049\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664654966030634\",\n" +
                    "            \"UserPlayerId\": \"88664634552353195\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664654966030634\",\n" +
                    "            \"UserPlayerId\": \"88664636612805050\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664635909210549\",\n" +
                    "            \"UserPlayerId\": \"88664654966030634\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664638362878404\",\n" +
                    "            \"UserPlayerId\": \"88664664977834318\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664641879802328\",\n" +
                    "            \"UserPlayerId\": \"88664678554796408\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664662621684037\",\n" +
                    "            \"UserPlayerId\": \"88664635140604336\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664639033967049\",\n" +
                    "            \"UserPlayerId\": \"88664670244831577\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664674284994911\",\n" +
                    "            \"UserPlayerId\": \"88664647883948288\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664648848638213\",\n" +
                    "            \"UserPlayerId\": \"88664633373753761\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664637316399551\",\n" +
                    "            \"UserPlayerId\": \"88664662621684037\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664642910552541\",\n" +
                    "            \"UserPlayerId\": \"88664681073475986\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664654966030634\",\n" +
                    "            \"UserPlayerId\": \"88664635909210549\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664678554796408\",\n" +
                    "            \"UserPlayerId\": \"88664641879802328\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664682300309916\",\n" +
                    "            \"UserPlayerId\": \"88664645396726252\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664650026189068\",\n" +
                    "            \"UserPlayerId\": \"88664633946276262\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664632768725404\",\n" +
                    "            \"UserPlayerId\": \"88664652021629211\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664633373753761\",\n" +
                    "            \"UserPlayerId\": \"88664654018117925\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664647883948288\",\n" +
                    "            \"UserPlayerId\": \"88664674284994911\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664644824203751\",\n" +
                    "            \"UserPlayerId\": \"88664680451670413\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664632768725404\",\n" +
                    "            \"UserPlayerId\": \"88664650664771857\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664681073475986\",\n" +
                    "            \"UserPlayerId\": \"88664642910552541\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664645986025969\",\n" +
                    "            \"UserPlayerId\": \"88664674939306340\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664645396726252\",\n" +
                    "            \"UserPlayerId\": \"88664682300309916\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664646607831542\",\n" +
                    "            \"UserPlayerId\": \"88664679175553409\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664641177256403\",\n" +
                    "            \"UserPlayerId\": \"88664682922115489\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664682922115489\",\n" +
                    "            \"UserPlayerId\": \"88664641177256403\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664680451670413\",\n" +
                    "            \"UserPlayerId\": \"88664644824203751\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664652021629211\",\n" +
                    "            \"UserPlayerId\": \"88664632768725404\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"AppPlayerId\": \"88664681645998487\",\n" +
                    "            \"UserPlayerId\": \"88664647212859899\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"Players\": null,\n" +
                    "    \"Result\": 0,\n" +
                    "    \"TeamPoints\": 10\n" +
                    "}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<String> myPlayerIds = new ArrayList<String>(){{
            add("88664647212859899");add("88664632768725404");add("88664644824203751");add("88664638362878404");add("88664637316399551");
        }};
        List<String> aiPlayerIds = new ArrayList<String>(){{
            add("88664681645998487");add("88664652021629211");add("88664680451670413");add("88664665582862676");add("88664662621684037");
        }};
        StorageManager manager = new StorageManager();
        for(String myPlayerId : myPlayerIds)
        {
            details.getPlayers().add(manager.GetPlayer(myPlayerId));
        }
        for(String aiPlayerId : aiPlayerIds)
        {
            details.getAppPlayers().add(manager.GetPlayer(aiPlayerId));
        }

        return details;

//        return mDetails;
    }

    private void fetchMatchDetails(String userId, String matchId) {
        Http http = new Http();
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Fetching match details");
        mDialog.show();
        http.get(Urls.Pick5Urls.getMatchesDetailsUrl(userId, matchId), new HashMap<String, String>(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                mDialog.dismiss();
                if (result.optJSONObject("Error") != null) {
                    SnackBarManager.showError(result.optJSONObject("Error").optString("ErrorMessage"), Pick5MatchActivity.this);
                    return;
                }
                mDetails = new Pick5MatchDetails(result);
                mDetails = Pick5MatchActivity.this.getMatchDetails();
                setupFragment();
            }

            @Override
            public void failure(Exception e) {
                mDialog.dismiss();
                SnackBarManager.showError("Something went wrong.", Pick5MatchActivity.this);
            }
        });
    }
}
