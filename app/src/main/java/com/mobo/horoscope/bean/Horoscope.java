package com.mobo.horoscope.bean;

/**
 * @Description: 星座信息
 * @Author: jzhou
 * @CreateDate: 19-8-14 上午8:45
 */
public class Horoscope {
    private int horoscopeId;
    private String name;
    private int iconId_1;
    private int iconId_2;
    private int beginMonth;
    private int beginDay;
    private int endMonth;
    private int endDay;
    private String date;

    public Horoscope() {
    }

    public Horoscope(int horoscopeId, String name, int iconId_1, int iconId_2, int beginMonth,
                     int beginDay, int endMonth, int endDay, String date) {
        this.horoscopeId = horoscopeId;
        this.name = name;
        this.iconId_1 = iconId_1;
        this.iconId_2 = iconId_2;
        this.beginMonth = beginMonth;
        this.beginDay = beginDay;
        this.endMonth = endMonth;
        this.endDay = endDay;
        this.date = date;
    }

    public int getHoroscopeId() {
        return horoscopeId;
    }

    public void setHoroscopeId(int horoscopeId) {
        this.horoscopeId = horoscopeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconId_1() {
        return iconId_1;
    }

    public void setIconId_1(int iconId_1) {
        this.iconId_1 = iconId_1;
    }

    public int getIconId_2() {
        return iconId_2;
    }

    public void setIconId_2(int iconId_2) {
        this.iconId_2 = iconId_2;
    }

    public int getBeginMonth() {
        return beginMonth;
    }

    public void setBeginMonth(int beginMonth) {
        this.beginMonth = beginMonth;
    }

    public int getBeginDay() {
        return beginDay;
    }

    public void setBeginDay(int beginDay) {
        this.beginDay = beginDay;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }

    public int getEndDay() {
        return endDay;
    }

    public void setEndDay(int endDay) {
        this.endDay = endDay;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
