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
import com.appacitive.khelkund.infra.transforms.CircleTransform2;
import com.appacitive.khelkund.model.Pick5MatchDetails;
import com.appacitive.khelkund.model.Player;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class Pick5PlayFragment extends Fragment {

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

    @InjectView(R.id.btn_pick5_play)
    public Button mSubmit;

    public Pick5PlayFragment() {
        // Required empty public constructor
    }

    public static Pick5PlayFragment newInstance() {
        Pick5PlayFragment fragment = new Pick5PlayFragment();
        return fragment;
    }

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
        this.mDetails = ((Pick5MatchActivity) getActivity()).getMatchDetails();
        loadEmptySlots();

        return view;
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

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.my_batsman)
    public void onBatsmanClick()
    {

    }

    @OnClick(R.id.my_bowler)
    public void onBowlerClick()
    {

    }

    @OnClick(R.id.my_allrounder)
    public void onAllRounderClick()
    {

    }

    @OnClick(R.id.my_wk)
    public void onWicketKeeperClick()
    {

    }

    @OnClick(R.id.my_any)
    public void onAnyPlayerClick()
    {

    }

}