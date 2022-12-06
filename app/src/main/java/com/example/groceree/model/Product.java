package com.example.groceree.model;

public class Product {
    private String name, imageUrl, price, quantity;

    public Product() {
    }

    public Product(String name, String imageUrl, String price) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public Product(String name, String imageUrl, String price, String quantity) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
