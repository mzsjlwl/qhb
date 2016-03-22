package com.handsome.qhb.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handsome.qhb.adapter.ShopCarAdapter;
import com.handsome.qhb.bean.Address;
import com.handsome.qhb.bean.IdNum;
import com.handsome.qhb.bean.OrderJson;
import com.handsome.qhb.bean.Product;
import com.handsome.qhb.bean.Products;
import com.handsome.qhb.bean.Slider;
import com.handsome.qhb.bean.User;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.db.UserDAO;
import com.handsome.qhb.db.UserDBOpenHelper;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.RequestQueueController;
import com.handsome.qhb.utils.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/13.
 */
public class GwcActivity extends BaseActivity {

    private SQLiteDatabase db;
    private List<Product> shopCarList;
    private ListView listView;
    private ImageButton ib_back;
    private LinearLayout ll_address;
    private List<Address> addressList = new ArrayList<Address>();
    private Gson gson = new Gson();
    private TextView tv_receName;
    private TextView tv_recePhone;
    private TextView tv_receAddr;
    private TextView tv_totalPrice;
    private Button btn_sub;
    private EditText et_liuyan;
    private float totalPrice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gwc);
        //初始化数据表
        db = UserDBOpenHelper.getInstance(this).getWritableDatabase();
        listView = (ListView) findViewById(R.id.ll_gwc);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        tv_receName = (TextView)findViewById(R.id.tv_receName);
        tv_recePhone = (TextView) findViewById(R.id.tv_recePhone);
        tv_receAddr = (TextView) findViewById(R.id.tv_receAddr);
        tv_totalPrice = (TextView)findViewById(R.id.tv_totalPrice);
        btn_sub = (Button)findViewById(R.id.btn_sub);
        et_liuyan = (EditText) findViewById(R.id.et_liuyan);
        shopCarList = (List<Product>)getIntent().getSerializableExtra("shopCarList");

        if(shopCarList!=null) {
            for (int i = 0; i < shopCarList.size(); i++) {
                    totalPrice = totalPrice+shopCarList.get(i).getPrice()*shopCarList.get(i).getNum();
            }
            tv_totalPrice.setText("￥"+String.valueOf(totalPrice));
            ShopCarAdapter shopCarAdapter = new ShopCarAdapter(this,shopCarList,R.layout.gwc_list_items, RequestQueueController.getInstance());
            listView.setAdapter(shopCarAdapter);
        }else{
            shopCarList = gson.fromJson(UserDAO.find(db,UserInfo.getInstance().getUid()),new TypeToken<List<Product>>() {
            }.getType());
            if(shopCarList!=null){
                for (int i = 0; i < shopCarList.size(); i++) {
                    totalPrice = totalPrice+shopCarList.get(i).getPrice()*shopCarList.get(i).getNum();
                }
                tv_totalPrice.setText("￥"+String.valueOf(totalPrice));
                ShopCarAdapter shopCarAdapter = new ShopCarAdapter(this,shopCarList,R.layout.gwc_list_items, RequestQueueController.getInstance());
                listView.setAdapter(shopCarAdapter);
            }
        }

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ll_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GwcActivity.this,AddressActivity.class);
                i.putExtra("TAG","GWC");
                startActivity(i);
                finish();
            }
        });
        btn_sub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(UserInfo.getInstance()==null){
                    return;
                }
                else if(shopCarList.size()<=0){
                    LogUtils.e("shopCarList", "null");
                    Toast toast = Toast.makeText(GwcActivity.this, "你的购物车为空,请添加商品", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    return ;
                }else if(UserInfo.getInstance()!=null&&UserInfo.getInstance().getIntegral()<totalPrice){
                    Toast toast =Toast.makeText(GwcActivity.this, "金额不足,请充值", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    return;
                }else if(tv_receAddr.getText().toString().equals("收货地址")
                        ||tv_recePhone.getText().toString().equals("手机号码")
                        ||tv_receName.getText().toString().equals("收货地址")){
                    Toast toast = Toast.makeText(GwcActivity.this,"请填写收货信息",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return ;

                }
                final ProgressDialog progressDialog = new ProgressDialog(GwcActivity.this);
                progressDialog.setMessage("提交中");
                progressDialog.setCancelable(true);
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+"Order/insert",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    progressDialog.dismiss();
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    if(status == "0"){
                                        Toast.makeText(GwcActivity.this, jsonObject.getString("info"), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    LogUtils.e("data", jsonObject.getString("data"));
                                    UserDAO.update(db, UserInfo.getInstance().getUid(), "");
                                    UserInfo.getInstance().setIntegral(UserInfo.getInstance().getIntegral() - totalPrice);
                                    Toast toast = Toast.makeText(GwcActivity.this,"提交成功",Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                    Intent i = new Intent(GwcActivity.this,MainActivity.class);
                                    i.putExtra("TAG", "ClearGWC");
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                        Toast.makeText(GwcActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        List<IdNum> idNumList = new ArrayList<IdNum>();
                        for(Product p:shopCarList){
                            IdNum idNum = new IdNum();
                            idNum.setId(p.getPid());
                            idNum.setNum(p.getNum());
                            idNumList.add(idNum);
                        }
                        OrderJson orderJson = new OrderJson(UserInfo.getInstance().getUid(),tv_receName.getText().toString()+";"
                                +tv_recePhone.getText().toString()+";"
                                +tv_receAddr.getText().toString(),et_liuyan.getText().toString(),idNumList);
                        LogUtils.e("orderJson",gson.toJson(orderJson));
                        map.put("order",gson.toJson(orderJson));
                        return map;
                    }
                };
                RequestQueueController.getInstance().add(stringRequest);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getIntent().getSerializableExtra("address") != null) {
            Address address = (Address) getIntent().getSerializableExtra("address");
            tv_receAddr.setText(address.getReceAddr());
            tv_recePhone.setText(address.getRecePhone());
            tv_receName.setText(address.getReceName());
        } else {
            addressList = gson.fromJson(UserDAO.findAddress(db, UserInfo.getInstance().getUid()),
                    new TypeToken<List<Address>>() {
                    }.getType());
            if (addressList != null) {
                tv_receAddr.setText(addressList.get(0).getReceAddr());
                tv_recePhone.setText(addressList.get(0).getRecePhone());
                tv_receName.setText(addressList.get(0).getReceName());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
