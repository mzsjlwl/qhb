package com.handsome.qhb.ui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handsome.qhb.adapter.CycleItemAdapter;
import com.handsome.qhb.application.ApplicationManager;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import tab.com.handsome.handsome.R;

public class CycleFragment extends Fragment implements AdapterView.OnItemClickListener {

    private View view;
    private ListView lv_cycle;
    private List<HostedRoom> roominfos;//服务器集合
    private XMPPConnection connection;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cycle, container, false);
        initViews();
        initData();
        return view;
    }

    private void initViews() {
        lv_cycle = (ListView) view.findViewById(R.id.id_lv_cycle);
    }

    /**
     * 初始化服务器列表
     */
    private void initData() {
        try {
            //获得连接
            connection = ApplicationManager.getXMPPConnection(this.getActivity());
            Collection<HostedRoom> hostrooms = null;
            if(roominfos == null){
                roominfos = new ArrayList<HostedRoom>();
            }
            //获取服务器集合
            hostrooms = MultiUserChat.getHostedRooms(connection,
                    connection.getServiceName());
            for (HostedRoom entry : hostrooms) {
                roominfos.add(entry);
            }
            //显示数据
            lv_cycle.setAdapter(new CycleItemAdapter(this.getActivity(), roominfos));
            lv_cycle.setOnItemClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println(roominfos.get(position).getName() + "");
    }
}
