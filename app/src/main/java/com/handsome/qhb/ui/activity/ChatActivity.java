package com.handsome.qhb.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.squareup.okhttp.OkHttpClient;

import tab.com.handsome.handsome.R;

public class ChatActivity extends Activity {

    private OkHttpClient okHttpClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        okHttpClient = new OkHttpClient();

    }

}
