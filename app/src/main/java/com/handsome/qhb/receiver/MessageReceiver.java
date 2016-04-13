package com.handsome.qhb.receiver;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.ChatMessage;
import com.handsome.qhb.bean.RandomBonus;
import com.handsome.qhb.bean.Room;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.db.MessageDAO;
import com.handsome.qhb.db.RoomDAO;
import com.handsome.qhb.ui.activity.AddressActivity;
import com.handsome.qhb.ui.activity.CDSActivity;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.UserInfo;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/22.
 */
public class MessageReceiver extends XGPushBaseReceiver {


    private Gson gson = new Gson();
    private List<Room>  roomList = new ArrayList<Room>();
    private List<Room> rooms = new ArrayList<Room>();
    private ChatMessage chatMessage;
    private static final int NOTIFYID_1 = 1;
    private Bitmap LargeBitmap = BitmapFactory.decodeResource(MyApplication.getContext().getResources(),R.mipmap.test_icon);
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Refresh refresh;


    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {

    }

    @Override
    public void onUnregisterResult(Context context, int i) {

    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {

    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {

    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        if(UserInfo.getInstance()==null){
            return;
        }
        if(xgPushTextMessage.getTitle().equals("Room")) {
            roomList = gson.fromJson(xgPushTextMessage.getContent(), new TypeToken<List<Room>>() {
            }.getType());
            if (roomList != null) {
                Message message = new Message();
                message.what = Config.INITROOM_MESSAGE;
                message.obj = roomList;
                MyApplication.getRoomHandler().handleMessage(message);
            }
        }else {
            //数据库中获取存储的房间
            rooms = RoomDAO.query(MyApplication.getSQLiteDatabase(), UserInfo.getInstance().getUid());
            chatMessage = gson.fromJson(xgPushTextMessage.getContent(), ChatMessage.class);
            String time = format.format(new Date());
            chatMessage.setDate(time);
            chatMessage.setStatus(1);
            //查找这条消息所对应的房间
            int i = 0;
            for(;i<rooms.size();i++){
                if(rooms.get(i).getRid()==chatMessage.getRid()){
                    RoomDAO.update(MyApplication.getSQLiteDatabase(),MyApplication.getGson().toJson(chatMessage),chatMessage.getRid());
                    break;
                }
            }

            //没有对应的房间，直接退出
            if(i==rooms.size()){
                return;
            }

            Notification.Builder mBuilder = new Notification.Builder(MyApplication.getContext());
            mBuilder.setContentTitle("楼下购");

            //消息到达时间
            chatMessage.setDate(format.format(new Date()));
            //普通消息
            if(xgPushTextMessage.getTitle().equals("chat")){
                mBuilder.setContentText(chatMessage.getNackname()+" : "+chatMessage.getContent())
                        .setTicker("收到" + chatMessage.getNackname() + "发送过来的信息");
                //判断该房间是否正打开
                if(MyApplication.getChatHandler()!=null&&MyApplication.getRoomId()==chatMessage.getRid()){
                    Message message = new Message();
                    message.what = Config.CHAT_MESSAGE;
                    message.obj = chatMessage;
                    Message message1 = new Message();
                    message1.what = Config.ROOM_REFRESH_LASTMESSAGE;
                    message1.obj = chatMessage;

                    MyApplication.getChatHandler().handleMessage(message);
                    MyApplication.getRoomHandler().handleMessage(message1);
                }else{
                    //更新房间信息
                    Message message = new Message();
                    message.what = Config.ADD_MESSAGE;
                    message.obj = chatMessage;
                    MyApplication.getRoomHandler().handleMessage(message);
                }

            }else if(xgPushTextMessage.getTitle().equals("RandomBonus")){
                //红包消息
                mBuilder.setContentText(chatMessage.getNackname()+chatMessage.getContent())
                        .setTicker("收到" + chatMessage.getNackname() + "发送过来的信息");
                chatMessage.setType(Config.TYPE_RANDOMBONUS);
                //判断该房间是否正打开
                if(MyApplication.getChatHandler()!=null&&MyApplication.getRoomId()==chatMessage.getRid()){
                    Message message = new Message();
                    message.what = Config.RANDOMBONUS_MESSAGE;
                    message.obj = chatMessage;

                    Message message1 = new Message();
                    message1.what = Config.ROOM_REFRESH_LASTMESSAGE;
                    message1.obj = chatMessage;

                    MyApplication.getChatHandler().handleMessage(message);
                    MyApplication.getRoomHandler().handleMessage(message1);
                }else{
                    //更新房间信息
                    Message message = new Message();
                    message.what = Config.ADD_MESSAGE;
                    message.obj = chatMessage;
                    MyApplication.getRoomHandler().handleMessage(message);
                }

            }else if(xgPushTextMessage.getTitle().equals("DSResult")){

                MessageDAO.updateStatus(MyApplication.getSQLiteDatabase(),Config.STATE_CDSBONUS_END,chatMessage.getId());

                if(MyApplication.getCdsHandler()!=null&&MyApplication.getCid()==chatMessage.getId()){
                    Message message = new Message();
                    message.what = Config.DS_RESULT;
                    message.obj = chatMessage;
                    LogUtils.e("result_before",chatMessage.toString());
                    MyApplication.getCdsHandler().handleMessage(message);
                }

                if(chatMessage.getContent().equals("1")){
                    chatMessage.setContent("本期单双开奖结果 : 单");

                }else if(chatMessage.getContent().equals("2")){
                    chatMessage.setContent("本期单双开奖结果 : 双");
                }

                //判断该房间是否正打开
                if(MyApplication.getChatHandler()!=null&&MyApplication.getRoomId()==chatMessage.getRid()){
                    Message message = new Message();
                    message.what = Config.CHAT_MESSAGE;
                    message.obj = chatMessage;
                    Message message1 = new Message();
                    message1.what = Config.ROOM_REFRESH_LASTMESSAGE;
                    message1.obj = chatMessage;

                    MyApplication.getChatHandler().handleMessage(message);
                    MyApplication.getRoomHandler().handleMessage(message1);
                }else{
                    //更新房间信息
                    Message message = new Message();
                    message.what = Config.ADD_MESSAGE;
                    message.obj = chatMessage;
                    MyApplication.getRoomHandler().handleMessage(message);
                }
            }
            else if(xgPushTextMessage.getTitle().equals("CDSBonus")){
                //判断是否有这个房间,若没有则不响应
                //红包消息
                mBuilder.setContentText(chatMessage.getNackname()+chatMessage.getContent())
                        .setTicker("收到" + chatMessage.getNackname() + "发送过来的信息");

                chatMessage.setType(Config.TYPE_CDSBONUS);
                LogUtils.e("message-cds-time",String.valueOf(chatMessage.getDsTime()));
                if(MyApplication.getChatHandler()!=null&&MyApplication.getRoomId()==chatMessage.getRid()){
                    Message message = new Message();
                    message.what = Config.CDSBONUS_MESSAGE;
                    message.obj = chatMessage;

                    Message message1 = new Message();
                    message1.what = Config.ROOM_REFRESH_LASTMESSAGE;
                    message1.obj = chatMessage;
                    MyApplication.getChatHandler().handleMessage(message);
                    MyApplication.getRoomHandler().handleMessage(message1);
                }else{
                    //更新房间信息
                    Message message = new Message();
                    message.what = Config.ADD_MESSAGE;
                    message.obj = chatMessage;
                    MyApplication.getRoomHandler().handleMessage(message);
                }
            }else if(xgPushTextMessage.getTitle().equals("CancelBonus")){
                MessageDAO.updateStatus(MyApplication.getSQLiteDatabase(),0,chatMessage.getId());
                //判断该房间是否正打开
                if(MyApplication.getChatHandler()!=null&&MyApplication.getRoomId()==chatMessage.getRid()){
                    Message message = new Message();
                    message.what = Config.CHAT_MESSAGE;
                    message.obj = chatMessage;
                    Message message1 = new Message();
                    message1.what = Config.ROOM_REFRESH_LASTMESSAGE;
                    message1.obj = chatMessage;

                    MyApplication.getChatHandler().handleMessage(message);
                    MyApplication.getRoomHandler().handleMessage(message1);
                }else{
                    //更新房间信息
                    Message message = new Message();
                    message.what = Config.ADD_MESSAGE;
                    message.obj = chatMessage;
                    MyApplication.getRoomHandler().handleMessage(message);
                }
            }

            //判断是不是自己的消息，若是自己的消息，不提醒
            if(chatMessage.getUid()!=UserInfo.getInstance().getUid()){
                mBuilder.setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.test_icon)
                        .setLargeIcon(LargeBitmap)
                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                        .setAutoCancel(true);
                MyApplication.getNotificationManager().notify(NOTIFYID_1, mBuilder.build());
            }
            MessageDAO.insert(MyApplication.getSQLiteDatabase(), chatMessage.getId(), chatMessage.getRid(), chatMessage.getUid(), chatMessage.getContent(),
                    chatMessage.getNackname(), chatMessage.getDate(), chatMessage.getStatus(), chatMessage.getType(), chatMessage.getBonus_total(),chatMessage.getDsTime(),chatMessage.getPhoto());
        }
    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {

    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {

    }

    public interface Refresh{
        void dealMessage(ChatMessage chatMessage);
    }
}