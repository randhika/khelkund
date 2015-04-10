package com.appacitive.khelkund.fragments;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.model.Pick5MatchDetails;

/**
 * A simple {@link Fragment} subclass.
 */
public class Pick5EmptyFragment extends Fragment {

    private Pick5MatchDetails mDetails;


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
        return inflater.inflate(R.layout.fragment_pick5_empty, container, false);
    }


}
