package com.example.shopapp.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shopapp.R;
import com.example.shopapp.activities.adapters.ProductAdapter;
import com.example.shopapp.activities.models.Product;
import com.example.shopapp.activities.network.ApiService;
import com.example.shopapp.activities.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerProducts;
    MaterialButton btnAddProduct;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    String firebaseToken = "";

    // Đổi thành public để ProductAdapter có thể gọi
    public final ActivityResultLauncher<Intent> addProductLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadProducts();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        recyclerProducts = findViewById(R.id.recyclerProducts);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_cart) startActivity(new Intent(this, CartActivity.class));
            else if (id == R.id.nav_orders) startActivity(new Intent(this, OrderHistoryActivity.class));
            else if (id == R.id.nav_darkmode) toggleDarkMode();
            else if (id == R.id.nav_logout) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        recyclerProducts.setLayoutManager(new GridLayoutManager(this, 2));

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    firebaseToken = task.getResult().getToken();
                    loadProducts();
                }
            });
        }

        btnAddProduct.setOnClickListener(v -> addProductLauncher.launch(new Intent(this, AddProductActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!firebaseToken.isEmpty()) loadProducts();
    }

    private void toggleDarkMode() {
        int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (mode == Configuration.UI_MODE_NIGHT_YES) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    public void loadProducts(){
        ApiService apiService = RetrofitClient.getRetrofit().create(ApiService.class);
        apiService.getProducts("Bearer " + firebaseToken).enqueue(new Callback<List<Product>>() {
            @Override public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful() && response.body() != null){
                    recyclerProducts.setAdapter(new ProductAdapter(HomeActivity.this, response.body(), "Bearer " + firebaseToken));
                }
            }
            @Override public void onFailure(Call<List<Product>> call, Throwable t) {}
        });
    }
}