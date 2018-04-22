package com.hispeed.development.portal.api;

import com.alibaba.fastjson.JSON;
import com.hispeed.development.domain.config.PullCallbackProtocol;
import com.hispeed.development.util.JedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-19
 * @desc 子系统拉取后回调接口，使用redis做统一的子系统SYN信息存储
 */
@RestController
public class PullCallbackController {

    private final static Logger LOGGER = LoggerFactory.getLogger(PullCallbackController.class);

    /**
     * 回调接口
     * @param request
     * @param response
     * @return ACK表示接受客户端回调并处理完成
     */
    @RequestMapping(value = "pull/callback")
    public String pullCallback(HttpServletRequest request, HttpServletResponse response) {
        String clientInfo = request.getParameter("clientInfo");
        PullCallbackProtocol protocol = JSON.parseObject(clientInfo, PullCallbackProtocol.class);
        JedisClient jedisClient = JedisClient.getInstance();
        jedisClient.setValue(protocol.getClientIp(), protocol.encode(protocol.getClientIp(),
                protocol.getAppName(), protocol.getUpdateTime()));
        LOGGER.info("客户端返回信息:" + jedisClient.getValue(protocol.getClientIp()));
        return "ACK";
    }
}
