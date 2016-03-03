package com.handsome.qhb.utils;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;

/**
 * Created by zhang on 2016/3/3.
 */
public class ViewHolder  {
    private SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;
    private RequestQueue mQueue;

    public View getConvertView()
    {
        return mConvertView;
    }

    public ViewHolder(Context context, ViewGroup parent, int layoutId, int position,RequestQueue mQueue)
    {
        this.mViews = new SparseArray<View>();
        this.mPosition = position;
        this.mQueue = mQueue;
        this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        this.mConvertView.setTag(this);
    }

    public static ViewHolder get(Context context, View convertView,
                                 ViewGroup parent, int layoutId, int position,RequestQueue mQueue)
    {
        if (null == convertView)
        {
            return new ViewHolder(context, parent, layoutId, position,mQueue);
        }
        else
        {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    /**
     * 通过viewId获取控件
     * @param viewId
     * @return
     * @return T
     * @author Yann
     * @date 2015-8-5 下午9:38:39
     */
    public <T extends View>T getView(int viewId)
    {
        View view = mViews.get(viewId);
        if (null == view)
        {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 给ID为viewId的TextView设置文字text，并返回this
     * @param viewId
     * @param text
     * @return
     * @return ViewHolder
     * @author Yann
     * @date 2015-8-5 下午11:05:17
     */
    public ViewHolder setText(int viewId, String text)
    {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }
    public ViewHolder setImage(int viewId,String url){
        ImageView iv = getView(viewId);
        ImageUtils.imageLoader(mQueue,url,iv);
        return this;

    }
}
