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

public class InviteFriendsDialog extends Dialog implements View.OnClickListener {
    private OnFacebookClickListener mOnFacebookClickListener;
    private OnSystemClickListener mOnSystemClickListener;
    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_facebookShare;

    public InviteFriendsDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_invite_friend);

        int dividerId = getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = findViewById(dividerId);
        if (divider != null) {
            divider.setBackgroundColor(Color.TRANSPARENT);
        }

        setCanceledOnTouchOutside(false);
        Window window = this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        findViewById(R.id.iv_sys_share).setOnClickListener(this);

        tv_facebookShare = findViewById(R.id.tv_facebook_share);
        tv_facebookShare.setOnClickListener(this);
        tv_title = findViewById(R.id.tv_invite_title);
        tv_content = findViewById(R.id.tv_invite_content);
        TextStyleSetting.setTvStyleRoboto(tv_content,getContext());
        TextStyleSetting.setTvStyleRobotoBold(tv_title,getContext());
        TextStyleSetting.setTvStyleRobotoBold(tv_facebookShare,getContext());
    }

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.tv_facebook_share:
                if (mOnFacebookClickListener != null) {
                    mOnFacebookClickListener.onFacebookClick();
                }
                break;
            case R.id.iv_sys_share:
                if (mOnSystemClickListener != null) {
                    mOnSystemClickListener.onSystemClick();
                }
                break;
            default:
                break;
        }
    }

    public void setOnFacebookClickListener(OnFacebookClickListener listener) {
        mOnFacebookClickListener = listener;
    }

    public void setOnSystemClickListener(OnSystemClickListener listener) {
        mOnSystemClickListener = listener;
    }

    public interface OnFacebookClickListener {
        void onFacebookClick();
    }

    public interface OnSystemClickListener {
        void onSystemClick();
    }
}
