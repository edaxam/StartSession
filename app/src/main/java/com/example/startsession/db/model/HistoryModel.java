package com.example.startsession.db.model;

public class HistoryModel {
    private Integer id_user;
    private Integer id_config;
    private String date_create;
    private Integer status_ws;
    private Integer admin;

    private long id_history;

    public HistoryModel(Integer id_user, Integer id_config, String date_create, Integer status_ws, Integer admin) {
        this.id_user = id_user;
        this.id_config = id_config;
        this.date_create = date_create;
        this.status_ws = status_ws;
        this.admin = admin;
    }

    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }

    public Integer getId_config() {
        return id_config;
    }

    public void setId_config(Integer id_config) {
        this.id_config = id_config;
    }

    public String getDate_create() {
        return date_create;
    }

    public void setDate_create(String date_create) {
        this.date_create = date_create;
    }

    public Integer getStatus_ws() {
        return status_ws;
    }

    public void setStatus_ws(Integer status_ws) {
        this.status_ws = status_ws;
    }

    public Integer getAdmin() {
        return admin;
    }

    public void setAdmin(Integer admin) {
        this.admin = admin;
    }

    public long getId_history() {
        return id_history;
    }

    public void setId_history(long id_history) {
        this.id_history = id_history;
    }
}
