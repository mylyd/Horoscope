package com.mobo.horoscope.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.mobo.horoscope.R;
import com.mobo.horoscope.bean.FortuneInfo;
import com.mobo.horoscope.bean.TextStyleSetting;
import com.mobo.horoscope.common.AdsState;
import com.mobo.horoscope.common.Constants;
import com.mobo.horoscope.common.NativeManager;
import com.mobo.horoscope.common.SPUtils;
import com.mobo.horoscope.dialog.LoadingDialog;
import com.mobo.horoscope.tracker.AppTracker;
import com.mobo.horoscope.tracker.FirebaseTracker;

import java.util.Calendar;

/**
 * 明日、一周、月、年运势
 */
public class TomorrowFortuneFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "TomorrowFortuneFragment";
    private static final String TOMORROW_TYPE = "tomorrow_type";
    private TextView mAnalysisText;
    private LinearLayout mWatchVideo;
    private RewardedVideoAd mRewardedVideoAd;
    private MyRewardedVideoAdListener mRewardedVideoAdListener;
    private UnifiedNativeAd mUnifiedNativeAd;
    private AdLoader mAdLoader;
    private LoadingDialog mLoadingDialog;
    private int mTomorrowType;

    public static TomorrowFortuneFragment newInstance(int type) {
        TomorrowFortuneFragment fragment = new TomorrowFortuneFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TOMORROW_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tomarrow_fortune, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTomorrowType = bundle.getInt(TOMORROW_TYPE);
            FirebaseTracker.getInstance().track(getTracker());
        }
        initView();
        initRewardedAdView();
        initNativeAdView();
    }

    private void initView() {
        mAnalysisText = findViewById(R.id.tv_analysis);
        mWatchVideo = findViewById(R.id.layout_watch_video);

        mWatchVideo.setOnClickListener(this);
        mLoadingDialog = new LoadingDialog(getContext(), true, true);
    }

    private void initRewardedAdView() {
        boolean hasWatchRewardAd = SPUtils.getBoolean(getContext(), buildHasWatchRewardAdKey(), false);
        if (hasWatchRewardAd) {
            mAnalysisText.setMaxLines(Integer.MAX_VALUE);
            mWatchVideo.setVisibility(View.GONE);
            return;
        }
        mRewardedVideoAdListener = new MyRewardedVideoAdListener();
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());
        String adId = getString(R.string.tomorrow_rewarded_id);
        mRewardedVideoAd.loadAd(adId, new AdRequest.Builder().build());
    }

    private void initNativeAdView() {
        if (!AdsState.tomorrow_horoscope_bottom_ads) {
            return;
        }

        try {
            String adId = getString(R.string.yesterday_native_id);
            mAdLoader = new AdLoader.Builder(getContext(), adId)
                    .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {

                        @Override
                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                            if (mUnifiedNativeAd != null) {
                                mUnifiedNativeAd.destroy();
                            }

                            mUnifiedNativeAd = unifiedNativeAd;
                            FrameLayout container = findViewById(R.id.layout_native_ad_container);
                            NativeManager.inflateNativeAdView(getContext(), container, unifiedNativeAd);
                        }
                    }).withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            Log.d(TAG, "NativeAd Failed To Load");
                        }
                    }).build();

            mAdLoader.loadAd(new AdRequest.Builder().build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_watch_video:
                //加载加载成功直接播放，否则继续加载
                mRewardedVideoAd.setRewardedVideoAdListener(mRewardedVideoAdListener);
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                } else {
                    mLoadingDialog.show();
//                    mWatchVideo.setEnabled(false);
                }
                break;
        }
    }

    public void notifyData(FortuneInfo fortuneInfo) {
        if (mAdLoader != null) {
            mAdLoader.loadAd(new AdRequest.Builder().build());
        }
        if (mRewardedVideoAd != null) {
            String adId = getString(R.string.tomorrow_rewarded_id);
            mRewardedVideoAd.loadAd(adId, new AdRequest.Builder().build());
        }

        if (fortuneInfo == null) {
            return;
        }

        String analysisText = fortuneInfo.getMain_horoscope();
        if (TextUtils.isEmpty(analysisText)) {
            return;
        }
        TextStyleSetting.setTvStyleFandolSong(mAnalysisText, getContext());
        mAnalysisText.setText(analysisText);
    }

    private String getTracker() {
        if (mTomorrowType == Constants.TOMORROW) {
            return AppTracker.tomorrow_horoscope_share_show;
        }
        if (mTomorrowType == Constants.WEEKLY) {
            return AppTracker.week_horoscope_share_show;
        }
        if (mTomorrowType == Constants.MONTHLY) {
            return AppTracker.month_horoscope_share_show;
        }
        if (mTomorrowType == Constants.YEARLY) {
            return AppTracker.year_horoscope_share_show;
        }

        return AppTracker.tomorrow_horoscope_share_show;
    }

    @Override
    public void onResume() {
        if (mRewardedVideoAd != null) {
            mRewardedVideoAd.resume(getContext());
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mRewardedVideoAd != null) {
            mRewardedVideoAd.pause(getContext());
        }
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (mUnifiedNativeAd != null) {
            mUnifiedNativeAd.destroy();
        }
        if (mRewardedVideoAd != null) {
            mRewardedVideoAd.destroy(getContext());
        }
        super.onDestroyView();
    }

    private String buildHasWatchRewardAdKey() {
        Calendar calendar = Calendar.getInstance();
        String key = mTomorrowType + "#" + Constants.HAS_WATCH_REWARD_ADS
                + calendar.get(Calendar.YEAR) + calendar.get(Calendar.MONTH)
                + calendar.get(Calendar.HOUR_OF_DAY);
        Log.d(TAG, key);
        return key;
    }

    private class MyRewardedVideoAdListener implements RewardedVideoAdListener {

        @Override
        public void onRewardedVideoAdLoaded() {
            Log.d(TAG, "onRewardedVideoAdLoaded");
            if (mLoadingDialog.isShowing() && mRewardedVideoAd.isLoaded()) {
                mLoadingDialog.dismiss();
                mRewardedVideoAd.show();
                mWatchVideo.setEnabled(true);
            }
        }

        @Override
        public void onRewardedVideoAdOpened() {
            Log.d(TAG, "onRewardedVideoAdLoaded");
        }

        @Override
        public void onRewardedVideoStarted() {
            Log.d(TAG, "onRewardedVideoAdLoaded");
        }

        @Override
        public void onRewardedVideoAdClosed() {
            Log.d(TAG, "onRewardedVideoAdClosed");

            String adId = getString(R.string.tomorrow_rewarded_id);
            mRewardedVideoAd.loadAd(adId, new AdRequest.Builder().build());
        }

        @Override
        public void onRewarded(RewardItem rewardItem) {
            Log.d(TAG, "onRewarded: " + rewardItem.toString());
        }

        @Override
        public void onRewardedVideoAdLeftApplication() {
            Log.d(TAG, "onRewardedVideoAdLeftApplication");
        }

        @Override
        public void onRewardedVideoAdFailedToLoad(int i) {
            Log.d(TAG, "onAdFailedToLoad: " + i);
            mLoadingDialog.dismiss();
            mWatchVideo.setEnabled(true);
            String adId = getString(R.string.tomorrow_rewarded_id);
            mRewardedVideoAd.loadAd(adId, new AdRequest.Builder().build());
            Toast.makeText(getContext(), R.string.ads_load_failed, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRewardedVideoCompleted() {
            Log.d(TAG, "onRewardedVideoCompleted");
            mLoadingDialog.dismiss();
            mWatchVideo.setEnabled(true);
            mAnalysisText.setMaxLines(Integer.MAX_VALUE);
            mWatchVideo.setVisibility(View.GONE);

            String adId = getString(R.string.tomorrow_rewarded_id);
            mRewardedVideoAd.loadAd(adId, new AdRequest.Builder().build());
            SPUtils.setBoolean(getContext(), buildHasWatchRewardAdKey(), true);
        }
    }
}
