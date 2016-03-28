package com.handsome.qhb.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.ChatMessage;
import com.handsome.qhb.bean.RandomBonus;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.ui.activity.BonusActivity;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/24.
 */
public class MsgAdapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private List<ChatMessage> mDatas;
    private List<RandomBonus> bonusList = new ArrayList<RandomBonus>();
    private Context context;
    private Gson gson = new Gson();

    public MsgAdapter(Context context, List<ChatMessage> datas)
    {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position)
    {
        ChatMessage msg = mDatas.get(position);
        return msg.getUid()==UserInfo.getInstance().getUid() ? 1 : 0;
    }

    @Override
    public int getViewTypeCount()
    {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessage = mDatas.get(position);

        LogUtils.e("getView", "=====>");
        ViewHolder viewHolder = null;

        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            if (chatMessage.getUid()!= UserInfo.getInstance().getUid())
            {
                convertView = mInflater.inflate(R.layout.chat_from_msg,
                        parent, false);
                viewHolder.createDate = (TextView) convertView
                        .findViewById(R.id.chat_from_createDate);
                viewHolder.content = (TextView) convertView
                        .findViewById(R.id.chat_from_content);
                //viewHolder.content.setBackgroundResource(R.drawable.balloon_l2);
                viewHolder.nickname = (TextView) convertView
                        .findViewById(R.id.chat_from_name);
                viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.id_progressBar);
                convertView.setTag(viewHolder);
            }else if(chatMessage.getUid()==UserInfo.getInstance().getUid())
            {
                convertView = mInflater.inflate(R.layout.chat_send_msg,
                        null);
                viewHolder.createDate = (TextView) convertView
                        .findViewById(R.id.chat_send_createDate);
                viewHolder.content = (TextView) convertView
                        .findViewById(R.id.chat_send_content);
                //viewHolder.content.setBackgroundResource(R.drawable.balloon_r2);

                viewHolder.nickname = (TextView) convertView
                        .findViewById(R.id.chat_send_name);
                viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.id_progressBar);
                convertView.setTag(viewHolder);
            }

        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.createDate.setText(chatMessage.getDate());
        if(chatMessage.getType()== Config.TYPE_RANDOMBONUS){
            viewHolder.content.setBackgroundResource(R.mipmap.cds1);
            viewHolder.content.setPadding(0,0,0,0);
            viewHolder.content.setText("");
            viewHolder.content.setOnClickListener(new RandomBonusOnclickListener(position));
            if(chatMessage.getUid()==0){
                viewHolder.nickname.setText("系统");
            }else{
                viewHolder.nickname.setText(chatMessage.getNackname());
            }

        }else if(chatMessage.getType()==Config.TYPE_CDSBONUS){

        }else {
            if(chatMessage.getUid()==UserInfo.getInstance().getUid()){
                //将之前的红包背景设置回来
                viewHolder.content.setBackgroundResource(R.drawable.balloon_r2);
                viewHolder.content.setPadding(15,5,20,5);
            }else{
                //将之前的红包背景设置回来
                viewHolder.content.setBackgroundResource(R.drawable.balloon_l2);
                viewHolder.content.setPadding(20, 5, 15, 5);
            }
            viewHolder.content.setText(chatMessage.getContent());
            viewHolder.nickname.setText(chatMessage.getNackname());
        }
        if(chatMessage.getStatus()==1) {
            if(viewHolder.progressBar!=null){
                viewHolder.progressBar.setVisibility(View.INVISIBLE);
            }
            LogUtils.e("chatMessage", "1");

        }

        return convertView;
    }

    private class ViewHolder
    {
        public TextView createDate;
        public TextView nickname;
        public TextView content;
        public ProgressBar progressBar;
    }

    class RandomBonusOnclickListener implements View.OnClickListener{

        private int position;


        public RandomBonusOnclickListener(int position){
            this.position = position;
        }
        @Override
        public void onClick(View view) {
            //如果已经拆过该红包
            if(mDatas.get(position).getStatus()==Config.TYPE_RANDOMBONUS_OPENED){
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("红包详情获取中");
                progressDialog.setCancelable(true);
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+"Room/openedBonus",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    progressDialog.dismiss();
                                    LogUtils.e("response", response);
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    if(status == "0"){
                                        Toast.makeText(context, jsonObject.getString("info"), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    String data = jsonObject.getString("data");
                                    JSONObject jsonObject1 = new JSONObject(data);
                                    bonusList = gson.fromJson(jsonObject1.getString("randombonus"),new TypeToken<List<RandomBonus>>(){}.getType());
                                    Intent i = new Intent(context, BonusActivity.class);
                                    Bundle b = new Bundle();
                                    b.putSerializable("ChatMessage", mDatas.get(position));
                                    b.putSerializable("bonusList", (Serializable) bonusList);
                                    i.putExtras(b);
                                    context.startActivity(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("id",String.valueOf(mDatas.get(position).getId()));
                        return map;
                    }
                };
                MyApplication.getmQueue().add(stringRequest);
            }
            //未拆过该红包
            else{
                mDatas.get(position).setStatus(Config.TYPE_RANDOMBONUS_OPENED);
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("抢包中");
                progressDialog.setCancelable(true);
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+"Room/getRandomBonus",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    progressDialog.dismiss();
                                    LogUtils.e("response", response);
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    if(status == "0"){
                                        Toast.makeText(context, jsonObject.getString("info"), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    String data = jsonObject.getString("data");
                                    JSONObject jsonObject1 = new JSONObject(data);
                                    bonusList = gson.fromJson(jsonObject1.getString("randombonus"),new TypeToken<List<RandomBonus>>(){}.getType());
                                    Intent i = new Intent(context, BonusActivity.class);
                                    Bundle b = new Bundle();
                                    b.putSerializable("ChatMessage", mDatas.get(position));
                                    b.putSerializable("bonusList", (Serializable) bonusList);
                                    i.putExtras(b);
                                    context.startActivity(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("uid", String.valueOf(UserInfo.getInstance().getUid()));
                        map.put("id",String.valueOf(mDatas.get(position).getId()));
                        return map;
                    }
                };
                MyApplication.getmQueue().add(stringRequest);

            }

        }
    }


    }

