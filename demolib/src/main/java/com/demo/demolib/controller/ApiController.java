package com.demo.demolib.controller;

import com.demo.demolib.utils.EncodeUtil;

public class ApiController{

    public static String getPayWithCreditCardUrl(String orderId) {

        String rawData="orderId"+orderId;
        String accessCode= EncodeUtil.CreateAccessCode(rawData,EncodeUtil.clientSecret);
//        String url = String.format("http://192.168.9.2:82/PayWithCreditCard/$s/$s", orderId, accessCode);
        String url="http://192.168.9.2:82/PayWithCreditCard/"+orderId+"/"+accessCode;
        System.out.println(url);
        return url;
    }
}
