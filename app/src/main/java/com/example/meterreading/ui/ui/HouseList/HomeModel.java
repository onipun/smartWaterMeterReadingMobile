package com.example.meterreading.ui.ui.HouseList;

import android.graphics.Bitmap;

public class HomeModel {

    String addr;
    String houseId;
    String IdentityCard;

    public String getIdentityCard() {
        return IdentityCard;
    }

    public void setIdentityCard(String identityCard) {
        IdentityCard = identityCard;
    }

    String index;
    String area;
    Bitmap meterImg;



    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }


    public Bitmap getMeterImg(){
        return meterImg;
    }
    public void setMeterImg(Bitmap meterImg){
        this.meterImg = meterImg;
    }
    public String getHouseId(){
        return houseId;
    }
    public void setHouseId(String houseId){
        this.houseId = houseId;
    }
    public String getAddr(){
        return addr;
    }
    public void setAddr(String addr){
        this.addr = addr;
    }
}
