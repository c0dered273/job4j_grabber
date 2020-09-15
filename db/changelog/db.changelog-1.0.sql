create table Post
(
    id   serial primary key,
    name varchar(2048),
    description text,
    link varchar(2048) UNIQUE,
    created timestamp
);

