package com.handsome.qhb.receiver;

import android.content.Context;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.ChatMessage;
import com.handsome.qhb.bean.RandomBonus;
import com.handsome.qhb.bean.Room;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.utils.LogUtils;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2016/3/22.
 */
public class MessageReceiver extends XGPushBaseReceiver {


    private Gson gson = new Gson();
    private List<Room>  roomList = new ArrayList<Room>();
    private ChatMessage chatMessage;
    private RandomBonus randomBonus;



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

        LogUtils.e("title==>", xgPushTextMessage.getTitle());
        if(xgPushTextMessage.getTitle().equals("Room")) {
            roomList = gson.fromJson(xgPushTextMessage.getContent(), new TypeToken<List<Room>>() {
            }.getType());
            if (roomList != null) {
                for (int i = 0; i < roomList.size(); i++) {
                    LogUtils.e("tongzhi====>", roomList.get(i).toString());
                }
                Message message = new Message();
                message.what = Config.INITROOM_MESSAGE;
                message.obj = roomList;
                MyApplication.getRoomHandler().handleMessage(message);
            }
        }else if(xgPushTextMessage.getTitle().equals("chat")){
            chatMessage = gson.fromJson(xgPushTextMessage.getContent(),new TypeToken<ChatMessage>(){}.getType());
            if(!isInRoomList(chatMessage.getRid())){
                return;
            }
            Message message = new Message();
            message.what = Config.CHAT_MESSAGE;
            message.obj = chatMessage;
            MyApplication.getChatHandler().handleMessage(message);
            LogUtils.e("xgmsg","chat");
        }else if(xgPushTextMessage.getTitle().equals("RandomBonus")){
            //randomBonus = gson.fromJson(xgPushTextMessage.getContent(),RandomBonus.class);
            chatMessage = gson.fromJson(xgPushTextMessage.getContent(),ChatMessage.class);
            //判断是否有这个房间,若没有则不响应
            if(!isInRoomList(chatMessage.getRid())){
                return;
            }
            Message message = new Message();
            message.what = Config.RANDOMBONUS_MESSAGE;
            message.obj = chatMessage;
            MyApplication.getChatHandler().handleMessage(message);
            LogUtils.e("randombonus","=====>message");
        }else if(xgPushTextMessage.getTitle().equals("CDSBonus")){
            chatMessage = gson.fromJson(xgPushTextMessage.getContent(),ChatMessage.class);
            //判断是否有这个房间,若没有则不响应
            if(!isInRoomList(chatMessage.getRid())){
                return;
            }
            Message message = new Message();
            message.what = Config.CDSBONUS_MESSAGE;
            message.obj = message;
            MyApplication.getChatHandler().handleMessage(message);
            LogUtils.e("CDSBonus","=======>message");
        }

        LogUtils.e("not Room", xgPushTextMessage.getContent());

    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {

    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {

    }

    public boolean isInRoomList(int rid){
        int i = 0;
        for(;i<MyApplication.getRooms().size();i++){
            if(MyApplication.getRooms().get(i)==rid){
                break;
            }
        }

        if(i==MyApplication.getRooms().size()){
            return false;
        }
        return true;
    }
}