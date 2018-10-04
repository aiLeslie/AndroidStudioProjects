package com.example.administrator.flagapitest.util.data;

public class Data<V> {
    private String name;
    private V value;
    private int resource;

    public Data(int resource,String name, V value) {
        this.name = name;
        this.value = value;
        this.resource = resource;
    }

    public int getResource() {
        return resource;
    }

    public String getName() {
        return name;
    }

    public V getValue() {
        return value;
    }
}
