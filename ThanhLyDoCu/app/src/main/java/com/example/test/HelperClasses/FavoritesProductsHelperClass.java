package com.example.test.HelperClasses;

public class FavoritesProductsHelperClass {
    String productID, sellerID, image, name, price;

    public FavoritesProductsHelperClass(){}

    public FavoritesProductsHelperClass(String productID, String sellerID, String image, String name, String price) {
        this.productID = productID;
        this.sellerID = sellerID;
        this.image = image;
        this.name = name;
        this.price = price;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
