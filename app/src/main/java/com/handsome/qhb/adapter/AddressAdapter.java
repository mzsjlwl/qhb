package com.handsome.qhb.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.handsome.qhb.bean.Address;
import com.handsome.qhb.ui.activity.AddressManagerActivity;
import com.handsome.qhb.ui.activity.GwcActivity;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.ViewHolder;

import java.io.Serializable;
import java.util.List;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/16.
 */
public class AddressAdapter extends CommonAdapter<Address> {
    public AddressAdapter(Context context, List<Address> datas, int layoutId, RequestQueue mQueue){
        super(context,datas,layoutId,mQueue);
    }

    @Override
    public void convert(int position, ViewHolder holder,ListView listView, Address address) {
        holder.setText(R.id.tv_receName,"收货人:"+address.getReceName());
        holder.setText(R.id.tv_receAddr,"收货地址:"+address.getReceAddr());
        holder.setText(R.id.tv_recePhone,"联系方式:"+address.getRecePhone());
        holder.getView(R.id.ll_items).setOnClickListener(new ItemClickListener(position));
    }

    class ItemClickListener implements View.OnClickListener{

        private int position;
        public ItemClickListener(int position){
            this.position = position;
        }
        @Override
        public void onClick(View view) {
            Activity activity =(Activity)mContext;
            Intent i ;
//            LogUtils.e("TAG",activity.getIntent().getStringExtra("TAG"));
            if(activity.getIntent().getStringExtra("TAG")!=null&&activity.getIntent().getStringExtra("TAG").equals("GWC")){
                 i = new Intent(activity, GwcActivity.class);
            }else{
                 i = new Intent(activity, AddressManagerActivity.class);
            }
            Bundle b = new Bundle();
            b.putSerializable("address",mDatas.get(position));
            i.putExtras(b);
            activity.startActivity(i);
            activity.finish();
        }
    }
}
