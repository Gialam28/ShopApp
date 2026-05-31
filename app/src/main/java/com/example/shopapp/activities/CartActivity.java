package com.example.shopapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopapp.R;
import com.example.shopapp.activities.adapters.CartAdapter;
import com.example.shopapp.activities.models.Product;
import com.example.shopapp.activities.network.ApiService;
import com.example.shopapp.activities.network.RetrofitClient;
import com.example.shopapp.activities.utils.CartManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerCart;

    TextView tvTotal;

    Button btnCheckout;

    String firebaseToken = "";

    List<Product> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerCart =
                findViewById(R.id.recyclerCart);

        tvTotal =
                findViewById(R.id.tvTotal);

        btnCheckout =
                findViewById(R.id.btnCheckout);

        recyclerCart.setLayoutManager(
                new LinearLayoutManager(this)
        );

        cartItems =
                CartManager.getCartItems();

        CartAdapter adapter =
                new CartAdapter(cartItems);

        recyclerCart.setAdapter(adapter);

        tvTotal.setText(
                "Tổng tiền: " +
                        CartManager.getTotalPrice() +
                        "đ"
        );

        FirebaseUser user =
                FirebaseAuth.getInstance()
                        .getCurrentUser();

        if(user != null){

            user.getIdToken(true)
                    .addOnCompleteListener(task -> {

                        if(task.isSuccessful()){

                            firebaseToken =
                                    task.getResult().getToken();
                        }

                    });
        }

        btnCheckout.setOnClickListener(v -> {

            checkout();
        });
    }

    void checkout(){

        ApiService apiService =
                RetrofitClient
                        .getRetrofit()
                        .create(ApiService.class);

        apiService.placeOrder(
                "Bearer " + firebaseToken,
                cartItems
        ).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(
                    Call<ResponseBody> call,
                    Response<ResponseBody> response
            ) {

                if(response.isSuccessful()){

                    Toast.makeText(
                            CartActivity.this,
                            "Đặt hàng thành công",
                            Toast.LENGTH_SHORT
                    ).show();

                    CartManager.clearCart();

                    finish();

                } else {

                    Toast.makeText(
                            CartActivity.this,
                            "Checkout thất bại",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<ResponseBody> call,
                    Throwable t
            ) {

                Toast.makeText(
                        CartActivity.this,
                        t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}