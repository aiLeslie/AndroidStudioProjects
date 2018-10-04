package com.example.jean.recyclerviewtest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements View.OnClickListener{
    private Context context;
    private List<String> list;

    public MyAdapter(Context context, List<String> list) {
        if (context == null || list == null) {
            throw new IllegalArgumentException();
        }
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.textview_item,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String s = list.get(position);
        holder.textView.setText(s);
        View view = holder.textView;
        view.setOnClickListener(MyAdapter.this);

    }





    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {

        Toast.makeText(context, ((TextView) v).getText().toString(), Toast.LENGTH_SHORT).show();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }
}
