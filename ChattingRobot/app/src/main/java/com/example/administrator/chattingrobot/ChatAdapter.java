package com.example.administrator.chattingrobot;

import android.content.Context;
import android.graphics.Color;
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
 * Created by Administrator on 2017/12/27.
 */

public class ChatAdapter extends ArrayAdapter<Chatting> {
    private int itemId;
    public ChatAdapter(@NonNull Context context, int resource, @NonNull List<Chatting> objects) {
        super(context, resource, objects);
        itemId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(itemId, parent, false);
        if(getItem(position).getRobotTalk() != null){
            Chatting robot = getItem(position);
            TextView text =(TextView)view.findViewById(R.id.RobotText) ;
            text.setText(robot.getRobotTalk());
            text.setBackgroundColor(Color.parseColor("#bee7ff"));
            ImageView imageView = (ImageView) view.findViewById(R.id.robotImage);
            imageView.setImageResource(robot.getRobotImageId());
        }else{
            Chatting me = getItem(position);
            TextView text =(TextView)view.findViewById(R.id.myText) ;
            text.setText(me.getMyTalk());
            text.setBackgroundColor(Color.parseColor("#bee7ff"));
            ImageView imageView = (ImageView) view.findViewById(R.id.myImage);
            imageView.setImageResource(me.getMyImageId());
        }
        return view;
    }
}
