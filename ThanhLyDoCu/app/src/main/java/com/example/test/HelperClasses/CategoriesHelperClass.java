package com.example.test.HelperClasses;

import android.graphics.drawable.Drawable;

public class CategoriesHelperClass {

    int image;
    String title;
    Drawable gradient;

    public CategoriesHelperClass(int image, String title, Drawable gradient) {
        this.image = image;
        this.title = title;
        this.gradient = gradient;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public Drawable getGradient() {
        return gradient;
    }
}
