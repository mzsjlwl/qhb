package com.handsome.qhb.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.utils.LogUtils;
import com.tencent.smtt.sdk.WebView;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/4/27.
 */
public class WelcomeActivity extends  BaseActivity  {

    private ImageView iv_welcome;
    private WebView wv_updateapp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        iv_welcome = (ImageView) findViewById(R.id.iv_welcome);
        wv_updateapp = (WebView) findViewById(R.id.wv_updateapp);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.BASE_URL+Config.UPDATEVERSION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            PackageManager packageManager = getPackageManager();
                            PackageInfo packInfo=packageManager.getPackageInfo(getPackageName(), 0);
                            String version = packInfo.versionName;
                            LogUtils.e("response====>",response);
                            LogUtils.e("version====>",version);
                            if(version.equals(response)){
                                Intent i = new Intent(WelcomeActivity.this,MainActivity.class);
                                startActivity(i);
                                finish();

                            }else{
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(WelcomeActivity.this);
                                alertDialog.setIcon(R.mipmap.logo)
                                        .setMessage("是否下载最新版本")
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(WelcomeActivity.this,MainActivity.class);
                                                startActivity(i);
                                                finish();

                                            }
                                        })
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                iv_welcome.setVisibility(View.INVISIBLE);
                                                wv_updateapp.loadUrl(Config.BASE_URL+Config.DOWNLOADAPP_URL);
                                            }
                                        });
                                alertDialog.show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        stringRequest.setTag(Config.USERLOGIN_TAG);
        MyApplication.getmQueue().add(stringRequest);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }




}
