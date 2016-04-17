package com.handsome.qhb.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.ChatMessage;
import com.handsome.qhb.bean.DS;
import com.handsome.qhb.bean.User;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.db.MessageDAO;
import com.handsome.qhb.receiver.MessageReceiver;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/4/1.
 */
public class CDSActivity extends BaseActivity  {


    private TextView tv_Btime,tv_money,tv_person,tv_time,tv_guess,tv_single,tv_double,tv_result;

    private LinearLayout ll_myguess,ll_guess,ll_result;

    private LinearLayout ll_back;
    private ChatMessage chatMessage;
    private DS ds;
    private TimerTask timerTask;
    private Timer timer;
    private SimpleDateFormat simpleDateFormat =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Date date;
    private long hbtime,nowtime,subtime;
    private int minute,seconds;
    private String s_minute,s_seconds;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==Config.DS_RESULT){
                refreshResult((ChatMessage) msg.obj);
            }else if(msg.what==Config.CDS_TIME){
                if(subtime<0){
                    timer.cancel();
                    return;
                }
                minute =(int)subtime/60;
                seconds = (int)subtime%60;
                if(minute<10){
                    s_minute = "0"+minute;
                }else{
                    s_minute =""+minute;
                }
                if(seconds<10){
                   s_seconds = "0"+seconds;
                }else{
                    s_seconds = ""+seconds;
                }
                tv_Btime.setText(s_minute + ":" + s_seconds);
            }
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
        ll_back = (LinearLayout)findViewById(R.id.ll_back);

       chatMessage = (ChatMessage) getIntent().getSerializableExtra("cdsMessage");
         ds = (DS) getIntent().getSerializableExtra("ds");


        MyApplication.setCdsHandler(handler,chatMessage.getId());
        tv_money.setText(String.valueOf(chatMessage.getBonus_total()));
        tv_person.setText(String.valueOf(ds.getPersonNum()));
        tv_time.setText(chatMessage.getDate());
        if(ds.getGuess()!=0){
            if(ds.getGuess()==1){
                tv_guess.setText("单");

            }else if(ds.getGuess()==2){
                tv_guess.setText("双");
            }
            ll_guess.setVisibility(View.INVISIBLE);
            ll_myguess.setVisibility(View.VISIBLE);
            MessageDAO.updateStatus(MyApplication.getSQLiteDatabase(),Config.STATE_CDSBONUS_GUESSED,chatMessage.getId());
        }

        if(ds.getResult()!=0){
            if(ds.getResult()==1){
                tv_result.setText("单");
            }else if(ds.getResult()==2){
                tv_result.setText("双");
            }
            ll_guess.setVisibility(View.INVISIBLE);
            ll_result.setVisibility(View.VISIBLE);

            MessageDAO.updateStatus(MyApplication.getSQLiteDatabase(),Config.STATE_CDSBONUS_END,chatMessage.getId());
        }
        tv_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                MessageDAO.updateStatus(MyApplication.getSQLiteDatabase(), Config.STATE_CDSBONUS_GUESSED, chatMessage.getId());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "DS/cds", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            LogUtils.e("single==>response==>",response);
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("status").equals("0")){
                                Toast toast = Toast.makeText(CDSActivity.this,jsonObject.getString("info"),Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                                return;
                            }
                            ll_guess.setVisibility(View.INVISIBLE);
                            tv_guess.setText("单");
                            ll_myguess.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(CDSActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("rid",String.valueOf(chatMessage.getRid()));
                        map.put("dsId",String.valueOf(ds.getId()));
                        map.put("uid",String.valueOf(UserInfo.getInstance().getUid()));
                        map.put("token",UserInfo.getInstance().getToken());
                        map.put("result",String.valueOf(1));
                        return map;
                    }
                };
                MyApplication.getmQueue().add(stringRequest);
            }
        });

        tv_double.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tv_double.setClickable(false);
                MessageDAO.updateStatus(MyApplication.getSQLiteDatabase(), Config.STATE_CDSBONUS_GUESSED, chatMessage.getId());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "DS/cds",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    tv_double.setClickable(true);
                                    LogUtils.e("tv_double====response==>",response);
                                    JSONObject jsonObject = new JSONObject(response);
                                    if(jsonObject.getString("status").equals("0")){
                                        Toast toast = Toast.makeText(CDSActivity.this,jsonObject.getString("info"),Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER,0,0);
                                        toast.show();
                                        return;
                                    }
                                    ll_guess.setVisibility(View.INVISIBLE);
                                    tv_guess.setText("双");
                                    ll_myguess.setVisibility(View.VISIBLE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(CDSActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("rid",String.valueOf(chatMessage.getRid()));
                        map.put("dsId",String.valueOf(ds.getId()));
                        map.put("token",UserInfo.getInstance().getToken());
                        map.put("uid",String.valueOf(UserInfo.getInstance().getUid()));
                        map.put("result",String.valueOf(2));
                        return map;
                    }
                };
                MyApplication.getmQueue().add(stringRequest);
            }
        });
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        try {
            date = simpleDateFormat.parse(chatMessage.getDate());
            hbtime= date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = Config.CDS_TIME;
                nowtime = System.currentTimeMillis();
                subtime = chatMessage.getDsTime()*60-(nowtime-hbtime)/1000;
                handler.sendMessage(msg);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask,0,1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.setCdsHandler(null, 0);
        timer.cancel();
    }

    public void refreshResult(ChatMessage msg){
        LogUtils.e("result-msg",msg.toString());
        if(msg.getContent().equals("1")){
            tv_result.setText("单");
        }else if(msg.getContent().equals("2")){
            tv_result.setText("双");
        }
        ll_guess.setVisibility(View.INVISIBLE);
        ll_result.setVisibility(View.VISIBLE);
    }


}
