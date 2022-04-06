package com.example.test.HelperClasses;

public class AuctionHelperClass {
    int image;
    String title, time, price;


    public AuctionHelperClass(int image, String title, String time, String price) {
        this.image = image;
        this.title = title;
        this.time = time;
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getPrice() {
        return price;
    }
}
