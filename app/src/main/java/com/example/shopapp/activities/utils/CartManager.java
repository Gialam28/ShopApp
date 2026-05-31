package com.example.shopapp.activities.utils;

import com.example.shopapp.activities.models.Product;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static final List<Product> cartItems =
            new ArrayList<>();

    public static void addToCart(Product product){

        cartItems.add(product);
    }

    public static List<Product> getCartItems(){

        return cartItems;
    }

    public static void clearCart(){

        cartItems.clear();
    }

    public static double getTotalPrice(){

        double total = 0;

        for(Product product : cartItems){

            total += product.getPrice();
        }

        return total;
    }
}