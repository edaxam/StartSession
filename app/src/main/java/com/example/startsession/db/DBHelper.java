package com.example.startsession.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {


    private static final String
            DATA_BASE_NAME = "mobility_lock",
            USER = "user",
            USER_CONFIG_LAUNCHER = "user_config_launcher",
            HISTORY_USER = "user_history",
            LOG = "log_app";
    private static final int VERSION_DATA_BASE = 1;

    public DBHelper(Context context) {
        super(context, DATA_BASE_NAME, null, VERSION_DATA_BASE);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(id_user integer primary key autoincrement, mail VARCHAR(255),user VARCHAR(255),password VARCHAR(255),name VARCHAR(255),last_name VARCHAR(255),mother_last_name VARCHAR(255),active INTEGER ,date_create TIMESTAMP DEFAULT CURRENT_TIMESTAMP,status_ws INTEGER, admin INTEGER DEFAULT 0, is_last_active INTEGER DEFAULT 0)", USER));
        sqLiteDatabase.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(id_config integer primary key autoincrement, id_user INTEGER, app_name VARCHAR(255), app_flag_system VARCHAR(255), app_image VARCHAR(255),active INTEGER ,date_create TIMESTAMP DEFAULT CURRENT_TIMESTAMP, status_ws INTEGER)", USER_CONFIG_LAUNCHER));
        sqLiteDatabase.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(id_history integer primary key autoincrement, id_user INTEGER, id_config INTEGER, date_create TIMESTAMP DEFAULT CURRENT_TIMESTAMP, status_ws INTEGER)", HISTORY_USER));
        sqLiteDatabase.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(id_log integer primary key autoincrement, id_user INTEGER,log TEXT,date_create TIMESTAMP DEFAULT CURRENT_TIMESTAMP, status_ws INTEGER)", LOG));

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
