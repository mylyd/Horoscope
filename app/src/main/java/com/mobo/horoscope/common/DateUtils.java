package com.mobo.horoscope.common;

import android.app.AlarmManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dfwm on 2016/6/24.
 */
public class DateUtils {

    public static String formatDateTime(long datetime, String formatPattern) {
        SimpleDateFormat format = new SimpleDateFormat(formatPattern, Locale.getDefault());
        return format.format(new Date(datetime));
    }

    public static int compareDate(long from, long to) {
        return (int) (from / AlarmManager.INTERVAL_DAY - to / AlarmManager.INTERVAL_DAY);
    }
}
