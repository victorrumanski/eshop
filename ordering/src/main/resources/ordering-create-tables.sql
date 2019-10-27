
DROP TABLE if exists public.orders cascade;

CREATE TABLE public.orders (
	id bigserial primary key,
	created timestamp not null default now(),
	userid bigint not NULL,
	addressid bigint not NULL ,
	cardid bigint not NULL ,
	status text not null
);



DROP TABLE if exists public.orderitem cascade;

CREATE TABLE public.orderitem (
	id bigserial primary key,
	productid bigint not NULL,
	orderid bigint not null,
	price numeric(19,2) not NULL,
	quantity numeric(19,2) default 1
);


DROP TABLE if exists public."event" cascade;

CREATE TABLE public."event" (
	id bigserial primary key,
	correlationid bigint NULL,
	created timestamp not NULL,
	payload text,
	"type" text not null 
);
