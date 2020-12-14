package com.mobo.horoscope.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.mobo.horoscope.R;
import com.mobo.horoscope.adapter.HoroscopeAdapter;
import com.mobo.horoscope.bean.Horoscope;
import com.mobo.horoscope.bean.HoroscopeManager;
import com.mobo.horoscope.common.AdsState;
import com.mobo.horoscope.common.NativeManager;
import com.mobo.horoscope.tracker.FirebaseTracker;
import com.mobo.horoscope.tracker.AppTracker;

public class CharacteristicActivity extends BaseActivity implements View.OnClickListener,
        HoroscopeAdapter.OnItemClickListener {
    private static final String TAG = "CharacteristicActivity";
    private UnifiedNativeAd mUnifiedNativeAd;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characteristic);
        FirebaseTracker.getInstance().track(AppTracker.character_page_show);

        initNativeAdView();
        initRecyclerView();
    }

    private void initNativeAdView() {
        if (!AdsState.today_horoscope_bottom_ads) {
            return;
        }

        try {
            String adId = getString(R.string.mapping_detail_native_id);
            AdLoader adLoader = new AdLoader.Builder(this, adId)
                    .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {

                        @Override
                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                            if (mUnifiedNativeAd != null) {
                                mUnifiedNativeAd.destroy();
                            }

                            mUnifiedNativeAd = unifiedNativeAd;
                            FrameLayout container = findViewById(R.id.layout_native_ad_container);
                            NativeManager.inflateNativeAdView(CharacteristicActivity.this, container, unifiedNativeAd);
                        }
                    }).withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            Log.d(TAG, "NativeAd Failed To Load");
                        }
                    }).build();

            adLoader.loadAd(new AdRequest.Builder().build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);

        HoroscopeAdapter mImageAdapter = new HoroscopeAdapter(HoroscopeManager.getHoroscopeList());
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mImageAdapter);
        mImageAdapter.setOnItemClickListener(this);

        findViewById(R.id.iv_close).setOnClickListener(this);
    }

    @Override
    public void onItemClick(int position, Horoscope horoscope) {
        CharacteristicDetailActivity.start(this, horoscope.getHoroscopeId());
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, CharacteristicActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (mUnifiedNativeAd != null) {
            mUnifiedNativeAd.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
