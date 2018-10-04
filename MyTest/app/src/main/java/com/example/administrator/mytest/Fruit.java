package com.example.administrator.mytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Fruit {
    private String name;
    private int imageId;
    private String comment;

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;

    }

    /*public Fruit(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }*/

    public Fruit(String name, int imageId, String comment) {
        this.name = name;
        this.imageId = imageId;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
