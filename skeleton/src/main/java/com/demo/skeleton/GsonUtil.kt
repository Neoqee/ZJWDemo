package com.demo.skeleton

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object GsonUtil {
    private val gson=Gson()

    fun toJson(any: Any):String{
        return gson.toJson(any)
    }

    fun <T> fromJson(jsonStr:String,cls:Class<T>):T{
        return gson.fromJson(jsonStr,cls)
    }

    fun <T> fromJson(jsonStr: String,type:Type):T{
//        var myType=object : TypeToken<Any>(){}.type
        return gson.fromJson(jsonStr,type)
    }

    fun <T> fromObject(any: Any,type: Type):T{
        val json = toJson(any)
        return fromJson(json,type)
    }

}