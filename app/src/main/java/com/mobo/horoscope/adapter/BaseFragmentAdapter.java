package com.mobo.horoscope.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/3/29.
 */
public class BaseFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
    private String[] mTitles;

    public BaseFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    public BaseFragmentAdapter(FragmentManager fm, List<Fragment> fragments, String[] titles) {
        this(fm, fragments);
        mTitles = titles;
    }

    public void notifyDataSetChanged(List<Fragment> fragments, String[] titles) {
        mFragments = fragments;
        mTitles = titles;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles == null ? "" : mTitles[position];
    }
}
