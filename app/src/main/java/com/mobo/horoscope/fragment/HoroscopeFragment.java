package com.mobo.horoscope.fragment;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.mobo.horoscope.R;
import com.mobo.horoscope.activity.HoroscopeSelectorActivity;
import com.mobo.horoscope.adapter.BaseFragmentAdapter;
import com.mobo.horoscope.bean.Horoscope;
import com.mobo.horoscope.bean.HoroscopeManager;
import com.mobo.horoscope.bean.MultiFortune;
import com.mobo.horoscope.bean.TextStyleSetting;
import com.mobo.horoscope.common.Constants;
import com.mobo.horoscope.common.DateUtils;
import com.mobo.horoscope.common.DownLoadUtil;
import com.mobo.horoscope.common.FileUtil;
import com.mobo.horoscope.common.SPUtils;
import com.mobo.horoscope.common.SystemUtils;
import com.mobo.horoscope.dialog.LoadingDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 星座运势主页
 */
public class HoroscopeFragment extends BaseFragment implements View.OnClickListener {
    private int mHoroscopeId;
    private TextView mHoroscopeName;
    private ImageView mHoroscopeIcon;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private View mNetworkError;
    private LoadingDialog mLoadingDialog;
    private BaseFragmentAdapter mFragmentAdapter;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            mHoroscopeId = intent.getIntExtra(Constants.HOROSCOPE_ID, 1);
            startDownLoadAsyncTask();
            resetHoroscopeInfo();
        }
    };

    public static HoroscopeFragment newInstance() {
        HoroscopeFragment fragment = new HoroscopeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_horoscope, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        registerReceiver();
    }

    private void initView() {
        mHoroscopeName = findViewById(R.id.tv_analysis);
        mHoroscopeIcon = findViewById(R.id.iv_horoscope_icon);
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewpager);
        mNetworkError = findViewById(R.id.layout_network_error);

        findViewById(R.id.tv_analysis).setOnClickListener(this);
        findViewById(R.id.tv_retry).setOnClickListener(this);

        mLoadingDialog = new LoadingDialog(getContext());
    }

    private void initData() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(YesterdayFortuneFragment.newInstance());
        fragments.add(TodayFortuneFragment.newInstance());
        fragments.add(TomorrowFortuneFragment.newInstance(Constants.TOMORROW));
        fragments.add(TomorrowFortuneFragment.newInstance(Constants.WEEKLY));
        fragments.add(TomorrowFortuneFragment.newInstance(Constants.MONTHLY));
        fragments.add(TomorrowFortuneFragment.newInstance(Constants.YEARLY));

        String[] titles = getResources().getStringArray(R.array.horoscope_tab_titles);

        mFragmentAdapter = new BaseFragmentAdapter(getChildFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mFragmentAdapter);
        //设置ViewPager的缓存界面数,避免切换时重复创建fragment实例
        mViewPager.setOffscreenPageLimit(fragments.size());
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(1);
        startDownLoadAsyncTask();
        resetHoroscopeInfo();
    }

    private void startDownLoadAsyncTask() {
        if (getContext() == null) {
            return;
        }
        if (!SystemUtils.isNetworkAvailable(getContext())) {
            Toast.makeText(getContext(), R.string.check_your_network, Toast.LENGTH_SHORT).show();
            mNetworkError.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.GONE);
            return;
        }
        new DownLoadAsyncTask().execute();
    }

    private void resetHoroscopeInfo() {
        mHoroscopeId = SPUtils.getInt(getContext(), Constants.HOROSCOPE_ID, 1);
        Horoscope horoscope = HoroscopeManager.getHoroscope(mHoroscopeId);
        if (horoscope == null) {
            return;
        }
        TextStyleSetting.setTvStyleRobotoBold(mHoroscopeName,getContext());
        mHoroscopeName.setText(horoscope.getName());
        mHoroscopeIcon.setImageResource(horoscope.getIconId_1());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_analysis:
                HoroscopeSelectorActivity.start(getContext(), mHoroscopeId);
                break;
            case R.id.tv_retry:
                startDownLoadAsyncTask();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
        super.onDestroyView();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.RECEIVER_ACTION);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, intentFilter);
    }

    public void notifyFragmentData(MultiFortune result) {
        try {
            ((YesterdayFortuneFragment) mFragmentAdapter.getItem(0)).notifyData(result.getGeneral_yesterday());
            ((TodayFortuneFragment) mFragmentAdapter.getItem(1)).notifyData(result.getGeneral_today(),result.getLove(),result.getCareer(),result.getHealth());
            ((TomorrowFortuneFragment) mFragmentAdapter.getItem(2)).notifyData(result.getGeneral_tomorrow());
            ((TomorrowFortuneFragment) mFragmentAdapter.getItem(3)).notifyData(result.getGeneral_weekly());
            ((TomorrowFortuneFragment) mFragmentAdapter.getItem(4)).notifyData(result.getGeneral_monthly());
            ((TomorrowFortuneFragment) mFragmentAdapter.getItem(5)).notifyData(result.getGeneral_yearly());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void notifyData() {
        startDownLoadAsyncTask();
    }

    class DownLoadAsyncTask extends AsyncTask<Void, Void, MultiFortune> {

        @Override
        protected void onPreExecute() {
            if (mLoadingDialog != null) {
                mLoadingDialog.show();
            }
        }

        private boolean downloadFile(int horoscopeId, File file, String fileName) {
            long currentTime = System.currentTimeMillis();
            long lastLoadTime = file.lastModified();
            //检查本地文件最近更新时间是否是今日，是则直接加载使用，否则重新加载今日数据
            if (DateUtils.compareDate(currentTime, lastLoadTime) == 0) {
                return true;
            }

            //加载今日数据
            String formatDate = DateUtils.formatDateTime(currentTime, "yyyyMMdd");
            String fileUrl = String.format(Constants.FORTUNE_URL, horoscopeId, formatDate);
            boolean ret = DownLoadUtil.downloadFile(getContext(), fileUrl, fileName);
            if (ret) {
                return true;
            }

            //检查本地文件最近更新时间是否是昨日，是则直接加载使用，否则重新加载昨日数据
            if (DateUtils.compareDate(currentTime, lastLoadTime) == 1) {
                return true;
            }

            //加载昨日数据
            formatDate = DateUtils.formatDateTime(currentTime - AlarmManager.INTERVAL_DAY, "yyyyMMdd");
            fileUrl = String.format(Constants.FORTUNE_URL, horoscopeId, formatDate);
            return DownLoadUtil.downloadFile(getContext(), fileUrl, fileName);
        }

        @Override
        protected MultiFortune doInBackground(Void... voids) {
            int horoscopeId = SPUtils.getInt(getContext(), Constants.HOROSCOPE_ID, 1);
            String fileName = horoscopeId + ".txt";
            File file = DownLoadUtil.getLocalFile(getContext(), fileName);
            if (file == null) {
                return null;
            }

            boolean ret = downloadFile(horoscopeId, file, fileName);
            if (!ret) {
                return null;
            }

            String fortune = FileUtil.getFromLocalFile(file.getPath());
            return new Gson().fromJson(fortune, MultiFortune.class);
        }

        @Override
        protected void onPostExecute(MultiFortune result) {
            if (result == null) {
                mNetworkError.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.GONE);
            } else {
                mNetworkError.setVisibility(View.GONE);
                mViewPager.setVisibility(View.VISIBLE);
                //刷新页面
                notifyFragmentData(result);
            }
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
            }
        }
    }
}
