DROP TABLE if exists  public."event" cascade;

CREATE TABLE public."event" (
	id bigserial primary key,
	correlationid bigint NULL,
	created timestamp not NULL,
	payload text,
	"type" text not null 
);

DROP TABLE if exists  public.users cascade;

CREATE TABLE public.users (
	id bigserial primary key,
	email text not null,
	joined timestamp not null,
	"name" text not null,
	"password" text not null
);

DROP TABLE if exists  public.card cascade; 

CREATE TABLE public.card (
	id bigserial primary key,
	"name" text not null,
	"number" text not null,
	removed bool NOT NULL,
	userid bigint not NULL,
	validuntil timestamp not NULL,
	verificationcode text not null 
);


DROP TABLE if exists  public.address cascade; 

CREATE TABLE public.address (
	id bigserial primary key,
	city text,
	postalcode text,
	removed bool NOT NULL,
	state text,
	street text,
	userid bigint not NULL 
);
