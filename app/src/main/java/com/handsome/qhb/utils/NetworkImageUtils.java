package com.handsome.qhb.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import tab.com.handsome.handsome.R;

/**
 * Created by Administrator on 2016/4/16.
 */
public class NetworkImageUtils {
    public static NetworkImageView imageLoader(RequestQueue mQueue,String url,NetworkImageView imageView){

        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());

//        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,
//                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
//        imageLoader.get(url, listener);

        imageView.setDefaultImageResId(R.mipmap.ic_launcher);
        imageView.setErrorImageResId(R.mipmap.ic_launcher);
        imageView.setImageUrl(url,
                imageLoader);
        return imageView;
    }

    public static class BitmapCache implements ImageLoader.ImageCache {

        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            int maxSize = 100 * 1024 * 1024;
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }

    }
}
