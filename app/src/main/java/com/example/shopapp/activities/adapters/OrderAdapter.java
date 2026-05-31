package com.example.shopapp.activities.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopapp.R;
import com.example.shopapp.activities.models.Order;
import com.example.shopapp.activities.models.Product;

import java.util.List;

public class OrderAdapter
        extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {

        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_order,
                                parent,
                                false
                        );

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull OrderViewHolder holder,
            int position
    ) {

        Order order =
                orderList.get(position);

        holder.txtOrderId.setText(
                "Order ID: " + order.getId()
        );

        holder.txtTotal.setText(
                "Total: $" + order.getTotal()
        );

        StringBuilder itemsText =
                new StringBuilder();

        if(order.getItems() != null){

            for(Product p : order.getItems()){

                itemsText.append("• ")
                        .append(p.getTitle())
                        .append("\n");
            }
        }

        holder.txtItems.setText(
                itemsText.toString()
        );
    }

    @Override
    public int getItemCount() {

        return orderList.size();
    }

    static class OrderViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtOrderId;

        TextView txtTotal;

        TextView txtItems;

        public OrderViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            txtOrderId =
                    itemView.findViewById(
                            R.id.txtOrderId
                    );

            txtTotal =
                    itemView.findViewById(
                            R.id.txtTotal
                    );

            txtItems =
                    itemView.findViewById(
                            R.id.txtItems
                    );
        }
    }
}