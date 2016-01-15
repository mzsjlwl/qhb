package com.handsome.qhb.utils;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

public class XMPPUtil {

    /**
     * 连接XMPP服务器
     *
     * @param server 服务器
     * @param port   端口
     * @return 连接对象
     */
    public static XMPPConnection getXMPPConnection(String server, int port) {
        try {
            ConnectionConfiguration config = new ConnectionConfiguration(server, port);
            //设置重连接
            config.setReconnectionAllowed(true);
            //设置安全模式
            config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            //设置连接标志
            config.setSendPresence(true);
            //设置验证方式
            SASLAuthentication.supportSASLMechanism("PLAIN", 0);
            //连接服务器
            XMPPConnection connection = new XMPPTCPConnection(config, null);

            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 连接XMPP服务器，默认端口号5222
     *
     * @param server 服务器
     * @return 连接对象
     */
    public static XMPPConnection getXMPPConnection(String server) {
        return getXMPPConnection(server, 5222);
    }

}
