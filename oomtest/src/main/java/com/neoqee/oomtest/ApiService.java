package com.neoqee.oomtest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("/goods/{goodsId}")
    Call<ResponseBody> getGoodsDetail(@Path("goodsId") String goodsId);

}
