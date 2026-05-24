package com.example.shopapp.activities.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopapp.R;
import com.example.shopapp.activities.models.Product;

import java.util.List;

public class ProductAdapter
        extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_product,
                                parent,
                                false
                        );

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ProductViewHolder holder,
            int position
    ) {

        Product product =
                productList.get(position);

        holder.txtTitle.setText(
                product.getTitle()
        );

        holder.txtPrice.setText(
                "$" + product.getPrice()
        );

        Glide.with(holder.itemView.getContext())
                .load(product.getImage())
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgProduct;
        TextView txtTitle, txtPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct =
                    itemView.findViewById(R.id.imgProduct);

            txtTitle =
                    itemView.findViewById(R.id.txtTitle);

            txtPrice =
                    itemView.findViewById(R.id.txtPrice);
        }
    }
}