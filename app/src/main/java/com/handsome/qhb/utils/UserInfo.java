package com.handsome.qhb.utils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.User;

/**
 * Created by zhang on 2016/3/14.
 */
public class UserInfo {
    private static User user;

    public static User getInstance(){
        return user;
    }
    public static void setUser(User u){
        user = u;
    }
    private UserInfo(){

    }
}
