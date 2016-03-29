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
import com.handsome.qhb.bean.Product;
import com.handsome.qhb.bean.Room;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.db.RoomDAO;
import com.handsome.qhb.db.UserDAO;
import com.handsome.qhb.db.UserDBOpenHelper;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.UserInfo;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what== Config.INITROOM_MESSAGE){
                LogUtils.e("0x128","-------->");
                refreshMessage((List<Room>)msg.obj);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = UserDBOpenHelper.getInstance(getActivity()).getWritableDatabase();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hall,container,false);
        lv_room = (ListView) view.findViewById(R.id.lv_room);
        MyApplication.setRoomHandler(handler);
        XGPushManager.registerPush(getActivity());
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
                                List<Room> roomList = gson.fromJson(jsonObject.getString("data"),new TypeToken<List<Room>>(){}.getType());
                                if(roomList!=null){
                                    for(Room r:roomList){
                                        MyApplication.addRooms(r.getRid());
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
    public void onPause() {
        super.onPause();
       List<Room> rooms = new ArrayList<Room>();
        if(UserInfo.getInstance()!=null&&rooms!=null){
            rooms = RoomDAO.query(db,UserInfo.getInstance().getUid());
        }
        if(roomList!=null&&rooms!=null){
            for(int i = 0;i<roomList.size();i++){
                int j = 0;
                for(;j< rooms.size();j++){
                    if(rooms.get(j).getRid()==roomList.get(i).getRid()){
                        break;
                    }
                }
                if(j==rooms.size()){
                    RoomDAO.insert(db,roomList.get(i).getRid(),UserInfo.getInstance().getUid(),
                           roomList.get(i).getRoomName(),roomList.get(i).getRoomCreater());
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void refreshMessage(List<Room> roomList){
        roomAdapter = new RoomAdapter(getActivity(),roomList,R.layout.room_list_items,MyApplication.getmQueue());
        lv_room.setAdapter(roomAdapter);
    }



}
