package com.demo.skeleton


/**
 * 登录数据的Json对象
 * */
data class LoginJsonObject(
    var Id:Int,
    var NickName:String ,
    var Email:String ,
    var Headimgurl:String ,
    var CreateDate:String ,
    var CreateDateText:String
)

/**
 * banner图片链接对象
 * */
data class BannerUrlObject(
    var ImgUrl:String
)

data class NavObject(
    var Id: Int,
    var name : String,
    var ImgUrl: String
)