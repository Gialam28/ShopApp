package com.example.shopapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Lấy dữ liệu danh sách tài khoản đã lưu
        SharedPreferences sp = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        Set<String> accounts = sp.getStringSet("saved_accounts", new HashSet<>());

        // 2. Logic điều hướng:
        // - Nếu chưa có tài khoản nào -> Login
        // - Nếu có nhiều hơn 1 tài khoản -> Chọn tài khoản
        // - Nếu chỉ có 1 tài khoản -> Vào thẳng (giữ nguyên logic cũ)
        if (accounts.isEmpty()) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (accounts.size() > 1) {
            startActivity(new Intent(this, AccountSelectionActivity.class));
        } else {
            startActivity(new Intent(this, HomeActivity.class));
        }

        // 3. Kết thúc Splash để người dùng không quay lại được màn hình này khi nhấn nút Back
        finish();
    }
}