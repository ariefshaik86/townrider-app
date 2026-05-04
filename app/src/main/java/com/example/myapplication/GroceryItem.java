package com.example.myapplication;

public class GroceryItem {

    private String name;
    private int price;
    private String image;

    public GroceryItem(String name, int price, String image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
}