package com.demo.textclock

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.properties.Delegates

class TextClockView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
        ) : View(context, attrs, defStyleAttr) {



    private var mWidth : Float = -1f
    private var mHeight : Float by Delegates.notNull()

    private var mHourR : Float by Delegates.notNull()
    private var mMinuteR : Float by Delegates.notNull()
    private var mSecondR : Float by Delegates.notNull()

    private var mHourDeg: Float by Delegates.notNull()
    private var mMinuteDeg: Float by Delegates.notNull()
    private var mSecondDeg: Float by Delegates.notNull()

    private val mHelperPaint = createPaint(Color.RED)
    private val mPaint = createPaint()

    /**
     * 创建画笔
     */
    private fun createPaint(color: Int = Color.WHITE): Paint {
        return Paint().apply {
            this.color = color
            this.isAntiAlias = true
            this.style = Paint.Style.FILL
        }
    }

    //在onLayout方法中计算View去除padding后的宽高
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mWidth = (measuredWidth - paddingLeft - paddingRight).toFloat()
        mHeight = (measuredHeight - paddingTop - paddingBottom).toFloat()

        mHourR = mWidth * 0.143f
        mMinuteR = mWidth * 0.35f
        mSecondR = mWidth * 0.35f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas==null) return
        //填充背景：黑色
        canvas.drawColor(Color.BLACK)
        canvas.save()
        //移动到View的中心
        canvas.translate(mWidth/2,mHeight/2)

        //绘制各元件
        drawCenterInfo(canvas)
        drawHour(canvas,mHourDeg)
        drawMinute(canvas,mMinuteDeg)
        drawSecond(canvas,mSecondDeg)

        //从原点处向右画一条辅助线
        canvas.drawLine(0f,0f,mWidth,0f,mHelperPaint)

        canvas.restore()
    }

    fun doInvalicate(){
        Calendar.getInstance().run {
            val hour=get(Calendar.HOUR)
            val minute=get(Calendar.MINUTE)
            val second=get(Calendar.SECOND)

            mHourDeg=-360/12f * (hour-1)
            mMinuteDeg=-360/60f * (minute-1)
            mSecondDeg=-360/60f * (second-1)

            invalidate()
        }
    }

    private fun drawCenterInfo(canvas: Canvas) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Calendar.getInstance().run {
            val hour=get(Calendar.HOUR_OF_DAY)
            val minute=get(Calendar.MINUTE)

            mPaint.textSize=mHourR*0.4f
            mPaint.alpha=255
            mPaint.textAlign=Paint.Align.CENTER
            canvas.drawText("$hour:$minute",0f,mPaint.getBottomedY(),mPaint)

            val month=(this.get(Calendar.MONTH)+1).let {
                if(it<10) "0$it" else "$it"
            }
            val day=this.get(Calendar.DAY_OF_MONTH)
            val dayOfWeek=(get(Calendar.DAY_OF_WEEK)-1).toText()

            mPaint.textSize=mHourR*0.16f
            mPaint.alpha=255
            mPaint.textAlign=Paint.Align.CENTER
            canvas.drawText("$month.$day 星期$dayOfWeek",0f,mPaint.getToppedY(),mPaint)
        }
    }

    private fun drawHour(canvas: Canvas, mHourDeg: Float) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        mPaint.textSize=mHourR*0.16f

        canvas.save()
        canvas.rotate(mHourDeg)

        for (i in 0 until 12){
            canvas.save()

            val iDeg=360/12f * i
            canvas.rotate(iDeg)

            mPaint.alpha=if(iDeg+mHourDeg==0f) 255 else (0.6f*255).toInt()
            mPaint.textAlign=Paint.Align.LEFT

            canvas.drawText("${(i+1).toText()}点",mHourR,mPaint.getCenteredY(),mPaint)
            canvas.restore()
        }
        canvas.restore()
    }

    private fun drawMinute(canvas: Canvas, mMinuteDeg: Float) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        mPaint.textSize=mHourR*0.16f

        canvas.save()
        canvas.rotate(mMinuteDeg)

        for (i in 0 until 60){
            canvas.save()

            val iDeg=360/60f * i
            canvas.rotate(iDeg)

            mPaint.alpha=if(iDeg+mMinuteDeg==0f) 255 else (0.6f*255).toInt()
            mPaint.textAlign=Paint.Align.RIGHT

            if(i<59){
                canvas.drawText("${(i+1).toText()}分",mMinuteR,mPaint.getCenteredY(),mPaint)
            }
            canvas.restore()
        }
        canvas.restore()
    }

    private fun drawSecond(canvas: Canvas, mSecondDeg: Float) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        mPaint.textSize=mHourR*0.16f

        canvas.save()
        canvas.rotate(mSecondDeg)

        for (i in 0 until 60){
            canvas.save()

            val iDeg=360/60f * i
            canvas.rotate(iDeg)

            mPaint.alpha=if(iDeg+mSecondDeg==0f) 255 else (0.6f*255).toInt()
            mPaint.textAlign=Paint.Align.LEFT

            if(i<59){
                canvas.drawText("${(i+1).toText()}秒",mSecondR,mPaint.getCenteredY(),mPaint)
            }
            canvas.restore()
        }
        canvas.restore()
    }
    /**
     * 扩展获取绘制文字时在x轴上 垂直居中的y坐标
     */
    private fun Paint.getCenteredY(): Float {
        return this.fontSpacing / 2 - this.fontMetrics.bottom
    }

    /**
     * 扩展获取绘制文字时在x轴上 贴紧x轴的上边缘的y坐标
     */
    private fun Paint.getBottomedY(): Float {
        return -this.fontMetrics.bottom
    }

    /**
     * 扩展获取绘制文字时在x轴上 贴近x轴的下边缘的y坐标
     */
    private fun Paint.getToppedY(): Float {
        return -this.fontMetrics.ascent
    }
    /**
     * 数字转换文字
     */
    private fun Int.toText(): String {
        var result = ""
        val iArr = "$this".toCharArray().map { it.toString().toInt() }

        //处理 10，11，12.. 20，21，22.. 等情况
        if (iArr.size > 1) {
            if (iArr[0] != 1) {
                result += NUMBER_TEXT_LIST[iArr[0]]
            }
            result += "十"
            if (iArr[1] > 0) {
                result += NUMBER_TEXT_LIST[iArr[1]]
            }
        } else {
            result = NUMBER_TEXT_LIST[iArr[0]]
        }

        return result
    }
    companion object {
        private val NUMBER_TEXT_LIST = listOf(
                "日",
                "一",
                "二",
                "三",
                "四",
                "五",
                "六",
                "七",
                "八",
                "九",
                "十"
        )
    }

}