package com.handsome.qhb.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/14.
 */

public class UpdatePasswordActivity extends BaseActivity {
    //标题
    private TextView tv_title;
    //返回键
    private ImageButton ib_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ib_back = (ImageButton) findViewById(R.id.ib_back);

        tv_title.setText("修改密码");
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
