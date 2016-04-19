package com.handsome.qhb.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.handsome.qhb.utils.LogUtils;

/**
 * Created by Administrator on 2016/4/17.
 */
public class NetBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(!mobNetInfo.isConnected()&&!wifiNetInfo.isConnected()){
            Toast.makeText(context,"网络不可用",Toast.LENGTH_LONG);
            LogUtils.e("BroadcastReceiver","网络不可用");
        }else{
            Toast.makeText(context,"网络可用",Toast.LENGTH_LONG);
            LogUtils.e("BroadcastReceiver","网络可用");
        }
    }


}