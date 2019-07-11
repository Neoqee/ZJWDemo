package com.demo.animation;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView=findViewById(R.id.text_view);
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.animation_test);
//        textView.startAnimation(animation);

        AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f,0.5f);
        alphaAnimation.setDuration(1000);
//        textView.startAnimation(alphaAnimation);

        AnimationSet animationSet=new AnimationSet(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(animation);
        animationSet.setFillAfter(true);
        textView.startAnimation(animationSet);

        ValueAnimator valueAnimator= ObjectAnimator.ofInt(textView,"backgroundColor", Color.RED);
        valueAnimator.setDuration(1000);
        valueAnimator.start();

    }
}
