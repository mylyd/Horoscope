package com.mobo.horoscope.common;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.mobo.horoscope.R;

/**
 * @Description: 原声广告公共加载类
 * @Author: jzhou
 * @CreateDate: 19-8-17 下午12:20
 */
public class NativeManager {

    public static void inflateNativeAdView(Context context, FrameLayout container, UnifiedNativeAd nativeAd) {
        if (context == null || container == null || nativeAd == null) {
            return;
        }
        UnifiedNativeAdView adView = (UnifiedNativeAdView)
                View.inflate(context, R.layout.layout_native_ad, null);
        populateUnifiedNativeAdView(nativeAd, adView);

        container.removeAllViews();
        container.addView(adView);
        container.setVisibility(View.VISIBLE);
    }

    public static void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        adView.setIconView(adView.findViewById(R.id.iv_icon));
        adView.setCallToActionView(adView.findViewById(R.id.tv_action));
        adView.setBodyView(adView.findViewById(R.id.tv_body));

        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
    }
}
