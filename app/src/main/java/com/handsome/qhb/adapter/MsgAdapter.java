package com.handsome.qhb.adapter;

import android.app.Activity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.reflect.TypeToken;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.ChatMessage;
import com.handsome.qhb.bean.DS;
import com.handsome.qhb.bean.RandomBonus;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.listener.MyListener;
import com.handsome.qhb.ui.activity.BonusActivity;
import com.handsome.qhb.ui.activity.CDSActivity;
import com.handsome.qhb.utils.HttpUtils;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.TimeUtils;
import com.handsome.qhb.utils.UserInfo;
import com.squareup.picasso.Picasso;

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
public class MsgAdapter extends BaseAdapter {


    private LayoutInflater mInflater;
    private List<ChatMessage> mDatas;
    private List<RandomBonus> bonusList = new ArrayList<RandomBonus>();
    private Context context;


    public MsgAdapter(Context context, List<ChatMessage> datas) {

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
    public int getItemViewType(int position) {
        ChatMessage msg = mDatas.get(position);

        if (msg.getType() == Config.TYPE_TEXT||msg.getType()==Config.TYPE_CANCELBONUS||msg.getType()==Config.TYPE_DSRESULT) {
            return msg.getUid() == UserInfo.getInstance().getUid() ? 1 : 0;
        }else{
            return msg.getUid() == UserInfo.getInstance().getUid() ? 3 : 2;
        }

    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChatMessage chatMessage = mDatas.get(position);
        BaseViewHolder viewHolder;
        int type = getItemViewType(position);
        Log.e("My", chatMessage.toString());
        if (convertView == null) {
            switch (type) {
                case 0:
                    viewHolder = new TextViewHolder();
                    convertView = mInflater.inflate(R.layout.chat_from_msg,
                            parent, false);
                    convertView.setTag(viewHolder);
                    fillTextViewMessage((TextViewHolder) viewHolder, convertView);
                    break;
                case 1:
                    viewHolder = new TextViewHolder();
                    convertView = mInflater.inflate(R.layout.chat_send_msg, parent, false);
                    convertView.setTag(viewHolder);
                    fillTextViewMessage((TextViewHolder) viewHolder, convertView);
                    break;
                case 2:
                    viewHolder = new ImageViewHolder();
                    convertView = mInflater.inflate(R.layout.bonus_from_msg,
                            parent, false);
                    convertView.setTag(viewHolder);
                    fillImageViewMessage((ImageViewHolder) viewHolder, convertView);
                    break;
                default :
                    viewHolder = new ImageViewHolder();
                    convertView = mInflater.inflate(R.layout.bonus_send_msg, parent, false);
                    convertView.setTag(viewHolder);
                    fillImageViewMessage((ImageViewHolder) viewHolder, convertView);
                    break;

            }

        } else {
            viewHolder = (BaseViewHolder) convertView.getTag();
        }
        if (position - 1 >= 0) {
            String data = TimeUtils.compareLast(chatMessage.getDate(), mDatas.get(position - 1).getDate());
            if (data.equals("")) {
                viewHolder.createDate.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.createDate.setVisibility(View.VISIBLE);
                viewHolder.createDate.setText(data);
            }
        } else {
            viewHolder.createDate.setText(TimeUtils.getInterval(chatMessage.getDate()));
        }

        Picasso.with(context).load(chatMessage.getPhoto()).into(viewHolder.chat_icon);
        //随机红包


        if(type ==0||type==1){
            handlerTextMsg((TextViewHolder) viewHolder, chatMessage);
        }else{

            handlerImageMsg((ImageViewHolder) viewHolder, chatMessage, position);
        }
        return convertView;
    }

    private void fillTextViewMessage(TextViewHolder holder, View convertView) {
        holder.createDate = (TextView) convertView.findViewById(R.id.chat_createDate);
        holder.chat_icon = (ImageView) convertView.findViewById(R.id.chat_icon);
        holder.nackname = (TextView) convertView
                .findViewById(R.id.chat_name);
        holder.content = (TextView) convertView
                .findViewById(R.id.chat_content);
        holder.rl_content = (RelativeLayout) convertView.findViewById(R.id.rl_content);
        if(convertView.findViewById(R.id.id_progressBar)!=null){
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.id_progressBar);
        }
    }

    private void fillImageViewMessage(ImageViewHolder holder, View convertView) {
        holder.createDate = (TextView) convertView.findViewById(R.id.chat_createDate);
        holder.chat_icon = (ImageView) convertView.findViewById(R.id.chat_icon);
        holder.nackname = (TextView) convertView
                .findViewById(R.id.chat_name);
        holder.iv_content = (ImageView) convertView.findViewById(R.id.iv_content);
    }

    private void handlerTextMsg(TextViewHolder holder, ChatMessage message) {
        LogUtils.e("handlerTextMsg====>", String.valueOf(message.getStatus()));
        holder.content.setText(message.getContent());
        holder.nackname.setText(message.getNackname());
        if (message.getStatus() == 1) {

            if (holder.progressBar != null) {
                holder.progressBar.setVisibility(View.GONE);
            }
        } else {
            if (holder.progressBar != null) {
                holder.progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    private void handlerImageMsg(ImageViewHolder holder, ChatMessage message, int position) {
        if (message.getType() == Config.TYPE_CDSBONUS) {
            ViewGroup.LayoutParams p = holder.iv_content.getLayoutParams();
            p.height = MyApplication.height * 5 / 36;
            p.width = MyApplication.width * 7 / 12;
            holder.iv_content.setBackgroundResource(R.mipmap.cds1);
            holder.iv_content.setOnClickListener(new CDSBonusOnclickListener(position));
            holder.nackname.setText(message.getNackname());
        } else {
            ViewGroup.LayoutParams p = holder.iv_content.getLayoutParams();
            p.height = MyApplication.height * 5 / 36;
            p.width = MyApplication.width * 7 / 12;
            if (message.getUid() == UserInfo.getInstance().getUid()) {
                holder.iv_content.setBackgroundResource(R.mipmap.sjhb);
            } else {
                holder.iv_content.setBackgroundResource(R.mipmap.sjhb1);
            }
            holder.iv_content.setOnClickListener(new RandomBonusOnclickListener(position));
            holder.nackname.setText(message.getNackname());

        }
    }


    private class BaseViewHolder {
        public TextView createDate;
        public TextView nackname;
        public ImageView chat_icon;
    }

    private class TextViewHolder extends BaseViewHolder {
        public TextView content;
        public ProgressBar progressBar;
        public RelativeLayout rl_content;
    }

    private class ImageViewHolder extends BaseViewHolder {
        public ImageView iv_content;
    }

    class RandomBonusOnclickListener implements View.OnClickListener {

        private int position;

        public RandomBonusOnclickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("获取中");
            progressDialog.setCancelable(true);
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "HB/getRandomBonus",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                LogUtils.e("randomresponse=====>", response);
                                progressDialog.dismiss();
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if (status.equals("0")) {
                                    Toast toast = Toast.makeText(context, jsonObject.getString("info"), Toast.LENGTH_SHORT);
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
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("uid", String.valueOf(UserInfo.getInstance().getUid()));
                    map.put("id", String.valueOf(mDatas.get(position).getId()));
                    map.put("token", UserInfo.getInstance().getToken());
                    return map;
                }
            };
            MyApplication.getmQueue().add(stringRequest);
        }
    }


    class CDSBonusOnclickListener implements View.OnClickListener {

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
                            b.putSerializable("cdsMessage", mDatas.get(position));
                            i.putExtras(b);
                            context.startActivity(i);
                        }

                        @Override
                        public void requestError(String error) {
                            Toast.makeText(context, "网络异常,请检查后再试", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }, map, Config.DSGETMYGUESS_TAG);

        }

        public CDSBonusOnclickListener(int position) {
            this.position = position;
        }

    }

}

