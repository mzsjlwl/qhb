package com.handsome.qhb.ui.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.handsome.qhb.bean.Slider;
import com.handsome.qhb.ui.fragment.CommunityFragment;
import com.handsome.qhb.ui.fragment.IndexFragment;
import com.handsome.qhb.ui.fragment.SearchFragment;
import com.handsome.qhb.ui.fragment.ShopCarFragment;
import com.handsome.qhb.ui.fragment.UserFragment;

import java.util.List;

import tab.com.handsome.handsome.R;


public class MainActivity extends Activity implements View.OnClickListener{

    Fragment indexFragment,searchFragment,communityFragment,shopCarFragment,userFragment;
    TextView tv_index,tv_search,tv_community,tv_shopcar,tv_user;
    FrameLayout ly_content;
    public static List<Slider> sliderLists = null  ;

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
    private void initViews(){
        ly_content = (FrameLayout) findViewById(R.id.ly_content);
        tv_index = (TextView) findViewById(R.id.tv_index);
        tv_search = (TextView) findViewById(R.id.tv_search);
        tv_community = (TextView) findViewById(R.id.tv_community);
        tv_shopcar = (TextView) findViewById(R.id.tv_shopcar);
        tv_user = (TextView) findViewById(R.id.tv_user);
        tv_index.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        tv_community.setOnClickListener(this);
        tv_shopcar.setOnClickListener(this);
        tv_user.setOnClickListener(this);

    }

    /**
     * 初始化碎片
     */
    private void initFragment() {
        indexFragment = new IndexFragment();
        searchFragment = new SearchFragment();
        communityFragment = new CommunityFragment();
        shopCarFragment = new ShopCarFragment();
        userFragment = new UserFragment();
        replaceFragment(indexFragment);
    }

    /**
     * 按钮点击事件
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_index:
                replaceFragment(indexFragment);
                break;
            case R.id.tv_search:
                replaceFragment(searchFragment);
                break;
            case R.id.tv_community:
                replaceFragment(communityFragment);
                break;
            case R.id.tv_shopcar:
                replaceFragment(shopCarFragment);
                break;
            case R.id.tv_user:
                replaceFragment(userFragment);
                break;
        }
    }

    /**
     * 切换碎片
     * @param fragement
     */
    private void replaceFragment(Fragment fragement){
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.replace(R.id.ly_content, fragement).commit();
    }

}
