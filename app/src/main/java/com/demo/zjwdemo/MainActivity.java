package com.demo.zjwdemo;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.UserDictionary;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private int length;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT>=23){
            String[] permissions={
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            for (String permission : permissions) {
                if(this.checkSelfPermission(permission)!=
                        PackageManager.PERMISSION_GRANTED){
                    this.requestPermissions(permissions,100);
                }
            }
        }
        initView();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private void initView() {
        Button button = findViewById(R.id.button);
        textView = findViewById(R.id.text_data);
        button.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            if(intent.resolveActivity(getPackageManager())!=null){
                startActivityForResult(intent,1);
            }
        });
        final Button upload = findViewById(R.id.upload);
        upload.setOnClickListener(v -> scaleFile());
        Button ftp = findViewById(R.id.ftp);
        ftp.setOnClickListener(v -> {
            startActivity(new Intent(this,FTPActivity.class));
        });
    }

    private void uploadFtpFile(){
        try (InputStream inputStream =
                     getContentResolver().openInputStream(uri)) {
            if (inputStream!=null){
                byte[] bytes=new byte[inputStream.available()];
                length=bytes.length;
                String file=Base64.encodeToString(bytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadFile() {
        if(!TextUtils.isEmpty(textView.getText())){
            String path = textView.getText().toString();
            File file = new File(path);
            length= (int) file.length();
            String fileStr=file2StrByBase64(file);
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(Property.Url)
                    .build();
            ApiService apiService = retrofit.create(ApiService.class);
//            System.out.println("fileStr:"+fileStr);
            System.out.println("filename:"+file.getName());
            System.out.println("length:"+length);
            System.out.println("base64:"+fileStr.length());
            Map<String,String> map=new HashMap<>();
            String params= "filename="+file.getName()+"&filedir=test&contentLength="+length;
            String appid=Property.CreateAppID(params);
            System.out.println("appid:"+appid);
            map.put("appid",appid);
            map.put("file",fileStr);
            map.put("filename",file.getName());
            map.put("filedir","test");
            map.put("contentLength",length+"");
            apiService.uploadFtpFile(map)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String s = response.body().string();
//                                System.out.println(response.isSuccessful());
//                                String s=response.message();
                                System.out.println(s);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
        }
    }

    private void scaleFile(){
        String path = textView.getText().toString();
        File file = new File(path);
        length= (int) file.length();
        System.out.println("length--->"+length);
        file2StrByBase64(file);
    }

    private String file2StrByBase64(File file) {
        InputStream in = null;
        String base64=null;
        try {
            in = new FileInputStream(file);
            byte[] bytes=new byte[in.available()];
            length=in.read(bytes);
            base64=Base64.encodeToString(bytes, Base64.DEFAULT);
            System.out.println("file--->"+base64.length());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(in!=null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return base64;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(1==requestCode&& Activity.RESULT_OK==resultCode){
            if (data != null) {
                uri = data.getData();
                System.out.println("uri--->"+ uri);
                if (uri != null) {
                    String scheme = uri.getScheme();
                    if("content".equals(scheme)){
                        ContentResolver resolver = this.getContentResolver();
                        String[] filePathColumn = {MediaStore.MediaColumns.DISPLAY_NAME};
                        Cursor cursor = resolver.query(uri, filePathColumn,null,
                                null,null);
                        if(cursor==null){
                            String path = uri.getPath();
                            File file = null;
                            if (path != null) {
                                file = new File(path);
                                textView.setText(file.getName());
                            }
                            return;
                        }
                        if(cursor.moveToFirst()){
                            String name = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
                            textView.setText(name);
                        }
                        cursor.close();
                    }
                }
            }
        }
    }


    private void test1(Uri uri){
        if (uri != null) {
            String filePathByUri = FileUtils.getFilePathByUri(this, uri);
            System.out.println("path--->"+filePathByUri);
            try {
                String readTextFromUri = readTextFromUri(uri);
                System.out.println("text--->"+readTextFromUri.length());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ContentResolver resolver = this.getContentResolver();
        if (uri != null) {
            System.out.println("uriScheme--->"+uri.getScheme());
            System.out.println("type--->"+resolver.getType(uri));
            String[] filePathColumn = {MediaStore.MediaColumns.SIZE, MediaStore.MediaColumns.DISPLAY_NAME};
            Cursor cursor = resolver.query(uri, filePathColumn,null,
                    null,null);
            if(cursor == null){
                String path = uri.getPath();
                textView.setText(path);
                return;
            }
            if(cursor.moveToFirst()){
                System.out.println(Arrays.toString(cursor.getColumnNames()));
                String path = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
                String name = cursor.getString(cursor.getColumnIndex(filePathColumn[1]));
                System.out.println("size--->"+path);
                System.out.println("name--->"+name);
                textView.setText(path);
            }
            cursor.close();
        }
    }

    private String readTextFromUri(Uri uri) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream =
                     getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            byte[] bytes=new byte[inputStream.available()];
            System.out.println("bytes"+bytes.length);
            String base64=Base64.encodeToString(bytes, Base64.DEFAULT);
            System.out.println("base64--->"+base64.length());
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }

}
