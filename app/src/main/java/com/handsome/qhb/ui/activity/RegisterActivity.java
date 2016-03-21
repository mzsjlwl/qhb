package com.handsome.qhb.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.utils.RequestQueueController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tab.com.handsome.handsome.R;


/**
 * Created by zhang on 2016/3/13.
 */
public class RegisterActivity extends BaseActivity {
    //姓名
    private EditText et_name;
    //手机号
    private EditText et_telphone;
    //密码
    private EditText et_password;
    //重复密码
    private EditText et_repeatPassword;
    //标题
    private TextView tv_title;
    //确定
    private TextView tv_makesure;
    //返回
    private TextView tv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_name = (EditText)findViewById(R.id.et_name);
        et_telphone = (EditText)findViewById(R.id.et_telphone);
        et_password = (EditText)findViewById(R.id.et_password);
        et_repeatPassword = (EditText)findViewById(R.id.et_repeatPassword);
        tv_makesure = (TextView)findViewById(R.id.tv_makesure);
        tv_back = (TextView)findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);

        tv_title.setText("用户注册");
        tv_makesure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(et_password.getText().toString().equals(et_repeatPassword.getText().toString()))) {
                    Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_LONG).show();
                    return;
                }
                ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage("注册中");
                progressDialog.setCancelable(true);
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+"User/register",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    if(status == "0"){
                                        Toast toast = Toast.makeText(RegisterActivity.this,jsonObject.getString("info"),Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER,0,0);
                                        toast.show();
                                        return;
                                    }
                                    Toast toast = Toast.makeText(RegisterActivity.this,"注册成功,请登录",Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
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
                        Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("username",et_telphone.getText().toString());
                        map.put("password",et_password.getText().toString());
                        map.put("nackname",et_name.getText().toString());
                        return map;
                    }
                };
                RequestQueueController.getInstance().add(stringRequest);
            }

        });

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });


    }
}
