package com.mobo.horoscope.fragment;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;

/**
 * Fragment基类
 */
public class BaseFragment extends Fragment {

    protected <T extends View> T findViewById(@IdRes int id) {
        try {
            return getView().findViewById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
