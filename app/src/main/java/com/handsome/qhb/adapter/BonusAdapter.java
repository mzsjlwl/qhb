package com.handsome.qhb.adapter;

import android.content.Context;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.handsome.qhb.bean.Order;
import com.handsome.qhb.bean.RandomBonus;
import com.handsome.qhb.bean.User;
import com.handsome.qhb.utils.ViewHolder;

import java.util.List;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/28.
 */
public class BonusAdapter extends CommonAdapter<RandomBonus> {

    private Gson gson = new Gson();
    private User user;

    public BonusAdapter(Context context, List<RandomBonus> datas, int layoutId, RequestQueue mQueue){
        super(context,datas,layoutId,mQueue);
    }
    @Override
    public void convert(int position, ViewHolder holder, ListView listView, RandomBonus randomBonus) {
        user = gson.fromJson(randomBonus.getUser(),User.class);
        holder.setImage(R.id.iv_user_photo, user.getPhoto());
        if(user.getUid()==0){
            holder.setText(R.id.tv_user_nackname,"系统的红包");
        }else{
            holder.setText(R.id.tv_user_nackname,user.getNackname()+"的红包");
        }

        holder.setText(R.id.tv_time,randomBonus.getTime());
        holder.setText(R.id.tv_bonus, String.valueOf(randomBonus.getBonus()));
    }
}
