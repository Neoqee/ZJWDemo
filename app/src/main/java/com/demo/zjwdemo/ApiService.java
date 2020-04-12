package com.demo.zjwdemo;


import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @POST(Property.upoadFtpFile)
    @FormUrlEncoded
    Call<ResponseBody> uploadFtpFile(@FieldMap Map<String,String> map);

    @POST(Property.loadFtpFileList_interfaceName)
    @FormUrlEncoded
    Call<ResponseBody> loadFtpFileList(@FieldMap Map<String,String> map);

}
