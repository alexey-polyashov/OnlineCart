
CREATE TABLE products (
    id bigserial primary key,
    price numeric(10,2) not null,
    create_time timestamp default current_timestamp,
    update_time timestamp default current_timestamp,
    title VARCHAR(255),
    category_id bigint
);

CREATE TABLE categories (
    id bigserial primary key,
    create_time timestamp default current_timestamp,
    update_time timestamp default current_timestamp,
    title VARCHAR(255)
);

CREATE TABLE orders (
    id bigserial primary key,
    user_id    bigint,
    phone      varchar(50),
    address_postcode     varchar(60),
    address_countrycode     varchar(2),
    address_line1     varchar(300),
    address_line2     varchar(300),
    address_area1     varchar(300),
    address_area2     varchar(120),
    create_time timestamp default current_timestamp,
    update_time timestamp default current_timestamp,
    total_price numeric(15,2),
    status      int
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

create table roles
(
    id         bigserial primary key,
    name       varchar(50) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

CREATE TABLE users_roles
(
    user_id bigint not null references users (id),
    role_id bigint not null references roles (id),
    primary key (user_id, role_id)
);
