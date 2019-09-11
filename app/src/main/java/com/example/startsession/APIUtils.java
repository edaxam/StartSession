package com.example.startsession;

import com.example.startsession.interfaces.UserService;

public class APIUtils {
    private APIUtils(){
    };

    public static final String API_URL = "http://192.168.15.39/Mobility-app/api/login_admin/TGVvbmFyZG9kaXNlclBpZXJvZGFWaW5jaQ==/";

    public static UserService getUserService(){
        return RetrofitClient.getClient(API_URL).create(UserService.class);
    }
}
