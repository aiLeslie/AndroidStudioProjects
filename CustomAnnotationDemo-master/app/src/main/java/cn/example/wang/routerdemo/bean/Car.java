package cn.example.wang.routerdemo.bean;

/**
 * Created by WANG on 17/11/21.
 */

public class Car {
    private String name;
    private int size;

    public Car(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Car [name=" + name + ", size=" + size + "]";
    }
}
