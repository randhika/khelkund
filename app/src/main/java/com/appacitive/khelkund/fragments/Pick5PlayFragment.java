package com.appacitive.khelkund.fragments;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.model.Pick5MatchDetails;

public class Pick5PlayFragment extends Fragment {

    private Pick5MatchDetails mDetails;


    public Pick5PlayFragment() {
        // Required empty public constructor
    }

    public static Pick5PlayFragment newInstance(Pick5MatchDetails details)
    {
        Pick5PlayFragment fragment = new Pick5PlayFragment();
        fragment.mDetails = details;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_pick5_play, container, false);
    }


}
