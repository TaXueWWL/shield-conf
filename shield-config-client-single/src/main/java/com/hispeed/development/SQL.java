package com.hispeed.development;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-17
 * @desc sql配置类
 */
class SQL {

    static String SQL_GET_ALL_CONFIGS = "SELECT\n" +
            "  t.CONFIG_ID configId,\n" +
            "  t.CONFIG_KEY configKey,\n" +
            "  t.CONFIG_VALUE configValue,\n" +
            "  t.CONFIG_DESC configDesc,\n" +
            "  t.CONFIG_SWITCH configSwitch,\n" +
            "  t.OPT_USER optUser,\n" +
            "  DATE_FORMAT(t.INSERT_TIME,'%Y-%m-%d %H:%i:%s') insertTime,\n" +
            "  DATE_FORMAT(t.UPDATE_TIME,'%Y-%m-%d %H:%i:%s') updateTime\n" +
            " FROM\n" +
            "  sys_config t\n" +
            "  where t.CONFIG_SWITCH=0";
}
