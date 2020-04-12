package com.demo.demolib.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GlideLoder {

    public static void display(Context context, Object path, ImageView imageView){
        Glide.with(context).load(path).into(imageView);
    }

}
