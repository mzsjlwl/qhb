package com.handsome.qhb.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.handsome.qhb.bean.Slider;
import com.handsome.qhb.ui.fragment.FragmentController;

import java.util.List;

import tab.com.handsome.handsome.R;


public class MainActivity extends BaseActivity implements View.OnClickListener{

    TextView tv_shop,tv_hall,tv_user;
    FrameLayout ly_content;
    private FragmentController controller;
    public static List<Slider> sliderLists = null  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        controller = FragmentController.getInstance(this,R.id.ly_content);
        controller.showFragment(0);
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_shop:
                  controller.showFragment(0);
                break;
            case R.id.tv_hall:
                controller.showFragment(1);
                break;
            case R.id.tv_user:
                controller.showFragment(2);
                break;
        }
    }
}
