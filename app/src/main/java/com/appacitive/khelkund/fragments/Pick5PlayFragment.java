package com.appacitive.khelkund.fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.activities.pick5.Pick5MatchActivity;
import com.appacitive.khelkund.adapters.Pick5PlayerAdapter;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.SnackBarManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.infra.carousel.CoverFlowCarousel;
import com.appacitive.khelkund.infra.transforms.CircleTransform;
import com.appacitive.khelkund.infra.transforms.CircleTransform2;
import com.appacitive.khelkund.model.Pick5MatchDetails;
import com.appacitive.khelkund.model.Player;
import com.appacitive.khelkund.model.TeamHelper;
import com.appacitive.khelkund.model.events.pick5.Pick5AllRounderChosenEvent;
import com.appacitive.khelkund.model.events.pick5.Pick5AnyPlayerChosenEvent;
import com.appacitive.khelkund.model.events.pick5.Pick5BatsmanChosenEvent;
import com.appacitive.khelkund.model.events.pick5.Pick5BowlerChosenEvent;
import com.appacitive.khelkund.model.events.pick5.Pick5WicketKeeperChosenEvent;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class Pick5PlayFragment extends Fragment {

    private Pick5MatchDetails mDetails;

    private Dialog mDialog;

    private static final int empty_background_color = Color.parseColor("#ffd3d3d3");

    //  My items

    @InjectView(R.id.my_bowler)
    public ImageView mMyBowler;

    @InjectView(R.id.my_bowler_name)
    public TextView mMyBowlerName;

    @InjectView(R.id.my_batsman)
    public ImageView mMyBatsman;

    @InjectView(R.id.my_batsman_name)
    public TextView mMyBatsmanName;

    @InjectView(R.id.my_allrounder)
    public ImageView mMyAllRounder;

    @InjectView(R.id.my_allrounder_name)
    public TextView mMyAllRounderName;

    @InjectView(R.id.my_wk)
    public ImageView mMyWicketKeeper;

    @InjectView(R.id.my_wk_name)
    public TextView mMyWicketKeeperName;

    @InjectView(R.id.my_any)
    public ImageView mMyAny;

    @InjectView(R.id.my_any_name)
    public TextView mMyAnyName;

//    AI items

    @InjectView(R.id.ai_bowler)
    public ImageView mAiBowler;

    @InjectView(R.id.ai_bowler_name)
    public TextView mAiBowlerName;

    @InjectView(R.id.ai_batsman)
    public ImageView mAiBatsman;

    @InjectView(R.id.ai_batsman_name)
    public TextView mAiBatsmanName;

    @InjectView(R.id.ai_allrounder)
    public ImageView mAiAllRounder;

    @InjectView(R.id.ai_allrounder_name)
    public TextView mAiAllRounderName;

    @InjectView(R.id.ai_wk)
    public ImageView mAiWicketKeeper;

    @InjectView(R.id.ai_wk_name)
    public TextView mAiWicketKeeperName;

    @InjectView(R.id.ai_any)
    public ImageView mAiAny;

    @InjectView(R.id.ai_any_name)
    public TextView mAiAnyName;

    @InjectView(R.id.btn_pick5_play)
    public Button mSubmit;

    public Pick5PlayFragment() {
        // Required empty public constructor
    }

    private StorageManager manager;

    public static Pick5PlayFragment newInstance() {
        Pick5PlayFragment fragment = new Pick5PlayFragment();
        return fragment;
    }

    private List<Player> allAvailablePlayers;

    public Pick5Team myTeam = new Pick5Team();
    public Pick5Team aiTeam = new Pick5Team();

    public class Pick5Team {
        public Player Batsman;
        public Player Bowler;
        public Player AllRounder;
        public Player WicketKeeper;
        public Player Any;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pick5_play, container, false);
        ButterKnife.inject(this, view);
        manager = new StorageManager();
        this.mDetails = ((Pick5MatchActivity) getActivity()).getMatchDetails();
        boolean teamExists = loadPlayers();

        if (teamExists == true) {
            displayPlayers();
            mSubmit.setEnabled(true);
        } else {
            loadEmptySlots();
            mSubmit.setEnabled(false);
        }

        fetchPlayers();

        showTutorialOverlay();
        return view;
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

    private void loadEmptySlots() {

        Picasso.with(getActivity()).load(R.drawable.batsman).resize(250, 375).centerInside().onlyScaleDown().transform(new CircleTransform2(empty_background_color)).into(mMyBatsman);
        Picasso.with(getActivity()).load(R.drawable.bowler).resize(250, 375).centerInside().onlyScaleDown().transform(new CircleTransform2(empty_background_color)).into(mMyBowler);
        Picasso.with(getActivity()).load(R.drawable.allrounder).resize(250, 375).centerInside().onlyScaleDown().transform(new CircleTransform2(empty_background_color)).into(mMyAllRounder);
        Picasso.with(getActivity()).load(R.drawable.wicketkeeper).resize(250, 375).centerInside().onlyScaleDown().transform(new CircleTransform2(empty_background_color)).into(mMyWicketKeeper);
        Picasso.with(getActivity()).load(R.drawable.any).resize(250, 375).centerInside().onlyScaleDown().transform(new CircleTransform2(empty_background_color)).into(mMyAny);

        Picasso.with(getActivity()).load(R.drawable.batsman).resize(250, 375).centerInside().onlyScaleDown().transform(new CircleTransform2(empty_background_color)).into(mAiBatsman);
        Picasso.with(getActivity()).load(R.drawable.bowler).resize(250, 375).centerInside().onlyScaleDown().transform(new CircleTransform2(empty_background_color)).into(mAiBowler);
        Picasso.with(getActivity()).load(R.drawable.allrounder).resize(250, 375).centerInside().onlyScaleDown().transform(new CircleTransform2(empty_background_color)).into(mAiAllRounder);
        Picasso.with(getActivity()).load(R.drawable.wicketkeeper).resize(250, 375).centerInside().onlyScaleDown().transform(new CircleTransform2(empty_background_color)).into(mAiWicketKeeper);
        Picasso.with(getActivity()).load(R.drawable.any).resize(250, 375).centerInside().onlyScaleDown().transform(new CircleTransform2(empty_background_color)).into(mAiAny);
    }

    private boolean loadPlayers() {
        List<Player> myPlayers = mDetails.getPlayers();
        if (myPlayers.size() == 0)
            return false;
        for (Player myPlayer : myPlayers) {
            String aiPlayerId = mDetails.getPlayerMappings().get(myPlayer.getId());
            Player aiPlayer = null;
            for (Player aiP : mDetails.getAppPlayers()) {
                if (aiP.getId().equals(aiPlayerId)) {
                    aiPlayer = aiP;
                    break;
                }
            }
            if (myPlayer.getType().equals("Batsman") && myTeam.Batsman == null) {
                myTeam.Batsman = myPlayer;
                aiTeam.Batsman = aiPlayer;
                continue;
            } else if (myPlayer.getType().equals("Bowler") && myTeam.Bowler == null) {
                myTeam.Bowler = myPlayer;
                aiTeam.Bowler = aiPlayer;
                continue;
            } else if (myPlayer.getType().equals("AllRounder") && myTeam.AllRounder == null) {
                myTeam.AllRounder = myPlayer;
                aiTeam.AllRounder = aiPlayer;
                continue;
            } else if (myPlayer.getType().equals("WicketKeeper") && myTeam.WicketKeeper == null) {
                myTeam.WicketKeeper = myPlayer;
                aiTeam.WicketKeeper = aiPlayer;
                continue;
            } else {
                myTeam.Any = myPlayer;
                aiTeam.Any = aiPlayer;
            }

        }
        return true;
    }

    private void displayPlayers() {

        //  show batsmen details

        Picasso.with(getActivity()).load(myTeam.Batsman.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(myTeam.Batsman.getShortTeamName()))))
                .into(mMyBatsman);
        mMyBatsmanName.setText(myTeam.Batsman.getDisplayName());

        Picasso.with(getActivity()).load(aiTeam.Batsman.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(aiTeam.Batsman.getShortTeamName())))).into(mAiBatsman);
        mAiBatsmanName.setText(aiTeam.Batsman.getDisplayName());

        //  show bowler details

        Picasso.with(getActivity()).load(myTeam.Bowler.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(myTeam.Bowler.getShortTeamName())))).into(mMyBowler);
        mMyBowlerName.setText(myTeam.Bowler.getDisplayName());

        Picasso.with(getActivity()).load(aiTeam.Bowler.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(aiTeam.Bowler.getShortTeamName())))).into(mAiBowler);
        mAiBowlerName.setText(aiTeam.Bowler.getDisplayName());

        //  show all rounder details

        Picasso.with(getActivity()).load(myTeam.AllRounder.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(myTeam.AllRounder.getShortTeamName())))).into(mMyAllRounder);
        mMyAllRounderName.setText(myTeam.AllRounder.getDisplayName());

        Picasso.with(getActivity()).load(aiTeam.AllRounder.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(aiTeam.AllRounder.getShortTeamName())))).into(mAiAllRounder);
        mAiAllRounderName.setText(aiTeam.AllRounder.getDisplayName());

        //  show wicket keeper details

        Picasso.with(getActivity()).load(myTeam.WicketKeeper.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(myTeam.WicketKeeper.getShortTeamName())))).into(mMyWicketKeeper);
        mMyWicketKeeperName.setText(myTeam.WicketKeeper.getDisplayName());

        Picasso.with(getActivity()).load(aiTeam.WicketKeeper.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(aiTeam.WicketKeeper.getShortTeamName())))).into(mAiWicketKeeper);
        mAiWicketKeeperName.setText(aiTeam.WicketKeeper.getDisplayName());

        //  show any player details

        Picasso.with(getActivity()).load(myTeam.Any.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(myTeam.Any.getShortTeamName())))).into(mMyAny);
        mMyAnyName.setText(myTeam.Any.getDisplayName());

        Picasso.with(getActivity()).load(aiTeam.Any.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(aiTeam.Any.getShortTeamName())))).into(mAiAny);
        mAiAnyName.setText(aiTeam.Any.getDisplayName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Subscribe
    public void batsmanChosen(Pick5BatsmanChosenEvent event) {
        mDialog.dismiss();

        YoYo.with(Techniques.FadeIn).duration(1000).playOn(mMyBatsman);

        myTeam.Batsman = event.player;
        Picasso.with(getActivity()).load(myTeam.Batsman.getImageUrl()).resize(200, 300).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(myTeam.Batsman.getShortTeamName()))))
                .into(mMyBatsman);
        mMyBatsmanName.setText(myTeam.Batsman.getDisplayName());

        YoYo.with(Techniques.SlideInRight).duration(1000).playOn(mAiBatsman);

        aiTeam.Batsman = manager.GetPlayer(mDetails.getPlayerMappings().get(myTeam.Batsman.getId()));
        Picasso.with(getActivity()).load(aiTeam.Batsman.getImageUrl()).resize(200, 300).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(aiTeam.Batsman.getShortTeamName()))))
                .into(mAiBatsman);
        mAiBatsmanName.setText(aiTeam.Batsman.getDisplayName());


        if (myTeam.Batsman != null && myTeam.Bowler != null && myTeam.AllRounder != null && myTeam.Any != null && myTeam.WicketKeeper != null)
        {
            mSubmit.setEnabled(true);
            new ShowcaseView.Builder(getActivity())
                    .setTarget(new ViewTarget(mSubmit))
                    .setContentTitle("SAVE your team once you have decided all your players.")
                    .hideOnTouchOutside()
                    .singleShot(444)
                    .build().hideButton();
        }


        new ShowcaseView.Builder(getActivity())
                .setTarget(new ViewTarget(mAiBatsman))
                .setContentTitle("The app automatically assigns an opponent for your chosen batsman")
                .hideOnTouchOutside()
                .singleShot(222)
                .build().hideButton();
    }

    @Subscribe
    public void bowlerChosen(Pick5BowlerChosenEvent event) {
        mDialog.dismiss();

        YoYo.with(Techniques.FadeIn).duration(1000).playOn(mMyBowler);
        myTeam.Bowler = event.player;
        Picasso.with(getActivity()).load(myTeam.Bowler.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(myTeam.Bowler.getShortTeamName()))))
                .into(mMyBowler);
        mMyBowlerName.setText(myTeam.Bowler.getDisplayName());

        YoYo.with(Techniques.SlideInRight).duration(1000).playOn(mAiBowler);
        aiTeam.Bowler = manager.GetPlayer(mDetails.getPlayerMappings().get(myTeam.Bowler.getId()));
        Picasso.with(getActivity()).load(aiTeam.Bowler.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(aiTeam.Bowler.getShortTeamName()))))
                .into(mAiBowler);
        mAiBowlerName.setText(aiTeam.Bowler.getDisplayName());

        if (myTeam.Batsman != null && myTeam.Bowler != null && myTeam.AllRounder != null && myTeam.Any != null && myTeam.WicketKeeper != null)
        {
            mSubmit.setEnabled(true);
            new ShowcaseView.Builder(getActivity())
                    .setTarget(new ViewTarget(mSubmit))
                    .setContentTitle("SAVE your team once you have decided all your players.")
                    .hideOnTouchOutside()
                    .singleShot(444)
                    .build().hideButton();
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                new ShowcaseView.Builder(getActivity())
                        .setTarget(new ViewTarget(mAiBowler))
                        .setContentTitle("The app automatically assigns an opponent for your chosen bowler")
                        .hideOnTouchOutside()
                        .singleShot(222)
                        .build().hideButton();
            }
        };
        new Handler().postDelayed(runnable, 1000);

    }

    @Subscribe
    public void allRounderChosen(Pick5AllRounderChosenEvent event) {
        mDialog.dismiss();
        YoYo.with(Techniques.FadeIn).duration(1000).playOn(mMyAllRounder);
        myTeam.AllRounder = event.player;
        Picasso.with(getActivity()).load(myTeam.AllRounder.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(myTeam.AllRounder.getShortTeamName()))))
                .into(mMyAllRounder);
        mMyAllRounderName.setText(myTeam.AllRounder.getDisplayName());

        YoYo.with(Techniques.SlideInRight).duration(1000).playOn(mAiAllRounder);
        aiTeam.AllRounder = manager.GetPlayer(mDetails.getPlayerMappings().get(myTeam.AllRounder.getId()));
        Picasso.with(getActivity()).load(aiTeam.AllRounder.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(aiTeam.AllRounder.getShortTeamName()))))
                .into(mAiAllRounder);
        mAiAllRounderName.setText(aiTeam.AllRounder.getDisplayName());

        if (myTeam.Batsman != null && myTeam.Bowler != null && myTeam.AllRounder != null && myTeam.Any != null && myTeam.WicketKeeper != null)
        {
            mSubmit.setEnabled(true);
            new ShowcaseView.Builder(getActivity())
                    .setTarget(new ViewTarget(mSubmit))
                    .setContentTitle("SAVE your team once you have decided all your players.")
                    .hideOnTouchOutside()
                    .singleShot(444)
                    .build().hideButton();
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                new ShowcaseView.Builder(getActivity())
                        .setTarget(new ViewTarget(mAiAllRounder))
                        .setContentTitle("The app automatically assigns an opponent for your chosen all rounder")
                        .hideOnTouchOutside()
                        .singleShot(222)
                        .build().hideButton();
            }
        };
        new Handler().postDelayed(runnable, 1000);
    }

    @Subscribe
    public void wicketKeeperChosen(Pick5WicketKeeperChosenEvent event) {
        mDialog.dismiss();
        YoYo.with(Techniques.FadeIn).duration(1000).playOn(mMyWicketKeeper);
        myTeam.WicketKeeper = event.player;
        Picasso.with(getActivity()).load(myTeam.WicketKeeper.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(myTeam.WicketKeeper.getShortTeamName()))))
                .into(mMyWicketKeeper);
        mMyWicketKeeperName.setText(myTeam.WicketKeeper.getDisplayName());

        YoYo.with(Techniques.SlideInRight).duration(1000).playOn(mAiWicketKeeper);
        aiTeam.WicketKeeper = manager.GetPlayer(mDetails.getPlayerMappings().get(myTeam.WicketKeeper.getId()));
        Picasso.with(getActivity()).load(aiTeam.WicketKeeper.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(aiTeam.WicketKeeper.getShortTeamName()))))
                .into(mAiWicketKeeper);
        mAiWicketKeeperName.setText(aiTeam.WicketKeeper.getDisplayName());

        if (myTeam.Batsman != null && myTeam.Bowler != null && myTeam.AllRounder != null && myTeam.Any != null && myTeam.WicketKeeper != null)
        {
            mSubmit.setEnabled(true);
            new ShowcaseView.Builder(getActivity())
                    .setTarget(new ViewTarget(mSubmit))
                    .setContentTitle("SAVE your team once you have decided all your players.")
                    .hideOnTouchOutside()
                    .singleShot(444)
                    .build().hideButton();
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                new ShowcaseView.Builder(getActivity())
                        .setTarget(new ViewTarget(mAiWicketKeeper))
                        .setContentTitle("The app automatically assigns an opponent for your chosen wicket keeper")
                        .hideOnTouchOutside()
                        .singleShot(222)
                        .build().hideButton();
            }
        };
        new Handler().postDelayed(runnable, 1000);
    }

    @Subscribe
    public void wildCardChosen(Pick5AnyPlayerChosenEvent event) {
        mDialog.dismiss();
        YoYo.with(Techniques.FadeIn).duration(1000).playOn(mMyAny);
        myTeam.Any = event.player;
        Picasso.with(getActivity()).load(myTeam.Any.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(myTeam.Any.getShortTeamName()))))
                .into(mMyAny);
        mMyAnyName.setText(myTeam.Any.getDisplayName());

        YoYo.with(Techniques.SlideInRight).duration(1000).playOn(mAiAny);
        aiTeam.Any = manager.GetPlayer(mDetails.getPlayerMappings().get(myTeam.Any.getId()));
        Picasso.with(getActivity()).load(aiTeam.Any.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(aiTeam.Any.getShortTeamName()))))
                .into(mAiAny);
        mAiAnyName.setText(aiTeam.Any.getDisplayName());

        if (myTeam.Batsman != null && myTeam.Bowler != null && myTeam.AllRounder != null && myTeam.Any != null && myTeam.WicketKeeper != null)
        {
            mSubmit.setEnabled(true);
            new ShowcaseView.Builder(getActivity())
                    .setTarget(new ViewTarget(mSubmit))
                    .setContentTitle("SAVE your team once you have decided all your players.")
                    .hideOnTouchOutside()
                    .singleShot(444)
                    .build().hideButton();
        }

        new ShowcaseView.Builder(getActivity())
                .setTarget(new Target() {
                    @Override
                    public Point getPoint() {
                        int[] coordinates = new int[2];
                        mMyAny.getLocationOnScreen(coordinates);
                        return new Point(coordinates[0], coordinates[1]);
                    }
                })
                .setContentTitle("Wildcard player")
                .setContentText("This player can be of any type. But should not already be part of your team.")
                .hideOnTouchOutside()
                .singleShot(333)
                .build().hideButton();
    }

    @OnClick(R.id.my_batsman)
    public void onBatsmanClick() {
        mDialog = new Dialog(getActivity());
        mDialog.setContentView(R.layout.layout_pick5_chooser_dialog);
        mDialog.setTitle("Pick your batsman");
        CoverFlowCarousel carousel = (CoverFlowCarousel) mDialog.findViewById(R.id.carousel);
        final Pick5PlayerAdapter adapter = new Pick5PlayerAdapter(getActivity(), TeamHelper.getBatsmen(allAvailablePlayers), new Pick5BatsmanChosenEvent());
        carousel.setAdapter(adapter);
        mDialog.show();
    }

    @OnClick(R.id.my_bowler)
    public void onBowlerClick() {
        mDialog = new Dialog(getActivity());
        mDialog.setContentView(R.layout.layout_pick5_chooser_dialog);
        mDialog.setTitle("Pick your bowler");
        CoverFlowCarousel carousel = (CoverFlowCarousel) mDialog.findViewById(R.id.carousel);
        final Pick5PlayerAdapter adapter = new Pick5PlayerAdapter(getActivity(), TeamHelper.getBowlers(allAvailablePlayers), new Pick5BowlerChosenEvent());
        carousel.setAdapter(adapter);
        mDialog.show();
    }

    @OnClick(R.id.my_allrounder)
    public void onAllRounderClick() {
        mDialog = new Dialog(getActivity());
        mDialog.setContentView(R.layout.layout_pick5_chooser_dialog);
        mDialog.setTitle("Pick your all rounder");
        CoverFlowCarousel carousel = (CoverFlowCarousel) mDialog.findViewById(R.id.carousel);
        final Pick5PlayerAdapter adapter = new Pick5PlayerAdapter(getActivity(), TeamHelper.getAllRounders(allAvailablePlayers), new Pick5AllRounderChosenEvent());
        carousel.setAdapter(adapter);
        mDialog.show();
    }

    @OnClick(R.id.my_wk)
    public void onWicketKeeperClick() {
        mDialog = new Dialog(getActivity());
        mDialog.setContentView(R.layout.layout_pick5_chooser_dialog);
        mDialog.setTitle("Pick your wicket keeper");
        CoverFlowCarousel carousel = (CoverFlowCarousel) mDialog.findViewById(R.id.carousel);
        final Pick5PlayerAdapter adapter = new Pick5PlayerAdapter(getActivity(), TeamHelper.getWicketKeepers(allAvailablePlayers), new Pick5WicketKeeperChosenEvent());
        carousel.setAdapter(adapter);
        mDialog.show();
    }

    @OnClick(R.id.my_any)
    public void onAnyPlayerClick() {
        mDialog = new Dialog(getActivity());
        mDialog.setContentView(R.layout.layout_pick5_chooser_dialog);
        mDialog.setTitle("Pick your wildcard player");
        CoverFlowCarousel carousel = (CoverFlowCarousel) mDialog.findViewById(R.id.carousel);
        final Pick5PlayerAdapter adapter = new Pick5PlayerAdapter(getActivity(), allAvailablePlayers, new Pick5AnyPlayerChosenEvent());
        carousel.setAdapter(adapter);
        mDialog.show();


    }

    @OnClick(R.id.btn_pick5_play)
    public void onSubmitClick() {
        JSONObject request = new JSONObject();

        JSONArray mappings = new JSONArray();
        try {
            request.put("MatchId", mDetails.getMatchDetails().getId());
            request.put("UserId", SharedPreferencesManager.ReadUserId());
            mappings.put(new JSONObject(new HashMap<String, String>() {{
                put("UserPlayerId", myTeam.Batsman.getId());
                put("AppPlayerId", aiTeam.Batsman.getId());
            }}));
            mappings.put(new JSONObject(new HashMap<String, String>() {{
                put("UserPlayerId", myTeam.Bowler.getId());
                put("AppPlayerId", aiTeam.Bowler.getId());
            }}));
            mappings.put(new JSONObject(new HashMap<String, String>() {{
                put("UserPlayerId", myTeam.AllRounder.getId());
                put("AppPlayerId", aiTeam.AllRounder.getId());
            }}));
            mappings.put(new JSONObject(new HashMap<String, String>() {{
                put("UserPlayerId", myTeam.WicketKeeper.getId());
                put("AppPlayerId", aiTeam.WicketKeeper.getId());
            }}));
            mappings.put(new JSONObject(new HashMap<String, String>() {{
                put("UserPlayerId", myTeam.Any.getId());
                put("AppPlayerId", aiTeam.Any.getId());
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
                new ShowcaseView.Builder(getActivity())
                        .setTarget(ViewTarget.NONE)
                        .setContentTitle("Congratulations")
                        .setContentText("You can check back here after the match to see the result.")
                        .hideOnTouchOutside()
                        .singleShot(555)
                        .build().hideButton();
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