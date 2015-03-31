package com.appacitive.khelkund.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.activities.CreateTeamActivity;
import com.appacitive.khelkund.activities.HomeActivity;
import com.appacitive.khelkund.activities.Pick5HomeActivity;
import com.appacitive.khelkund.activities.ViewTeamActivity;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.ConnectionManager;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.SnackBarManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.model.Team;
import com.appacitive.khelkund.model.User;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeFragment extends Fragment {

    private User mUser;

    private Team mTeam;

    @InjectView(R.id.tv_rank)
    public TextView mRank;

    @InjectView(R.id.tv_points)
    public TextView mPoints;

    @InjectView(R.id.tv_name)
    public TextView mName;

    @InjectView(R.id.card_view_fantasy)
    public CardView mFantasy;

    @InjectView(R.id.card_view_pick5)
    public CardView mPick5;

    private ProgressDialog mProgressDialog;

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
        ConnectionManager.checkNetworkConnectivity(getActivity());
        String userId = SharedPreferencesManager.ReadUserId();
        final StorageManager manager = new StorageManager();
        mUser = manager.GetUser(userId);

        String name = mUser.getFirstName();
        if (mUser.getLastName() != null && mUser.getLastName().equals("null") == false)
            name += " " + mUser.getLastName();
        mName.setText(name);

        fetchTeam(userId);
        if(mTeam == null)
        {
            mTeam = manager.GetTeam(userId);
        }
        mFantasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTeam != null && mTeam.getId() != null && TextUtils.isEmpty(mTeam.getId()) == false) {
                    Intent viewTeamIntent = new Intent(getActivity(), ViewTeamActivity.class);
                    startActivity(viewTeamIntent);
                    getActivity().finish();
                } else {
                    Intent createTeamIntent = new Intent(getActivity(), CreateTeamActivity.class);
                    startActivity(createTeamIntent);
                }
            }
        });

        mPick5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Pick5HomeActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void fetchTeam(final String userId) {
        Http http = new Http();
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Fetching your team");
        mProgressDialog.show();
        http.get(Urls.TeamUrls.getMyTeamUrl(userId), new HashMap<String, String>(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                mProgressDialog.dismiss();
                if (result.optJSONObject("Error") != null) {
                    SnackBarManager.showError(result.optJSONObject("Error").optString("ErrorMessage"), getActivity());
                    return;
                }
                if (result.optString("Id") != null) {

                    mTeam = new Team(result);
                    mRank.setText(String.valueOf(mTeam.getRank()));
                    mPoints.setText(String.valueOf(mTeam.getTotalPoints()));
                    mTeam.setUserId(userId);
                    StorageManager storageManager = new StorageManager();
                    storageManager.SaveTeam(mTeam);
                }
            }

            @Override
            public void failure(Exception e) {
                mProgressDialog.dismiss();
                SnackBarManager.showError("Unable to fetch your team.", getActivity());
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
