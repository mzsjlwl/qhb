package com.handsome.qhb.ui.activity;

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
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.utils.MD5Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
    private Matcher m;
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
                m = p.matcher(et_telphone.getText().toString());
                if (!(et_password.getText().toString().equals(et_repeatPassword.getText().toString()))) {
                    Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_LONG).show();
                    return;
                } else if (et_name.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(RegisterActivity.this, "姓名不能为空", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                } else if (et_telphone.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(RegisterActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                } else if (et_password.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                } else if (!m.matches()) {
                    Toast toast = Toast.makeText(RegisterActivity.this, "请输入正确的手机", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                } else if (et_password.getText().toString().length()<6) {
                    Toast toast = Toast.makeText(RegisterActivity.this, "密码不能低于6位", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage("注册中");
                progressDialog.setCancelable(true);
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "User/register",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    if (status == "0") {
                                        progressDialog.dismiss();
                                        Toast toast = Toast.makeText(RegisterActivity.this, jsonObject.getString("info"), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                        return;
                                    }
                                    progressDialog.dismiss();
                                    Toast toast = Toast.makeText(RegisterActivity.this, "注册成功,请登录", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
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
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("username", et_telphone.getText().toString());
                        map.put("password", et_password.getText().toString());
                        map.put("nackname", et_name.getText().toString());
                        return map;
                    }
                };
                stringRequest.setTag(Config.USERREGISTER_TAG);
                MyApplication.getmQueue().add(stringRequest);
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

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getmQueue().cancelAll(Config.USERREGISTER_TAG);
    }
}
