package com.mobo.horoscope.bean;

import java.util.List;

/**
 * @Description: 性格分析数据
 * @Author: jzhou
 * @CreateDate: 19-8-15 下午8:44
 */
public class CharacteristicInfo {
    private String name;
    private String date;
    private String keywords1;
    private String keywords2;
    private String keywords3;
    private String element;
    private String polarity;
    private String color;
    private String gem;
    private String flower;
    private String analysis;
    private String motto;
    private List<String> say;

    public CharacteristicInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKeywords1() {
        return keywords1;
    }

    public void setKeywords1(String keywords1) {
        this.keywords1 = keywords1;
    }

    public String getKeywords2() {
        return keywords2;
    }

    public void setKeywords2(String keywords2) {
        this.keywords2 = keywords2;
    }

    public String getKeywords3() {
        return keywords3;
    }

    public void setKeywords3(String keywords3) {
        this.keywords3 = keywords3;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getPolarity() {
        return polarity;
    }

    public void setPolarity(String polarity) {
        this.polarity = polarity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getGem() {
        return gem;
    }

    public void setGem(String gem) {
        this.gem = gem;
    }

    public String getFlower() {
        return flower;
    }

    public void setFlower(String flower) {
        this.flower = flower;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public List<String> getSay() {
        return say;
    }

    public void setSay(List<String> say) {
        this.say = say;
    }
}
