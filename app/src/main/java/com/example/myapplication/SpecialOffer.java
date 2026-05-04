package com.example.myapplication;

public class SpecialOffer {

    private String name;
    private String image;
    private String originalPrice;
    private String appPrice;

    public SpecialOffer(String name, String image, String originalPrice, String appPrice) {
        this.name = name;
        this.image = image;
        this.originalPrice = originalPrice;
        this.appPrice = appPrice;
    }

    public String getName() { return name; }
    public String getImage() { return image; }
    public String getOriginalPrice() { return originalPrice; }
    public String getAppPrice() { return appPrice; }
}