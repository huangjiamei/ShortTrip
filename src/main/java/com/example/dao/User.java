package com.example.dao;

import java.util.Map;

/**
 * Created by damei on 18/1/16.
 */
public class User {
    String user_id; //用户在平台相对于开发者某个应用的ID，注：应用不同，用户的ID也会不同
    Map<String, String> attributes; //用户具体参数，依据每个应用的不同，参数也不相同，此字段中主要以用户针对当前应用的基础配置，如：默认手机号、默认地址等。
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

}
