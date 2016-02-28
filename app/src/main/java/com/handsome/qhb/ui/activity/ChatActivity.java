package com.handsome.qhb.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.handsome.qhb.application.ApplicationManager;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.delay.packet.DelayInformation;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.Date;

import tab.com.handsome.handsome.R;

public class ChatActivity extends Activity implements View.OnClickListener {

    private Button bt_send;
    private EditText et_chat;
    private ListView lv_chat;
    private MultiUserChat muc;
    private XMPPConnection connection;

    private boolean isHistory = false;
    private int count = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Bundle bd = msg.getData();
                    String from = bd.getString("from");
                    String body = bd.getString("body");
                    String msgHeader = "有新的群消息了";
                    String msgContent = "\n" + from + ":" + "\n" + body;
                    Toast.makeText(ChatActivity.this, "" + msgHeader + msgContent, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //获得连接
        connection = ApplicationManager.getXMPPConnection(this);
        initViews();

        String entityId = getIntent().getStringExtra("entityid");
        if (muc == null) {
            muc = new MultiUserChat(connection, entityId);
        }
        muc.addMessageListener(new ChatPacketListener(muc));
//        muc.addParticipantListener(new PacketListener() {
//            public void processPacket(Packet arg0) {
//                Message msg = (Message)arg0;
//                Toast.makeText(ChatActivity.this, msg.getFrom() + ":" + msg.getBody(),Toast.LENGTH_SHORT).show();
//            }
//        });
        connection.addPacketListener(new ChatPacketListener(muc), null);
    }

    private void initViews() {
        bt_send = (Button) findViewById(R.id.id_bt_send);
        et_chat = (EditText) findViewById(R.id.id_et_chat);
        lv_chat = (ListView) findViewById(R.id.id_lv_chat);
        bt_send.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_bt_send:
                try {
                    System.out.println("begin send");
                    Message msg = new Message();
                    msg.setBody("圣体");
                    muc.sendMessage(msg);
                    connection.sendPacket(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    private class ChatPacketListener implements PacketListener {
        private String _number;
        private Date _lastDate;
        private MultiUserChat _muc;
        private String _roomName;

        public ChatPacketListener(MultiUserChat muc) {
            _number = "0";
            _lastDate = new Date(0);
            _muc = muc;
            _roomName = muc.getRoom();
        }

        @Override
        public void processPacket(Packet packet) {
            System.out.println("消息格式:" + packet.toXML());
            Message message = (Message) packet;
            String from = message.getFrom();
            System.out.println("----------------" + message.getBody());
            if (message.getBody() != null) {
                DelayInformation inf = (DelayInformation) message.getExtension(
                        "x", "jabber:x:delay");
                System.out.println("判断消息");
                if (inf == null && count >= 1) {
                    System.out.println("新消息来了");
                    isHistory = true;
                } else {
                    System.out.println("这是旧的消息");
                }
                android.os.Message msg = new android.os.Message();
                msg.what = 1;
                Bundle bd = new Bundle();
                bd.putString("from", from);
                bd.putString("body", message.getBody());
                msg.setData(bd);
                mHandler.sendMessage(msg);
            }
            count++;
        }
    }
}
