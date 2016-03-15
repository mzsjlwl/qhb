package com.handsome.qhb.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.handsome.qhb.bean.Product;
import com.handsome.qhb.listener.OnRefreshListener;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.ViewHolder;

import java.util.List;

import tab.com.handsome.handsome.R;


/**
 * Created by zhang on 2016/2/22.
 */
 public class ProductAdapter extends CommonAdapter<Product> {

    public ProductAdapter(Context context, List<Product> datas, int layoutId, RequestQueue mQueue){
        super(context,datas,layoutId,mQueue);
    }
    @Override
    public void convert(final int position,final ViewHolder holder, Product product) {
        holder.setText(R.id.tx_pname, product.getPname());
        holder.setText(R.id.tx_price, String.valueOf(product.getPrice()) + "￥");
        holder.setText(R.id.tv_num,String.valueOf(product.getNum()));
        holder.setImage(R.id.iv_product, product.getPicture());
        holder.getView(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNum(position);
            }
        });
        holder.getView(R.id.btn_sub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subNum(position);
            }
        });
    }


    public void addNum(int position){
        Product product = super.mDatas.get(position);
        int num = product.getNum();
        num++;
        this.mDatas.get(position).setNum(num);
        notifyDataSetChanged();
    }

    public void subNum(int position){
        Product product = super.mDatas.get(position);
        int num = product.getNum();
        if(num<=0){
            return;
        }
        num--;
        this.mDatas.get(position).setNum(num);
        notifyDataSetChanged();
    }

}
