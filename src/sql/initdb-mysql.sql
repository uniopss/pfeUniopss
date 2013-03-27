drop database uniopss
go
CREATE DATABASE  uniopss
go
USE uniopss
go

CREATE TABLE categories
(
id VARCHAR(250) NOT NULL PRIMARY KEY,
metadata BLOB
)
TYPE=InnoDB
go

CREATE TABLE categories_publications
(
  category_id VARCHAR(250) NOT NULL,
  publication_id VARCHAR(250) NOT NULL,
  PRIMARY KEY(category_id,publication_id)
) TYPE=InnoDB
go

CREATE TABLE groups
(
  id VARCHAR(250) NOT NULL PRIMARY KEY,
  metadata BLOB
) TYPE=InnoDB
go

CREATE TABLE publications
(
  id VARCHAR(250) NOT NULL PRIMARY KEY,
  workflow_type VARCHAR(250),
  type_id VARCHAR(250),
  datepubli VARCHAR(250),
  metadata BLOB
) TYPE=InnoDB
go
CREATE TABLE sections
(
  id VARCHAR(250) NOT NULL
  PRIMARY KEY,parent_id VARCHAR(250),
  numindex INTEGER,
  metadata BLOB
) TYPE=InnoDB
go
CREATE TABLE sections_publications
(
  id VARCHAR(250) NOT NULL ,
  section_id VARCHAR(250) NOT NULL,
  publication_id VARCHAR(250) NOT NULL,
  numindex INTEGER,
  PRIMARY KEY(section_id,publication_id)
) TYPE=InnoDB
go
CREATE TABLE type_publications
(
  id VARCHAR(250) NOT NULL PRIMARY KEY,
  metadata BLOB
) TYPE=InnoDB
go
CREATE TABLE users
(
  login VARCHAR(250) NOT NULL PRIMARY KEY,
  password VARCHAR(250),
  metadata BLOB
) TYPE=InnoDB
go
CREATE TABLE users_groups
(	login VARCHAR(250) NOT NULL,
  group_id VARCHAR(250) NOT NULL,
  PRIMARY KEY(login,group_id)
) TYPE=InnoDB
go
CREATE TABLE versions_publications
(	id VARCHAR(250) NOT NULL PRIMARY KEY,
  author_id VARCHAR(250),
  workflow_id INTEGER,
  publication_id VARCHAR(250),
  version INTEGER,
  data BLOB
) TYPE=InnoDB
go

INSERT INTO `users` (`login`,`password`,`metadata`) VALUES ('admin','YXplcnR5',0xACED0005737200136A6176612E7574696C2E486173687461626C6513BB0F25214AE4B803000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000877080000000300000002740005656D61696C74000E61646D696E406D6172732E636F6D7400046E616D6574001841646D696E69737472617465757220546563686E6971756578)
go
INSERT INTO `users` (`login`,`password`,`metadata`) VALUES ('visiteurAnonyme','',0xACED0005737200136A6176612E7574696C2E486173687461626C6513BB0F25214AE4B803000246000A6C6F6164466163746F724900097468726573686F6C6478703F400000000000087708000000030000000078)
go

INSERT INTO `groups` (`id`,`metadata`) VALUES ('admins',0xACED0005737200136A6176612E7574696C2E486173687461626C6513BB0F25214AE4B803000246000A6C6F6164466163746F724900097468726573686F6C6478703F40000000000008770800000003000000017400046E616D6574000F41646D696E6973747261746575727378)
go
INSERT INTO `groups` (`id`,`metadata`) VALUES ('visitors',0xACED0005737200136A6176612E7574696C2E486173687461626C6513BB0F25214AE4B803000246000A6C6F6164466163746F724900097468726573686F6C6478703F400000000000087708000000030000000078)
go
INSERT INTO `groups` (`id`,`metadata`) VALUES ('gValideurs',0xACED0005737200136A6176612E7574696C2E486173687461626C6513BB0F25214AE4B803000246000A6C6F6164466163746F724900097468726573686F6C6478703F40000000000008770800000003000000017400046E616D6574000956616C69646575727378)
go
INSERT INTO `groups` (`id`,`metadata`) VALUES ('gAuteurs',0xACED0005737200136A6176612E7574696C2E486173687461626C6513BB0F25214AE4B803000246000A6C6F6164466163746F724900097468726573686F6C6478703F40000000000008770800000003000000017400046E616D657400074175746575727378)
go

INSERT INTO `users_groups` (`login`,`group_id`) VALUES ('visiteurAnonyme','visitors')
go
INSERT INTO `users_groups` (`login`,`group_id`) VALUES ('admin','admins')
go


ALTER TABLE sections
  add foreign key (parent_id) references sections(id)
go

ALTER TABLE categories_publications
  add foreign key (category_id) references categories(id)
go
ALTER TABLE categories_publications
  add foreign key (publication_id) references publications(id)
go

ALTER TABLE publications
  add foreign key (type_id) references type_publications(id)
go


ALTER TABLE sections_publications
  add foreign key (section_id) references sections(id)
go


ALTER TABLE sections_publications
  add foreign key (publication_id) references publications(id)
go



ALTER  TABLE users_groups
  add foreign key (group_id) references groups(id)
go

ALTER  TABLE users_groups
  add foreign key (login) references users(login)
go



ALTER TABLE versions_publications
ADD foreign key (publication_id) references publications(id)
go

ALTER TABLE versions_publications
ADD foreign key (author_id) references users(login)
go


/**  osworkflow legacy database */

/**  copy du schema de etc/mysql.sql ( 2.8.0 ) */

drop table if exists OS_PROPERTYENTRY cascade
go
create table OS_PROPERTYENTRY
(
  GLOBAL_KEY varchar(250) NOT NULL,
  ITEM_KEY varchar(250) NOT NULL,
  ITEM_TYPE tinyint,
  STRING_VALUE varchar(255),
  DATE_VALUE datetime,
  DATA_VALUE blob,
  FLOAT_VALUE float,
  NUMBER_VALUE numeric,
  primary key (GLOBAL_KEY, ITEM_KEY)
)TYPE=InnoDB
go

drop table if exists OS_WFENTRY cascade
go
create table OS_WFENTRY
(
    ID bigint NOT NULL,
    NAME varchar(60),
    STATE integer,
    primary key (ID)
)TYPE=InnoDB
go

drop table if exists OS_CURRENTSTEP
go
create table OS_CURRENTSTEP
(
    ID bigint NOT NULL,
    ENTRY_ID bigint,
    STEP_ID integer,
    ACTION_ID integer,
    OWNER varchar(35),
    START_DATE datetime,
    FINISH_DATE datetime,
    DUE_DATE datetime,
    STATUS varchar(40),
  CALLER varchar(35),

    primary key (ID),
    index (ENTRY_ID),
    foreign key (ENTRY_ID) references OS_WFENTRY(ID),
    index (OWNER),
    index (CALLER)
)TYPE=InnoDB
go

drop table if exists OS_HISTORYSTEP
go
create table OS_HISTORYSTEP
(
    ID bigint NOT NULL,
    ENTRY_ID bigint,
    STEP_ID integer,
    ACTION_ID integer,
    OWNER varchar(35),
    START_DATE datetime,
    FINISH_DATE datetime,
    DUE_DATE datetime,
    STATUS varchar(40),
    CALLER varchar(35),

    primary key (ID),
    index (ENTRY_ID),
    foreign key (ENTRY_ID) references OS_WFENTRY(ID),
    index (OWNER),
    index (CALLER)
)TYPE=InnoDB
go

drop table if exists OS_CURRENTSTEP_PREV
go
create table OS_CURRENTSTEP_PREV
(
    ID bigint NOT NULL,
    PREVIOUS_ID bigint NOT NULL,
    primary key (ID, PREVIOUS_ID),
    index (ID),
    foreign key (ID) references OS_CURRENTSTEP(ID),
    index (PREVIOUS_ID),
    foreign key (PREVIOUS_ID) references OS_HISTORYSTEP(ID)
)TYPE=InnoDB
go

drop table if exists OS_HISTORYSTEP_PREV
go
create table OS_HISTORYSTEP_PREV
(
    ID bigint NOT NULL,
    PREVIOUS_ID bigint NOT NULL,
    primary key (ID, PREVIOUS_ID),
    index (ID),
    foreign key (ID) references OS_HISTORYSTEP(ID),
    index (PREVIOUS_ID),
    foreign key (PREVIOUS_ID) references OS_HISTORYSTEP(ID)
)TYPE=InnoDB
go

drop table if exists OS_STEPIDS
go
CREATE TABLE OS_STEPIDS
(
   ID bigint NOT NULL AUTO_INCREMENT,
   PRIMARY KEY (id)
 )TYPE=InnoDB
go

drop table if exists OS_ENTRYIDS
go

CREATE TABLE OS_ENTRYIDS
(
   ID bigint NOT NULL AUTO_INCREMENT,
   PRIMARY KEY (id)
 )TYPE=InnoDB

go


create table ref_sequence
(
	seq_name varchar (40) NOT NULL ,
	seq_count int NOT NULL ,
	seq_inc int NOT NULL,
	primary key (seq_name)
)
GO

insert into ref_sequence(seq_name, seq_count, seq_inc)
values ('publications', 50000, 1)
go



