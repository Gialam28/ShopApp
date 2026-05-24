package com.example.shopapp.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopapp.R;
import com.example.shopapp.activities.adapters.ProductAdapter;
import com.example.shopapp.activities.models.Product;
import com.example.shopapp.activities.network.ApiService;
import com.example.shopapp.activities.network.RetrofitClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerProducts =
                findViewById(R.id.recyclerProducts);

        recyclerProducts.setLayoutManager(
                new LinearLayoutManager(this)
        );

        FirebaseUser user =
                FirebaseAuth.getInstance()
                        .getCurrentUser();

        if(user != null){

            user.getIdToken(true)
                    .addOnCompleteListener(task -> {

                        if(task.isSuccessful()){

                            String token =
                                    task.getResult().getToken();

                            loadProducts(token);
                        }

                    });
        }
    }

    void loadProducts(String token){

        ApiService apiService =
                RetrofitClient
                        .getRetrofit()
                        .create(ApiService.class);

        Call<List<Product>> call =
                apiService.getProducts(
                        "Bearer " + token
                );

        call.enqueue(new Callback<List<Product>>() {

            @Override
            public void onResponse(
                    Call<List<Product>> call,
                    Response<List<Product>> response
            ) {

                if(response.isSuccessful()
                        && response.body() != null){

                    ProductAdapter adapter =
                            new ProductAdapter(
                                    response.body()
                            );

                    recyclerProducts.setAdapter(adapter);

                } else {

                    Toast.makeText(
                            HomeActivity.this,
                            "API Error",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<List<Product>> call,
                    Throwable t
            ) {

                Toast.makeText(
                        HomeActivity.this,
                        t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}