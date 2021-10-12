
set schema public;

drop table IF EXISTS FIBONACCI;

create table FIBONACCI (
    number INT PRIMARY KEY NOT NULL,
    fibonacci INT NOT NULL
);

