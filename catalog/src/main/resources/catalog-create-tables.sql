DROP TABLE if exists public."event" cascade;

CREATE TABLE public."event" (
	id bigserial primary key,
	correlationid bigint NULL,
	created timestamp not NULL,
	payload text,
	"type" text not null
);

DROP TABLE if exists public.product cascade;

CREATE TABLE public.product (
	id bigserial primary key,
	available bool NOT NULL,
	description text,
	"name" text,
	price numeric(19,2) NULL
);