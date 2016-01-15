package com.handsome.qhb.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.handsome.qhb.application.ApplicationManager;
import com.handsome.qhb.bean.LoginData;
import com.handsome.qhb.utils.XMPPUtil;

import org.jivesoftware.smack.XMPPConnection;

import tab.com.handsome.handsome.R;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Button bt_login, bt_register;
    private EditText et_username, et_password;
    private CheckBox cb_savePassword, cb_autoLogin;
    private LoginData loginData;//用户信息

    public static final int LOGIN_SUCCESS = 1;
    public static final int LOGIN_ERROR = 2;

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOGIN_SUCCESS:
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    break;
                case LOGIN_ERROR:
                    Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    private void initViews() {
        //获取登陆信息对象
        loginData = ApplicationManager.getMyApplication(this).loginData;

        bt_login = (Button) findViewById(R.id.id_bt_login);
        bt_register = (Button) findViewById(R.id.id_bt_register);
        bt_login.setOnClickListener(this);
        bt_register.setOnClickListener(this);
        et_username = (EditText) findViewById(R.id.id_et_username);
        et_password = (EditText) findViewById(R.id.id_et_password);
        cb_autoLogin = (CheckBox) findViewById(R.id.id_cb_autoLogin);
        cb_savePassword = (CheckBox) findViewById(R.id.id_cb_savePassword);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_bt_login:
                //保存用户信息
                loginData.username = et_username.getText().toString();
                loginData.password = et_password.getText().toString();
                loginData.isAutoLogin = cb_autoLogin.isChecked();
                loginData.isSavePassword = cb_savePassword.isChecked();
                //异步线程登陆
                new Thread(new Runnable() {
                    public void run() {
                        if(login()){//登陆成功
                            mHandler.sendEmptyMessage(LOGIN_SUCCESS);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{//登陆失败
                            mHandler.sendEmptyMessage(LOGIN_ERROR);
                        }
                    }
                }).start();
                break;
        }
    }

    /**
     * 登陆服务器
     *
     */
    private boolean login() {
        try {
            XMPPConnection connection = XMPPUtil.getXMPPConnection();
            if (connection == null) {
                throw new Exception("连接聊天服务器失败");
            }
            //登陆
            connection.login(loginData.username, loginData.password);
            //将connection保存到全局变量
            ApplicationManager.setXMPPConnection(this, connection);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
