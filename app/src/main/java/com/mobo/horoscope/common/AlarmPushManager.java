package com.mobo.horoscope.common;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;

public class AlarmPushManager {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void startAlarmPush4KitKat(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null) {
            return;
        }

        PendingIntent pendingIntent = buildPendingIntent(context);

        am.setWindow(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + AlarmManager.INTERVAL_HALF_DAY,
                AlarmManager.INTERVAL_HOUR, pendingIntent);
    }

    public static void startAlarmPush(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null) {
            return;
        }

        PendingIntent pendingIntent = buildPendingIntent(context);

        Calendar calendar = getCalendar();
        //版本适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 19及以上
            am.setWindow(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_HALF_DAY, pendingIntent);
        } else {
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_HALF_DAY, pendingIntent);
        }
    }

    public static void stopAlarmPush(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null) {
            return;
        }

        PendingIntent pendingIntent = buildPendingIntent(context);
        am.cancel(pendingIntent);
    }

    private static Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 7) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Log.d("Calendar", calendar.toString());
        return calendar;
    }

    private static PendingIntent buildPendingIntent(Context context) {
        Intent intent = new Intent(Constants.ALARM_BROADCAST_ACTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setComponent(new ComponentName(context.getApplicationContext(),
                    "com.mobo.horoscope.receiver.AlarmPushReceiver"));
        }
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}