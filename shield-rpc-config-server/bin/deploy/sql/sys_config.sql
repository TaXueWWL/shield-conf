/*
Navicat MySQL Data Transfer

Source Server         : 172.30.61.11
Source Server Version : 50721
Source Host           : 172.30.61.11:3306
Source Database       : gyweixin

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-04-09 09:23:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `CONFIG_ID` int(11) NOT NULL AUTO_INCREMENT,
  `CONFIG_KEY` varchar(64) NOT NULL COMMENT '配置key',
  `CONFIG_VALUE` varchar(256) NOT NULL COMMENT '配置value',
  `CONFIG_DESC` varchar(256) DEFAULT NULL COMMENT '配置描述',
  `PROJECT_ID` int(11) DEFAULT NULL COMMENT '工程id',
  `PROJECT_NAME` varchar(255) NOT NULL,
  `MODULE_ID` int(11) DEFAULT NULL COMMENT '模块id',
  `MODULE_NAME` varchar(255) DEFAULT NULL,
  `CONFIG_SWITCH` int(1) DEFAULT '0' COMMENT '配置开关，默认为0-开启，1-关闭',
  `OPT_USER` varchar(32) DEFAULT 'administrator',
  `INSERT_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
  `UPDATE_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`CONFIG_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
