/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50718
Source Host           : localhost:3306
Source Database       : hopper_v1

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2017-08-21 11:06:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for conf_jdks
-- ----------------------------
DROP TABLE IF EXISTS `conf_jdks`;
CREATE TABLE `conf_jdks` (
  `id` varchar(32) NOT NULL,
  `jdkname` varchar(255) DEFAULT NULL,
  `javahome` varchar(255) DEFAULT NULL,
  `classpath` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for conf_servers
-- ----------------------------
DROP TABLE IF EXISTS `conf_servers`;
CREATE TABLE `conf_servers` (
  `id` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `args` varchar(255) DEFAULT NULL,
  `plat` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for operation
-- ----------------------------
DROP TABLE IF EXISTS `operation`;
CREATE TABLE `operation` (
  `operationid` varchar(32) NOT NULL,
  `serverid` varchar(32) DEFAULT NULL,
  `operationtype` int(1) NOT NULL,
  `operator` varchar(255) DEFAULT NULL,
  `operationtime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`operationid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for path
-- ----------------------------
DROP TABLE IF EXISTS `path`;
CREATE TABLE `path` (
  `pathid` varchar(32) NOT NULL,
  `server_id` varchar(32) DEFAULT NULL,
  `docbase` varchar(255) DEFAULT NULL,
  `contextpath` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pathid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for server
-- ----------------------------
DROP TABLE IF EXISTS `server`;
CREATE TABLE `server` (
  `serverid` varchar(32) NOT NULL,
  `mainport` int(10) DEFAULT NULL,
  `shutport` int(10) DEFAULT NULL,
  `servername` varchar(255) DEFAULT NULL,
  `jdkid` varchar(32) DEFAULT NULL,
  `tomcatid` varchar(32) DEFAULT NULL,
  `opts` varchar(200) DEFAULT '0' COMMENT '0无限制，1操作限制，2全面禁止(废止)',
  `operations` int(255) DEFAULT NULL,
  `lasttime` datetime DEFAULT NULL,
  `mark` text,
  PRIMARY KEY (`serverid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
