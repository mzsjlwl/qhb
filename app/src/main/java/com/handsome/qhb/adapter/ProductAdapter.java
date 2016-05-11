package com.handsome.qhb.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.handsome.qhb.bean.Product;
import com.handsome.qhb.ui.activity.LoginActivity;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.UserInfo;
import com.handsome.qhb.utils.ViewHolder;

import java.text.DecimalFormat;
import java.util.List;

import tab.com.handsome.handsome.R;


/**
 * Created by zhang on 2016/2/22.
 */
 public class ProductAdapter extends CommonAdapter<Product> {

    DecimalFormat df = new DecimalFormat("#0.00");
    public ProductAdapter(Context context, List<Product> datas, int layoutId, RequestQueue mQueue){
        super(context,datas,layoutId,mQueue);
    }
    @Override
    public void convert(final int position,final ViewHolder holder,final ListView listView, final Product product) {
        holder.setText(R.id.tx_pname, product.getPname());
        System.out.println(product.getPrice());
        LogUtils.e("money",String.valueOf(product.getPrice()));
        holder.setText(R.id.tx_price, df.format(product.getPrice()) + "￥");
        holder.setText(R.id.tv_num, String.valueOf(product.getNum()));
        holder.setImage(R.id.iv_product, product.getPicture());
        holder.getView(R.id.btn_add).setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserInfo.getInstance()==null){
                    Intent i = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(i);
                    ((Activity) mContext).finish();
                    return;
                }
                addNum(listView,position);
            }
        });
        holder.getView(R.id.btn_sub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subNum(listView,position);
            }
        });
    }


    public void addNum(ListView listView,int position){
        Product product = super.mDatas.get(position);
        int num = product.getNum();
        num++;
        this.mDatas.get(position).setNum(num);
        updateSingleRow(listView, position);
    }

    public void subNum(ListView listView,int position){
        Product product = super.mDatas.get(position);
        int num = product.getNum();
        if(num<=0){
            return;
        }
        num--;
        this.mDatas.get(position).setNum(num);
        updateSingleRow(listView,position);
    }

    private void updateSingleRow(ListView listView,int id){
        if(listView!=null){
            int start = listView.getFirstVisiblePosition();
            View view = listView.getChildAt(id-start+1);
            ViewHolder holder = ViewHolder.get(mContext, view, listView, mlayoutId,id,mQueue);
            holder.setText(R.id.tv_num,String.valueOf(mDatas.get(id).getNum()));
        }
    }

}
