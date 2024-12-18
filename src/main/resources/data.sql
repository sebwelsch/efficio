INSERT INTO users (username, password)
VALUES ('santa', '$2a$10$H5GJscoD5p.U6Hyu1/cunu3z4QZ/HksjKU8h.YT1opL0xPrJHawJO'),
       ('elf1', '$2a$10$H5GJscoD5p.U6Hyu1/cunu3z4QZ/HksjKU8h.YT1opL0xPrJHawJO'),
       ('elf2', '$2a$10$H5GJscoD5p.U6Hyu1/cunu3z4QZ/HksjKU8h.YT1opL0xPrJHawJO');

INSERT INTO projects (name, description, start_date, deadline, expected_time)
VALUES ('Project Santa', 'Santa needs to be ready for christmas', '2024-12-01', '2024-12-24',
        22);

INSERT INTO subprojects (name, description, start_date, deadline, expected_time, project_id)
VALUES ('Make gifts', 'Have santas helpers make all christmas gifts', '2024-12-10', '2024-12-23',
        9, 1);

INSERT INTO tasks (name, description, expected_time, subproject_id)
VALUES ('Make LEGO Ferrari', 'Make the LEGO Ferrari set ready', 3, 1);

INSERT INTO tasks (name, description, expected_time, subproject_id)
VALUES ('Make LEGO Titanic', 'Make the LEGO Titanic set ready', 6, 1);

INSERT INTO subprojects (name, description, start_date, deadline, expected_time, project_id)
VALUES ('Get sleigh ready', 'Prepare the sleigh and reindeer', '2024-12-21', '2024-12-23',
        13, 1);

INSERT INTO tasks (name, description, expected_time, subproject_id)
VALUES ('Paint the sleigh', 'Paint the sleigh so it looks shiny and brand new', 10, 2);

INSERT INTO tasks (name, description, expected_time, subproject_id)
VALUES ('Feed the reindeer', 'Make the LEGO Titanic set ready', 3, 2);

INSERT INTO project_users (project_id, user_id)
VALUES (1, 1),
       (1, 2);
