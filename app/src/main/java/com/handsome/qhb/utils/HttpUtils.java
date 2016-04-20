package com.handsome.qhb.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.User;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.listener.AutoLoginListener;
import com.handsome.qhb.listener.CDSListener;
import com.handsome.qhb.listener.MyListener;
import com.handsome.qhb.ui.activity.LoginActivity;
import com.handsome.qhb.ui.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhang on 2016/4/19.
 */
public class HttpUtils {
    public static String error = "CURL ERROR: Problem (2) in the Chunked-Encoded data";
    public static void request(final Activity activity,final String url, final MyListener listener, final Map<String, String> map, final int tag){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            LogUtils.e("response",response);
                            if(response.contains("CURL ERROR")){
                                response = response.substring(error.length());
                            }
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if(status.equals("0")){

                                Toast toast = Toast.makeText(activity, jsonObject.getString("info"), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                return;
                            }else if(status.equals("-1")){
                                login(activity,url,listener,map,tag);
                                return;
                            }else if(status.equals("-2")){
                                Toast toast = Toast.makeText(activity,"用户异地登录", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                UserInfo.setUser(null);
                                Intent i = new Intent(activity, LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                activity.startActivity(i);
                                activity.finish();
                                return;
                            }
                            listener.dataController(jsonObject.getString("data"),tag);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        stringRequest.setTag(tag);
        MyApplication.getmQueue().add(stringRequest);
    }

    public static void login(final Activity activity,final String url, final MyListener listener, final Map<String, String> map, final int tag){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "User/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if(status.equals("0")){
                                Toast toast = Toast.makeText(activity, jsonObject.getString("info"), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                return;
                            }

                            User user =  MyApplication.getGson().fromJson(jsonObject.getString("data"), User.class);
                            UserInfo.setUser(user);
                            SharedPreferences.Editor editor =MyApplication.getContext().getSharedPreferences("data", MyApplication.getContext().MODE_PRIVATE).edit();
                            editor.clear();
                            editor.putString("user", jsonObject.getString("data"));
                            editor.putLong("date", new Date().getTime());
                            editor.commit();
                            request(activity, url, listener, map, tag);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> loginMap = new HashMap<String,String>();
                loginMap.put("username",UserInfo.getInstance().getUsername());
                loginMap.put("password", UserInfo.getInstance().getPassword());
                return loginMap;
            }
        };
        stringRequest.setTag(tag);
        MyApplication.getmQueue().add(stringRequest);
    }
}


