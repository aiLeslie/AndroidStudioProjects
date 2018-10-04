package com.example.administrator.fruitlistviewdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/12/11.
 */

public class FruitAdapter extends ArrayAdapter<Fruit> {


    private int resourceId;

    public FruitAdapter(Context context, int textViewResourceid, List<Fruit> obj) {
        super(context, textViewResourceid, obj);
        resourceId = textViewResourceid;

    }
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Fruit fruit = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ImageView fruitimage = (ImageView) view.findViewById(R.id.imageView);
        TextView fruitName = (TextView) view.findViewById(R.id.textView);
        fruitimage.setImageResource(fruit.getImageId());
        fruitName.setText(fruit.getName());
        return view;
    }



}
