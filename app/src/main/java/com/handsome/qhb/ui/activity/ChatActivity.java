package com.handsome.qhb.ui.activity;

import android.app.ProgressDialog;
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
import com.handsome.qhb.adapter.MsgAdapter;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.APS;
import com.handsome.qhb.bean.ChatMessage;
import com.handsome.qhb.bean.IOSMessage;
import com.handsome.qhb.bean.Room;
import com.handsome.qhb.bean.XGMessage;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.db.MessageDAO;
import com.handsome.qhb.listener.MessageListener;
import com.handsome.qhb.utils.HttpUtils;
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
public class ChatActivity extends BaseActivity implements MessageListener {
    private TextView tv_room_title;
    private EditText et_chat_msg;
    private ImageButton ib_chat_send;
    private LinearLayout ll_back;
    private ListView lv_chat;
    private Room room;
    private List<ChatMessage> messageList = new ArrayList<ChatMessage>();
    private MsgAdapter msgAdapter;
    private ChatMessage message;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

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
        MyApplication.messageListenersList.add(this);

        tv_room_title = (TextView) findViewById(R.id.tv_room_title);
        et_chat_msg = (EditText)findViewById(R.id.et_chat_msg);

        ib_chat_send = (ImageButton)findViewById(R.id.ib_chat_send);
        ll_back = (LinearLayout)findViewById(R.id.ll_back);
        lv_chat = (ListView)findViewById(R.id.lv_chat);
        room = (Room) getIntent().getSerializableExtra("room");
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
                LogUtils.e("room=====", "onclick");

                final ProgressDialog progressDialog = new ProgressDialog(ChatActivity.this);
                progressDialog.setMessage("系统正在安排您退出房间");
                progressDialog.setCancelable(true);
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.BASE_URL+"Room/exitRoom",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    progressDialog.dismiss();
                                    LogUtils.e("response",response);
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    if(status.equals("0")){

                                       Toast toast = Toast.makeText(ChatActivity.this,jsonObject.getString("info"), Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                        return;
                                    }

                                    Toast toast = Toast.makeText(ChatActivity.this,jsonObject.getString("info"), Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(ChatActivity.this, "网络错误,请检查后重试", Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("uid", String.valueOf(UserInfo.getInstance().getUid()));
                        map.put("token", UserInfo.getInstance().getToken());
                        map.put("rid", String.valueOf(room.getRid()));
                        return map;
                    }
                };
                MyApplication.getmQueue().add(stringRequest);
            }
        });


        ib_chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_chat_msg.getText().toString().trim().equals("")) {
                    return;
                }
                message= new ChatMessage();
                message.setUid(UserInfo.getInstance().getUid());
                message.setPhoto(UserInfo.getInstance().getPhoto());
                message.setContent(et_chat_msg.getText().toString());
                message.setId(messageList.size());
                message.setType(1);
                Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                message.setDate(format.format(new Date()));
                message.setRid(room.getRid());
                message.setNackname(UserInfo.getInstance().getNackname());
                messageList.add(message);
                msgAdapter.notifyDataSetChanged();
                lv_chat.setSelection(messageList.size() - 1);
                et_chat_msg.setText("");

                //Android
                Map<String, String> mapA = new HashMap<String, String>();
                Map<String,String> paramsA = new TreeMap<String, String>();
                String timestampA = String.valueOf((long)(System.currentTimeMillis()/1000));

                XGMessage xgMessage = new XGMessage();
                xgMessage.setContent(message);
                LogUtils.e("message===========>",message.getContent());
                xgMessage.setTitle("1");
                mapA.put("message_type", String.valueOf(Config.TYPE_MESSAGE));
                mapA.put("message", MyApplication.getGson().toJson(xgMessage));
                mapA.put("access_id", String.valueOf(Config.ACCESSID));
                mapA.put("timestamp", timestampA);
                mapA.put("expire_time",String.valueOf(259200));


                paramsA.put("message",MyApplication.getGson().toJson(xgMessage));
                paramsA.put("message_type",String.valueOf(Config.TYPE_MESSAGE));
                paramsA.put("access_id", String.valueOf(Config.ACCESSID));
                paramsA.put("timestamp",timestampA);
                paramsA.put("expire_time", String.valueOf(259200));
                try {
                    mapA.put("sign", SignUtil.getSign("post", Config.XG_PUSH_URL + "all_device", paramsA));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                HttpUtils.sendMessage(ChatActivity.this, "all_device", mapA, Config.SENDMSG_TAG);

                //IOS
                Map<String, String> mapI = new HashMap<String, String>();
                Map<String,String> paramsI = new TreeMap<String, String>();
                String timestampI = String.valueOf((long)(System.currentTimeMillis()/1000));

                APS aps = new APS();
                aps.setAlert(message);
                IOSMessage iosMessage = new IOSMessage();
                iosMessage.setAps(aps);
                mapI.put("message_type","0");
                mapI.put("message", MyApplication.getGson().toJson(iosMessage));
                mapI.put("access_id", Config.ACCESSIDIOS);
                mapI.put("timestamp", timestampI);
                mapI.put("environment","1");
                mapI.put("expire_time",String.valueOf(259200));

                paramsI.put("message",MyApplication.getGson().toJson(iosMessage));
                paramsI.put("message_type","0");
                paramsI.put("access_id", Config.ACCESSIDIOS);
                paramsI.put("timestamp", timestampI);
                paramsI.put("environment", "1");
                paramsI.put("expire_time", String.valueOf(259200));
                try {
                    mapI.put("sign", SignUtil.getIosSign("post", Config.XG_PUSH_URL + "all_device", paramsI));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                HttpUtils.sendMessage(ChatActivity.this,"all_device",mapI,Config.SENDMSG_TAG);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void ReceiverMessage(ChatMessage message) {
        if (message.getRid() == room.getRid()) {
            if (message.getUid() == UserInfo.getInstance().getUid()) {
                if(message.getType()==1){
                    for(int i = messageList.size()-1;i>=0;i--){
                        if(message.getId()==messageList.get(i).getId()){
                            messageList.get(i).setStatus(1);
                            LogUtils.e("message.status==>", String.valueOf(messageList.get(messageList.size() - 1).getStatus()));
                            msgAdapter.notifyDataSetChanged();
                            lv_chat.setSelection(messageList.size() - 1);
                            return;
                        }
                    }
                }else {
                    message.setStatus(1);
                }
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
        MyApplication.getmQueue().cancelAll(Config.SENDMSG_TAG);
        MyApplication.messageListenersList.remove(this);
    }


    @Override
    public void handleMsg(Message message) {
        handler.handleMessage(message);
    }
}
