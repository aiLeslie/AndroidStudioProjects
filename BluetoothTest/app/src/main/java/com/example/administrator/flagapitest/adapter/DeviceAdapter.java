package com.example.administrator.flagapitest.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DeviceAdapter extends BaseAdapter {
    private List<BluetoothDevice> data;
    private Context context;

    public DeviceAdapter(Context context,List<BluetoothDevice> data) {
        this.data = data;
        this.context = context.getApplicationContext();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = view;
        if (itemView == null){
            itemView = LayoutInflater.from(context).inflate(android.R.layout.simple_expandable_list_item_2,viewGroup,false);
        }
        TextView line1 = (TextView) itemView.findViewById(android.R.id.text1);
        TextView line2 = (TextView) itemView.findViewById(android.R.id.text2);

        //获取对应的蓝牙设备
        BluetoothDevice device = (BluetoothDevice) getItem(i);

        //显示名称
        line1.setText(device.getName());
        //显示地址
        line2.setText(device.getAddress());
        return itemView;
    }
    public void refresh(){
        notifyDataSetChanged();
    }

}
