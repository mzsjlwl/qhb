package com.handsome.qhb.adapter;

import android.content.Context;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.handsome.qhb.bean.Product;
import com.handsome.qhb.bean.Room;
import com.handsome.qhb.utils.ViewHolder;

import java.util.List;

import tab.com.handsome.handsome.R;


/**
 * Created by zhang on 2016/2/22.
 */
 public class RoomAdapter extends CommonAdapter<Room> {

    public RoomAdapter(Context context, List<Room> datas, int layoutId, RequestQueue mQueue){
        super(context,datas,layoutId,mQueue);
    }
    @Override
    public void convert(int position,ViewHolder holder,ListView listView ,Room room) {
        holder.setText(R.id.id_tv_roomName,room.getRoomName());
        holder.setText(R.id.id_tv_time, room.getRoomEndTime());
        holder.setText(R.id.id_tv_message,room.getRoomCreateTime());
    }

}
