package com.hispeed.development;

import com.hispeed.development.domain.config.SysConfig;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 单机版配置测试服务启动类
 */
@SpringBootApplication
@EnableScheduling
public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    @Autowired
    ConfigRepository configRepository;

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext =
                SpringApplication.run(App.class, args);
        LOGGER.info("shop-portal-server启动完成......");
        LOGGER.debug(Config.get("name3"));
//        for (String str : ConfigHolder.getConfig().keySet()) {
//            System.out.println("key=" + str + ",value=" + Config.get(str));
//        }
//        System.out.println(configRepository.getAllConfigs().size());
        ConfigRepository configRepository =
                (ConfigRepository)configurableApplicationContext.getBean("configRepository");
        SysConfig sysConfig = new SysConfig().setConfigKey("app-name")
                .setConfigValue("snowalker-test")
                .setConfigDesc("测试配置2222")
                .setOptUser("snowalker2222")
                .setProjectName("测试工程2222")
                .setConfigId(20);
        System.out.println(configRepository.updateSysConfig(sysConfig));
    }

    /**
     * @author wuwl@19pay.com.cn
     * @date 2017-3-17
     * @describe 优化tomcat线程数目
     */
    class MyTomcatConnectorCustomizer implements TomcatConnectorCustomizer {
        public void customize(Connector connector) {
            Http11NioProtocol protocol = (Http11NioProtocol) connector
                    .getProtocolHandler();
            // 设置最大连接数
            protocol.setMaxConnections(2000);
            // 设置最大线程数
            protocol.setMaxThreads(2000);
            protocol.setConnectionTimeout(30000);
        }
    }
}
