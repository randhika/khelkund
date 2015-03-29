package com.appacitive.khelkund.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appacitive.khelkund.R;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        callbackManager = CallbackManager.Factory.create();
        facebookLoginButton = (LoginButton) view.findViewById(R.id.facebook_login_button);
        facebookLoginButton.setFragment(this);
        facebookLoginButton.registerCallback(callbackManager, facebookSessionCallback);

        digitsLoginButton = (DigitsAuthButton) view.findViewById(R.id.auth_button);
        digitsLoginButton.setAuthTheme(android.R.style.Theme_Material_Light_DarkActionBar);
        digitsLoginButton.setCallback(digitsSessionCallback);

        twitterLoginButton = (TwitterLoginButton) view.findViewById(R.id.twitter_login_button);
        twitterLoginButton.setCallback(twitterSessionCallback);

        return view;
    }

    private Callback<TwitterSession> twitterSessionCallback = new Callback<TwitterSession>() {
        @Override
        public void success(Result<TwitterSession> result) {

        }

        @Override
        public void failure(TwitterException e) {

        }
    };

    private AuthCallback digitsSessionCallback = new AuthCallback() {
        @Override
        public void success(DigitsSession digitsSession, String s) {

        }

        @Override
        public void failure(DigitsException e) {

        }
    };

    private FacebookCallback<LoginResult> facebookSessionCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }


}
