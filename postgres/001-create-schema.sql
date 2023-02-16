create type permission_type as enum ('ALL', 'FRIENDS');
create type action_type as enum ('BLOCK', 'UNBLOCK');
create type code_type as enum ('REQUEST', 'FRIEND', 'BLOCKED', 'DECLINED', 'SUBSCRIBED');
create type read_status_type as enum ('SENT', 'READ');
create type notification_code_type as enum ('POST', 'POST_COMMENT', 'COMMENT_COMMENT', 'FRIEND_REQUEST', 'MESSAGE'
, 'FRIEND_BIRTHDAY');

create table if not exists person (
    id serial,
    first_name varchar(50),
    last_name varchar(50),
    reg_date bigint,
    birth_date bigint,
    e_mail varchar(50),
    phone varchar(15),
    password varchar(512),
    photo varchar(255),
    about text,
    city varchar(100),
    country varchar(100),
    confirmation_code varchar(20),
    is_approved boolean,
    messages_permission permission_type,
    last_online_time bigint,
    is_blocked boolean,
    is_deleted boolean,
    primary key (id)
);

create table if not exists deleted_users(
  id serial,
  person_id int4,
  photo varchar(255),
  first_name varchar(50),
  last_name varchar(50),
  expire bigint
);

create table if not exists block_history (
    id serial,
    time bigint,
    person_id int4,
    post_id int4,
    comment_id int4,
    action action_type,
    primary key (id)
);

create table if not exists friendship_status (
    id serial,
    time bigint,
    name varchar(50),
    code code_type,
    primary key (id)
);

create table if not exists friendship (
    id serial,
    time bigint,
    code code_type,
	status_id int4,
    src_person_id int4,
    dst_person_id int4,
    CONSTRAINT fk_friendShipStatus FOREIGN KEY (status_id) REFERENCES friendship_status (id) ON DELETE CASCADE,
    CONSTRAINT fk_personSrc FOREIGN KEY (src_person_id) REFERENCES person (id) ON DELETE CASCADE,
    CONSTRAINT fk_personDst FOREIGN KEY (dst_person_id) REFERENCES person (id) ON DELETE CASCADE,
    primary key (id)
);

create table if not exists message (
    id serial,
    time bigint,
    author_id int4,
    recipient_id int4,
    dialog_id int4,
    message_text text,
    read_status read_status_type,
    primary key (id)
);

create table if not exists dialog (
    id serial,
    author_id int4,
    recipient_id int4,
    dialog_id int4,
    primary key (id)
);

create table if not exists post (
    id serial,
    time bigint,
    author int4,
    title varchar(100),
    post_text text,
    is_blocked boolean,
    likes int4,
    primary key (id)
);

create table if not exists tag (
    id serial,
    tag varchar(15),
    primary key (id),
    CONSTRAINT tag_unique UNIQUE (tag)
);

create table if not exists post2tag (
    id serial,
    post_id int4,
    tag_id int4,
    primary key (id)
);

create table if not exists post_like (
    id serial,
    time bigint,
    person_id int4,
    post_id int4,
    primary key (id)
);

create table if not exists post_file (
    id serial,
    post_id int4,
    name varchar(255),
    path varchar(255),
    primary key (id)
);

create table if not exists post_comment (
    id serial,
    time bigint,
    post_id int4,
    parent_id int4,
    author_id int4,
    comment_text text,
    is_blocked boolean,
    likes int4,
    primary key (id)
);

create table if not exists comment_like (
    id serial,
    time bigint,
    person_id int4,
    comment_id int4,
    primary key (id)
);

create table if not exists notification (
    id serial,
    notification_type notification_code_type,
    sent_time bigint,
    person_id int4,
    entity_id int4,
    dist_user_id int4,
    status read_status_type,
    title varchar(50),
    primary key (id)
);

create table  if not exists notification_settings (
    id serial,
	person_id int4,
    type notification_code_type,
    enable boolean,
    primary key (id)
);

create table if not exists temptoken (
    id serial,
    email varchar(255),
    token varchar(255),
    primary key (id)
);