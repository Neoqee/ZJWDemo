package com.demo.demolib.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("/api/getbannerlist")
    Call<ResponseBody> bannerApi();

    @GET("/api/getnavlist")
    Call<ResponseBody> navApi();

}
