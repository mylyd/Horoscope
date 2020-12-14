package com.mobo.horoscope.bean;

import java.util.List;

/**
 * @author : create by zq
 * @time : 19-7-31  10:36
 */
public class GrayItem {

    /**
     * status : 0
     * msg :
     * data : [{"status":true,"tag":"testpage_bottom_banner_ads"},{"status":true,"tag":"homepage_top_banner_ads"},{"status":true,"tag":"homepage_full_screen_ads"},{"status":true,"tag":"answerpage_bottom_banner_ads"},{"status":true,"tag":"testpage_full_screen_ads"}]
     */

    private int status;
    private String msg;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * status : true
         * tag : testpage_bottom_banner_ads
         */

        private boolean status;
        private String tag;

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }
}
