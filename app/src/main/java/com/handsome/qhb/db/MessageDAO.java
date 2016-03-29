package com.handsome.qhb.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Message;

import com.handsome.qhb.bean.ChatMessage;
import com.handsome.qhb.bean.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2016/3/29.
 */
public class MessageDAO  {
    public static List<ChatMessage> find (SQLiteDatabase db,Integer rid,Integer uid){
        Cursor cursor = db.rawQuery("select * from message where rid = ? and uid = ? ",new String[]{String.valueOf(rid),
        String.valueOf(uid)});

        List<ChatMessage> messageList = new ArrayList<ChatMessage>();
        if(cursor.moveToFirst()){
            do{
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setRid(cursor.getInt(cursor.getColumnIndex("rid")));
                chatMessage.setNackname(cursor.getString(cursor.getColumnIndex("nackname")));
                chatMessage.setContent(cursor.getString(cursor.getColumnIndex("content")));
                chatMessage.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                chatMessage.setType(cursor.getInt(cursor.getColumnIndex("type")));
                chatMessage.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
                chatMessage.setDate(cursor.getString(cursor.getColumnIndex("date")));
                chatMessage.setRound(cursor.getInt(cursor.getColumnIndex("round")));
                messageList.add(chatMessage);
            }while(cursor.moveToNext());
        }
        return messageList;
    }

    public static void insert(SQLiteDatabase db,Integer id,Integer rid,Integer uid,String content,String nackname,String date,int status,int type,int round){
        db.execSQL("insert into message(id,rid,uid,content,nackname,date,status,type,round) values(?,?,?,?,?,?,?,?,?)", new String[]{
                String.valueOf(id),String.valueOf(rid),String.valueOf(uid),content,nackname,date,String.valueOf(status),String.valueOf(type),String.valueOf(round)
        });
    }

    //    public static void update(SQLiteDatabase db,Integer uid,String product){
//        db.execSQL("update room set product = ? where uid = ?", new String[]{
//                product, String.valueOf(uid)});
//    }
    public static void delete(SQLiteDatabase db,Integer rid,Integer uid ){
        db.execSQL("delete from room  where uid = ? and rid = ?",new String[]{
                "",String.valueOf(uid)
        } );
    }
}
