/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : plan

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 28/06/2020 15:59:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单/按钮ID',
  `parent_id` bigint(20) NOT NULL COMMENT '上级菜单ID',
  `permission_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单/按钮名称',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对应路由path',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对应路由组件component',
  `perms` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `type` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类型 0菜单 1按钮 2 API',
  `order_num` double(20, 0) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (5, 0, '系统管理', '/system', 'layout', 'system:list', 'guide', '0', 1, '2019-12-25 09:13:07', '2019-12-25 09:13:09');
INSERT INTO `sys_permission` VALUES (6, 5, '用户管理', 'user', 'system/pages/UserManage', 'user:list', NULL, '0', 2, '2019-12-25 09:14:04', '2019-12-25 09:14:07');
INSERT INTO `sys_permission` VALUES (7, 5, '角色管理', 'role', 'system/pages/RoleManage', 'role:list', NULL, '0', 3, '2019-12-25 09:15:29', '2019-12-25 09:15:31');
INSERT INTO `sys_permission` VALUES (8, 5, '菜单管理', 'menu', 'system/pages/MenuManage', 'permission:list', NULL, '0', 4, '2019-12-25 09:16:37', '2019-12-25 09:16:39');
INSERT INTO `sys_permission` VALUES (10, 5, 'Menu1', 'menu1', 'system/pages/menu/index', 'menu:list', NULL, '0', 10, '2019-12-26 02:35:29', NULL);
INSERT INTO `sys_permission` VALUES (11, 10, 'Menu123', 'menu123', 'system/pages/menu/menu', 'menu-1:list', NULL, '0', 25, '2019-12-26 02:49:36', NULL);
INSERT INTO `sys_permission` VALUES (21, 10, 'VVV1', 'VVV', 'VV', 'VV', NULL, '0', 55, '2019-12-27 02:35:09', NULL);
INSERT INTO `sys_permission` VALUES (22, 6, '重复校验', '/system/sanme', NULL, NULL, NULL, '2', NULL, '2019-12-27 02:41:43', NULL);
INSERT INTO `sys_permission` VALUES (25, 6, 'HAHAH', 'asd', NULL, NULL, NULL, '2', NULL, '2019-12-27 07:06:16', NULL);
INSERT INTO `sys_permission` VALUES (26, 0, '聊天室', '/chat', 'layout', 'chat:menu', 'qq', '0', 1, '2020-05-17 03:14:19', NULL);
INSERT INTO `sys_permission` VALUES (27, 26, '在线聊天', 'onlineChat', 'chat/pages/onlineChat', 'chat:onlineChat', NULL, '0', 2, '2020-05-17 03:17:46', NULL);
INSERT INTO `sys_permission` VALUES (30, 26, '客户端聊天', 'clientChat', 'chat/pages/clientChat', 'clientChat', NULL, '0', 3, '2020-05-17 03:30:18', NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'admin11122', '管理员remark', '2019-12-19 18:10:17', '2020-05-17 03:30:31');
INSERT INTO `sys_role` VALUES (2, 'vipuser', '高级用户remark', '2019-12-19 18:10:17', '2019-12-27 09:43:07');
INSERT INTO `sys_role` VALUES (3, 'user', '普通用户remark', '2019-12-19 18:10:17', '2019-12-19 18:10:19');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `permission_id` int(11) NOT NULL COMMENT '权限ID'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (3, 5);
INSERT INTO `sys_role_permission` VALUES (3, 6);
INSERT INTO `sys_role_permission` VALUES (3, 7);
INSERT INTO `sys_role_permission` VALUES (2, 10);
INSERT INTO `sys_role_permission` VALUES (2, 11);
INSERT INTO `sys_role_permission` VALUES (2, 21);
INSERT INTO `sys_role_permission` VALUES (2, 5);
INSERT INTO `sys_role_permission` VALUES (1, 5);
INSERT INTO `sys_role_permission` VALUES (1, 6);
INSERT INTO `sys_role_permission` VALUES (1, 7);
INSERT INTO `sys_role_permission` VALUES (1, 8);
INSERT INTO `sys_role_permission` VALUES (1, 10);
INSERT INTO `sys_role_permission` VALUES (1, 11);
INSERT INTO `sys_role_permission` VALUES (1, 21);
INSERT INTO `sys_role_permission` VALUES (1, 26);
INSERT INTO `sys_role_permission` VALUES (1, 27);
INSERT INTO `sys_role_permission` VALUES (1, 30);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态 0锁定 1有效',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '最近访问时间',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '0男 1女 2保密',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'ximen', '$2a$10$/WorGbDQ.JldBetSlCK/4.0ibWDgWOKOxA/K68zbMAszjpu6tIalu', 2, '1632659187@qq.com', '13236128814', '0', '2019-12-22 14:09:45', '2019-12-22 06:22:31', NULL, '2', 'haha', 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif');
INSERT INTO `sys_user` VALUES (5, 'xiaohei', '$2a$10$/WorGbDQ.JldBetSlCK/4.0ibWDgWOKOxA/K68zbMAszjpu6tIalu', 2, '78@qq.com', '65874', '0', '2019-12-19 12:17:25', '2019-12-27 09:40:15', NULL, '1', NULL, 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif');
INSERT INTO `sys_user` VALUES (6, 'xixi', '$2a$10$Rqe7Rs1mlPpdzgAwleP9JuhEm1s1SXYRwM8fNm7SEEQVqblyXQ5DK', NULL, '782099197@qq.com', '13236128814', '1', '2019-12-22 07:43:43', '2020-05-10 08:26:52', NULL, '0', NULL, 'default.jpg');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (3, 1);
INSERT INTO `sys_user_role` VALUES (3, 2);
INSERT INTO `sys_user_role` VALUES (3, 3);
INSERT INTO `sys_user_role` VALUES (4, 1);
INSERT INTO `sys_user_role` VALUES (4, 2);
INSERT INTO `sys_user_role` VALUES (4, 3);
INSERT INTO `sys_user_role` VALUES (3, 1);
INSERT INTO `sys_user_role` VALUES (3, 2);
INSERT INTO `sys_user_role` VALUES (3, 3);
INSERT INTO `sys_user_role` VALUES (4, 1);
INSERT INTO `sys_user_role` VALUES (4, 2);
INSERT INTO `sys_user_role` VALUES (4, 3);
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (5, 2);
INSERT INTO `sys_user_role` VALUES (6, 1);
INSERT INTO `sys_user_role` VALUES (6, 2);

SET FOREIGN_KEY_CHECKS = 1;
