package com.handsome.qhb.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;


import java.util.ArrayList;

/**
 * Created by zhang on 2016/3/4.
 */
public class FragmentController {
    private int containerId;
    private FragmentManager fm;
    private ArrayList<Fragment> fragments;
    private static FragmentController controller;

    public static FragmentController getInstance(Activity activity,int containerId){
        if(controller ==null){
            controller = new FragmentController(activity,containerId);
        }
        return controller;
    }
    private FragmentController(Activity activity,int containerId){
        this.containerId = containerId;
        this.fm = activity.getFragmentManager();
        initFragment();
    }

    private void initFragment() {
        fragments = new ArrayList<Fragment>();
        fragments.add(new IndexFragment());
        fragments.add(new SearchFragment());
        fragments.add(new CommunityFragment());
        fragments.add(new ShopCarFragment());
        fragments.add(new UserFragment());

        FragmentTransaction ft = fm.beginTransaction();
        for(Fragment fragment : fragments) {
            ft.add(containerId, fragment);
        }
        ft.commit();
    }

    public void showFragment(int position) {
        hideFragments();
        Fragment fragment = fragments.get(position);
        FragmentTransaction ft = fm.beginTransaction();
        ft.show(fragment);
        ft.commit();
    }

    public void hideFragments() {
        FragmentTransaction ft = fm.beginTransaction();
        for(Fragment fragment : fragments) {
            if(fragment != null) {
                ft.hide(fragment);
            }
        }
        ft.commit();
    }

    public Fragment getFragment(int position) {
        return fragments.get(position);
    }
}
