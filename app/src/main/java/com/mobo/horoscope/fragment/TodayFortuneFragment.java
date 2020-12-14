package com.mobo.horoscope.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.mobo.horoscope.R;
import com.mobo.horoscope.bean.FortuneInfo;
import com.mobo.horoscope.bean.Horoscope;
import com.mobo.horoscope.bean.HoroscopeManager;
import com.mobo.horoscope.bean.MatchInfo;
import com.mobo.horoscope.bean.RatingInfo;

import com.mobo.horoscope.bean.TextStyleSetting;

import com.mobo.horoscope.common.AdsState;
import com.mobo.horoscope.common.NativeManager;
import com.mobo.horoscope.view.CommonRatingBar;

/**
 * 今日运势
 */
public class TodayFortuneFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "TodayFortuneFragment";

    private CommonRatingBar mSexRating;
    private CommonRatingBar mHustleRating;
    private CommonRatingBar mVibeRating;
    private CommonRatingBar mSuccessRating;
    private ImageView mLoveIcon;
    private ImageView mFriendshipIcon;
    private ImageView mCarrerIcon;
    private TextView mLoveName;
    private TextView mFriendshipName;
    private TextView mCarrerName;
    private TextView mAnalysisText;
    private TextView mReadMore;
    private UnifiedNativeAd mUnifiedNativeAd;
    private AdLoader mAdLoader;
    private TextView mRatingTitle;
    private TextView mMatcheTitle;
    private TextView mAnalysisTitle;
    private TextView mTvSex;
    private TextView mTvHustle;
    private TextView mTvVibe;
    private TextView mTvSuccess;
    private TextView mLoveTitle;
    private TextView mCareerTitle;
    private TextView mFriendshipTitle;
    private TextView mLovetitle;
    private TextView mLoveText;
    private TextView mHealthTitle;
    private TextView mHealthText;
    private TextView mCareertitle;
    private TextView mCareerText;

    public static TodayFortuneFragment newInstance() {
        TodayFortuneFragment fragment = new TodayFortuneFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_today_fortune, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        setTvStyle();
        initNativeAdView();
    }

    private void initView() {
        mSexRating = findViewById(R.id.rb_sex);
        mHustleRating = findViewById(R.id.rb_hustle);
        mVibeRating = findViewById(R.id.rb_vibe);
        mSuccessRating = findViewById(R.id.rb_success);
        mLoveIcon = findViewById(R.id.iv_love_icon);
        mFriendshipIcon = findViewById(R.id.iv_friendship_icon);
        mCarrerIcon = findViewById(R.id.iv_carrer_icon);
        mLoveName = findViewById(R.id.tv_love_name);
        mFriendshipName = findViewById(R.id.tv_friendship_name);
        mCarrerName = findViewById(R.id.tv_carrer_name);
        mReadMore = findViewById(R.id.tv_read_more);
        mRatingTitle = findViewById(R.id.tv_rating_title);
        mMatcheTitle = findViewById(R.id.tv_matches_title);
        mTvSex = findViewById(R.id.tv_sex);
        mTvHustle = findViewById(R.id.tv_hustle);
        mTvVibe = findViewById(R.id.tv_vibe);
        mTvSuccess = findViewById(R.id.tv_success);
        mLoveTitle = findViewById(R.id.tv_love_title);
        mCareerTitle = findViewById(R.id.tv_career_title);
        mFriendshipTitle = findViewById(R.id.tv_friendship_title);
        mReadMore.setOnClickListener(this);

        mAnalysisTitle = findViewById(R.id.tv_title_analysis);
        mAnalysisText = findViewById(R.id.tv_content_analysis);
        mLovetitle = findViewById(R.id.tv_title_love);
        mLoveText = findViewById(R.id.tv_content_love);
        mHealthTitle = findViewById(R.id.tv_title_health);
        mHealthText = findViewById(R.id.tv_content_health);
        mCareertitle = findViewById(R.id.tv_title_career);
        mCareerText = findViewById(R.id.tv_content_career);
    }

    /** */
    private void setTvStyle(){
        TextView[] textViews_b = {mRatingTitle, mAnalysisTitle,mMatcheTitle,mLovetitle,mHealthTitle,mCareertitle};
        TextView[] textViews = {mTvHustle, mTvSex, mTvSuccess, mTvVibe,mCarrerName,mFriendshipName,mLoveName};
        TextView[] textViews_F ={mLoveTitle, mCareerTitle, mFriendshipTitle,mAnalysisText,mLoveText,mHealthText,mCareerText};
        for (TextView tv_b:textViews_b){
            TextStyleSetting.setTvStyleRobotoBold(tv_b,getContext());
        }
        for (TextView tv:textViews){
            TextStyleSetting.setTvStyleRoboto(tv,getContext());
        }
        for (TextView tv_f:textViews_F){
            TextStyleSetting.setTvStyleFandolSong(tv_f,getContext());
        }
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
        mAnalysisText.setMaxLines(Integer.MAX_VALUE);
        view.setVisibility(View.GONE);
    }

    public void notifyData(FortuneInfo... fortuneInfo) {
        if (mAdLoader != null) {
            mAdLoader.loadAd(new AdRequest.Builder().build());
        }

        if (fortuneInfo == null) {
            return;
        }

        String analysisText = fortuneInfo[0].getMain_horoscope();
        String loveText = fortuneInfo[1].getMain_horoscope();
        String careerText = fortuneInfo[2].getMain_horoscope();
        String healthText = fortuneInfo[3].getMain_horoscope();

        if (TextUtils.isEmpty(analysisText)||TextUtils.isEmpty(loveText)||TextUtils.isEmpty(careerText)||TextUtils.isEmpty(healthText)) {
            return;
        }
        mAnalysisText.setText(analysisText);
        mLoveText.setText(loveText);
        mCareerText.setText(careerText);
        mHealthText.setText(healthText);

        RatingInfo ratings = fortuneInfo[0].getRatings();
        if (ratings == null) {
            return;
        }
        mSexRating.setRatingScore(ratings.getSEX());
        mHustleRating.setRatingScore(ratings.getHUSTLE());
        mVibeRating.setRatingScore(ratings.getVIBE());
        mSuccessRating.setRatingScore(ratings.getSUCCESS());

        MatchInfo match = fortuneInfo[0].getMatch();
        if (match == null) {
            return;
        }

        Horoscope horoscope = HoroscopeManager.getHoroscope(match.getLOVE());
        mLoveName.setText(horoscope.getName());
        mLoveIcon.setImageResource(horoscope.getIconId_1());
        horoscope = HoroscopeManager.getHoroscope(match.getFRIENDSHIP());
        mFriendshipName.setText(horoscope.getName());
        mFriendshipIcon.setImageResource(horoscope.getIconId_1());
        horoscope = HoroscopeManager.getHoroscope(match.getCAREER());
        mCarrerName.setText(horoscope.getName());
        mCarrerIcon.setImageResource(horoscope.getIconId_1());
    }

    @Override
    public void onDestroyView() {
        if (mUnifiedNativeAd != null) {
            mUnifiedNativeAd.destroy();
        }
        super.onDestroyView();
    }
}
