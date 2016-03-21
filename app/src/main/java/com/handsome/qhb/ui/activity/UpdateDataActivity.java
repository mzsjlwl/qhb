package com.handsome.qhb.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handsome.qhb.bean.IdNum;
import com.handsome.qhb.bean.OrderJson;
import com.handsome.qhb.bean.Product;
import com.handsome.qhb.bean.User;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.db.UserDAO;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.RequestQueueController;
import com.handsome.qhb.utils.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/14.
 */

public class UpdateDataActivity extends BaseActivity {
   //返回键
    private ImageButton ib_back;
    //修改用户名
    private EditText et_nackname;
    //makesure
    private TextView tv_makesure;
    //用户头像
    private ImageView iv_user_photo;



    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        et_nackname = (EditText)findViewById(R.id.et_nackname);
        tv_makesure = (TextView)findViewById(R.id.tv_makesure);
        iv_user_photo = (ImageView)findViewById(R.id.iv_user_photo);

        et_nackname.setText(UserInfo.getInstance().getNackname());

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_makesure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "User/update",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    if (status == "0") {
                                        Toast.makeText(UpdateDataActivity.this, jsonObject.getString("info"), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    Toast.makeText(UpdateDataActivity.this, jsonObject.getString("info"), Toast.LENGTH_LONG).show();
                                    LogUtils.e("udpateUser", jsonObject.getString("data"));
                                    UserInfo.setUser((User) (gson.fromJson(jsonObject.getString("data"), new TypeToken<User>() {
                                    }.getType())));
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(UpdateDataActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("uid", String.valueOf(UserInfo.getInstance().getUid()));
                        map.put("nackname", et_nackname.getText().toString());
                        return map;
                    }
                };
                RequestQueueController.getInstance().add(stringRequest);
            }
        });

    }
}
