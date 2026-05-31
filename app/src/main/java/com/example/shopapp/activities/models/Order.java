package com.example.shopapp.activities.models;

import com.google.gson.annotations.SerializedName; // Đảm bảo đã import thư viện này
import java.util.List;

public class Order {

    @SerializedName("id")
    private String id;

    @SerializedName("user_id") // Khớp với tag json:"user_id" ở Go
    private String user_id;

    @SerializedName("items")
    private List<Product> items;

    @SerializedName("total")
    private double total;

    public Order() {
    }

    // Các Getter và Setter giữ nguyên...
    public String getId() { return id; }
    public String getUser_id() { return user_id; }
    public List<Product> getItems() { return items; }
    public double getTotal() { return total; }

    public void setId(String id) { this.id = id; }
    public void setUser_id(String user_id) { this.user_id = user_id; }
    public void setItems(List<Product> items) { this.items = items; }
    public void setTotal(double total) { this.total = total; }
}