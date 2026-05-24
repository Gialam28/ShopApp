package com.example.shopapp.activities.network;

import com.example.shopapp.activities.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiService {

    @GET("products")
    Call<List<Product>> getProducts(
            @Header("Authorization") String token
    );
}