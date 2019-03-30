package com.example.vartikasharma.splashscreen.rest;

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

    @FormUrlEncoded
    @POST("/api/driver/update_location_driver/")
    Call<JsonObject> update_driver_location(@Header("Authorization") String token, @Field("lat") double lat, @Field("lon") double lon);

    @GET("/api/driver/get_ride_request_count/")
    Call<JsonObject> get_ride_request_count(@Header("Authorization") String token);

}
