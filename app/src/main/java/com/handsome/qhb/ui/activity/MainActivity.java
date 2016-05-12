package com.handsome.qhb.ui.activity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.User;
import com.handsome.qhb.ui.fragment.FragmentController;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.UserInfo;

import java.util.Date;

import tab.com.handsome.handsome.R;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    LinearLayout ll_shop,ll_hall,ll_user;
    FrameLayout ly_content;
    private FragmentController fragmentController;
    private NetworkBroadcastReceiver myReceiver;
    private int networkstate = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager windowManager = getWindowManager();
        MyApplication.width = windowManager.getDefaultDisplay().getWidth();
        MyApplication.height = windowManager.getDefaultDisplay().getHeight();

        LogUtils.e("width",String.valueOf(MyApplication.width));
        LogUtils.e("height",String.valueOf(MyApplication.height));
        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);

        if(sharedPreferences.getString("active","").equals("")){
            Intent i = new Intent(this,IndexActivity.class);
            startActivity(i);
            finish();
            return;
        }

//        if(MyApplication.welcome==0){
//            Intent i = new Intent(this,WelcomeActivity.class);
//            startActivity(i);
//            LogUtils.e("WELECOME", String.valueOf(MyApplication.welcome));
//            MyApplication.welcome=1;
//            finish();
//            return;
//        }
        setContentView(R.layout.activity_main);
        registerReceiver();
        if(UserInfo.getInstance().getUid()==0){
            //判断登录情况
            sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
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


        if(savedInstanceState==null){
            fragmentController = new FragmentController(this,R.id.ly_content,0);
        }else{
            fragmentController = new FragmentController(this,R.id.ly_content,1);
        }
        //初始化控件
        initViews();
        fragmentController.showFragment(0);
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
        switch (v.getId()){
            case R.id.ll_shop:
                    fragmentController.showFragment(0);
                break;
            case R.id.ll_hall:
                    fragmentController.showFragment(1);
                break;
            case R.id.ll_user:
                    fragmentController.showFragment(2);
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
        LogUtils.e("Mainactivity", "onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.e("activity", "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e("Mainactivity", "onDestroy");
//        MyApplication.welcome = 0;
        if(myReceiver!=null){
            unregisterReceiver();
        }
    }


    private void registerReceiver(){
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        myReceiver = new NetworkBroadcastReceiver();
        this.registerReceiver(myReceiver, filter);
    }

    private void unregisterReceiver(){
        this.unregisterReceiver(myReceiver);
    }

    public class NetworkBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if(!mobNetInfo.isConnected()&&!wifiNetInfo.isConnected()){
                Toast.makeText(MainActivity.this,"网络不可用",Toast.LENGTH_LONG).show();
                LogUtils.e("BroadcastReceiver", "网络不可用");
                networkstate = 0;
            }else{
                if(networkstate==0){
                    ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                    ComponentName componentName = activityManager.getRunningTasks(Integer.MAX_VALUE).get(0).topActivity;
                    Intent i;
                    switch (componentName.getClassName()){
                        case "com.handsome.qhb.ui.activity.MainActivity":
                            i= new Intent(MainActivity.this,MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(i);
                            break;
                    }
                }
                networkstate=1;
            }
        }
    }
}
