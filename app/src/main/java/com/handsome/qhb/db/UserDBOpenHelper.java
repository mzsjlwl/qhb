package com.handsome.qhb.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhang on 2016/3/16.
 */


public class UserDBOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "user.db";
    public static final String CREATE_SHOPCAR = "create table shopcar"+
            "(cid integer primary key AUTOINCREMENT,"+
            "uid integer,"+
            "product text,address text)";
    public static final String CREATE_ROOMLIST = "create table room "+
            "(id integer primary key AUTOINCREMENT,"+
            "rid integer,uid integer"+
            "roomName varchar(255),"+
            "roomCreater varchar(255))";
    public static final String CREATE_MESSAGE = "create table message"+
            "(mid integer primary key AUTOINCREMENT,)"+
            "(id integer,rid integer,uid integer,content varchar(255),"+
            "nackname varchar(255),date varchar(50),status integer,"+
            "type integer,round integer)";

    private static  UserDBOpenHelper userDBOpenHelper;
    public UserDBOpenHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);

    }

    public static synchronized  UserDBOpenHelper getInstance(Context context){
        if(userDBOpenHelper==null){
            userDBOpenHelper = new UserDBOpenHelper(context,DATABASE_NAME,null,4);

        }
        return userDBOpenHelper;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SHOPCAR);
        db.execSQL(CREATE_ROOMLIST);
        db.execSQL(CREATE_MESSAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
        onCreate(db);
    }

}
