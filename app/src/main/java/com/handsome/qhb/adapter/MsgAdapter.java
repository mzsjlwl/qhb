package com.handsome.qhb.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.handsome.qhb.bean.DS;
import com.handsome.qhb.bean.RandomBonus;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.db.MessageDAO;
import com.handsome.qhb.ui.activity.BonusActivity;
import com.handsome.qhb.ui.activity.CDSActivity;
import com.handsome.qhb.utils.ImageUtils;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.UserInfo;

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
    public String error = "CURL ERROR: Problem (2) in the Chunked-Encoded data";

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
                convertView = mInflater.inflate(R.layout.chat_from_msg,
                        parent, false);
                viewHolder.createDate = (TextView) convertView
                        .findViewById(R.id.chat_from_createDate);
                viewHolder.content = (TextView) convertView
                        .findViewById(R.id.chat_from_content);
                viewHolder.nackname = (TextView) convertView
                        .findViewById(R.id.chat_from_name);
                viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.id_progressBar);
                viewHolder.chat_icon = (ImageView)convertView.findViewById(R.id.chat_icon);
                convertView.setTag(viewHolder);
            }else if(chatMessage.getUid()==UserInfo.getInstance().getUid())
            {
                convertView = mInflater.inflate(R.layout.chat_send_msg,
                        null);
                viewHolder.createDate = (TextView) convertView
                        .findViewById(R.id.chat_send_createDate);
                viewHolder.content = (TextView) convertView
                        .findViewById(R.id.chat_send_content);
                viewHolder.nackname = (TextView) convertView
                        .findViewById(R.id.chat_send_name);
                viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.id_progressBar);
                viewHolder.chat_icon = (ImageView)convertView.findViewById(R.id.chat_icon);
                convertView.setTag(viewHolder);
            }

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.createDate.setText(chatMessage.getDate());
        ImageUtils.imageLoader(MyApplication.getmQueue(), chatMessage.getPhoto(),viewHolder.chat_icon);
        //随机红包
        if(chatMessage.getType()== Config.TYPE_RANDOMBONUS){
            if(chatMessage.getUid()==UserInfo.getInstance().getUid()){
                viewHolder.content.setBackgroundResource(R.mipmap.sjhb);
            }else {
                viewHolder.content.setBackgroundResource(R.mipmap.sjhb1);
            }
            viewHolder.content.setPadding(0,0,0,0);
            viewHolder.content.setText("");
            viewHolder.content.setOnClickListener(new RandomBonusOnclickListener(position));
            viewHolder.nackname.setText(chatMessage.getNackname());
            if(chatMessage.getStatus()==1) {
                if(viewHolder.progressBar!=null){
                    viewHolder.progressBar.setVisibility(View.INVISIBLE);
                }
            }

        }
        //单双红包
        else if(chatMessage.getType()==Config.TYPE_CDSBONUS){
            viewHolder.content.setBackgroundResource(R.mipmap.cds1);
            viewHolder.content.setPadding(0, 0, 0, 0);
            viewHolder.content.setText("");
            viewHolder.content.setOnClickListener(new CDSBonusOnclickListener(position));
            viewHolder.nackname.setText(chatMessage.getNackname());
        }
        //普通消息
        else {
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
            viewHolder.nackname.setText(chatMessage.getNackname());
            if(chatMessage.getStatus()==1) {
                if(viewHolder.progressBar!=null){
                    viewHolder.progressBar.setVisibility(View.INVISIBLE);
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
                                    progressDialog.dismiss();
                                    LogUtils.e("hbresponse", response);
                                    if(response.contains("CURL ERROR")){
                                        response = response.substring(error.length());
                                    }
                                    LogUtils.e("response===>",response);
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    if(status.equals("0")){
                                        Toast toast = Toast.makeText(context, jsonObject.getString("info"),Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER,0,0);
                                        toast.show();
                                        LogUtils.e("status===>","0");
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
            int statuts = Integer.valueOf(MessageDAO.getStatus(MyApplication.getSQLiteDatabase(), mDatas.get(position).getId(), mDatas.get(
                    position).getType()));
            if(statuts==Config.STATE_CDSBONUS_START){
                progressDialog.dismiss();
                LogUtils.e("DSONCLICK=====>", "1");
                getPersonNum(mDatas.get(position).getId(), mDatas.get(position).getRid(),
                        mDatas.get(position));

            }else if(statuts==Config.STATE_CDSBONUS_GUESSED){
                progressDialog.dismiss();
                LogUtils.e("DSONCLICK=====>", "2");
                getMyguess(mDatas.get(position).getId(),mDatas.get(position).getRid(),
                        UserInfo.getInstance().getUid(),mDatas.get(position));
            }else if(statuts==Config.STATE_CDSBONUS_END){
                progressDialog.dismiss();
                LogUtils.e("DSONCLICK=====>", "3");
                getResult(mDatas.get(position).getId(),mDatas.get(position).getRid(),
                        UserInfo.getInstance().getUid(),mDatas.get(position));
            }

        }

        public CDSBonusOnclickListener(int position){
            this.position = position;
        }


    }

    public void getPersonNum(final int id, final int rid, final ChatMessage msg) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "DS/getpersonNum",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getString("status").equals("0")) {
                                return;
                            }
                            LogUtils.e("getPersonNum",s);
                            String data = jsonObject.getString("data");
                            DS ds = new DS();
                            ds = MyApplication.getGson().fromJson(data, DS.class);
                            LogUtils.e("getPersonNum-ds",ds.toString());
                            Intent i = new Intent(context, CDSActivity.class);
                            Bundle b = new Bundle();
                            b.putSerializable("ds", ds);
                            b.putSerializable("cdsMessage", msg);
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("dsId", String.valueOf(id));
                map.put("rid", String.valueOf(rid));
                map.put("uid",String.valueOf(UserInfo.getInstance().getUid()));
                map.put("token",UserInfo.getInstance().getToken());
                return map;
            }
        };
        MyApplication.getmQueue().add(stringRequest);
    }

    public void getMyguess(final int id,final int rid,final int uid,final ChatMessage msg){
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "DS/getMyguess",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getString("status").equals("0")) {
                                return;
                            }
                            LogUtils.e("getMyGuess",s);
                            String data = jsonObject.getString("data");
                            DS ds = new DS();
                            ds =  MyApplication.getGson().fromJson(data, DS.class);
                            LogUtils.e("getMyguess-ds",ds.toString());
                            Intent i = new Intent(context, CDSActivity.class);
                            Bundle b = new Bundle();
                            b.putSerializable("ds", ds);
                            b.putSerializable("cdsMessage", msg);
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("dsId", String.valueOf(id));
                map.put("rid", String.valueOf(rid));
                map.put("uid",String.valueOf(UserInfo.getInstance().getUid()));
                map.put("token",UserInfo.getInstance().getToken());
                return map;
            }
        };
        MyApplication.getmQueue().add(stringRequest);
    }

    public void getResult(final int id,final int rid,final int uid,final ChatMessage msg){
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "DS/getResult",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getString("status").equals("0")) {
                                return;
                            }
                            LogUtils.e("getMyResult",s);
                            String data = jsonObject.getString("data");
                            DS ds = new DS();
                            ds =MyApplication.getGson().fromJson(data, DS.class);
                            LogUtils.e("getResult-ds",ds.toString());
                            Intent i = new Intent(context, CDSActivity.class);
                            Bundle b = new Bundle();
                            b.putSerializable("ds", ds);
                            b.putSerializable("cdsMessage", msg);
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("dsId", String.valueOf(id));
                map.put("rid", String.valueOf(rid));
                map.put("uid",String.valueOf(UserInfo.getInstance().getUid()));
                map.put("token",UserInfo.getInstance().getToken());
                return map;
            }
        };
        MyApplication.getmQueue().add(stringRequest);
    }
}

