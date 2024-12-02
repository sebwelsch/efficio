INSERT INTO users (username, password)
VALUES ('admin', '123'),
       ('user1', '123'),
       ('user2', '123');

INSERT INTO projects (name, description, start_date, deadline, expected_time)
VALUES ('Project Alpha', 'A shared project to test features in our development process', '2024-11-20', '2024-12-18',
        672);

INSERT INTO project_users (project_id, user_id)
VALUES (1, 1),
       (1, 2);
