package com.handsome.qhb.listener;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

public class ChatPacketListener implements PacketListener {
    @Override
    public void processPacket(Packet packet) {
        Message message = (Message) packet;
        System.out.println(message.getBody());
    }
}
