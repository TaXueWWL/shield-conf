/*
Navicat MySQL Data Transfer

Source Server         : 172.30.61.11
Source Server Version : 50721
Source Host           : 172.30.61.11:3306
Source Database       : gyweixin

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-04-17 19:08:47
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
  `PROJECT_NAME` varchar(255) NOT NULL DEFAULT 'common' COMMENT '工程名，公共配置为common',
  `MODULE_ID` int(11) DEFAULT NULL COMMENT '模块id',
  `MODULE_NAME` varchar(255) DEFAULT NULL,
  `CONFIG_SWITCH` int(1) DEFAULT '1' COMMENT '配置开关，默认为0-开启，1-关闭',
  `OPT_USER` varchar(32) DEFAULT 'administrator',
  `INSERT_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
  `UPDATE_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`CONFIG_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES ('1', 'name1', 'snowalker1', null, null, 'shop-portal-server', null, null, '0', 'administrator', '2018-04-02 20:51:45', '2018-04-02 20:51:45');
INSERT INTO `sys_config` VALUES ('3', 'name3', 'snowalker3', '测试配置', null, 'shop-portal-server', null, null, '0', 'administrator', '2018-04-03 15:08:42', '2018-04-03 15:08:42');
INSERT INTO `sys_config` VALUES ('4', 'name4', 'snowalker412', null, null, 'shop-portal-server', null, null, '0', 'administrator', '2018-04-03 16:32:28', '2018-04-03 16:32:28');
INSERT INTO `sys_config` VALUES ('5', 'name5', 'snowalker5666', null, null, 'shop-portal-server', null, null, '0', 'administrator', '2018-04-03 16:35:43', '2018-04-03 16:35:43');
INSERT INTO `sys_config` VALUES ('7', 'QINQIN', '海纳百川123', null, null, 'shop-portal-server', null, null, '0', 'administrator', '2018-04-09 10:35:25', '2018-04-09 10:35:25');
INSERT INTO `sys_config` VALUES ('11', 'switch', 'close', null, null, 'common', null, null, '0', 'administrator', '2018-04-09 20:04:52', '2018-04-09 20:04:52');
INSERT INTO `sys_config` VALUES ('12', 'name666', '123123', null, null, 'common', null, null, '0', 'administrator', '2018-04-17 18:26:21', '2018-04-17 18:26:21');
INSERT INTO `sys_config` VALUES ('13', '武文良', '111111', null, null, 'common', null, null, '0', 'administrator', '2018-04-17 18:29:29', '2018-04-17 18:29:29');
INSERT INTO `sys_config` VALUES ('14', 'DADDY', '你爸爸1231', null, null, 'common', null, null, '0', 'administrator', '2018-04-17 18:36:15', '2018-04-17 18:36:15');
INSERT INTO `sys_config` VALUES ('15', 'mom123', '23333', null, null, 'common', null, null, '0', 'administrator', '2018-04-17 18:38:38', '2018-04-17 18:38:38');
INSERT INTO `sys_config` VALUES ('16', '阿萨德撒旦撒大123', '阿萨德撒旦撒123123', null, null, 'common', null, null, '0', 'administrator', '2018-04-17 18:56:17', '2018-04-17 18:56:17');
