package com.mobo.horoscope.common;

/**
 * Created by Administrator on 2017/3/29.
 */
public class Constants {
    public static final String FORTUNE_URL = "http://d.c-launcher.com/horoscope/%d_%s.json";
//    public static final String FORTUNE_URL = "http://d.c-launcher.com/horoscope/";
    public static final String PRIVACY_URL = "https://sites.google.com/view/horoscopeplus";
    public static final String APP_STARE_URL = "https://play.google.com/store/apps/details?id=com.mobo.horoscope";
    public static final String RECEIVER_ACTION = "com.mobo.horoscope.HOROSCOPE_CHANGED";
    public static final String ALARM_BROADCAST_ACTION = "com.mobo.horoscope.ALARM_BROADCAST_ACTION";

    /*每一种运势时间Fragment页面今日是否已经看过激励广告*/
    public static final String HAS_WATCH_REWARD_ADS = "has_watch_reward_ads_";
    /*每一种星座最近一次加载运势数据的日期*/
    public static final String LAST_LOAD_FORTUNE_DATE = "last_load_fortune_date_";

    public static final String FROM_HOROSCOPE_ID = "from_horoscope_id";
    public static final String TO_HOROSCOPE_ID = "to_horoscope_id";

    public static final String HOROSCOPE_ID = "horoscope_id";
    public static final String HOROSCOPE_DATE = "horoscope_date";
    public static final String FIRST_LOGIN = "first_login";
    public static final String PUSH_ENABLE = "push_enable";//是否接收推送


    public static final int TOMORROW = 1;
    public static final int WEEKLY = 2;
    public static final int MONTHLY = 3;
    public static final int YEARLY = 4;
}
