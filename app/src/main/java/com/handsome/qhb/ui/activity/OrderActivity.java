package com.handsome.qhb.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handsome.qhb.adapter.OrderAdapter;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.Order;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.listener.OnRefreshListener;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.UserInfo;
import com.handsome.qhb.widget.RefreshListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/14.
 */
public class OrderActivity extends BaseActivity {


    //refreshListView
    private RefreshListView refreshListView;
    //orderList
    private List<Order> orderList ;

    private Gson gson = new Gson();
    //订单分页Json
    private JSONObject pageJson;
    //订单页数
    private int page;
    //订单下一页
    private String nextpage;

    private TextView tv_title;

    private LinearLayout ll_back;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==Config.ORDER_MESSAGE){
                LogUtils.e("--->0x127", "orders");
                initOrderListView();
            }else if(msg.what == Config.REFRESH_ORDER){
                LogUtils.e("----->0x128","ordersrefresh");
                initOrderListView();
                refreshListView.hideHeaderView();
            }else if(msg.what == Config.LOADMORE_ORDER){
                LogUtils.e("---->0x129","loadmore");
                initOrderListView();
                refreshListView.hideFooterView();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ll_back = (LinearLayout)findViewById(R.id.ll_back);

        refreshListView = (RefreshListView) findViewById(R.id.refreshlistview);

        tv_title.setText("订单管理");
        if(UserInfo.getInstance()==null){
            Intent i = new Intent(OrderActivity.this,LoginActivity.class);
            i.putExtra("TAG",Config.ORDER_TAG);
            startActivity(i);
            return;
        }
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.BASE_URL+"Order/getJson",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if(status.equals("0")){
                                Toast.makeText(OrderActivity.this, jsonObject.getString("info"), Toast.LENGTH_LONG).show();
                                return;
                            }
                            Message msg = new Message();
                            msg.what = Config.ORDER_MESSAGE;
                            JSONObject jsonObjectdata = new JSONObject(jsonObject.getString("data"));
                            orderList= new ArrayList<Order>();
                            orderList = gson.fromJson(jsonObjectdata.getString("orders"), new TypeToken<List<Order>>() {
                            }.getType());
                            if(orderList!=null){
                                for(Order order:orderList){
                                    LogUtils.e("orderlist",order.toString());}
                            }
                            pageJson = new JSONObject(jsonObjectdata.getString("page"));
                            page = Integer.valueOf(pageJson.getString("nums"));
                            nextpage = pageJson.getString("next");
                            handler.handleMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                Toast.makeText(OrderActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid",String.valueOf(UserInfo.getInstance().getUid()));
                map.put("token",UserInfo.getInstance().getToken());
                return map;
            }
        };
        stringRequest.setTag(Config.GETORDER_TAG);
        MyApplication.getmQueue().add(stringRequest);
    }

    public void initOrderListView(){
         OrderAdapter orderAdapter = new OrderAdapter(this,orderList,R.layout.order_list_items,MyApplication.getmQueue());
        if(orderAdapter!=null){
            refreshListView.setAdapter(orderAdapter);
            refreshListView.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onDownPullRefresh() {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.BASE_URL+"Order/getJson",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String status = jsonObject.getString("status");
                                        if(status.equals("0")){
                                            Toast.makeText(OrderActivity.this, jsonObject.getString("info"), Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        Message msg = new Message();
                                        msg.what = Config.REFRESH_ORDER;
                                        if(orderList!=null){
                                            orderList.clear();
                                        }
                                        JSONObject jsonObjectdata = new JSONObject(jsonObject.getString("data"));
                                        if(jsonObjectdata==null){
                                            return;
                                        }
                                        orderList = gson.fromJson(jsonObjectdata.getString("orders"),new TypeToken<List<Order>>(){}.getType());
                                        pageJson = new JSONObject(jsonObjectdata.getString("page"));
                                        nextpage = pageJson.getString("next");
                                        page = Integer.valueOf(pageJson.getString("nums"));
                                        handler.handleMessage(msg);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TAG", error.getMessage(), error);
                            Toast.makeText(OrderActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("uid",String.valueOf(UserInfo.getInstance().getUid()));
                            map.put("token",UserInfo.getInstance().getPassword());
                            return map;
                        }
                    };
                    stringRequest.setTag(Config.GETORDER_TAG);
                    MyApplication.getmQueue().add(stringRequest);
                }
                @Override
                public void onLoadingMore() {
                    if (page <= 1) {
                        refreshListView.hideFooterView();
                        return;
                    } else {
                        page--;
                        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.BASE_URL+"Order/getJson",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String status = jsonObject.getString("status");
                                            if(status.equals("0")){
                                                Toast.makeText(OrderActivity.this, jsonObject.getString("info"), Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                            JSONObject jsonObjectdata = new JSONObject(jsonObject.getString("data"));
                                            if (jsonObjectdata.getString("orders").equals("")) {
                                                return;
                                            }
                                            Message msg = new Message();
                                            msg.what = Config.LOADMORE_ORDER;
                                            List<Order> nextOrders = new ArrayList<Order>();
                                            nextOrders = gson.fromJson(jsonObjectdata.getString("orders"), new TypeToken<List<Order>>() {
                                            }.getType());
                                            for (Order order : nextOrders) {
                                                orderList.add(order);
                                            }
                                            pageJson = new JSONObject(jsonObjectdata.getString("page"));
                                            nextpage = pageJson.getString("next");
                                            handler.handleMessage(msg);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("TAG", error.getMessage(), error);
                                Toast.makeText(OrderActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("uid",String.valueOf(UserInfo.getInstance().getUid()));
                                map.put("token",UserInfo.getInstance().getPassword());
                                return map;
                            }
                        };
                        stringRequest.setTag(Config.GETORDER_TAG);
                        MyApplication.getmQueue().add(stringRequest);
                    }
                    }

            });
        }

    }
}
