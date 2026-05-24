package com.example.shopapp.activities.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // emulator dùng 10.0.2.2 thay localhost
    private static final String BASE_URL =
            "http://10.0.2.2:3000/";

    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {

        if(retrofit == null){

            retrofit =
                    new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(
                                    GsonConverterFactory.create()
                            )
                            .build();
        }

        return retrofit;
    }
}