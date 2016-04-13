package com.handsome.qhb.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import com.handsome.qhb.adapter.MsgAdapter;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.ChatMessage;
import com.handsome.qhb.bean.RandomBonus;
import com.handsome.qhb.bean.Room;
import com.handsome.qhb.bean.XGMessage;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.db.MessageDAO;
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
    private LinearLayout ll_back;
    private ListView lv_chat;
    private Room room;
    private List<ChatMessage> messageList = new ArrayList<ChatMessage>();
    private MsgAdapter msgAdapter;
    private ChatMessage message;


    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==Config.CHAT_MESSAGE){
                ReceiverMessage((ChatMessage) msg.obj);
            }else if(msg.what==Config.RANDOMBONUS_MESSAGE){
                ReceiverRandomBonus((ChatMessage) msg.obj);
            }else if(msg.what==Config.CDSBONUS_MESSAGE){
                ReceiverCDSBonus((ChatMessage)msg.obj);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().getSerializableExtra("room")==null){
            return;
        }
        setContentView(R.layout.activity_chat);

        tv_room_title = (TextView) findViewById(R.id.tv_room_title);
        et_chat_msg = (EditText)findViewById(R.id.et_chat_msg);
        ib_chat_send = (ImageButton)findViewById(R.id.ib_chat_send);
        ll_back = (LinearLayout)findViewById(R.id.ll_back);
        lv_chat = (ListView)findViewById(R.id.lv_chat);
        room = (Room) getIntent().getSerializableExtra("room");
        MyApplication.setChatHandler(handler,room.getRid());
        messageList = MessageDAO.query(MyApplication.getSQLiteDatabase(),room.getRid());
        for(int i = 0;i<messageList.size();i++){
            if(messageList.get(i).getStatus()==0){
                messageList.get(i).setStatus(1);
            }
        }
        msgAdapter = new MsgAdapter(this, messageList);
        lv_chat.setAdapter(msgAdapter);
        lv_chat.setSelection(messageList.size() - 1);

        tv_room_title.setText(room.getRoomName());

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ib_chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_chat_msg.getText().toString().trim().equals("")) {
                    return;
                }
                message = new ChatMessage();
                message.setUid(UserInfo.getInstance().getUid());
                message.setPhoto(UserInfo.getInstance().getPhoto());
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

                        XGMessage xgMessage = new XGMessage();
                        xgMessage.setContent(message);
                        xgMessage.setTitle("chat");
                        map.put("message_type", String.valueOf(Config.TYPE_MESSAGE));
                        map.put("message", MyApplication.getGson().toJson(xgMessage));
                        map.put("access_id", String.valueOf(Config.ACCESSID));
                        map.put("timestamp", timestamp);


                        params.put("message",MyApplication.getGson().toJson(xgMessage));
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

    @Override
    protected void onStart() {
        super.onStart();
        if(MyApplication.getChatHandler()==null){
            MyApplication.setChatHandler(handler,room.getRid());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void ReceiverMessage(ChatMessage message) {
        LogUtils.e("message.before.status==>",String.valueOf(message.getStatus()));
        if (message.getRid() == room.getRid()) {
            if (message.getUid() == UserInfo.getInstance().getUid()) {
                messageList.get(messageList.size() - 1).setStatus(1);
                msgAdapter.notifyDataSetChanged();
                lv_chat.setSelection(messageList.size() - 1);
                LogUtils.e("message.status==>", "1");
                return;
            }
            messageList.add(message);
            msgAdapter.notifyDataSetChanged();
            lv_chat.setSelection(messageList.size() - 1);
        }
    }

    public void ReceiverRandomBonus(ChatMessage msg){
            msg.setStatus(1);
            messageList.add(msg);
            msgAdapter.notifyDataSetChanged();
            lv_chat.setSelection(messageList.size() - 1);
    }

    public void ReceiverCDSBonus(ChatMessage msg){
        if(msg.getRid()==room.getRid()){
            messageList.add(msg);
            msgAdapter.notifyDataSetChanged();
            lv_chat.setSelection(messageList.size() - 1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.setChatHandler(null, 0);
    }
}
