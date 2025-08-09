create database Repertorio;
use Repertorio;

create table fingerstyle(
id int auto_increment primary key,
fingerstyle varchar(2)
);

insert into fingerstyle (fingerstyle) values ("Si");
insert into fingerstyle (fingerstyle) values ("No");
select * from fingerstyle;

create table Canciones(
id int auto_increment primary key,
nombre varchar(100),
artista varchar(100),
album varchar(100),
año date,
fkfingerstyle int,
foto longblob,

foreign key(fkfingerstyle) references fingerstyle(id) on delete cascade on update cascade
);

drop table Canciones;
