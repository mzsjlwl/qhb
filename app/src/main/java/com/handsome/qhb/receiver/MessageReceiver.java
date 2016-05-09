package com.handsome.qhb.receiver;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.ChatMessage;
import com.handsome.qhb.bean.Room;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.db.MessageDAO;
import com.handsome.qhb.db.RoomDAO;
import com.handsome.qhb.listener.MessageListener;
import com.handsome.qhb.ui.activity.CDSActivity;
import com.handsome.qhb.ui.activity.ChatActivity;
import com.handsome.qhb.ui.activity.MainActivity;
import com.handsome.qhb.ui.fragment.HallFragment;
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
import java.util.InputMismatchException;
import java.util.List;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/22.
 */
public class MessageReceiver extends XGPushBaseReceiver {


    private List<Room>  roomList = new ArrayList<Room>();
    private List<Room> rooms = new ArrayList<Room>();
    private ChatMessage chatMessage;
    private static final int NOTIFYID_1 = 1;
    private Bitmap LargeBitmap = BitmapFactory.decodeResource(MyApplication.getContext().getResources(),R.mipmap.logo);
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



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
            roomList = MyApplication.getGson().fromJson(xgPushTextMessage.getContent(), new TypeToken<List<Room>>() {
            }.getType());
            if (roomList != null) {
                Message message = new Message();
                message.what = Config.INITROOM_MESSAGE;
                message.obj = roomList;
                for (MessageListener ml:MyApplication.messageListenersList) {
                    ml.handleMsg(message);
                }
            }
        }else {
            LogUtils.e("xgMessage==>",xgPushTextMessage.getContent());
            //数据库中获取存储的房间
            rooms = RoomDAO.query(MyApplication.getSQLiteDatabase(), UserInfo.getInstance().getUid());
            chatMessage = MyApplication.getGson().fromJson(xgPushTextMessage.getContent(), ChatMessage.class);
            String time = format.format(new Date());
            chatMessage.setDate(time);
            //查找这条消息所对应的房间
            int i = 0;
            for(;i<rooms.size();i++){
                if(rooms.get(i).getRid()==chatMessage.getRid()){
                    RoomDAO.update(MyApplication.getSQLiteDatabase(),MyApplication.getGson().toJson(chatMessage),chatMessage.getRid());
                    String dbPhoto = MessageDAO.getDBPhoto(MyApplication.getSQLiteDatabase(),chatMessage.getUid());

                    if(dbPhoto!=null&&!dbPhoto.equals(chatMessage.getPhoto())){
                       MessageDAO.updateDBPhoto(MyApplication.getSQLiteDatabase(),chatMessage.getUid(),chatMessage.getPhoto());
                    }
                    break;
                }
            }

            //没有对应的房间，直接退出
            if(i==rooms.size()){
                return;
            }


            Notification.Builder mBuilder = new Notification.Builder(MyApplication.getContext());
            mBuilder.setContentTitle(Config.APP_NAME);

            //消息到达时间
            chatMessage.setDate(format.format(new Date()));
            //普通消息
            if(xgPushTextMessage.getTitle().equals(String.valueOf(Config.TYPE_TEXT))){
                chatMessage.setType(Config.TYPE_TEXT);
                mBuilder.setContentText(chatMessage.getNackname()+" : "+chatMessage.getContent())
                        .setTicker("收到" + chatMessage.getNackname() + "发送过来的信息");
                //判断该房间是否正打开
                if(MyApplication.messageListenersList.size()==1){
                    //更新房间信息
                    Message message = new Message();
                    message.what = Config.ADD_MESSAGE;
                    message.obj = chatMessage;
                    for (MessageListener ml:MyApplication.messageListenersList) {
                        ml.handleMsg(message);
                    }
                }else{
                    Message message = new Message();
                    message.what = Config.CHAT_MESSAGE;
                    message.obj = chatMessage;
                    Message message1 = new Message();
                    message1.what = Config.ROOM_REFRESH_LASTMESSAGE;
                    message1.obj = chatMessage;

                    for (MessageListener ml:MyApplication.messageListenersList) {
                        ml.handleMsg(message);
                        ml.handleMsg(message1);
                    }
                }

            }else if(xgPushTextMessage.getTitle().equals(String.valueOf(Config.TYPE_RANDOMBONUS))){
                //红包消息
                mBuilder.setContentText(chatMessage.getNackname()+chatMessage.getContent())
                        .setTicker("收到" + chatMessage.getNackname() + "发送过来的信息");
                chatMessage.setType(Config.TYPE_RANDOMBONUS);
                //判断该房间是否正打开
                if(MyApplication.messageListenersList.size()==1){
                    //更新房间信息
                    Message message = new Message();
                    message.what = Config.ADD_MESSAGE;
                    message.obj = chatMessage;
                    for (MessageListener ml:MyApplication.messageListenersList) {
                        ml.handleMsg(message);
                    }
                }else{
                    Message message = new Message();
                    message.what = Config.CHAT_MESSAGE;
                    message.obj = chatMessage;
                    Message message1 = new Message();
                    message1.what = Config.ROOM_REFRESH_LASTMESSAGE;
                    message1.obj = chatMessage;

                    for (MessageListener ml:MyApplication.messageListenersList) {
                        ml.handleMsg(message);
                        ml.handleMsg(message1);
                    }
                }

            }else if(xgPushTextMessage.getTitle().equals(String.valueOf(Config.TYPE_DSRESULT))){
                chatMessage.setType(Config.TYPE_DSRESULT);
                MessageDAO.updateStatus(MyApplication.getSQLiteDatabase(),Config.STATE_CDSBONUS_END,chatMessage.getId());

                if(MyApplication.messageListenersList.size()==3){
                    Message message = new Message();
                    message.what = Config.DS_RESULT;
                    message.obj = chatMessage;
                    LogUtils.e("result_before", chatMessage.toString());
                    for (MessageListener ml:MyApplication.messageListenersList) {
                        ml.handleMsg(message);
                    }
                }

                if(chatMessage.getContent().equals("1")){
                    chatMessage.setContent("本期单双开奖结果 : 单");

                }else if(chatMessage.getContent().equals("2")){
                    chatMessage.setContent("本期单双开奖结果 : 双");
                }

                //判断该房间是否正打开
                if(MyApplication.messageListenersList.size()==1){
                    //更新房间信息
                    Message message = new Message();
                    message.what = Config.ADD_MESSAGE;
                    message.obj = chatMessage;
                    for (MessageListener ml:MyApplication.messageListenersList) {
                        ml.handleMsg(message);
                    }
                }else{
                    Message message = new Message();
                    message.what = Config.CHAT_MESSAGE;
                    message.obj = chatMessage;
                    Message message1 = new Message();
                    message1.what = Config.ROOM_REFRESH_LASTMESSAGE;
                    message1.obj = chatMessage;

                    for (MessageListener ml:MyApplication.messageListenersList) {
                        ml.handleMsg(message);
                        ml.handleMsg(message1);
                    }
                }
            }
            else if(xgPushTextMessage.getTitle().equals(String.valueOf(Config.TYPE_CDSBONUS))){
                //判断是否有这个房间,若没有则不响应
                //红包消息
                mBuilder.setContentText(chatMessage.getNackname()+chatMessage.getContent())
                        .setTicker("收到" + chatMessage.getNackname() + "发送过来的信息");

                chatMessage.setType(Config.TYPE_CDSBONUS);
                chatMessage.setStatus(1);
                if(MyApplication.messageListenersList.size()==1){
                    //更新房间信息
                    Message message = new Message();
                    message.what = Config.ADD_MESSAGE;
                    message.obj = chatMessage;
                    for (MessageListener ml:MyApplication.messageListenersList) {
                        ml.handleMsg(message);
                    }
                }else{
                    Message message = new Message();
                    message.what = Config.CHAT_MESSAGE;
                    message.obj = chatMessage;
                    Message message1 = new Message();
                    message1.what = Config.ROOM_REFRESH_LASTMESSAGE;
                    message1.obj = chatMessage;

                    for (MessageListener ml:MyApplication.messageListenersList) {
                        ml.handleMsg(message);
                        ml.handleMsg(message1);
                    }
                }
            }else if(xgPushTextMessage.getTitle().equals(String.valueOf(Config.TYPE_CANCELBONUS))){
                chatMessage.setType(Config.TYPE_CANCELBONUS);
                MessageDAO.updateStatus(MyApplication.getSQLiteDatabase(),0,chatMessage.getId());
                //判断该房间是否正打开
                if(MyApplication.messageListenersList.size()==1){
                    //更新房间信息
                    Message message = new Message();
                    message.what = Config.ADD_MESSAGE;
                    message.obj = chatMessage;
                    for (MessageListener ml:MyApplication.messageListenersList) {
                        ml.handleMsg(message);
                    }
                }else{
                    Message message = new Message();
                    message.what = Config.CHAT_MESSAGE;
                    message.obj = chatMessage;
                    Message message1 = new Message();
                    message1.what = Config.ROOM_REFRESH_LASTMESSAGE;
                    message1.obj = chatMessage;

                    for (MessageListener ml:MyApplication.messageListenersList) {
                        ml.handleMsg(message);
                        ml.handleMsg(message1);
                    }
                }
            }

            //判断是不是自己的消息，若是自己的消息，不提醒
            if(chatMessage.getUid()!=UserInfo.getInstance().getUid()&&!isForeground(MyApplication.getContext(),"com.handsome.qhb")){
                mBuilder.setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.logo)
                        .setLargeIcon(LargeBitmap)
                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                        .setAutoCancel(true);
                Intent intent = new Intent(MyApplication.getContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(MyApplication.getContext(),NOTIFYID_1,intent,0);
                mBuilder.setContentIntent(pendingIntent);
                MyApplication.getNotificationManager().notify(NOTIFYID_1, mBuilder.build());
            }
            //添加到数据库
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

    private boolean isForeground(Context context,String className){
        if(context==null|| TextUtils.isEmpty(className)){
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if(list!=null&&list.size()>0){
            ComponentName cpn = list.get(0).topActivity;
            if(cpn.getClassName().contains(className)){
                return true;
            }
        }
        return  false;
    }

}