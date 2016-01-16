package com.handsome.qhb.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.handsome.qhb.application.ApplicationManager;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;

import tab.com.handsome.handsome.R;

public class ChatActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        String entityId = getIntent().getStringExtra("entityid");
        MultiUserChat muc = new MultiUserChat(ApplicationManager.getXMPPConnection(this), entityId);
        try {
            Message msg = new Message();
            msg.setBody("aa");
            muc.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        muc.addMessageListener(new PacketListener() {
            public void processPacket(Packet packet) throws SmackException.NotConnectedException {
                Message message = (Message) packet;
                String body = message.getBody();
                String language = message.getLanguage();
                String subject = message.getSubject();
                System.out.println("body:"+body+"---lanuage:"+language+"---subject:"+subject);
            }
        });
    }

}
