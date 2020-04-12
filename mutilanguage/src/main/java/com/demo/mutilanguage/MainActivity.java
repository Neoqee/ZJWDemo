package com.demo.mutilanguage;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView=findViewById(R.id.text);
        textView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.text) {
            startActivity(new Intent(this,LanguageActivity.class));
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        System.out.println(1);
        super.attachBaseContext(newBase.getApplicationContext());
//        super.attachBaseContext(MyContextWrapper.wap(newBase));
    }

}
