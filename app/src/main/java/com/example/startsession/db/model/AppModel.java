package com.example.startsession.db.model;

import android.graphics.drawable.Drawable;
import android.widget.CheckBox;

public class AppModel {
    private int id_user;
    private String app_name;
    private String app_flag_system;
    private String app_icon_string;
    private int active;
    private int status_ws;
    Drawable app_icon;
    CheckBox app_check;
    boolean checked;

    public AppModel(String app_name, String app_flag_system, Drawable app_icon) {
        this.app_name = app_name;
        this.app_flag_system = app_flag_system;
        this.app_icon = app_icon;
    }

    public AppModel(String app_name, String app_flag_system, Drawable app_icon, boolean checked) {
        this.app_name = app_name;
        this.app_flag_system = app_flag_system;
        this.app_icon = app_icon;
        this.checked = checked;
    }

    public AppModel(int id_user, String app_name, String app_flag_system, String app_icon_string) {
        this.id_user = id_user;
        this.app_name = app_name;
        this.app_flag_system = app_flag_system;
        this.app_icon_string = app_icon_string;
    }

    public AppModel(String app_name, String app_flag_system, String app_icon_string,int active, int status_ws){
        this.app_name=app_name;
        this.app_flag_system=app_flag_system;
        this.app_icon_string=app_icon_string;
        this.active=active;
        this.status_ws=status_ws;
    }

    public AppModel(int id_user,String app_name, String app_flag_system, String app_icon_string,int active, int status_ws){
        this.id_user=id_user;
        this.app_name=app_name;
        this.app_flag_system=app_flag_system;
        this.app_icon_string=app_icon_string;
        this.active=active;
        this.status_ws=status_ws;
    }

    public AppModel(int id_user, String app_flag_system) {
        this.id_user = id_user;
        this.app_flag_system = app_flag_system;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getApp_flag_system() {
        return app_flag_system;
    }

    public void setApp_flag_system(String app_flag_system) {
        this.app_flag_system = app_flag_system;
    }

    public Drawable getApp_icon() {
        return app_icon;
    }

    public void setApp_icon(Drawable app_icon) {
        this.app_icon = app_icon;
    }

    public CheckBox getApp_check() {
        return app_check;
    }

    public void setApp_check(CheckBox app_check) {
        this.app_check = app_check;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getApp_icon_string() {
        return app_icon_string;
    }

    public void setApp_icon_string(String app_icon_string) {
        this.app_icon_string = app_icon_string;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getStatus_ws() {
        return status_ws;
    }

    public void setStatus_ws(int status_ws) {
        this.status_ws = status_ws;
    }
}
