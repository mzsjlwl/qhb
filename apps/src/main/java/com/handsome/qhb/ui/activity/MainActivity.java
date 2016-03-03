package com.handsome.qhb.ui.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handsome.qhb.ui.fragment.CycleFragment;
import com.handsome.qhb.ui.fragment.MeFragment;
import com.handsome.qhb.ui.fragment.ShopFragment;

import tab.com.handsome.handsome.R;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    Fragment MeFragment, shopFragment, cycleFragment;//3个碎片布局
    LinearLayout ll_content;//碎片放置的容器
    TextView tv_me, tv_cycle, tv_shop;//3个碎片切换按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initFragment();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        ll_content = (LinearLayout)findViewById(R.id.id_ll_content);
        tv_me = (TextView) findViewById(R.id.id_tv_me);
        tv_cycle = (TextView) findViewById(R.id.id_tv_cycle);
        tv_shop = (TextView) findViewById(R.id.id_tv_shop);
        tv_me.setOnClickListener(this);
        tv_cycle.setOnClickListener(this);
        tv_shop.setOnClickListener(this);
    }

    /**
     * 初始化碎片
     */
    private void initFragment() {
        shopFragment = new ShopFragment();
        MeFragment = new MeFragment();
        cycleFragment = new CycleFragment();
        replaceFragment(cycleFragment);
    }

    /**
     * 按钮点击事件
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_tv_me:
                replaceFragment(MeFragment);
                break;
            case R.id.id_tv_cycle:
                replaceFragment(cycleFragment);
                break;
            case R.id.id_tv_shop:
                replaceFragment(shopFragment);
                break;
            default:
                break;
        }
    }

    /**
     * 切换碎片
     *
     * @param f 碎片
     */
    private void replaceFragment(Fragment f) {
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.replace(R.id.id_ll_content, f).commit();
    }

}
