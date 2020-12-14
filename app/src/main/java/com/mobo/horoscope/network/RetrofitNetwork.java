package com.mobo.horoscope.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Description: 网络请求
 * @Author: jzhou
 * @CreateDate: 19-8-13 上午9:12
 */
public enum RetrofitNetwork {
    INSTANCE;

    private static Retrofit retrofit;
    private static volatile LaunRequest request = null;

    RetrofitNetwork() {
        init();
    }

    private void init() {
        // 初始化okhttp
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        // 初始化Retrofit

        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(LaunRequest.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public LaunRequest getRequest() {
        if (request == null) {
            synchronized (LaunRequest.class) {
                request = retrofit.create(LaunRequest.class);
            }
        }
        return request;
    }
}
