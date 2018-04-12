package com.hispeed.development.cache;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hispeed.development.config.IConfigService;
import com.hispeed.development.domain.config.SysConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-2
 * @desc
 */
@Component
public class ConfigMap {

    @Reference(version = "1.0.0")
    IConfigService configService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigMap.class);

    private static Map<String, SysConfig> config = new ConcurrentHashMap<>();

    public static Map<String, SysConfig> getConfig() {
        return config;
    }

    public static void setConfig(Map<String, SysConfig> config) {
        ConfigMap.config = config;
    }

    public static SysConfig get(String key) {
        return config.get(key);
    }

    public static void set(String key, SysConfig sysConfig) {
        config.put(key, sysConfig);
    }

    public static void remove(String key) {
        config.remove(key);
    }
}
