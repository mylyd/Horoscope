package com.mobo.horoscope.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

import com.mobo.horoscope.R;

public class LoadingDialog extends Dialog {
    private boolean isContentVisible;
    private boolean isCancelable;

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    public LoadingDialog(@NonNull Context context, boolean isCancelable, boolean isContentVisible) {
        super(context);
        this.isCancelable = isCancelable;
        this.isContentVisible = isContentVisible;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);

        int dividerId = getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = findViewById(dividerId);
        if (divider != null) {
            divider.setBackgroundColor(Color.TRANSPARENT);
        }

        setCanceledOnTouchOutside(false);
        setCancelable(isCancelable);
        Window window = this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View content = findViewById(R.id.tv_content);
        content.setVisibility(isContentVisible ? View.VISIBLE : View.GONE);
    }
}
