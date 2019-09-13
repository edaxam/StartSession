package com.example.startsession.interfaces;

import com.example.startsession.db.model.ResponseServiceModel;
import com.example.startsession.db.model.UserModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {
    //http://localhost/Mobility-app/api/login_admin/TGVvbmFyZG9kaXNlclBpZXJvZGFWaW5jaQ==/?user={user}&password={password}
    @GET("Mobility-app/api/login_admin/TGVvbmFyZG9kaXNlclBpZXJvZGFWaW5jaQ==/")
    Call<ResponseServiceModel> getUsers(@Query("user") String user, @Query("password") String password);
}