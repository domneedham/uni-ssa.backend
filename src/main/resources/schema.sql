CREATE TABLE app_user (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    firstname   CHAR (20) NOT NULL,
    surname     CHAR (20) NOT NULL,
    email       CHAR (50) NOT NULL,
    PASSWORD    CHAR (255) NOT NULL,
    user_role   CHAR (20) NOT NULL
);

CREATE TABLE manager (
    user_id     INT NOT NULL PRIMARY KEY,
    foreign key (user_id) references app_user (id)
);

CREATE TABLE staff (
    user_id     INT NOT NULL PRIMARY KEY,
    manager_id  INT NOT NULL,
    foreign key (user_id) references app_user (id),
    foreign key (manager_id) references app_user (id)
);

CREATE TABLE manager_staff_link (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    manager_id  INT NOT NULL,
    staff_id    INT NOT NULL,
    foreign key (manager_id) references manager (user_id),
    foreign key (staff_id) references staff (user_id)
);

CREATE TABLE category (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        CHAR (255) NOT NULL,
    icon        INT NOT NULL
);

CREATE TABLE skill (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        CHAR (255) NOT NULL,
    category_id INT NOT NULL,
    foreign key (category_id) references category (id)
);

CREATE TABLE staff_skill (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    skill_id    INT NOT NULL,
    staff_id    INT NOT NULL,
    rating      INT NOT NULL,
    last_updated DATE NOT NULL,
    expires     DATE,
    foreign key (skill_id) references skill (id),
    foreign key (staff_id) references staff (user_id)
);







