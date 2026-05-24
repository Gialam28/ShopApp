package com.example.shopapp.activities.models;

public class Product {

    private String id;
    private String title;
    private double price;
    private String description;
    private String image;
    private String category;

    public Product() {
    }

    public String getId() {
        return id;
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
}