package com.appacitive.khelkund.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.activities.HomeActivity;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.KhelkundApplication;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.SnackBarManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.infra.runnables.FetchAllPlayersIntentService;
import com.appacitive.khelkund.infra.runnables.FetchMyPLayersIntentService;
import com.appacitive.khelkund.model.User;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.nispok.snackbar.Snackbar;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    DigitsAuthButton digitsLoginButton;
    LoginButton facebookLoginButton;
    CallbackManager callbackManager;
    TwitterLoginButton twitterLoginButton;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FacebookSdk.sdkInitialize(KhelkundApplication.getAppContext());
        callbackManager = CallbackManager.Factory.create();
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        facebookLoginButton = (LoginButton) view.findViewById(R.id.facebook_login_button);
        facebookLoginButton.setFragment(this);
        facebookLoginButton.registerCallback(callbackManager, facebookSessionCallback);

        digitsLoginButton = (DigitsAuthButton) view.findViewById(R.id.digits_login_button);
        digitsLoginButton.setAuthTheme(android.R.style.Theme_Material_Light_DarkActionBar);
        digitsLoginButton.setCallback(digitsSessionCallback);

        twitterLoginButton = (TwitterLoginButton) view.findViewById(R.id.twitter_login_button);
        twitterLoginButton.setCallback(twitterSessionCallback);

        return view;
    }

    private Callback<TwitterSession> twitterSessionCallback = new Callback<TwitterSession>() {
        @Override
        public void success(Result<TwitterSession> result) {
            TwitterSession session = Twitter.getSessionManager().getActiveSession();
            TwitterAuthToken authToken = session.getAuthToken();
            String token = authToken.token;
            String secret = authToken.secret;
            JSONObject request = new JSONObject();

            try {
                request.put("AccessToken", token);
                request.put("AccessSecret", secret);
                request.put("ConsumerKey", "N1J0SKJLkMAoQe2kERO9LaX6j");
                request.put("ConsumerSecret", "ehmqbKoGROgCV2Ynk8jL9TsC7laL5xPvjMWXti9xZBSdO7zgqP");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Http http = new Http();
            http.post(Urls.UserUrls.getTwitterLoginUrl(), new HashMap<String, String>(), request, new APCallback() {
                @Override
                public void success(JSONObject result) {
                    if(result.optJSONObject("Error") != null)
                    {
                        SnackBarManager.showMessage(result.optJSONObject("Error").optString("ErrorMessage"), getActivity());
                        return;
                    }

                    User user = new User(result);
                    SharedPreferencesManager.WriteUserId(user.getId());
                    StorageManager storageManager = new StorageManager();
                    storageManager.Save(user);

                    Intent mServiceIntent = new Intent(getActivity(), FetchMyPLayersIntentService.class);
                    getActivity().startService(mServiceIntent);
                    Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(homeIntent);
                    getActivity().finish();
                }

                @Override
                public void failure(Exception e) {
                    SnackBarManager.showMessage("Something went wrong", getActivity());
                }
            });

        }

        @Override
        public void failure(TwitterException e) {
            SnackBarManager.showMessage("Something went wrong", getActivity());
        }
    };

    private AuthCallback digitsSessionCallback = new AuthCallback() {
        @Override
        public void success(DigitsSession digitsSession, String s) {
            final JSONObject request = new JSONObject();

            try {
                request.put("AccessToken", digitsSession.getAuthToken());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Http http = new Http();
            http.post(Urls.UserUrls.getTwitterLoginUrl(), new HashMap<String, String>(), request, new APCallback() {
                @Override
                public void success(JSONObject result) {
                    if (result.optJSONObject("Error") != null) {
                        SnackBarManager.showMessage(result.optJSONObject("Error").optString("ErrorMessage"), getActivity());
                        return;
                    }
                    User user = new User(result);
                    SharedPreferencesManager.WriteUserId(user.getId());
                    StorageManager storageManager = new StorageManager();
                    storageManager.Save(user);

                    Intent mServiceIntent = new Intent(getActivity(), FetchMyPLayersIntentService.class);
                    getActivity().startService(mServiceIntent);
                    Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(homeIntent);
                    getActivity().finish();
                }

                @Override
                public void failure(Exception e) {
                    SnackBarManager.showMessage("Something went wrong", getActivity());
                }
            });
        }

        @Override
        public void failure(DigitsException e) {
            SnackBarManager.showMessage("Something went wrong", getActivity());
        }
    };

    private FacebookCallback<LoginResult> facebookSessionCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            final JSONObject request = new JSONObject();

            try {
                request.put("AccessToken", loginResult.getAccessToken().getToken());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Http http = new Http();
            http.post(Urls.UserUrls.getFacebookLoginUrl(), new HashMap<String, String>(), request, new APCallback() {
                @Override
                public void success(JSONObject result) {
                    if(result.optJSONObject("Error") != null)
                    {
                        SnackBarManager.showMessage(result.optJSONObject("Error").optString("ErrorMessage"), getActivity());
                        return;
                    }
                    User user = new User(result);
                    SharedPreferencesManager.WriteUserId(user.getId());
                    StorageManager storageManager = new StorageManager();
                    storageManager.Save(user);

                    Intent mServiceIntent = new Intent(getActivity(), FetchMyPLayersIntentService.class);
                    getActivity().startService(mServiceIntent);
                    Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(homeIntent);
                    getActivity().finish();
                }

                @Override
                public void failure(Exception e) {
                    SnackBarManager.showMessage("Something went wrong", getActivity());
                }
            });
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {
            SnackBarManager.showMessage("Something went wrong", getActivity());
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }


}
