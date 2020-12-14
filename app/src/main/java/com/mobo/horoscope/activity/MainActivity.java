package com.mobo.horoscope.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.mobo.horoscope.R;
import com.mobo.horoscope.adapter.BaseFragmentAdapter;
import com.mobo.horoscope.bean.TextStyleSetting;
import com.mobo.horoscope.fragment.FeatureFragment;
import com.mobo.horoscope.fragment.HoroscopeFragment;
import com.mobo.horoscope.fragment.SettingFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 主页
 * @Author: jzhou
 * @CreateDate: 19-8-13 上午9:12
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private BaseFragmentAdapter mFragmentAdapter;
    private ViewPager mViewPager;
    private SparseArray<View> mTabGroup;
    private View tabHoroscope;
    private View tabFeature;
    private View tabSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initIndicator();
        setTvStyle();
        initViewPager();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        ((HoroscopeFragment) mFragmentAdapter.getItem(0)).notifyData();
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void initViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(HoroscopeFragment.newInstance());
        fragments.add(FeatureFragment.newInstance());
        fragments.add(SettingFragment.newInstance());
        mFragmentAdapter = new BaseFragmentAdapter(getSupportFragmentManager(), fragments);

        //初始化ViewPager
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(mFragmentAdapter);
        //设置ViewPager的缓存界面数,避免切换时重复创建fragment实例
        mViewPager.setOffscreenPageLimit(fragments.size());
        switchTabIndicator(0);
    }

    private void initIndicator() {

        tabHoroscope = findViewById(R.id.tv_tab_horoscope);
        tabFeature = findViewById(R.id.tv_tab_feature);
        tabSetting = findViewById(R.id.tv_tab_setting);

        tabHoroscope.setOnClickListener(this);
        tabFeature.setOnClickListener(this);
        tabSetting.setOnClickListener(this);

        tabSetting.setSelected(true);

        mTabGroup = new SparseArray<>(5);
        mTabGroup.put(0, tabHoroscope);
        mTabGroup.put(1, tabFeature);
        mTabGroup.put(2, tabSetting);
    }

    /** */
    private void setTvStyle(){
        TextView[] textViews = {(TextView)tabHoroscope,(TextView)tabFeature,(TextView)tabSetting};
        for (TextView tv:textViews){
            TextStyleSetting.setTvStyleRobotoBold(tv,this);
        }
    }

    /**
     * 控制各模块切换逻辑
     *
     * @param index
     */
    private void switchTabIndicator(int index) {
        for (int i = 0; i < mTabGroup.size(); i++) {
            mTabGroup.valueAt(i).setSelected(false);
        }

        mTabGroup.valueAt(index).setSelected(true);
        mViewPager.setCurrentItem(index, false);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tab_horoscope:
                switchTabIndicator(0);
                break;
            case R.id.tv_tab_feature:
                switchTabIndicator(1);
                break;
            case R.id.tv_tab_setting:
                switchTabIndicator(2);
                break;
            default:
                break;
        }
    }
}
