package com.example.administrator.fruitlistviewdemo;

/**
 * Created by Administrator on 2017/12/11.
 */

public class Fruit {
    private String name;
    private int imageId;
    private String comment = "";

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Fruit(String name, int imageId, String comment) {
        this.name = name;

        this.imageId = imageId;
        this.comment = comment;
    }

    public Fruit(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
