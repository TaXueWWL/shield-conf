package com.hispeed.development.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.hispeed.development.config.IConfigService;
import com.hispeed.development.domain.config.SysConfig;
import com.hispeed.development.mapper.SysConfigMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-2
 * @desc 系统配置实现类service
 */
@Service(version = "1.0.0", timeout = 5000)
public class SysConfigServiceImpl implements IConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysConfigServiceImpl.class);

    @Autowired
    SysConfigMapper sysConfigMapper;

    /**
     * 新增配置到数据库中，注意幂等
     *
     * @param sysConfig
     */
    @Override
    public void set(SysConfig sysConfig) {
        // 库中已经存在不再插入
        LOGGER.debug("判断是否已经存在配置项:sysConfig={}", sysConfig.toString());
        if (null == get(sysConfig.getConfigKey(), sysConfig.getProjectName())) {
            sysConfigMapper.set(
                    new SysConfig().setProjectName(sysConfig.getProjectName())
                            .setConfigKey(sysConfig.getConfigKey())
                            .setConfigValue(sysConfig.getConfigValue())
                            .setConfigDesc(sysConfig.getConfigDesc())
            );
            LOGGER.debug("数据源中不存在配置项:sysConfig={}, 将配置项持久化完成", sysConfig.toString());
            return;
        }
        LOGGER.debug("数据源中存在配置项:sysConfig={}, 不进行操作", sysConfig.toString());
    }

    @Override
    public SysConfig get(String key, String projectName) {
        Map<String, String> params = new ConcurrentHashMap<>();
        params.put("projectName", projectName);
        params.put("configKey", key);

        SysConfig sysConfig = sysConfigMapper.get(params);
        if (null == sysConfig) {
            LOGGER.debug("根据key={}, projectName={}, 获取到的配置项sysConfig={}", key, projectName, null);
            return null;
        }
        LOGGER.debug("根据key={}, projectName={}, 获取到的配置项sysConfig={}", key, projectName, sysConfig.toString());
        return sysConfig;
    }

    @Override
    public List<SysConfig> fetchAll(String projectName) {
        LOGGER.debug("根据projectName={}开始获取全量配置信息", projectName);
        Map<String, String> params = new ConcurrentHashMap<>();
        params.put("projectName", projectName);
        List<SysConfig> sysConfigs = sysConfigMapper.fetchAll(params);
        List<SysConfig> sysConfigFinal = new CopyOnWriteArrayList<>();
        for (SysConfig sysConfig : sysConfigs) {
            String md5Source = sysConfig.getProjectName() + sysConfig.getConfigKey()
                    + sysConfig.getConfigValue();
            sysConfig.setMd5Value(DigestUtils.md5Hex(md5Source));
            sysConfigFinal.add(sysConfig);

        }
        // TODO 收集调用方的端口 ip
        String host = RpcContext.getContext().getRemoteHost();
        LOGGER.debug("远程主机{},根据projectName={}获取全量配置信息--{}", host,projectName, sysConfigFinal.toString());
        return sysConfigFinal;
    }

}
