package com.demo.zjwdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FTPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftp);

        initView();

    }

    private void initView() {
        Button dir=findViewById(R.id.dir);
        dir.setOnClickListener(v -> {
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(Property.Url)
                    .build();
            ApiService apiService = retrofit.create(ApiService.class);
            Map<String, String> map=new HashMap<>();
            map.put("appid",Property.CreateAppID("filedir=3Y"));
            map.put("filedir","3Y");
            apiService.loadFtpFileList(map)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                System.out.println(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
        });
    }
}
