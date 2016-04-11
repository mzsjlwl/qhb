package com.handsome.qhb.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handsome.qhb.adapter.OrderItemAdapter;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.Order;
import com.handsome.qhb.bean.Products;
import com.handsome.qhb.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/14.
 */
public class OrderDetailActivity extends BaseActivity{

    private TextView tv_title;
    //Order
    private Order order = new Order();
    //shopcarListView
    private ListView lv_products;
    //总价格
    private TextView tv_totalMoney;
    //状态
    private TextView tv_orderStatus;
    //订单Id
    private TextView tv_orderId;
    //下单时间
    private TextView tv_orderTime;
    //收货人
    private TextView tv_receName;
    //联系方式
    private TextView tv_recePhone;
    //地址
    private TextView tv_receAddr;
    //返回
    private LinearLayout ll_back;
    //Gson
    private Gson gson = new Gson();
    //productsList
    private List<Products> productsList = new ArrayList<Products>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        tv_title = (TextView) findViewById(R.id.tv_title);
        lv_products = (ListView) findViewById(R.id.lv_products);

        tv_totalMoney = (TextView)findViewById(R.id.tv_totalMoney);
        tv_orderStatus = (TextView)findViewById(R.id.tv_orderStatus);
        tv_orderId = (TextView)findViewById(R.id.tv_orderId);
        tv_orderTime = (TextView)findViewById(R.id.tv_orderTime);
        tv_receName = (TextView)findViewById(R.id.tv_receName);
        tv_recePhone = (TextView)findViewById(R.id.tv_recePhone);
        tv_receAddr = (TextView)findViewById(R.id.tv_receAddr);
        ll_back = (LinearLayout)findViewById(R.id.ll_back);
        tv_title.setText("订单详情");
        if(getIntent().getSerializableExtra("order")!=null){
            order = (Order) getIntent().getSerializableExtra("order");
            String totalMoney = getIntent().getStringExtra("totalMoney");
            List<Products> productsList = new ArrayList<Products>();
            productsList = gson.fromJson(order.getProducts(),new TypeToken<List<Products>>(){}.getType());
            OrderItemAdapter orderItemAdapter = new OrderItemAdapter(this,productsList,R.layout.gwc_list_items, MyApplication.getmQueue());
            lv_products.setAdapter(orderItemAdapter);
            tv_totalMoney.setText(totalMoney);
            if(order.getState()==0){
                tv_orderStatus.setText("待收货");
            }else{
                tv_orderStatus.setText("已完成");
            }
            tv_orderId.setText(String.valueOf(order.getOid()));
            tv_orderTime.setText(order.getTime());
            tv_receName.setText(order.getReceName());
            tv_recePhone.setText(order.getRecePhone());
            tv_receAddr.setText(order.getReceAddr());
        }
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
