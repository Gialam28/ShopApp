package com.example.shopapp.activities.network;

import com.example.shopapp.activities.models.Order;
import com.example.shopapp.activities.models.Product;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // =========================
    // PRODUCTS
    // =========================

    @GET("api/products")
    Call<List<Product>> getProducts(
            @Header("Authorization") String token
    );

    @POST("api/products")
    Call<ResponseBody> createProduct(
            @Header("Authorization") String token,
            @Body Product product
    );

    @PUT("api/products/{id}")
    Call<ResponseBody> updateProduct(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Body Product product
    );

    @DELETE("api/products/{id}")
    Call<ResponseBody> deleteProduct(
            @Header("Authorization") String token,
            @Path("id") String id
    );

    // =========================
    // ORDERS
    // =========================

    @POST("api/orders")
    Call<ResponseBody> placeOrder(
            @Header("Authorization") String token,
            @Body List<Product> orderItems
    );

    @GET("api/orders")
    Call<List<Order>> getOrders(
            @Header("Authorization") String token
    );

    // =========================
    // USER & FCM
    // =========================

    @POST("api/users/fcm-token")
    Call<ResponseBody> updateFCMToken(
            @Header("Authorization") String token,
            @Body FCMTokenRequest body
    );
}