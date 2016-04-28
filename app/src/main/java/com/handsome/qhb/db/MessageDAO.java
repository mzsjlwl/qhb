package com.handsome.qhb.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.handsome.qhb.bean.ChatMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2016/3/29.
 */
public class MessageDAO  {
    public static List<ChatMessage> query (SQLiteDatabase db,Integer rid){
        Cursor cursor = db.rawQuery("select * from message where rid = ?",new String[]{String.valueOf(rid),

       });

        List<ChatMessage> messageList = new ArrayList<ChatMessage>();
        if(cursor.moveToFirst()){
            do{
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(cursor.getInt(cursor.getColumnIndex("id")));
                chatMessage.setRid(cursor.getInt(cursor.getColumnIndex("rid")));
                chatMessage.setNackname(cursor.getString(cursor.getColumnIndex("nackname")));
                chatMessage.setContent(cursor.getString(cursor.getColumnIndex("content")));
                chatMessage.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                chatMessage.setType(cursor.getInt(cursor.getColumnIndex("type")));
                chatMessage.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
                chatMessage.setDate(cursor.getString(cursor.getColumnIndex("date")));
                chatMessage.setBonus_total(cursor.getFloat(cursor.getColumnIndex("bonus_total")));
                chatMessage.setDsTime(cursor.getInt(cursor.getColumnIndex("dsTime")));
                chatMessage.setPhoto(cursor.getString(cursor.getColumnIndex("photo")));
                messageList.add(chatMessage);
            }while(cursor.moveToNext());
        }
        return messageList;
    }

    public static void insert(SQLiteDatabase db,Integer id,Integer rid,Integer uid,String content,String nackname,String date,int status,int type,float bonus_total,int dsTime,String photo){
        db.execSQL("insert into message(id,rid,uid,content,nackname,date,status,type,bonus_total,dsTime,photo) values(?,?,?,?,?,?,?,?,?,?,?)", new String[]{
                String.valueOf(id),String.valueOf(rid),String.valueOf(uid),content,nackname,date,String.valueOf(status),String.valueOf(type),String.valueOf(bonus_total),String.valueOf(dsTime),photo
        });
    }

    public static void updateStatus(SQLiteDatabase db,int status,int id){
        db.execSQL("update message set status = ? where id = ?", new String[]{
                String.valueOf(status), String.valueOf(id)});
    }

    public static String getStatus(SQLiteDatabase db,int id,int type){


        Cursor cursor = db.rawQuery("select * from message where id = ? and type = ?",new String[]{
                String.valueOf(id),String.valueOf(type)});
        if(cursor.moveToFirst()){
            String status  = cursor.getString(cursor.getColumnIndex("status"));
            return status;
        }
        return null;
    }

    public static String getDBPhoto(SQLiteDatabase db,int uid){
        Cursor cursor = db.rawQuery("select * from message where uid = ? order by mid desc limit 1",new String[]{String.valueOf(uid),
        });

        if(cursor.moveToFirst()){
            String photo = cursor.getString(cursor.getColumnIndex("photo"));
            return photo;
        }
        return "";
    }

    public static void updateDBPhoto(SQLiteDatabase db,int uid,String photo){
        db.execSQL("update message set photo = ? where uid = ?", new String[]{photo,String.valueOf(uid)});
    }



}
