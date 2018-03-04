package com.example.dao;

/**
 * Created by damei on 18/1/16.
 *
 */
public class DirectiveItems {
    String content; //TTS播报内容 AUDIO 的url连接
    String type;    //类型：1.TTS 2.AUDIO

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
