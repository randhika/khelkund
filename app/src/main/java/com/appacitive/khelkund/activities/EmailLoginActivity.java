package com.appacitive.khelkund.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.appacitive.core.AppacitiveEmail;
import com.appacitive.core.AppacitiveUser;
import com.appacitive.core.model.Callback;
import com.appacitive.khelkund.R;
import com.appacitive.khelkund.fragments.LoginFragment;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.ConnectionManager;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.SnackBarManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.infra.services.FetchMyPLayersIntentService;
import com.appacitive.khelkund.model.KhelkundUser;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class EmailLoginActivity extends ActionBarActivity implements LoaderCallbacks<Cursor> {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @InjectView(R.id.tv_register)
    TextView mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emaillogin);
        ConnectionManager.checkNetworkConnectivity(this);
        ButterKnife.inject(this);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.email_login_form);
        mProgressView = findViewById(R.id.login_progress);

        mRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(EmailLoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    public void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Email", mEmailView.getText().toString().trim());
                jsonObject.put("Password", mPasswordView.getText().toString().trim());
            } catch (JSONException e) {
                throw new RuntimeException(e.getMessage());
            }
            Http http = new Http();
            http.post(Urls.UserUrls.getLoginUrl(), new HashMap<String, String>(), jsonObject, new APCallback() {
                @Override
                public void success(JSONObject result) {
                    showProgress(false);
                    if (result.optJSONObject("Error") != null) {
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                        return;
                    }

                    SharedPreferencesManager.WriteUserId(result.optJSONObject("User").optString("UserId"));
                    KhelkundUser user = new KhelkundUser(result.optJSONObject("User"));
                    StorageManager manager = new StorageManager();
                    manager.SaveUser(user);

                    Intent mServiceIntent = new Intent(EmailLoginActivity.this, FetchMyPLayersIntentService.class);
                    startService(mServiceIntent);
                    Intent homeIntent = new Intent(EmailLoginActivity.this, HomeActivity.class);
                    startActivity(homeIntent);

                }

                @Override
                public void failure(Exception e) {
                    showProgress(false);
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 4;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(EmailLoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @OnClick(R.id.tv_forgot_password)
    public void onForgotPasswordClick()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forgot Password?");
        builder.setMessage("We'll email you instructions for resetting your password.");
        final EditText input = new EditText(this);
        // TODO add margin to input
        input.setHint("Email Address");
        builder.setView(input);
        builder.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String email = input.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    SnackBarManager.showError("You provided an invalid email address", EmailLoginActivity.this);
                    return;
                }
                sendResetPasswordEmail(email);
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void sendResetPasswordEmail(String email) {
        AppacitiveUser.sendResetPasswordEmailInBackground(email, "Reset your Khelkund password", new Callback<Void>() {
            @Override
            public void success(Void result) {
                SnackBarManager.showSuccess("Your request has been submitted.", EmailLoginActivity.this);
            }

            @Override
            public void failure(Void result, Exception e) {
                SnackBarManager.showError("Your request could not be submitted. Try again later.", EmailLoginActivity.this);
            }
        });
    }

}

