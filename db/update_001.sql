CREATE TABLE roles (
    id serial primary key,
    authority VARCHAR(50) NOT NULL unique
);

CREATE TABLE persons (
    id serial primary key,
    username VARCHAR(50) NOT NULL unique,
    login VARCHAR(50) NOT NULL unique,
    password VARCHAR(100) NOT NULL,
    enabled boolean default true,
    role_id int not null references roles(id)
);

CREATE TABLE rooms (
    id serial primary key,
    name VARCHAR(50) NOT NULL unique
);

create table room_person (
    id serial primary key,
    room_id INT REFERENCES rooms(id),
    person_id INT REFERENCES persons(id)
);

CREATE TABLE messages (
    id serial primary key,
    message TEXT NOT NULL,
    created timestamp without time zone not null default now(),
    author_id int not null references persons(id),
    room_id int not null references rooms(id)
);

insert into roles (authority) values ('ROLE_USER');
insert into roles (authority) values ('ROLE_ADMIN');

insert into persons (username, login, password, enabled, role_id)
values ('Admin', 'admin@shishka.ru', '123456', true,
        (select id from roles where authority = 'ROLE_ADMIN'));

insert into persons (username, login, password, enabled, role_id)
values ('Simple_User', 'user@shishka.ru', '123456', true,
        (select id from roles where authority = 'ROLE_USER'));

insert into rooms (name) values ('Job for Java Devs');

insert into room_person (id, room_id, person_id) values (1, 1, 1);
insert into room_person (id, room_id, person_id) values (2, 1, 2);

