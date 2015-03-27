package com.appacitive.khelkund.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.activities.CreateTeamActivity;
import com.appacitive.khelkund.activities.HomeActivity;
import com.appacitive.khelkund.activities.ViewTeamActivity;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.model.Team;
import com.appacitive.khelkund.model.User;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeFragment extends Fragment {

    private User mUser;

    private Team mTeam;


//    @InjectView(R.id.tv_fantasy_teamname)
//    public TextView mFantasyName;
//
//    @InjectView(R.id.tv_fantasy_nextmatch)
//    public TextView mFantasyLastMathPlayed;
//
//    @InjectView(R.id.tv_fantasy_points)
//    public TextView mFantasyLastMatchScore;
//
//    @InjectView(R.id.layout_fantasyleague)
//    public LinearLayout mFantasyLeague;

    @InjectView(R.id.card_view_fantasy)
    public CardView mFantasy;

    public static HomeFragment newInstance(int sectionNumber) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private static final String ARG_SECTION_NUMBER = "section_number";

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, rootView);
        String userId = SharedPreferencesManager.ReadUserId();
        final StorageManager manager = new StorageManager();
        mUser = manager.GetUser(userId);


        mFantasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTeam = manager.GetTeam(mUser.getId());

                mTeam = null;
                if(mTeam != null)
                {
                    Intent viewTeamIntent = new Intent(getActivity(), ViewTeamActivity.class);
                    startActivity(viewTeamIntent);
                }
                else
                {
                    Intent createTeamIntent = new Intent(getActivity(), CreateTeamActivity.class);
                    startActivity(createTeamIntent);
                }
            }
        });


//        mFantasyName.setText(mTeam.getName());
//        mFantasyLastMathPlayed.setText(mTeam.getPreviousMatch());
//        mFantasyLastMatchScore.setText(String.valueOf(mTeam.getTeamHistory().last().getPoints()));
//
//        mFantasyLeague.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent viewTeamIntent = new Intent(getActivity(), ViewTeamActivity.class);
//                startActivity(viewTeamIntent);
//            }
//        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
