CREATE TABLE `mq_trans_message` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_code` varchar(64) NOT NULL DEFAULT 'system' COMMENT '租户唯一编码，system-系统级别，跨租户共享',
  `topic` varchar(128) NOT NULL DEFAULT '' COMMENT 'topic',
  `tag` varchar(128) NOT NULL DEFAULT '' COMMENT 'tag',
  `message_key` varchar(256) NOT NULL DEFAULT '' COMMENT '消息key',
  `message` text NOT NULL COMMENT '消息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='事务消息记录表';

CREATE TABLE `user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(128) NOT NULL DEFAULT '' COMMENT '用户名',
  `age` int(11) NOT NULL DEFAULT '0' COMMENT '年龄',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户表';