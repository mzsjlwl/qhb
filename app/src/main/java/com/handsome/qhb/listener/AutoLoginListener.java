package com.handsome.qhb.listener;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;

import com.android.volley.Response;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.User;
import com.handsome.qhb.utils.HttpUtils;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

/**
 * Created by zhang on 2016/4/19.
 */
public class AutoLoginListener implements Response.Listener<String> {

    private String url;
    private Map<String, String> map;
    private MyListener listener;
    private Activity activity;
    private int tag;

    public AutoLoginListener(Activity activity,String url, MyListener listener, Map<String, String> map,int tag) {
        this.url = url;
        this.map = map;
        this.listener = listener;
        this.activity = activity;
        this.tag = tag;
    }

    @Override
    public void onResponse(String response) {
        try {
            LogUtils.e("AutoLogin", "=======");
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            if(status.equals("0")){
                return;
            }
            User user =  MyApplication.getGson().fromJson(jsonObject.getString("data"), User.class);
            UserInfo.setUser(user);
            SharedPreferences.Editor editor =MyApplication.getContext().getSharedPreferences("data", MyApplication.getContext().MODE_PRIVATE).edit();
            editor.clear();
            editor.putString("user", jsonObject.getString("data"));
            editor.putLong("date", new Date().getTime());
            editor.commit();
            HttpUtils.request(activity,url,listener,map,tag);
        } catch (JSONException e) {
                e.printStackTrace();
        }
    }


}
