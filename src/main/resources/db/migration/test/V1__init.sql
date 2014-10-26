-- ----------------------------
-- Table structure for `statuses`
-- ----------------------------
DROP TABLE IF EXISTS `statuses`;
CREATE TABLE `statuses` (
  `id` INT(11) NOT NULL,
  `name` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for `movie`
-- ----------------------------
DROP TABLE IF EXISTS `movies`;
CREATE TABLE `movies` (
  `id`    INT(11) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(256) DEFAULT NULL,
  `imdb_id` VARCHAR(256) DEFAULT NULL,
  `tmdb_id` INT(11) DEFAULT NULL,
  `url`   VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for `failed_movies`
-- ----------------------------
DROP TABLE IF EXISTS `failed_movies`;
CREATE TABLE `failed_movies` (
  `id`        INT(11)      NOT NULL AUTO_INCREMENT,
  `title`     VARCHAR(255) NOT NULL,
  `url`       VARCHAR(255) NOT NULL,
  `status_id` INT(11)      NOT NULL,
  `imdb_id`   VARCHAR(255) NOT NULL,
  `tmdb_id`   INT          NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`status_id`) references `statuses` (`id`)
);

-- ----------------------------
-- Table structure for `genomes`
-- ----------------------------
DROP TABLE IF EXISTS `genomes`;
CREATE TABLE `genomes` (
  `id`   INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(256) DEFAULT NULL,
  `movie_id` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`)
);

-- ----------------------------
-- Table structure for `genome_values`
-- ----------------------------
DROP TABLE IF EXISTS `genome_values`;
CREATE TABLE `genome_values` (
  `id`   INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(256) DEFAULT NULL,
  `genome_id` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`genome_id`) REFERENCES `genomes` (`id`)
);
-- ----------------------------
-- Records of statuses
-- ----------------------------
INSERT INTO `statuses` VALUES ('0', 'UNKNOWN');
INSERT INTO `statuses` VALUES ('1', 'SUSPICIOUS');
INSERT INTO `statuses` VALUES ('2', 'RECOVERED');
INSERT INTO `statuses` VALUES ('3', 'FAILED');


-- Autogenerated: do not edit this file

CREATE TABLE BATCH_JOB_INSTANCE  (
  JOB_INSTANCE_ID BIGINT IDENTITY NOT NULL PRIMARY KEY ,
  VERSION BIGINT ,
  JOB_NAME VARCHAR(100) NOT NULL,
  JOB_KEY VARCHAR(32) NOT NULL,
  constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
) ;

CREATE TABLE BATCH_JOB_EXECUTION  (
  JOB_EXECUTION_ID BIGINT IDENTITY NOT NULL PRIMARY KEY ,
  VERSION BIGINT  ,
  JOB_INSTANCE_ID BIGINT NOT NULL,
  CREATE_TIME TIMESTAMP NOT NULL,
  START_TIME TIMESTAMP DEFAULT NULL ,
  END_TIME TIMESTAMP DEFAULT NULL ,
  STATUS VARCHAR(10) ,
  EXIT_CODE VARCHAR(2500) ,
  EXIT_MESSAGE VARCHAR(2500) ,
  LAST_UPDATED TIMESTAMP,
  JOB_CONFIGURATION_LOCATION VARCHAR(2500) NULL,
  constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
  references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
) ;

CREATE TABLE BATCH_JOB_EXECUTION_PARAMS  (
  JOB_EXECUTION_ID BIGINT NOT NULL ,
  TYPE_CD VARCHAR(6) NOT NULL ,
  KEY_NAME VARCHAR(100) NOT NULL ,
  STRING_VAL VARCHAR(250) ,
  DATE_VAL TIMESTAMP DEFAULT NULL ,
  LONG_VAL BIGINT ,
  DOUBLE_VAL DOUBLE PRECISION ,
  IDENTIFYING CHAR(1) NOT NULL ,
  constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
  references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;

CREATE TABLE BATCH_STEP_EXECUTION  (
  STEP_EXECUTION_ID BIGINT IDENTITY NOT NULL PRIMARY KEY ,
  VERSION BIGINT NOT NULL,
  STEP_NAME VARCHAR(100) NOT NULL,
  JOB_EXECUTION_ID BIGINT NOT NULL,
  START_TIME TIMESTAMP NOT NULL ,
  END_TIME TIMESTAMP DEFAULT NULL ,
  STATUS VARCHAR(10) ,
  COMMIT_COUNT BIGINT ,
  READ_COUNT BIGINT ,
  FILTER_COUNT BIGINT ,
  WRITE_COUNT BIGINT ,
  READ_SKIP_COUNT BIGINT ,
  WRITE_SKIP_COUNT BIGINT ,
  PROCESS_SKIP_COUNT BIGINT ,
  ROLLBACK_COUNT BIGINT ,
  EXIT_CODE VARCHAR(2500) ,
  EXIT_MESSAGE VARCHAR(2500) ,
  LAST_UPDATED TIMESTAMP,
  constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
  references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;

CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT  (
  STEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
  SHORT_CONTEXT VARCHAR(2500) NOT NULL,
  SERIALIZED_CONTEXT LONGVARCHAR ,
  constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
  references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
) ;

CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT  (
  JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
  SHORT_CONTEXT VARCHAR(2500) NOT NULL,
  SERIALIZED_CONTEXT LONGVARCHAR ,
  constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
  references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;

CREATE SEQUENCE BATCH_STEP_EXECUTION_SEQ;
CREATE SEQUENCE BATCH_JOB_EXECUTION_SEQ;
CREATE SEQUENCE BATCH_JOB_SEQ;