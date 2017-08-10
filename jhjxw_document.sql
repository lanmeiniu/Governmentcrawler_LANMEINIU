/*
Navicat MySQL Data Transfer

Source Server         : xieguotao
Source Server Version : 50716
Source Host           : 192.168.1.107:3306
Source Database       : videoshare

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2017-08-02 14:50:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for jhjxw_document
-- ----------------------------
DROP TABLE IF EXISTS `jhjxw_document`;
CREATE TABLE `jhjxw_document` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件id',
  `title` varchar(500) DEFAULT NULL COMMENT '文件标题',
  `url` varchar(500) DEFAULT NULL COMMENT '文件网址',
  `date` date DEFAULT NULL COMMENT '文章发表时间',
  `url_attachment` varchar(2000) DEFAULT NULL COMMENT '文章附件的地址',
  `content` text COMMENT '文章内容',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `url_unique` (`url`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
