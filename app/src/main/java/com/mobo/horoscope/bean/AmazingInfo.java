package com.mobo.horoscope.bean;

public class AmazingInfo {
    private String tvSubtitle;
    private String tvText;

    public AmazingInfo(String tvSubtitle, String tvText) {
        this.tvSubtitle = tvSubtitle;
        this.tvText = tvText;
    }

    public String getTvSubtitle() {
        return tvSubtitle;
    }

    public void setTvSubtitle(String tvSubtitle) {
        this.tvSubtitle = tvSubtitle;
    }

    public String getTvText() {
        return tvText;
    }

    public void setTvText(String tvText) {
        this.tvText = tvText;
    }
}
