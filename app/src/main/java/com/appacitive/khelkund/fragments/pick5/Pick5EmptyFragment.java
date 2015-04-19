package com.appacitive.khelkund.fragments.pick5;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.model.Pick5MatchDetails;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Pick5EmptyFragment extends Fragment {

    private Pick5MatchDetails mDetails;


    @InjectView(R.id.iv_match_over_flag)
    public ImageView mFlag;

    public Pick5EmptyFragment() {
        // Required empty public constructor
    }

    public static Pick5EmptyFragment newInstance(Pick5MatchDetails details) {
        Pick5EmptyFragment fragment = new Pick5EmptyFragment();
        fragment.mDetails = details;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pick5_empty, container, false);
        ButterKnife.inject(this, view);
        YoYo.with(Techniques.ZoomIn).duration(1000).playOn(mFlag);
        return view;
    }


}
