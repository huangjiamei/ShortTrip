package com.example.dao;

import java.util.Map;

/**
 * Created by damei on 18/1/16.
 */
public class Session {
    boolean is_new;    //是否为新会话：true 是 false 否
    String session_id; //本次交互会话中的会话ID，由平台方负责保持，在一次会话结束前，此ID不会变化
    Map<String, String> attributes; //会话具体参数，依据每个应用的不同，参数也不相同，此字段中主要以之前的槽值为主。
    public boolean is_new() {
        return is_new;
    }

    public void setIs_new(boolean is_new) {
        this.is_new = is_new;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }


}
