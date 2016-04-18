package com.handsome.qhb.ui.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.User;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.ui.activity.AddMoneyActivity;
import com.handsome.qhb.ui.activity.AddressActivity;
import com.handsome.qhb.ui.activity.LoginActivity;
import com.handsome.qhb.ui.activity.MainActivity;
import com.handsome.qhb.ui.activity.OrderActivity;
import com.handsome.qhb.ui.activity.UpdateDataActivity;
import com.handsome.qhb.ui.activity.UpdatePasswordActivity;
import com.handsome.qhb.ui.activity.UpdatePhotoActivity;
import com.handsome.qhb.utils.ImageUtils;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.MD5Utils;
import com.handsome.qhb.utils.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import tab.com.handsome.handsome.R;


/**
 * Created by zhang on 2016/2/20.
 */

public class UserFragment extends Fragment {
    //用户名
    private TextView tv_name;
    //积分
    private TextView tv_integral;
    //修改资料
    private LinearLayout ll_update_data;
    //修改密码
    private LinearLayout ll_update_password;
    //充值
    private LinearLayout ll_addMoney;
    //订单管理
    private LinearLayout ll_order_manager;
    //收货地址
    private LinearLayout ll_address;
    //退出
    private LinearLayout ll_logout;
    //头像
    private ImageView iv_user_photo;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==Config.REFRESH_USER){
                ImageUtils.imageLoader(MyApplication.getmQueue(), UserInfo.getInstance().getPhoto(), iv_user_photo);
                tv_name.setText(UserInfo.getInstance().getNackname().toString());
                tv_integral.setText(String.valueOf(UserInfo.getInstance().getIntegral()));
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user,container,false);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_integral = (TextView)view.findViewById(R.id.tv_integral);
        if(UserInfo.getInstance()!=null) {
            tv_name.setText(UserInfo.getInstance().getNackname().toString());
            tv_integral.setText(String.valueOf(UserInfo.getInstance().getIntegral()));
        }

        ll_update_data = (LinearLayout) view.findViewById(R.id.ll_update_data);
        ll_update_password = (LinearLayout) view.findViewById(R.id.ll_update_password);
        ll_addMoney = (LinearLayout)view.findViewById(R.id.ll_addMoney);
        ll_order_manager = (LinearLayout)view.findViewById(R.id.ll_order_manager);
        ll_address = (LinearLayout)view.findViewById(R.id.ll_address);
        ll_logout = (LinearLayout)view.findViewById(R.id.ll_logout);
        iv_user_photo = (ImageView)view.findViewById(R.id.iv_user_photo);
        if(UserInfo.getInstance()!=null) {
            ImageUtils.imageLoader(MyApplication.getmQueue(), UserInfo.getInstance().getPhoto(), iv_user_photo);
        }
            iv_user_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UpdatePhotoActivity.class);
                startActivity(i);
            }
        });
        ll_update_data.setOnClickListener(
               new View.OnClickListener(){

                   @Override
                   public void onClick(View view) {
                       Intent i = new Intent(getActivity(), UpdateDataActivity.class);
                       startActivity(i);
                   }
               }
        );
        ll_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UpdatePasswordActivity.class);
                startActivity(i);
            }
        });

        ll_addMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddMoneyActivity.class);
                startActivity(i);
            }
        });

        ll_order_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), OrderActivity.class);
                startActivity(i);
            }
        });

        ll_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddressActivity.class);
                startActivity(i);
            }
        });

        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage("确定退出吗?");
                alertDialog.setCancelable(true);
                alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserInfo.setUser(null);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alertDialog.show();

            }
        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        LogUtils.e("Userfragment", "onstart");

    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.e("Userfragment", "onResume");
        StringRequest stringRequest1= new StringRequest(Request.Method.POST, Config.BASE_URL+"User/getInfo",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            LogUtils.e("response", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if(status.equals("0")){
                                if(UserInfo.getInstance()!=null){
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.BASE_URL+"User/login",
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        LogUtils.e("response-zidongdenglu",response);
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        String status = jsonObject.getString("status");
                                                        if(status.equals("0")){
                                                            return;
                                                        }
                                                        User user =  MyApplication.getGson().fromJson(jsonObject.getString("data"),User.class);
                                                        UserInfo.setUser(user);
                                                        SharedPreferences.Editor editor =getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE).edit();
                                                        editor.clear();
                                                        editor.putString("user", jsonObject.getString("data"));
                                                        editor.putLong("date", new Date().getTime());
                                                        editor.commit();
                                                        Message message = new Message();
                                                        message.what=Config.REFRESH_USER;
                                                        handler.handleMessage(message);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("TAG", error.getMessage(), error);
                                        }
                                    }){
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> map = new HashMap<String, String>();
                                            map.put("username",UserInfo.getInstance().getUsername());
                                            map.put("password",UserInfo.getInstance().getPassword());
                                            return map;
                                        }
                                    };
                                    MyApplication.getmQueue().add(stringRequest);
                                }
                                return;
                            }
                            User user =  MyApplication.getGson().fromJson(jsonObject.getString("data"),User.class);
                            UserInfo.setUser(user);
                            SharedPreferences.Editor editor =getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE).edit();
                            editor.clear();
                            editor.putString("user", jsonObject.getString("data"));
                            editor.putLong("date", new Date().getTime());
                            editor.commit();
                            Message message = new Message();
                            message.what=Config.REFRESH_USER;
                            handler.handleMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid",String.valueOf(UserInfo.getInstance().getUid()));
                map.put("token",String.valueOf(UserInfo.getInstance().getToken()));
                return map;
            }
        };
        stringRequest1.setTag(Config.USERLOGIN_TAG);
        MyApplication.getmQueue().add(stringRequest1);
    }

    @Override
    public void onStop() {
        super.onStop();
        MyApplication.getmQueue().cancelAll(Config.USERLOGIN_TAG);
    }
}
