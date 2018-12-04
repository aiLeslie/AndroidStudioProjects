package com.example.administrator.bluetoothtest.adapter;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bluetoothtest.R;
import com.example.administrator.bluetoothtest.activities.checkup.view.CoordinateView;

import java.util.HashMap;
import java.util.List;

public class CoordAdapter extends RecyclerView.Adapter<CoordAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<HashMap<String, Object>> list;
    private HashMap<String, Object> map;

    public CoordAdapter(Context context, List<HashMap<String, Object>> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public CoordAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.coord_item, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        map = list.get(position);

        holder.textView.setText((String) map.get("text"));

        holder.coordView.getCoord().setY_MIN((float)map.get("Y_MIN")).setY_MAX((float)map.get("Y_MAX")).setY_UNIT(20).setY_DESC((String) map.get("Y_DESC")).updeate();
        holder.coordView.getCoord().setX_MIN((float)map.get("X_MIN")).setX_MAX((float)map.get("X_MAX")).setX_UNIT((float)map.get("X_UNIT")).setX_DESC((String) map.get("X_DESC")).updeate();
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
        public CoordinateView coordView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.zol);
            coordView = (CoordinateView) itemView.findViewById(R.id.coordView);
        }
    }
}
