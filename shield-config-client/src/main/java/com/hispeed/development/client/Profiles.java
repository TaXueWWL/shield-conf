package com.hispeed.development.client;

import com.hispeed.development.cache.ConfigMap;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-2
 * @desc 配置客户端
 * 默认获取本地的配置，基于全量获取的内容进行配置的获取
 * get()默认获取ConfigMap
 * set()直接更新远程，通过定时pull更新本地, 不适合在客户端做set操作
 * <p>如果获取远端失败则使用默认配置，推荐使用get(String key, String defaultValue)<p/>
 */
public class Profiles {

    private Profiles() {}

    private volatile static Profiles profiles;

    public static Profiles getInstance() {
        if (profiles == null) {
            synchronized (Profiles.class) {
                if (profiles == null) {
                    profiles = new Profiles();
                }
            }
        }
        return profiles;
    }

    /**
     * 客户端获取配置项
     * @param key
     * @return
     */
    public static String get(String key) {
        return getInstance().get(key, "");
    }

    /**
     * 客户端获取配置项，如果不存在则使用默认值
     * @param key
     * @return
     */
    public static String get(String key, String defaultValue) {
        return ConfigMap.get(key).getConfigValue() == null ? defaultValue : ConfigMap.get(key).getConfigValue();
    }

}
