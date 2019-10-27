

DROP TABLE if exists public.basketitem cascade;

CREATE TABLE public.basketitem (
	id bigserial primary key,
	price numeric(19,2) NULL,
	productid bigint not NULL,
	quantity numeric(19,2) default 1,
	userid bigint not NULL 
);


DROP TABLE if exists public."event" cascade;

CREATE TABLE public."event" (
	id bigserial primary key,
	correlationid bigint NULL,
	created timestamp not NULL,
	payload text,
	"type" text not null 
);

DROP TABLE if exists public.pricechangealert cascade;

CREATE TABLE public.pricechangealert (
	id bigserial primary key,
	created timestamp not NULL default now(),
	oldprice numeric(19,2) NULL,
	newprice numeric(19,2) NULL,
	basketitemid bigint not NULL
);
