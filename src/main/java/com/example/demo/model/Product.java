package com.example.demo.model;

public class Product {
    private String id;
    private String name;
    private String category;
    private double price;
    private String desc;
    private String image;
    private String badge;
    private int sales;

    public Product() {} // Required for Firebase

    // Add Getter and Setters for ALL variables above!
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public String getBadge() { return badge; }
    public void setBadge(String badge) { this.badge = badge; }
    public int getSales() { return sales; }
    public void setSales(int sales) { this.sales = sales; }
}