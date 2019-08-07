/*
SQLyog Ultimate v12.4.1 (64 bit)
MySQL - 5.7.17-log : Database - blsq
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`sys_blsq` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `sys_blsq`;

/*Table structure for table `sys_dept` */

DROP TABLE IF EXISTS `sys_dept`;

CREATE TABLE `sys_dept` (
  `dept_id` int(20) NOT NULL AUTO_INCREMENT,
  `org_id` int(11) DEFAULT NULL COMMENT '组织Id',
  `user_id` int(11) DEFAULT NULL COMMENT '创建人Id',
  `name` varchar(50) DEFAULT NULL COMMENT '部门名称',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '是否删除  1：已删除  0：正常',
  `parent_id` int(11) DEFAULT '0' COMMENT '父类Id',
  `tenant_id` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='部门管理';

/*Data for the table `sys_dept` */

insert  into `sys_dept`(`dept_id`,`org_id`,`user_id`,`name`,`sort`,`create_time`,`update_time`,`del_flag`,`parent_id`,`tenant_id`,`version`) values 
(1,NULL,NULL,'山东农信',NULL,'2018-01-22 19:00:23','2018-09-13 01:46:29','0',0,1,NULL),
(2,NULL,NULL,'沙县国际',NULL,'2018-01-22 19:00:38','2018-09-13 01:46:30','0',0,1,NULL),
(3,NULL,NULL,'潍坊农信',NULL,'2018-01-22 19:00:44','2018-09-13 01:46:31','0',1,1,NULL),
(4,NULL,NULL,'高新农信',NULL,'2018-01-22 19:00:52','2018-10-06 10:41:52','0',3,1,NULL),
(5,NULL,NULL,'院校农信',NULL,'2018-01-22 19:00:57','2018-10-06 10:42:51','0',4,1,NULL),
(6,NULL,NULL,'潍院农信',NULL,'2018-01-22 19:01:06','2019-01-09 10:58:18','1',5,1,NULL),
(7,NULL,NULL,'山东沙县',NULL,'2018-01-22 19:01:57','2018-09-13 01:46:42','0',2,1,NULL),
(8,NULL,NULL,'潍坊沙县',NULL,'2018-01-22 19:02:03','2018-09-13 01:46:43','0',7,1,NULL),
(9,NULL,NULL,'高新沙县',NULL,'2018-01-22 19:02:14','2018-09-13 01:46:44','1',8,1,NULL),
(10,NULL,NULL,'租户2',NULL,'2018-11-18 13:27:11','2018-11-18 13:42:19','0',0,2,NULL),
(11,NULL,NULL,'院校沙县',NULL,'2018-12-10 21:19:26','2019-04-24 15:55:09','1',8,1,NULL),
(12,NULL,NULL,'nihao11',12312,'2019-04-25 14:50:11','2019-05-28 23:16:54','0',12312,12312,NULL),
(13,NULL,NULL,'liujaiyi',1,'2019-04-25 14:51:51',NULL,'0',12,12312,NULL),
(14,NULL,NULL,'liujaiyi',1,'2019-05-28 23:16:47',NULL,'0',12,12312,NULL);

/*Table structure for table `sys_dept_house` */

DROP TABLE IF EXISTS `sys_dept_house`;

CREATE TABLE `sys_dept_house` (
  `dept_id` int(11) NOT NULL COMMENT '部门Id',
  `house_id` int(11) NOT NULL COMMENT '小区Id',
  PRIMARY KEY (`dept_id`,`house_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `sys_dept_house` */

/*Table structure for table `sys_dept_relation` */

DROP TABLE IF EXISTS `sys_dept_relation`;

CREATE TABLE `sys_dept_relation` (
  `ancestor` int(11) NOT NULL COMMENT '祖先节点',
  `descendant` int(11) NOT NULL COMMENT '后代节点',
  PRIMARY KEY (`ancestor`,`descendant`) USING BTREE,
  KEY `idx1` (`ancestor`) USING BTREE,
  KEY `idx2` (`descendant`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC COMMENT='部门关系表';

/*Data for the table `sys_dept_relation` */

insert  into `sys_dept_relation`(`ancestor`,`descendant`) values 
(1,1),
(1,3),
(1,4),
(1,5),
(2,2),
(2,7),
(2,8),
(3,3),
(3,4),
(3,5),
(4,4),
(4,5),
(5,5),
(7,7),
(7,8),
(8,8),
(10,10),
(12,12),
(12,13),
(12,14),
(13,13),
(14,14);

/*Table structure for table `sys_menu` */

DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  `name` varchar(32) NOT NULL COMMENT '菜单名称',
  `description` varchar(128) DEFAULT NULL COMMENT '描述',
  `permission` varchar(32) DEFAULT NULL COMMENT '菜单权限标识',
  `url` varchar(60) DEFAULT '' COMMENT '请求url',
  `path` varchar(128) DEFAULT NULL COMMENT '前端URL',
  `parent_id` int(11) DEFAULT NULL COMMENT '父菜单ID',
  `icon` varchar(32) DEFAULT NULL COMMENT '图标',
  `component` varchar(64) DEFAULT NULL COMMENT 'VUE页面',
  `sort` int(11) DEFAULT '1' COMMENT '排序值',
  `keep_alive` char(1) DEFAULT '0' COMMENT '0-开启，1- 关闭',
  `type` char(1) DEFAULT NULL COMMENT '菜单类型 （0菜单 1按钮）',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单权限表';

/*Data for the table `sys_menu` */

insert  into `sys_menu`(`menu_id`,`name`,`description`,`permission`,`url`,`path`,`parent_id`,`icon`,`component`,`sort`,`keep_alive`,`type`,`create_time`,`update_time`,`del_flag`) values 
(100,'运营管理','',NULL,'','',-1,'x-fa fa-university',NULL,1,'0','0','2019-06-01 15:58:13',NULL,'0'),
(200,'系统管理','',NULL,'','',-1,'x-fa fa-cog',NULL,2,'0','0','2019-06-01 16:01:54','2019-06-01 17:07:04','0'),
(10001,'小区管理','',NULL,'','housePlotGrid',100,'x-fa fa-building',NULL,1,'0','0','2019-06-01 15:58:45','2019-06-01 15:59:29','0'),
(10002,'房屋信息管理','',NULL,'','houseUnitPanel.houseBuildingGrid',100,'x-fa fa-h-square',NULL,1,'0','0','2019-06-01 15:59:23',NULL,'0'),
(10003,'住户管理','',NULL,'','houseUnitPanel.houseUserGrid',100,'x-fa fa-square',NULL,1,'0','0','2019-06-01 15:59:54',NULL,'0'),
(10004,'缴费管理','',NULL,'','houseUnitPanel.feePayGrid',100,'x-fa fa-gavel',NULL,1,'0','0','2019-06-01 16:00:16',NULL,'0'),
(10005,'意见反馈管理','',NULL,'','housePlotPanel.feedbackOpinionGrid',100,'x-fa fa-at',NULL,1,'0','0','2019-06-01 16:00:37',NULL,'0'),
(10006,'故障报修管理','',NULL,'','houseUnitPanel.feedbackFaultGrid',100,'x-fa fa-book',NULL,1,'0','0','2019-06-01 16:00:59',NULL,'0'),
(10007,'公告管理','',NULL,'','housePlotPanel.messageNoticeGrid',100,'x-fa fa-comment',NULL,1,'0','0','2019-06-01 16:01:21',NULL,'0'),
(20001,'字典管理','',NULL,'','systemDictionaryPanel',200,'x-fa fa-list',NULL,1,'0','0','2019-06-01 16:02:16',NULL,'0'),
(20002,'菜单管理','',NULL,'','systemMenuPanel.systemMenuEdit',200,'x-fa fa-list',NULL,1,'0','0','2019-06-01 16:02:45',NULL,'0'),
(20003,'权限管理','',NULL,'','systemMenuPanel.systemPermissionsGrid',200,'x-fa fa-list',NULL,1,'0','0','2019-06-01 16:03:05',NULL,'0'),
(20004,'角色管理','',NULL,'','systemRoleGrid',200,'x-fa fa-reddit',NULL,1,'0','0','2019-06-01 16:03:23',NULL,'0'),
(20005,'组织管理','',NULL,'','purviewPanel.tissueEdit',200,'x-fa fa-building',NULL,1,'0','0','2019-06-01 16:03:44',NULL,'0'),
(20006,'账号管理','',NULL,'','purviewPanel.purviewAccountGrid',200,'x-fa fa-user',NULL,1,'0','0','2019-06-01 16:04:09',NULL,'0'),
(100011,'新增',NULL,'add','',NULL,10001,NULL,NULL,1,'0','1','2019-06-01 16:07:15',NULL,'0'),
(100012,'编辑',NULL,'edit','',NULL,10001,NULL,NULL,1,'0','1','2019-06-01 16:07:36',NULL,'0'),
(100013,'删除',NULL,'delete','',NULL,10001,NULL,NULL,1,'0','1','2019-06-01 16:07:55',NULL,'0'),
(100014,'启用/禁用',NULL,'state','',NULL,10001,NULL,NULL,1,'0','1','2019-06-01 16:08:35',NULL,'0'),
(100015,'详情',NULL,'info','',NULL,10001,NULL,NULL,1,'0','1','2019-06-01 16:08:48',NULL,'0'),
(100021,'新增',NULL,'add','',NULL,10002,NULL,NULL,1,'0','1','2019-06-01 16:09:03',NULL,'0'),
(100022,'编辑',NULL,'edit','',NULL,10002,NULL,NULL,1,'0','1','2019-06-01 16:09:15',NULL,'0'),
(100023,'删除',NULL,'delete','',NULL,10002,NULL,NULL,1,'0','1','2019-06-01 16:09:25',NULL,'0'),
(100031,'新增',NULL,'add','',NULL,10003,NULL,NULL,1,'0','1','2019-06-01 16:09:37',NULL,'0'),
(100032,'编辑',NULL,'edit','',NULL,10003,NULL,NULL,1,'0','1','2019-06-01 16:09:50',NULL,'0'),
(100033,'删除',NULL,'delete','',NULL,10003,NULL,NULL,1,'0','1','2019-06-01 16:10:04',NULL,'0'),
(100041,'查看详情',NULL,'info','',NULL,10004,NULL,NULL,1,'0','1','2019-06-01 16:11:47',NULL,'0'),
(100042,'缴费设置',NULL,'site','',NULL,10004,NULL,NULL,1,'0','1','2019-06-01 16:12:06',NULL,'0'),
(100051,'导出',NULL,'export','',NULL,10005,NULL,NULL,1,'0','1','2019-06-01 16:12:20',NULL,'0'),
(100061,'导出',NULL,'export','',NULL,10006,NULL,NULL,1,'0','1','2019-06-01 16:12:31',NULL,'0'),
(100062,'处理',NULL,'dispose','',NULL,10006,NULL,NULL,1,'0','1','2019-06-01 16:13:13',NULL,'0'),
(100071,'新增',NULL,'add','',NULL,10007,NULL,NULL,1,'0','1','2019-06-01 16:13:27',NULL,'0'),
(100072,'编辑',NULL,'edit','',NULL,10007,NULL,NULL,1,'0','1','2019-06-01 16:13:39',NULL,'0'),
(100073,'删除',NULL,'delete','',NULL,10007,NULL,NULL,1,'0','1','2019-06-01 16:13:51',NULL,'0'),
(200011,'新增',NULL,'add','',NULL,20001,NULL,NULL,1,'0','1','2019-06-01 16:14:15',NULL,'0'),
(200012,'编辑',NULL,'edit','',NULL,20001,NULL,NULL,1,'0','1','2019-06-01 16:14:25',NULL,'0'),
(200013,'删除',NULL,'delete','',NULL,20001,NULL,NULL,1,'0','1','2019-06-01 16:14:39',NULL,'0'),
(200021,'新增',NULL,'add','',NULL,20002,NULL,NULL,1,'0','1','2019-06-01 16:14:59',NULL,'0'),
(200022,'编辑',NULL,'edit','',NULL,20002,NULL,NULL,1,'0','1','2019-06-01 16:15:09',NULL,'0'),
(200023,'删除',NULL,'delete','',NULL,20002,NULL,NULL,1,'0','1','2019-06-01 16:15:20',NULL,'0'),
(200031,'删除',NULL,'delete','',NULL,20003,NULL,NULL,1,'0','1','2019-06-01 16:15:36',NULL,'0'),
(200032,'编辑',NULL,'edit','',NULL,20003,NULL,NULL,1,'0','1','2019-06-01 16:15:47',NULL,'0'),
(200033,'新增',NULL,'add','',NULL,20003,NULL,NULL,1,'0','1','2019-06-01 16:16:04',NULL,'0'),
(200041,'新增',NULL,'add','',NULL,20004,NULL,NULL,1,'0','1','2019-06-01 16:16:15',NULL,'0'),
(200042,'编辑',NULL,'edit','',NULL,20004,NULL,NULL,1,'0','1','2019-06-01 16:16:32',NULL,'0'),
(200043,'删除',NULL,'delete','',NULL,20004,NULL,NULL,1,'0','1','2019-06-01 16:16:43',NULL,'0'),
(200044,'权限设置',NULL,'permissions','',NULL,20004,NULL,NULL,1,'0','1','2019-06-01 16:16:58',NULL,'0'),
(200051,'新增',NULL,'add','',NULL,20005,NULL,NULL,1,'0','1','2019-06-01 16:17:09',NULL,'0'),
(200052,'编辑',NULL,'edit','',NULL,20005,NULL,NULL,1,'0','1','2019-06-01 16:17:23',NULL,'0'),
(200053,'删除',NULL,'delete','',NULL,20005,NULL,NULL,1,'0','1','2019-06-01 16:17:36',NULL,'0'),
(200061,'新增',NULL,'add','',NULL,20006,NULL,NULL,1,'0','1','2019-06-01 16:17:49',NULL,'0'),
(200062,'编辑',NULL,'edit','',NULL,20006,NULL,NULL,1,'0','1','2019-06-01 16:18:02',NULL,'0'),
(200063,'删除',NULL,'delete','',NULL,20006,NULL,NULL,1,'0','1','2019-06-01 16:18:12',NULL,'0');

/*Table structure for table `sys_org` */

DROP TABLE IF EXISTS `sys_org`;

CREATE TABLE `sys_org` (
  `org_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '组织Id',
  `user_id` int(11) DEFAULT NULL COMMENT '创建人Id',
  `org_name` varchar(200) DEFAULT NULL COMMENT '组织名称',
  `org_code` varchar(200) DEFAULT NULL COMMENT '组织编码',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `parent_id` int(11) DEFAULT NULL COMMENT '父类Id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标识（0-正常,1-删除）',
  `tenant_id` int(11) DEFAULT NULL COMMENT '所属租户',
  `version` int(11) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COMMENT='组织';

/*Data for the table `sys_org` */

insert  into `sys_org`(`org_id`,`user_id`,`org_name`,`org_code`,`sort`,`parent_id`,`create_time`,`update_time`,`del_flag`,`tenant_id`,`version`) values 
(1,1,'组织-1','org-1',0,0,'2019-05-31 14:01:46','2019-05-31 14:02:00','0',NULL,0),
(16,1,'组织-1-1','org-1-1',0,1,'2019-05-31 14:08:11',NULL,'0',NULL,0),
(20,1,'组织-1-1','org-1-1',0,1,'2019-05-31 15:31:43',NULL,'0',NULL,0),
(21,1,'组织-1-1','org-1-1',0,1,'2019-05-31 15:31:52',NULL,'0',NULL,0),
(22,1,'组织-1-1','org-1-1',0,1,'2019-05-31 15:31:53','2019-06-01 19:05:28','1',NULL,0),
(23,1,'组织-1-1','org-1-1',0,16,'2019-05-31 15:32:26',NULL,'0',NULL,0),
(24,1,'组织-1-1','org-1-1',0,16,'2019-06-01 12:00:32','2019-06-01 19:05:39','1',NULL,0),
(25,1,'11','11',11,23,'2019-06-01 19:06:16',NULL,'0',NULL,0),
(26,1,'33','33',33,25,'2019-06-01 19:06:44',NULL,'0',NULL,0),
(27,1,'1','11',11,20,'2019-06-01 19:08:43',NULL,'0',NULL,0),
(28,1,'1','1',1,21,'2019-06-01 19:11:27','2019-06-01 19:12:15','1',NULL,0),
(29,1,'1','1',1,21,'2019-06-01 19:12:30','2019-06-01 19:23:50','1',NULL,0),
(30,1,'1','1',1,26,'2019-06-01 19:18:52','2019-06-01 19:20:13','1',NULL,0),
(31,1,'1','1',11,25,'2019-06-01 19:20:02',NULL,'0',NULL,0),
(32,1,'1','1',1,26,'2019-06-01 19:20:21',NULL,'0',NULL,0),
(33,1,'1','1',1,25,'2019-06-01 19:21:28',NULL,'0',NULL,0),
(34,1,'1','1',1,20,'2019-06-01 19:22:07',NULL,'0',NULL,0),
(35,1,'1','1',1,21,'2019-06-01 19:23:41','2019-06-01 19:23:53','1',NULL,0);

/*Table structure for table `sys_org_house` */

DROP TABLE IF EXISTS `sys_org_house`;

CREATE TABLE `sys_org_house` (
  `org_id` int(11) NOT NULL COMMENT '组织Id',
  `house_id` varchar(36) NOT NULL COMMENT '小区Id',
  PRIMARY KEY (`org_id`,`house_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `sys_org_house` */

insert  into `sys_org_house`(`org_id`,`house_id`) values 
(1,'0c3c99de645803de79a8eb2d3a65ac33'),
(1,'0ccfb8bd75661be58be3f9594979cc34'),
(1,'2200d85ce941524b6f90a8976ab8eba2'),
(1,'3a1a75b3aa8f186b8bc2e39e99e8278c'),
(1,'4aab518106074dc34a394f4b42bae293'),
(1,'573411a6484042739f99b476922de723'),
(1,'8b4a0aff-332c-11e9-aaaf-0242ac110004'),
(1,'b76aefe3d25841f6bf07055dcbb0b273'),
(1,'c5929858abafb16191a07c79ee9269a6'),
(1,'e264226ab64eae8335e4ee0bf865bc84'),
(1,'eb17c87b4538eb553a628ce36ec7d6ae'),
(16,'312'),
(16,'eb17c87b4538eb553a628ce36ec7d6ae'),
(20,'123'),
(20,'312'),
(21,'123'),
(21,'312'),
(23,'123'),
(23,'312'),
(25,'0c3c99de645803de79a8eb2d3a65ac33'),
(25,'0ccfb8bd75661be58be3f9594979cc34'),
(25,'2200d85ce941524b6f90a8976ab8eba2'),
(25,'3a1a75b3aa8f186b8bc2e39e99e8278c'),
(25,'4aab518106074dc34a394f4b42bae293'),
(25,'573411a6484042739f99b476922de723'),
(25,'8b4a0aff-332c-11e9-aaaf-0242ac110004'),
(25,'b76aefe3d25841f6bf07055dcbb0b273'),
(25,'c5929858abafb16191a07c79ee9269a6'),
(25,'e264226ab64eae8335e4ee0bf865bc84'),
(25,'eb17c87b4538eb553a628ce36ec7d6ae'),
(26,'0c3c99de645803de79a8eb2d3a65ac33'),
(26,'0ccfb8bd75661be58be3f9594979cc34'),
(26,'2200d85ce941524b6f90a8976ab8eba2'),
(26,'3a1a75b3aa8f186b8bc2e39e99e8278c'),
(26,'4aab518106074dc34a394f4b42bae293'),
(26,'573411a6484042739f99b476922de723'),
(26,'8b4a0aff-332c-11e9-aaaf-0242ac110004'),
(26,'b76aefe3d25841f6bf07055dcbb0b273'),
(26,'c5929858abafb16191a07c79ee9269a6'),
(26,'e264226ab64eae8335e4ee0bf865bc84'),
(26,'eb17c87b4538eb553a628ce36ec7d6ae'),
(27,'0c3c99de645803de79a8eb2d3a65ac33'),
(27,'0ccfb8bd75661be58be3f9594979cc34'),
(27,'2200d85ce941524b6f90a8976ab8eba2'),
(27,'3a1a75b3aa8f186b8bc2e39e99e8278c'),
(27,'4aab518106074dc34a394f4b42bae293'),
(27,'573411a6484042739f99b476922de723'),
(27,'8b4a0aff-332c-11e9-aaaf-0242ac110004'),
(27,'b76aefe3d25841f6bf07055dcbb0b273'),
(27,'c5929858abafb16191a07c79ee9269a6'),
(27,'e264226ab64eae8335e4ee0bf865bc84'),
(27,'eb17c87b4538eb553a628ce36ec7d6ae'),
(31,'0c3c99de645803de79a8eb2d3a65ac33'),
(31,'0ccfb8bd75661be58be3f9594979cc34'),
(31,'2200d85ce941524b6f90a8976ab8eba2'),
(31,'3a1a75b3aa8f186b8bc2e39e99e8278c'),
(31,'4aab518106074dc34a394f4b42bae293'),
(31,'573411a6484042739f99b476922de723'),
(31,'8b4a0aff-332c-11e9-aaaf-0242ac110004'),
(31,'b76aefe3d25841f6bf07055dcbb0b273'),
(31,'c5929858abafb16191a07c79ee9269a6'),
(31,'e264226ab64eae8335e4ee0bf865bc84'),
(31,'eb17c87b4538eb553a628ce36ec7d6ae'),
(32,'0c3c99de645803de79a8eb2d3a65ac33'),
(32,'0ccfb8bd75661be58be3f9594979cc34'),
(32,'2200d85ce941524b6f90a8976ab8eba2'),
(32,'3a1a75b3aa8f186b8bc2e39e99e8278c'),
(32,'4aab518106074dc34a394f4b42bae293'),
(32,'573411a6484042739f99b476922de723'),
(32,'8b4a0aff-332c-11e9-aaaf-0242ac110004'),
(32,'b76aefe3d25841f6bf07055dcbb0b273'),
(32,'c5929858abafb16191a07c79ee9269a6'),
(32,'e264226ab64eae8335e4ee0bf865bc84'),
(32,'eb17c87b4538eb553a628ce36ec7d6ae'),
(33,'0c3c99de645803de79a8eb2d3a65ac33'),
(33,'0ccfb8bd75661be58be3f9594979cc34'),
(33,'2200d85ce941524b6f90a8976ab8eba2'),
(33,'3a1a75b3aa8f186b8bc2e39e99e8278c'),
(33,'4aab518106074dc34a394f4b42bae293'),
(33,'573411a6484042739f99b476922de723'),
(33,'8b4a0aff-332c-11e9-aaaf-0242ac110004'),
(33,'b76aefe3d25841f6bf07055dcbb0b273'),
(33,'c5929858abafb16191a07c79ee9269a6'),
(33,'e264226ab64eae8335e4ee0bf865bc84'),
(33,'eb17c87b4538eb553a628ce36ec7d6ae'),
(34,'0c3c99de645803de79a8eb2d3a65ac33'),
(34,'0ccfb8bd75661be58be3f9594979cc34'),
(34,'2200d85ce941524b6f90a8976ab8eba2'),
(34,'3a1a75b3aa8f186b8bc2e39e99e8278c'),
(34,'4aab518106074dc34a394f4b42bae293'),
(34,'573411a6484042739f99b476922de723'),
(34,'8b4a0aff-332c-11e9-aaaf-0242ac110004'),
(34,'b76aefe3d25841f6bf07055dcbb0b273'),
(34,'c5929858abafb16191a07c79ee9269a6'),
(34,'e264226ab64eae8335e4ee0bf865bc84'),
(34,'eb17c87b4538eb553a628ce36ec7d6ae');

/*Table structure for table `sys_org_relation` */

DROP TABLE IF EXISTS `sys_org_relation`;

CREATE TABLE `sys_org_relation` (
  `ancestor` int(11) NOT NULL COMMENT '祖先节点',
  `descendant` int(11) NOT NULL COMMENT '后代节点',
  PRIMARY KEY (`ancestor`,`descendant`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `sys_org_relation` */

insert  into `sys_org_relation`(`ancestor`,`descendant`) values 
(1,1),
(1,16),
(1,20),
(1,21),
(1,23),
(1,25),
(1,26),
(1,27),
(1,31),
(1,32),
(1,33),
(1,34),
(16,16),
(16,23),
(16,25),
(16,26),
(16,31),
(16,32),
(16,33),
(20,20),
(20,27),
(20,34),
(21,21),
(23,23),
(23,25),
(23,26),
(23,31),
(23,32),
(23,33),
(25,25),
(25,26),
(25,31),
(25,32),
(25,33),
(26,26),
(26,32),
(27,27),
(31,31),
(32,32),
(33,33),
(34,34);

/*Table structure for table `sys_org_role` */

DROP TABLE IF EXISTS `sys_org_role`;

CREATE TABLE `sys_org_role` (
  `org_id` int(11) NOT NULL COMMENT '组织ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`org_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `sys_org_role` */

insert  into `sys_org_role`(`org_id`,`role_id`) values 
(1,1),
(11,2),
(11,3),
(12,1),
(12,2),
(13,1),
(13,2),
(14,1),
(14,2),
(15,1),
(15,2),
(16,1),
(16,2),
(20,1),
(20,2),
(21,1),
(21,2),
(25,1),
(25,2),
(25,4),
(26,1),
(26,2),
(26,4),
(27,1),
(31,1),
(32,1),
(33,1),
(34,1);

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色Id',
  `role_name` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '角色名称',
  `role_code` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '角色编码',
  `role_desc` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '角色描述',
  `ds_type` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '2' COMMENT '数据权限类型',
  `ds_scope` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '数据权限范围(自定义的)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) COLLATE utf8mb4_bin DEFAULT '0' COMMENT '删除标识（0-正常,1-删除）',
  `tenant_id` int(11) DEFAULT NULL COMMENT '所属租户',
  `version` int(11) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE KEY `role_idx1_role_code` (`role_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='系统角色表';

/*Data for the table `sys_role` */

insert  into `sys_role`(`role_id`,`role_name`,`role_code`,`role_desc`,`ds_type`,`ds_scope`,`create_time`,`update_time`,`del_flag`,`tenant_id`,`version`) values 
(1,'管理员','ROLE_ADMIN','管理员','3','4','2017-10-29 15:45:51','2019-04-25 09:16:00','0',1,0),
(2,'ROLE_CQQ','ROLE_CQQ','ROLE_CQQ','0',NULL,'2018-11-11 19:42:26','2018-12-26 14:09:07','0',2,0),
(4,'刘家111','nihaao','123112','2',NULL,'2019-04-22 17:51:46','2019-04-22 17:52:17','0',NULL,0);

/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色菜单表';

/*Data for the table `sys_role_menu` */

insert  into `sys_role_menu`(`role_id`,`menu_id`) values 
(1,100),
(1,200),
(1,10001),
(1,10002),
(1,10003),
(1,10004),
(1,10005),
(1,10006),
(1,10007),
(1,20001),
(1,20002),
(1,20003),
(1,20004),
(1,20005),
(1,20006),
(1,100011),
(1,100012),
(1,100013),
(1,100014),
(1,100015),
(1,100021),
(1,100022),
(1,100023),
(1,100031),
(1,100032),
(1,100033),
(1,100041),
(1,100042),
(1,100051),
(1,100061),
(1,100062),
(1,100071),
(1,100072),
(1,100073),
(1,200011),
(1,200012),
(1,200013),
(1,200021),
(1,200022),
(1,200023),
(1,200031),
(1,200032),
(1,200033),
(1,200041),
(1,200042),
(1,200043),
(1,200044),
(1,200051),
(1,200052),
(1,200053),
(1,200061),
(1,200062),
(1,200063),
(2,100),
(2,200),
(2,10001),
(2,10002),
(2,10003),
(2,10004),
(2,10005),
(2,10006),
(2,10007),
(2,20001),
(2,20002),
(2,20003),
(2,20004),
(2,20005),
(2,20006),
(2,100011),
(2,100012),
(2,100013),
(2,100014),
(2,100015),
(2,100021),
(2,100022),
(2,100023),
(2,100031),
(2,100032),
(2,100033),
(2,100041),
(2,100042),
(2,100051),
(2,100061),
(2,100062),
(2,100071),
(2,100072),
(2,100073),
(2,200011),
(2,200012),
(2,200013),
(2,200021),
(2,200022),
(2,200023),
(2,200031),
(2,200032),
(2,200033),
(2,200041),
(2,200042),
(2,200043),
(2,200044),
(2,200051),
(2,200052),
(2,200053),
(2,200061),
(2,200062),
(2,200063),
(4,100),
(4,200),
(4,10001),
(4,10002),
(4,10003),
(4,10004),
(4,10005),
(4,10006),
(4,10007),
(4,20001),
(4,20002),
(4,20003),
(4,20004),
(4,20005),
(4,20006),
(4,100011),
(4,100012),
(4,100013),
(4,100014),
(4,100015),
(4,100021),
(4,100022),
(4,100023),
(4,100031),
(4,100032),
(4,100033),
(4,100041),
(4,100042),
(4,100051),
(4,100061),
(4,100062),
(4,100071),
(4,100072),
(4,100073),
(4,200011),
(4,200012),
(4,200013),
(4,200021),
(4,200022),
(4,200023),
(4,200031),
(4,200032),
(4,200033),
(4,200041),
(4,200042),
(4,200043),
(4,200044),
(4,200051),
(4,200052),
(4,200053),
(4,200061),
(4,200062),
(4,200063);

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `create_user_id` int(11) DEFAULT NULL COMMENT '创建用户Id',
  `realname` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '真实名字',
  `username` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
  `password` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `salt` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '随机盐',
  `phone` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '电话',
  `identity_card` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '身份证',
  `mailbox` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像',
  `org_id` int(11) DEFAULT NULL COMMENT '组织ID',
  `dept_id` int(11) DEFAULT NULL COMMENT '部门ID',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `lock_flag` char(1) COLLATE utf8mb4_bin DEFAULT '0' COMMENT '0-正常，9-锁定',
  `del_flag` char(1) COLLATE utf8mb4_bin DEFAULT '0' COMMENT '0-正常，1-删除',
  `wx_openid` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '微信openid',
  `qq_openid` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'QQ openid',
  `tenant_id` int(11) NOT NULL DEFAULT '0' COMMENT '所属租户',
  `version` int(11) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`user_id`),
  KEY `user_wx_openid` (`wx_openid`) USING BTREE,
  KEY `user_qq_openid` (`qq_openid`) USING BTREE,
  KEY `user_idx1_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用户表';

/*Data for the table `sys_user` */

insert  into `sys_user`(`user_id`,`create_user_id`,`realname`,`username`,`password`,`salt`,`phone`,`identity_card`,`mailbox`,`avatar`,`org_id`,`dept_id`,`create_time`,`update_time`,`lock_flag`,`del_flag`,`wx_openid`,`qq_openid`,`tenant_id`,`version`) values 
(1,NULL,'yuanfang','admin','$2a$10$a6qU.9rS2k.BeAwA8cx6wu0PXU/2wapTiLa2hUFTtR5jFOApHehP.','string','17608422904',NULL,NULL,NULL,NULL,NULL,'2019-06-01 15:55:41','2019-06-01 15:56:33','0','0',NULL,NULL,0,0),
(2,1,'12','admin1','$2a$10$RpFJjxYiXdEsAGnWp/8fsOetMuOON96Ntk/Ym2M/RKRyU0GZseaDC',NULL,'17034642887',NULL,NULL,NULL,NULL,3,'2018-04-20 07:15:18','2019-05-29 22:13:06','0','0','o_0FT0uyg_H1vVy2H0JpSwlVGhWQ',NULL,2,0),
(5,1,'12','liujiayi','$2a$10$7b0nSDIm35Qk1fB01fSU1efy3WsHjxfbvOCMzzmHpOktj5QTHNrde','666668','155232984121',NULL,NULL,'1',NULL,1,'2019-04-24 16:19:09','2019-05-29 22:13:13','0','0',NULL,NULL,1,1),
(6,NULL,'yuan','yuan','$2a$10$gbFpllVzz068MB/St2ZSO.B8q1MpiwCuKYIxI1YQN3n4W3tKa1wYS',NULL,'1234567890',NULL,NULL,NULL,NULL,NULL,'2019-05-30 11:06:28',NULL,'0','0',NULL,NULL,0,0),
(7,NULL,'string','string','$2a$10$FoRTd7RZVws4xsvXeeOof.7qZaeKvuS8UQMeoGz/ZsZPxBQK6s7qe','string','12312312',NULL,NULL,NULL,0,NULL,'2019-05-30 18:49:46','2019-05-30 18:51:33','0','0',NULL,NULL,0,0),
(8,NULL,'string','string','$2a$10$fo8zr.9uDe5Q8HErJ5LYjestdlxtqrCKdKxUk/.oEwZtwCUhFzA56','string','string',NULL,NULL,NULL,0,NULL,'2019-05-30 18:52:00',NULL,'0','0',NULL,NULL,0,0),
(9,NULL,'string','string','$2a$10$xCOIq4A1j/VVV07BBv8UPegIQdrjfqg7CIxYzTFCv1fRFqKg3v4ry','string','string',NULL,NULL,NULL,0,NULL,'2019-05-30 18:52:45',NULL,'0','0',NULL,NULL,0,0),
(13,NULL,'csdds','admin2323','$2a$10$Q/F6o2IrHueo2fRfCmNUw.Q1jZAjJEFtNEbN0zuGUP/VC3lm25qhS',NULL,'18666665555',NULL,NULL,'/images/20190601144501/4.jpg',NULL,NULL,'2019-06-01 14:45:22',NULL,'0','0',NULL,NULL,0,0);

/*Table structure for table `sys_user_dept` */

DROP TABLE IF EXISTS `sys_user_dept`;

CREATE TABLE `sys_user_dept` (
  `user_id` int(11) NOT NULL COMMENT '用户Id',
  `dept_id` int(11) NOT NULL COMMENT '部门Id',
  PRIMARY KEY (`user_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `sys_user_dept` */

/*Table structure for table `sys_user_org` */

DROP TABLE IF EXISTS `sys_user_org`;

CREATE TABLE `sys_user_org` (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `org_id` int(11) NOT NULL COMMENT '组织ID',
  PRIMARY KEY (`user_id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `sys_user_org` */

insert  into `sys_user_org`(`user_id`,`org_id`) values 
(1,1),
(2,16),
(5,20),
(6,21),
(7,22),
(13,-1),
(14,0),
(15,0),
(16,0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
