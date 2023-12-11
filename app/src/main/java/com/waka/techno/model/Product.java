package com.waka.techno.model;

import java.util.ArrayList;

public class Product {
    private int id;
    private String name;
    private String category;
    private String brand;
    private String model;
    private String Description;
    private double price;
    private int qty;
    private ArrayList<String> productImage;

    public Product() {
    }

    public Product(String name, String category, String brand, String model, String description, double price, int qty, ArrayList<String> productImage) {
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.model = model;
        Description = description;
        this.price = price;
        this.qty = qty;
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
