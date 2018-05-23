package com.hispeed.development;

import com.hispeed.development.domain.config.SysConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-17
 * @desc
 */
@Controller
public class ConfigController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigController.class);


    @Autowired
    ConfigRepository configRepository;
    /**
     * @param request
     * @param response
     * @return pool-size=10&delay=5
     */
    @RequestMapping(value = "execute", method = {RequestMethod.GET, RequestMethod.POST})
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.debug("ConfigSubject配置被观察者接口开始执行......");
        String pool_size = request.getParameter("pool-size") == null ? "10" : request.getParameter("pool-size");
        String delay = request.getParameter("delay") == null ? "5" : request.getParameter("delay");
        ConfigCommandLineRunner.configExec.shutdown();
        if (ConfigCommandLineRunner.configExec.isShutdown()) {
            ConfigCommandLineRunner.configExec = Executors.newScheduledThreadPool(Integer.valueOf(pool_size));
            // 定义配置更新被观察者
            ConfigSubject configSubject = new ConfigSubject
                    (ConfigCommandLineRunner.configExec,
                            Integer.valueOf(pool_size),
                            0,
                            Integer.valueOf(delay),
                            TimeUnit.SECONDS);
            configSubject.runExec();

            LOGGER.debug("新配置: pool_size={}, delay={}秒设置完毕，执行新的同步操作", pool_size, delay);
            request.setAttribute("return", "配置刷新线程池建立完成-->pool_size="
                    + pool_size + ",delay=" + delay);
            return "configure";
        }
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("return", "默认线程池尚未完全关闭，请稍等然后重试");
        return "redirect:/configure.html";
    }


    /**
     * 页面路由
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "configure", method = {RequestMethod.GET})
    public String configure(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.debug("进入配置页面......");
        /**加载所有配置项*/
        List<SysConfig> sysConfigs = configRepository.getAllConfigs();
        if (sysConfigs == null) {
            sysConfigs = new CopyOnWriteArrayList<>();
        }
        for (SysConfig sysConfig : sysConfigs) {
            if (sysConfig.getConfigSwitch().intValue() == 0) {
                sysConfig.setConfigSwitchDesc("启用");
            } else {
                sysConfig.setConfigSwitchDesc("禁用");
            }
        }
        request.setAttribute("sysConfigs", sysConfigs);
        return "configure";
    }

    @RequestMapping(value = "/api/config/disable-config", method = {RequestMethod.GET})
    public String disableConfigAction(@RequestParam(value = "config-id", defaultValue = "0") Integer configId,
                                      HttpServletResponse response) {
        LOGGER.debug("进入配置禁用action，要禁用的配置config-id={}", configId);
        if (configRepository.disableConfig(configId)) {
            return "redirect:/configure.html";
        }
        response.setStatus(500);
        return "redirect:/error.html";
    }

    @RequestMapping(value = "/api/config/enable-config", method = {RequestMethod.GET})
    public String enableConfig(@RequestParam(value = "config-id", defaultValue = "0") Integer configId,
                                      HttpServletResponse response) {
        LOGGER.debug("进入配置启用action，启用的配置config-id={}", configId);
        if (configRepository.enableConfig(configId)) {
            return "redirect:/configure.html";
        }
        response.setStatus(500);
        return "redirect:/error.html";
    }

}
