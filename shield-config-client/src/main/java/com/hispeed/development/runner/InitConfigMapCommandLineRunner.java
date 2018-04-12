package com.hispeed.development.runner;

import com.hispeed.development.schedule.FetchAllConfigSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-3
 * @desc 初始化配置加载，在容器加载完成就获取一次全量的数据，防止出现空指针
 */
@Component
public class InitConfigMapCommandLineRunner implements CommandLineRunner {

    @Autowired
    InitConfigBean initConfigBean;

    @Override
    public void run(String... strings) throws Exception {
        initConfigBean.initConfigFetchAll();
    }

}


