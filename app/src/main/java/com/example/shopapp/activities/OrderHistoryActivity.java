package com.example.shopapp.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopapp.R;
import com.example.shopapp.activities.adapters.OrderAdapter;
import com.example.shopapp.activities.models.Order;
import com.example.shopapp.activities.network.ApiService;
import com.example.shopapp.activities.network.RetrofitClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity
        extends AppCompatActivity {

    RecyclerView recyclerOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        recyclerOrders =
                findViewById(R.id.recyclerOrders);

        recyclerOrders.setLayoutManager(
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

                            loadOrders(token);
                        }

                    });
        }
    }

    void loadOrders(String token){

        ApiService apiService =
                RetrofitClient
                        .getRetrofit()
                        .create(ApiService.class);

        apiService.getOrders(
                "Bearer " + token
        ).enqueue(new Callback<List<Order>>() {

            @Override
            public void onResponse(
                    Call<List<Order>> call,
                    Response<List<Order>> response
            ) {

                if(response.isSuccessful()
                        && response.body() != null){

                    OrderAdapter adapter =
                            new OrderAdapter(
                                    response.body()
                            );

                    recyclerOrders.setAdapter(adapter);

                } else {

                    Toast.makeText(
                            OrderHistoryActivity.this,
                            "Cannot load orders",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<List<Order>> call,
                    Throwable t
            ) {

                Toast.makeText(
                        OrderHistoryActivity.this,
                        t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}