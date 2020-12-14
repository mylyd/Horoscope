package com.mobo.horoscope.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.mobo.horoscope.bean.TextStyleSetting;
import com.mobo.horoscope.common.AdsState;
import com.mobo.horoscope.common.Constants;
import com.mobo.horoscope.common.NativeManager;
import com.mobo.horoscope.common.SPUtils;
import com.mobo.horoscope.tracker.FirebaseTracker;
import com.mobo.horoscope.tracker.AppTracker;

public class CompatibilityActivity extends BaseActivity implements View.OnClickListener,
        HoroscopeAdapter.OnItemClickListener {
    private static final String TAG = "CompatibilityActivity";
    private int mHoroscopeId;
    private UnifiedNativeAd mUnifiedNativeAd;
    private RecyclerView mRecyclerView;
    private TextView mConfirm;
    private TextView mMeName;
    private TextView mItName;
    private ImageView mMeIcon;
    private ImageView mItIcon;
    private TextView mtitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compatibility);
        FirebaseTracker.getInstance().track(AppTracker.compatibility_page_show);

        initView();
        setTvStyle();
        initNativeAdView();
        initData();
    }

    private void initView() {
        mMeName = findViewById(R.id.tv_me_name);
        mItName = findViewById(R.id.tv_it_name);
        mMeIcon = findViewById(R.id.iv_me_icon);
        mItIcon = findViewById(R.id.iv_it_icon);
        mConfirm = findViewById(R.id.tv_confirm);
        mtitle = findViewById(R.id.tv_compatibility_title);
        mRecyclerView = findViewById(R.id.recyclerView);

        mMeIcon.setOnClickListener(this);
        mItIcon.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        findViewById(R.id.iv_close).setOnClickListener(this);
    }

    /** */
    private void setTvStyle(){
        TextView[] textViews = {mtitle,mItName,mConfirm,mMeName};
        for (TextView tv:textViews){
            TextStyleSetting.setTvStyleRobotoBold(tv,this);
        }
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
                            NativeManager.inflateNativeAdView(CompatibilityActivity.this, container, unifiedNativeAd);
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

    private void initData() {
        mHoroscopeId = SPUtils.getInt(this, Constants.HOROSCOPE_ID, 1);
        Horoscope horoscope = HoroscopeManager.getHoroscope(mHoroscopeId);
        if (horoscope == null) {
            return;
        }

        mMeName.setText(horoscope.getName());
        mMeIcon.setImageResource(horoscope.getIconId_1());
        mMeIcon.setTag(horoscope.getHoroscopeId());

        HoroscopeAdapter mImageAdapter = new HoroscopeAdapter(HoroscopeManager.getHoroscopeList());
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mImageAdapter);
        mImageAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(int position, Horoscope horoscope) {
        if (horoscope == null) {
            return;
        }

        if (mMeIcon.getTag() != null && mItIcon.getTag() != null) {
            return;
        }

        if (mMeIcon.getTag() == null) {
            mMeName.setText(horoscope.getName());
            mMeIcon.setImageResource(horoscope.getIconId_1());
            mMeIcon.setTag(horoscope.getHoroscopeId());
        } else {
            mItName.setText(horoscope.getName());
            mItIcon.setImageResource(horoscope.getIconId_1());
            mItIcon.setTag(horoscope.getHoroscopeId());
        }

        if (mMeIcon.getTag() != null && mItIcon.getTag() != null) {
            mConfirm.setEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.iv_me_icon:
                mMeName.setText(R.string.select);
                mMeIcon.setImageResource(R.drawable.ic_match_empty);
                mMeIcon.setTag(null);
                mConfirm.setEnabled(false);
                break;
            case R.id.iv_it_icon:
                mItName.setText(R.string.select);
                mItIcon.setImageResource(R.drawable.ic_match_empty);
                mItIcon.setTag(null);
                mConfirm.setEnabled(false);
                break;
            case R.id.tv_confirm:
                if (mMeIcon.getTag() == null || mItIcon.getTag() == null) {
                    return;
                }
                int fromId = (int) mMeIcon.getTag();
                int toId = (int) mItIcon.getTag();
                CompatibilityResultActivity.start(this, fromId, toId);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (mUnifiedNativeAd != null) {
            mUnifiedNativeAd.destroy();
        }
        super.onDestroy();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, CompatibilityActivity.class);
        context.startActivity(intent);
    }
}