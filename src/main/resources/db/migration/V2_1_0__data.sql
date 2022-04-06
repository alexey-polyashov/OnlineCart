
INSERT INTO products (price, title, category_id)
VALUES
(10,'Сыр', 1),
(15,'Молоко', 1),
(15,'Яйца (6шт)', 3),
(20,'Сметана 10%', 1),
(30,'Колбаса сырокопченная', 2),
(30,'Колбаса варенная', 2),
(40,'Окорок свинной', 4),
(40,'Сметана 15%', 1),
(50,'Сметана 20%', 1),
(60,'product 10', 3);


INSERT INTO categories (title)
VALUES
('Молочные продукты'),
('Колбасные изделия'),
('Яйца'),
('Охлажденное мясо'),
('Птица'),
('Рыба'),
('Хлебобулочные изделия'),
('Чай');

insert into users (username, password, email, phone, address)
values ('user', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'bob_johnson@gmail.com', '222-55-44', 'Moscow, Panfilova 4'),
       ('admin', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'john_johnson@gmail.com', '123-34-55', 'Rostov, Lenina 12' ),
       ('user2', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'nick_davidson@gmail.com', '324-53-34', 'Voronezh, Lizyukova 8');

insert into roles (name)
values ('ROLE_USER'),
       ('ROLE_ADMIN');

insert into users_roles (user_id, role_id)
values (1, 1),
       (2, 2),
       (3, 1);
