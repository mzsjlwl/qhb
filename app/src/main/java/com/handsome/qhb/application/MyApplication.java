package com.handsome.qhb.application;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handsome.qhb.bean.Room;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.db.UserDBOpenHelper;
import com.handsome.qhb.utils.LogUtils;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    private static Context context;
    private static RequestQueue mQueue;
    private static Handler roomHandler;
    private static Handler chatHandler;
    private static Handler cdsHandler;
    private static int rid ;
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
        XGPushManager.registerPush(context);

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
        rid = id;
    }

    public synchronized static int getRoomId(){
        return rid;
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
