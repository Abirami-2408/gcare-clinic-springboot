-- GCare database schema (MySQL 8)
CREATE DATABASE IF NOT EXISTS gcare CHARACTER SET utf8mb4;
USE gcare;

CREATE TABLE IF NOT EXISTS reviews (
                                       id      INT NOT NULL AUTO_INCREMENT,
                                       name    VARCHAR(100),
    comment TEXT,
    rating  INT,
    status  BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS sliders (
                                       id          INT NOT NULL AUTO_INCREMENT,
                                       title       VARCHAR(150),
    description TEXT,
    image       VARCHAR(255),
    status      BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS services (
                                        id          INT NOT NULL AUTO_INCREMENT,
                                        title       VARCHAR(150),
    description TEXT,
    image       VARCHAR(255),
    status      BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS appointments (
                                            id               INT NOT NULL AUTO_INCREMENT,
                                            patient_name     VARCHAR(100) NOT NULL,
    phone            VARCHAR(15)  NOT NULL,
    email            VARCHAR(100),
    appointment_date DATE         NOT NULL,
    appointment_time VARCHAR(20)  NOT NULL,
    message          TEXT,
    status           VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_slot (appointment_date, appointment_time)
    );

CREATE TABLE IF NOT EXISTS admin_users (
                                           id       INT NOT NULL AUTO_INCREMENT,
                                           username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
    );