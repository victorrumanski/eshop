
DROP TABLE if exists public.payment cascade;

CREATE TABLE public.payment (
	id bigserial primary key,
	created timestamp not null default now(),
	orderid bigint not NULL,
	cardid bigint not NULL ,
	status text not null,
	amount numeric(19,2) not null,
	lasttry timestamp,
 	paid timestamp,
	refunded timestamp,
	failed timestamp
);

DROP TABLE if exists public.refund cascade;

CREATE TABLE public.refund (
	id bigserial primary key,
	created timestamp not null default now(),
	paymentid bigint not NULL,
	refunded bool default false
);


DROP TABLE if exists public."event" cascade;

CREATE TABLE public."event" (
	id bigserial primary key,
	correlationid bigint NULL,
	created timestamp not NULL,
	payload text,
	"type" text not null 
);
