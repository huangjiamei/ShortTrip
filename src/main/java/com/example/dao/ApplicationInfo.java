package com.example.dao;

/**
 * Created by damei on 18/1/16.
 */
public class ApplicationInfo {
    String application_name; //开发者在平台创建应用时，填写的应用名称
    String application_id;   //开发者在平台创建应用时，平台分配的ID
    public String getApplication_name() {
        return application_name;
    }

    public void setApplication_name(String application_name) {
        this.application_name = application_name;
    }

    public String getApplication_id() {
        return application_id;
    }

    public void setApplication_id(String application_id) {
        this.application_id = application_id;
    }

}
