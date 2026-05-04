package com.example.myapplication;

public class GroceryOffer {

    private String name;
    private String originalPrice;
    private String offerPrice;
    private String image;

    public GroceryOffer(String name,
                        String originalPrice,
                        String offerPrice,
                        String image) {

        this.name = name;
        this.originalPrice = originalPrice;
        this.offerPrice = offerPrice;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public String getImage() {
        return image;
    }
}