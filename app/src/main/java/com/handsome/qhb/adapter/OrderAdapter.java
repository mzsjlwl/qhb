package com.handsome.qhb.adapter;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.handsome.qhb.bean.Order;
import com.handsome.qhb.bean.Product;
import com.handsome.qhb.utils.ViewHolder;

import java.util.List;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/14.
 */
public class OrderAdapter extends CommonAdapter<Order> {
    public OrderAdapter(Context context, List<Order> datas, int layoutId, RequestQueue mQueue){
        super(context,datas,layoutId,mQueue);
    }
    @Override
    public void convert(int position,ViewHolder holder, Order order) {
        holder.setImage(R.id.iv_order,order.getProductsList().get(0).getProduct().getPicture());
        holder.setText(R.id.tv_totalMoney, String.valueOf(order.getTotalMoney()));
        holder.setText(R.id.tv_orderTime,String.valueOf(order.getTime()));
        if(order.getState()==0){
            holder.setText(R.id.tv_orderStatus,"待收货");
        }else{
            holder.setText(R.id.tv_orderStatus,"已完成");
        }
    }
}
