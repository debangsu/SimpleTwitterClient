package com.codepath.apps.basictwitter.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.helpers.DateTimeUtils;
import com.codepath.apps.basictwitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * TweetAdapter is an ArrayAdapter that adapts a List of Tweet objects to the context's view.
 */
public class TweetAdapter extends ArrayAdapter<Tweet> {
    public TweetAdapter(Context context, List<Tweet> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        View v;
        if (convertView == null) {
            LayoutInflater inflator = LayoutInflater.from(getContext());
            v = inflator.inflate(R.layout.tweet_item, parent, false);
        } else {
            v = convertView;
        }
        // Find views within template
        ImageView ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);
        ivProfileImage.setImageResource(android.R.color.transparent);
        TextView tvUserName = (TextView) v.findViewById(R.id.tvUserName);
        tvUserName.setText(Html.fromHtml("<b> " + tweet.getUser().getName() + " </b>"));
        TextView tvUserScreenName = (TextView) v.findViewById(R.id.tvUserScreenName);
        tvUserScreenName.setText(tweet.getUser().getScreenName());
        TextView tvBody = (TextView) v.findViewById(R.id.tvBody);
        tvBody.setText(tweet.getBody());
        TextView tvRelativeTime = (TextView) v.findViewById(R.id.tvRelativeTime);
        tvRelativeTime.setText(DateTimeUtils.getRelativeTimeAgo(tweet.getCreatedAt()));
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
        return v;
    }
}
