package com.hispeed.development;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-17
 * @desc sql配置类
 */
class SQL {

    /**
     * 查询所有
     */
    static String SQL_GET_ALL_CONFIGS = "SELECT\n" +
            "  t.CONFIG_ID configId,\n" +
            "  t.CONFIG_KEY configKey,\n" +
            "  t.CONFIG_VALUE configValue,\n" +
            "  t.CONFIG_DESC configDesc,\n" +
            "  t.CONFIG_SWITCH configSwitch,\n" +
            "  t.OPT_USER optUser,\n" +
            "  t.PROJECT_NAME projectName,\n" +
            "  DATE_FORMAT(t.INSERT_TIME,'%Y-%m-%d %H:%i:%s') insertTime,\n" +
            "  DATE_FORMAT(t.UPDATE_TIME,'%Y-%m-%d %H:%i:%s') updateTime\n" +
            " FROM\n" +
            "  sys_config t\n" +
            "  where t.CONFIG_SWITCH=0";

    /**
     * 新增配置
     */
    static String INSERT_NEW_SYSCONFIG = "INSERT INTO sys_config(CONFIG_KEY,CONFIG_VALUE,CONFIG_DESC,OPT_USER) VALUES (?,?,?,?)";

    /**
     * 启用配置项
     */
    static String ENABLE_CONFIG = "update sys_config t set t.CONFIG_SWITCH=0 where t.CONFIG_ID=?";

    /**
     * 禁用配置项
     */
    static String DISABLE_CONFIG = "update sys_config t set t.CONFIG_SWITCH=1 where t.CONFIG_ID=?";

    /**
     * 启用配置项
     */
    static String UPDATE_CONFIG = "update sys_config t set t.CONFIG_VALUE=?," +
            "t.CONFIG_DESC=?," +
            "t.OPT_USER=?," +
            "t.PROJECT_NAME=?," +
            "t.UPDATE_TIME=NOW() " +
            "where t.CONFIG_ID=?";
}
