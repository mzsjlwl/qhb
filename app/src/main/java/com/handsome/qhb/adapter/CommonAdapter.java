package com.handsome.qhb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.android.volley.RequestQueue;
import com.handsome.qhb.bean.Product;
import com.handsome.qhb.utils.ViewHolder;

import java.util.List;

/**
 * Created by zhang on 2016/3/3.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected int mlayoutId;
    protected RequestQueue mQueue;

    public CommonAdapter(Context context, List<T> datas, int layoutId,RequestQueue mQueue)
    {
        this.mContext = context;
        this.mDatas = datas;
        this.mlayoutId = layoutId;
        this.mQueue = mQueue;
        mInflater = LayoutInflater.from(context);
    }

    /**
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount()
    {
        return mDatas.size();
    }

    /**
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public T getItem(int position)
    {
        return mDatas.get(position);
    }

    /**
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
//	@Override
//	public abstract View getView(int position, View convertView, ViewGroup parent);

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = ViewHolder.get(mContext, convertView, parent, mlayoutId, position,mQueue);

        convert(position,holder, getItem(position));

        return holder.getConvertView();
    }







    public abstract void convert(int position,ViewHolder holder, T t);
}
