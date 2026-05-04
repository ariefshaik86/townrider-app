package com.example.myapplication;

public class BestRatedGrocery {

    private String name;
    private String price;
    private String image;
    private String rating;

    public BestRatedGrocery(String name,
                            String price,
                            String image,
                            String rating) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.rating = rating;
    }

    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getImage() { return image; }
    public String getRating() { return rating; }
}