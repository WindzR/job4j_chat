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