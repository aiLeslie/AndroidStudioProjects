package com.example.administrator.flagapitest.util.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.flagapitest.R;

import java.util.List;

public class DataAdapter extends ArrayAdapter<Data> {
    private Context context;

    public DataAdapter(@NonNull Context context, int resource, @NonNull List<Data> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    public DataAdapter(@NonNull Context context, @NonNull List<Data> objects) {
        super(context, R.layout.data_item, objects);
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.data_item,parent,false);
        }
        Data item = getItem(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageResource(item.getResource());
        TextView textView = (TextView) view.findViewById(R.id.dataTextView);
        textView.setText(item.getName() + ":\t" + item.getValue());

        return view;
    }
}
