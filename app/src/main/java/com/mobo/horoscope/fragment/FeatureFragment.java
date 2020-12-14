package com.mobo.horoscope.fragment;

import android.os.Bundle;
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
import com.mobo.horoscope.activity.CharacteristicActivity;
import com.mobo.horoscope.activity.CompatibilityActivity;
import com.mobo.horoscope.bean.TextStyleSetting;
import com.mobo.horoscope.common.AdsState;
import com.mobo.horoscope.common.NativeManager;
import com.mobo.horoscope.tracker.FirebaseTracker;
import com.mobo.horoscope.tracker.AppTracker;

/**
 * 星座匹配页面
 */
public class FeatureFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "FeatureFragment";
    private UnifiedNativeAd mUnifiedNativeAd;
    private AdLoader mAdLoader;
    private TextView tvTitle;

    public static FeatureFragment newInstance() {
        FeatureFragment fragment = new FeatureFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feature, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FirebaseTracker.getInstance().track(AppTracker.compatibility_page_show);

        initView();
        TextStyleSetting.setTvStyleRobotoBold(tvTitle,getContext());
        initNativeAdView();
    }

    private void initView() {
        findViewById(R.id.iv_characteristic_enter).setOnClickListener(this);
        findViewById(R.id.iv_compatibility_enter).setOnClickListener(this);
        tvTitle = findViewById(R.id.tv_feature_title);
    }

    private void initNativeAdView() {
        if (!AdsState.today_horoscope_bottom_ads) {
            return;
        }

        try {
            String adId = getString(R.string.today_native_id);
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
            case R.id.iv_characteristic_enter:
                CharacteristicActivity.start(getContext());
                break;
            case R.id.iv_compatibility_enter:
                CompatibilityActivity.start(getContext());
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mAdLoader != null) {
            mAdLoader.loadAd(new AdRequest.Builder().build());
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
