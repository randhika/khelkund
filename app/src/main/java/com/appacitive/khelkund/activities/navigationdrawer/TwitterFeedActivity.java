package com.appacitive.khelkund.activities.navigationdrawer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.ConnectionManager;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TwitterFeedActivity extends ActionBarActivity {

    @InjectView(R.id.twitter_feed)
    public ListView mTwitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_feed);
        ButterKnife.inject(this);
        ConnectionManager.checkNetworkConnectivity(this);
        final SearchTimeline searchTimeline = new SearchTimeline.Builder()
                .query("#IPL")
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(this, searchTimeline);
        mTwitter.setAdapter(adapter);
    }
}
