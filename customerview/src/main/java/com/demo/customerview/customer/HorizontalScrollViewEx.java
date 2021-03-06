package com.demo.customerview.customer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class HorizontalScrollViewEx extends ViewGroup {

    private static final String TAG="HorizontalScrollViewEx";

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    private int mLastX=0;
    private int mLastY=0;
    private int mLastXIntercept=0;
    private int mLastYIntercept=0;

    private int mChildrenSize;
    private int mChildWidth;
    private int mChildIndex;

    public HorizontalScrollViewEx(Context context) {
        super(context);
        init();
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

//    public HorizontalScrollViewEx(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    private void init(){
        if(mScroller==null){
            mScroller=new Scroller(getContext());
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        boolean intercepted=false;
        int x= (int) ev.getX();
        int y= (int) ev.getY();

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:{
                intercepted=false;
                if(!mScroller.isFinished()){
                    mScroller.abortAnimation();
                    intercepted=true;
                }
                break;}
            case MotionEvent.ACTION_MOVE:{
                int deltaX=x- mLastXIntercept;
                int deltaY=y-mLastYIntercept;
                if(Math.abs(deltaX)>Math.abs(deltaY)){
                    intercepted=true;
                }else {
                    intercepted=false;
                }
                break;}
            case MotionEvent.ACTION_UP:{
                intercepted=false;
                break;}
            default:
                break;
        }

        Log.d(TAG, "Intercepted="+intercepted);
        mLastX=x;
        mLastY=y;
        mLastXIntercept=x;
        mLastYIntercept=y;

        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mVelocityTracker.addMovement(event);

        int x= (int) event.getX();
        int y= (int) event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX=x-mLastX;
                int deltaY=y-mLastY;
                scrollBy(-deltaX,0);
                break;
            case MotionEvent.ACTION_UP:
                int scrollX=getScrollX();
                mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity=mVelocityTracker.getXVelocity();
                if (Math.abs(xVelocity) >= 50) {
                    mChildIndex=xVelocity>0?mChildIndex-1:mChildIndex+1;
                }else {
                    mChildIndex=(scrollX+mChildWidth/2)/mChildWidth;
                }
                mChildIndex=Math.max(0,Math.min(mChildIndex,mChildrenSize-1));
                int dx=mChildIndex*mChildWidth-scrollX;
                smoothScrollBy(dx,0);
                mVelocityTracker.clear();
                break;
            default:
                break;
        }

        mLastX=x;
        mLastY=y;

        return true;
    }

    private void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(getScrollX(),0,dx,dy);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {

        super.onDetachedFromWindow();
        mVelocityTracker.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childLeft=0;
        final int childCount=getChildCount();
        mChildrenSize=childCount;

        for (int i=0;i<childCount;i++){
            final View childView=getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                int childWidth=childView.getMeasuredWidth();
                mChildWidth=childWidth;
                childView.layout(childLeft,0,childLeft+childWidth,childView.getMeasuredHeight());
                childLeft+=childWidth;
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measureWidth=0;
        int measureHeight=0;
        final int childCount=getChildCount();
        measureChildren(widthMeasureSpec,heightMeasureSpec);

        int widthSpaceSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthSpecMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightSpaceSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode=MeasureSpec.getMode(heightMeasureSpec);
        if (childCount == 0) {
            setMeasuredDimension(0,0);
        }else if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            final View childView=getChildAt(0);
            measureWidth=childView.getMeasuredWidth()*childCount;
            measureHeight=childView.getMeasuredHeight();
            setMeasuredDimension(measureWidth, measureHeight);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            final View childView=getChildAt(0);
            measureWidth=childView.getMeasuredWidth()*childCount;
            setMeasuredDimension(measureWidth,heightSpaceSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            final View childView=getChildAt(0);
            measureHeight=childView.getMeasuredHeight();
            setMeasuredDimension(widthSpaceSize,measureHeight);
        }

    }
}
