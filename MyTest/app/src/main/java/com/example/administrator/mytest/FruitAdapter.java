package com.example.administrator.mytest;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.List;

import static android.R.attr.resource;




public class FruitAdapter extends ArrayAdapter<Fruit> {
    private int id;



    public FruitAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Fruit> objects) {
        super(context, resource, objects);
        this.id = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Fruit fruit = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(id,parent,false);
        ((ImageView)(view.findViewById(R.id.imageViewPeople))).setImageResource(fruit.getImageId());
        ((TextView)(view.findViewById(R.id.textViewPepleName))).setText(fruit.getName());

        return view;
    }
}