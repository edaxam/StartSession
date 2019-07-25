package com.example.startsession.db.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

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
        return db.insert(TABLE_NAME,null,valuesInsert);
    }


    public ArrayList<AppModel> getAppsByUser(AppModel apps){
        ArrayList<AppModel> apps_by_user = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] column_names = {"id_user","app_name","app_flag_system","app_image"};
        String[] args = {"1",""+apps.getId_user()};
        Cursor cursor = db.query(TABLE_NAME,column_names," active = ? AND id_user = ? ",args,null,null,null);

        if (cursor == null) {
            return apps_by_user;
        }
        if (!cursor.moveToFirst()) return apps_by_user;

        do {

            int id_userDB = cursor.getInt(0);
            String app_nameDB = cursor.getString(1);
            String app_flag_systemDB = cursor.getString(2);
            String app_imageDB = cursor.getString(3);


            AppModel getUserBD = new AppModel(id_userDB,app_nameDB,app_flag_systemDB,app_imageDB);
            apps_by_user.add(getUserBD);
        } while (cursor.moveToNext());

        cursor.close();
        return apps_by_user;
    }
}
