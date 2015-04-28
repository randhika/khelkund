package com.appacitive.khelkund.activities.navigationdrawer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.activities.misc.HomeActivity;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.SnackBarManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.model.KhelkundUser;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PromotionsActivity extends ActionBarActivity {

    @InjectView(R.id.et_promotion_code)
    public EditText mCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);
        ButterKnife.inject(this);

        mCode.requestFocus();
    }


    @OnClick(R.id.btn_promotion_apply)
    public void onApplyCLick()
    {
        mCode.setError(null);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        String code = mCode.getText().toString();
        if(TextUtils.isEmpty(code))
        {
            YoYo.with(Techniques.Shake).duration(700).playOn(mCode);
        }
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Applying Referral Code");


        String userId = SharedPreferencesManager.ReadUserId();
        KhelkundUser user = new StorageManager().GetUser(userId);

        JSONObject request = new JSONObject();
        try {
            request.put("UserId", userId);
            request.put("ReferralCode", code);
        } catch (JSONException e) {

        }

        Http http = new Http();
        http.post(Urls.UserUrls.getReferralCodeUrl(), new HashMap<String, String>(), request, new APCallback() {
            @Override
            public void success(JSONObject result) {
                dialog.dismiss();
                mCode.setText("");
                if (result.optJSONObject("Error") != null) {
                    mCode.setError(result.optJSONObject("Error").optString("ErrorMessage"));
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(PromotionsActivity.this);
                builder.setTitle("Referral code successfully applied");
                builder.setMessage("You received 1 extra transfer. Invite more friends and get up to 5 additional transfers.");
                builder.setIcon(R.drawable.ic_local_attraction_grey600_36dp);
                builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(PromotionsActivity.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right_fast, R.anim.slide_out_left_fast);
                    }
                });
                builder.setNegativeButton("ADD ANOTHER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mCode.requestFocus();
                    }
                });
                builder.show();

            }

            @Override
            public void failure(Exception e) {
                dialog.dismiss();
                mCode.setText("");
                mCode.setError("Something went wrong!");
                YoYo.with(Techniques.Shake).duration(700).playOn(mCode);
            }
        });

    }
}
