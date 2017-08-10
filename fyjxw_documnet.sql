/*
Navicat MySQL Data Transfer

Source Server         : lanmeiniu
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : government_document_crawler

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-07-27 18:28:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for fyjxw_documnet
-- ----------------------------
DROP TABLE IF EXISTS `fyjxw_documnet`;
CREATE TABLE `fyjxw_documnet` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件id',
  `title` varchar(500) NOT NULL COMMENT '文件标题',
  `url` varchar(500) NOT NULL COMMENT '文件网址',
  `date` date DEFAULT NULL COMMENT '文章发表时间',
  `editor` varchar(255) DEFAULT NULL,
  `url_attachment` varchar(2000) DEFAULT NULL COMMENT '文章附件的地址',
  `content` text COMMENT '文章内容',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `url_unique` (`url`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
