package com.appacitive.khelkund.activities.navigationdrawer;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.appacitive.khelkund.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

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
            mCode.setError("Invalid code");
        }
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Applying Referral Code");
    }
}
