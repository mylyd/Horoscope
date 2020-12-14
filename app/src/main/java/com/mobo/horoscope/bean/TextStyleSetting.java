package com.mobo.horoscope.bean;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * 设置字体样式
 */
public class TextStyleSetting {

    public static String Tv_Style_FandolSong = "fonts/FandolSong.ttf";
    public static String Tv_Style_Roboto = "fonts/Roboto.ttf";
    public static String Tv_Style_RobotoBold = "fonts/Roboto_bold.ttf";

    public static void setTvStyleFont(TextView tv, Context context,String tvFont){
        AssetManager asset = context.getAssets();
        Typeface tyf = Typeface.createFromAsset(asset,tvFont);
        tv.setTypeface(tyf);
    }

    public static void setTvStyleFandolSong(TextView tv, Context context){
        setTvStyleFont(tv,context,TextStyleSetting.Tv_Style_FandolSong);
    }

    public static void setTvStyleRoboto(TextView tv, Context context){
        setTvStyleFont(tv,context,TextStyleSetting.Tv_Style_Roboto);
    }

    public static void setTvStyleRobotoBold(TextView tv, Context context){
        setTvStyleFont(tv,context,TextStyleSetting.Tv_Style_RobotoBold);
    }
}
