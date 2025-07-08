CREATE TABLE IF NOT EXISTS permissions
(
    id      INT PRIMARY KEY,
    name    VARCHAR(25)
);
-- Insert permissions
INSERT INTO permissions (id, name)
SELECT 1, 'list' WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'list');
INSERT INTO permissions (id, name)
SELECT 2, 'create' WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'create');
INSERT INTO permissions (id, name)
SELECT 3, 'edit' WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'edit');
INSERT INTO permissions (id, name)
SELECT 4, 'delete' WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE name = 'delete');


CREATE TABLE IF NOT EXISTS `groups`
(
    id      INT PRIMARY KEY,
    name    VARCHAR(25)
);
-- Insert groups
INSERT INTO `groups` (id, name)
SELECT 1, 'ADMIN' WHERE NOT EXISTS (SELECT 1 FROM `groups` WHERE name = 'ADMIN');
INSERT INTO `groups` (id, name)
SELECT 2, 'USER' WHERE NOT EXISTS (SELECT 1 FROM `groups` WHERE name = 'USER');


CREATE TABLE IF NOT EXISTS group_permission
(
    group_id            INT,
    permission_id       INT,
    FOREIGN KEY(group_id) REFERENCES `groups`(id),
    FOREIGN KEY(permission_id) REFERENCES permissions(id),
    PRIMARY KEY (group_id, permission_id)
);
-- Assign permissions to groups
INSERT INTO group_permission (group_id, permission_id)
SELECT 1, 1 WHERE NOT EXISTS (SELECT 1 FROM group_permission WHERE group_id = 1 AND permission_id = 1);
INSERT INTO group_permission (group_id, permission_id)
SELECT 1, 2 WHERE NOT EXISTS (SELECT 1 FROM group_permission WHERE group_id = 1 AND permission_id = 2);
INSERT INTO group_permission (group_id, permission_id)
SELECT 1, 3 WHERE NOT EXISTS (SELECT 1 FROM group_permission WHERE group_id = 1 AND permission_id = 3);
INSERT INTO group_permission (group_id, permission_id)
SELECT 1, 4 WHERE NOT EXISTS (SELECT 1 FROM group_permission WHERE group_id = 1 AND permission_id = 4);
INSERT INTO group_permission (group_id, permission_id)
SELECT 2, 1 WHERE NOT EXISTS (SELECT 1 FROM group_permission WHERE group_id = 2 AND permission_id = 1);


CREATE TABLE IF NOT EXISTS users
(
    id          CHAR(36) PRIMARY KEY,
    user_name   VARCHAR(50) UNIQUE NOT NULL,
    password    VARCHAR(255) NOT NULL,
    group_id    INT,
    FOREIGN KEY(group_id) REFERENCES `groups`(id)
);
-- Insert default admin user (change default bcrypt password to your own)
INSERT INTO users (id, user_name, password, group_id)
SELECT UUID(),
       'admin',
       '$2a$12$KqDcGB.tOpZlSJmVurF1x.7ihyTG2Dotjz6x3kwoPfhHig5Ed6XM2',
       1    WHERE NOT EXISTS (SELECT 1 FROM users WHERE user_name = 'admin');

CREATE TABLE IF NOT EXISTS employees
(
    id          BIGINT PRIMARY KEY,
    first_name  VARCHAR(50) NOT NULL,
    last_name   VARCHAR(50) NOT NULL,
    email       VARCHAR(100) UNIQUE NOT NULL
);