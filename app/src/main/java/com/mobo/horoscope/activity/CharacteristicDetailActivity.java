package com.mobo.horoscope.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.gson.Gson;
import com.mobo.horoscope.R;
import com.mobo.horoscope.adapter.AmazingAdapter;
import com.mobo.horoscope.bean.AmazingInfo;
import com.mobo.horoscope.bean.CharacteristicInfo;
import com.mobo.horoscope.bean.Horoscope;
import com.mobo.horoscope.bean.HoroscopeManager;
import com.mobo.horoscope.bean.TextStyleSetting;
import com.mobo.horoscope.common.AdsState;
import com.mobo.horoscope.common.Constants;
import com.mobo.horoscope.common.FileUtil;
import com.mobo.horoscope.common.NativeManager;
import com.mobo.horoscope.tracker.FirebaseTracker;
import com.mobo.horoscope.tracker.AppTracker;

import java.util.ArrayList;
import java.util.List;


/**
 * @Description: 星座详情页
 * @Author: jzhou
 * @CreateDate: 19-8-13 上午9:12
 */
public class CharacteristicDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "CharacteristicDetail";

    private TextView mCharacteristicTitle;
    private TextView mHoroscopeName;
    private TextView mHoroscopeDate;
    private TextView mKey1;
    private TextView mKey2;
    private TextView mKey3;
    private TextView mElement;
    private TextView mPolarity;
    private TextView mColor;
    private TextView mGem;
    private TextView mFlower;
    private TextView mAnalysisTitle;
    private TextView mAnalysisText;
    private TextView mMottoTitle;
    private TextView mMottoText;
    private TextView mAmazingNameTitle;
    private TextView mReadMore;
    private UnifiedNativeAd mUnifiedNativeAd;
    private int mHoroscopeId;
    private RecyclerView recyclerView;
    private List<AmazingInfo> mlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characteristic_detail);
        FirebaseTracker.getInstance().track(AppTracker.character_detail_page_show);

        initView();
        initData();
        initNativeAdView();
    }

    private void initView() {
        mCharacteristicTitle = findViewById(R.id.tv_characteristic_title);
        mHoroscopeName = findViewById(R.id.tv_analysis);
        mHoroscopeDate = findViewById(R.id.tv_date);
        mKey1 = findViewById(R.id.tv_key_1);
        mKey2 = findViewById(R.id.tv_key_2);
        mKey3 = findViewById(R.id.tv_key_3);
        mElement = findViewById(R.id.tv_element);
        mPolarity = findViewById(R.id.tv_polarity);
        mColor = findViewById(R.id.tv_color);
        mGem = findViewById(R.id.tv_gem);
        mFlower = findViewById(R.id.tv_flower);
        mAnalysisTitle = findViewById(R.id.tv_analysis_title);
        mAnalysisText = findViewById(R.id.tv_analysis_text);
        mMottoTitle = findViewById(R.id.tv_motto_title);
        mMottoText = findViewById(R.id.tv_motto_text);
        mAmazingNameTitle = findViewById(R.id.tv_amazing_name_title);
        mReadMore = findViewById(R.id.tv_read_more);
        mReadMore.setOnClickListener(this);
        findViewById(R.id.iv_close).setOnClickListener(this);
        //
        recyclerView = findViewById(R.id.recyclerView_amazing);
    }

    /** */
    private void setTvStyle(){
        TextView[] textViews_b = {mCharacteristicTitle,mHoroscopeName,mHoroscopeDate,mKey1,mKey2,mKey3,mAnalysisTitle,mMottoTitle,mAmazingNameTitle};
        TextView[] textViews_f = {mElement,mPolarity,mColor,mGem,mFlower,mAnalysisText,mMottoText};
        for (TextView tv:textViews_b){
            TextStyleSetting.setTvStyleRobotoBold(tv,this);
        }
        for (TextView tv:textViews_f){
            TextStyleSetting.setTvStyleFandolSong(tv,this);
        }
    }

    private void initNativeAdView() {
        if (!AdsState.compatibility_analysis_ads) {
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
                            NativeManager.inflateNativeAdView(CharacteristicDetailActivity.this, container, unifiedNativeAd);
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
        if (intent == null) {
            return;
        }
        mHoroscopeId = intent.getIntExtra(Constants.HOROSCOPE_ID, 1);
        Horoscope horoscope = HoroscopeManager.getHoroscope(mHoroscopeId);
        mCharacteristicTitle.setText(horoscope.getName());
        new LoadAsyncTask().execute();
    }

    private void initMappingResult(CharacteristicInfo result) {
        if (result == null) {
            return;
        }
        mHoroscopeName.setText(String.format(getString(R.string.facts), result.getName()));
        mHoroscopeDate.setText(result.getDate());
        mKey1.setText(result.getKeywords1());
        mKey2.setText(result.getKeywords2());
        mKey3.setText(result.getKeywords3());
        mElement.setText(String.format(getString(R.string.element), result.getElement()));
        mPolarity.setText(String.format(getString(R.string.polarity), result.getPolarity()));
        mColor.setText(String.format(getString(R.string.color), result.getColor()));
        mGem.setText(String.format(getString(R.string.gem), result.getGem()));
        mFlower.setText(String.format(getString(R.string.flower), result.getFlower()));

        mAnalysisTitle.setText(String.format(getString(R.string.analysis), result.getName()));
        mAnalysisText.setText(result.getAnalysis());
        mMottoTitle.setText(String.format(getString(R.string.motto), result.getName()));
        mMottoText.setText(result.getMotto());
        mAmazingNameTitle.setText(String.format(getString(R.string.amazing_name), result.getName()));

        for (int i = 0; i < result.getSay().size(); i++) {
            List<String> says = result.getSay();
            String say = says.get(i);
            mlist.add(new AmazingInfo(setSubString(say,0,1),setSubString(say,1)));
        }
        LinearLayoutManager llm = new LinearLayoutManager(this);
        AmazingAdapter amazingAdapter = new AmazingAdapter(mlist);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(amazingAdapter);
    }

    private String setSubString(String str,int indexStart,int indexEnd){
        return str.substring(indexStart,indexEnd);
    }

    private String setSubString(String str,int index){
        return str.substring(index);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_read_more:
                mReadMore.setMaxLines(Integer.MAX_VALUE);
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

    public static void start(Context context, int horoscopeId) {
        Intent intent = new Intent(context, CharacteristicDetailActivity.class);
        intent.putExtra(Constants.HOROSCOPE_ID, horoscopeId);
        context.startActivity(intent);
    }

    class LoadAsyncTask extends AsyncTask<Void, Void, CharacteristicInfo> {

        @Override
        protected CharacteristicInfo doInBackground(Void... voids) {
            String characteristic = FileUtil.getFromAssets(CharacteristicDetailActivity.this, mHoroscopeId + ".json");
            return new Gson().fromJson(characteristic, CharacteristicInfo.class);
        }

        @Override
        protected void onPostExecute(CharacteristicInfo result) {
            initMappingResult(result);
        }
    }
}
