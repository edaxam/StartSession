package com.example.startsession.interfaces;

import com.example.startsession.db.model.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    @GET("/")
    Call<String> getSomething();

    @POST("add/")
    Call<UserModel> addUser(@Body UserModel user);

    @PUT("update/{id}")
    Call<UserModel> updateUser(@Path("id") int id, @Body UserModel user);

    @DELETE("delete/{id}")
    Call<UserModel> deleteUser(@Path("id") int id);
}