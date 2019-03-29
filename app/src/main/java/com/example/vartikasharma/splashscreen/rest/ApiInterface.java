package com.example.vartikasharma.splashscreen.rest;

import com.example.vartikasharma.splashscreen.models.LoginModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("/api/driver/driver_login/")
    Call<JsonObject> login(@Field("username") String username, @Field("password") String password);

    @POST("/api/driver/update_driver_location/")
    Call<JsonObject> update_driver_location(@Header("Authorization") String token, @Field("lat") Float lat, @Field("lon") Float lon);

}
