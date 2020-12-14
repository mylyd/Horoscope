package com.mobo.horoscope.tracker;

/**
 * @Description: tracker埋点
 * @Author: jzhou
 * @CreateDate: 19-8-16 上午8:38
 */
public interface AppTracker {
    /*启动页展示次数*/
    String start_page_show = "start_page_show";
    /*星座选择页面展示次数*/
    String constellations_select_page_show = "constellations_select_page_show";
    /*星座选择页面“String GET STARTED NOW”按钮点击次数*/
    String constellations_select_start_click = "constellations_select_start_click";
    /*今日运势页面FACEBOOK分享按钮展示次数*/
    String today_horoscope_share_show = "today_horoscope_share_show";
    /*今日运势页面系统分享按钮点击次数*/
    String today_horoscope_share_click = "today_horoscope_share_click";
    /*今日运势页面FACEBOOK分享按钮点击次数*/
    String today_horoscope_facebook_click = "today_horoscope_facebook_click";
    /*昨日运势页面展示次数*/
    String yesterday_horoscope_share_show = "yesterday_horoscope_share_show";
    /*明日运势页面展示次数*/
    String tomorrow_horoscope_share_show = "tomorrow_horoscope_share_show";
    /*周运势页面展示次数*/
    String week_horoscope_share_show = "week_horoscope_share_show";
    /* 月运势页面展示次数*/
    String month_horoscope_share_show = "month_horoscope_share_show";
    /*年运势页面展示次数*/
    String year_horoscope_share_show = "year_horoscope_share_show";
    /*星座匹配页面展示次数*/
    String compatibility_page_show = "compatibility_page_show";
    /*匹配分析页面展示次数*/
    String compatibility_detail_page_show = "compatibility_detail_page_show";
    /*匹配分析页面FACEBOOK分享按钮展示次数*/
    String compatibility_detail_share_show = "compatibility_detail_share_show";
    /*匹配分析页面系统分享按钮点击次数*/
    String compatibility_detail_share_click = "compatibility_detail_share_click";
    /*匹配分析页面FACEBOOK分享按钮点击次数*/
    String compatibility_detail_facebook_click = "compatibility_detail_facebook_click";
    /*性格分析页面展示次数*/
    String character_page_show = "character_page_show";
    /* 性格分析解析页面展示次数*/
    String character_detail_page_show = "character_detail_page_show";
    /*性格分析解析页面FACEBOOK分享按钮展示次数*/
    String character_detail_share_show = "character_detail_share_show";
    /*性格分析解析页面系统分享按钮点击次数*/
    String character_detail_share_click = "character_detail_share_click";
    /*性格分析解析页面FACEBOOK分享按钮点击次数*/
    String character_detail_facebook_click = "character_detail_facebook_click";
    /*设置页面展示次数*/
    String setting_page_show = "setting_page_show";
    /*Rate Us弹窗展示次数*/
    String rate_us_show = "rate_us_show";
    /*好评按钮点击次数*/
    String rate_us_good_click = "rate_us_good_click";
    /*FeedBack弹窗展示次数*/
    String feedback_page_show = "feedback_page_show";
    /*FeedBack页面提交按钮点击次数*/
    String feedback_page_sub_click = "feedback_page_sub_click";
}
