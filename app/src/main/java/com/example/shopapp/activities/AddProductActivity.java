package com.example.shopapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopapp.R;
import com.example.shopapp.activities.models.Product;
import com.example.shopapp.activities.network.ApiService;
import com.example.shopapp.activities.network.RetrofitClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {

    EditText edtTitle;
    EditText edtPrice;
    EditText edtDescription;
    EditText edtImage;
    EditText edtCategory;
    Button btnAdd;

    String firebaseToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        edtTitle = findViewById(R.id.edtTitle);
        edtPrice = findViewById(R.id.edtPrice);
        edtDescription = findViewById(R.id.edtDescription);
        edtImage = findViewById(R.id.edtImage);
        edtCategory = findViewById(R.id.edtCategory);
        btnAdd = findViewById(R.id.btnAdd);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.getIdToken(true)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            firebaseToken = task.getResult().getToken();
                        }
                    });
        }

        btnAdd.setOnClickListener(v -> {
            createProduct();
        });
    }

    void createProduct() {
        String title = edtTitle.getText().toString().trim();
        String priceText = edtPrice.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String image = edtImage.getText().toString().trim();
        String category = edtCategory.getText().toString().trim();

        if (title.isEmpty() || priceText.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceText);

        Product product = new Product();
        product.setTitle(title);
        product.setPrice(price);
        product.setDescription(description);
        product.setImage(image);
        product.setCategory(category);

        ApiService apiService = RetrofitClient.getRetrofit().create(ApiService.class);

        apiService.createProduct("Bearer " + firebaseToken, product)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(AddProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();

                            // --- CHỖ NÀY QUAN TRỌNG ĐỂ BÁO VỀ HOME ---
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(AddProductActivity.this, "Add failed: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(AddProductActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}