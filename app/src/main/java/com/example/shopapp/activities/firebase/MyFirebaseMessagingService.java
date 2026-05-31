package com.example.shopapp.activities.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.example.shopapp.R;
import com.example.shopapp.activities.network.ApiService;
import com.example.shopapp.activities.network.FCMTokenRequest;
import com.example.shopapp.activities.network.RetrofitClient;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM_SERVICE";

    // 1. Logic gửi Token lên server (Giữ nguyên của bạn)
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Token mới: " + token);
        sendTokenToServer(token);
    }

    private void sendTokenToServer(String token) {
        // LƯU Ý: Kiểm tra lại tên SharedPreferences (dùng "AppPrefs" cho đồng bộ với LoginActivity)
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String jwtToken = sharedPreferences.getString("jwt_token", "");

        if (jwtToken.isEmpty()) {
            Log.d(TAG, "Chưa đăng nhập, chưa thể gửi token lên server");
            return;
        }

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        FCMTokenRequest request = new FCMTokenRequest(token);

        apiService.updateFCMToken("Bearer " + jwtToken, request)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) Log.d(TAG, "Đã gửi FCM Token lên server thành công!");
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(TAG, "Lỗi kết nối: " + t.getMessage());
                    }
                });
    }

    // 2. Logic nhận thông báo (Bổ sung mới)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Kiểm tra xem tin nhắn có chứa phần tử notification hay không
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            showNotification(title, body);
        }
    }

    private void showNotification(String title, String body) {
        String channelId = "order_channel";
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Tạo kênh thông báo cho Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, "Order Notifications", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher) // Đảm bảo icon này tồn tại
                .setContentTitle(title != null ? title : "Thông báo đơn hàng")
                .setContentText(body != null ? body : "Đơn hàng của bạn đã được cập nhật.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        manager.notify(1001, builder.build());
    }
}