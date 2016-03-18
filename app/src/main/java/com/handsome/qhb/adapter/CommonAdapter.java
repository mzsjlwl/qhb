package com.handsome.qhb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.handsome.qhb.bean.Product;
import com.handsome.qhb.utils.ViewHolder;

import java.util.List;

import tab.com.handsome.handsome.R;

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


    @Override
    public int getCount()
    {
        return mDatas.size();
    }


    @Override
    public T getItem(int position)
    {
        return mDatas.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = ViewHolder.get(mContext, convertView, parent, mlayoutId, position,mQueue);

        convert(position,holder, (ListView) parent,getItem(position));

        return holder.getConvertView();
    }


    public abstract void convert(int position,ViewHolder holder,ListView listView,T t);
}
