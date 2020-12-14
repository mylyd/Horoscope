package com.mobo.horoscope.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.mobo.horoscope.R;
import com.mobo.horoscope.bean.FortuneInfo;
import com.mobo.horoscope.bean.TextStyleSetting;

import com.mobo.horoscope.common.AdsState;
import com.mobo.horoscope.common.NativeManager;
import com.mobo.horoscope.tracker.FirebaseTracker;
import com.mobo.horoscope.tracker.AppTracker;

/**
 * 昨日运势
 */
public class YesterdayFortuneFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "YesterdayFortune";

    private TextView mAnalysisText;
    private TextView mReadMore;
    private UnifiedNativeAd mUnifiedNativeAd;
    private AdLoader mAdLoader;

    public static YesterdayFortuneFragment newInstance() {
        YesterdayFortuneFragment fragment = new YesterdayFortuneFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_yesterday_fortune, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FirebaseTracker.getInstance().track(AppTracker.yesterday_horoscope_share_show);

        initView();
        initNativeAdView();
    }

    private void initView() {
        mAnalysisText = findViewById(R.id.tv_analysis);
        mReadMore = findViewById(R.id.tv_read_more);

        mReadMore.setOnClickListener(this);
    }

    private void initNativeAdView() {
        if (!AdsState.yesterday_horoscope_bottom_ads) {
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
        mAnalysisText.setMaxLines(Integer.MAX_VALUE);
        view.setVisibility(View.GONE);
    }

    public void notifyData(FortuneInfo fortuneInfo) {
        if (mAdLoader != null) {
            mAdLoader.loadAd(new AdRequest.Builder().build());
        }

        if (fortuneInfo == null) {
            return;
        }

        String analysisText = fortuneInfo.getMain_horoscope();
        if (TextUtils.isEmpty(analysisText)) {
            return;
        }
        if (mAnalysisText != null) {
            TextStyleSetting.setTvStyleFandolSong(mAnalysisText,getContext());
            mAnalysisText.setText(analysisText);
        }
    }

    @Override
    public void onDestroyView() {
        if (mUnifiedNativeAd != null) {
            mUnifiedNativeAd.destroy();
        }
        super.onDestroyView();
    }
}
