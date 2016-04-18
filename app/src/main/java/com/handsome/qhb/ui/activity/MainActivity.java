package com.handsome.qhb.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.User;
import com.handsome.qhb.receiver.NetBroadcastReceiver;
import com.handsome.qhb.ui.fragment.FragmentController;
import com.handsome.qhb.ui.fragment.ShopFragment;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.NetUtils;
import com.handsome.qhb.utils.UserInfo;

import java.util.Date;

import tab.com.handsome.handsome.R;


public class MainActivity extends BaseActivity implements View.OnClickListener,NetBroadcastReceiver.netEventHandler {
    LinearLayout ll_shop,ll_hall,ll_user;
    FrameLayout ly_content;
    private FragmentController fragmentController;
    private ShopFragment shopFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtils.e("mainactivity", "oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetBroadcastReceiver.mListeners.add(this);
        if(UserInfo.getInstance()==null){
            //判断登录情况
            SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
            User user = MyApplication.getGson().fromJson(sharedPreferences.getString("user", ""),User.class);
            if(user==null||user.getUid()==0){
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
                return;
            }else{
                UserInfo.setUser(user);
            }
        }


        LogUtils.e("user==>",UserInfo.getInstance().toString());
        if(savedInstanceState==null){
            LogUtils.e("savedInstanceState","null");
            fragmentController = new FragmentController(this,R.id.ly_content,0);
        }else{
            LogUtils.e("savedInstance","nonull");
            fragmentController = new FragmentController(this,R.id.ly_content,1);
        }
        //初始化控件
        initViews();
        fragmentController.showFragment(0);
        shopFragment  =(ShopFragment)fragmentController.getFragment(0);

    }

    /**
     * 初始化控件
     */
    private void initViews(){
        ly_content = (FrameLayout) findViewById(R.id.ly_content);
        ll_shop = (LinearLayout) findViewById(R.id.ll_shop);
        ll_hall = (LinearLayout) findViewById(R.id.ll_hall);
        ll_user = (LinearLayout) findViewById(R.id.ll_user);
        ll_shop.setOnClickListener(this);
        ll_hall.setOnClickListener(this);
        ll_user.setOnClickListener(this);
    }

    /**
     * 按钮点击事件
     */
    @Override
    public void onClick(View v) {
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (v.getId()){
            case R.id.ll_shop:
                if(shopFragment!=null&&shopFragment.getScheduledExecutorService()!=null) {
                    shopFragment.getScheduledExecutorService().shutdown();
                    shopFragment.onStartSlider();
                    fragmentController.showFragment(0);
                }
                break;
            case R.id.ll_hall:
                if(UserInfo.getInstance()==null){
                    Intent i = new Intent(this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    if(shopFragment!=null&&shopFragment.getScheduledExecutorService()!=null) {
                        shopFragment.getScheduledExecutorService().shutdown();
                        fragmentController.showFragment(1);
                    }
                }
                break;
            case R.id.ll_user:
                if(UserInfo.getInstance()==null){
                    Intent i = new Intent(this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    if(shopFragment!=null&&shopFragment.getScheduledExecutorService()!=null) {
                        shopFragment.getScheduledExecutorService().shutdown();
                        fragmentController.showFragment(2);
                    }
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.e("activity", "onstart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.e("activity","onstop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e("activity", "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.e("activity", "onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.e("activity", "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e("activity", "onDestroy");
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.clear();
        editor.putString("user", MyApplication.getGson().toJson(UserInfo.getInstance()));
        editor.putLong("date", new Date().getTime());
        editor.commit();
    }

    @Override
    public void onNetChange() {
        if (NetUtils.getNetworkState(this) == NetUtils.NETWORN_NONE) {
            Toast.makeText(MainActivity.this,"网络异常,请检查后再试",Toast.LENGTH_LONG);
        }else {

        }
    }
}
