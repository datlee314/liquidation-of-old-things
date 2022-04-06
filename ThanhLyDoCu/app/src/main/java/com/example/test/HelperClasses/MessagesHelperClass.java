package com.example.test.HelperClasses;

public class MessagesHelperClass {
    String data, time, type, message, from, productID, sellerID;

    public MessagesHelperClass(){}

    public MessagesHelperClass(String data, String time, String type, String message, String from, String productID, String sellerID) {
        this.data = data;
        this.time = time;
        this.type = type;
        this.message = message;
        this.from = from;
        this.productID = productID;
        this.sellerID = sellerID;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
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
}
