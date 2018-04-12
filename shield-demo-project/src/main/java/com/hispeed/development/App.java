package com.hispeed.development;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hispeed.development.cache.ConfigMap;
import com.hispeed.development.client.Profiles;
import com.hispeed.development.config.IConfigService;
import com.hispeed.development.domain.config.SysConfig;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableScheduling
public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        LOGGER.info("shop-portal-server启动完成......");
        LOGGER.debug(Profiles.get("name3"));
        for (String str : ConfigMap.getConfig().keySet()) {
            System.out.println("key=" + str + ",value=" + Profiles.get(str));
        }

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
