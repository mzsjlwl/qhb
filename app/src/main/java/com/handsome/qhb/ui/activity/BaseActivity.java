package com.handsome.qhb.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.android.volley.toolbox.RequestFuture;

/**
 * Created by handsome on 2016/3/8.
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
