package com.mobo.horoscope.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.mobo.horoscope.R;
import com.mobo.horoscope.bean.Horoscope;
import com.mobo.horoscope.bean.HoroscopeManager;
import com.mobo.horoscope.bean.TextStyleSetting;
import com.mobo.horoscope.common.Constants;
import com.mobo.horoscope.common.SPUtils;
import com.mobo.horoscope.common.SystemUtils;
import com.mobo.horoscope.tracker.AppTracker;
import com.mobo.horoscope.tracker.FirebaseTracker;
import com.zyyoona7.picker.DatePickerView;
import com.zyyoona7.picker.base.BaseDatePickerView;
import com.zyyoona7.picker.listener.OnDateSelectedListener;
import com.zyyoona7.wheel.WheelView;

import java.util.Calendar;
import java.util.Date;

public class HoroscopeSelectorActivity extends BaseActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, OnDateSelectedListener {
    private int mHoroscopeId;
    private String mHoroscopeDate;
    private ImageView mClose;
    private TextView mConfirm;
    private TextView mName;
    private ImageView mImage;
    private CheckBox mAccept;
    private TextView mPrivacy;
    private View mLayoutPrivacy;
    private DatePickerView mDatePicker;
    private boolean isFirstLogin;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horoscope_selector);
        FirebaseTracker.getInstance().track(AppTracker.constellations_select_page_show);

        initView();
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initHoroscopeData(intent);
    }

    private void initView() {
        mClose = findViewById(R.id.iv_close);
        mName = findViewById(R.id.tv_analysis);
        mImage = findViewById(R.id.iv_image);
        mConfirm = findViewById(R.id.tv_confirm);
        mAccept = findViewById(R.id.cb_accept);
        mPrivacy = findViewById(R.id.tv_privacy);
        mLayoutPrivacy = findViewById(R.id.layout_privacy);
        mDatePicker = findViewById(R.id.date_picker);
        mTitle = findViewById(R.id.tv_title);
    }

    private void initData() {
        initHoroscopeData(getIntent());
        initDatePicker();
        setTvStyle();
        mPrivacy.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        isFirstLogin = SPUtils.getBoolean(this, Constants.FIRST_LOGIN, true);
        mClose.setVisibility(isFirstLogin ? View.GONE : View.VISIBLE);
        mConfirm.setText(isFirstLogin ? R.string.get_started_now : R.string.confirm);
        mLayoutPrivacy.setVisibility(isFirstLogin ? View.VISIBLE : View.GONE);
        mAccept.setOnCheckedChangeListener(this);

        mClose.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        mPrivacy.setOnClickListener(this);

        mDatePicker.setOnDateSelectedListener(this);
    }

    /** */
    private void setTvStyle(){
        TextView[] textViews = {mTitle,mName,mConfirm};
        for (TextView tv:textViews){
            TextStyleSetting.setTvStyleRobotoBold(tv,this);
        }
    }

    private void initHoroscopeData(Intent intent) {
        if (intent == null) {
            return;
        }

        mHoroscopeDate = SPUtils.getString(this, Constants.HOROSCOPE_DATE, "");
        mHoroscopeId = intent.getIntExtra(Constants.HOROSCOPE_ID, 5);
        Horoscope horoscope = HoroscopeManager.getHoroscope(mHoroscopeId);
        if (horoscope == null) {
            return;
        }

        mName.setText(horoscope.getName());
        mImage.setImageResource(horoscope.getIconId_1());
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        mDatePicker.setYearRange(1900, calendar.get(Calendar.YEAR));
        if (TextUtils.isEmpty(mHoroscopeDate)) {
            mDatePicker.setSelectedYear(year - 19);
            mDatePicker.setSelectedMonth(horoscope.getBeginMonth());
            mDatePicker.setSelectedDay(horoscope.getBeginDay());
        } else {
            String[] arr = mHoroscopeDate.split("#");
            mDatePicker.setSelectedYear(Integer.valueOf(arr[0]));
            mDatePicker.setSelectedMonth(Integer.valueOf(arr[1]));
            mDatePicker.setSelectedDay(Integer.valueOf(arr[2]));
        }
    }

    private void initDatePicker() {
        mDatePicker.setCyclic(true);
        mDatePicker.setShowLabel(false);
        mDatePicker.setShowDivider(true);
        mDatePicker.setVisibleItems(3);
        mDatePicker.setTextBoundaryMargin(28, true);
        mDatePicker.setDividerColorRes(android.R.color.white);
        mDatePicker.setDividerHeight(0.5f, true);
        mDatePicker.setDividerType(WheelView.DIVIDER_TYPE_WRAP);
        mDatePicker.setNormalItemTextColorRes(R.color.gray_text_color);
        mDatePicker.setSelectedItemTextColorRes(android.R.color.white);
        mDatePicker.setTextSize(15, true);
        mDatePicker.setLineSpacing(12, true);
        mDatePicker.setCurved(false);
        mDatePicker.getMonthWv().setIntegerNeedFormat(true);

        mDatePicker.setDividerPaddingForWrap(12, true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.tv_confirm:
                FirebaseTracker.getInstance().track(AppTracker.constellations_select_start_click);
                if (isFirstLogin) {
                    SPUtils.setBoolean(this, Constants.FIRST_LOGIN, false);
                } else {
                    Intent intent = new Intent(Constants.RECEIVER_ACTION);
                    intent.putExtra(Constants.HOROSCOPE_ID, mHoroscopeId);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                }
                SPUtils.setInt(this, Constants.HOROSCOPE_ID, mHoroscopeId);
                SPUtils.setString(this, Constants.HOROSCOPE_DATE, mHoroscopeDate);
                MainActivity.start(this);
                finish();
                break;
            case R.id.tv_privacy:
                SystemUtils.startWebView(this, Constants.PRIVACY_URL);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        mConfirm.setEnabled(isChecked);
    }

    @Override
    public void onDateSelected(BaseDatePickerView datePickerView, int year, int month, int day, @Nullable Date date) {
        Horoscope horoscope = HoroscopeManager.getHoroscope(month, day);
        if (horoscope == null) {
            return;
        }

        mHoroscopeDate = year + "#" + month + "#" + day;
        mHoroscopeId = horoscope.getHoroscopeId();
        mName.setText(horoscope.getName());
        mImage.setImageResource(horoscope.getIconId_1());
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, HoroscopeSelectorActivity.class);
        context.startActivity(intent);
    }

    public static void start(Context context, int horoscopeId) {
        Intent intent = new Intent(context, HoroscopeSelectorActivity.class);
        intent.putExtra(Constants.HOROSCOPE_ID, horoscopeId);
        context.startActivity(intent);
    }
}
