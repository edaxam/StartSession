package com.example.startsession.db.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.startsession.db.DBHelper;
import com.example.startsession.db.model.AppModel;

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

    public  void afterInsert(int id_user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String var = "UPDATE "+TABLE_NAME+" SET active=0 WHERE id_user= "+id_user;
        db.execSQL(var);
        //Log.e("Insert Apps",var);
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

    public int getIdConfigByUser(int id_user){
        int id_config = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c_app = db.rawQuery("SELECT id_config FROM user_config_launcher WHERE id_user = " + id_user + " AND active = 1", null);

        if(c_app.moveToFirst()){
            id_config = c_app.getInt(0);
        }
        return id_config;
    }

    public Cursor exportTablas(String tabla){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor registos_tablas = db.rawQuery("SELECT * FROM " + tabla +";", null);
        Log.e("Consulta: ", "" + registos_tablas);
        return registos_tablas;
    }

    public Cursor exportTablaConfig(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor registos_tablas = db.rawQuery("SELECT user.mail,user_config_launcher.app_name,user_config_launcher.app_flag_system,user_config_launcher.app_image,user_config_launcher.date_create,user_config_launcher.active,user_config_launcher.status_ws FROM user INNER JOIN user_config_launcher USING (id_user) WHERE user_config_launcher.status_ws <> 1 ;" , null);
        Log.e("Consulta: ", "" + registos_tablas);
        return registos_tablas;
    }

    public String getUserName(int id_user){
        String user_name="";
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor c_user = database.rawQuery("SELECT user FROM user WHERE id_user="+id_user,null);
        if(c_user.moveToFirst()){
            user_name = c_user.getString(0);
        }
        return user_name;
    }

    public long importConfigApps(AppModel app_conf,int[]status){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valuesInsert = new ContentValues();
        valuesInsert.put("id_user",app_conf.getId_user());
        valuesInsert.put("app_name",app_conf.getApp_name());
        valuesInsert.put("app_flag_system",app_conf.getApp_flag_system());
        valuesInsert.put("app_image",app_conf.getApp_icon_string());
        valuesInsert.put("active",status[0]);
        valuesInsert.put("status_ws",1);
        return db.insert(TABLE_NAME,null,valuesInsert);
    }

    public long importConfigAppsWS(AppModel app_conf){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valuesInsert = new ContentValues();
        valuesInsert.put("id_user",app_conf.getId_user());
        valuesInsert.put("app_name",app_conf.getApp_name());
        valuesInsert.put("app_flag_system",app_conf.getApp_flag_system());
        valuesInsert.put("app_image",app_conf.getApp_icon_string());
        valuesInsert.put("active",app_conf.getActive());
        valuesInsert.put("status_ws",1);
        return db.insert(TABLE_NAME,null,valuesInsert);
    }

    public void clearDatabases(String nameTable){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.delete(nameTable,null,null);
    }

    public Cursor searchAppConfigByID(int id_user) {
        Cursor cursor=null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query="SELECT app_name,app_flag_system,date_create,active,status_ws FROM "+TABLE_NAME+" WHERE id_user="+id_user;
        try {
            cursor=db.rawQuery(query,null);
            cursor.moveToNext();
        } catch (Exception e) {
            Log.e("Error APPS",e.getMessage());
        }

        return cursor;
    }
}
