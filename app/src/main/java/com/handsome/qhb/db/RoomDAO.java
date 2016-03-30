package com.handsome.qhb.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
                roomList.add(room);
            }while(cursor.moveToNext());
        }
        return roomList;
    }

    public static void insert(SQLiteDatabase db,Integer rid,Integer uid,String roomName,String roomCreater,String lastTime){
        db.execSQL("insert into room(rid,uid,roomName,roomCreater,lastTime) values(?,?,?,?,?)", new String[]{
                String.valueOf(rid),String.valueOf(uid),roomName,roomCreater,lastTime
        });
    }

    public static void update(SQLiteDatabase db,String lastTime,Integer rid){
        db.execSQL("update room set lastTime = ? where rid = ?", new String[]{
                lastTime, String.valueOf(rid)});
    }

    public static void delete(SQLiteDatabase db,Integer rid,Integer uid ){
        db.execSQL("delete from room  where uid = ? and rid = ?",new String[]{
                "",String.valueOf(uid)
        } );
    }

}
