package com.example.shopapp.activities.network;

public class FCMTokenRequest {
    private String fcm_token;

    public FCMTokenRequest(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }
}