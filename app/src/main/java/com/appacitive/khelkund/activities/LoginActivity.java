package com.appacitive.khelkund.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.fragments.LoginFragment;
import com.facebook.shimmer.ShimmerFrameLayout;

public class LoginActivity extends ActionBarActivity {

    private int mLoginFragmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            LoginFragment loginFragment = new LoginFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, loginFragment).commit();
            mLoginFragmentId = loginFragment.getId();
        }
        ShimmerFrameLayout shimmerContainer =
                (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        shimmerContainer.setRepeatDelay(1200);
        shimmerContainer.startShimmerAnimation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(mLoginFragmentId);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }


}
