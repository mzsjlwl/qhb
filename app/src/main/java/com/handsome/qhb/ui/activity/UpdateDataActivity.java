package com.handsome.qhb.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.User;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.listener.MyListener;
import com.handsome.qhb.utils.HttpUtils;
import com.handsome.qhb.utils.ImageUtils;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.UserInfo;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/14.
 */

public class UpdateDataActivity extends BaseActivity implements MyListener{
   //返回键
    private LinearLayout ll_back;
    //修改用户名
    private EditText et_nackname;
    //makesure
    private TextView tv_makesure;
    //用户头像
    private ImageView iv_user_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        et_nackname = (EditText)findViewById(R.id.et_nackname);
        tv_makesure = (TextView)findViewById(R.id.tv_makesure);
        iv_user_photo = (ImageView)findViewById(R.id.iv_user_photo);
        Picasso.with(this).load(UserInfo.getInstance().getPhoto()).into(iv_user_photo);
        et_nackname.setText(UserInfo.getInstance().getNackname());

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_makesure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid", String.valueOf(UserInfo.getInstance().getUid()));
                map.put("nackname", et_nackname.getText().toString());
                map.put("token", UserInfo.getInstance().getToken());
                HttpUtils.request(UpdateDataActivity.this,Config.USERUPDATE_URL,UpdateDataActivity.this,map,Config.UPDATEDATA_TAG);
            }
        });
        iv_user_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UpdateDataActivity.this, UpdatePhotoActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
            Picasso.with(this).load(UserInfo.getInstance().getPhoto()).into(iv_user_photo);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getmQueue().cancelAll(Config.USERUPDATE_TAG);
    }

    @Override
    public void dataController(String response, int tag) {
        switch (tag){
            case Config.UPDATEDATA_TAG:
                LogUtils.e("udpateUser", response);
                UserInfo.setUser((User) (MyApplication.getGson().fromJson(response, new TypeToken<User>() {
                }.getType())));
                finish();
                break;
        }

    }

    @Override
    public void requestError(String error) {

    }
}
