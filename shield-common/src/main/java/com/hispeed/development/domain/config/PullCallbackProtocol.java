package com.hispeed.development.domain.config;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by snowalker on 2018/4/22./**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-19
 * @desc 子系统拉取配置返回协议
 * clientIp:
 * appName:
 * updateTime:
 */
public class PullCallbackProtocol implements Serializable {

    private static final long serialVersionUID = 7173154101565448136L;

    private String clientIp;
    private String appName;
    private String updateTime;

    public PullCallbackProtocol() {
    }

    public String encode(String clientIp, String appName, String updateTime) {
        PullCallbackProtocol pullCallbackProtocol = new PullCallbackProtocol();
        pullCallbackProtocol.setAppName(appName);
        pullCallbackProtocol.setClientIp(clientIp);
        pullCallbackProtocol.setUpdateTime(updateTime);
        return JSON.toJSONString(pullCallbackProtocol);
    }

    public PullCallbackProtocol decode(String encodeObject) {
        PullCallbackProtocol pullCallbackProtocol = JSON.parseObject(encodeObject, PullCallbackProtocol.class);
        return pullCallbackProtocol;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
