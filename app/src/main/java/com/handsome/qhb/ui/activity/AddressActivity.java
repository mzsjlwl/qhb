package com.handsome.qhb.ui.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handsome.qhb.adapter.AddressAdapter;
import com.handsome.qhb.bean.Address;
import com.handsome.qhb.bean.Product;
import com.handsome.qhb.db.UserDAO;
import com.handsome.qhb.db.UserDBOpenHelper;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.RequestQueueController;
import com.handsome.qhb.utils.UserInfo;

import java.util.ArrayList;
import java.util.List;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/14.
 */
public class AddressActivity extends BaseActivity{
    //返回
    private ImageButton ib_back;
    //添加收货地址
    private TextView tv_add;
    //ListView
    private ListView lv_address;

    private SQLiteDatabase db;

    private Gson gson = new Gson();
    private List<Address> addressList = new ArrayList<Address>();
    private int size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        tv_add = (TextView)findViewById(R.id.tv_add);
        lv_address = (ListView) findViewById(R.id.lv_address);
        db = UserDBOpenHelper.getInstance(this).getWritableDatabase();
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddressActivity.this,AddressManagerActivity.class);
                if(getIntent().getStringExtra("TAG")!=null&&getIntent().getStringExtra("TAG").equals("GWC")){
                    i.putExtra("TAG","GWC");
                }
                i.putExtra("aid",String.valueOf(size));
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //本地数据表获取
        addressList = gson.fromJson(UserDAO.findAddress(db, UserInfo.getInstance().getUid()),
                new TypeToken<List<Address>>() {
                }.getType());

        //删除
        if(getIntent().getStringExtra("delete")!=null){
            LogUtils.e("==>","delete");
            int aid = Integer.valueOf(getIntent().getStringExtra("delete"));
            if(addressList!=null){
                for(int i = 0;i<addressList.size();i++){
                    if(addressList.get(i).getAid()==aid){
                        addressList.remove(i);
                        break;
                    }
                }
            }
        }
        //增加与修改
        if(getIntent().getSerializableExtra("address")!=null){
            LogUtils.e("AddressManager","fdsfsad");
            Address address = (Address) getIntent().getSerializableExtra("address");
            LogUtils.e("address.aid",String.valueOf(address.getAid()));
            LogUtils.e("address.receName",address.getReceName());
            if(addressList!=null){
                int i;
                for( i=0;i<addressList.size();i++){
                    //如果有aid,则更新
                    if(addressList.get(i).getAid()==address.getAid()){
                        addressList.get(i).setReceAddr(address.getReceAddr());
                        addressList.get(i).setReceName(address.getReceName());
                        addressList.get(i).setRecePhone(address.getRecePhone());
                        break;
                    }
                }
                //没有对应aid,则增加记录
                if(i==addressList.size()){
                    addressList.add(address);
                }
            }else{
                addressList= new ArrayList<Address>();
                addressList.add(address);
            }
        }

        //装载到ListView
        if(addressList!=null){
            //获取list中最后一条的aid再加一
            size = addressList.get(addressList.size()-1).getAid()+1;
            LogUtils.e("size",String.valueOf(size));
            AddressAdapter addressAdapter = new AddressAdapter(this,addressList,R.layout.address_list_items, RequestQueueController.getInstance());
            lv_address.setAdapter(addressAdapter);
        }else{
            size = 0;
            LogUtils.e("size", String.valueOf(size));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        UserDAO.updateAddress(db, UserInfo.getInstance().getUid(), gson.toJson(addressList));
        LogUtils.e("addressActivity","onStop");
    }
}
