package com.demo.mutilanguage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private Configuration configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        TextView textView=findViewById(R.id.change);
        textView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.change) {
            if("en".equals(MyContextWrapper.localeStr)){
                System.out.println("en");
//                MyContextWrapper.localeStr="zh";
                MyApplication.localeStr="zh";
//                configuration = getResources().getConfiguration();
//                Locale locale=new Locale("zh");
//                Locale.setDefault(locale);
//                configuration.setLocale(locale);
//                context = createConfigurationContext(configuration);
//                MyContextWrapper.wap(context);
            }else {
                System.out.println("zh");
//                MyContextWrapper.localeStr="en";
                MyApplication.localeStr="en";
//                configuration=getResources().getConfiguration();
//                Locale locale=new Locale("en");
//                Locale.setDefault(locale);
//                configuration.setLocale(locale);
//                context =
//                        createConfigurationContext(configuration);
//                MyContextWrapper.wap(context);
            }
            Intent intent=new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

        @Override
    protected void attachBaseContext(Context newBase) {
        System.out.println(2);
//        super.attachBaseContext(MyContextWrapper.wap(newBase));
        super.attachBaseContext(newBase.getApplicationContext());
    }
}
