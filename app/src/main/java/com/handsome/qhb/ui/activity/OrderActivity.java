package com.handsome.qhb.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handsome.qhb.adapter.OrderAdapter;
import com.handsome.qhb.bean.Order;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.RequestQueueController;
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
    //分页json数据
    private JSONObject pageJson;
    //refreshListView
    private RefreshListView refreshListView;
    //orderList
    private List<Order> orderList;

    private Gson gson = new Gson();

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x127){
                LogUtils.e("--->0x127","orders");
                orderList = gson.fromJson(msg.obj.toString(),new TypeToken<List<Order>>(){}.getType());
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        refreshListView = (RefreshListView) findViewById(R.id.refreshlistview);
        orderList = new ArrayList<Order>();
        if(UserInfo.getInstance()==null){
            Intent i = new Intent(OrderActivity.this,LoginActivity.class);
            i.putExtra("TAG",Config.ORDER_TAG);
            startActivity(i);
            return;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Config.BASE_URL + "Order/getJson/uid/"+UserInfo.getInstance().getUid(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Message msg = new Message();
                        msg.what = 0x127;
                        try {
                            msg.obj = response.getString("orders");
                            pageJson = new JSONObject(response.getString("page"));
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

        RequestQueueController.getInstance().add(jsonObjectRequest);

    }

    public void initOrderListView(){
        OrderAdapter orderAdapter = new OrderAdapter(this,orderList,R.layout.order_list_items,RequestQueueController.getInstance());
        refreshListView.setAdapter(orderAdapter);
    }
}
