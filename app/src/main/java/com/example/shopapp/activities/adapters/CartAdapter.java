package com.example.shopapp.activities.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopapp.R;
import com.example.shopapp.activities.models.Product;

import java.util.List;

public class CartAdapter
        extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    List<Product> cartList;

    public CartAdapter(List<Product> cartList) {

        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_cart,
                                parent,
                                false
                        );

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull CartViewHolder holder,
            int position
    ) {

        Product product =
                cartList.get(position);

        holder.txtTitle.setText(
                product.getTitle()
        );

        holder.txtPrice.setText(
                "$" + product.getPrice()
        );
    }

    @Override
    public int getItemCount() {

        return cartList.size();
    }

    static class CartViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtTitle;

        TextView txtPrice;

        public CartViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            txtTitle =
                    itemView.findViewById(
                            R.id.txtTitle
                    );

            txtPrice =
                    itemView.findViewById(
                            R.id.txtPrice
                    );
        }
    }
}