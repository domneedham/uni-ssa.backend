INSERT INTO app_user (firstname, surname, email, password, user_role) VALUES ('Test', 'Staff', 'test@staff.com', '$2a$10$vvmS9h8e6cQjkXikKdR23ez3H2p3vjdq81F2RgtgsMwsJ7XqkRpQG', 'STAFF');
INSERT INTO app_user (firstname, surname, email, password, user_role) VALUES ('Test', 'Manager', 'test@manager.com', '$2a$10$vvmS9h8e6cQjkXikKdR23ez3H2p3vjdq81F2RgtgsMwsJ7XqkRpQG', 'MANAGER');
INSERT INTO app_user (firstname, surname, email, password, user_role) VALUES ('Staffy', 'Staff', 'test@staffy.com', '$2a$10$vvmS9h8e6cQjkXikKdR23ez3H2p3vjdq81F2RgtgsMwsJ7XqkRpQG', 'STAFF');

INSERT INTO manager (user_id) VALUES (2);
INSERT INTO staff (user_id, manager_id) VALUES (1, 2);
INSERT INTO staff (user_id, manager_id) VALUES (3, 2);
INSERT INTO manager_staff_link (manager_id, staff_id) VALUES (2, 1);
INSERT INTO manager_staff_link (manager_id, staff_id) VALUES (2, 3);

INSERT INTO category (name, icon) VALUES ('Catastrophic', 57718);
INSERT INTO category (name, icon) VALUES ('Cabin Fever', 57629);

INSERT INTO skill (name, category_id) VALUES ('Skiing', 1);
INSERT INTO skill (name, category_id) VALUES ('Skiball', 1);
INSERT INTO skill (name, category_id) VALUES ('Skipping', 2);

INSERT INTO staff_skill (skill_id, staff_id, last_updated, rating, expires) VALUES (1, 1, NOW(), 5, NOW());
INSERT INTO staff_skill (skill_id, staff_id, last_updated, rating) VALUES (3, 1, NOW(), 5);
INSERT INTO staff_skill (skill_id, staff_id, last_updated, rating) VALUES (1, 3, NOW(), 3);
INSERT INTO staff_skill (skill_id, staff_id, last_updated, rating) VALUES (3, 3, NOW(), 3);