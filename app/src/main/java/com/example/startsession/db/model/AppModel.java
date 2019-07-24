package com.example.startsession.db.model;

import android.graphics.drawable.Drawable;
import android.widget.CheckBox;

public class AppModel {
    private String app_name;
    private String app_flag_system;
    Drawable app_icon;
    CheckBox app_check;
    boolean checked;

    public AppModel(String app_name, String app_flag_system, Drawable app_icon) {
        this.app_name = app_name;
        this.app_flag_system = app_flag_system;
        this.app_icon = app_icon;
    }

    public AppModel(String app_name, String app_flag_system, CheckBox app_check) {
        this.app_name = app_name;
        this.app_flag_system = app_flag_system;
        this.app_check = app_check;
    }

    public AppModel(String app_name, Drawable app_icon, boolean checked) {
        this.app_name = app_name;
        this.app_icon = app_icon;
        this.checked = checked;
    }

    public AppModel(String app_name, Drawable app_icon) {
        this.app_name = app_name;
        this.app_icon = app_icon;
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
}
