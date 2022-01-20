create user boardproject@localhost identified by '1234';

create database boardproject;

grant all privileges on boardproject.* to boardproject@localhost;