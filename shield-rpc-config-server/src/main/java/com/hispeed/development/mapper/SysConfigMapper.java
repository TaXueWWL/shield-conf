package com.hispeed.development.mapper;

import com.hispeed.development.domain.config.SysConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-2
 * @desc 系统配置Mapper
 */
@Mapper
public interface SysConfigMapper {

    void set(SysConfig sysConfig);

    SysConfig get(Map<String, String> params);

    List<SysConfig> fetchAll(Map<String, String> params);
}
