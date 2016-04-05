package com.handsome.qhb.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.ChatMessage;
import com.handsome.qhb.bean.DS;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.db.MessageDAO;

import java.util.HashMap;
import java.util.Map;


import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/4/1.
 */
public class CDSActivity extends BaseActivity {


    private TextView tv_Btime,tv_money,tv_person,tv_time,tv_guess,tv_single,tv_double,tv_result;

    private LinearLayout ll_myguess,ll_guess,ll_result;

    private ImageButton ib_back;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cds);

        tv_Btime = (TextView)findViewById(R.id.tv_Btime);
        tv_money = (TextView)findViewById(R.id.tv_money);
        tv_person = (TextView)findViewById(R.id.tv_person);
        tv_time = (TextView)findViewById(R.id.tv_time);
        tv_guess = (TextView)findViewById(R.id.tv_guess);
        tv_single = (TextView)findViewById(R.id.tv_single);
        tv_double= (TextView)findViewById(R.id.tv_double);
        tv_result = (TextView)findViewById(R.id.tv_result);
        ll_myguess = (LinearLayout)findViewById(R.id.ll_myguess);
        ll_guess = (LinearLayout)findViewById(R.id.ll_guess);
        ll_result = (LinearLayout)findViewById(R.id.ll_result);
        ib_back = (ImageButton)findViewById(R.id.ib_back);

        ChatMessage chatMessage = (ChatMessage) getIntent().getSerializableExtra("cdsMessage");
        DS ds = (DS) getIntent().getSerializableExtra("ds");


        MyApplication.setCdsHandler(handler,chatMessage.getRid());
        tv_person.setText(ds.getPersonNum());
        tv_time.setText(chatMessage.getDate());
        if(ds.getGuess()!=0){
            if(ds.getGuess()==1){
                tv_guess.setText("单");

            }else if(ds.getGuess()==2){
                tv_guess.setText("双");
            }
            ll_guess.setVisibility(View.INVISIBLE);
            MessageDAO.updateStatus(MyApplication.getSQLiteDatabase(),Config.STATE_CDSBONUS_GUESSED,chatMessage.getId());
        }

        if(ds.getResult()!=0){
            if(ds.getResult()==1){
                tv_result.setText("单");
            }else if(ds.getResult()==2){
                tv_result.setText("双");
            }
            ll_result.setVisibility(View.VISIBLE);
            MessageDAO.updateStatus(MyApplication.getSQLiteDatabase(),Config.STATE_CDSBONUS_END,chatMessage.getId());
        }
        tv_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_guess.setVisibility(View.INVISIBLE);
                tv_guess.setText("单");
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();

                        return map;
                    }
                };
            }
        });

        tv_double.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_guess.setVisibility(View.INVISIBLE);
                tv_guess.setText("双");
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        return map;
                    }
                };
            }
        });

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.setCdsHandler(null,0);
    }
}
