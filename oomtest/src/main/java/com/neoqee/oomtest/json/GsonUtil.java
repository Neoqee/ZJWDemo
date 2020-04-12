package com.neoqee.oomtest.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class GsonUtil {

    private static Gson gson = new Gson();

    public static <T> T fromJson(String json,Class<T> cls){
        return gson.fromJson(json,cls);
    }

    public static <T> T fromObject(Object object,Class<T> cls){
        String s = gson.toJson(object);
        return gson.fromJson(s,cls);
    }

}
