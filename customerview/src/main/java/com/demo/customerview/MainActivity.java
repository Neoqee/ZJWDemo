package com.demo.customerview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.demo.customerview.customer.CustomDrawable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView img=findViewById(R.id.img);

        CustomDrawable customDrawable=new CustomDrawable(Color.RED);
        img.setBackground(customDrawable);

    }
}
