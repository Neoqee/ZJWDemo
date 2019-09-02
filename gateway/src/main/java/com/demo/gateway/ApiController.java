package com.demo.gateway;

import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.mastercard.gateway.android.sdk.GatewayMap;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ApiController {

    interface CreateSessionCallback {
        void onSuccess(String sessionId, String apiVersion);

        void onError(Throwable throwable);
    }

    interface Check3DSecureEnrollmentCallback {
        void onSuccess(GatewayMap response);

        void onError(Throwable throwable);
    }

    interface CompleteSessionCallback {
        void onSuccess(String result);

        void onError(Throwable throwable);
    }

    public void createSession(CreateSessionCallback callback){

        String str="merchant.TEST010826188:e225c196ea527542a6ab2e5374a06753";
        String base64Str=Base64.encodeToString(str.getBytes(),Base64.NO_WRAP);
        String authorization="Basic "+base64Str;

        Retrofit retrofit=new Retrofit.Builder()
//                .baseUrl("https://gateway-test-merchant-server.herokuapp.com")
                .baseUrl("https://ap-gateway.mastercard.com/api/rest/version/52/merchant/TEST010826188/")
                .build();
        ApiService apiService=retrofit.create(ApiService.class);
        Call<ResponseBody> session = apiService.createSession(authorization);
        session.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String msg=response.body().string();
                    Log.d("createSession", "onResponse: "+msg);
                    GatewayMap map=new GatewayMap(msg);
                    String sessionId = (String) map.get("session.id");
                    callback.onSuccess(sessionId,"52");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("createSession", "onFailure: "+t.getMessage());
            }
        });
    }

    public void check3DSecureEnrollment(final String sessionId, final String amount, final String currency, final String threeDSecureId, final Check3DSecureEnrollmentCallback callback){

    }

    public void completeSession(final String sessionId, final String orderId, final String transactionId, final String amount, final String currency, final CompleteSessionCallback callback){

        GatewayMap request = new GatewayMap()
                .set("apiOperation", "PAY")
                .set("session.id", sessionId)
                .set("order.amount", amount)
                .set("order.currency", currency)
                .set("sourceOfFunds.type", "CARD")
                .set("transaction.source", "INTERNET");
//                .set("transaction.frequency", "SINGLE");

        String jsonBody= new Gson().toJson(request);
        System.out.println(jsonBody);

        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"),jsonBody);

        String str="merchant.TEST010826188:e225c196ea527542a6ab2e5374a06753";
        String base64Str=Base64.encodeToString(str.getBytes(),Base64.NO_WRAP);
        String authorization="Basic "+base64Str;

        Retrofit retrofit=new Retrofit.Builder()
//                .baseUrl("https://gateway-test-merchant-server.herokuapp.com")
                .baseUrl("https://ap-gateway.mastercard.com/api/rest/version/52/merchant/TEST010826188/")
                .build();
        ApiService apiService=retrofit.create(ApiService.class);
        Call<ResponseBody> pay = apiService.completeSession(authorization,orderId,transactionId,requestBody);
        pay.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println(response);
                    System.out.println(response.body());
                    String msg=response.body().string();
                    callback.onSuccess(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("pay", "onFailure: "+t.getMessage());
            }
        });
    }

}
