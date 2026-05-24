package com.example.shopapp.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopapp.R;
import com.example.shopapp.activities.models.Product;
import com.example.shopapp.activities.network.ApiService;
import com.example.shopapp.activities.network.RetrofitClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView txtData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtData = findViewById(R.id.txtData);

        FirebaseUser user =
                FirebaseAuth
                        .getInstance()
                        .getCurrentUser();

        if(user != null){

            user.getIdToken(true)
                    .addOnCompleteListener(task -> {

                        if(task.isSuccessful()){

                            String token =
                                    task.getResult().getToken();

                            callApi(token);
                        }

                    });
        }
    }

    void callApi(String firebaseToken){

        ApiService apiService =
                RetrofitClient
                        .getRetrofit()
                        .create(ApiService.class);

        Call<List<Product>> call =
                apiService.getProducts(
                        "Bearer " + firebaseToken
                );

        call.enqueue(new Callback<List<Product>>() {

            @Override
            public void onResponse(
                    Call<List<Product>> call,
                    Response<List<Product>> response
            ) {

                if(response.isSuccessful()
                        && response.body() != null){

                    List<Product> products =
                            response.body();

                    StringBuilder data =
                            new StringBuilder();

                    for(Product p : products){

                        data.append(
                                p.getTitle()
                        ).append("\n\n");
                    }

                    txtData.setText(
                            data.toString()
                    );

                } else {

                    txtData.setText(
                            "API ERROR"
                    );
                }
            }

            @Override
            public void onFailure(
                    Call<List<Product>> call,
                    Throwable t
            ) {

                txtData.setText(
                        t.getMessage()
                );
            }
        });
    }
}