package com.handsome.qhb.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.listener.MyListener;
import com.handsome.qhb.utils.HttpUtils;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.UserInfo;

import java.util.HashMap;
import java.util.Map;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/14.
 */

public class UpdatePasswordActivity extends BaseActivity implements MyListener{

    //标题
    private TextView tv_title;
    //返回键
    private LinearLayout ll_back;
    //旧密码
    private EditText et_oldPassword;
    //新密码
    private EditText et_password;
    //重复密码
    private EditText et_repeatPassword;
    //makesure
    private TextView tv_makesure;
    //back
    private TextView tv_back;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);

        et_oldPassword = (EditText)findViewById(R.id.et_oldPassword);
        et_password =(EditText) findViewById(R.id.et_password);
        et_repeatPassword=(EditText) findViewById(R.id.et_repeatPassword);
        tv_makesure = (TextView)findViewById(R.id.tv_makesure);
        tv_back = (TextView)findViewById(R.id.tv_back);

        tv_title.setText("修改密码");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        tv_makesure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_password.getText().toString().equals("")){
                    Toast.makeText(UpdatePasswordActivity.this,"新密码不能为空",Toast.LENGTH_SHORT).show();
                    return ;
                }else if(et_repeatPassword.getText().toString().equals("")){
                    Toast.makeText(UpdatePasswordActivity.this,"重复密码不能为空",Toast.LENGTH_SHORT).show();
                    return ;
                }else if(!et_repeatPassword.getText().toString().equals(et_password.getText().toString())){
                    Toast.makeText(UpdatePasswordActivity.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                    return ;
                }else if(et_password.getText().toString().length()<6){
                    Toast.makeText(UpdatePasswordActivity.this,"密码长度不能小于6",Toast.LENGTH_SHORT).show();
                    return;
                }

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("uid", String.valueOf(UserInfo.getInstance().getUid()));
                    map.put("oldPassword", et_oldPassword.getText().toString());
                    map.put("password",et_password.getText().toString());
                    map.put("repeatPassword",et_repeatPassword.getText().toString());
                    map.put("token", UserInfo.getInstance().getToken());
                    HttpUtils.request(UpdatePasswordActivity.this, Config.USERUPDATE_URL, UpdatePasswordActivity.this, map, Config.UPDATEPASSWORD_TAG);

            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getmQueue().cancelAll(Config.USERUPDATE_TAG);
    }

    @Override
    public void dataController(String response, int tag) {
        switch (tag){
            case Config.UPDATEPASSWORD_TAG:
                Toast.makeText(UpdatePasswordActivity.this,"修改成功,请重新登录",Toast.LENGTH_LONG).show();
                Intent i =new Intent(UpdatePasswordActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }

    @Override
    public void requestError(String error) {

    }
}
