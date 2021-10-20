
CREATE TABLE products (
    id bigserial primary key,
    price numeric(10,2) not null,
    create_time timestamp default current_timestamp,
    update_time timestamp default current_timestamp,
    title VARCHAR(255),
    category_id bigint
);

INSERT INTO products (price, title, category_id)
VALUES
(10,'product 1', 1),
(15,'product 2', 1),
(15,'product 3', 1),
(20,'product 4', 1),
(50,'product 5', 2),
(50,'product 6', 2),
(50,'product 7', 2),
(10,'product 8', 3),
(18,'product 9', 3),
(19,'product 10', 3),
(19,'product 12', 4),
(19,'product 13', 4),
(19,'product 14', 4),
(19,'product 15', 4),
(19,'product 16', 5),
(19,'product 17', 5),
(19,'product 18', 5),
(19,'product 19', 5),
(19,'product 20', 5),
(19,'product 21', 6);

CREATE TABLE categories (
    id bigserial primary key,
    create_time timestamp default current_timestamp,
    update_time timestamp default current_timestamp,
    title VARCHAR(255)
);

INSERT INTO categories (title)
VALUES
('Category 1'),
('Category 2'),
('Category 3'),
('Category 4'),
('Category 5'),
('Category 6'),
('Category 7'),
('Category 8');

CREATE TABLE orders (
    id bigserial primary key,
    user_id    bigint,
    phone      varchar(50),
    address     varchar(250),
    create_time timestamp default current_timestamp,
    update_time timestamp default current_timestamp,
    total_price numeric(15,2)
);

CREATE TABLE order_items (
    id bigserial primary key,
    order_id bigint,
    product_id bigint,
    price numeric(15,2),
    quantity numeric,
    total_price numeric(15,2)
);

--USERS & ROLES
create table users
(
    id         bigserial primary key,
    username   varchar(30) not null,
    password   varchar(80) not null,
    email      varchar(50) unique,
    phone      varchar(50),
    address     varchar(250),
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

insert into users (username, password, email, phone, address)
values ('user', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'bob_johnson@gmail.com', '222-55-44', 'Moscow, Panfilova 4'),
       ('admin', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'john_johnson@gmail.com', '123-34-55', 'Rostov, Lenina 12' ),
       ('user2', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'nick_davidson@gmail.com', '324-53-34', 'Voronezh, Lizyukova 8');

create table roles
(
    id         bigserial primary key,
    name       varchar(50) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

insert into roles (name)
values ('ROLE_USER'),
       ('ROLE_ADMIN');

CREATE TABLE users_roles
(
    user_id bigint not null references users (id),
    role_id bigint not null references roles (id),
    primary key (user_id, role_id)
);

insert into users_roles (user_id, role_id)
values (1, 1),
       (2, 2),
       (3, 1);
