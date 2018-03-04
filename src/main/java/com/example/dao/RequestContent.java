package com.example.dao;

import java.util.Map;

/**
 * Created by damei on 18/1/18.
 * 调用API的请求报文类
 */
public class RequestContent {
    String pkgName;
    String versionName;
    String methodName;
    String argsJSONStr;

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getArgsJSONStr() {
        return argsJSONStr;
    }

    public void setArgsJSONStr(String argsJSONStr) {
        this.argsJSONStr = argsJSONStr;
    }


}
