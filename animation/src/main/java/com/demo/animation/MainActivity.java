package com.demo.animation;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private WindowManager.LayoutParams layoutParams;
    private WindowManager windowManager;
    private Button button;

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

        button = new Button(this);
        button.setText("button");
        windowManager = getWindowManager();
        layoutParams = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,  PixelFormat.TRANSPARENT);
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.gravity= Gravity.LEFT|Gravity.TOP;
        layoutParams.type= WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        layoutParams.x=300;
        layoutParams.y=600;
        windowManager.addView(button, layoutParams);

        button.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int rawX= (int) event.getRawX();
        int rawY= (int) event.getRawY();
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                layoutParams.x=rawX;
                layoutParams.y=rawY;
                windowManager.updateViewLayout(button,layoutParams);
                break;
            default:
                break;
        }

        return false;
    }
}
