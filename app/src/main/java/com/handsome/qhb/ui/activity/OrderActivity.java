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
import com.handsome.qhb.listener.MyListener;
import com.handsome.qhb.listener.OnRefreshListener;
import com.handsome.qhb.utils.HttpUtils;
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
public class OrderActivity extends BaseActivity implements MyListener{


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
    private Map<String,String> map;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==Config.ORDER_MESSAGE){
                initOrderListView();
            }else if(msg.what == Config.REFRESH_ORDER){
                initOrderListView();
                refreshListView.hideHeaderView();
            }else if(msg.what == Config.LOADMORE_ORDER){
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
        map = new HashMap<String, String>();
        map.put("uid", String.valueOf(UserInfo.getInstance().getUid()));
        map.put("token", UserInfo.getInstance().getToken());
        HttpUtils.request(OrderActivity.this,Config.GETORDER_URL,this,map,Config.GETORDER_TAG);
    }

    public void initOrderListView(){
         OrderAdapter orderAdapter = new OrderAdapter(this,orderList,R.layout.order_list_items,MyApplication.getmQueue());
        if(orderAdapter!=null){
            refreshListView.setAdapter(orderAdapter);
            refreshListView.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onDownPullRefresh() {
                    HttpUtils.request(OrderActivity.this,Config.GETORDER_URL,OrderActivity.this,map,Config.REFRESHORDER_TAG);
                }
                @Override
                public void onLoadingMore() {
                    if (page <= 1) {
                        refreshListView.hideFooterView();
                        return;
                    } else {
                        page--;
                        HttpUtils.request(OrderActivity.this,nextpage.substring(Config.BASE_URL.length()),
                                OrderActivity.this,map,Config.LOADMOREORDER_TAG);
                    }
                    }

            });
        }

    }

    @Override
    public void dataController(String response, int tag) {
        try {
            switch (tag){
                case Config.GETORDER_TAG:
                    Message msg1 = new Message();
                    msg1.what = Config.ORDER_MESSAGE;
                    JSONObject jsonObjectdata1 = new JSONObject(response);
                    orderList= new ArrayList<Order>();
                    orderList = gson.fromJson(jsonObjectdata1.getString("orders"), new TypeToken<List<Order>>() {
                    }.getType());
                    pageJson = new JSONObject(jsonObjectdata1.getString("page"));
                    page = Integer.valueOf(pageJson.getString("nums"));
                    nextpage = pageJson.getString("next");
                    handler.handleMessage(msg1);
                    break;
                case Config.REFRESHORDER_TAG:
                    Message msg2 = new Message();
                    msg2.what = Config.REFRESH_ORDER;
                    if(orderList!=null){
                        orderList.clear();
                    }
                    JSONObject jsonObjectdata2 = new JSONObject(response);
                    if(jsonObjectdata2==null){
                        return;
                    }
                    orderList = gson.fromJson(jsonObjectdata2.getString("orders"),new TypeToken<List<Order>>(){}.getType());
                    pageJson = new JSONObject(jsonObjectdata2.getString("page"));
                    nextpage = pageJson.getString("next");
                    page = Integer.valueOf(pageJson.getString("nums"));
                    handler.handleMessage(msg2);
                    break;
                case Config.LOADMORE_ORDER:
                    JSONObject jsonObjectdata3 = new JSONObject(response);
                    if (jsonObjectdata3.getString("orders").equals("")) {
                        return;
                    }
                    Message msg3 = new Message();
                    msg3.what = Config.LOADMORE_ORDER;
                    List<Order> nextOrders = new ArrayList<Order>();
                    nextOrders = gson.fromJson(jsonObjectdata3.getString("orders"), new TypeToken<List<Order>>() {
                    }.getType());
                    for (Order order : nextOrders) {
                        orderList.add(order);
                    }
                    pageJson = new JSONObject(jsonObjectdata3.getString("page"));
                    nextpage = pageJson.getString("next");
                    handler.handleMessage(msg3);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
