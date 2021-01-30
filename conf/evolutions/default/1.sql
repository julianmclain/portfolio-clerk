-- noinspection SqlNoDataSourceInspectionForFile

# --- !Ups

create table "photos" (
    "id" bigint,
    "title" varchar not null,
    "caption" varchar,
    "image_url" varchar not null,
    "album_id" varchar not null,
    "created_by" varchar not null
);

# --- !Downs

drop table photos;
