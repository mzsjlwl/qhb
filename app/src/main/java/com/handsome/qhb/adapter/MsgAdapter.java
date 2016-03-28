package com.handsome.qhb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handsome.qhb.bean.ChatMessage;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.UserInfo;

import java.util.List;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/24.
 */
public class MsgAdapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private List<ChatMessage> mDatas;

    public MsgAdapter(Context context, List<ChatMessage> datas)
    {
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
                viewHolder.content.setBackgroundResource(R.drawable.balloon_r2);
                viewHolder.content.setPadding(15,5,20,5);
            }else{
                viewHolder.content.setBackgroundResource(R.drawable.balloon_l2);
                viewHolder.content.setPadding(20,5,15,5);
            }
            viewHolder.content.setText(chatMessage.getContent());
            viewHolder.nickname.setText(chatMessage.getNackname());
        }
        if(chatMessage.getStatus()==1) {
            viewHolder.progressBar.setVisibility(View.INVISIBLE);
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

        @Override
        public void onClick(View view) {

        }

        public RandomBonusOnclickListener(int position){

        }
    }
}
