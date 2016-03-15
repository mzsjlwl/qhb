package com.handsome.qhb.ui.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handsome.qhb.bean.Slider;
import com.handsome.qhb.bean.User;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.ui.fragment.FragmentController;
import com.handsome.qhb.ui.fragment.HallFragment;
import com.handsome.qhb.ui.fragment.ShopFragment;
import com.handsome.qhb.ui.fragment.UserFragment;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.UserInfo;

import java.util.List;

import tab.com.handsome.handsome.R;


public class MainActivity extends BaseActivity implements View.OnClickListener{

    TextView tv_shop,tv_hall,tv_user;
    FrameLayout ly_content;
    private FragmentController controller;
    public static List<Slider> sliderLists = null  ;
    private FragmentManager fragmentManager ;
//    private FragmentTransaction fragmentTransaction;
    private ShopFragment shopFragment;
    private UserFragment userFragment;
    private HallFragment hallFragment;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        shopFragment = new ShopFragment();
        hallFragment = new HallFragment();
        userFragment = new UserFragment();
        fragmentTransaction.replace(R.id.ly_content, shopFragment);
        fragmentTransaction.commit();
//        controller = FragmentController.getInstance(this,R.id.ly_content);
//        Bundle b= getIntent().getExtras();
//        if(b!=null){
//            if(b.getString("TAG")== Config.USER_TAG){
//                controller.showFragment(2);
//                return ;
//            }
//        }
        Bundle b = getIntent().getExtras();
        if(b!=null){
            if(b.getSerializable("user")!=null){
                User user = (User)b.getSerializable("user");
                UserInfo.setUser(user);
            }
        }
//        controller.showFragment(0);


    }

    /**
     * 初始化控件
     */
    private void initViews(){
        ly_content = (FrameLayout) findViewById(R.id.ly_content);
        tv_shop = (TextView) findViewById(R.id.tv_shop);
        tv_hall = (TextView) findViewById(R.id.tv_hall);
        tv_user = (TextView) findViewById(R.id.tv_user);
        tv_shop.setOnClickListener(this);
        tv_hall.setOnClickListener(this);
        tv_user.setOnClickListener(this);
    }



    /**
     * 按钮点击事件
     */
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.tv_shop:
//                  controller.showFragment(0);
//                break;
//            case R.id.tv_hall:
//                controller.showFragment(1);
//                break;
//            case R.id.tv_user:
//                if(UserInfo.getInstance()==null){
//                    Intent i = new Intent(this, LoginActivity.class);
//                    //i.putExtra("TAG",Config.USER_TAG);
//                    startActivity(i);
//
//                }else{
//                    controller.showFragment(2);
//                }
//                break;
//        }
//    }


    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (v.getId()){
            case R.id.tv_shop:
                fragmentTransaction.replace(R.id.ly_content, shopFragment);

                break;
            case R.id.tv_hall:
                fragmentTransaction.replace(R.id.ly_content, hallFragment);
                break;
            case R.id.tv_user:
                if(UserInfo.getInstance()==null){
                    Intent i = new Intent(this, LoginActivity.class);
                    //i.putExtra("TAG",Config.USER_TAG);
                    startActivity(i);
                    finish();
                }else{
//                    controller.showFragment(2);
                    fragmentTransaction.replace(R.id.ly_content,userFragment);
                }



                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.e("TAG","Mainactivity.pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.e("onstop","Mainactivity");
    }
}
