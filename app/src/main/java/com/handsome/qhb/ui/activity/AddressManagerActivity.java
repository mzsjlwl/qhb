package com.handsome.qhb.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handsome.qhb.bean.Address;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/14.
 */
public class AddressManagerActivity extends BaseActivity {

    //收货人
    private EditText receName;
    //联系方式
    private EditText recePhone;
    //地址
    private EditText receAddr;
    //返回键
    private LinearLayout ll_back;
    //地址Id
    private int aid;
    //确定
    private TextView tv_makesure;
    //返回
    private TextView tv_back;
    //删除
    private ImageButton ib_delete;

    private Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
    private Matcher m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manager);

        receName = (EditText) findViewById(R.id.et_receName);
        recePhone = (EditText) findViewById(R.id.et_recePhone);
        receAddr = (EditText) findViewById(R.id.et_receAddr);
        ll_back = (LinearLayout)findViewById(R.id.ll_back);
        tv_makesure = (TextView)findViewById(R.id.tv_makesure);
        tv_back =(TextView)findViewById(R.id.tv_back);
        ib_delete = (ImageButton)findViewById(R.id.ib_delete);


        if(getIntent().getSerializableExtra("address")!=null){
            Address address = (Address) getIntent().getSerializableExtra("address");
            aid = address.getAid();
            receName.setText(address.getReceName());
            recePhone.setText(address.getRecePhone());
            receAddr.setText(address.getReceAddr());
        }else{
            aid= Integer.valueOf(getIntent().getStringExtra("aid"));
        }
        tv_makesure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                m = p.matcher(recePhone.getText().toString());

                if(receName.getText().toString().equals("")){
                    Toast.makeText(AddressManagerActivity.this,"请输入收货人姓名",Toast.LENGTH_LONG).show();
                    return;
                }else if(recePhone.getText().toString().equals("")){
                    Toast.makeText(AddressManagerActivity.this,"请输入手机号码",Toast.LENGTH_LONG).show();
                    return;
                }else if(receAddr.getText().toString().equals("")){
                    Toast.makeText(AddressManagerActivity.this,"请输入收货地址",Toast.LENGTH_LONG).show();
                    return;
                }else if(!m.matches()){
                    Toast.makeText(AddressManagerActivity.this,"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                    return;
                }
                Address address = new Address(aid,recePhone.getText().toString(),
                        receName.getText().toString(),receAddr.getText().toString());
                Intent i = new Intent(AddressManagerActivity.this,AddressActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("address",address);
                if(getIntent().getStringExtra("TAG")!=null&&getIntent().getStringExtra("TAG").equals("GWC")){
                    i.putExtra("TAG","GWC");
                }
                i.putExtras(b);
                startActivity(i);
                finish();
            }
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ib_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().getSerializableExtra("address")==null){
                    Intent i= new Intent(AddressManagerActivity.this,AddressActivity.class);
                    if(getIntent().getStringExtra("TAG")!=null&&getIntent().getStringExtra("TAG").equals("GWC")){
                        i.putExtra("TAG","GWC");
                    }
                    startActivity(i);
                    finish();
                }else{
                    Intent i= new Intent(AddressManagerActivity.this,AddressActivity.class);
                    if(getIntent().getStringExtra("TAG")!=null&&getIntent().getStringExtra("TAG").equals("GWC")){
                        i.putExtra("TAG","GWC");
                    }
                    i.putExtra("delete", String.valueOf(aid));
                    startActivity(i);
                    finish();
                }
            }
        });
    }
}
