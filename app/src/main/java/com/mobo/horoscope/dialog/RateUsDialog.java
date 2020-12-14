package com.mobo.horoscope.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mobo.horoscope.R;
import com.mobo.horoscope.bean.TextStyleSetting;

public class RateUsDialog extends Dialog implements View.OnClickListener {
    private OnHighRateClickListener mOnHighRateClickListener;
    private OnBadRateClickListener mOnBadRateClickListener;
    private TextView tvContent;
    private TextView tvtitle;

    public RateUsDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rate_us);

        int dividerId = getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = findViewById(dividerId);
        if (divider != null) {
            divider.setBackgroundColor(Color.TRANSPARENT);
        }

        setCanceledOnTouchOutside(false);
        Window window = this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        findViewById(R.id.iv_high_rate).setOnClickListener(this);
        findViewById(R.id.iv_bad_rate).setOnClickListener(this);
        tvtitle = findViewById(R.id.tv_dialog_rateUpTitle);
        tvContent = findViewById(R.id.tv_dialog_rateUpContent);
        TextStyleSetting.setTvStyleRobotoBold(tvtitle,getContext());
        TextStyleSetting.setTvStyleRoboto(tvContent,getContext());
    }


    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.iv_high_rate:
                if (mOnHighRateClickListener != null) {
                    mOnHighRateClickListener.onHighRateClick();
                }
                break;
            case R.id.iv_bad_rate:
                if (mOnBadRateClickListener != null) {
                    mOnBadRateClickListener.onBadRateClick();
                }
                break;
            default:
                break;
        }
    }

    public void setOnHighRateClickListener(OnHighRateClickListener listener) {
        mOnHighRateClickListener = listener;
    }

    public void setOnBadRateClickListener(OnBadRateClickListener listener) {
        mOnBadRateClickListener = listener;
    }

    public interface OnHighRateClickListener {
        void onHighRateClick();
    }

    public interface OnBadRateClickListener {
        void onBadRateClick();
    }
}
