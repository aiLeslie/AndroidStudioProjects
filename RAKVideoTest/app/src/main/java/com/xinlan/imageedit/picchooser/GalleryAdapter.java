/*
 * Copyright 2013 Thomas Hoffmann
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xinlan.imageedit.picchooser;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.xinlan.imageedit.picchooser.util.ImageLoader;
import com.example.jean.rakvideotest.R;

@SuppressLint("InflateParams") class GalleryAdapter extends BaseAdapter {

    private final Context context;
    private final List<GridItem> items;
    private final ImageLoader imageLoader;
    private final LayoutInflater mInflater;

    public GalleryAdapter(final Context context, final List<GridItem> buckets) {
        this.items = buckets;
        this.imageLoader = new ImageLoader();
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (items.get(0) instanceof BucketItem) { // show buckets
            ViewHolder holder;
            if (convertView == null) {
				convertView = mInflater.inflate(R.layout.bucketitem, null);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                holder.text = (TextView) convertView.findViewById(R.id.text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            BucketItem bi = (BucketItem) items.get(position);
            holder.text.setText(bi.images > 1 ?
                            bi.name + " - " + context.getString(R.string.images, bi.images) :
                            bi.name);
            imageLoader.DisplayImage(bi.path, holder.icon);
            return convertView;
        } else { // show images in a bucket
            ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = (ImageView) mInflater.inflate(R.layout.imageitem, null);
            } else {
                imageView = (ImageView) convertView;
            }
            imageLoader.DisplayImage(items.get(position).path, imageView);
            return imageView;
        }
    }

    private static class ViewHolder {
        private ImageView icon;
        private TextView text;
    }

}
