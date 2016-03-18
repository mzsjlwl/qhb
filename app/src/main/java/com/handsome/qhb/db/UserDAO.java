package com.handsome.qhb.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.handsome.qhb.bean.ShopCar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2016/3/16.
 */
public class UserDAO {
    public static String find (SQLiteDatabase db,Integer uid){
        Cursor cursor = db.rawQuery("select * from user where uid = ?",new String[]{String.valueOf(uid)});
        if(cursor.moveToFirst()){
            String product = cursor.getString(cursor.getColumnIndex("product"));
            return product;
        }
        return null;
    }

    public static void insert(SQLiteDatabase db,Integer uid,String product){
        db.execSQL("insert into user(uid,product) values(?,?)", new String[]{
                String.valueOf(uid), product
        });
    }

    public static void update(SQLiteDatabase db,Integer uid,String product){
        db.execSQL("update user set product = ? where uid = ?", new String[]{
                product, String.valueOf(uid)});
    }
    public static void delete(SQLiteDatabase db,Integer uid){
        db.execSQL("update user set product = ? where uid = ?",new String[]{
                "",String.valueOf(uid)
        } );
    }

    public static String findAddress(SQLiteDatabase db,Integer uid){
        Cursor cursor = db.rawQuery("select * from user where uid = ?",new String[]{String.valueOf(uid)});
        if(cursor.moveToFirst()){
            String address  = cursor.getString(cursor.getColumnIndex("address"));
            return address;
        }
        return null;
    }

    public static void updateAddress(SQLiteDatabase db,Integer uid,String address){
        db.execSQL("update user set address = ? where uid = ?", new String[]{
                address, String.valueOf(uid)});
    }
}
