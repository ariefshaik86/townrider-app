package com.example.myapplication;

public class BestReviewed {

    private String name;
    private String image;
    private double price;
    private double rating;

    public BestReviewed(String name, String image, double price, double rating) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.rating = rating;
    }

    public String getName() { return name; }
    public String getImage() { return image; }
    public double getPrice() { return price; }
    public double getRating() { return rating; }
}