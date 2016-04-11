package com.handsome.qhb.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/4/6.
 */
public class AddMoneyActivity extends BaseActivity{

    private LinearLayout ll_back;
    private TextView tv_notice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmoney);
        ll_back =(LinearLayout)findViewById(R.id.ll_back);
        tv_notice = (TextView)findViewById(R.id.tv_notice);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+"User/notice",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            LogUtils.e("response", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if(status == "0"){
                                Toast.makeText(AddMoneyActivity.this, jsonObject.getString("info"), Toast.LENGTH_LONG).show();
                                return;
                            }
                            tv_notice.setText(jsonObject.getString("data"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                Toast.makeText(AddMoneyActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        MyApplication.getmQueue().add(stringRequest);
    }
}
