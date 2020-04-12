package com.demo.skeleton

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.demo.demolib.ApiCallback
import com.demo.demolib.controller.ApiController
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mMyAdapter : MyAdapter
    lateinit var skeletonScreen : SkeletonScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        mMyAdapter= MyAdapter()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation=LinearLayoutManager.HORIZONTAL
        recycler.layoutManager=layoutManager
        mMyAdapter.openLoadAnimation()
        mMyAdapter.isFirstOnly(false)
//        mMyAdapter.setNotDoAnimationCount(3)
//        recycler.adapter=mMyAdapter
        skeletonScreen = Skeleton.bind(recycler)
                .adapter(mMyAdapter)
                .load(R.layout.skeleton_item)
                .show()
        recycler.postDelayed(Runnable { getNavList() }, 3000)
//        getNavList()
    }

    private fun getNavList(){
        ApiController.getNavList(object : ApiCallback {
            override fun onSuccess(response: String) {
                val singleJsonObject = GsonUtil.fromJson(response, SingleJsonObject::class.java)
                println(singleJsonObject)
                if(1==singleJsonObject.code){
                    val navObjects = GsonUtil.fromObject<List<NavObject>>(singleJsonObject.data,
                            object : TypeToken<List<NavObject>>() {}.type)
//                    showRecycler(navObjects.toMutableList())
                    skeletonScreen.hide()
                    mMyAdapter.setNewData(navObjects)
                }
            }

            override fun onFailure(throwable: Throwable) {
                throwable.printStackTrace()
            }

        })
    }

}
