package com.handsome.qhb.ui.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handsome.qhb.adapter.CycleItemAdapter;
import com.handsome.qhb.application.ApplicationManager;
import com.handsome.qhb.ui.activity.ChatActivity;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverItems;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import tab.com.handsome.handsome.R;

public class CycleFragment extends Fragment implements AdapterView.OnItemClickListener {

    private View view;
    private ListView lv_cycle;
    private List<HostedRoom> roominfos;//服务器集合
    private XMPPConnection connection;
    private List<DiscoverItems.Item> listDiscoverItems;//房间集合

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //填充布局
        view = inflater.inflate(R.layout.fragment_cycle, container, false);
        //获得连接
        connection = ApplicationManager.getXMPPConnection(this.getActivity());
        initViews();
        init();
        return view;
    }

    private void initViews() {
        lv_cycle = (ListView) view.findViewById(R.id.id_lv_cycle);
    }

    /**
     * 初始化服务器列表
     *
     * 暂时没用到
     */
    private void initData() {
        try {
            Collection<HostedRoom> hostrooms = null;
            if (roominfos == null) {
                roominfos = new ArrayList<HostedRoom>();
            }
            //获取服务器集合
            hostrooms = MultiUserChat.getHostedRooms(connection,
                    connection.getServiceName());
            for (HostedRoom entry : hostrooms) {
                roominfos.add(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化群聊房间列表
     *
     * 默认进入public.acer服务器
     */
    public void init() {
        listDiscoverItems = new ArrayList<DiscoverItems.Item>();
        // 获得与XMPPConnection相关的ServiceDiscoveryManager
        ServiceDiscoveryManager discoManager = ServiceDiscoveryManager
                .getInstanceFor(connection);
        // 获得指定XMPP实体的项目
        // 这个例子获得与在线目录服务相关的项目
        DiscoverItems discoItems;
        try {
            discoItems = discoManager.discoverItems("public.acer");
            // 获得被查询的XMPP实体的要查看的项目
            Iterator it = discoItems.getItems().iterator();
            // 显示远端XMPP实体的项目
            while (it.hasNext()) {
                DiscoverItems.Item item = (DiscoverItems.Item) it.next();
                listDiscoverItems.add(item);
            }
            lv_cycle.setAdapter(new CycleItemAdapter(this.getActivity(), listDiscoverItems));
            lv_cycle.setOnItemClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击进入房间
     */
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            MultiUserChat muc = new MultiUserChat(connection, listDiscoverItems.get(position).getEntityID());
            muc.join(connection.getUser());
            //进入聊天界面
            Intent intent = new Intent(this.getActivity(), ChatActivity.class);
            intent.putExtra("entityid", listDiscoverItems.get(position).getEntityID());
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
