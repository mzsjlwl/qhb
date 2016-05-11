package com.handsome.qhb.adapter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.ChatMessage;
import com.handsome.qhb.bean.DS;
import com.handsome.qhb.bean.RandomBonus;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.db.MessageDAO;
import com.handsome.qhb.listener.MyListener;
import com.handsome.qhb.ui.activity.BonusActivity;
import com.handsome.qhb.ui.activity.CDSActivity;
import com.handsome.qhb.utils.HttpUtils;
import com.handsome.qhb.utils.ImageUtils;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.NetworkImageUtils;
import com.handsome.qhb.utils.TimeUtils;
import com.handsome.qhb.utils.UserInfo;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
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
        ViewHolder viewHolder = null;


        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            if (chatMessage.getUid()!= UserInfo.getInstance().getUid())
            {
                if(chatMessage.getType()==Config.TYPE_CDSBONUS||chatMessage.getType()==Config.TYPE_RANDOMBONUS){
                    convertView = mInflater.inflate(R.layout.bonus_from_msg,
                            parent, false);
                    viewHolder.createDate = (TextView) convertView
                            .findViewById(R.id.chat_createDate);
                    viewHolder.content = (TextView) convertView
                            .findViewById(R.id.chat_from_content);
                    viewHolder.nackname = (TextView) convertView
                            .findViewById(R.id.chat_from_name);
                    viewHolder.chat_icon = (ImageView) convertView.findViewById(R.id.chat_icon);
                    viewHolder.rl_content = (RelativeLayout) convertView.findViewById(R.id.rl_content);
                }else{
                    convertView = mInflater.inflate(R.layout.chat_from_msg,
                            parent, false);
                    viewHolder.createDate = (TextView) convertView
                            .findViewById(R.id.chat_createDate);
                    viewHolder.content = (TextView) convertView
                            .findViewById(R.id.chat_from_content);
                    viewHolder.nackname = (TextView) convertView
                            .findViewById(R.id.chat_from_name);
                    viewHolder.chat_icon = (ImageView) convertView.findViewById(R.id.chat_icon);
                    viewHolder.rl_content = (RelativeLayout) convertView.findViewById(R.id.rl_content);
                }

                convertView.setTag(viewHolder);
            }else if(chatMessage.getUid()==UserInfo.getInstance().getUid())
            {
                if(chatMessage.getType()==Config.TYPE_CDSBONUS||chatMessage.getType()==Config.TYPE_RANDOMBONUS){
                    convertView = mInflater.inflate(R.layout.bonus_send_msg,
                            null);
                    viewHolder.createDate = (TextView) convertView
                            .findViewById(R.id.chat_createDate);
                    viewHolder.content = (TextView) convertView
                            .findViewById(R.id.chat_send_content);
                    viewHolder.nackname = (TextView) convertView
                            .findViewById(R.id.chat_send_name);
                    viewHolder.chat_icon = (ImageView) convertView.findViewById(R.id.chat_icon);
                    viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.id_progressBar);
                    viewHolder.rl_content = (RelativeLayout) convertView.findViewById(R.id.rl_content);
                }else{
                    convertView = mInflater.inflate(R.layout.chat_send_msg,
                            null);
                    viewHolder.createDate = (TextView) convertView
                            .findViewById(R.id.chat_createDate);
                    viewHolder.content = (TextView) convertView
                            .findViewById(R.id.chat_send_content);
                    viewHolder.nackname = (TextView) convertView
                            .findViewById(R.id.chat_send_name);
                    viewHolder.chat_icon = (ImageView) convertView.findViewById(R.id.chat_icon);
                    viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.id_progressBar);
                    viewHolder.rl_content = (RelativeLayout) convertView.findViewById(R.id.rl_content);
                }
                convertView.setTag(viewHolder);
            }

        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        if(position-1>=0){
            String data = TimeUtils.compareLast(chatMessage.getDate(), mDatas.get(position - 1).getDate());
            if(data.equals("")){
                viewHolder.createDate.setVisibility(View.INVISIBLE);
            }else{
                viewHolder.createDate.setVisibility(View.VISIBLE);
                viewHolder.createDate.setText(data);
            }
        }else{
            viewHolder.createDate.setText(TimeUtils.getInterval(chatMessage.getDate()));
        }

        Picasso.with(context).load(chatMessage.getPhoto()).into(viewHolder.chat_icon);
        //随机红包

        if(chatMessage.getType()== Config.TYPE_RANDOMBONUS){
            ViewGroup.LayoutParams p = viewHolder.rl_content.getLayoutParams();
            p.height = MyApplication.height*5/36;
            p.width = MyApplication.width*7/12;
            if(chatMessage.getUid()==UserInfo.getInstance().getUid()){
                viewHolder.rl_content.setBackgroundResource(R.mipmap.sjhb);
                viewHolder.rl_content.setLayoutParams(p);
            }else {
                viewHolder.rl_content.setBackgroundResource(R.mipmap.sjhb1);
                viewHolder.rl_content.setLayoutParams(p);
            }
            viewHolder.content.setPadding(0,0,0,0);
            viewHolder.content.setText("");
            viewHolder.rl_content.setOnClickListener(new RandomBonusOnclickListener(position));
            viewHolder.nackname.setText(chatMessage.getNackname());
            if(chatMessage.getStatus()==1) {
                if(viewHolder.progressBar!=null){
                    viewHolder.progressBar.setVisibility(View.INVISIBLE);
                }
            }

        }
        //单双红包
        else if(chatMessage.getType()==Config.TYPE_CDSBONUS){
            ViewGroup.LayoutParams p = viewHolder.rl_content.getLayoutParams();
            p.height = MyApplication.height*5/36;
            p.width = MyApplication.width*7/12;
            viewHolder.rl_content.setBackgroundResource(R.mipmap.cds1);
            viewHolder.rl_content.setLayoutParams(p);
            viewHolder.content.setPadding(0, 0, 0, 0);
            viewHolder.content.setText("");

            viewHolder.rl_content.setOnClickListener(new CDSBonusOnclickListener(position));
            viewHolder.nackname.setText(chatMessage.getNackname());
        }
        //普通消息
        else {
            if(chatMessage.getUid()==UserInfo.getInstance().getUid()){
                //将之前的红包背景设置回来
                viewHolder.content.setPadding(0,5,0,0);
                viewHolder.rl_content.setBackgroundResource(R.drawable.balloon_r2);
            }else{
                //将之前的红包背景设置回来
                viewHolder.content.setPadding(0,5,0,0);
                viewHolder.rl_content.setBackgroundResource(R.drawable.balloon_l2);
            }
            viewHolder.content.setText(chatMessage.getContent());
            viewHolder.nackname.setText(chatMessage.getNackname());
            if(chatMessage.getStatus()==1) {

                if(viewHolder.progressBar!=null){
                    viewHolder.progressBar.setVisibility(View.GONE);
                }
            }else{
                if(viewHolder.progressBar!=null){
                    viewHolder.progressBar.setVisibility(View.VISIBLE);
                }
            }
        }
        return convertView;
    }

    private class ViewHolder
    {
        public TextView createDate;
        public TextView nackname;
        public TextView content;
        public ProgressBar progressBar;
        public ImageView chat_icon;
        public RelativeLayout rl_content;

    }

    class RandomBonusOnclickListener implements View.OnClickListener{

        private int position;
        public RandomBonusOnclickListener(int position){
            this.position = position;
        }
        @Override
        public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("获取中");
                progressDialog.setCancelable(true);
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+"HB/getRandomBonus",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    LogUtils.e("randomresponse=====>",response);
                                    progressDialog.dismiss();
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    if(status.equals("0")){
                                        Toast toast = Toast.makeText(context, jsonObject.getString("info"),Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                        return;
                                    }
                                    String data = jsonObject.getString("data");
                                    JSONObject jsonObject1 = new JSONObject(data);
                                    bonusList = MyApplication.getGson().fromJson(jsonObject1.getString("randombonus"), new TypeToken<List<RandomBonus>>() {
                                    }.getType());
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
                        map.put("id", String.valueOf(mDatas.get(position).getId()));
                        map.put("token",UserInfo.getInstance().getToken());
                        return map;
                    }
                };
                MyApplication.getmQueue().add(stringRequest);
            }
        }


    class CDSBonusOnclickListener implements  View.OnClickListener{

        private int position;
        @Override
        public void onClick(View view) {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("加载中");
            progressDialog.setCancelable(true);
            progressDialog.show();

            Map<String, String> map = new HashMap<String, String>();
            map.put("dsId", String.valueOf(mDatas.get(position).getId()));
            map.put("rid", String.valueOf(mDatas.get(position).getRid()));
            map.put("uid", String.valueOf(UserInfo.getInstance().getUid()));
            map.put("token", UserInfo.getInstance().getToken());
            HttpUtils.request((Activity) context, Config.DSGETRESULT_URL,
                    new MyListener() {
                        @Override
                        public void dataController(String response, int tag) {
                            progressDialog.dismiss();
                            LogUtils.e("getResult=====>", response);
                            DS ds = new DS();
                            ds = MyApplication.getGson().fromJson(response, DS.class);
                            Intent i = new Intent(context, CDSActivity.class);
                            Bundle b = new Bundle();
                            b.putSerializable("ds", ds);
                            b.putSerializable("cdsMessage",mDatas.get(position));
                            i.putExtras(b);
                            context.startActivity(i);
                        }
                    }, map, Config.DSGETMYGUESS_TAG);

        }

        public CDSBonusOnclickListener(int position){
            this.position = position;
        }

    }

}

