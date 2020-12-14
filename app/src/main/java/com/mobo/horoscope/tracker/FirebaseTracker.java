package com.mobo.horoscope.tracker;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * @Description: tracker埋点
 * @Author: jzhou
 * @CreateDate: 19-8-16 上午8:38
 */
public class FirebaseTracker {

    private static Context mContext;
    private static FirebaseTracker mFirebaseTracker;

    public static FirebaseTracker getInstance() {
        if (mFirebaseTracker == null) {
            synchronized (FirebaseTracker.class) {
                if (mFirebaseTracker == null) {
                    mFirebaseTracker = new FirebaseTracker();
                }
            }
        }
        return mFirebaseTracker;
    }

    public void init(Context context) {
        mContext = context;
    }

    public void track(String eventName) {
        if (mContext == null) {
            throw new IllegalStateException("FirebaseTracker should be initialzed first.");
        }
        //在这里截取40长度，因为firebase的eventName超过40会报错
        if (eventName.length() > 40)
            eventName = eventName.substring(0, 39);
        FirebaseAnalytics.getInstance(mContext).logEvent(eventName, null);
    }

    public void track(String eventName, Bundle bundle) {
        if (mContext == null) {
            throw new IllegalStateException("FirebaseTracker should be initialzed first.");
        }
        //在这里截取40长度，因为firebase的eventName超过40会报错
        if (eventName.length() > 40)
            eventName = eventName.substring(0, 39);
        FirebaseAnalytics.getInstance(mContext).logEvent(eventName, bundle);
    }
}
