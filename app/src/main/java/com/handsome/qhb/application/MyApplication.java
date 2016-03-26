package com.handsome.qhb.application;

import android.app.Application;
import android.content.Context;
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
import com.handsome.qhb.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MyApplication extends Application {

    private static Context context;
    private static RequestQueue mQueue;
    private static Handler roomHandler;
    private static Handler chatHandler;
    private static String Tag;
    private static TelephonyManager tm;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
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

    public synchronized  static void setChatHandler(Handler handler){
        chatHandler= handler;
    }


    public synchronized static String getTag(){
        return tm.getDeviceId();
    }

}
