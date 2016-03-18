package com.handsome.qhb.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;


import com.handsome.qhb.utils.LogUtils;

import java.util.ArrayList;

/**
 * Created by zhang on 2016/3/4.
 */
public class FragmentController {
    private int containerId;
    private FragmentManager fm;
    private ArrayList<Fragment> fragments;
    private static FragmentController controller;

//    public static FragmentController getInstance(Activity activity,int containerId,int flag){
//        if(controller ==null){
//            controller = new FragmentController(activity,containerId,flag);
//        }
//        return controller;
//    }
    public FragmentController(Activity activity,int containerId,int flag){
        this.containerId = containerId;
        this.fm = activity.getFragmentManager();
        if(flag==0){
            initFragment();
        }else{
            findFragment();
        }

    }

    private void initFragment() {
        fragments = new ArrayList<Fragment>();
        fragments.add(new ShopFragment());
        fragments.add(new HallFragment());
        fragments.add(new UserFragment());
        LogUtils.e("initFragment",">>>>>>");
        FragmentTransaction ft = fm.beginTransaction();
        for(int i = 0;i<fragments.size();i++) {
            ft.add(containerId,fragments.get(i),"TAG"+i);
        }
        ft.commitAllowingStateLoss();
    }

    private void findFragment(){
//        FragmentTransaction ft = fm.beginTransaction();
        fragments = new ArrayList<Fragment>();
        for(int i = 0;i<3;i++) {
            fragments.add(fm.findFragmentByTag("TAG"+i));
        }
//        for(int i = 0;i<fragments.size();i++) {
//            ft.add(containerId,fragments.get(i),"TAG"+i);
//        }
//        ft.commit();
    }
    public void showFragment(int position) {
        hideFragments();
        Fragment fragment = fragments.get(position);
        FragmentTransaction ft = fm.beginTransaction();
        ft.show(fragment);
        ft.commitAllowingStateLoss();
    }

    public void hideFragments() {
        FragmentTransaction ft = fm.beginTransaction();
        for(int i = 0;i<fragments.size();i++) {
            Fragment fragment = fragments.get(i);
            ft.hide(fragment);
        }
        ft.commitAllowingStateLoss();
    }

    public Fragment getFragment(int position) {
        return fragments.get(position);
    }
}
