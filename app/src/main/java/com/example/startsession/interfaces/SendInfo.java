package com.example.startsession.interfaces;

import org.json.JSONArray;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SendInfo{
    @FormUrlEncoded
    @POST("http://192.168.15.2/Roberto/Mobility-app/api/load_admin/UGVkYXpvYWxhbWJyZWNvbXBsZXRhbGF0YWJsYQ==/")
    Call<ResponseBody> savePost(@Field("user") String user,@Field("password") String password,@Field("data") JSONArray data);

}
