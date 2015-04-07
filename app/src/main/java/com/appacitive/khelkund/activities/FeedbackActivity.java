package com.appacitive.khelkund.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.appacitive.core.AppacitiveEmail;
import com.appacitive.core.model.Callback;
import com.appacitive.core.model.RawEmailBody;
import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.SnackBarManager;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FeedbackActivity extends ActionBarActivity {

    @InjectView(R.id.et_feedback)
    public EditText mFeedbackForm;

    @InjectView(R.id.btn_feedback)
    public Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.btn_feedback)
    public void onSubmit()
    {
        //  hide keyboard
        InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        String userId = SharedPreferencesManager.ReadUserId();
        final String to1 = "khelkund@gmail.com";
        final String to2 = "asinha@tavisca.com";
        String message = mFeedbackForm.getText().toString();
        if(TextUtils.isEmpty(message))
            return;
        String subject = "Khelkund Feedback : User " + userId;


        AppacitiveEmail email = new AppacitiveEmail(subject).withBody(new RawEmailBody(message, false));
        email.to.add(to1);
        email.cc.add(to2);
        email.sendInBackground(new Callback<AppacitiveEmail>() {
            @Override
            public void success(AppacitiveEmail result) {
                SnackBarManager.showSuccess("Thank you for your feedback", FeedbackActivity.this);
            }

            @Override
            public void failure(AppacitiveEmail result, Exception e) {
                SnackBarManager.showError("Unable to send feedback at the moment", FeedbackActivity.this);
            }
        });
    }


}
