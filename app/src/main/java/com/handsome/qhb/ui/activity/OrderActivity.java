package com.handsome.qhb.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.util.List;

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

    private ImageButton ib_back;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x127){
                LogUtils.e("--->0x127", "orders");
                initOrderListView();
            }else if(msg.what == 0x128){
                LogUtils.e("----->0x128","ordersrefresh");
                initOrderListView();
                refreshListView.hideHeaderView();
            }else if(msg.what == 0x129){
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
        ib_back = (ImageButton)findViewById(R.id.ib_back);

        refreshListView = (RefreshListView) findViewById(R.id.refreshlistview);

        tv_title.setText("订单管理");
        if(UserInfo.getInstance()==null){
            Intent i = new Intent(OrderActivity.this,LoginActivity.class);
            i.putExtra("TAG",Config.ORDER_TAG);
            startActivity(i);
            return;
        }
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Config.BASE_URL + "Order/getJson/uid/"+UserInfo.getInstance().getUid(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        LogUtils.e("response","000000");
                        Message msg = new Message();
                        msg.what = 0x127;
                        try {
                            LogUtils.e("order",response.toString());
                            orderList= new ArrayList<Order>();
                            orderList = gson.fromJson(response.getString("orders"), new TypeToken<List<Order>>() {
                            }.getType());
                            if(orderList!=null){
                                for(Order order:orderList){
                                LogUtils.e("orderlist",order.toString());}
                            }
                            pageJson = new JSONObject(response.getString("page"));
                            page = Integer.valueOf(pageJson.getString("nums"));
                            nextpage = pageJson.getString("next");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        handler.handleMessage(msg);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        MyApplication.getmQueue().add(jsonObjectRequest);
    }

    public void initOrderListView(){
         OrderAdapter orderAdapter = new OrderAdapter(this,orderList,R.layout.order_list_items,MyApplication.getmQueue());
        if(orderAdapter!=null){
            refreshListView.setAdapter(orderAdapter);
            refreshListView.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onDownPullRefresh() {
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Config.BASE_URL + "Order/getJson/uid/"+UserInfo.getInstance().getUid(), null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Message msg = new Message();
                                    msg.what = 0x128;
                                    try {
                                        orderList.clear();
                                        orderList = gson.fromJson(response.getString("orders"),new TypeToken<List<Order>>(){}.getType());
                                        pageJson = new JSONObject(response.getString("page"));
                                        nextpage = pageJson.getString("next");
                                        page = Integer.valueOf(pageJson.getString("nums"));
                                        LogUtils.e("===", pageJson.getString("next"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    handler.handleMessage(msg);
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TAG", error.getMessage(), error);
                        }
                    });
                    MyApplication.getmQueue().add(jsonObjectRequest);
                }
                @Override
                public void onLoadingMore() {
                    if (page <= 1) {
                        refreshListView.hideFooterView();
                        return;
                    } else {
                        page--;
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(nextpage, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Message msg = new Message();
                                        msg.what = 0x129;
                                        try {
                                            LogUtils.e("order",response.toString());
                                            if (response.getString("orders") == "") {
                                                return;
                                            }
                                            List<Order> nextOrders = new ArrayList<Order>();
                                            nextOrders = gson.fromJson(response.getString("orders"), new TypeToken<List<Order>>() {
                                            }.getType());
                                            for (Order order : nextOrders) {
                                                orderList.add(order);
                                            }
                                            pageJson = new JSONObject(response.getString("page"));
                                            nextpage = pageJson.getString("next");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        handler.handleMessage(msg);
                                    }
                                }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("TAG", error.getMessage(), error);
                            }
                        });
                        MyApplication.getmQueue().add(jsonObjectRequest);
                    }
                    }

            });
        }

    }
}
