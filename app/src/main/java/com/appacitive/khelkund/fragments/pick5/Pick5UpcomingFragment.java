package com.appacitive.khelkund.fragments.pick5;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appacitive.khelkund.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Pick5UpcomingFragment extends Fragment {

    @InjectView(R.id.iv_match_over_flag)
    public ImageView mTimer;

    public static Pick5UpcomingFragment newInstance() {
        Pick5UpcomingFragment fragment = new Pick5UpcomingFragment();
        return fragment;
    }

    public Pick5UpcomingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_pick5_upcoming, container, false);
        ButterKnife.inject(this, view);
        YoYo.with(Techniques.FadeIn).duration(1000).playOn(mTimer);
        return view;
    }


}
