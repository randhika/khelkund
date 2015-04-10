package com.appacitive.khelkund.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.activities.pick5.Pick5MatchActivity;
import com.appacitive.khelkund.infra.transforms.CircleTransform;
import com.appacitive.khelkund.model.Pick5MatchDetails;
import com.appacitive.khelkund.model.Player;
import com.appacitive.khelkund.model.TeamHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Pick5FinishedReadonlyFragment extends Fragment {

    private Pick5MatchDetails mDetails;

    private static final int empty_background_color = Color.parseColor("#ffd3d3d3");

    //  My items

    @InjectView(R.id.my_bowler)
    public ImageView mMyBowler;

    @InjectView(R.id.my_bowler_score)
    public TextView mMyBowlerScore;

    @InjectView(R.id.my_bowler_name)
    public TextView mMyBowlerName;

    @InjectView(R.id.my_batsman)
    public ImageView mMyBatsman;

    @InjectView(R.id.my_batsman_score)
    public TextView mMyBatsmanScore;

    @InjectView(R.id.my_batsman_name)
    public TextView mMyBatsmanName;

    @InjectView(R.id.my_allrounder)
    public ImageView mMyAllRounder;

    @InjectView(R.id.my_allrounder_score)
    public TextView mMyAllRounderScore;

    @InjectView(R.id.my_allrounder_name)
    public TextView mMyAllRounderName;

    @InjectView(R.id.my_wk)
    public ImageView mMyWicketKeeper;

    @InjectView(R.id.my_wk_score)
    public TextView mMyWicketKeeperScore;

    @InjectView(R.id.my_wk_name)
    public TextView mMyWicketKeeperName;

    @InjectView(R.id.my_any)
    public ImageView mMyAny;

    @InjectView(R.id.my_any_score)
    public TextView mMyAnyScore;

    @InjectView(R.id.my_any_name)
    public TextView mMyAnyName;

//    AI items

    @InjectView(R.id.ai_bowler)
    public ImageView mAiBowler;

    @InjectView(R.id.ai_bowler_score)
    public TextView mAiBowlerScore;

    @InjectView(R.id.ai_bowler_name)
    public TextView mAiBowlerName;

    @InjectView(R.id.ai_batsman)
    public ImageView mAiBatsman;

    @InjectView(R.id.ai_batsman_score)
    public TextView mAiBatsmanScore;

    @InjectView(R.id.ai_batsman_name)
    public TextView mAiBatsmanName;

    @InjectView(R.id.ai_allrounder)
    public ImageView mAiAllRounder;

    @InjectView(R.id.ai_allrounder_score)
    public TextView mAiAllRounderScore;

    @InjectView(R.id.ai_allrounder_name)
    public TextView mAiAllRounderName;

    @InjectView(R.id.ai_wk)
    public ImageView mAiWicketKeeper;

    @InjectView(R.id.ai_wk_score)
    public TextView mAiWicketKeeperScore;

    @InjectView(R.id.ai_wk_name)
    public TextView mAiWicketKeeperName;

    @InjectView(R.id.ai_any)
    public ImageView mAiAny;

    @InjectView(R.id.ai_any_score)
    public TextView mAiAnyScore;

    @InjectView(R.id.ai_any_name)
    public TextView mAiAnyName;

    //  result
    @InjectView(R.id.tv_pick5_readonly_result)
    public TextView mResult;

    @InjectView(R.id.tv_readonly_score)
    public TextView mScore;

    @InjectView(R.id.btn_readonly_share)
    public Button mShare;


    public Pick5FinishedReadonlyFragment() {
        // Required empty public constructor
    }

    public static Pick5FinishedReadonlyFragment newInstance() {
        Pick5FinishedReadonlyFragment fragment = new Pick5FinishedReadonlyFragment();
        return fragment;
    }

    public Pick5FinishedReadonlyFragment.Pick5Team myTeam = new Pick5Team();
    public Pick5FinishedReadonlyFragment.Pick5Team aiTeam = new Pick5Team();

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

        View rootView = inflater.inflate(R.layout.fragment_pick5_finished_readonly, container, false);
        ButterKnife.inject(this, rootView);
        this.mDetails = ((Pick5MatchActivity) getActivity()).getMatchDetails();
        displayResults();
        loadPlayers();
        displayPlayers();

        return rootView;
    }

    private void displayResults() {

        if (mDetails.getResult() == 1) {
            mResult.setText("This match is under progress");
            mScore.setText("TBD");
            return;
        }

        if (mDetails.getTeamPoints() > mDetails.getAppTeamPoints()) {
            mResult.setText("You won this match");
            mResult.setTextColor(getActivity().getResources().getColor(R.color.accent));
        }
        if (mDetails.getTeamPoints() < mDetails.getAppTeamPoints()) {
            mResult.setText("You lost this match");
            mResult.setTextColor(getActivity().getResources().getColor(R.color.primary));
        }
        if (mDetails.getTeamPoints() == mDetails.getAppTeamPoints()) {
            mResult.setText("This match was a tie");
            mResult.setTextColor(getActivity().getResources().getColor(R.color.primary_dark));
        }

        mScore.setText(String.valueOf(mDetails.getTeamPoints()));
    }

    private void displayPlayers() {
        int myBorderColor = 0;
        int aiBorderColor = 0;

        //  show batsmen details

        if (mDetails.getResult() == 1)
            myBorderColor = aiBorderColor = 0;
        else {
            myBorderColor = (myTeam.Batsman.getPoints() > aiTeam.Batsman.getPoints()) ? (Color.GREEN) : Color.RED;
            aiBorderColor = (myTeam.Batsman.getPoints() > aiTeam.Batsman.getPoints()) ? (Color.RED) : Color.GREEN;
        }

        Picasso.with(getActivity()).load(myTeam.Batsman.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(myTeam.Batsman.getShortTeamName())), myBorderColor))
                .into(mMyBatsman);
        mMyBatsmanName.setText(myTeam.Batsman.getDisplayName());
        mMyBatsmanScore.setText(String.valueOf(myTeam.Batsman.getPoints()));
        Picasso.with(getActivity()).load(aiTeam.Batsman.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(aiTeam.Batsman.getShortTeamName())), aiBorderColor)).into(mAiBatsman);
        mAiBatsmanScore.setText(String.valueOf(aiTeam.Batsman.getPoints()));
        mAiBatsmanName.setText(aiTeam.Batsman.getDisplayName());

        //  show bowler details

        if (mDetails.getResult() == 1)
            myBorderColor = aiBorderColor = 0;
        else {
            myBorderColor = (myTeam.Bowler.getPoints() > aiTeam.Bowler.getPoints()) ? (Color.GREEN) : Color.RED;
            aiBorderColor = (myTeam.Bowler.getPoints() > aiTeam.Bowler.getPoints()) ? (Color.RED) : Color.GREEN;
        }
        Picasso.with(getActivity()).load(myTeam.Bowler.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(myTeam.Bowler.getShortTeamName())), myBorderColor)).into(mMyBowler);
        mMyBowlerName.setText(myTeam.Bowler.getDisplayName());
        mMyBowlerScore.setText(String.valueOf(myTeam.Bowler.getPoints()));
        Picasso.with(getActivity()).load(aiTeam.Bowler.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(aiTeam.Bowler.getShortTeamName())), aiBorderColor)).into(mAiBowler);
        mAiBowlerScore.setText(String.valueOf(aiTeam.Bowler.getPoints()));
        mAiBowlerName.setText(aiTeam.Bowler.getDisplayName());

        //  show all rounder details

        if (mDetails.getResult() == 1)
            myBorderColor = aiBorderColor = 0;
        else {
            myBorderColor = (myTeam.AllRounder.getPoints() > aiTeam.AllRounder.getPoints()) ? (Color.GREEN) : Color.RED;
            aiBorderColor = (myTeam.AllRounder.getPoints() > aiTeam.AllRounder.getPoints()) ? (Color.RED) : Color.GREEN;
        }
        Picasso.with(getActivity()).load(myTeam.AllRounder.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(myTeam.AllRounder.getShortTeamName())), myBorderColor)).into(mMyAllRounder);
        mMyAllRounderName.setText(myTeam.AllRounder.getDisplayName());
        mMyAllRounderScore.setText(String.valueOf(myTeam.AllRounder.getPoints()));
        Picasso.with(getActivity()).load(aiTeam.AllRounder.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(aiTeam.AllRounder.getShortTeamName())), aiBorderColor)).into(mAiAllRounder);
        mAiAllRounderScore.setText(String.valueOf(aiTeam.AllRounder.getPoints()));
        mAiAllRounderName.setText(aiTeam.AllRounder.getDisplayName());

        //  show wicket keeper details

        if (mDetails.getResult() == 1)
            myBorderColor = aiBorderColor = 0;
        else {
            myBorderColor = (myTeam.WicketKeeper.getPoints() > aiTeam.WicketKeeper.getPoints()) ? (Color.GREEN) : Color.RED;
            aiBorderColor = (myTeam.WicketKeeper.getPoints() > aiTeam.WicketKeeper.getPoints()) ? (Color.RED) : Color.GREEN;
        }
        Picasso.with(getActivity()).load(myTeam.WicketKeeper.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(myTeam.WicketKeeper.getShortTeamName())), myBorderColor)).into(mMyWicketKeeper);
        mMyWicketKeeperName.setText(myTeam.WicketKeeper.getDisplayName());
        mMyWicketKeeperScore.setText(String.valueOf(myTeam.WicketKeeper.getPoints()));
        Picasso.with(getActivity()).load(aiTeam.WicketKeeper.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(aiTeam.WicketKeeper.getShortTeamName())), aiBorderColor)).into(mAiWicketKeeper);
        mAiWicketKeeperScore.setText(String.valueOf(aiTeam.WicketKeeper.getPoints()));
        mAiWicketKeeperName.setText(aiTeam.WicketKeeper.getDisplayName());

        //  show any player details

        if (mDetails.getResult() == 1)
            myBorderColor = aiBorderColor = 0;
        else {
            myBorderColor = (myTeam.Any.getPoints() > aiTeam.Any.getPoints()) ? (Color.GREEN) : Color.RED;
            aiBorderColor = (myTeam.Any.getPoints() > aiTeam.Any.getPoints()) ? (Color.RED) : Color.GREEN;
        }
        Picasso.with(getActivity()).load(myTeam.Any.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(myTeam.Any.getShortTeamName())), myBorderColor)).into(mMyAny);
        mMyAnyName.setText(myTeam.Any.getDisplayName());
        mMyAnyScore.setText(String.valueOf(myTeam.Any.getPoints()));
        Picasso.with(getActivity()).load(aiTeam.Any.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(aiTeam.Any.getShortTeamName())), aiBorderColor)).into(mAiAny);
        mAiAnyScore.setText(String.valueOf(aiTeam.Any.getPoints()));
        mAiAnyName.setText(aiTeam.Any.getDisplayName());
    }

    private void loadPlayers() {
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
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


}
