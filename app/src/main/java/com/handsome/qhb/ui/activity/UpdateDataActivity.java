package com.handsome.qhb.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/14.
 */

public class UpdateDataActivity extends BaseActivity {
   //返回键
    private ImageButton ib_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);
        ib_back = (ImageButton) findViewById(R.id.ib_back);

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
