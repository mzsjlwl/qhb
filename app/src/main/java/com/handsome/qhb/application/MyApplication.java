package com.handsome.qhb.application;

import android.app.Application;

import com.handsome.qhb.bean.LoginData;

import org.jivesoftware.smack.XMPPConnection;

public class MyApplication extends Application {

    public XMPPConnection connection;//用户连接
    public LoginData loginData = new LoginData();//用户信息

}
