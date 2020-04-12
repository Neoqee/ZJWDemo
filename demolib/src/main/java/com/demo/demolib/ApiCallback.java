package com.demo.demolib;

public interface ApiCallback {

    void onSuccess(String string);
    void onFailure(Throwable throwable);

}
