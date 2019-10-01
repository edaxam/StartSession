package com.example.startsession.db.model;

import java.util.List;

public class UserModel {
    private String user;
    private String password;
    private String mail;

    private String name;
    private String last_name;
    private String mother_last_name;


    private String date_create;
    private Integer active;
    private Integer status_ws;
    private Integer admin;

    private long id_user;

    private String conf;

    //CONSTRUCTOR PARA INSERTAR
    public UserModel(String user, String mail, String password,  String name, String last_name, String mother_last_name, String date_create, Integer active, Integer status_ws, Integer admin) {
        this.user = user;
        this.password = password;
        this.mail = mail;
        this.name = name;
        this.last_name = last_name;
        this.mother_last_name = mother_last_name;
        this.date_create = date_create;
        this.active = active;
        this.status_ws = status_ws;
        this.admin = admin;
    }

    //CONSTRUCTOR PARA ACTUALIZAR
    public UserModel(String user, String mail, String password, String name, String last_name, String mother_last_name, String date_create, Integer active, Integer status_ws, long id_user, Integer admin) {
        this.user = user;
        this.password = password;
        this.mail = mail;
        this.name = name;
        this.last_name = last_name;
        this.mother_last_name = mother_last_name;
        this.date_create = date_create;
        this.active = active;
        this.status_ws = status_ws;
        this.id_user = id_user;
        this.admin = admin;
    }

    public UserModel(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public UserModel(long id_user) {
        this.id_user = id_user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMother_last_name() {
        return mother_last_name;
    }

    public void setMother_last_name(String mother_last_name) {
        this.mother_last_name = mother_last_name;
    }

    public String getDate_create() {
        return date_create;
    }

    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }

    public void setDate_create(String date_create) {
        this.date_create = date_create;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getStatus_ws() {
        return status_ws;
    }

    public void setStatus_ws(Integer status_ws) {
        this.status_ws = status_ws;
    }

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }

    public Integer getAdmin() { return admin; }

    public void setAdmin(Integer admin) { this.admin = admin; }


    @Override
    public String toString() {
        return "UserModel{" +
                "id_user='" + id_user + '\'' +
                ", user='" + user + '\'' +
                ", password=" + password +
                ", mail=" + mail +
                ", name=" + name +
                ", last_name=" + last_name +
                ", mother_last_name=" + mother_last_name +
                ", date_create=" + date_create +
                ", status_ws=" + status_ws +
                ", admin=" + admin +
                '}';
    }
}
