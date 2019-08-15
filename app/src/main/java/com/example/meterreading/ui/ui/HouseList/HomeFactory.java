package com.example.meterreading.ui.ui.HouseList;

import java.util.ArrayList;
import java.util.List;

public class HomeFactory {
    List<HomeModel> homeModels = new ArrayList<>();

    private static HomeFactory instance = null;

    public static HomeFactory getInstance() {
        if(instance == null) {
            instance = new HomeFactory();
        }
        return instance;
    }

    public void setHomeModels(List<HomeModel> homeModels) {
        this.homeModels = homeModels;
    }

    public List<HomeModel> getHomes(){
        return homeModels;

    }

    public HomeModel getHome(String houseId){
        HomeModel homeModel = null;
        for (HomeModel home: homeModels) {
            if ((home.getIndex()).equals(houseId)){
                homeModel =  home;
                break;
            }
        }

        return homeModel;
    }

    public void clear(){
        homeModels.clear();
    }


}
