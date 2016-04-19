package com.handsome.qhb.application;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.telephony.TelephonyManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.handsome.qhb.db.UserDBOpenHelper;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.NetUtils;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

public class MyApplication extends Application {

    private static Context context;
    private static RequestQueue mQueue;
    private static Handler roomHandler;
    private static Handler chatHandler;
    private static Handler cdsHandler;
    private static Handler hbHandler;
    private static int rid ;
    private static int cid;
    private static TelephonyManager tm;
    private static NotificationManager nm;
    private static SQLiteDatabase db;
    private static Gson gson;

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
                LogUtils.e("TPush","注册成功,设备token为:"+o);
            }

            @Override
            public void onFail(Object o, int i, String s) {
                LogUtils.e("TPush","注册失败,错误码"+i+",错误信息："+s);
            }
        });


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

    public synchronized static Handler getRoomHandler(){
        return roomHandler;
    }


    public synchronized  static void setRoomHandler(Handler hallHandler){
        roomHandler = hallHandler;

    }

    public synchronized  static Handler getChatHandler(){
        return chatHandler;
    }


    public synchronized  static void setChatHandler(Handler handler,int id){
        chatHandler= handler;
        rid = id;
    }


    public synchronized  static Handler getCdsHandler(){
        return cdsHandler;
    }
    public synchronized  static void setCdsHandler(Handler handler,int id){
        cdsHandler = handler;
        cid = id;
    }

    public synchronized  static Handler getHbHandler(){
        return hbHandler;
    }

    public synchronized  static void setHbHandler(Handler handler){
        hbHandler = handler;
    }


    public synchronized static int getRoomId(){
        return rid;
    }

    public synchronized  static int getCid(){
        return cid;
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

}
