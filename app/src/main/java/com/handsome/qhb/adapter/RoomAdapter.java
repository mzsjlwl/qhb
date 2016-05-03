package com.handsome.qhb.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.ChatMessage;
import com.handsome.qhb.bean.Product;
import com.handsome.qhb.bean.Room;
import com.handsome.qhb.bean.User;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.db.MessageDAO;
import com.handsome.qhb.db.RoomDAO;
import com.handsome.qhb.listener.MyListener;
import com.handsome.qhb.ui.activity.ChatActivity;
import com.handsome.qhb.ui.activity.MainActivity;
import com.handsome.qhb.ui.activity.OrderDetailActivity;
import com.handsome.qhb.utils.HttpUtils;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.TimeUtils;
import com.handsome.qhb.utils.UserInfo;
import com.handsome.qhb.utils.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tab.com.handsome.handsome.R;


/**
 * Created by zhang on 2016/2/22.
 */
public class RoomAdapter extends CommonAdapter<Room> implements MyListener{

    public ChatMessage chatMessage;
    public List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
    public RoomAdapter(Context context, List<Room> datas, int layoutId, RequestQueue mQueue){
        super(context,datas,layoutId,mQueue);
    }
    @Override
    public void convert(int position,ViewHolder holder,ListView listView ,Room room) {
        holder.setText(R.id.id_tv_roomName, room.getRoomName());
        holder.getView(R.id.room_list_items).setOnClickListener(new RoomItemOnclick(position));
        if(room.getLastMessage()!=null){
            holder.setText(R.id.id_tv_time, TimeUtils.getInterval(room.getLastMessage().getDate()));
            if(room.getChatMessageList()==null||room.getChatMessageList().size()==0){
                holder.setText(R.id.id_tv_num,"");
                holder.setText(R.id.id_tv_content,
                        room.getLastMessage().getNackname()+" : "+
                room.getLastMessage().getContent());
            }else {
                chatMessage = room.getChatMessageList().get(room.getChatMessageList().size()-1);
                holder.setText(R.id.id_tv_num,"[ "+room.getChatMessageList().size()+"条 ]");
                holder.setText(R.id.id_tv_content,chatMessage.getNackname()+" : "+chatMessage.getContent());
                holder.setText(R.id.id_tv_time, TimeUtils.getInterval(chatMessage.getDate()));
            }
        }

        holder.setImage(R.id.iv_roomPhoto,room.getRoomPhoto());
    }

    @Override
    public void dataController(String response, int tag) {
        LogUtils.e("enterroom.response===",response);
    }

    class RoomItemOnclick implements View.OnClickListener{
        private int position;
        public RoomItemOnclick(int position){
            this.position = position;
        }
        @Override
        public void onClick(View view) {
            LogUtils.e("room=====","onclick");
            if (mDatas.get(position).getChatMessageList() != null) {
                mDatas.get(position).getChatMessageList().clear();
            }
            final ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("请求中");
            progressDialog.setCancelable(true);
            progressDialog.show();
            //数据库更新
            RoomDAO.updateMessage(MyApplication.getSQLiteDatabase(), "", mDatas.get(position).getRid());
            Intent i = new Intent(mContext, ChatActivity.class);
            Bundle b = new Bundle();
            b.putSerializable("room", mDatas.get(position));
            i.putExtras(b);
            mContext.startActivity(i);
            Map<String, String> map = new HashMap<String, String>();
            map.put("uid", String.valueOf(UserInfo.getInstance().getUid()));
            map.put("token", UserInfo.getInstance().getToken());
            map.put("rid", String.valueOf(mDatas.get(position).getRid()));
            HttpUtils.request((Activity) mContext, Config.ENTERROOM_URL, RoomAdapter.this, map, Config.ENTERROOM_TAG);
            progressDialog.dismiss();
        }
    }

}
