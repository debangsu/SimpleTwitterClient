package com.codepath.apps.basictwitter.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.activities.ThirdPartyActivity;
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
        ivProfileImage.setTag(tweet.getUser().getScreenName());
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ThirdPartyActivity.class);
                i.putExtra("screenName", v.findViewById(R.id.ivProfileImage).getTag().toString());
                v.getContext().startActivity(i);
            }
        });

        if (tweet.getUser() != null) {
            TextView tvUserName = (TextView) v.findViewById(R.id.tvUserName);
            tvUserName.setText(Html.fromHtml("<b> " + tweet.getUser().getName() + " </b>"));
            TextView tvUserScreenName = (TextView) v.findViewById(R.id.tvUserScreenName);
            tvUserScreenName.setText(tweet.getUser().getScreenName());
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
        }
        TextView tvBody = (TextView) v.findViewById(R.id.tvBody);
        tvBody.setText(tweet.getBody());
        TextView tvRelativeTime = (TextView) v.findViewById(R.id.tvRelativeTime);
        tvRelativeTime.setText(DateTimeUtils.getRelativeTimeAgo(tweet.getCreatedAt()));
        return v;
    }
}
