package com.example.shopapp.activities.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // emulator dùng 10.0.2.2 thay localhost
    private static final String BASE_URL =
            "http://192.168.100.19:3000/";  //đổi nếu không chạy máy ảo

    private static Retrofit instance = null;

    // ĐÂY LÀ PHƯƠNG THỨC MÀ LOGINACTIVITY ĐANG CẦN
    public static Retrofit getInstance() {
        if (instance == null) {
            instance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instance;
    }

    // PHƯƠNG THỨC CŨ (GIỮ LẠI): Phục vụ cho ProductAdapter và các file cũ
    // Đảm bảo không làm hỏng logic của dự án hiện có
    public static Retrofit getRetrofit() {
        return getInstance();
    }
}