package com.appacitive.khelkund.navigationdrawer;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.appacitive.khelkund.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ScoringChartActivity extends ActionBarActivity {

    @InjectView(R.id.web_scoring_chart)
    public WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoring_chart);

        ButterKnife.inject(this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setClickable(true);
        String path = "file:///android_asset/scoring_chart.html";
        mWebView.loadUrl(path);
        mWebView.setBackgroundColor(Color.TRANSPARENT);
    }


}
