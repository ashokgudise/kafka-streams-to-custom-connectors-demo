CREATE DATABASE IF NOT EXISTS deals_db;
USE deals_db;

CREATE TABLE IF NOT EXISTS ALERTSUBSCRIPTION (
                                                 ID serial NOT NULL PRIMARY KEY,
                                                 NAME varchar(100),
    TYPE varchar(50),
    EMAIL varchar(200),
    SMS varchar(200),
    MODIFIED TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    INDEX `modified_index` (`MODIFIED`)
    );