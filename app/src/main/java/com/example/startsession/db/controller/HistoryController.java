package com.example.startsession.db.controller;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.startsession.db.DBHelper;
import com.example.startsession.db.model.HistoryModel;

import java.util.ArrayList;

public class HistoryController {
    private DBHelper dbHelper;
    private String TABLE_NAME = "user";

    public HistoryController(Context context){
        dbHelper = new DBHelper(context);
    }

    public long addHistory(HistoryModel history) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valuesInsert = new ContentValues();
        valuesInsert.put("id_user", history.getId_user() );
        valuesInsert.put("id_config", history.getId_config());
        valuesInsert.put("date_create", history.getDate_create());
        valuesInsert.put("status_ws", history.getStatus_ws());
        valuesInsert.put("admin", history.getAdmin());

        return db.insert(TABLE_NAME,null,valuesInsert);
    }

    /*public ArrayList<HistoryModel> getHistory(){
        ArrayList<HistoryModel> history = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] column_names = {"id_history", "id_user", "id_config", "date_create","status_ws", "admin"};
        String[] args = new String[] {"1"};
        Cursor cursor = db.query(TABLE_NAME,column_names,"active =?",args,null,null,null);

        if (cursor == null) {
            return history;
        }
        if (!cursor.moveToFirst()) return history;

        do {

            Long id_historyDB = cursor.getLong(0);
            Integer id_userDB = cursor.getInt(1);
            Integer id_configDB = cursor.getInt(2);
            String date_createDB = cursor.getString(3);
            Integer status_wsDB = cursor.getInt(4);
            Integer adminDB = cursor.getInt(5);

            HistoryModel getHistoryDB = new HistoryModel(id_historyDB, id_userDB, id_configDB, date_createDB, status_wsDB, adminDB);
            history.add(getHistoryDB);

        } while (cursor.moveToNext());

        cursor.close();
        return history;
    }*/
}
