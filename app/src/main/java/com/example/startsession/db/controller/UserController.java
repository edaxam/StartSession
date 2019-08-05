package com.example.startsession.db.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.IntegerRes;
import android.util.Log;

import com.example.startsession.db.DBHelper;
import com.example.startsession.db.model.UserModel;

import java.util.ArrayList;

public class UserController {
    private DBHelper dbHelper;
    private String TABLE_NAME = "user";

    public UserController(Context context){
        dbHelper = new DBHelper(context);
    }

    public long addUser(UserModel user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valuesInsert = new ContentValues();
        valuesInsert.put("user",user.getUser());
        valuesInsert.put("mail",user.getMail());
        valuesInsert.put("password",user.getPassword());
        valuesInsert.put("name",user.getName());
        valuesInsert.put("last_name",user.getLast_name());
        valuesInsert.put("mother_last_name",user.getMother_last_name());
        valuesInsert.put("active",1);
        valuesInsert.put("status_ws",1);
        valuesInsert.put("admin", user.getAdmin());
        return db.insert(TABLE_NAME,null,valuesInsert);
    }


    public ArrayList<UserModel> getUsers(String campo_search){
        ArrayList<UserModel> users = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] column_names = {"id_user","user","mail","password","name","last_name","mother_last_name","active","status_ws","admin","date_create"};
        String[] args = new String[] {"1"};

        String where = "";
        if(!campo_search.equals("")){
            where = " AND " + column_names + " LIKE '%" + campo_search + "%' ";
        }
        
        Cursor cursor = db.query(TABLE_NAME,column_names,"active =? " + where,args,null,null,null);

        if (cursor == null) {
            return users;
        }
        if (!cursor.moveToFirst()) return users;

        do {

            Long id_userDB = cursor.getLong(0);
            String userDB = cursor.getString(1);
            String mailDB = cursor.getString(2);
            String passwordDB = cursor.getString(3);
            String nameDB = cursor.getString(4);
            String last_nameDB = cursor.getString(5);
            String mother_last_nameDB = cursor.getString(6);
            Integer activeDB = cursor.getInt(7);
            Integer status_wsDB = cursor.getInt(8);
            Integer adminDB = cursor.getInt(9);
            String date_createDB = cursor.getString(10);



            UserModel getUserBD = new UserModel(userDB, mailDB, passwordDB, nameDB, last_nameDB, mother_last_nameDB, date_createDB, activeDB, status_wsDB, id_userDB, adminDB);
            users.add(getUserBD);
        } while (cursor.moveToNext());

        cursor.close();
        return users;
    }



    public int updateUser(UserModel user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valuesUpdate = new ContentValues();
        valuesUpdate.put("user",user.getUser());
        valuesUpdate.put("mail",user.getMail());
        valuesUpdate.put("password",user.getPassword());
        valuesUpdate.put("name",user.getName());
        valuesUpdate.put("last_name",user.getLast_name());
        valuesUpdate.put("mother_last_name",user.getMother_last_name());
        valuesUpdate.put("active",1);
        valuesUpdate.put("status_ws",1);

        String where = "id_user = ?";

        String[] argsUpdate = {String.valueOf(user.getId_user())};
        Log.e("id user", "" + user.getId_user() );
        return db.update(TABLE_NAME, valuesUpdate, where, argsUpdate);
    }



    public int deleteUser(UserModel user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valuesUpdate = new ContentValues();
        valuesUpdate.put("active",2);
        valuesUpdate.put("status_ws",0);

        String where = "id_user = ?";

        String[] argsUpdate = {String.valueOf(user.getId_user())};
        return db.update(TABLE_NAME, valuesUpdate, where, argsUpdate);
    }

    public UserModel login(UserModel user){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] args = new String[] {user.getUser(), user.getPassword()};
        String[] column_names = {"id_user"};

        UserModel userModel = new UserModel(0);

        Cursor cursor = db.query(TABLE_NAME,column_names,"user=? AND password=?",args,null,null,null);
        if(cursor == null){
            return userModel;
        }
        if (!cursor.moveToFirst()) return userModel;

        do {
            userModel.setId_user(cursor.getInt(0));

        } while (cursor.moveToNext());

        cursor.close();
        return userModel;


    }

    public int users(){
        int num_user = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c_user = db.rawQuery("SELECT COUNT(id_user) FROM user", null);

        if(c_user.moveToFirst()){
            num_user = c_user.getInt(0);
        }

        return num_user;
    }

    public int lanzamientos(){
        int num_lanzamientos = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c_lanz = db.rawQuery("SELECT COUNT(id_history) FROM user_history", null);

        if(c_lanz.moveToFirst()){
            num_lanzamientos = c_lanz.getInt(0);
        }
        return num_lanzamientos;

    }

    public String getPassword(String user){
        String password = "";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] args = new String[] {user};
        String[] column_names = {"password"};

        Cursor cursor = db.query(TABLE_NAME,column_names,"user=? ",args,null,null,null);
        if(cursor == null){
            return password;
        }
        if (!cursor.moveToFirst()) return password;

        do {
            password = cursor.getString(0);

        } while (cursor.moveToNext());

        cursor.close();
        return password;
    }

}
