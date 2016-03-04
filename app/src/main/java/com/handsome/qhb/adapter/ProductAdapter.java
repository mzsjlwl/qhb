package com.handsome.qhb.adapter;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.handsome.qhb.bean.Product;
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
    public void convert(ViewHolder holder, Product product) {
        holder.setText(R.id.tx_pname,product.getPname());
        holder.setText(R.id.tx_price, String.valueOf(product.getPrice()));
        holder.setImage(R.id.iv_product,product.getPicture());
    }

}
