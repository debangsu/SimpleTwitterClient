package com.codepath.apps.basictwitter.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.fragments.HomeTimelineFragment;
import com.codepath.apps.basictwitter.fragments.MentionsTimelineFragment;
import com.codepath.apps.basictwitter.listeners.SupportFragmentTabListener;

/**
 * Represents activity for interacting with a user's home timeline.
 * We can populate the timeline with latest tweets, update current logged in
 * user's credentials, launch a ComposeActivity to compose a tweet and insert it
 * to the timeline.
 */
public class TimelineActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        setupTabs();
    }

    private void setupTabs() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        ActionBar.Tab tab1 = actionBar
                .newTab()
                .setText("HOME")
                .setIcon(R.drawable.ic_home)
                .setTag("HomeTimelineFragment")
                .setTabListener(new SupportFragmentTabListener<HomeTimelineFragment>(
                        R.id.flContainer, this, "home", HomeTimelineFragment.class));

        actionBar.addTab(tab1);
        actionBar.selectTab(tab1);

        ActionBar.Tab tab2 = actionBar
                .newTab()
                .setText("MENTIONS")
                .setIcon(R.drawable.ic_mentions)
                .setTag("MentionsTimelineFragment")
                .setTabListener(new SupportFragmentTabListener<MentionsTimelineFragment>(
                        R.id.flContainer, this, "mentions", MentionsTimelineFragment.class));
        actionBar.addTab(tab2);
    }
}
