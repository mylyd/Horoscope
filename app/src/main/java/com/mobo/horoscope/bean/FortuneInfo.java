package com.mobo.horoscope.bean;

public class FortuneInfo {
    private String main_horoscope;
    private MatchInfo match;
    private RatingInfo ratings;

    public String getMain_horoscope() {
        return main_horoscope;
    }

    public void setMain_horoscope(String main_horoscope) {
        this.main_horoscope = main_horoscope;
    }

    public MatchInfo getMatch() {
        return match;
    }

    public void setMatch(MatchInfo match) {
        this.match = match;
    }

    public RatingInfo getRatings() {
        return ratings;
    }

    public void setRatings(RatingInfo ratings) {
        this.ratings = ratings;
    }
}