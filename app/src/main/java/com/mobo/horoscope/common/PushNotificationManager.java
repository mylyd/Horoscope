package com.mobo.horoscope.common;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.mobo.horoscope.R;
import com.mobo.horoscope.activity.MainActivity;

/**
 * Created by Administrator on 2017/7/12.
 */
public class PushNotificationManager {

    public static void showNotification(Context context) {
        android.app.NotificationManager mNotificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String appName = context.getString(R.string.app_name);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, appName);
        mBuilder.setContentTitle(appName)//设置通知栏标题
                .setContentText(context.getString(R.string.a_friend_of_yours_checks_your_horoscope_for_the_day)) //设置通知栏显示内容
                .setContentIntent(createPendingIntent(context, Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图
                //.setNumber(number) //设置通知集合的数量
                .setTicker(context.getString(R.string.notify_ticker)) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.mipmap.ic_app_logo)//设置通知小ICON
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_app_logo));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {// 8.0及以上
            createNotificationChannel(mNotificationManager, appName, appName, NotificationManager.IMPORTANCE_DEFAULT);
        }
        mNotificationManager.notify(1, mBuilder.build());
    }

    @TargetApi(Build.VERSION_CODES.O)
    private static void createNotificationChannel(NotificationManager notificationManager, String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        notificationManager.createNotificationChannel(channel);
    }

    public static PendingIntent createPendingIntent(Context context, int flags) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        return PendingIntent.getActivity(context, 1, intent, flags);
    }
}
