package com.mobo.horoscope.bean;

/**
 * @Description: 匹配结果数据
 * @Author: jzhou
 * @CreateDate: 19-8-15 下午8:44
 */
public class MappingResult {
    private String love;
    private String career;
    private String friendship;

    public MappingResult() {
    }

    public String getLove() {
        return love;
    }

    public void setLove(String love) {
        this.love = love;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getFriendship() {
        return friendship;
    }

    public void setFriendship(String friendship) {
        this.friendship = friendship;
    }
}
