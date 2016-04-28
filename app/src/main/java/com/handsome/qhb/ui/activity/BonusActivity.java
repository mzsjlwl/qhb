package com.handsome.qhb.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handsome.qhb.adapter.BonusAdapter;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.ChatMessage;
import com.handsome.qhb.bean.RandomBonus;
import com.handsome.qhb.utils.ImageUtils;
import com.handsome.qhb.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/28.
 */
public class BonusActivity extends BaseActivity {

    private ChatMessage chatMessage;
    private List<RandomBonus> bonusList = new ArrayList<RandomBonus>();
    private BonusAdapter bonusAdapter;
    private ListView lv_bonus;
    private TextView tv_user_nackname;
    private ImageView iv_user_photo;
    private LinearLayout ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus);
        lv_bonus = (ListView) findViewById(R.id.lv_bonus);
        tv_user_nackname = (TextView) findViewById(R.id.tv_user_nackname);
        iv_user_photo = (ImageView) findViewById(R.id.iv_user_photo);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        if(getIntent().getSerializableExtra("ChatMessage")!=null&&getIntent().getSerializableExtra("bonusList")!=null){

            chatMessage = (ChatMessage) getIntent().getSerializableExtra("ChatMessage");
            tv_user_nackname.setText(chatMessage.getNackname()+"的红包");
            ImageUtils.imageLoader(MyApplication.getmQueue(), chatMessage.getPhoto(), iv_user_photo);

            LogUtils.e("chatMessage",chatMessage.toString());


            bonusList = (List<RandomBonus>) getIntent().getSerializableExtra("bonusList");
            bonusAdapter = new BonusAdapter(this,bonusList,R.layout.bonus_list_items, MyApplication.getmQueue());
            lv_bonus.setAdapter(bonusAdapter);
        }

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
