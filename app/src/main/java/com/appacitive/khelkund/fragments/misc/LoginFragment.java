package com.appacitive.khelkund.fragments.misc;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.activities.misc.EmailLoginActivity;
import com.appacitive.khelkund.activities.misc.HomeActivity;
import com.appacitive.khelkund.activities.misc.RegisterActivity;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.SnackBarManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.model.KhelkundUser;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    DigitsAuthButton digitsLoginButton;
    LoginButton facebookLoginButton;
    CallbackManager callbackManager;
    TwitterLoginButton twitterLoginButton;

    ProgressDialog progressDialog;

    @InjectView(R.id.btn_signup_email)
    public Button mEmailSignup;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        callbackManager = CallbackManager.Factory.create();
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.inject(this, view);
        //  Logout
        LoginManager.getInstance().logOut();
        Twitter.logOut();
        Digits.getSessionManager().clearActiveSession();

        facebookLoginButton = (LoginButton) view.findViewById(R.id.facebook_login_button);
        facebookLoginButton.setBackgroundResource(R.drawable.f_bg);
        facebookLoginButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        facebookLoginButton.setFragment(this);
        facebookLoginButton.registerCallback(callbackManager, facebookSessionCallback);

        digitsLoginButton = (DigitsAuthButton) view.findViewById(R.id.digits_login_button);
        digitsLoginButton.setBackgroundResource(R.drawable.white_bg);
        digitsLoginButton.setText("Use my phone number");
        digitsLoginButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        digitsLoginButton.setAuthTheme(android.R.style.ThemeOverlay_Material_Dark_ActionBar);
        digitsLoginButton.setCallback(digitsSessionCallback);
        digitsLoginButton.setVisibility(View.INVISIBLE);

        twitterLoginButton = (TwitterLoginButton) view.findViewById(R.id.twitter_login_button);
        twitterLoginButton.setBackgroundResource(R.drawable.t_bg);
        twitterLoginButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        twitterLoginButton.setCallback(twitterSessionCallback);

        mEmailSignup.setBackgroundResource(R.drawable.white_bg);
        return view;
    }


    private void fireLoginCall(String url, JSONObject payload) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("LOGGING IN");
        progressDialog.show();
        Http http = new Http();
        http.post(url, new HashMap<String, String>(), payload, new APCallback() {
            @Override
            public void success(JSONObject result) {
                progressDialog.dismiss();
                if (result.optJSONObject("Error") != null) {
                    SnackBarManager.showError(result.optJSONObject("Error").optString("ErrorMessage"), getActivity());
                    return;
                }

                KhelkundUser user = new KhelkundUser(result.optJSONObject("User"));
                SharedPreferencesManager.WriteUserId(user.getId());
                StorageManager storageManager = new StorageManager();
                storageManager.SaveUser(user);

                Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                startActivity(homeIntent);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_right_fast, R.anim.slide_out_left_fast);
            }

            @Override
            public void failure(Exception e) {
                progressDialog.dismiss();
                SnackBarManager.showError("Something went wrong", getActivity());
            }
        });

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
                request.put("ConsumerKey", "IjYVjFetPBvINUGr5JxxbP99V");
                request.put("ConsumerSecret", "gnTvCVeDBp0GSPRYDtCDiAWeGoC1HEAMs6AOBZxeOcC5bURWpS");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            fireLoginCall(Urls.UserUrls.getTwitterLoginUrl(), request);

        }

        @Override
        public void failure(TwitterException e) {
            SnackBarManager.showError("Something went wrong", getActivity());
        }
    };

    private AuthCallback digitsSessionCallback = new AuthCallback() {
        @Override
        public void success(DigitsSession digitsSession, String s) {
            String tokens = digitsSession.getAuthToken().toString();

            final JSONObject request = new JSONObject();
            String[] parts = tokens.split(",");
            String token = parts[0].substring(6);
            String secret = parts[1].substring(7);
            try {
                request.put("AccessToken", token);
                request.put("AccessSecret", secret);
                request.put("ConsumerKey", "IjYVjFetPBvINUGr5JxxbP99V");
                request.put("ConsumerSecret", "gnTvCVeDBp0GSPRYDtCDiAWeGoC1HEAMs6AOBZxeOcC5bURWpS");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            fireLoginCall(Urls.UserUrls.getTwitterLoginUrl(), request);
        }

        @Override
        public void failure(DigitsException e) {
            SnackBarManager.showError("Something went wrong", getActivity());
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

            fireLoginCall(Urls.UserUrls.getFacebookLoginUrl(), request);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {
            SnackBarManager.showError("Something went wrong", getActivity());
        }
    };

    @OnClick(R.id.btn_signup_email)
    public void onEmailSignupClick() {
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_login_email)
    public void onEmailLoginClick()
    {
        Intent intent = new Intent(getActivity(), EmailLoginActivity.class);
        startActivity(intent);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


}
