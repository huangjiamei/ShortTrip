package com.example.dao;

/**
 * Created by damei on 18/1/16.
 * 开发者需要平台推送到音箱设备关联的手机APP展现的内容，其中可以包含：文本、文本+图片、连接等
 */
public class Card {
    String title; //开发者需要平台推送到用户音箱关联的手机APP上展现的标题内容。注：不能超过20个字
    String type;  //APP展现内容类型：1.纯文字 2.文字+图片url 3.外部连接
    String text;  //type为1时使用
    RichContent[] richContents; //type为2时使用 注：APP会依据开发者返回的顺序展示
    String url;   //type为3时使用
}
