package com.example.shopapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.shopapp.R;
import com.example.shopapp.activities.network.*;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task; // Đã thêm
import com.google.firebase.auth.*;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.*;
import okhttp3.ResponseBody;
import retrofit2.*;

public class LoginActivity extends AppCompatActivity {
    Button btnGoogle;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;
    int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnGoogle = findViewById(R.id.btnGoogle);
        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogle.setOnClickListener(v -> {
            googleSignInClient.signOut().addOnCompleteListener(task -> {
                startActivityForResult(googleSignInClient.getSignInIntent(), RC_SIGN_IN);
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Lỗi đăng nhập: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    void firebaseAuthWithGoogle(String idToken) {
        firebaseAuth.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() && firebaseAuth.getCurrentUser() != null) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        user.getIdToken(true).addOnCompleteListener(t -> {
                            if (t.isSuccessful()) {
                                String token = t.getResult().getToken();
                                String email = user.getEmail();

                                SharedPreferences sp = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
                                sp.edit().putString("jwt_token", token).apply();

                                Set<String> emails = new HashSet<>(sp.getStringSet("saved_accounts", new HashSet<>()));
                                if(email != null) {
                                    emails.add(email);
                                    sp.edit().putStringSet("saved_accounts", emails).apply();
                                }

                                sendFcmTokenToServer(token);
                                startActivity(new Intent(this, HomeActivity.class));
                                finish();
                            }
                        });
                    }
                });
    }

    private void sendFcmTokenToServer(String jwtToken) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) return;
            ApiService api = RetrofitClient.getInstance().create(ApiService.class);
            api.updateFCMToken("Bearer " + jwtToken, new FCMTokenRequest(task.getResult()))
                    .enqueue(new Callback<ResponseBody>() {
                        @Override public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {}
                        @Override public void onFailure(Call<ResponseBody> call, Throwable t) {}
                    });
        });
    }
}