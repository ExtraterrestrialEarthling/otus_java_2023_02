-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence client_SEQ start with 1 increment by 1;
create sequence address_SEQ start with 1 increment by 1;
create sequence phone_SEQ start with 1 increment by 1;

create table clients
(
    id   bigint not null primary key,
    name varchar(50)
);

create table addresses
(
    id   bigint not null primary key,
    street varchar(50)
);

create table phones
(
    id   bigint not null primary key,
    phone_number varchar(50)
);
