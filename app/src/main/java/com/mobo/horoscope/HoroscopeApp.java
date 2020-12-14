package com.mobo.horoscope;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.ads.MobileAds;
import com.mobo.horoscope.bean.GrayItem;
import com.mobo.horoscope.common.AdsState;
import com.mobo.horoscope.common.MD5Util;
import com.mobo.horoscope.common.SystemUtils;
import com.mobo.horoscope.network.CommonCallback;
import com.mobo.horoscope.network.RetrofitNetwork;
import com.mobo.horoscope.tracker.FirebaseTracker;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;

/**
 * @Author: jzhou
 * @CreateDate: 19-8-12 下午8:11
 * @Description: TODO
 */
public class HoroscopeApp extends Application {
    private static final String TAG = "HoroscopeApp";

    @Override
    public void onCreate() {
        super.onCreate();
        try {

            String appId = getString(R.string.gms_app_id);
            MobileAds.initialize(this, appId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FirebaseTracker.getInstance().init(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        requestGrayStatus();
    }

    private void requestGrayStatus() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("did", MD5Util.getStringMD5(SystemUtils.getAndroidId(this)));
        Log.d(TAG, "getDefault " + Locale.getDefault().toString());
        if (Locale.getDefault().toString().equals("zh_CN_#Hans")) {
            queryParams.put("lc", "zh_CN");
        } else {
            queryParams.put("lc", Locale.getDefault().toString());
        }
        queryParams.put("pn", getPackageName());
        queryParams.put("appvc", String.valueOf(SystemUtils.getVersionCode(this)));
        queryParams.put("appvn", SystemUtils.getVersionName(this));
        queryParams.put("os", "android");
//        queryParams.put("chn", TextUtils.equals(Util.getChannelId(this), "10011") ? "ofw" : Util.getChannelId(this));
        queryParams.put("chn", "ofw");
        queryParams.put("avn", String.valueOf(Build.VERSION.SDK_INT));

        RetrofitNetwork.INSTANCE.getRequest().getGrayStatus(queryParams).enqueue(new CommonCallback<GrayItem>() {
            @Override
            public void onResponse(GrayItem response) {
                Log.d(TAG, "onResponse");
                handleGrayStatus(response.getData());
            }

            @Override
            public void onFailure(Call<GrayItem> call, Throwable t) {
                Log.d(TAG, "onFailure");
            }
        });
    }

    private void handleGrayStatus(List<GrayItem.DataBean> data) {
        if (data == null) {
            return;
        }

        for (GrayItem.DataBean bean : data) {
            Log.d(TAG, bean.getTag());
            switch (bean.getTag()) {
                case "today_horoscope_bottom_ads":
                    AdsState.today_horoscope_bottom_ads = bean.isStatus();
                    break;
                case "yesterday_horoscope_bottom_ads":
                    AdsState.yesterday_horoscope_bottom_ads = bean.isStatus();
                    break;
                case "tomorrow_horoscope_bottom_ads":
                    AdsState.tomorrow_horoscope_bottom_ads = bean.isStatus();
                    break;
                case "constellation_page_ads":
                    AdsState.constellation_page_ads = bean.isStatus();
                    break;
                case "compatibility_analysis_ads":
                    AdsState.compatibility_analysis_ads = bean.isStatus();
                    break;
                case "character_analysis_ads":
                    AdsState.character_analysis_ads = bean.isStatus();
                    break;
                case "setting_page_ads":
                    AdsState.setting_page_ads = bean.isStatus();
                    break;
                default:
                    break;
            }
        }
    }
}
