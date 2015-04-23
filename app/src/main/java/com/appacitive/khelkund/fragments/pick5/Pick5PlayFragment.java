package com.appacitive.khelkund.fragments.pick5;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.activities.pick5.Pick5MatchActivity;
import com.appacitive.khelkund.adapters.Pick5ChoosePlayerAdapter;
import com.appacitive.khelkund.adapters.Pick5TeamAdapter;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.SnackBarManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.TeamHelper;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.infra.widgets.CircleView;
import com.appacitive.khelkund.infra.widgets.carousel.Carousel;
import com.appacitive.khelkund.infra.widgets.carousel.CoverFlowCarousel;
import com.appacitive.khelkund.model.Pick5MatchDetails;
import com.appacitive.khelkund.model.Player;
import com.appacitive.khelkund.model.events.pick5.Pick5PlayerChosenEvent;
import com.appacitive.khelkund.model.events.pick5.Pick5PlayerClickedEvent;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class Pick5PlayFragment extends Fragment {

    private Pick5MatchDetails mDetails;

    private static final int green = Color.parseColor("#64DD17");
    private static final int red = Color.parseColor("#D50000");

    private Dialog mChoosePlayerDialog;

    private static final int empty_background_color = Color.parseColor("#ffd3d3d3");

    @InjectView(R.id.btn_pick5_play)
    public Button mSubmit;

    @InjectView(R.id.iv_pick5_you)
    public CircleImageView mImageYou;

    @InjectView(R.id.breadcrumb_btsm)
    public CircleView mBreadcrumbBtsm;

    @InjectView(R.id.breadcrumb_bwlr)
    public CircleView mBreadcrumbBwlr;

    @InjectView(R.id.breadcrumb_ar)
    public CircleView mBreadcrumbAr;

    @InjectView(R.id.breadcrumb_wk)
    public CircleView mBreadcrumbWk;

    @InjectView(R.id.breadcrumb_any)
    public CircleView mBreadcrumbAny;

    @InjectView(R.id.my_carousel)
    public Carousel mMyCarousel;

    @InjectView(R.id.ai_carousel)
    public Carousel mAiCarousel;

    public Pick5PlayFragment() {
        // Required empty public constructor
    }

    private StorageManager manager;

    public static Pick5PlayFragment newInstance() {
        Pick5PlayFragment fragment = new Pick5PlayFragment();
        return fragment;
    }

    private List<Player> allAvailablePlayers;

    private Pick5TeamAdapter mMyAdapter;
    private Pick5TeamAdapter mAiAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pick5_play, container, false);
        ButterKnife.inject(this, view);
        manager = new StorageManager();
        this.mDetails = ((Pick5MatchActivity) getActivity()).getMatchDetails();
        initAdapters();

        boolean teamExists = loadPlayers();
        if (teamExists == true) {
            mBreadcrumbAny.setFillColor(green);
            mBreadcrumbBwlr.setFillColor(green);
            mBreadcrumbBtsm.setFillColor(green);
            mBreadcrumbWk.setFillColor(green);
            mBreadcrumbAr.setFillColor(green);
        }
        mMyCarousel.setAdapter(mMyAdapter);
        mMyCarousel.setSelection(2);
        mMyCarousel.setSlowDownCoefficient(Integer.MAX_VALUE);
        mMyCarousel.setSpacing(0.6f);

        mAiCarousel.setAdapter(mAiAdapter);
        mAiCarousel.setSelection(2);
        mAiCarousel.setSlowDownCoefficient(Integer.MAX_VALUE);
        mAiCarousel.setSpacing(0.6f);

        resetAdapters();
        fetchAndDisplayUserImage();
        fetchPlayers();
        showTutorialOverlay();
        return view;
    }

    private void resetAdapters() {
        mMyAdapter.notifyDataSetChanged();
        mAiAdapter.notifyDataSetChanged();
    }

    private void fetchAndDisplayUserImage() {
        StorageManager manager = new StorageManager();
        Bitmap me = manager.FetchImage(SharedPreferencesManager.ReadUserId());
        if (me != null)
            mImageYou.setImageBitmap(me);
    }

    private void showTutorialOverlay() {
        new ShowcaseView.Builder(getActivity())
                .setTarget(ViewTarget.NONE)
                .setContentTitle("Welcome to Pick'em 5")
                .setContentText("Start by creating your team on the left")
                .hideOnTouchOutside()
                .singleShot(111)
                .build().hideButton();
    }

    private void fetchPlayers() {

        allAvailablePlayers = new ArrayList<Player>();
        for (String playerId : mDetails.getPlayerMappings().keySet()) {
            allAvailablePlayers.add(manager.GetPlayer(playerId));
        }
    }

    private boolean loadPlayers() {
        if (mDetails == null || mDetails.getPlayers() == null || mDetails.getPlayers().size() != 5 || mDetails.getAppPlayers().size() != 5)
            return false;
        List<Player> myPlayers = mDetails.getPlayers();
        for (Player myPlayer : myPlayers) {
            String aiPlayerId = mDetails.getPlayerMappings().get(myPlayer.getId());
            Player aiPlayer = null;
            for (Player aiP : mDetails.getAppPlayers()) {
                if (aiP.getId().equals(aiPlayerId)) {
                    aiPlayer = aiP;
                    break;
                }
            }
            if (myPlayer.getType().equals("Batsman") && mMyAdapter.mTeam[0] == null) {
                mMyAdapter.mTeam[0] = myPlayer;
                mAiAdapter.mTeam[0] = aiPlayer;
                continue;
            } else if (myPlayer.getType().equals("Bowler") && mMyAdapter.mTeam[1] == null) {
                mMyAdapter.mTeam[1] = myPlayer;
                mAiAdapter.mTeam[1] = aiPlayer;
                continue;
            } else if (myPlayer.getType().equals("AllRounder") && mMyAdapter.mTeam[2] == null) {
                mMyAdapter.mTeam[2] = myPlayer;
                mAiAdapter.mTeam[2] = aiPlayer;
                continue;
            } else if (myPlayer.getType().equals("WicketKeeper") && mMyAdapter.mTeam[3] == null) {
                mMyAdapter.mTeam[3] = myPlayer;
                mAiAdapter.mTeam[3] = aiPlayer;
                continue;
            } else {
                mMyAdapter.mTeam[4] = myPlayer;
                mAiAdapter.mTeam[4] = aiPlayer;
            }
        }
        return true;
    }

    private void initAdapters() {
        mMyAdapter = new Pick5TeamAdapter(getActivity(), true);
        mAiAdapter = new Pick5TeamAdapter(getActivity(), false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.breadcrumb_btsm)
    public void OnBatsmanBreadcrumbClick() {
        BusProvider.getInstance().post(new Pick5PlayerClickedEvent(0));
    }

    @OnClick(R.id.breadcrumb_bwlr)
    public void OnBowlerBreadcrumbClick() {
        BusProvider.getInstance().post(new Pick5PlayerClickedEvent(1));
    }

    @OnClick(R.id.breadcrumb_ar)
    public void OnAllRouonderBreadcrumbClick() {
        BusProvider.getInstance().post(new Pick5PlayerClickedEvent(2));
    }

    @OnClick(R.id.breadcrumb_wk)
    public void OnWkBreadcrumbClick() {
        BusProvider.getInstance().post(new Pick5PlayerClickedEvent(3));
    }

    @OnClick(R.id.breadcrumb_any)
    public void OnAnyBreadcrumbClick() {
        BusProvider.getInstance().post(new Pick5PlayerClickedEvent(4));
    }

    @Subscribe
    public void playerChosen(Pick5PlayerChosenEvent event) {
        mChoosePlayerDialog.dismiss();

        mMyAdapter.mTeam[event.position] = event.player;
        mAiAdapter.mTeam[event.position] = manager.GetPlayer(mDetails.getPlayerMappings().get(mMyAdapter.mTeam[event.position].getId()));

        resetAdapters();

        switch (event.position) {
            case 0: {

                mBreadcrumbBtsm.setFillColor(green);
                YoYo.with(Techniques.FlipInX).duration(700).playOn(mBreadcrumbBtsm);
                break;
            }
            case 1: {
                mBreadcrumbBwlr.setFillColor(green);
                YoYo.with(Techniques.FlipInX).duration(700).playOn(mBreadcrumbBwlr);
                break;
            }
            case 2: {
                mBreadcrumbAr.setFillColor(green);
                YoYo.with(Techniques.FlipInX).duration(700).playOn(mBreadcrumbAr);
                break;
            }
            case 3: {
                mBreadcrumbWk.setFillColor(green);
                YoYo.with(Techniques.FlipInX).duration(700).playOn(mBreadcrumbWk);
                break;
            }
            default: {
                mBreadcrumbAny.setFillColor(green);
                YoYo.with(Techniques.FlipInX).duration(700).playOn(mBreadcrumbAny);
                break;
            }
        }

    }

    @Subscribe
    public void onPlayerClick(Pick5PlayerClickedEvent event) {
        mChoosePlayerDialog = new Dialog(getActivity());
        mChoosePlayerDialog.setContentView(R.layout.layout_pick5_chooser_dialog);
        CoverFlowCarousel carousel = (CoverFlowCarousel) mChoosePlayerDialog.findViewById(R.id.carousel);
        Pick5ChoosePlayerAdapter adapter = null;
        switch (event.position) {
            case 0: {
                mChoosePlayerDialog.setTitle("Pick your Batsman");
                adapter = new Pick5ChoosePlayerAdapter(getActivity(), TeamHelper.getBatsmen(allAvailablePlayers), 0);
                break;
            }

            case 1: {
                mChoosePlayerDialog.setTitle("Pick your Bowler");
                adapter = new Pick5ChoosePlayerAdapter(getActivity(), TeamHelper.getBowlers(allAvailablePlayers), 1);
                break;
            }

            case 2: {
                mChoosePlayerDialog.setTitle("Pick your All Rounder");
                adapter = new Pick5ChoosePlayerAdapter(getActivity(), TeamHelper.getAllRounders(allAvailablePlayers), 2);
                break;
            }

            case 3: {
                mChoosePlayerDialog.setTitle("Pick your Wicket Keeper");
                adapter = new Pick5ChoosePlayerAdapter(getActivity(), TeamHelper.getWicketKeepers(allAvailablePlayers), 3);
                break;
            }
            default: {
                mChoosePlayerDialog.setTitle("Pick your Wild Card Player");
                adapter = new Pick5ChoosePlayerAdapter(getActivity(), allAvailablePlayers, 4);
                break;
            }

        }
        carousel.setAdapter(adapter);
        mChoosePlayerDialog.show();


    }

    @OnClick(R.id.btn_pick5_play)
    public void onSubmitClick() {

        for(int i = 0; i < mMyAdapter.mTeam.length; i++)
        {
            if(mMyAdapter.mTeam[i] == null)
            {
                SnackBarManager.showError("Your team is missing players", getActivity());
                return;
            }
        }


        JSONObject request = new JSONObject();

        JSONArray mappings = new JSONArray();
        try {
            request.put("MatchId", mDetails.getMatchDetails().getId());
            request.put("UserId", SharedPreferencesManager.ReadUserId());
            mappings.put(new JSONObject(new HashMap<String, String>() {{
                put("UserPlayerId", mMyAdapter.mTeam[0].getId());
                put("AppPlayerId", mAiAdapter.mTeam[0].getId());
            }}));
            mappings.put(new JSONObject(new HashMap<String, String>() {{
                put("UserPlayerId", mMyAdapter.mTeam[1].getId());
                put("AppPlayerId", mAiAdapter.mTeam[1].getId());
            }}));
            mappings.put(new JSONObject(new HashMap<String, String>() {{
                put("UserPlayerId", mMyAdapter.mTeam[2].getId());
                put("AppPlayerId", mAiAdapter.mTeam[2].getId());
            }}));
            mappings.put(new JSONObject(new HashMap<String, String>() {{
                put("UserPlayerId", mMyAdapter.mTeam[3].getId());
                put("AppPlayerId", mAiAdapter.mTeam[3].getId());
            }}));
            mappings.put(new JSONObject(new HashMap<String, String>() {{
                put("UserPlayerId", mMyAdapter.mTeam[4].getId());
                put("AppPlayerId", mAiAdapter.mTeam[4].getId());
            }}));

            request.put("PlayerMappings", mappings);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Http http = new Http();
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Saving your pick");
        progress.show();
        http.post(Urls.Pick5Urls.getSaveTeamUrl(), new HashMap<String, String>(), request, new APCallback() {
            @Override
            public void success(JSONObject result) {
                progress.dismiss();
                if (result.optJSONObject("Error") != null) {
                    SnackBarManager.showError(result.optJSONObject("Error").optString("ErrorMessage"), getActivity());
                    return;
                }
                SnackBarManager.showSuccess("Pick saved successfully", getActivity());
                ShowcaseView sv =
                        new ShowcaseView.Builder(getActivity())
                                .setTarget(ViewTarget.NONE)
                                .setContentTitle("Congratulations!")
                                .setContentText("Check back soon for results")
                                .hideOnTouchOutside()
                                .singleShot(555)
                                .build();
                sv.setShouldCentreText(true);
                sv.hideButton();
            }

            @Override
            public void failure(Exception e) {
                progress.dismiss();
                SnackBarManager.showError("Something went wrong", getActivity());
            }
        });

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

}