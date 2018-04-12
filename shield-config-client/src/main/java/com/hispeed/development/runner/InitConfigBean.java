package com.hispeed.development.runner;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hispeed.development.cache.ConfigMap;
import com.hispeed.development.config.IConfigService;
import com.hispeed.development.domain.config.SysConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-3
 * @desc 初始化全量配置文件
 */
@Component
public class InitConfigBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitConfigBean.class);


    @Reference(version = "1.0.0")
    IConfigService configService;

    @Value("${spring.application.name}")
    String applicationName;

    @Autowired
    ConfigMap configMap;

    public void initConfigFetchAll() {
        List<SysConfig> sysConfigs = configService.fetchAll(applicationName);
        LOGGER.debug("获取到应用{}的全量配置为:{}", applicationName, sysConfigs.toString());
        for (SysConfig sysConfig : sysConfigs) {
            configMap.set(sysConfig.getConfigKey(), sysConfig);
        }
        LOGGER.debug("初始化加载全量配置完成，应用名--{}", applicationName);
    }
}
