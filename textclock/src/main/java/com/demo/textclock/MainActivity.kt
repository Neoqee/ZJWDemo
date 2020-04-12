package com.demo.textclock

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    private var mTimer:Timer?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTimer= timer(period = 1000){
            runOnUiThread {
                textClock.doInvalicate()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mTimer?.cancel()
    }
}
