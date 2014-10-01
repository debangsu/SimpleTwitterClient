package com.codepath.apps.basictwitter.helpers;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Utility methods related to date and time.
 */
public class DateTimeUtils {
    /**
     * Get date time as a time relative to now. Returned in abbreviated format for compact display.
     */
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
        Date convertDate = null;
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            convertDate = new Date(dateMillis);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ERROR", "Error parsing relative time");
        }

        Calendar now = Calendar.getInstance();
        Calendar then = Calendar.getInstance();

        now.setTime(new Date());
        then.setTime(convertDate);

        // Get the represented date in milliseconds
        long nowMs = now.getTimeInMillis();
        long thenMs = then.getTimeInMillis();

        // Calculate difference in milliseconds
        long diff = nowMs - thenMs;

        // Calculate difference in seconds
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);

        if (diffMinutes < 60) {
            return diffMinutes + "m";

        } else if (diffHours < 24) {
            return diffHours + "h";

        } else if (diffDays < 7) {
            return diffDays + "d";

        } else {
            SimpleDateFormat todate = new SimpleDateFormat("MMM dd",
                    Locale.ENGLISH);
            return todate.format(convertDate);
        }
    }
}
