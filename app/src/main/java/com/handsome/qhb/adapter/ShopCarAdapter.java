package com.handsome.qhb.adapter;

import android.content.Context;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.handsome.qhb.bean.Product;
import com.handsome.qhb.utils.ViewHolder;

import java.util.List;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/15.
 */
public class ShopCarAdapter extends CommonAdapter<Product> {

    public ShopCarAdapter(Context context, List<Product> datas, int layoutId, RequestQueue mQueue){
        super(context,datas,layoutId,mQueue);
    }
    @Override
    public void convert(int position, ViewHolder holder,ListView listView, Product product) {
        holder.setText(R.id.tv_pname,product.getPname());
        holder.setText(R.id.tv_num,String.valueOf(product.getNum()));
        holder.setText(R.id.tv_price,String.valueOf(product.getNum()*product.getPrice()));
    }
}
