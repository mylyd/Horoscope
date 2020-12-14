package com.mobo.horoscope.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.gson.JsonElement;
import com.mobo.horoscope.R;
import com.mobo.horoscope.bean.TextStyleSetting;
import com.mobo.horoscope.common.AdsState;
import com.mobo.horoscope.common.AlarmPushManager;
import com.mobo.horoscope.common.Constants;
import com.mobo.horoscope.common.MD5Util;
import com.mobo.horoscope.common.NativeManager;
import com.mobo.horoscope.common.SPUtils;
import com.mobo.horoscope.common.SystemUtils;
import com.mobo.horoscope.dialog.FeedBackDialog;
import com.mobo.horoscope.dialog.InviteFriendsDialog;
import com.mobo.horoscope.dialog.RateUsDialog;
import com.mobo.horoscope.network.CommonCallback;
import com.mobo.horoscope.network.RetrofitNetwork;
import com.mobo.horoscope.tracker.FirebaseTracker;
import com.mobo.horoscope.tracker.AppTracker;

import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 设置页面
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "SettingFragment";

    private Switch mPushSwitcher;
    private TextView mVersion;
    private TextView mPrivacy;
    private AdLoader mAdLoader;
    private UnifiedNativeAd mUnifiedNativeAd;
    private RateUsDialog mRateUsDialog;
    private FeedBackDialog mFeedBackDialog;
    private InviteFriendsDialog mInviteFriendsDialog;
    private CallbackManager mCallbackManager;
    private ShareDialog mShareDialog;
    private TextView tvTitle;
    private TextView tvNotification;
    private TextView tvReteUs;
    private TextView tvInviteFriends;

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FirebaseTracker.getInstance().track(AppTracker.setting_page_show);

        initView();
        initData();
        initNativeAdView();
    }

    private void initView() {
        mVersion = findViewById(R.id.tv_version);
        mPrivacy = findViewById(R.id.tv_privacy);
        mPushSwitcher = findViewById(R.id.push_switcher);
        tvTitle = findViewById(R.id.tv_setting_title);
        tvNotification = findViewById(R.id.tv_setting_notification);
        tvReteUs = findViewById(R.id.tv_setting_rateUs);
        tvInviteFriends = findViewById(R.id.tv_setting_inviteFriends);

        mPrivacy.setOnClickListener(this);
        findViewById(R.id.layout_rate_us).setOnClickListener(this);
        findViewById(R.id.layout_invite).setOnClickListener(this);

        initFacebook();
        initRateUsDialog();
        initFeedBackDialog();
        initInviteFriendsDialog();
    }

    /** */
    private void setTvStyle(){
        TextView[] textViews = {tvTitle,tvNotification,tvReteUs,tvInviteFriends,mPrivacy};
        for (TextView tv:textViews){
            TextStyleSetting.setTvStyleRobotoBold(tv,getContext());
        }
        TextStyleSetting.setTvStyleRoboto(mVersion,getContext());
    }

    private void initNativeAdView() {
        if (!AdsState.setting_page_ads) {
            return;
        }

        try {
            String adId = getString(R.string.setting_native_id);
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

    private void initData() {
        setTvStyle();
        boolean isChecked = SPUtils.getBoolean(getContext(), Constants.PUSH_ENABLE, true);
        mPushSwitcher.setChecked(isChecked);
        mPushSwitcher.setOnCheckedChangeListener(this);
        resetAlarmPush(isChecked);
        mVersion.setText(getResources().getText(R.string.horoscope) + " V " + SystemUtils.getVersionName(getContext()));
        mPrivacy.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    private void initRateUsDialog() {
        mRateUsDialog = new RateUsDialog(getContext());
        mRateUsDialog.setOnHighRateClickListener(new RateUsDialog.OnHighRateClickListener() {
            @Override
            public void onHighRateClick() {
                FirebaseTracker.getInstance().track(AppTracker.rate_us_good_click);
                SystemUtils.startWebView(getContext(), Constants.APP_STARE_URL);
            }
        });
        mRateUsDialog.setOnBadRateClickListener(new RateUsDialog.OnBadRateClickListener() {
            @Override
            public void onBadRateClick() {
                if (mFeedBackDialog != null) {
                    FirebaseTracker.getInstance().track(AppTracker.feedback_page_show);
                    mFeedBackDialog.show();
                }
            }
        });
    }

    private void initFeedBackDialog() {
        mFeedBackDialog = new FeedBackDialog(getContext());
        mFeedBackDialog.setOnConfirmClickListener(new FeedBackDialog.setOnConfirmClickListener() {
            @Override
            public void onConfirmClick(String email, String suggestion) {
                //提交反馈请求
                FirebaseTracker.getInstance().track(AppTracker.feedback_page_sub_click);
                requestFeedBack(email, suggestion);
            }
        });
    }

    private void initInviteFriendsDialog() {
        mInviteFriendsDialog = new InviteFriendsDialog(getContext());
        mInviteFriendsDialog.setOnFacebookClickListener(new InviteFriendsDialog.OnFacebookClickListener() {
            @Override
            public void onFacebookClick() {
                if (!SystemUtils.isNetworkAvailable(getContext())) {
                    Toast.makeText(getContext(), R.string.check_your_network, Toast.LENGTH_SHORT).show();
                    return;
                }

                ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                        .setContentTitle(getString(R.string.invite_content_title))
                        .setContentDescription(getString(R.string.invite_content_des))
                        .setContentUrl(Uri.parse(Constants.APP_STARE_URL))
                        .build();
                mShareDialog.show(shareLinkContent);
            }
        });
        mInviteFriendsDialog.setOnSystemClickListener(new InviteFriendsDialog.OnSystemClickListener() {
            @Override
            public void onSystemClick() {
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.invite_content) + Constants.APP_STARE_URL);
                startActivity(shareIntent);
            }
        });
    }

    private void requestFeedBack(String email, String suggestion) {
        Map<String, String> paramMap = new LinkedHashMap<>();
        paramMap.put("pkgname", getContext().getPackageName());
        paramMap.put("version", SystemUtils.getVersionName(getContext()));
        paramMap.put("vercode", String.valueOf(SystemUtils.getVersionCode(getContext())));
        paramMap.put("did", MD5Util.getStringMD5(SystemUtils.getAndroidId(getContext())));
        paramMap.put("deviceModel", android.os.Build.MODEL);
        paramMap.put("os", android.os.Build.VERSION.RELEASE);
        paramMap.put("language", SystemUtils.getCurrentLanguage());
        paramMap.put("country", SystemUtils.getCurrentCountry());
        paramMap.put("channelId", SystemUtils.getChannelId(getContext()));
        paramMap.put("resolution", String.valueOf(SystemUtils.getWindowHeight(getContext())));
        paramMap.put("cpu", android.os.Build.BOARD);
        paramMap.put("email", email);
        paramMap.put("message", suggestion);

        RetrofitNetwork.INSTANCE.getRequest().postFeedBack(paramMap).enqueue(new CommonCallback<JsonElement>() {
            @Override
            public void onResponse(JsonElement response) {
                Toast.makeText(getContext(), R.string.feedback_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(getContext(), R.string.feedback_failed, Toast.LENGTH_SHORT).show();
                Log.d(TAG, call.toString());
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        SPUtils.setBoolean(getContext(), Constants.PUSH_ENABLE, isChecked);
        resetAlarmPush(isChecked);
    }

    private void resetAlarmPush(boolean isChecked) {
        if (isChecked) {
            AlarmPushManager.startAlarmPush(getContext());
        } else {
            AlarmPushManager.stopAlarmPush(getContext());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_rate_us:
                if (mRateUsDialog != null) {
                    FirebaseTracker.getInstance().track(AppTracker.rate_us_show);
                    mRateUsDialog.show();
                }
                break;
            case R.id.layout_invite:
                if (mInviteFriendsDialog != null) {
                    mInviteFriendsDialog.show();
                }
                break;
            case R.id.tv_privacy:
                SystemUtils.startWebView(getContext(), Constants.PRIVACY_URL);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        if (mUnifiedNativeAd != null) {
            mUnifiedNativeAd.destroy();
        }
        super.onDestroyView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mAdLoader != null) {
            mAdLoader.loadAd(new AdRequest.Builder().build());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initFacebook() {
        mCallbackManager = CallbackManager.Factory.create();
        mShareDialog = new ShareDialog(this);
        mShareDialog.registerCallback(mCallbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.w(TAG, "onSuccess: ");
                Toast.makeText(getContext(), R.string.share_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Log.w(TAG, "onCancel: ");
                Toast.makeText(getContext(), R.string.share_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.w(TAG, "onError: " + error.getMessage());
                Toast.makeText(getContext(), R.string.share_success, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
