package com.handsome.qhb.application;

import android.content.Context;

import org.jivesoftware.smack.XMPPConnection;

public class ApplicationManager {

    /**
     * 获取全局对象
     * @param ctx
     * @return
     */
    public static MyApplication getMyApplication(Context ctx) {
        return (MyApplication) ctx.getApplicationContext();
    }

    /**
     * 获取全局的XMPPConnection
     * @param ctx
     * @return
     */
    public static XMPPConnection getXMPPConnection(Context ctx){
        return getMyApplication(ctx).connection;
    }

    /**
     * 设置全局的XMPPConnection
     * @param ctx
     * @param connection
     */
    public static void setXMPPConnection(Context ctx,XMPPConnection connection){
        getMyApplication(ctx).connection = connection;
    }
}
