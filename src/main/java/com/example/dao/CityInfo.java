package com.example.dao;

/**
 * Created by damei on 18/1/21.
 * 城市信息
 */
public class CityInfo {
    String cityCode;
    String cityName;
    String cityNameAbbr; //城市名称属性
    String pinyinName;
    String provinceCode;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityNameAbbr() {
        return cityNameAbbr;
    }

    public void setCityNameAbbr(String cityNameAbbr) {
        this.cityNameAbbr = cityNameAbbr;
    }

    public String getPinyinName() {
        return pinyinName;
    }

    public void setPinyinName(String pinyinName) {
        this.pinyinName = pinyinName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }
}
