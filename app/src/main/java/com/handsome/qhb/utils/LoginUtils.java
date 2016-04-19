package com.handsome.qhb.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.User;
import com.handsome.qhb.config.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhang on 2016/4/14.
 */
public class LoginUtils {
    public static void AutoLogin(final Activity activity){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+"User/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            LogUtils.e("AutoLogin","=======");
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if(status.equals("0")){
                                return;
                            }
                            User user =  MyApplication.getGson().fromJson(jsonObject.getString("data"), User.class);
                            UserInfo.setUser(user);
                            SharedPreferences.Editor editor =activity.getSharedPreferences("data", activity.MODE_PRIVATE).edit();
                            editor.clear();
                            editor.putString("user", jsonObject.getString("data"));
                            editor.putLong("date", new Date().getTime());
                            editor.commit();
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
                Map<String, String> map = new HashMap<String, String>();
                map.put("username",UserInfo.getInstance().getUsername());
                map.put("password",UserInfo.getInstance().getPassword());
                return map;
            }
        };
        stringRequest.setTag(Config.AUTOLOGIN_TAG);
        MyApplication.getmQueue().add(stringRequest);
    }
}
