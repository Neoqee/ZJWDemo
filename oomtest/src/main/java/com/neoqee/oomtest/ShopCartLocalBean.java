package com.neoqee.oomtest;

public class ShopCartLocalBean {
    private int Id;
    private String goodsID;
    private String goodsName;
    private String goodsPhoto;
    private String goodsNumber;
    private String goodsIntegral;
    private String goodsTexture;
    private String goodsPrice;
    private boolean isChecked;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(String goodsID) {
        this.goodsID = goodsID;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsPhoto() {
        return goodsPhoto;
    }

    public void setGoodsPhoto(String goodsPhoto) {
        this.goodsPhoto = goodsPhoto;
    }

    public String getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(String goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public String getGoodsIntegral() {
        return goodsIntegral;
    }

    public void setGoodsIntegral(String goodsIntegral) {
        this.goodsIntegral = goodsIntegral;
    }

    public String getGoodsTexture() {
        return goodsTexture;
    }

    public void setGoodsTexture(String goodsTexture) {
        this.goodsTexture = goodsTexture;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
