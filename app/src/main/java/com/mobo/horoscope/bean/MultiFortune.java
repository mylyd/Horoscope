package com.mobo.horoscope.bean;

/**
 * @Description: 综合运势信息
 * @Author: jzhou
 * @CreateDate: 19-8-14 下午4:40
 */
public class MultiFortune {
    private FortuneInfo general_yesterday;
    private FortuneInfo general_today;
    private FortuneInfo general_tomorrow;
    private FortuneInfo general_weekly;
    private FortuneInfo general_monthly;
    private FortuneInfo general_yearly;
    private FortuneInfo love;
    private FortuneInfo career;
    private FortuneInfo health;

    public FortuneInfo getGeneral_yesterday() {
        return general_yesterday;
    }

    public void setGeneral_yesterday(FortuneInfo general_yesterday) {
        this.general_yesterday = general_yesterday;
    }

    public FortuneInfo getGeneral_today() {
        return general_today;
    }

    public void setGeneral_today(FortuneInfo general_today) {
        this.general_today = general_today;
    }

    public FortuneInfo getGeneral_tomorrow() {
        return general_tomorrow;
    }

    public void setGeneral_tomorrow(FortuneInfo general_tomorrow) {
        this.general_tomorrow = general_tomorrow;
    }

    public FortuneInfo getGeneral_weekly() {
        return general_weekly;
    }

    public void setGeneral_weekly(FortuneInfo general_weekly) {
        this.general_weekly = general_weekly;
    }

    public FortuneInfo getGeneral_monthly() {
        return general_monthly;
    }

    public void setGeneral_monthly(FortuneInfo general_monthly) {
        this.general_monthly = general_monthly;
    }

    public FortuneInfo getGeneral_yearly() {
        return general_yearly;
    }

    public void setGeneral_yearly(FortuneInfo general_yearly) {
        this.general_yearly = general_yearly;
    }

    public FortuneInfo getLove() {
        return love;
    }

    public void setLove(FortuneInfo love) {
        this.love = love;
    }

    public FortuneInfo getCareer() {
        return career;
    }

    public void setCareer(FortuneInfo career) {
        this.career = career;
    }

    public FortuneInfo getHealth() {
        return health;
    }

    public void setHealth(FortuneInfo health) {
        this.health = health;
    }
}
