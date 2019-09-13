package com.example.startsession.db.model;

import java.util.List;

public class ResponseServiceModel {
    private boolean status ;
    private String message ;
    private List<UserModel> log ;

    public ResponseServiceModel() {
    }

    public ResponseServiceModel(boolean status, String message, List<UserModel> log) {
        this.status = status;
        this.message = message;
        this.log = log;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UserModel> getLog() {
        return log;
    }

    public void setLog(List<UserModel> log) {
        this.log = log;
    }
}
