package com.antonk.urantestapp;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Anton on 07-Nov-15.
 */
public interface ApiMethods {

    @GET("/test.php ")
    void getCurrentTime(Callback<String> callback);
}