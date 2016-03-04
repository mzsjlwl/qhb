package com.handsome.qhb.utils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.handsome.qhb.application.MyApplication;

/**
 * Created by zhang on 2016/3/4.
 */
public class RequestQueueController {
    private static RequestQueue mQueue;

    public static RequestQueue getInstance(){
        if(mQueue == null){
            mQueue = Volley.newRequestQueue(MyApplication.getContext());
        }
        return mQueue;
    }
    private RequestQueueController(){

    }
}
