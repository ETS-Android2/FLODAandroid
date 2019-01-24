create table floda_codes
(
  ID     int  not null,
  Type   text null,
  active int  not null,
  Email  text null
);

-- auto-generated definition
create table FLODA_pre_settings
(
  CODE    varchar(5) null,
  val1    int        null,
  val2    int        null,
  comment text       null,
  constraint FLODA_pre_settings_CODE_uindex
    unique (CODE)
)
  comment 'predefinied settings for Floda main database';

-- auto-generated definition
create table floda_user_stats
(
  id          int(6) auto_increment,
  id_kto      int(6)                             not null,
  description text                               null,
  arg2        text                               null,
  arg3        text                               null,
  time        datetime default CURRENT_TIMESTAMP null,
  constraint id
    unique (id),
  constraint kto
    foreign key (id_kto) references floda_user_detail (ID)
)
  comment 'statystyki o uzytkownikach';

-- auto-generated definition
create table FLODA_connections
(
  ID           int auto_increment
    primary key,
  whose        int  null,
  ID_sondy     int  null,
  Name         text null,
  ID_from_base int  null,
  constraint baza
    foreign key (ID_from_base) references FLODA_main_database (ID),
  constraint sonda
    foreign key (ID_sondy) references floda_sonda_settings (id_device),
  constraint whose
    foreign key (whose) references floda_user_detail (ID)
)
  comment 'powiazania uzytkownika z informacjami z sond';

-- auto-generated definition
create table floda_sonda_settings
(
  id_device          int                                    not null
    primary key,
  SSID_optional_wifi text                                   null comment 'SSID do awaryjnej sieci wifi',
  PASS_optional_wifi text                                   null comment 'Haslo do awaryjnej sieci wifi',
  Delay_time         bigint       default 0                 null comment 'Czas pomiedzy uploadem do serwera',
  acess_password     varchar(255) default ''                null comment 'Haslo do dodania sondy do swojego profilu',
  is_on              tinyint(1)   default 0                 null,
  arg41              text                                   null,
  arg42              text                                   null,
  Date               timestamp    default CURRENT_TIMESTAMP null
)
  comment 'Ustawienia konkretnej sondy';

-- auto-generated definition
create table floda_log
(
  id          int(8) auto_increment
    primary key,
  nr_floda    int                                 null,
  temperature text                                null,
  soil        text                                null,
  ph          text                                null,
  humidity    text                                null,
  sun         text                                null,
  IP          text                                null,
  haslo       text                                null,
  date        timestamp default CURRENT_TIMESTAMP null,
  watered     timestamp                           null,
  constraint nr_floda
    foreign key (nr_floda) references floda_sonda_settings (id_device)
)
  comment 'informacje zbierane z sondy';

-- auto-generated definition
create table floda_user_detail
(
  ID        int auto_increment,
  Nick      varchar(50)          null,
  Name      text                 null,
  Surname   text                 null,
  Email     varchar(30)          null,
  passwd    text                 null,
  blocked   tinyint(1) default 0 null,
  su        tinyint(1) default 0 null,
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

-- auto-generated definition
create table FLODA_main_database
(
  ID        int auto_increment
    primary key,
  Nazwa     varchar(255)                         null comment 'nazwa/gatunek',
  s_d_s     int        default 0                 null comment 'srednie dobowe naslonecznienie w luxach, (dodac podzial na zacienione itd)',
  s_d_s_x   int        default 0                 null,
  a_w_g     int        default 0                 null comment 'alert o zbyt niskej wilgotnosci gleby (ustawiany na podstawie jaka wilgotnosc gleby powinna byc do rosliny)(oparte o podzial podstawowych ustawien) ',
  a_w_g_x   int        default 0                 null,
  c_k_p     int        default 0                 null comment 'druga opcja do podlewania czyli czas co jaki kwiat powinno sie podlac (dni)
',
  s_d_t     int        default 0                 null comment 'srednia dzienna min temperatura ',
  s_d_t_x   int        default 0                 null comment 'max dzienna temp',
  s_d_w     int        default 0                 null comment 'srednia dzienna wilgotnosc ustalana na podstawie wlasnych ustawien albo wczesniej ustawionych',
  s_d_w_x   int        default 0                 null,
  www       varchar(250)                         null comment 'strona internetowa ze szczególowymi informacjami',
  id_autora int                                  null comment 'id_autora',
  kiedy     timestamp  default CURRENT_TIMESTAMP null,
  display   tinyint(1) default 1                 null,
  constraint FLODA_main_database_Nazwa_uindex
    unique (Nazwa),
  constraint autor
    foreign key (id_autora) references floda_user_detail (ID)
)
  comment 'baza dbania o rosliny';

create definer = root@`%` event be on schedule
  every '3' HOUR
    starts '2019-01-08 00:23:13'
  enable
  do
  begin
  CREATE temporary table csd as
  select d.id_device,current_timestamp - c.date < d.Delay_time as ison
  from floda_sonda_settings d
         left join floda_log c on c.nr_floda = d.id_device
  group by d.id_device
  order by c.date desc;
  update floda_sonda_settings s left join csd d on d.id_device = s.id_device
  set s.is_on=IF(isnull(d.ison), 0, d.ison);
  drop temporary table csd;

end;

create view dataget as
select `s`.`ID`                     AS `ID`,
       `s`.`whose`                  AS `whose`,
       `s`.`ID_sondy`               AS `ID_sondy`,
       `s`.`Name`                   AS `Name`,
       `database2`.`Nazwa`          AS `Nazwa`,
       `database2`.`s_d_s`          AS `s_d_s`,
       `database2`.`a_w_g`          AS `a_w_g`,
       `database2`.`c_k_p`          AS `c_k_p`,
       `database2`.`s_d_t`          AS `s_d_t`,
       `database2`.`s_d_w`          AS `s_d_w`,
       `database2`.`www`            AS `www`,
       concat(monthname(`ger`.`date`), ' ', dayofmonth(`ger`.`date`), ' ', hour(`ger`.`date`), ':',
              minute(`ger`.`date`)) AS `time`,
       `ger`.`temperature`          AS `temperature`,
       `ger`.`soil`                 AS `soil`,
       `ger`.`humidity`             AS `humidity`,
       `ger`.`sun`                  AS `sun`,
       `ger`.`date`                 AS `date`,
       `ger`.`watered`              AS `watered`,
       `database2`.`s_d_t_x`        AS `s_d_t_x`,
       `database2`.`a_w_g_x`        AS `a_w_g_x`,
       `database2`.`s_d_w_x`        AS `s_d_w_x`,
       `database2`.`s_d_s_x`        AS `s_d_s_x`
from ((`24939152_0000008`.`FLODA_connections` `s` left join `24939152_0000008`.`FLODA_main_database` `database2` on ((`s`.`ID_from_base` = `database2`.`ID`)))
       left join `24939152_0000008`.`floda_log` `ger` on ((`ger`.`nr_floda` = `s`.`ID_sondy`)))
order by `database2`.`kiedy`;

create view srednia_dniowa as
select `a`.`nr_floda`                   AS `nr_floda`,
       round(avg(`a`.`temperature`), 2) AS `temp`,
       round(avg(`a`.`sun`), 2)         AS `sun`,
       round(avg(`a`.`soil`), 2)        AS `soil`,
       round(avg(`a`.`humidity`), 2)    AS `humidity`,
       monthname(`a`.`date`)            AS `month`,
       dayofmonth(`a`.`date`)           AS `day`,
       `a`.`date`                       AS `date`
from `24939152_0000008`.`floda_log` `a`
group by monthname(`a`.`date`),dayofmonth(`a`.`date`),`a`.`nr_floda`;

