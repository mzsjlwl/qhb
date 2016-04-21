package com.handsome.qhb.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.listener.MyListener;
import com.handsome.qhb.utils.HttpUtils;
import com.handsome.qhb.utils.UserInfo;

import java.util.HashMap;
import java.util.Map;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/4/6.
 */
public class AddMoneyActivity extends BaseActivity implements MyListener{

    private LinearLayout ll_back;
    private TextView tv_notice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmoney);
        ll_back =(LinearLayout)findViewById(R.id.ll_back);
        tv_notice = (TextView)findViewById(R.id.tv_notice);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Map<String,String> map = new HashMap<String,String>();
        map.put("uid",String.valueOf(UserInfo.getInstance().getUid()));
        map.put("token",UserInfo.getInstance().getToken());
        HttpUtils.request(AddMoneyActivity.this, Config.USERNOTICE_URL, this, map,Config.USERNOTICE_TAG);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getmQueue().cancelAll(Config.USERNOTICE_TAG);
    }


    @Override
    public void dataController(String response, int tag) {
        switch (tag){
            case Config.USERNOTICE_TAG:
                tv_notice.setText(response);
                break;
        }
    }
}
