package com.example.startsession.db.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.startsession.db.DBHelper;
import com.example.startsession.db.model.AppModel;
import com.example.startsession.db.model.UserModel;

import java.util.ArrayList;

public class AppController {
    private DBHelper dbHelper;
    private String TABLE_NAME = "user_config_launcher";

    public AppController(Context context){
        dbHelper = new DBHelper(context);
    }

    public long addApp(AppModel app_conf) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valuesInsert = new ContentValues();
        valuesInsert.put("id_user",app_conf.getId_user());
        valuesInsert.put("app_name",app_conf.getApp_name());
        valuesInsert.put("app_flag_system",app_conf.getApp_flag_system());
        valuesInsert.put("app_image",app_conf.getApp_icon_string());
        valuesInsert.put("active",1);
        valuesInsert.put("status_ws",0);
        Log.e("Insert Apps","id: " + app_conf.getId_user() + "  | app_name: " + app_conf.getApp_name() + "   | pack" + app_conf.getApp_flag_system());
        return db.insert(TABLE_NAME,null,valuesInsert);
    }


    public boolean appActiveByUser(AppModel apps){
        boolean status = false;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] column_names = {"id_user"};
        String[] args = {"1",""+apps.getId_user(),apps.getApp_flag_system()};
        Cursor cursor = db.query(TABLE_NAME,column_names," active = ? AND id_user = ? AND app_flag_system = ?",args,null,null,null);

        if(cursor == null){
            return status;
        }
        if (!cursor.moveToFirst()) return status;

        do {
            status = true;
        } while (cursor.moveToNext());

        cursor.close();
        return status;
    }

    public int num_app(){
        int num_app = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c_app = db.rawQuery("SELECT COUNT(id_config) FROM user_config_launcher GROUP BY app_flag_system", null);

        if(c_app.moveToFirst()){
            num_app = c_app.getInt(0);
        }
        return num_app;
    }
}
