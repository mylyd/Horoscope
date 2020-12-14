package com.mobo.horoscope.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * SharedPreferences的一个工具类，调用setParam就能保存String, Integer, Boolean, Float, Long类型的参数
 * 同样调用getParam就能获取到保存在手机里面的数据
 *
 * @author xiaanming
 */
public class SPUtils {
    //保存在手机里面的文件名
    private static final String FILE_NAME = "sp_horoscope";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public static void put(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String get(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    public static <T> void setArrayObject(Context context, String key, List<T> objects) {
        if (objects != null && objects.size() > 0) {
            String value = new Gson().toJson(objects);
            SharedPreferences.Editor editor = getEditor(context);
            editor.putString(key, value);
            editor.commit();
        }
    }

    public static <T> List<T> getArrayObject(Context context, String key, Type type) {
        String jsonString = getSharedPreferences(context).getString(key, "");
        if (!TextUtils.isEmpty(jsonString)) {
            return new Gson().fromJson(jsonString, type);
        }
        return new ArrayList<>();
    }

    public static <T> void setObject(Context context, T object) {
        if (object != null) {
            String key = object.getClass().getSimpleName();
            String value = new Gson().toJson(object);
            SharedPreferences.Editor editor = getEditor(context);
            editor.putString(key, value);
            editor.commit();
        }
    }

    public static <T> T getObject(Context context, Class<T> classType) {
        String key = classType.getSimpleName();
        String jsonString = getSharedPreferences(context).getString(key, "");
        if (!TextUtils.isEmpty(jsonString)) {
            return new Gson().fromJson(jsonString, classType);
        }
        return null;
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(key, value);
        editor.commit();
    }

    public static void setInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt(key, value);
        editor.commit();
    }

    public static synchronized void setBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void setFloat(Context context, String key, float value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putFloat(key, value);
        editor.commit();
    }

    public static void setLong(Context context, String key, long value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putLong(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static int getInt(Context context, String key, int defValue) {
        return getSharedPreferences(context).getInt(key, defValue);
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getSharedPreferences(context).getBoolean(key, defValue);
    }

    public static float getFloat(Context context, String key, float defValue) {
        return getSharedPreferences(context).getFloat(key, defValue);
    }

    public static long getLong(Context context, String key, long defValue) {
        return getSharedPreferences(context).getLong(key, defValue);
    }

    public static void removeData(Context context, String... keys) {
        SharedPreferences.Editor editor = getEditor(context);
        for (String key : keys) {
            editor.remove(key);
        }
        editor.commit();
    }

    public static void removeObject(Context context, Class<?> cls) {
        String key = cls.getSimpleName();
        removeData(context, key);
    }
}
