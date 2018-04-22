package com.hispeed.development.schedule;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.base.Preconditions;
import com.hispeed.development.cache.ConfigMap;
import com.hispeed.development.config.IConfigService;
import com.hispeed.development.domain.config.PullCallbackProtocol;
import com.hispeed.development.domain.config.SysConfig;
import com.hispeed.development.util.OkClient;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-2
 * @desc 定时抓取全量配置文件
 * TODO 1.服务端需要感知客户端的更新状态 （业务层的可靠性）2.服务端要对配置进行统一管理
 * TODO 1.应用初始化加载所有配置项返回客户端消息到服务端，2.此处也返回客户端信息到服务端
 * 策略：
 * <p>1. 应用加载的时候会首先调用InitConfigMapCommandLineRunner加载一次</p>
 * <p>2. 然后每隔15s获取全量数据</p>
 * <p>3. 全量数据获取的时候对比本地的MD5和远程MD5，不同则进行替换</p>
 * <p>4. 其余地方直接通过get() set()进行调用即可</p>
 * <p>5. 定时pull逻辑，定时pull获取全量之后进行MD5比对，不同则更新本地，否则抛弃</p>
 */
@Component
public class FetchAllConfigSchedule {

    @Value("${spring.application.name}")
    String applicationName;

    @Value("${config.server.url}")
    String serverUrl;

    @Reference(version = "1.0.0")
    IConfigService configService;

    @Autowired
    ConfigMap configMap;

    StringBuffer stringBuffer;

    private static final Logger LOGGER = LoggerFactory.getLogger(FetchAllConfigSchedule.class);

    @Scheduled(cron = "${config.client.fetchall.cron}")
    public void execute() {
        doWork();
    }

    /**
     * <p>一共有三种情况</p>
     * <p>1. 本地有远程没有，基于本地迭代
     *      需要同步远程，本地优先</p>
     * <p>2. 远程有本地没有，基于远程迭代
     *      直接同步本地</p>
     * <p>3. 本地和远程都有，基于远程迭代
     *      比较MD5进行更新操作</p>
     * <p>当然，存在一种情况，某个配置项不再需要使用，即修改了代码逻辑
     * 删除了该配置<br/>
     * 此时，如果加载应用，根据逻辑还是会获取到不需要的配置项，这时只需要
     * 重启应用，<br/>并在远程删除配置项。如果是集群，可以增加同步配置的时间，保证一致性
     * <br/>当然，我们允许应用中存在一定的配置冗余<p/>
     */
    private void doWork() {
        LOGGER.debug("开始定时获取应用{}全量配置定时任务", applicationName);
        List<SysConfig> sysConfigs = configService.fetchAll(applicationName);
        LOGGER.debug("获取到应用{}的全量配置为:{}", applicationName, sysConfigs.toString());
        stringBuffer = new StringBuffer();
        for (SysConfig sysConfig : sysConfigs) {
            SysConfig localConfig = ConfigMap.get(sysConfig.getConfigKey());
            if (localConfig == null) {
                // 本地不存在配置项，新增配置
                updateLocalConfigIfNotExist(sysConfig);
            } else if (!getMd5Value(localConfig.getProjectName(),
                    localConfig.getConfigKey(),
                    localConfig.getConfigValue(),
                    sysConfig).equals(sysConfig.getMd5Value())) {
                // 相同key对应的配置项MD5比对不相同，更新配置项
                updateLocalConfigIfInvalid(sysConfig);
            }
        }
        // 迭代本地，对比远程，条件：当本地和远程该应用配置数量不一致时触发
        updateServerConfigIfRemoteNotExist(sysConfigs);
        LOGGER.debug("应用{}配置文件更新完毕, 发生变更的配置项列表为:{}", applicationName, stringBuffer.toString());
        // 获取完毕发送响应到服务端
        sendCallbackToServer();
    }

    /**
     * 获取完毕发送响应到服务端
     */
    private void sendCallbackToServer() {
        try {
            String url = serverUrl + "?clientInfo=" +
                    URLEncoder.encode(new PullCallbackProtocol().encode(
                            InetAddress.getLocalHost().getHostAddress(),
                            applicationName,
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:SS")
                                    .format(Calendar.getInstance().getTimeInMillis())
                    ), "UTF-8");
            String result = OkClient.getInstance().sendGet(url);
            LOGGER.debug("服务端返回信息:" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 远程配置项缺失，根据本地配置补充到远程
     * @param sysConfigs
     */
    private void updateServerConfigIfRemoteNotExist(List<SysConfig> sysConfigs) {
        stringBuffer.append(", 根据本地配置同步远程配置: ");
        int remoteConfigSize = sysConfigs.size();
        int localConfigSize = configMap.getConfig().size();
        LOGGER.debug("远程配置数量remoteConfigSize=" + remoteConfigSize +
                " ,本地配置数量localConfigSize=" + localConfigSize);
        if (sysConfigs != null && remoteConfigSize != localConfigSize) {
            // 转储远程配置项的key到局部变量List中
            List<String> remoteConfigKeyList = new CopyOnWriteArrayList<>();
            for (SysConfig sysConfig : sysConfigs) {
                remoteConfigKeyList.add(sysConfig.getConfigKey());
            }
            LOGGER.debug("转储远程配置到临时列表中完成，size={}", remoteConfigKeyList.size());
            for (String key : configMap.getConfig().keySet()) {
                if (!remoteConfigKeyList.contains(key)) {
                    // 同步远程配置项，读本地写远程
                    configService.set(new SysConfig().setConfigKey(key)
                                .setConfigValue(configMap.getConfig().get(key).getConfigValue())
                                .setConfigDesc(key)
                                .setProjectName(applicationName));
                    LOGGER.debug("同步远程配置缺失的本地配置项完成, 配置key={}", key);
                    stringBuffer.append(key + ",");
                }
            }
        }
    }

    /**
     * 相同key对应的配置项MD5比对不相同，更新配置项
     * @param sysConfig
     */
    private void updateLocalConfigIfInvalid(SysConfig sysConfig) {
        // 移除过时的配置项，更新为新的配置项
        configMap.remove(sysConfig.getConfigKey());
        configMap.set(sysConfig.getConfigKey(), sysConfig);
        stringBuffer.append(",key=" + sysConfig.getConfigKey() + ",");
    }

    /**
     * 本地不存在根据远程配置项跟新本地配置
     * @param sysConfig
     */
    private void updateLocalConfigIfNotExist(SysConfig sysConfig) {
        LOGGER.debug("key={}为新增配置，直接更新本地配置", sysConfig.getConfigKey());
        configMap.set(sysConfig.getConfigKey(), sysConfig);
        stringBuffer.append(",key=" + sysConfig.getConfigKey() + ",");
    }

    /**
     * 计算本地配置MD5 哈希值
     * @param projectName
     * @param configKey
     * @param configValue
     * @return
     */
    private String getMd5Value(String projectName, String configKey, String configValue, SysConfig sysConfig) {
        String md5Value = DigestUtils.md5Hex(projectName + configKey + configValue);
        LOGGER.debug("configKey={}对应的本地MD5为--{}, 远程MD5为--{}", configKey, md5Value, sysConfig.getMd5Value());
        return md5Value;
    }

}
