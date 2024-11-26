CREATE TABLE IF NOT EXISTS users
(
    user_id  INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS projects
(
    project_id    INT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    description   TEXT,
    start_date    VARCHAR(255),
    deadline      VARCHAR(255),
    expected_time INT
);

CREATE TABLE IF NOT EXISTS project_users
(
    project_id INT,
    user_id    INT,
    PRIMARY KEY (project_id, user_id),
    FOREIGN KEY (project_id) REFERENCES projects (project_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS subprojects
(
    subproject_id INT AUTO_INCREMENT PRIMARY KEY,
    project_id    INT,
    name          VARCHAR(255) NOT NULL,
    description   TEXT,
    start_date    VARCHAR(255),
    deadline      VARCHAR(255),
    expected_time INT,
    FOREIGN KEY (project_id) REFERENCES projects (project_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tasks
(
    task_id       INT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    description   TEXT,
    expected_time INT,
    subproject_id INT,
    FOREIGN KEY (subproject_id) REFERENCES subprojects (subproject_id) ON DELETE CASCADE
);

-- Example Data
INSERT INTO users (username, password)
VALUES ('admin', '123'),
       ('user1', '123'),
       ('user2', '123');

INSERT INTO projects (name, description, start_date, deadline, expected_time)
VALUES ('Project Alpha', 'A shared project', '2024-01-01', '2024-06-30', 120);

INSERT INTO project_users (project_id, user_id)
VALUES (1, 1), -- Admin shares Project Alpha
       (1, 2); -- with User1
