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

public class UpdateProductActivity extends AppCompatActivity {

    EditText edtTitle, edtPrice, edtDescription, edtImage, edtCategory;
    Button btnUpdate;
    Product product;
    String firebaseToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        edtTitle = findViewById(R.id.edtTitle);
        edtPrice = findViewById(R.id.edtPrice);
        edtDescription = findViewById(R.id.edtDescription);
        edtImage = findViewById(R.id.edtImage);
        edtCategory = findViewById(R.id.edtCategory);
        btnUpdate = findViewById(R.id.btnUpdate);

        product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            edtTitle.setText(product.getTitle());
            edtPrice.setText(String.valueOf(product.getPrice()));
            edtDescription.setText(product.getDescription());
            edtImage.setText(product.getImage());
            edtCategory.setText(product.getCategory());
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.getIdToken(true).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    firebaseToken = task.getResult().getToken();
                }
            });
        }

        btnUpdate.setOnClickListener(v -> updateProduct());
    }

    void updateProduct() {
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

        // Sử dụng setter đúng như logic cũ của bạn
        Product updatedProduct = new Product();
        updatedProduct.setTitle(title);
        updatedProduct.setPrice(price);
        updatedProduct.setDescription(description);
        updatedProduct.setImage(image);
        updatedProduct.setCategory(category);

        ApiService apiService = RetrofitClient.getRetrofit().create(ApiService.class);

        apiService.updateProduct("Bearer " + firebaseToken, product.getId(), updatedProduct)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(UpdateProductActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();

                            // Báo thành công về HomeActivity
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(UpdateProductActivity.this, "Update failed: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(UpdateProductActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}