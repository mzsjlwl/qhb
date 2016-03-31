package com.handsome.qhb.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.reflect.TypeToken;
import com.handsome.qhb.application.MyApplication;
import com.handsome.qhb.bean.ChatMessage;
import com.handsome.qhb.bean.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2016/3/29.
 */
public class RoomDAO {

    public static List<Room> query(SQLiteDatabase db,Integer uid){

        Cursor cursor = db.rawQuery("select * from room where uid = ?",new String[]{String.valueOf(uid)});

        List<Room> roomList = new ArrayList<Room>();
        if(cursor.moveToFirst()){
            do{
                Room room = new Room();
                room.setRid(cursor.getInt(cursor.getColumnIndex("rid")));
                room.setRoomCreater(cursor.getString(cursor.getColumnIndex("roomCreater")));
                room.setRoomName(cursor.getString(cursor.getColumnIndex("roomName")));
                room.setLastTime(cursor.getString(cursor.getColumnIndex("lastTime")));
                List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
                chatMessageList = MyApplication.getGson().fromJson(cursor.getString(cursor.getColumnIndex("chatMessage")),
                        new TypeToken<List<ChatMessage>>(){}.getType());
                room.setChatMessageList(chatMessageList);
                roomList.add(room);
            }while(cursor.moveToNext());
        }
        return roomList;
    }

    public static void insert(SQLiteDatabase db,Integer rid,Integer uid,String roomName,String roomCreater,String lastTime,String chatMessage){
        db.execSQL("insert into room(rid,uid,roomName,roomCreater,lastTime,chatMessage) values(?,?,?,?,?,?)", new String[]{
                String.valueOf(rid),String.valueOf(uid),roomName,roomCreater,lastTime,chatMessage
        });
    }

    public static void update(SQLiteDatabase db,String lastTime,Integer rid){
        db.execSQL("update room set lastTime = ? where rid = ?", new String[]{
                lastTime, String.valueOf(rid)});
    }

    public static void updateMessage(SQLiteDatabase db,String chatMessage,Integer rid){
        db.execSQL("update room set chatMessage = ? where rid = ?",new String[]{
                chatMessage,String.valueOf(rid)
        });
    }


    public static void delete(SQLiteDatabase db,Integer rid,Integer uid ){
        db.execSQL("delete from room  where uid = ? and rid = ?",new String[]{
                "",String.valueOf(uid)
        } );
    }

}
