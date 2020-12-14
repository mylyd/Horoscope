package com.mobo.horoscope.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.mobo.horoscope.R;
import com.mobo.horoscope.bean.Horoscope;
import com.mobo.horoscope.bean.HoroscopeManager;
import com.mobo.horoscope.bean.MappingResult;
import com.mobo.horoscope.bean.TextStyleSetting;
import com.mobo.horoscope.common.AdsState;
import com.mobo.horoscope.common.Constants;
import com.mobo.horoscope.common.FileUtil;
import com.mobo.horoscope.common.GsonUtils;
import com.mobo.horoscope.common.NativeManager;
import com.mobo.horoscope.tracker.FirebaseTracker;
import com.mobo.horoscope.tracker.AppTracker;

import java.util.Map;

/**
 * @Description: 匹配结果页
 * @Author: jzhou
 * @CreateDate: 19-8-13 上午9:12
 */
public class CompatibilityResultActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "CompatibilityResultActivity";

    private TextView mMappingTitle;
    private TextView mLoveMatch;
    private TextView mCareerMatch;
    private TextView mFriendshipMatch;
    private UnifiedNativeAd mUnifiedNativeAd;
    private TextView mLoveReadMore;
    private TextView mCarrerReadMore;
    private TextView mFriendshipReadMore;
    private int mFromHoroscopeId;
    private int mToHoroscopeId;
    private TextView mLoveTitle;
    private TextView mCareerTitle;
    private TextView mFrienfshipTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compatibility_result);
        FirebaseTracker.getInstance().track(AppTracker.compatibility_detail_share_show);

        initView();
        initData();
        initNativeAdView();
    }

    private void initView() {
        mMappingTitle = findViewById(R.id.tv_mapping_title);
        mLoveTitle = findViewById(R.id.tv_love_title);
        mLoveMatch = findViewById(R.id.tv_love_match);
        mCareerTitle = findViewById(R.id.tv_career_title);
        mCareerMatch = findViewById(R.id.tv_career_match);
        mFrienfshipTitle = findViewById(R.id.tv_friendship_title);
        mFriendshipMatch = findViewById(R.id.tv_friendship_match);

        mLoveReadMore = findViewById(R.id.tv_love_read_more);
        mCarrerReadMore = findViewById(R.id.tv_career_read_more);
        mFriendshipReadMore = findViewById(R.id.tv_friendship_read_more);

        mLoveReadMore.setOnClickListener(this);
        mCarrerReadMore.setOnClickListener(this);
        mFriendshipReadMore.setOnClickListener(this);
        findViewById(R.id.iv_close).setOnClickListener(this);
    }

    /** */
    private void setTvStyle(){
        TextView[] textViews = {mMappingTitle,mLoveTitle,mCareerTitle,mFrienfshipTitle};
        TextView[] textViews_f = {mLoveMatch,mCareerMatch,mFriendshipMatch};
        for (TextView tv:textViews){
            TextStyleSetting.setTvStyleRobotoBold(tv,this);
        }
        for (TextView tv:textViews_f){
            TextStyleSetting.setTvStyleFandolSong(tv,this);
        }
    }

    private void initNativeAdView() {
        if (!AdsState.constellation_page_ads) {
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
                            NativeManager.inflateNativeAdView(CompatibilityResultActivity.this, container, unifiedNativeAd);
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
        setTvStyle();
        Intent intent = getIntent();
        mFromHoroscopeId = intent.getIntExtra(Constants.FROM_HOROSCOPE_ID, 0);
        mToHoroscopeId = intent.getIntExtra(Constants.TO_HOROSCOPE_ID, 0);
        Horoscope fromHoroscope = HoroscopeManager.getHoroscope(mFromHoroscopeId);
        Horoscope toHoroscope = HoroscopeManager.getHoroscope(mToHoroscopeId);
        if (fromHoroscope == null || toHoroscope == null) {
            return;
        }

        mMappingTitle.setText(String.format(getString(R.string.match_pair), fromHoroscope.getName(), toHoroscope.getName()));
        new LoadAsyncTask().execute();
    }

    private void initMappingResult(MappingResult result) {
        if (result == null) {
            return;
        }

        mLoveMatch.setText(result.getLove());
        mCareerMatch.setText(result.getCareer());
        mFriendshipMatch.setText(result.getFriendship());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_love_read_more:
                mLoveMatch.setMaxLines(Integer.MAX_VALUE);
                view.setVisibility(View.GONE);
                break;
            case R.id.tv_career_read_more:
                mCareerMatch.setMaxLines(Integer.MAX_VALUE);
                view.setVisibility(View.GONE);
                break;
            case R.id.tv_friendship_read_more:
                mFriendshipMatch.setMaxLines(Integer.MAX_VALUE);
                view.setVisibility(View.GONE);
                break;
            case R.id.iv_close:
                finish();
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

    public static void start(Context context, int fromId, int toId) {
        Intent intent = new Intent(context, CompatibilityResultActivity.class);
        intent.putExtra(Constants.FROM_HOROSCOPE_ID, fromId);
        intent.putExtra(Constants.TO_HOROSCOPE_ID, toId);
        context.startActivity(intent);
    }

    class LoadAsyncTask extends AsyncTask<Void, Void, Map<String, MappingResult>> {

        @Override
        protected Map<String, MappingResult> doInBackground(Void... voids) {
            String horoscope_match = FileUtil.getFromAssets(CompatibilityResultActivity.this, "horoscope_match.json");
            return GsonUtils.jsonToMap(horoscope_match, MappingResult.class);
        }

        @Override
        protected void onPostExecute(Map<String, MappingResult> result) {
            MappingResult ret = result.get(mFromHoroscopeId + "_" + mToHoroscopeId);
            if (ret == null) {
                ret = result.get(mToHoroscopeId + "_" + mFromHoroscopeId);
            }
            initMappingResult(ret);
        }
    }
}
