package com.mobo.horoscope.network;


import com.mobo.horoscope.bean.GrayItem;
import com.google.gson.JsonElement;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * @author : create by zq
 * @time : 19-4-30  15:47
 */
public interface LaunRequest {
    String HOST = "http://api.u-launcher.com/";

    @GET("http://mobotoolpush.moboapps.io/ipo/api/gray/status")
    Call<GrayItem> getGrayStatus(@QueryMap Map<String, String> params);

    @POST("http://api.u-launcher.com/client/v2/user/feedback.json")
    Call<JsonElement> postFeedBack(@QueryMap Map<String, String> params);
}
