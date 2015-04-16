package com.appacitive.khelkund.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.activities.CreateTeamActivity;
import com.appacitive.khelkund.activities.HomeActivity;
import com.appacitive.khelkund.activities.LoginActivity;
import com.appacitive.khelkund.activities.ViewTeamActivity;
import com.appacitive.khelkund.activities.pick5.Pick5HomeActivity;
import com.appacitive.khelkund.activities.privateleague.PrivateLeagueHomeActivity;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.ConnectionManager;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.model.KhelkundUser;
import com.appacitive.khelkund.model.Team;
import com.facebook.AccessToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class HomeFragment extends Fragment {

    private KhelkundUser mUser;

    private Team mTeam;

    private String userId;

    @InjectView(R.id.tv_rank)
    public TextView mRank;

    @InjectView(R.id.tv_points)
    public TextView mPoints;

    @InjectView(R.id.tv_name)
    public TextView mName;

    @InjectView(R.id.iv_home_photo)
    public ImageView mPhoto;

    private ProgressDialog mProgressDialog;
    private StorageManager manager;
    private TeamStatus mTeamStatus = TeamStatus.DONT_KNOW;

    private enum TeamStatus {
        CREATED,
        NOT_CREATED,
        DONT_KNOW
    }

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
        this.mTeamStatus = TeamStatus.DONT_KNOW;
        ConnectionManager.checkNetworkConnectivity(getActivity());
        manager = new StorageManager();
        userId = SharedPreferencesManager.ReadUserId();

        if (userId == null) {
            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(loginIntent);
            getActivity().finish();
        }

        mUser = manager.GetUser(userId);
        showUserBasicDetails();
        fetchTeam(userId);
        fetchProfileImage();


        return rootView;
    }

    private void fetchProfileImage() {
        final StorageManager manager = new StorageManager();
        Bitmap photo = manager.FetchImage(mUser.getId());
        if (photo != null) {
            mPhoto.setImageBitmap(photo);
            return;
        }

        // facebook
        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token != null) {
            String url = "http://graph.facebook.com/" + token.getUserId() + "/picture?type=large&redirect=false";
            Http http = new Http();
            http.get(url, new HashMap<String, String>(), new APCallback() {
                @Override
                public void success(JSONObject result) {
                    Picasso.with(getActivity()).load(result.optJSONObject("data").optString("url")).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            if (bitmap != null) {
                                manager.SaveImage(mUser.getId(), bitmap);
                                mPhoto.setImageBitmap(bitmap);
                            }
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });

                }
            });

        } else if (Twitter.getSessionManager().getActiveSession() != null) {
            Twitter.getApiClient().getAccountService().verifyCredentials(true, false, new Callback<User>() {
                @Override
                public void success(Result<User> result) {
                    if (result.data.profileImageUrl != null) {
                        Picasso.with(getActivity()).load(result.data.profileImageUrl.replace("_normal", "")).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                if (bitmap != null) {
                                    manager.SaveImage(mUser.getId(), bitmap);
                                    mPhoto.setImageBitmap(bitmap);
                                }
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });

                    }

                }

                @Override
                public void failure(TwitterException e) {

                }

            });
        } else Picasso.with(getActivity()).load(R.drawable.user).into(mPhoto);

    }

    private void showTeamBasicDetails() {
        mRank.setText(String.valueOf(mTeam.getRank()));
        mPoints.setText(String.valueOf(mTeam.getTotalPoints()));
    }

    private void showUserBasicDetails() {
        String name = mUser.getFirstName();
        if (mUser.getLastName() != null && mUser.getLastName().equals("null") == false)
            name += " " + mUser.getLastName();
        mName.setText(name);
    }

    private void refreshTeamDetails() {
        fetchTeam(SharedPreferencesManager.ReadUserId());
    }

    @OnClick(R.id.card_view_fantasy)
    public void onFantasyClick() {
        if (mTeamStatus == TeamStatus.CREATED) {
            Intent viewTeamIntent = new Intent(getActivity(), ViewTeamActivity.class);
            startActivity(viewTeamIntent);
        } else if (mTeamStatus == TeamStatus.NOT_CREATED) {
            Intent createTeamIntent = new Intent(getActivity(), CreateTeamActivity.class);
            startActivity(createTeamIntent);
        } else if (mTeamStatus == TeamStatus.DONT_KNOW) {

            Toast.makeText(getActivity(), "Unable to fetch team details at the moment. PLease try again later", Toast.LENGTH_SHORT).show();

        }

    }

    private boolean isTeamCreatedOnServer() {
        if (mTeam != null && mTeam.getId() != null && TextUtils.isEmpty(mTeam.getId()) == false)
            return true;
        else {
            return false;
        }
    }

    @OnClick(R.id.card_view_privateleague)
    public void onPrivateLeagueClick() {
        Intent intent = new Intent(getActivity(), PrivateLeagueHomeActivity.class);
        startActivity(intent);
//        Toast.makeText(getActivity(), "This game is currently unavailable. Check back soon.", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.card_view_pick5)
    public void onPick5Click() {
        Intent intent = new Intent(getActivity(), Pick5HomeActivity.class);
        startActivity(intent);
//        Toast.makeText(getActivity(),"This game is currently unavailable. Check back soon.", Toast.LENGTH_SHORT).show();
    }

    private void fetchTeam(final String userId) {
        Http http = new Http();
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Fetching your team");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        http.get(Urls.TeamUrls.getMyTeamUrl(userId), new HashMap<String, String>(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                mProgressDialog.dismiss();
                if (result.optJSONObject("Error") != null) {
                    mTeamStatus = TeamStatus.NOT_CREATED;
//                    SnackBarManager.showMessage("Start by creating a Fantasy team", getActivity());
                    return;
                }
                if (result.optString("Id") != null) {

                    mTeam = new Team(result);
                    mTeam.setUserId(userId);
                    StorageManager storageManager = new StorageManager();
                    storageManager.SaveTeam(mTeam);
                    mTeamStatus = TeamStatus.CREATED;
                    showTeamBasicDetails();
                }
            }

            @Override
            public void failure(Exception e) {
                mProgressDialog.dismiss();
                StorageManager manager = new StorageManager();
                mTeam = manager.GetTeam(userId);
                if (mTeam != null && mTeam.getId() != null) {
                    mTeamStatus = TeamStatus.CREATED;
                    showTeamBasicDetails();
                } else mTeamStatus = TeamStatus.DONT_KNOW;
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
