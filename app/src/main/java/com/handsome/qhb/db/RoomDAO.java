package com.handsome.qhb.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

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
                room.setRoomPhoto(cursor.getString(cursor.getColumnIndex("roomPhoto")));
                room.setRoomName(cursor.getString(cursor.getColumnIndex("roomName")));
                if(!TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("lastMessage")))){
                    ChatMessage chatMessage  = new ChatMessage();
                    chatMessage = MyApplication.getGson().fromJson(cursor.getString(cursor.getColumnIndex("lastMessage")),
                            ChatMessage.class);
                    room.setLastMessage(chatMessage);
                }
                List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
                chatMessageList = MyApplication.getGson().fromJson(cursor.getString(cursor.getColumnIndex("chatMessage")),
                        new TypeToken<List<ChatMessage>>(){}.getType());
                room.setChatMessageList(chatMessageList);
                roomList.add(room);
            }while(cursor.moveToNext());
        }
        return roomList;
    }

    public static void insert(SQLiteDatabase db,Integer rid,Integer uid,String roomPhoto,String roomName,String roomCreater,String lastMessage,String chatMessage){
        db.execSQL("insert into room(rid,uid,roomPhoto,roomName,roomCreater,lastMessage,chatMessage) values(?,?,?,?,?,?,?)", new String[]{
                String.valueOf(rid),String.valueOf(uid),roomPhoto,roomName,roomCreater,lastMessage,chatMessage
        });
    }

    public static void update(SQLiteDatabase db,String lastMessage,Integer rid){
        db.execSQL("update room set lastMessage = ? where rid = ?", new String[]{
                lastMessage, String.valueOf(rid)});
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
