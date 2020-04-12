package com.demo.demolib.controller;

import com.demo.demolib.ApiCallback;
import com.demo.demolib.service.ApiService;
import com.demo.demolib.utils.EncodeUtil;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ApiController{

    private static final Retrofit retrofit=new Retrofit.Builder()
            .baseUrl("http://192.168.9.2:88")
            .build();
    private static final ApiService apiService=retrofit.create(ApiService.class);

    public static String getPayWithCreditCardUrl(String orderId) {

        String rawData="orderId"+orderId;
        String accessCode= EncodeUtil.CreateAccessCode(rawData,EncodeUtil.clientSecret);
//        String url = String.format("http://192.168.9.2:82/PayWithCreditCard/$s/$s", orderId, accessCode);
        String url="http://192.168.9.2:82/PayWithCreditCard/"+orderId+"/"+accessCode;
        System.out.println(url);
        return url;
    }

    public static void getBannerList(final ApiCallback apiCallback){
        Call<ResponseBody> bannerList = apiService.bannerApi();
        bannerList.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    apiCallback.onSuccess(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiCallback.onFailure(t);
            }
        });
    }

    public static void getNavList(final ApiCallback apiCallback){
        Call<ResponseBody> navList = apiService.navApi();
        navList.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    apiCallback.onSuccess(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                apiCallback.onFailure(t);
            }
        });
    }

}
