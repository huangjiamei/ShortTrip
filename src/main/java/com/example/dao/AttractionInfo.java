package com.example.dao;

/**
 * Created by damei on 18/1/19.
 * 去哪网的景点信息
 */
public class AttractionInfo {
    String name;
    int id;
    int cityId;
    //String tag;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    /*
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }*/
}
