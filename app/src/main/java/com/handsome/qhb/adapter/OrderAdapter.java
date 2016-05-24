package com.handsome.qhb.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.Order;
import com.handsome.qhb.bean.Product;
import com.handsome.qhb.bean.Products;
import com.handsome.qhb.ui.activity.OrderDetailActivity;
import com.handsome.qhb.utils.ViewHolder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/14.
 */
public class OrderAdapter extends CommonAdapter<Order> {

    DecimalFormat df = new DecimalFormat("#0.00");
    public OrderAdapter(Context context, List<Order> datas, int layoutId, RequestQueue mQueue){
        super(context,datas,layoutId,mQueue);
    }
    @Override
    public void convert(int position,ViewHolder holder,ListView listView, Order order) {
        List<Products> productsList = new ArrayList<Products>();
        productsList = MyApplication.getGson().fromJson(order.getProducts(), new TypeToken<List<Products>>() {
        }.getType());
        float totalMoney = 0;
        if(productsList!=null){
            for(Products p:productsList){
                totalMoney=totalMoney+p.getNum()*p.getProduct().getPrice();
            }
            holder.setImage(R.id.iv_order,productsList.get(0).getProduct().getPicture());
            holder.setText(R.id.tv_totalMoney,df.format(totalMoney));
            holder.setText(R.id.tv_orderTime,String.valueOf(order.getTime()));
            if(order.getState()==0){
                holder.setText(R.id.tv_orderStatus,"待收货");
            }else{
                holder.setText(R.id.tv_orderStatus,"已完成");
            }
            holder.getView(R.id.rl_items).setOnClickListener(new OrderItemOnclick(position,totalMoney));
        }

    }

    class OrderItemOnclick implements View.OnClickListener{
        private int position;
        private float totalMoney;
        public OrderItemOnclick(int position,float totalMoney){
            this.position = position;
            this.totalMoney = totalMoney;
        }
        @Override
        public void onClick(View view) {
            Intent i = new Intent(mContext, OrderDetailActivity.class);
            Bundle b = new Bundle();
            b.putSerializable("order",mDatas.get(position));
            b.putString("totalMoney", df.format(totalMoney));
            i.putExtras(b);
            mContext.startActivity(i);
        }
    }
}
