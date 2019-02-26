create table FLODA_pre_settings
(
	CODE varchar(5) null,
	val1 int null,
	val2 int null,
	comment text null,
	constraint FLODA_pre_settings_CODE_uindex
		unique (CODE)
)
comment 'predefinied settings for Floda main database';

create table floda_codes
(
	ID int not null,
	Type text null,
	active int not null,
	Email text null
);

create table floda_forum
(
	ID int auto_increment,
	Title text not null,
	Score int default 0 null,
	Description text null,
	owner_id int not null,
	category text null,
	blocked tinyint default 0 null,
	date timestamp default CURRENT_TIMESTAMP null,
	constraint and_forum_ID_uindex
		unique (ID)
)
comment 'forum dla uzytjiwbujiw';

alter table floda_forum
	add primary key (ID);

create table floda_forum_comments
(
	ID int auto_increment,
	text text null,
	user_id int not null,
	id_post int null,
	time timestamp default CURRENT_TIMESTAMP null,
	blocked int default 0 null,
	constraint floda_forum_comments_id_index
		unique (ID)
);

alter table floda_forum_comments
	add primary key (ID);

create table floda_forum_rating
(
	ID int auto_increment,
	ID_post int not null,
	ID_user int null,
	ID_comment int null,
	constraint floda_forum_rating_index
		unique (ID)
);

create index ID_comment
	on floda_forum_rating (ID_comment);

alter table floda_forum_rating
	add primary key (ID);

create table floda_sonda_settings
(
	id_device int not null
		primary key,
	SSID_optional_wifi text null comment 'SSID do awaryjnej sieci wifi',
	PASS_optional_wifi text null comment 'Haslo do awaryjnej sieci wifi',
	Delay_time bigint default 0 null comment 'Czas pomiedzy uploadem do serwera w sekundach',
	acess_password varchar(255) default '' null comment 'Haslo do dodania sondy do swojego profilu',
	is_on tinyint(1) default 0 null,
	arg41 text null,
	arg42 text null,
	Date timestamp default CURRENT_TIMESTAMP null
)
comment 'Ustawienia konkretnej sondy';

create table floda_log
(
	id int(8) auto_increment
		primary key,
	nr_floda int null,
	temperature text null,
	soil text null,
	ph text null,
	humidity text null,
	sun text null,
	IP text null,
	haslo text null,
	date timestamp default CURRENT_TIMESTAMP null,
	watered timestamp null,
	constraint `floda device`
		foreign key (nr_floda) references floda_sonda_settings (id_device)
)
comment 'informacje zbierane z sondy';

create table floda_user_detail
(
	ID int auto_increment,
	Nick varchar(50) null,
	Name text null,
	Surname text null,
	Email varchar(30) null,
	passwd text null,
	blocked tinyint(1) default 0 null,
	su tinyint(1) default 0 null,
	activated tinyint(1) default 0 null,
	constraint floda_user_detail_Email_uindex
		unique (Email),
	constraint floda_user_detail_ID_uindex
		unique (ID),
	constraint floda_user_detail_Nick_uindex
		unique (Nick)
)
comment 'informacje na temat uzytkownikow';

alter table floda_user_detail
	add primary key (ID);

create table FLODA_main_database
(
	ID int auto_increment
		primary key,
	Nazwa varchar(255) null comment 'nazwa/gatunek',
	s_d_s int default 0 null comment 'srednie dobowe naslonecznienie w luxach, (dodac podzial na zacienione itd)',
	s_d_s_x int default 0 null,
	a_w_g int default 0 null comment 'alert o zbyt niskej wilgotnosci gleby (ustawiany na podstawie jaka wilgotnosc gleby powinna byc do rosliny)(oparte o podzial podstawowych ustawien)',
	a_w_g_x int default 0 null,
	c_k_p int default 0 null comment 'druga opcja do podlewania czyli czas co jaki kwiat powinno sie podlac (dni)
',
	s_d_t int default 0 null comment 'srednia dzienna min temperatura',
	s_d_t_x int default 0 null comment 'max dzienna temp',
	s_d_w int default 0 null comment 'srednia dzienna wilgotnosc ustalana na podstawie wlasnych ustawien albo wczesniej ustawionych',
	s_d_w_x int default 0 null,
	www varchar(250) null comment 'strona internetowa ze szczególowymi informacjami',
	id_autora int null comment 'id_autora',
	kiedy timestamp default CURRENT_TIMESTAMP null,
	display tinyint(1) default 1 null,
	constraint FLODA_main_database_Nazwa_uindex
		unique (Nazwa),
	constraint autor
		foreign key (id_autora) references floda_user_detail (ID)
)
comment 'baza dbania o rosliny';

create table FLODA_connections
(
	ID int auto_increment
		primary key,
	whose int null,
	ID_sondy int null,
	Name text null,
	ID_from_base int null,
	constraint baza
		foreign key (ID_from_base) references FLODA_main_database (ID),
	constraint sonda
		foreign key (ID_sondy) references floda_sonda_settings (id_device),
	constraint whose
		foreign key (whose) references floda_user_detail (ID)
)
comment 'powiazania uzytkownika z informacjami z sond';

create table floda_user_stats
(
	id int(6) auto_increment,
	id_kto int(6) not null,
	lang text null,
	default_server text null,
	time datetime default CURRENT_TIMESTAMP null,
	constraint id
		unique (id),
	constraint kto
		foreign key (id_kto) references floda_user_detail (ID)
)
comment 'statystyki o uzytkownikach';

