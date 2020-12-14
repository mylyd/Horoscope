package com.mobo.horoscope.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobo.horoscope.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 自定义评分控件
 * @Author: jzhou
 * @CreateDate: 19-8-15 上午11:54
 */
public class CommonRatingBar extends LinearLayout {
    private List<ImageView> mStartList;

    public CommonRatingBar(Context context) {
        super(context);
        initView(context);
    }

    public CommonRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CommonRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_rating_bar, this, true);

        mStartList = new ArrayList<>(5);
        mStartList.add((ImageView) findViewById(R.id.iv_start_1));
        mStartList.add((ImageView) findViewById(R.id.iv_start_2));
        mStartList.add((ImageView) findViewById(R.id.iv_start_3));
        mStartList.add((ImageView) findViewById(R.id.iv_start_4));
        mStartList.add((ImageView) findViewById(R.id.iv_start_5));
    }

    public void setRatingScore(int score) {
        if (score <= 0) {
            score = 0;
        }

        if (score >= mStartList.size()) {
            score = mStartList.size();
        }

        for (int i = 0; i < mStartList.size(); i++) {
            ImageView star = mStartList.get(i);
            if (star == null) {
                break;
            }
            if (i < score) {
                star.setImageResource(R.drawable.ic_star_on);
            } else {
                star.setImageResource(R.drawable.ic_star_off);
            }
        }
    }
}
