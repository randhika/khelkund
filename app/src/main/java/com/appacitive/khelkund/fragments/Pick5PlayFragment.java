package com.appacitive.khelkund.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.model.Pick5MatchDetails;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Pick5PlayFragment extends Fragment {

    private Pick5MatchDetails mDetails;

    private static final int empty_background_color = Color.parseColor("#ffd3d3d3");

    @InjectView(R.id.my_bowler)
    public ImageView mMyBowler;

    @InjectView(R.id.my_bowler_score)
    public TextView mMyBowlerScore;

    @InjectView(R.id.my_batsman)
    public ImageView mMyBatsman;

    @InjectView(R.id.my_batsman_score)
    public TextView mMyBatsmanScore;

    @InjectView(R.id.my_allrounder)
    public ImageView mMyAllRounder;

    @InjectView(R.id.my_allrounder_score)
    public TextView mMyAllRounderScore;

    @InjectView(R.id.my_wk)
    public ImageView mMyWicketKeeper;

    @InjectView(R.id.my_wk_score)
    public TextView mMyWicketKeeperScore;

    @InjectView(R.id.my_any)
    public ImageView mMyAny;

    @InjectView(R.id.my_any_score)
    public TextView mMyAnyScore;

//    AI items

    @InjectView(R.id.ai_bowler)
    public ImageView mAiBowler;

    @InjectView(R.id.ai_bowler_score)
    public TextView mAiBowlerScore;

    @InjectView(R.id.ai_batsman)
    public ImageView mAiBatsman;

    @InjectView(R.id.ai_batsman_score)
    public TextView mAiBatsmanScore;

    @InjectView(R.id.ai_allrounder)
    public ImageView mAiAllRounder;

    @InjectView(R.id.ai_allrounder_score)
    public TextView mAiAllRounderScore;

    @InjectView(R.id.ai_wk)
    public ImageView mAiWicketKeeper;

    @InjectView(R.id.ai_wk_score)
    public TextView mAiWicketKeeperScore;

    @InjectView(R.id.ai_any)
    public ImageView mAiAny;

    @InjectView(R.id.ai_any_score)
    public TextView mAiAnyScore;

    public Pick5PlayFragment() {
        // Required empty public constructor
    }

    public static Pick5PlayFragment newInstance(Pick5MatchDetails details) {
        Pick5PlayFragment fragment = new Pick5PlayFragment();
        fragment.mDetails = details;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pick5_play, container, false);
        ButterKnife.inject(this, view);

        loadPlayerImages();

        return view;
    }

    private void loadPlayerImages() {
    }

}


//Picasso.with(getActivity()).load(R.drawable.demo2).resize(200, 300).centerInside().transform(new CircleTransform(color)).into(mMyBatsman);
//        Picasso.with(getActivity()).load(R.drawable.batsman).resize(200, 200).centerInside().onlyScaleDown().transform(new CircleTransform2(empty_background_color)).into(mMyBowler);