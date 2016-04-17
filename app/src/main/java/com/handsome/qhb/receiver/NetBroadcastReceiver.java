package com.handsome.qhb.receiver;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.utils.NetUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/17.
 */
public class NetBroadcastReceiver extends BroadcastReceiver {
    public static ArrayList<netEventHandler> mListeners = new ArrayList<netEventHandler>();
    private static String NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(NET_CHANGE_ACTION)) {
            MyApplication.mNetWorkState = NetUtils.getNetworkState(context);
            if (mListeners.size() > 0)// 通知接口完成加载
                for (netEventHandler handler : mListeners) {
                    handler.onNetChange();
                }
        }
    }

    public static abstract interface netEventHandler {

        public abstract void onNetChange();
    }
}