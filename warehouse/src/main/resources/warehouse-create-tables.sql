
DROP TABLE if exists public.stock cascade;

CREATE TABLE public.stock (
	id bigserial primary key,
	created timestamp not null default now(),
	productid bigint not NULL,
	total numeric(19,2) not NULL,
	reserved numeric(19,2) not NULL,
	available numeric(19,2) not NULL
);

DROP TABLE if exists public.reservation cascade;

CREATE TABLE public.reservation (
	id bigserial primary key,
	orderitemid bigint not NULL,
	stockid bigint not null,
	quantity numeric(19,2) not NULL,
	status text not null
);


DROP TABLE if exists public."event" cascade;

CREATE TABLE public."event" (
	id bigserial primary key,
	correlationid bigint NULL,
	created timestamp not NULL,
	payload text,
	"type" text not null 
);
