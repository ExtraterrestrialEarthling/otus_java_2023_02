drop table if exists test;

create table test
(
    id   int,
    name varchar(50)
);

drop table if exists client;

create table client
(
    id   bigserial not null primary key,
    name varchar(100)
);

drop table if exists manager;

create table manager
(
    no bigserial not null primary key,
    label varchar(100),
    param1 varchar(100)
);

