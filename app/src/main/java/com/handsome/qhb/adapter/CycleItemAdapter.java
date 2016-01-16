package com.handsome.qhb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.jivesoftware.smackx.muc.HostedRoom;

import java.util.List;

import tab.com.handsome.handsome.R;

public class CycleItemAdapter extends BaseAdapter{

    private List<HostedRoom> list;
    private LayoutInflater inflater;

    public CycleItemAdapter(Context context,List<HostedRoom> list){
        this.list = list;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HostedRoom room = list.get(position);
        View view = inflater.inflate(R.layout.adapter_cycle_item,null);
        TextView tv_cycle_item = (TextView) view.findViewById(R.id.id_tv_cycle_item);
        tv_cycle_item.setText(room.getName()+room.getJid());
        return view;
    }
}
