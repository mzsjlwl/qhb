package com.handsome.qhb.application;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.TelephonyManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.handsome.qhb.db.UserDBOpenHelper;
import com.handsome.qhb.listener.MessageListener;
import com.handsome.qhb.utils.LogUtils;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    private static Context context;
    private static RequestQueue mQueue;
    private static TelephonyManager tm;
    private static NotificationManager nm;
    private static SQLiteDatabase db;
    private static Gson gson;
    public static List<MessageListener> messageListenersList = new ArrayList<MessageListener>();
    public static int netstate = 1;
    public static int welcome = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //获取ID作为tag
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //获取消息通知
        nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        XGPushManager.registerPush(context, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                LogUtils.e("TPush", "注册成功,设备token为:" + o);
            }

            @Override
            public void onFail(Object o, int i, String s) {
                LogUtils.e("TPush", "注册失败,错误码" + i + ",错误信息：" + s);
            }
        });
       // LeakCanary.install(this);

    }

    public synchronized static Context getContext(){
        return context;
    }

    public synchronized static RequestQueue getmQueue(){
        if(mQueue == null){
            mQueue = Volley.newRequestQueue(MyApplication.getContext());
        }
        return mQueue;
    }
    public synchronized static String getTag(){
        return tm.getDeviceId();
    }

    public synchronized static NotificationManager getNotificationManager(){
        return nm;
    }


    public synchronized  static SQLiteDatabase getSQLiteDatabase(){
        if(db==null){
            db = UserDBOpenHelper.getInstance(context).getWritableDatabase();
        }
        return db;
    }

    public synchronized  static Gson getGson(){
        if(gson==null){
            gson = new Gson();
        }
        return gson;
    }

    public synchronized  static void setNetstate(int state){
        netstate = state;
    }
    public synchronized  static int getNetstate(){
        return netstate;
    }

}
