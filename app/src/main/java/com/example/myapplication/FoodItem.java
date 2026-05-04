package com.example.myapplication;

public class FoodItem {

    private String name;
    private int price;
    private String image;   // 🔥 change from int to String
    private int quantity;

    public FoodItem(String name, int price, String image) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = 1;
    }

    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getImage() { return image; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void increaseQuantity() { quantity++; }
    public void decreaseQuantity() {
        if (quantity > 1) quantity--;
    }
}