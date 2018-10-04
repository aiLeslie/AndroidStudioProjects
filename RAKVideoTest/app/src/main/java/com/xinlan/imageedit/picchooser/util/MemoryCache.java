/*
 * from http://www.androidhive.info/2012/07/android-loading-image-from-url-http/
 */
package com.xinlan.imageedit.picchooser.util;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;

class MemoryCache {
    private static final HashMap<String, SoftReference<Bitmap>> cache =
            new HashMap<String, SoftReference<Bitmap>>();

    Bitmap get(final String id) {
        if (!cache.containsKey(id)) return null;
        SoftReference<Bitmap> ref = cache.get(id);
        return ref.get();
    }

    void put(final String id, final Bitmap bitmap) {
        cache.put(id, new SoftReference<Bitmap>(bitmap));
    }
}