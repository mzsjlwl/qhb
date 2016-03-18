package com.handsome.qhb.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.handsome.qhb.bean.User;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.utils.RequestQueueController;
import com.handsome.qhb.utils.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/14.
 */

public class UpdatePasswordActivity extends BaseActivity {
    //标题
    private TextView tv_title;
    //返回键
    private ImageButton ib_back;
    //旧密码
    private EditText et_oldPassword;
    //新密码
    private EditText et_password;
    //重复密码
    private EditText et_repeatPassword;
    //makesure
    private TextView tv_makesure;
    //back
    private TextView tv_back;
    //Gson
    private Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ib_back = (ImageButton) findViewById(R.id.ib_back);

        et_oldPassword = (EditText)findViewById(R.id.et_oldPassword);
        et_password =(EditText) findViewById(R.id.et_password);
        et_repeatPassword=(EditText) findViewById(R.id.et_repeatPassword);
        tv_makesure = (TextView)findViewById(R.id.tv_makesure);
        tv_back = (TextView)findViewById(R.id.tv_back);

        tv_title.setText("修改密码");
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        tv_makesure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_password.getText().toString().equals(et_repeatPassword.getText().toString())){
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.BASE_URL+"User/update",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String status = jsonObject.getString("status");
                                        if(status == "0"){
                                            Toast.makeText(UpdatePasswordActivity.this, jsonObject.getString("info"), Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        Toast.makeText(UpdatePasswordActivity.this,"修改成功,请重新登录",Toast.LENGTH_LONG).show();
                                        Intent i =new Intent(UpdatePasswordActivity.this,LoginActivity.class);
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
                            Toast.makeText(UpdatePasswordActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("uid", String.valueOf(UserInfo.getInstance().getUid()));
                            map.put("oldPassword",et_oldPassword.getText().toString());
                            map.put("password",et_password.getText().toString());
                            map.put("repeatPassword",et_repeatPassword.getText().toString());
                            return map;
                        }
                    };
                    RequestQueueController.getInstance().add(stringRequest);
                }
            }
        });


    }
}
