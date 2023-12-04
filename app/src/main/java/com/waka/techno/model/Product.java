package com.waka.techno.model;

import java.util.ArrayList;

public class Product {
    private int id;
    private String name;
    private String category;
    private String brand;
    private double price;
    private int qty;
    private ArrayList<String> productImage;

    public Product() {
    }

    public Product(String name, String category, double price, ArrayList<String> productImage) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.productImage = productImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public ArrayList<String> getProductImage() {
        return productImage;
    }

    public void setProductImage(ArrayList<String> productImage) {
        this.productImage = productImage;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
