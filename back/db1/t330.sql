/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80300
 Source Host           : localhost:3306
 Source Schema         : t330

 Target Server Type    : MySQL
 Target Server Version : 80300
 File Encoding         : 65001

 Date: 04/04/2026 13:48:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for baoxui
-- ----------------------------
DROP TABLE IF EXISTS `baoxui`;
CREATE TABLE `baoxui`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 ',
  `yonghu_id` int NULL DEFAULT NULL COMMENT '用户',
  `chongdianzhuang_id` int NULL DEFAULT NULL COMMENT '充电桩',
  `baoxui_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '报修名称 Search111',
  `baoxui_photo` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '报修图片',
  `baoxui_types` int NOT NULL COMMENT '报修类型 Search111',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '申请反馈时间',
  `baoxui_zhuangtai_types` int NOT NULL COMMENT '报修状态 Search111',
  `baoxui_content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '报修详情',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间 show3 listShow',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '反馈' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of baoxui
-- ----------------------------
INSERT INTO `baoxui` VALUES (1, 2, 1, '报修名称1', 'upload/baoxui1.jpg', 1, '2023-03-11 10:04:07', 2, '报修详情1', '2023-03-11 10:04:07');
INSERT INTO `baoxui` VALUES (2, 3, 2, '报修名称2', 'upload/baoxui2.jpg', 4, '2023-03-11 10:04:07', 1, '报修详情2', '2023-03-11 10:04:07');
INSERT INTO `baoxui` VALUES (3, 3, 3, '报修名称3', 'upload/baoxui3.jpg', 1, '2023-03-11 10:04:07', 2, '报修详情3', '2023-03-11 10:04:07');
INSERT INTO `baoxui` VALUES (4, 2, 4, '报修名称4', 'upload/baoxui4.jpg', 3, '2023-03-11 10:04:07', 2, '报修详情4', '2023-03-11 10:04:07');
INSERT INTO `baoxui` VALUES (5, 2, 5, '报修名称5', 'upload/baoxui5.jpg', 3, '2023-03-11 10:04:07', 2, '报修详情5', '2023-03-11 10:04:07');
INSERT INTO `baoxui` VALUES (6, 2, 6, '报修名称6', 'upload/baoxui6.jpg', 4, '2023-03-11 10:04:07', 1, '报修详情6', '2023-03-11 10:04:07');
INSERT INTO `baoxui` VALUES (7, 2, 7, '报修名称7', 'upload/baoxui7.jpg', 2, '2023-03-11 10:04:07', 1, '报修详情7', '2023-03-11 10:04:07');
INSERT INTO `baoxui` VALUES (8, 1, 8, '报修名称8', 'upload/baoxui8.jpg', 1, '2023-03-11 10:04:07', 1, '报修详情8', '2023-03-11 10:04:07');
INSERT INTO `baoxui` VALUES (9, 3, 9, '报修名称9', 'upload/baoxui9.jpg', 3, '2023-03-11 10:04:07', 1, '报修详情9', '2023-03-11 10:04:07');
INSERT INTO `baoxui` VALUES (10, 3, 10, '报修名称10', 'upload/baoxui10.jpg', 4, '2023-03-11 10:04:07', 1, '报修详情10', '2023-03-11 10:04:07');
INSERT INTO `baoxui` VALUES (11, 3, 11, '报修名称11', 'upload/baoxui11.jpg', 4, '2023-03-11 10:04:07', 2, '报修详情11', '2023-03-11 10:04:07');
INSERT INTO `baoxui` VALUES (12, 1, 12, '报修名称12', 'upload/baoxui12.jpg', 1, '2023-03-11 10:04:07', 2, '报修详情12', '2023-03-11 10:04:07');
INSERT INTO `baoxui` VALUES (13, 2, 13, '报修名称13', 'upload/baoxui13.jpg', 4, '2023-03-11 10:04:07', 2, '报修详情13', '2023-03-11 10:04:07');
INSERT INTO `baoxui` VALUES (14, 1, 14, '报修名称14', 'upload/baoxui14.jpg', 3, '2023-03-11 10:04:07', 1, '报修详情14', '2023-03-11 10:04:07');
INSERT INTO `baoxui` VALUES (25, 1, 1, '22', 'upload/1678504431311.jpg', 1, '2023-03-11 11:13:57', 2, '<p>安排人员维修 </p>', '2023-03-11 11:13:57');

-- ----------------------------
-- Table structure for charging_gun
-- ----------------------------
DROP TABLE IF EXISTS `charging_gun`;
CREATE TABLE `charging_gun`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `station_id` int NOT NULL COMMENT '充电桩ID',
  `gun_no` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '枪头编号',
  `gun_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '枪头名称',
  `gun_type` int NULL DEFAULT 1 COMMENT '接口类型 1:国标交流 2:国标直流 3:欧标 4:特斯拉',
  `power_kw` decimal(10, 2) NULL DEFAULT NULL COMMENT '功率(kW)',
  `status` int NULL DEFAULT 1 COMMENT '状态 1:空闲 2:占用 3:故障 4:离线',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_station_id`(`station_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '充电枪头' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of charging_gun
-- ----------------------------
INSERT INTO `charging_gun` VALUES (1, 1, 'GUN-001-01', '1号快充枪', 2, 120.00, 1, '2026-03-22 02:03:27', '2026-03-22 02:03:27');
INSERT INTO `charging_gun` VALUES (2, 1, 'GUN-001-02', '2号慢充枪', 1, 7.00, 1, '2026-03-22 02:03:27', '2026-03-22 02:03:27');
INSERT INTO `charging_gun` VALUES (3, 2, 'GUN-002-01', '1号快充枪', 2, 60.00, 2, '2026-03-22 02:03:27', '2026-03-22 02:03:27');
INSERT INTO `charging_gun` VALUES (4, 2, 'GUN-002-02', '2号快充枪', 2, 60.00, 1, '2026-03-22 02:03:27', '2026-03-22 02:03:27');
INSERT INTO `charging_gun` VALUES (5, 3, 'GUN-003-01', '1号慢充枪', 1, 7.00, 1, '2026-03-22 02:03:27', '2026-03-22 02:03:27');
INSERT INTO `charging_gun` VALUES (6, 4, 'GUN-004-01', '1号快充枪', 2, 120.00, 3, '2026-03-22 02:03:27', '2026-03-22 02:03:27');
INSERT INTO `charging_gun` VALUES (7, 5, 'GUN-005-01', '1号慢充枪', 1, 7.00, 1, '2026-03-22 02:03:27', '2026-03-22 02:03:27');
INSERT INTO `charging_gun` VALUES (8, 5, 'GUN-005-02', '2号慢充枪', 1, 7.00, 2, '2026-03-22 02:03:27', '2026-03-22 02:03:27');

-- ----------------------------
-- Table structure for chat
-- ----------------------------
DROP TABLE IF EXISTS `chat`;
CREATE TABLE `chat`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `yonghu_id` int NULL DEFAULT NULL COMMENT '提问用户',
  `chat_issue` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '问题',
  `issue_time` timestamp NULL DEFAULT NULL COMMENT '问题时间 Search111',
  `chat_reply` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '回复',
  `reply_time` timestamp NULL DEFAULT NULL COMMENT '回复时间 Search111',
  `zhuangtai_types` int NULL DEFAULT NULL COMMENT '状态',
  `chat_types` int NULL DEFAULT NULL COMMENT '数据类型',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '客服聊天' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat
-- ----------------------------
INSERT INTO `chat` VALUES (1, 1, '呀呀呀', '2023-03-11 11:14:23', NULL, NULL, 2, 1, '2023-03-11 11:14:24');
INSERT INTO `chat` VALUES (2, 1, NULL, NULL, '你哈偶', '2023-03-11 11:16:20', NULL, 2, '2023-03-11 11:16:20');
INSERT INTO `chat` VALUES (3, 1, '116156', '2025-04-29 21:39:36', NULL, NULL, 2, 1, '2025-04-29 21:39:36');
INSERT INTO `chat` VALUES (4, 1, '56+5', '2025-04-29 21:39:40', NULL, NULL, 2, 1, '2025-04-29 21:39:41');
INSERT INTO `chat` VALUES (5, 3, '11', '2026-03-22 18:36:55', NULL, NULL, 1, NULL, '2026-03-22 18:36:55');
INSERT INTO `chat` VALUES (6, 3, '22', '2026-03-22 18:37:37', NULL, NULL, 1, NULL, '2026-03-22 18:37:37');
INSERT INTO `chat` VALUES (7, 3, '33', '2026-03-22 18:37:38', NULL, NULL, 1, NULL, '2026-03-22 18:37:38');
INSERT INTO `chat` VALUES (8, 3, '22', '2026-03-22 18:37:54', NULL, NULL, 1, NULL, '2026-03-22 18:37:54');
INSERT INTO `chat` VALUES (9, 3, '123123', '2026-03-22 18:38:02', NULL, NULL, 1, NULL, '2026-03-22 18:38:02');
INSERT INTO `chat` VALUES (10, 3, '12312', '2026-03-22 18:38:04', NULL, NULL, 1, NULL, '2026-03-22 18:38:04');
INSERT INTO `chat` VALUES (11, 1, NULL, NULL, '11', '2026-03-22 18:38:08', NULL, 2, '2026-03-22 18:38:09');
INSERT INTO `chat` VALUES (12, 1, NULL, NULL, '222', '2026-03-22 18:38:17', NULL, 2, '2026-03-22 18:38:17');
INSERT INTO `chat` VALUES (13, 3, '22', '2026-03-22 18:58:56', NULL, NULL, 1, NULL, '2026-03-22 18:58:56');
INSERT INTO `chat` VALUES (14, 3, '22', '2026-03-22 18:59:01', NULL, NULL, 1, NULL, '2026-03-22 18:59:01');
INSERT INTO `chat` VALUES (15, 3, '11', '2026-03-22 18:59:04', NULL, NULL, 1, NULL, '2026-03-22 18:59:04');
INSERT INTO `chat` VALUES (16, 3, '11', '2026-03-22 18:59:05', NULL, NULL, 1, NULL, '2026-03-22 18:59:05');
INSERT INTO `chat` VALUES (17, 3, '22', '2026-03-22 18:59:26', NULL, NULL, 1, NULL, '2026-03-22 18:59:26');
INSERT INTO `chat` VALUES (18, 3, '33', '2026-03-22 18:59:27', NULL, NULL, 1, NULL, '2026-03-22 18:59:27');
INSERT INTO `chat` VALUES (19, 3, '22', '2026-03-22 18:59:27', NULL, NULL, 1, NULL, '2026-03-22 18:59:27');
INSERT INTO `chat` VALUES (20, 3, '11', '2026-03-22 22:49:32', NULL, NULL, 2, 1, '2026-03-22 22:49:32');
INSERT INTO `chat` VALUES (21, 3, '22', '2026-03-22 22:49:44', NULL, NULL, 1, 1, '2026-03-22 22:49:44');
INSERT INTO `chat` VALUES (22, 3, '22', '2026-03-22 22:49:46', NULL, NULL, 2, 1, '2026-03-22 22:49:46');
INSERT INTO `chat` VALUES (23, 3, NULL, NULL, '22', '2026-03-22 22:49:50', NULL, 2, '2026-03-22 22:49:51');
INSERT INTO `chat` VALUES (24, 3, NULL, NULL, '22', '2026-03-22 22:49:51', NULL, 2, '2026-03-22 22:49:51');
INSERT INTO `chat` VALUES (25, 3, NULL, NULL, '111', '2026-03-22 22:49:58', NULL, 2, '2026-03-22 22:49:59');
INSERT INTO `chat` VALUES (26, 3, NULL, NULL, '222', '2026-03-22 22:50:02', NULL, 2, '2026-03-22 22:50:03');
INSERT INTO `chat` VALUES (27, 3, NULL, NULL, '222', '2026-03-22 22:50:04', NULL, 2, '2026-03-22 22:50:05');
INSERT INTO `chat` VALUES (28, 3, NULL, NULL, 'sfsdf', '2026-03-22 22:50:08', NULL, 2, '2026-03-22 22:50:08');
INSERT INTO `chat` VALUES (29, 3, NULL, NULL, '22', '2026-03-22 22:50:23', NULL, 2, '2026-03-22 22:50:24');

-- ----------------------------
-- Table structure for chongdianzhuang
-- ----------------------------
DROP TABLE IF EXISTS `chongdianzhuang`;
CREATE TABLE `chongdianzhuang`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 ',
  `chongdianzhuang_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '充电桩名称  Search111 ',
  `chongdianzhuang_uuid_number` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '充电桩编号',
  `chongdianzhuang_photo` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '充电桩照片',
  `chongdianzhuang_types` int NULL DEFAULT NULL COMMENT '充电桩类型 Search111',
  `chongdianzhuang_zhuangtai_types` int NULL DEFAULT NULL COMMENT '充电桩状态',
  `chongdianzhuang_kucun_number` int NULL DEFAULT NULL COMMENT '可充时长',
  `chongdianzhuang_new_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '金额/小时 ',
  `chongdianzhuang_content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '充电桩介绍 ',
  `longitude` double NULL DEFAULT NULL COMMENT '经度',
  `latitude` double NULL DEFAULT NULL COMMENT '纬度',
  `address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '地址',
  `brand` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '品牌',
  `is_fast_charge` tinyint(1) NULL DEFAULT 0 COMMENT '是否快充 0否1是',
  `is_free_parking` tinyint(1) NULL DEFAULT 0 COMMENT '是否免费停车 0否1是',
  `shangxia_types` int NULL DEFAULT NULL COMMENT '是否上架 ',
  `chongdianzhuang_delete` int NULL DEFAULT NULL COMMENT '逻辑删除',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '录入时间',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间  show1 show2 photoShow',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '充电桩' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chongdianzhuang
-- ----------------------------
INSERT INTO `chongdianzhuang` VALUES (1, '充电桩名称1', '1678500247811', '/upload/1678504274093.jpg', 2, 2, 101, 20.00, '充电桩介绍1', 121.473701, 31.230416, '上海市黄浦区人民大道201号', NULL, 0, 0, 1, 1, '2023-03-11 02:04:07', '2023-03-11 02:04:07');
INSERT INTO `chongdianzhuang` VALUES (2, '充电桩名称2', '1678500247820', '/upload/1678504253759.jpg!bd800', 2, 1, 102, 20.00, '充电桩介绍2', 121.507181, 31.239218, '上海市浦东新区世纪大道1号', NULL, 0, 0, 1, 1, '2023-03-11 02:04:07', '2023-03-11 02:04:07');
INSERT INTO `chongdianzhuang` VALUES (3, '充电桩名称3', '1678500247785', '/upload/1678504238032.jpg', 4, 1, 103, 20.00, '充电桩介绍3', 121.484787, 31.246721, '上海市虹口区四川北路1941号', NULL, 0, 0, 1, 1, '2023-03-11 02:04:07', '2023-03-11 02:04:07');
INSERT INTO `chongdianzhuang` VALUES (4, '充电桩名称4', '1678500247849', '/upload/1678504221288.jpg', 4, 2, 104, 20.00, '充电桩介绍4', 121.446521, 31.224832, '上海市静安区南京西路1376号', NULL, 0, 0, 1, 1, '2023-03-11 02:04:07', '2023-03-11 02:04:07');
INSERT INTO `chongdianzhuang` VALUES (5, '充电桩名称5', '1678500247769', '/upload/1678504157716.jpg', 1, 2, 105, 20.00, '充电桩介绍5', 121.430512, 31.208356, '上海市长宁区长宁路1018号', NULL, 0, 0, 1, 1, '2023-03-11 02:04:07', '2023-03-11 02:04:07');
INSERT INTO `chongdianzhuang` VALUES (6, '充电桩名称6', '1678500247809', '/upload/1678504274093.jpg', 3, 2, 106, 20.00, '充电桩介绍6', 121.415677, 31.179234, '上海市徐汇区虹桥路1号', NULL, 0, 0, 1, 1, '2023-03-11 02:04:07', '2023-03-11 02:04:07');
INSERT INTO `chongdianzhuang` VALUES (7, '充电桩名称7', '1678500247790', '/upload/1678504253759.jpg!bd800', 4, 2, 107, 20.00, '充电桩介绍7', 121.525683, 31.215976, '上海市浦东新区张杨路501号', NULL, 0, 0, 1, 1, '2023-03-11 02:04:07', '2023-03-11 02:04:07');
INSERT INTO `chongdianzhuang` VALUES (8, '充电桩名称8', '1678500247792', '/upload/1678504238032.jpg', 3, 2, 108, 20.00, '充电桩介绍8', 121.463891, 31.252677, '上海市静安区万航渡路1575号', NULL, 0, 0, 1, 1, '2023-03-11 02:04:07', '2023-03-11 02:04:07');
INSERT INTO `chongdianzhuang` VALUES (9, '充电桩名称9', '1678500247787', '/upload/1678504221288.jpg', 3, 2, 109, 20.00, '充电桩介绍9', 121.537456, 31.227835, '上海市浦东新区金桥路1201号', NULL, 0, 0, 1, 1, '2023-03-11 02:04:07', '2023-03-11 02:04:07');
INSERT INTO `chongdianzhuang` VALUES (10, '充电桩名称10', '1678500247854', '/upload/1678504274093.jpg', 4, 2, 1009, 20.00, '<p>充电桩介绍10</p>', 121.445643, 31.193842, '上海市徐汇区漕宝路66号', NULL, 0, 0, 1, 1, '2023-03-11 02:04:07', '2023-03-11 02:04:07');
INSERT INTO `chongdianzhuang` VALUES (11, '充电桩名称11', '1678500247785', '/upload/1678504253759.jpg!bd800', 3, 1, 1011, 20.00, '<p>充电桩介绍11</p>', 121.504933, 31.256735, '上海市杨浦区黄兴路1628号', NULL, 0, 0, 1, 1, '2023-03-11 02:04:07', '2023-03-11 02:04:07');
INSERT INTO `chongdianzhuang` VALUES (12, '充电桩名称12', '1678500247855', '/upload/1678504238032.jpg', 1, 1, 1012, 20.00, '<p>充电桩介绍12</p>', 121.481927, 31.283652, '上海市宝山区牡丹江路1569号', NULL, 0, 0, 1, 1, '2023-03-11 02:04:07', '2023-03-11 02:04:07');
INSERT INTO `chongdianzhuang` VALUES (13, '充电桩名称13', '1678500247784', '/upload/1678504221288.jpg', 2, 2, 1007, 20.00, '<p>充电桩介绍13</p>', 121.553721, 31.242563, '上海市浦东新区金高路2216号', NULL, 0, 0, 1, 1, '2023-03-11 02:04:07', '2023-03-11 02:04:07');
INSERT INTO `chongdianzhuang` VALUES (14, '充电桩名称14', '1678500247769', '/upload/1678504157716.jpg', 3, 1, 1014, 20.00, '<p>充电桩介绍14</p>', 121.394752, 31.243108, '上海市普陀区真北路1108号', NULL, 0, 0, 1, 1, '2023-03-11 02:04:07', '2023-03-11 02:04:07');

-- ----------------------------
-- Table structure for chongdianzhuang_order
-- ----------------------------
DROP TABLE IF EXISTS `chongdianzhuang_order`;
CREATE TABLE `chongdianzhuang_order`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `chongdianzhuang_id` int NULL DEFAULT NULL COMMENT '充电桩',
  `gun_id` int NULL DEFAULT NULL COMMENT '枪头ID',
  `yonghu_id` int NULL DEFAULT NULL COMMENT '用户',
  `buy_number` int NULL DEFAULT NULL COMMENT '充电小时',
  `chongdianzhuang_order_time` timestamp NULL DEFAULT NULL COMMENT '预约时间',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '开始充电时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '结束充电时间',
  `charge_kwh` decimal(10, 2) NULL DEFAULT NULL COMMENT '充电度数(kWh)',
  `electric_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '电费',
  `service_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '服务费',
  `reserve_expire_time` timestamp NULL DEFAULT NULL COMMENT '预约过期时间',
  `chongdianzhuang_order_true_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '实付价格',
  `chongdianzhuang_order_types` int NULL DEFAULT NULL COMMENT '订单类型 Search111 ',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '订单创建时间',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间 show3 listShow',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '充电桩预约' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chongdianzhuang_order
-- ----------------------------
INSERT INTO `chongdianzhuang_order` VALUES (1, 13, NULL, 1, 3, '2023-03-11 10:12:58', NULL, NULL, NULL, NULL, NULL, NULL, 60.00, 104, '2023-03-11 10:13:00', '2023-03-11 10:13:00');
INSERT INTO `chongdianzhuang_order` VALUES (2, 13, NULL, 1, 3, '2023-03-12 11:13:26', NULL, NULL, NULL, NULL, NULL, NULL, 60.00, 102, '2023-03-11 11:13:33', '2023-03-11 11:13:33');
INSERT INTO `chongdianzhuang_order` VALUES (3, 8, NULL, 1, 1, '2025-04-21 10:33:23', NULL, NULL, NULL, NULL, NULL, NULL, 20.00, 103, '2025-04-21 10:33:25', '2025-04-21 10:33:25');
INSERT INTO `chongdianzhuang_order` VALUES (4, 10, NULL, 1, 1, '2025-04-29 22:05:47', NULL, NULL, NULL, NULL, NULL, NULL, 20.00, 101, '2025-04-29 22:05:49', '2025-04-29 22:05:49');
INSERT INTO `chongdianzhuang_order` VALUES (5, 1, NULL, 3, 0, '2026-03-22 12:02:38', NULL, NULL, NULL, NULL, NULL, '2026-03-22 12:17:38', 0.00, 102, '2026-03-22 12:02:38', '2026-03-22 12:02:38');
INSERT INTO `chongdianzhuang_order` VALUES (6, 1, NULL, 3, 0, '2026-03-22 15:25:57', NULL, NULL, NULL, NULL, NULL, '2026-03-22 15:40:57', 0.00, 102, '2026-03-22 15:25:57', '2026-03-22 15:25:57');
INSERT INTO `chongdianzhuang_order` VALUES (7, 1, NULL, 3, 0, '2026-03-22 15:26:21', NULL, NULL, NULL, NULL, NULL, '2026-03-22 15:41:21', 0.00, 102, '2026-03-22 15:26:21', '2026-03-22 15:26:21');
INSERT INTO `chongdianzhuang_order` VALUES (8, 1, NULL, 3, 0, '2026-03-22 15:29:10', NULL, NULL, NULL, NULL, NULL, '2026-03-22 15:44:10', 0.00, 102, '2026-03-22 15:29:10', '2026-03-22 15:29:10');
INSERT INTO `chongdianzhuang_order` VALUES (9, 1, NULL, 3, 0, '2026-03-22 15:37:08', '2026-03-22 15:37:12', '2026-03-22 18:32:36', 1227.68, 1104.91, 736.61, '2026-03-22 15:52:08', 1841.52, 104, '2026-03-22 15:37:08', '2026-03-22 15:37:08');
INSERT INTO `chongdianzhuang_order` VALUES (10, 3, NULL, 4, 0, '2026-03-22 18:29:03', '2026-03-22 18:29:06', NULL, NULL, NULL, NULL, '2026-03-22 18:44:03', 0.00, 103, '2026-03-22 18:29:03', '2026-03-22 18:29:03');
INSERT INTO `chongdianzhuang_order` VALUES (11, 3, NULL, 4, 0, '2026-03-22 18:29:26', '2026-03-22 18:29:30', '2026-03-22 18:29:40', 1.05, 0.95, 0.63, '2026-03-22 18:44:26', 1.58, 104, '2026-03-22 18:29:26', '2026-03-22 18:29:26');
INSERT INTO `chongdianzhuang_order` VALUES (12, 5, NULL, 6, 0, '2026-04-04 13:45:26', NULL, NULL, NULL, NULL, NULL, '2026-04-04 14:00:26', 0.00, 101, '2026-04-04 13:45:26', '2026-04-04 13:45:26');

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '配置参数名称',
  `value` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '配置参数值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '配置文件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config
-- ----------------------------
INSERT INTO `config` VALUES (1, '轮播图1', 'upload/config1.jpg');
INSERT INTO `config` VALUES (2, '轮播图2', 'upload/config2.jpg');
INSERT INTO `config` VALUES (3, '轮播图3', 'upload/config3.jpg');

-- ----------------------------
-- Table structure for dictionary
-- ----------------------------
DROP TABLE IF EXISTS `dictionary`;
CREATE TABLE `dictionary`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dic_code` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '字段',
  `dic_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '字段名',
  `code_index` int NULL DEFAULT NULL COMMENT '编码',
  `index_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '编码名字  Search111 ',
  `super_id` int NULL DEFAULT NULL COMMENT '父字段id',
  `beizhu` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '字典' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dictionary
-- ----------------------------
INSERT INTO `dictionary` VALUES (1, 'sex_types', '性别类型', 1, '男', NULL, NULL, '2023-03-11 10:03:55');
INSERT INTO `dictionary` VALUES (2, 'sex_types', '性别类型', 2, '女', NULL, NULL, '2023-03-11 10:03:55');
INSERT INTO `dictionary` VALUES (3, 'gonggao_types', '公告类型', 1, '公告类型1', NULL, NULL, '2023-03-11 10:03:55');
INSERT INTO `dictionary` VALUES (4, 'gonggao_types', '公告类型', 2, '公告类型2', NULL, NULL, '2023-03-11 10:03:55');
INSERT INTO `dictionary` VALUES (5, 'chat_types', '数据类型', 1, '问题', NULL, NULL, '2023-03-11 10:03:55');
INSERT INTO `dictionary` VALUES (6, 'chat_types', '数据类型', 2, '回复', NULL, NULL, '2023-03-11 10:03:55');
INSERT INTO `dictionary` VALUES (7, 'zhuangtai_types', '状态', 1, '未回复', NULL, NULL, '2023-03-11 10:03:55');
INSERT INTO `dictionary` VALUES (8, 'zhuangtai_types', '状态', 2, '已回复', NULL, NULL, '2023-03-11 10:03:55');
INSERT INTO `dictionary` VALUES (9, 'shangxia_types', '上下架', 1, '上架', NULL, NULL, '2023-03-11 10:03:55');
INSERT INTO `dictionary` VALUES (10, 'shangxia_types', '上下架', 2, '下架', NULL, NULL, '2023-03-11 10:03:55');
INSERT INTO `dictionary` VALUES (11, 'chongdianzhuang_zhuangtai_types', '充电桩状态', 1, '使用中', NULL, NULL, '2023-03-11 10:03:55');
INSERT INTO `dictionary` VALUES (12, 'chongdianzhuang_zhuangtai_types', '充电桩状态', 2, '未使用', NULL, NULL, '2023-03-11 10:03:56');
INSERT INTO `dictionary` VALUES (13, 'chongdianzhuang_types', '充电桩类型', 1, '充电桩类型1', NULL, NULL, '2023-03-11 10:03:56');
INSERT INTO `dictionary` VALUES (14, 'chongdianzhuang_types', '充电桩类型', 2, '充电桩类型2', NULL, NULL, '2023-03-11 10:03:56');
INSERT INTO `dictionary` VALUES (15, 'chongdianzhuang_types', '充电桩类型', 3, '充电桩类型3', NULL, NULL, '2023-03-11 10:03:56');
INSERT INTO `dictionary` VALUES (16, 'chongdianzhuang_types', '充电桩类型', 4, '充电桩类型4', NULL, NULL, '2023-03-11 10:03:56');
INSERT INTO `dictionary` VALUES (17, 'chongdianzhuang_order_types', '订单类型', 101, '已预约充电', NULL, NULL, '2023-03-11 10:03:56');
INSERT INTO `dictionary` VALUES (18, 'chongdianzhuang_order_types', '订单类型', 102, '已取消预约', NULL, NULL, '2023-03-11 10:03:56');
INSERT INTO `dictionary` VALUES (19, 'chongdianzhuang_order_types', '订单类型', 103, '已在充电中', NULL, NULL, '2023-03-11 10:03:56');
INSERT INTO `dictionary` VALUES (20, 'chongdianzhuang_order_types', '订单类型', 104, '已完成充电', NULL, NULL, '2023-03-11 10:03:56');
INSERT INTO `dictionary` VALUES (21, 'baoxui_types', '充电桩类型', 1, '充电桩类型1', NULL, NULL, '2023-03-11 10:03:56');
INSERT INTO `dictionary` VALUES (22, 'baoxui_types', '充电桩类型', 2, '充电桩类型2', NULL, NULL, '2023-03-11 10:03:56');
INSERT INTO `dictionary` VALUES (23, 'baoxui_types', '充电桩类型', 3, '充电桩类型3', NULL, NULL, '2023-03-11 10:03:56');
INSERT INTO `dictionary` VALUES (24, 'baoxui_types', '充电桩类型', 4, '充电桩类型4', NULL, NULL, '2023-03-11 10:03:56');
INSERT INTO `dictionary` VALUES (25, 'baoxui_zhuangtai_types', '报修状态', 1, '未维修', NULL, NULL, '2023-03-11 10:03:56');
INSERT INTO `dictionary` VALUES (26, 'baoxui_zhuangtai_types', '报修状态', 2, '已维修', NULL, NULL, '2023-03-11 10:03:56');
INSERT INTO `dictionary` VALUES (27, 'gun_type', '枪头接口类型', 1, '国标交流(AC)', NULL, NULL, '2026-03-22 02:03:27');
INSERT INTO `dictionary` VALUES (28, 'gun_type', '枪头接口类型', 2, '国标直流(DC)', NULL, NULL, '2026-03-22 02:03:27');
INSERT INTO `dictionary` VALUES (29, 'gun_type', '枪头接口类型', 3, '欧标', NULL, NULL, '2026-03-22 02:03:27');
INSERT INTO `dictionary` VALUES (30, 'gun_type', '枪头接口类型', 4, '特斯拉', NULL, NULL, '2026-03-22 02:03:27');
INSERT INTO `dictionary` VALUES (31, 'gun_status', '枪头状态', 1, '空闲', NULL, NULL, '2026-03-22 02:03:27');
INSERT INTO `dictionary` VALUES (32, 'gun_status', '枪头状态', 2, '占用', NULL, NULL, '2026-03-22 02:03:27');
INSERT INTO `dictionary` VALUES (33, 'gun_status', '枪头状态', 3, '故障', NULL, NULL, '2026-03-22 02:03:27');
INSERT INTO `dictionary` VALUES (34, 'gun_status', '枪头状态', 4, '离线', NULL, NULL, '2026-03-22 02:03:27');
INSERT INTO `dictionary` VALUES (35, 'price_period_type', '电价时段类型', 1, '尖', NULL, NULL, '2026-03-22 02:03:27');
INSERT INTO `dictionary` VALUES (36, 'price_period_type', '电价时段类型', 2, '峰', NULL, NULL, '2026-03-22 02:03:27');
INSERT INTO `dictionary` VALUES (37, 'price_period_type', '电价时段类型', 3, '平', NULL, NULL, '2026-03-22 02:03:27');
INSERT INTO `dictionary` VALUES (38, 'price_period_type', '电价时段类型', 4, '谷', NULL, NULL, '2026-03-22 02:03:27');
INSERT INTO `dictionary` VALUES (39, 'integral_source_type', '积分来源', 1, '充电奖励', NULL, NULL, '2026-03-22 02:03:27');
INSERT INTO `dictionary` VALUES (40, 'integral_source_type', '积分来源', 2, '反馈审核', NULL, NULL, '2026-03-22 02:03:27');
INSERT INTO `dictionary` VALUES (41, 'integral_source_type', '积分来源', 3, '签到', NULL, NULL, '2026-03-22 02:03:27');
INSERT INTO `dictionary` VALUES (42, 'integral_source_type', '积分来源', 4, '消费抵扣', NULL, NULL, '2026-03-22 02:03:27');

-- ----------------------------
-- Table structure for gonggao
-- ----------------------------
DROP TABLE IF EXISTS `gonggao`;
CREATE TABLE `gonggao`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 ',
  `gonggao_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '公告名称 Search111  ',
  `gonggao_photo` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '公告图片 ',
  `gonggao_types` int NOT NULL COMMENT '公告类型 Search111 ',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '公告发布时间 ',
  `gonggao_content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '公告详情 ',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间 show1 show2 nameShow',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '新能源公告' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gonggao
-- ----------------------------
INSERT INTO `gonggao` VALUES (1, '公告名称1', 'upload/gonggao1.jpg', 2, '2023-03-11 10:04:07', '公告详情1', '2023-03-11 10:04:07');
INSERT INTO `gonggao` VALUES (2, '公告名称2', 'upload/gonggao2.jpg', 1, '2023-03-11 10:04:07', '公告详情2', '2023-03-11 10:04:07');
INSERT INTO `gonggao` VALUES (3, '公告名称3', 'upload/gonggao3.jpg', 2, '2023-03-11 10:04:07', '公告详情3', '2023-03-11 10:04:07');
INSERT INTO `gonggao` VALUES (4, '公告名称4', 'upload/gonggao4.jpg', 1, '2023-03-11 10:04:07', '公告详情4', '2023-03-11 10:04:07');
INSERT INTO `gonggao` VALUES (5, '公告名称5', 'upload/gonggao5.jpg', 2, '2023-03-11 10:04:07', '公告详情5', '2023-03-11 10:04:07');
INSERT INTO `gonggao` VALUES (6, '公告名称6', 'upload/gonggao6.jpg', 1, '2023-03-11 10:04:07', '公告详情6', '2023-03-11 10:04:07');
INSERT INTO `gonggao` VALUES (7, '公告名称7', 'upload/gonggao7.jpg', 2, '2023-03-11 10:04:07', '公告详情7', '2023-03-11 10:04:07');
INSERT INTO `gonggao` VALUES (8, '公告名称8', 'upload/gonggao8.jpg', 1, '2023-03-11 10:04:07', '公告详情8', '2023-03-11 10:04:07');
INSERT INTO `gonggao` VALUES (9, '公告名称9', 'upload/gonggao9.jpg', 1, '2023-03-11 10:04:07', '公告详情9', '2023-03-11 10:04:07');
INSERT INTO `gonggao` VALUES (10, '公告名称10', 'upload/gonggao10.jpg', 2, '2023-03-11 10:04:07', '公告详情10', '2023-03-11 10:04:07');
INSERT INTO `gonggao` VALUES (11, '公告名称11', 'upload/gonggao11.jpg', 2, '2023-03-11 10:04:07', '公告详情11', '2023-03-11 10:04:07');
INSERT INTO `gonggao` VALUES (12, '公告名称12', 'upload/gonggao12.jpg', 2, '2023-03-11 10:04:07', '公告详情12', '2023-03-11 10:04:07');
INSERT INTO `gonggao` VALUES (13, '公告名称13', 'upload/gonggao13.jpg', 2, '2023-03-11 10:04:07', '公告详情13', '2023-03-11 10:04:07');
INSERT INTO `gonggao` VALUES (14, '公告名称14', 'upload/gonggao14.jpg', 2, '2023-03-11 10:04:07', '公告详情14', '2023-03-11 10:04:07');

-- ----------------------------
-- Table structure for price_strategy
-- ----------------------------
DROP TABLE IF EXISTS `price_strategy`;
CREATE TABLE `price_strategy`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `station_id` int NULL DEFAULT NULL COMMENT '充电桩ID，NULL表示全局默认',
  `strategy_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '策略名称',
  `period_type` int NOT NULL COMMENT '时段类型 1:尖 2:峰 3:平 4:谷',
  `start_hour` tinyint NOT NULL COMMENT '开始小时(0-23)',
  `end_hour` tinyint NOT NULL COMMENT '结束小时(0-23)',
  `electric_price` decimal(10, 4) NOT NULL COMMENT '电价(元/kWh)',
  `service_price` decimal(10, 4) NOT NULL COMMENT '服务费(元/kWh)',
  `is_active` tinyint(1) NULL DEFAULT 1 COMMENT '是否启用',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_station_id`(`station_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '分时电价策略' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of price_strategy
-- ----------------------------
INSERT INTO `price_strategy` VALUES (1, NULL, '尖时段', 1, 10, 15, 1.2000, 0.8000, 1, '2026-03-22 02:03:27');
INSERT INTO `price_strategy` VALUES (2, NULL, '峰时段', 2, 7, 10, 0.9000, 0.6000, 1, '2026-03-22 02:03:27');
INSERT INTO `price_strategy` VALUES (3, NULL, '峰时段', 2, 15, 22, 0.9000, 0.6000, 1, '2026-03-22 02:03:27');
INSERT INTO `price_strategy` VALUES (4, NULL, '平时段', 3, 6, 7, 0.6500, 0.4000, 1, '2026-03-22 02:03:27');
INSERT INTO `price_strategy` VALUES (5, NULL, '平时段', 3, 22, 23, 0.6500, 0.4000, 1, '2026-03-22 02:03:27');
INSERT INTO `price_strategy` VALUES (6, NULL, '谷时段', 4, 23, 6, 0.3500, 0.2000, 1, '2026-03-22 02:03:27');

-- ----------------------------
-- Table structure for token
-- ----------------------------
DROP TABLE IF EXISTS `token`;
CREATE TABLE `token`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint NOT NULL COMMENT '儿童id',
  `username` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '儿童名',
  `tablename` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '表名',
  `role` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '角色',
  `token` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `expiratedtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = 'token表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of token
-- ----------------------------
INSERT INTO `token` VALUES (1, 1, 'a1', 'yonghu', '用户', '7f4kiq34ffa8ci1rxnku20c5bel2y34g', '2023-03-11 10:06:01', '2025-05-05 11:43:30');
INSERT INTO `token` VALUES (2, 1, 'admin', 'users', '管理员', 'avkn42jvmq4rjezpfznh6ounxysybztl', '2023-03-11 10:11:36', '2026-03-22 23:49:22');
INSERT INTO `token` VALUES (4, 3, '用户3', 'yonghu', '用户', '0f03bddc75414da8b24c104aaf83b3ac', '2026-03-22 11:34:00', '2026-03-29 11:34:00');
INSERT INTO `token` VALUES (5, 4, 'wx_mock_177', 'yonghu', '用户', 'd370278e8aed4e39b5df74140960da62', '2026-03-22 18:28:31', '2026-03-29 18:28:31');
INSERT INTO `token` VALUES (6, 5, 'wx_mock_177', 'yonghu', '用户', 'd9ab2c05253f427ba71efe5c6502e458', '2026-04-04 13:41:58', '2026-04-11 13:41:58');
INSERT INTO `token` VALUES (7, 6, 'wx_mock_177', 'yonghu', '用户', '40f2aa96588745b8a3ef6d898b1a41a5', '2026-04-04 13:44:57', '2026-04-11 13:44:57');

-- ----------------------------
-- Table structure for user_integral
-- ----------------------------
DROP TABLE IF EXISTS `user_integral`;
CREATE TABLE `user_integral`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `yonghu_id` int NOT NULL COMMENT '用户ID',
  `integral` int NOT NULL DEFAULT 0 COMMENT '积分变动(正增负减)',
  `source` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '来源描述',
  `source_type` int NULL DEFAULT 1 COMMENT '来源类型 1:充电奖励 2:反馈审核 3:签到 4:消费抵扣',
  `ref_id` int NULL DEFAULT NULL COMMENT '关联业务ID',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_yonghu_id`(`yonghu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户积分' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_integral
-- ----------------------------
INSERT INTO `user_integral` VALUES (1, 4, 1, '充电奖励', 1, 11, '2026-03-22 18:29:41');
INSERT INTO `user_integral` VALUES (2, 3, 1227, '充电奖励', 1, 9, '2026-03-22 18:32:59');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '儿童名',
  `password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `role` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '管理员' COMMENT '角色',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '管理员' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'admin', '123456', '管理员', '2023-03-11 10:03:55');

-- ----------------------------
-- Table structure for yonghu
-- ----------------------------
DROP TABLE IF EXISTS `yonghu`;
CREATE TABLE `yonghu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '账户',
  `password` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '密码',
  `yonghu_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户姓名 Search111 ',
  `yonghu_phone` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户手机号',
  `yonghu_id_number` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户身份证号',
  `yonghu_photo` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `sex_types` int NULL DEFAULT NULL COMMENT '性别',
  `yonghu_email` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `new_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '余额 ',
  `total_integral` int NULL DEFAULT 0 COMMENT '总积分',
  `openid` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '微信openid',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of yonghu
-- ----------------------------
INSERT INTO `yonghu` VALUES (1, '用户1', '123456', '用户姓名1', '17703786901', '410224199010102001', 'upload/yonghu1.jpg', 1, '1@qq.com', 796.66, 0, NULL, '2023-03-11 10:04:07');
INSERT INTO `yonghu` VALUES (2, '用户2', '123456', '用户姓名2', '17703786902', '410224199010102002', 'upload/yonghu2.jpg', 1, '2@qq.com', 694.02, 0, NULL, '2023-03-11 10:04:07');
INSERT INTO `yonghu` VALUES (3, '用户3', '123456', '用户姓名3', '17703786903', '410224199010102003', 'upload/yonghu3.jpg', 1, '3@qq.com', 342.43, 1227, NULL, '2023-03-11 10:04:07');
INSERT INTO `yonghu` VALUES (4, 'wx_mock_177', 'c86f61cb', '微信用户', NULL, NULL, 'https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132', NULL, NULL, 98.42, 1, 'mock_1774175311289', '2026-03-22 18:28:31');
INSERT INTO `yonghu` VALUES (5, 'wx_mock_177', 'a6fba19a', '微信用户', NULL, NULL, 'https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132', NULL, NULL, 0.00, 0, 'mock_1775281317848', '2026-04-04 13:41:58');
INSERT INTO `yonghu` VALUES (6, 'wx_mock_177', 'ffdf4cc3', '微信用户', NULL, NULL, 'https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132', NULL, NULL, 0.00, 0, 'mock_1775281496518', '2026-04-04 13:44:57');

SET FOREIGN_KEY_CHECKS = 1;
