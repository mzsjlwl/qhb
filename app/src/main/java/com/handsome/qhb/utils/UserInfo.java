package com.handsome.qhb.utils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.User;

/**
 * Created by zhang on 2016/3/14.
 */
public class UserInfo {
    private static User user = new User();

    public static User getInstance(){
        return user;
    }
    public static void setUser(User u){

        if(u==null){
            user.setUid(0);
            user.setIntegral(0);
            user.setNackname("");
            user.setPhoto("");
            user.setToken("");
            user.setUsername("");
            return;
        }
        user.setUid(u.getUid());
        user.setIntegral(u.getIntegral());
        user.setNackname(u.getNackname());
        user.setPhoto(u.getPhoto());
        user.setToken(u.getToken());
        user.setUsername(u.getUsername());
    }
    private UserInfo(){

    }
}
