CREATE DATABASE IF NOT EXISTS efficio_db;

USE efficio_db;

CREATE TABLE IF NOT EXISTS users
(
    user_id  INT auto_increment PRIMARY KEY,
    username VARCHAR (255) UNIQUE NOT NULL,
    password VARCHAR (255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS projects
(
    project_id    INT auto_increment PRIMARY KEY,
    name          VARCHAR (255) NOT NULL,
    description   TEXT,
    start_date    VARCHAR (255),
    deadline      VARCHAR (255),
    expected_time INT
    );

CREATE TABLE IF NOT EXISTS project_users
(
    project_id INT,
    user_id    INT,
    PRIMARY KEY ( project_id, user_id ),
    FOREIGN KEY ( project_id ) REFERENCES projects ( project_id ) ON DELETE
    CASCADE,
    FOREIGN KEY ( user_id ) REFERENCES users ( user_id ) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS subprojects
(
    subproject_id INT auto_increment PRIMARY KEY,
    project_id    INT,
    name          VARCHAR (255) NOT NULL,
    description   TEXT,
    start_date    VARCHAR (255),
    deadline      VARCHAR (255),
    expected_time INT,
    FOREIGN KEY ( project_id ) REFERENCES projects ( project_id ) ON DELETE
    CASCADE
    );

CREATE TABLE IF NOT EXISTS tasks
(
    task_id       INT auto_increment PRIMARY KEY,
    name          VARCHAR (255) NOT NULL,
    description   TEXT,
    expected_time INT,
    subproject_id INT,
    FOREIGN KEY ( subproject_id ) REFERENCES subprojects ( subproject_id ) ON
                                                                               DELETE CASCADE
    );