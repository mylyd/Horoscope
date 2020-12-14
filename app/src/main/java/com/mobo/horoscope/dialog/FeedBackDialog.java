package com.mobo.horoscope.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mobo.horoscope.R;
import com.mobo.horoscope.bean.TextStyleSetting;

public class FeedBackDialog extends Dialog implements View.OnClickListener {
    private setOnConfirmClickListener mOnConfirmClickListener;
    private EditText mEmail;
    private EditText mSuggestion;
    private TextView tvSubmit;
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvEmail;
    private TextView tvSuggestion;

    public FeedBackDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_feedback);

        int dividerId = getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = findViewById(dividerId);
        if (divider != null) {
            divider.setBackgroundColor(Color.TRANSPARENT);
        }

        setCanceledOnTouchOutside(false);
        Window window = this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mEmail = findViewById(R.id.et_email);
        mSuggestion = findViewById(R.id.et_suggestion);
        tvSubmit = findViewById(R.id.tv_submit);
        tvSubmit.setOnClickListener(this);
        tvTitle = findViewById(R.id.tv_facebook_title);
        tvContent = findViewById(R.id.tv_facebook_content);
        tvEmail = findViewById(R.id.tv_facebook_email);
        tvSuggestion = findViewById(R.id.tv_facebook_suggestion);
        setTvStyle();
    }

    private void setTvStyle(){
        TextView[] textViews = {tvTitle,tvEmail,tvSubmit,tvSuggestion};
        for (TextView tv : textViews){
            TextStyleSetting.setTvStyleRobotoBold(tv,getContext());
        }
        TextStyleSetting.setTvStyleRoboto(tvContent,getContext());
    }

    @Override
    public void onClick(View v) {
        if (mOnConfirmClickListener != null) {
            String email = mEmail.getText().toString();
            String suggestion = mSuggestion.getText().toString();
            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(suggestion)) {
                mOnConfirmClickListener.onConfirmClick(email, suggestion);
                mEmail.setText("");
                mSuggestion.setText("");
                dismiss();
            }
        }
    }

    public void setOnConfirmClickListener(setOnConfirmClickListener listener) {
        mOnConfirmClickListener = listener;
    }

    public interface setOnConfirmClickListener {
        void onConfirmClick(String email, String suggestion);
    }
}
