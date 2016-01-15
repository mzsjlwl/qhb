package com.handsome.qhb.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import tab.com.handsome.handsome.R;

public class LoginActivity extends Activity implements View.OnClickListener{

    Button bt_login,bt_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews(){
        bt_login = (Button) findViewById(R.id.id_bt_login);
        bt_register = (Button) findViewById(R.id.id_bt_register);
        bt_login.setOnClickListener(this);
        bt_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_bt_login:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
