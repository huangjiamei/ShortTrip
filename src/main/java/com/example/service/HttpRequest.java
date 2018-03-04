package com.example.service;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;


/**
 * Created by damei on 18/1/18.
 * 调用API，发送HTTP请求
 */
public class HttpRequest {
    HttpURLConnection con = null;
    final String address = "http://59.110.30.187:8888";
    /*
    发送GET请求
     */
    public String sendGet(String param) {
        String result = "";
        try {
            URL url = new URL(address + "?" + param); //定义url
            con = (HttpURLConnection) url.openConnection(); //打开与url之间的连接
            //定义连接属性
            con.setRequestMethod("GET");
            con.setConnectTimeout(8000);
            con.setReadTimeout(8000);
            //输入流
            InputStream inputStream = con.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String str;
            while((str = bufferedReader.readLine()) != null)
                result += str;
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(con != null)
                con.disconnect();
        }
        return result;
    }

    /*
    发送POST请求
     */
    public String sendPost(String urlPath, String json) {
        String result = "";
        try{
            URL url = new URL(urlPath);
            con = (HttpURLConnection) url.openConnection();
            //定义连接属性
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type","application/json ; charset=UTF-8");
            if(json != null) {
                //设置文件长度
                byte[] writebytes = json.getBytes();
                con.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                OutputStream out = con.getOutputStream();
                out.write(json.getBytes());
                out.flush();
                out.close();
            }
            if(con.getResponseCode() == 200) {
                InputStream in = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                result = reader.readLine();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(con != null)
                con.disconnect();
        }
        return result;
    }
}
