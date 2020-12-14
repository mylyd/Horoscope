package com.mobo.horoscope.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * 不可以通过触摸左右滑动的ViewPager，setCurrentItem(m)会有滑动效果
 * Created by Administrator on 2017/3/29.
 */
public class UnTouchScrollViewPager extends ViewPager {

    public UnTouchScrollViewPager(Context context) {
        super(context);
    }

    public UnTouchScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
