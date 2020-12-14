package com.mobo.horoscope.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mobo.horoscope.common.AlarmPushManager;
import com.mobo.horoscope.common.Constants;
import com.mobo.horoscope.common.PushNotificationManager;

/**
 * @Description: 闹钟广播接收器
 * @Author: jzhou
 * @CreateDate: 19-8-17 下午5:30
 */
public class AlarmPushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Constants.ALARM_BROADCAST_ACTION.equals(intent.getAction())) {
            return;
        }
        //发送推送通知
        PushNotificationManager.showNotification(context);

        AlarmPushManager.startAlarmPush4KitKat(context);
    }
}
