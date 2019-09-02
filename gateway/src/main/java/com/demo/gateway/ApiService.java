package com.demo.gateway;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @POST("session")
    @Headers({"Content-Type:application/json;charset=UTF-8"})
    Call<ResponseBody> createSession(@Header("Authorization") String authorization);

    @PUT("order/{orderid}/transaction/{transactionid}")
    @Headers({"Content-Type:application/json;charset=UTF-8"})
    Call<ResponseBody> completeSession(@Header("Authorization") String authorization, @Path("orderid") String orderId,@Path("transactionid") String transactionId, @Body RequestBody requestBody);

}
