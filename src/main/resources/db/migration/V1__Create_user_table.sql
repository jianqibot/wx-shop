create table USER
(
    ID  bigint primary key auto_increment,
    NAME varchar(100),
    TEL  varchar(20) unique,
    AVATAR_URL varchar(300),
    CREATED_AT timestamp,
    MODIFIED_AT timestamp
)