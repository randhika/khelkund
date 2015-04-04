package com.appacitive.khelkund.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.SharedPreferencesManager;

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
        String userId = SharedPreferencesManager.ReadUserId();
        String to1 = "khelkund@gmail.com";
//        String to2= "sathley@appacitive.com";
        String message = mFeedbackForm.getText().toString();
        if(TextUtils.isEmpty(message))
            return;
        String subject = "Khelkund Feedback : User " + userId;

        Intent mEmail = new Intent(Intent.ACTION_SEND);
        mEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{ to1});
        mEmail.putExtra(Intent.EXTRA_SUBJECT, subject);
        mEmail.putExtra(Intent.EXTRA_TEXT, message);

        // prompts to choose email client
        mEmail.setType("message/rfc822");

        startActivity(Intent.createChooser(mEmail, "Send email via"));
        finish();
    }


}
