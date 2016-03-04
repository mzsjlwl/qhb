package com.handsome.qhb.ui.fragment;

/**
 * Created by zhang on 2016/3/4.
 */
public class FragmentController {
    private static FragmentController controller;

    public static FragmentController getInstance(){
        if(controller ==null){
            controller = new FragmentController();
        }
        return controller;
    }
    private FragmentController(){

    }
}
