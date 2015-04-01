package com.appacitive.khelkund.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appacitive.khelkund.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * A simple {@link Fragment} subclass.
 */
public class Pick5PlayFragment extends Fragment {


    public Pick5PlayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        YoYo.with(Techniques.SlideInLeft).playOn()
        return inflater.inflate(R.layout.fragment_pick5_play, container, false);
    }


}
