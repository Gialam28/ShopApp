package com.example.shopapp.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopapp.R;
import com.example.shopapp.activities.HomeActivity;
import com.example.shopapp.activities.UpdateProductActivity;
import com.example.shopapp.activities.models.Product;
import com.example.shopapp.activities.network.ApiService;
import com.example.shopapp.activities.network.RetrofitClient;
import com.example.shopapp.activities.utils.CartManager;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    List<Product> productList;
    String token;

    public ProductAdapter(Context context, List<Product> productList, String token) {
        this.context = context;
        this.productList = productList;
        this.token = token;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.txtTitle.setText(product.getTitle());
        holder.txtPrice.setText("$" + product.getPrice());
        holder.txtCategory.setText("Category: " + product.getCategory());
        holder.txtDescription.setText(product.getDescription());

        if (product.getImage() != null && !product.getImage().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(product.getImage())
                    .into(holder.imgProduct);
        }

        holder.btnAddCart.setOnClickListener(v -> {
            CartManager.addToCart(product);
            Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
        });

        // NÚT EDIT ĐÃ SỬA: Gọi qua Launcher của HomeActivity
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateProductActivity.class);
            intent.putExtra("product", product);

            if (context instanceof HomeActivity) {
                ((HomeActivity) context).addProductLauncher.launch(intent);
            } else {
                context.startActivity(intent);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            ApiService apiService = RetrofitClient.getRetrofit().create(ApiService.class);
            apiService.deleteProduct(token, product.getId()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        int currentPosition = holder.getAdapterPosition();
                        if (currentPosition != RecyclerView.NO_POSITION) {
                            productList.remove(currentPosition);
                            notifyItemRemoved(currentPosition);
                            Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtTitle, txtPrice, txtCategory, txtDescription;
        Button btnAddCart, btnEdit, btnDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            btnAddCart = itemView.findViewById(R.id.btnAddCart);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}