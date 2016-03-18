package com.handsome.qhb.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.User;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.RequestQueueController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import tab.com.handsome.handsome.R;

public class LoginActivity extends BaseActivity  {

    //手机号
    private EditText et_telphone;
    //密码
    private EditText et_password;
    //登录
    private TextView tv_login;
    //注册
    private TextView tv_register;

    private Gson gson;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        gson =  new Gson();
        et_telphone = (EditText) findViewById(R.id.et_telphone);
        et_password = (EditText) findViewById(R.id.et_password);

        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_register = (TextView) findViewById(R.id.tv_register);

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.BASE_URL+"User/login",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    if(status == "0"){
                                        Toast.makeText(LoginActivity.this,jsonObject.getString("info"),Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    JSONObject jsonData = new JSONObject(jsonObject.getString("data"));
                                    User user =  gson.fromJson(jsonData.getString("user"),User.class);
                                    Intent i =new Intent(LoginActivity.this,MainActivity.class);
                                    Bundle b = new Bundle();
                                    SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                                    editor.putString("user",jsonData.getString("user"));
                                    editor.putLong("date", new Date().getTime());
                                    editor.commit();
                                    b.putSerializable("user", user);
                                    i.putExtras(b);
                                    startActivity(i);
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("username",et_telphone.getText().toString());
                        map.put("password",et_password.getText().toString());
                        return map;
                    }
                };
                RequestQueueController.getInstance().add(stringRequest);
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });
    }

}
