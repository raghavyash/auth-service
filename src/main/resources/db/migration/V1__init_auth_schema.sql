CREATE TABLE `user_login` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_date` datetime(10) DEFAULT NULL,
  `expiry_date` datetime(10) NOT NULL,
  `ip_address` varchar(255) DEFAULT NULL,
  `is_valid` bit(1) DEFAULT NULL,
  `role` enum('USER','ADMIN') NOT NULL,
  `token` varchar(255) NOT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  `user_email` varchar(255) NOT NULL,
  `user_password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY  (`token`),
  UNIQUE KEY (`user_email`)
);

/

DROP TABLE IF EXISTS `user_login_audit`;

CREATE TABLE `user_login_audit` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_date` datetime(10) DEFAULT NULL,
  `ip_address` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  `user_email` varchar(255) NOT NULL,
  `user_login_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY  (`user_email`),
  KEY  (`user_login_id`),
  CONSTRAINT  FOREIGN KEY (`user_login_id`) REFERENCES `user_login` (`id`)
);

