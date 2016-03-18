package com.handsome.qhb.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhang on 2016/3/16.
 */


public class UserDBOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "user.db";
    public static final String CREATE_USER = "create table user"+
            "(cid integer primary key AUTOINCREMENT,"+
            "uid integer,"+
            "product text,address text)";

    private static  UserDBOpenHelper userDBOpenHelper;
    public UserDBOpenHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);

    }

    public static synchronized  UserDBOpenHelper getInstance(Context context){
        if(userDBOpenHelper==null){
            userDBOpenHelper = new UserDBOpenHelper(context,DATABASE_NAME,null,3);

        }
        return userDBOpenHelper;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
        onCreate(db);
    }

}
