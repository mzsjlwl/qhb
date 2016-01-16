package com.handsome.qhb.ui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handsome.qhb.application.ApplicationManager;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import tab.com.handsome.handsome.R;

public class CycleFragment extends Fragment {

    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cycle, container, false);

        try {
            XMPPConnection connection = ApplicationManager.getXMPPConnection(this.getActivity());
            Collection<HostedRoom> hostrooms = null;
            List<HostedRoom> roominfos = new ArrayList<HostedRoom>();
            hostrooms = MultiUserChat.getHostedRooms(connection,
                    connection.getServiceName());
            for (HostedRoom entry : hostrooms) {
                roominfos.add(entry);
                System.out.println("名字：" + entry.getName() + " - ID:" + entry.getJid());
            }
            System.out.println(roominfos.size()+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}
