package com.handsome.qhb.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.handsome.qhb.utils.LogUtils;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/4/27.
 */
public class WelcomeActivity extends  BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mHandler.sendEmptyMessageDelayed(GOTO_MAIN_ACTIVITY, 5000);
    }

    protected final int GOTO_MAIN_ACTIVITY = 0;
    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch(msg.what){
                case GOTO_MAIN_ACTIVITY:
                    Intent i = new Intent(WelcomeActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
