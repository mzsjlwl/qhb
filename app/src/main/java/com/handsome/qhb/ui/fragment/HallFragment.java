package com.handsome.qhb.ui.fragment;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handsome.qhb.adapter.RoomAdapter;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.ChatMessage;
import com.handsome.qhb.bean.Product;
import com.handsome.qhb.bean.Room;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.db.MessageDAO;
import com.handsome.qhb.db.RoomDAO;
import com.handsome.qhb.db.UserDAO;
import com.handsome.qhb.db.UserDBOpenHelper;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.UserInfo;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/7.
 */
public class HallFragment extends Fragment  {

    private ListView lv_room;
    private static List<Room> roomList = new ArrayList<Room>();
    private RoomAdapter roomAdapter;
    private RequestQueue requestQueue = MyApplication.getmQueue();
    private SQLiteDatabase db ;
    private Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what== Config.INITROOM_MESSAGE){
                LogUtils.e("0x128","-------->");
                roomList = (List<Room>)msg.obj;
                roomAdapter.notifyDataSetChanged();
            }else if(msg.what == Config.ADD_MESSAGE){
                ChatMessage chatMessage = new ChatMessage();
                chatMessage = (ChatMessage) msg.obj;
                addChatMessage(chatMessage);
            }else if(msg.what==Config.ROOM_REFRESH_LASTMESSAGE){
                ChatMessage chatMessage = new ChatMessage();
                chatMessage = (ChatMessage)msg.obj;
                refreshLastMessage(chatMessage);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogUtils.e("HallFragment", "oncreate");
        super.onCreate(savedInstanceState);
        db = MyApplication.getSQLiteDatabase();
        if(UserInfo.getInstance()!=null){
            roomList = RoomDAO.query(MyApplication.getSQLiteDatabase(),UserInfo.getInstance().getUid());
        }
        for(Room r : roomList){
            LogUtils.e("room=====>",r.toString());
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hall,container,false);
        lv_room = (ListView) view.findViewById(R.id.lv_room);
        MyApplication.setRoomHandler(handler);
        roomAdapter = new RoomAdapter(getActivity(),roomList,R.layout.room_list_items,MyApplication.getmQueue());
        lv_room.setAdapter(roomAdapter);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+"Room/sendRoomPHP",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            LogUtils.e("room",response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if(status.equals(0)){
                                LogUtils.e("room","服务器发送失败");
                            }else{
                                Gson gson = new Gson();
                                List<Room> roomLists = gson.fromJson(jsonObject.getString("data"),new TypeToken<List<Room>>(){}.getType());
                                if(roomList!=null&&roomLists!=null){
                                    for(int i = 0;i<roomLists.size();i++){
                                        int j = 0;
                                        for(;j<roomList.size();j++){
                                            if(roomList.get(j).getRid()==roomLists.get(i).getRid()){
                                                break;
                                            }
                                        }
                                        if(j==roomList.size()) {
                                            Room r = new Room();
                                            r = roomLists.get(i);
                                            roomList.add(r);
                                        }
                                    }
                                }
                                Message message = new Message();
                                message.what = Config.INITROOM_MESSAGE;
                                message.obj = roomList;
                                handler.handleMessage(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        MyApplication.getmQueue().add(stringRequest);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        LogUtils.e("hallFragment====>", "onstart");
//        if(roomList==null){
//            roomList = new ArrayList<Room>();
//            if(UserInfo.getInstance()!=null){
//                roomList = RoomDAO.query(MyApplication.getSQLiteDatabase(),UserInfo.getInstance().getUid());
//            }
//        }
    }

    @Override
    public void onPause() {
        LogUtils.e("hallFragment====>", "onPause");
        super.onPause();
        //先从数据库中获取房间信息,对比后更新房间数据
       List<Room> rooms = new ArrayList<Room>();
        if(UserInfo.getInstance()!=null&&rooms!=null){
            rooms = RoomDAO.query(db,UserInfo.getInstance().getUid());
        }
        if(roomList!=null&&rooms!=null&&UserInfo.getInstance()!=null){
            for(int i = 0;i<roomList.size();i++){
                int j = 0;
                for(;j< rooms.size();j++){
                    if(rooms.get(j).getRid()==roomList.get(i).getRid()){
                        //更新未读消息
                        if(roomList.get(i).getChatMessageList()!=null&&roomList.get(i).getChatMessageList().size()!=0){
                            RoomDAO.updateMessage(MyApplication.getSQLiteDatabase(),
                                    MyApplication.getGson().toJson(roomList.get(i).getChatMessageList()),
                                    roomList.get(i).getRid());
                        }
                        break;
                    }
                }
                if(j==rooms.size()){
                    RoomDAO.insert(db,roomList.get(i).getRid(),UserInfo.getInstance().getUid(),
                           roomList.get(i).getRoomName(),roomList.get(i).getRoomCreater(),
                            MyApplication.getGson().toJson(roomList.get(i).getLastMessage()),
                            MyApplication.getGson().toJson(roomList.get(i).getChatMessageList()));
                }
            }
        }



    }

    @Override
    public void onResume() {
        super.onResume();
        if(roomAdapter!=null){
            roomAdapter.notifyDataSetChanged();
        }
//        roomList.clear();
    }


    public void refreshMessage(){

    }

    public void addChatMessage(ChatMessage msg){
        for(int i = 0;i<roomList.size();i++){
            if(roomList.get(i).getRid()==msg.getRid()){
                if(roomList.get(i).getChatMessageList()==null){
                    roomList.get(i).setChatMessageList(new ArrayList<ChatMessage>());
                }
                roomList.get(i).getChatMessageList().add(msg);
                roomList.get(i).setLastMessage(msg);

            }
        }
        Collections.sort(roomList, new Comparator<Room>() {
            @Override
            public int compare(Room room, Room t1) {
                        if(room.getLastMessage()==null||t1.getLastMessage()==null){
                            return t1.getLastMessage()==null?-1:1;
                        }
                        String s1 = room.getLastMessage().getDate();
                        String s2 = t1.getLastMessage().getDate();
                        LogUtils.e("s1====>",s1);
                        LogUtils.e("s2===>",s2);
                        if(s2.compareTo(s1)==0){
                            return Integer.valueOf(t1.getRid()).compareTo(Integer.valueOf(
                                    room.getRid()
                            ));
                        }else{
                            return s2.compareTo(s1);
                        }
            }
        });
        roomAdapter.notifyDataSetChanged();
    }

    public void refreshLastMessage(ChatMessage msg){
        //设置数据
        for(int i = 0;i<roomList.size();i++){
            if(roomList.get(i).getRid()==msg.getRid()){
                if(roomList.get(i).getChatMessageList()==null){
                    roomList.get(i).setChatMessageList(new ArrayList<ChatMessage>());
                }
                roomList.get(i).setLastMessage(msg);
            }
        }
        //排序
        Collections.sort(roomList, new Comparator<Room>() {
            @Override
            public int compare(Room room, Room t1) {
                if(room.getLastMessage()==null||t1.getLastMessage()==null){
                    return t1.getLastMessage()==null?-1:1;
                }
                String s1 = room.getLastMessage().getDate();
                String s2 = t1.getLastMessage().getDate();
                LogUtils.e("s1====>",s1);
                LogUtils.e("s2===>",s2);
                if(s2.compareTo(s1)==0){
                    return Integer.valueOf(t1.getRid()).compareTo(Integer.valueOf(
                            room.getRid()
                    ));
                }else{
                    return s2.compareTo(s1);
                }
            }
        });
        //更新数据
        roomAdapter.notifyDataSetChanged();

    }
}
