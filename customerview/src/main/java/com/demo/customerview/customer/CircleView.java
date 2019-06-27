package com.demo.customerview.customer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.demo.customerview.R;

public class CircleView extends View {

    private int mWidth=400;
    private int mHeight=400;
    private int mColor;
    private Paint mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);

    //继承View之后需要实现相应的构造函数
    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context,@Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleView(Context context,@Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        mColor=typedArray.getColor(R.styleable.CircleView_circle_color,Color.RED);
        typedArray.recycle();
        init();
    }

    //初始化，设置圆的颜色
    private void init(){
        mPaint.setColor(mColor);
    }

    /**
     * 自定义View直接继承View，需要对wrap_Content和padding做处理
     * 在onDraw中处理padding
     * 在onMeasure中处理wrap_Content的预设值
     */

    //自定义View直接继承View的情况，需要重写onDraw方法，实现绘制
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int paddingLeft=getPaddingLeft();
        final int paddingTop=getPaddingTop();
        final int paddingRight=getPaddingRight();
        final int paddingBottom=getPaddingBottom();
        int width=getWidth()-paddingLeft-paddingRight;
        int height=getHeight()-paddingTop-paddingBottom;
        int radius=Math.min(width,height)/2;
        canvas.drawCircle(paddingLeft+width/2,paddingTop+height/2,radius,mPaint);
    }

    //重写onMeasure方法，测量View的宽高，主要是针对wrap_Content,设置一个初始值，让View可以实现该布局
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * Mode 模式 ps：100dp，match_Parent-->EXACTLY,wrap_Content-->AT_MOST
         * Size 大小 表示宽高的大小
         */
        int widthSpecMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize=MeasureSpec.getSize(heightMeasureSpec);
        if(widthSpecMode==MeasureSpec.AT_MOST&&heightSpecMode==MeasureSpec.AT_MOST){
            setMeasuredDimension(mWidth,mHeight);
        } else if (widthSpecMode==MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth,heightSpecSize);
        }else if (heightSpecMode==MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize,mHeight);
        }
    }
}
