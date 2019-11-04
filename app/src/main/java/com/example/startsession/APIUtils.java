package com.example.startsession;

import com.example.startsession.interfaces.SendInfo;
import com.example.startsession.interfaces.UserService;

public class APIUtils {
    private APIUtils(){
    }

    public static final String GET_API_URL  = "http://192.168.15.2/Roberto/Mobility-app/api/login_admin/TGVvbmFyZG9kaXNlclBpZXJvZGFWaW5jaQ==/";
    public static final String SEND_API_URL = "http://192.168.15.2/Roberto/Mobility-app/api/load_admin/UGVkYXpvYWxhbWJyZWNvbXBsZXRhbGF0YWJsYQ==/";

    public static UserService getUserService(){
        return RetrofitClient.getClient(GET_API_URL).create(UserService.class);
    }

    public static SendInfo sendInfo(){
        return RetrofitClient.setClient(SEND_API_URL).create(SendInfo.class);
    }
}
