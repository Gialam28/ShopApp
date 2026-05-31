package com.example.shopapp.activities.models;

import com.google.gson.annotations.SerializedName; // Nhớ import thư viện này
import java.io.Serializable;

public class Product implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("owner_id")
    private String owner_id;

    @SerializedName("title")
    private String title;

    @SerializedName("price")
    private double price;

    @SerializedName("description")
    private String description;

    @SerializedName("image")
    private String image;

    @SerializedName("category") // Đảm bảo khớp với key trong JSON của server
    private String category;

    public Product() {
    }

    public Product(
            String id,
            String owner_id,
            String title,
            double price,
            String description,
            String image,
            String category
    ) {

        this.id = id;
        this.owner_id = owner_id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.image = image;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getCategory() {
        return category;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}