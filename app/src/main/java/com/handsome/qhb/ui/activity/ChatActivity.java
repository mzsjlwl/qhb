package com.handsome.qhb.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.handsome.qhb.adapter.MsgAdapter;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.ChatMessage;
import com.handsome.qhb.bean.RandomBonus;
import com.handsome.qhb.bean.Room;
import com.handsome.qhb.bean.XGMessage;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.SignUtil;
import com.handsome.qhb.utils.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/23.
 */
public class ChatActivity extends BaseActivity {

    private TextView tv_room_title;
    private EditText et_chat_msg;
    private ImageButton ib_chat_send;
    private ImageButton ib_back;
    private ListView lv_chat;
    private Room room;
    private List<ChatMessage> messageList = new ArrayList<ChatMessage>();
    private MsgAdapter msgAdapter;
    private ChatMessage message;
    private Gson gson = new Gson();


    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==Config.CHAT_MESSAGE){
                ChatMessage chatMessage = new ChatMessage();
                chatMessage = (ChatMessage) msg.obj;
                ReceiverMessage(chatMessage);
            }else if(msg.what==Config.RANDOMBONUS_MESSAGE){
                ChatMessage chatMessage = new ChatMessage();
                chatMessage = (ChatMessage)msg.obj;
                ReceiverRandomBonus(chatMessage);
            }else if(msg.what==Config.CDSBONUS_MESSAGE){
                ChatMessage chatMessage = new ChatMessage();
                chatMessage = (ChatMessage)msg.obj;
                ReceiverCDSBonus(chatMessage);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.setChatHandler(handler);
        if(getIntent().getSerializableExtra("room")==null){
            return;
        }
        setContentView(R.layout.activity_chat);
        tv_room_title = (TextView) findViewById(R.id.tv_room_title);
        et_chat_msg = (EditText)findViewById(R.id.et_chat_msg);
        ib_chat_send = (ImageButton)findViewById(R.id.ib_chat_send);
        ib_back = (ImageButton)findViewById(R.id.ib_back);
        lv_chat = (ListView)findViewById(R.id.lv_chat);
        msgAdapter = new MsgAdapter(this, messageList);
        lv_chat.setAdapter(msgAdapter);
        room = (Room) getIntent().getSerializableExtra("room");

        tv_room_title.setText(room.getRoomName());


        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+"Room/exitRoom",
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                try {
//                                    LogUtils.e("room",response);
//                                    JSONObject jsonObject = new JSONObject(response);
//                                    String status = jsonObject.getString("status");
//                                    if(status.equals(0)){
//                                        LogUtils.e("room", jsonObject.getString("info"));
//                                        Toast toast = Toast.makeText(ChatActivity.this,jsonObject.getString("info"),Toast.LENGTH_LONG);
//                                        toast.setGravity(Gravity.CENTER,0,0);
//                                        toast.show();
//                                    }else{
//                                        finish();
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("TAG", error.getMessage(), error);
//                    }
//                }){
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> map = new HashMap<String, String>();
//                        map.put("rid", String.valueOf(room.getRid()));
//                        map.put("uid", String.valueOf(UserInfo.getInstance().getUid()));
//                        return map;
//                    }
//                };
//                MyApplication.getmQueue().add(stringRequest);
                finish();
            }
        });

        ib_chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = new ChatMessage();
                message.setUid(UserInfo.getInstance().getUid());
                message.setContent(et_chat_msg.getText().toString());
                Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                message.setDate(format.format(new Date()));
                message.setRid(room.getRid());
                message.setNackname(UserInfo.getInstance().getNackname());
                messageList.add(message);
                msgAdapter.notifyDataSetChanged();
                lv_chat.setSelection(messageList.size() - 1);
                et_chat_msg.setText("");
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.XG_PUSH_URL+"all_device",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String ret_code = jsonObject.getString("ret_code");
                                    if(!ret_code.equals("0")){
                                        Toast.makeText(ChatActivity.this, jsonObject.getString("err_msg"), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(ChatActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        Map<String,String> params = new TreeMap<String, String>();
                        String timestamp = String.valueOf((long)(System.currentTimeMillis()/1000));
                        LogUtils.e("timestamp", timestamp);

                        XGMessage xgMessage = new XGMessage();
                        xgMessage.setContent(message);
                        xgMessage.setTitle("chat");
                        map.put("message_type", String.valueOf(Config.TYPE_MESSAGE));
                        map.put("message", gson.toJson(xgMessage));
                        map.put("access_id", String.valueOf(Config.ACCESSID));
                        map.put("timestamp", timestamp);


                        params.put("message",gson.toJson(xgMessage));
                        params.put("message_type",String.valueOf(Config.TYPE_MESSAGE));
                        params.put("access_id", String.valueOf(Config.ACCESSID));
                        params.put("timestamp",timestamp);

                        try {
                            map.put("sign", SignUtil.getSign("post", Config.XG_PUSH_URL + "all_device", params));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        return map;
                    }
                };
                MyApplication.getmQueue().add(stringRequest);
            }
        });
    }

    public void ReceiverMessage(ChatMessage message) {
        if (message.getRid() == room.getRid()) {
            if (message.getUid() == UserInfo.getInstance().getUid()) {
                messageList.get(messageList.size() - 1).setStatus(1);
                msgAdapter.notifyDataSetChanged();
                lv_chat.setSelection(messageList.size() - 1);
                LogUtils.e("Receiver", "same");
                return;
            }
            LogUtils.e("Receiver", "different");
            messageList.add(message);
            msgAdapter.notifyDataSetChanged();
            lv_chat.setSelection(messageList.size() - 1);
        }
    }

    public void ReceiverRandomBonus(ChatMessage msg){
        if(msg.getRid()==room.getRid()){
            ChatMessage chatMessage = new ChatMessage();
            //chatMessage.setStatus(1);
            chatMessage.setType(Config.TYPE_RANDOMBONUS);
            chatMessage.setUid(msg.getUid());
            chatMessage.setNackname("系统");
            Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            chatMessage.setDate(format.format(new Date()));
            chatMessage.setId(msg.getId());
            messageList.add(chatMessage);
            msgAdapter.notifyDataSetChanged();
            lv_chat.setSelection(messageList.size() - 1);
        }
    }

    public void ReceiverCDSBonus(ChatMessage msg){
        if(msg.getRid()==room.getRid()){
            ChatMessage chatMessage =  new ChatMessage();
            //chatMessage.setStatus(1);
            chatMessage.setType(Config.TYPE_CDSBONUS);
            chatMessage.setUid(msg.getUid());
            chatMessage.setNackname("系统");
            messageList.add(chatMessage);
            msgAdapter.notifyDataSetChanged();
            lv_chat.setSelection(messageList.size()-1);
        }
    }



}
