package com.example.administrator.fileexplorer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/2/2.
 */

public class FileAdapter extends ArrayAdapter<File> {
    private int resourceId;

    public FileAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        resourceId = resource;
    }

    public FileAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        resourceId = resource;

    }

    public FileAdapter(@NonNull Context context, int resource, @NonNull File[] objects) {
        super(context, resource, objects);
        resourceId = resource;

    }

    public FileAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull File[] objects) {
        super(context, resource, textViewResourceId, objects);
        resourceId = resource;

    }

    public FileAdapter(@NonNull Context context, int resource, @NonNull List<File> objects) {
        super(context, resource, objects);
        resourceId = resource;

    }

    public FileAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<File> objects) {
        super(context, resource, textViewResourceId, objects);
        resourceId = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null, true);
        File file = getItem(position);
        TextView textView = (TextView) view.findViewById(R.id.textViewName);
        textView.setText(file.getName());
        textView = (TextView) view.findViewById(R.id.textViewInfo);
        if (file.list() != null)
            textView.setText(file.list().length + "项" + " | " + new Date(file.lastModified()).toLocaleString());

        else {
            int pointIndex = (file.length() / Math.pow(1024, 2) + "").indexOf(".");
            if (pointIndex != -1) {
                textView.setText((file.length() / Math.pow(1024, 2) + "").substring(0, pointIndex + 2) +
                        "MB" + " | " + new Date(file.lastModified()).toLocaleString());
            } else {
                textView.setText((file.length() / Math.pow(1024, 2) + "") +
                        "MB" + " | " + new Date(file.lastModified()).toLocaleString());
            }

        }
        //String.format("%.2lf",(double)file.getFreeSpace() / Math.pow(1024,2))
        return view;

    }
}
