create table FLODA_ustawienia_enum
(
  ID   int auto_increment
    primary key,
  TEST int null
);

create table and_forum
(
  ID       int auto_increment,
  Title    text                                not null,
  Score    int       default 0                 null,
  owner_id int                                 not null,
  category text                                null,
  Descr    text                                null,
  date     timestamp default CURRENT_TIMESTAMP null,
  constraint and_forum_ID_uindex
    unique (ID)
)
  comment 'forum posts on cardview';

alter table and_forum
  add primary key (ID);

create table and_forum_comment
(
  ID      int auto_increment,
  `Desc`  text                                null,
  user_id int                                 not null,
  id_post int                                 null,
  Date    timestamp default CURRENT_TIMESTAMP null,
  VISIBLE int       default 1                 null,
  constraint and_forum_comment_ID_uindex
    unique (ID)
);

alter table and_forum_comment
  add primary key (ID);

create table and_forum_raters
(
  ID         int auto_increment,
  ID_post    int not null,
  ID_user    int null,
  ID_comment int null,
  constraint and_forum_raters_ID_uindex
    unique (ID)
);

create index ID_comment
  on and_forum_raters (ID_comment);

alter table and_forum_raters
  add primary key (ID);

create table floda_codes
(
  ID     int  not null,
  Type   text null,
  active int  not null,
  Email  text null
);

create table floda_sonda_settings
(
  id_device          int                                    not null
    primary key,
  SSID_optional_wifi text                                   null comment 'SSID do awaryjnej sieci wifi',
  PASS_optional_wifi text                                   null comment 'Haslo do awaryjnej sieci wifi',
  Delay_time         bigint       default 0                 null comment 'Czas pomiedzy uploadem do serwera',
  acess_password     varchar(255) default ''                null comment 'Haslo do dodania sondy do swojego profilu',
  podlanie           timestamp                              null,
  is_on              tinyint(1)   default 0                 null,
  arg41              text                                   null,
  arg42              text                                   null,
  Date               timestamp    default CURRENT_TIMESTAMP null
)
  comment 'Ustawienia konkretnej sondy';

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

create trigger czy_podlany
  after INSERT
  on floda_log
  for each row
begin
  declare soil integer;
  set @soil := (select soil from floda_log where nr_floda = NEW.nr_floda order by id desc limit 1);
  if (@soil < NEW.soil - 20) then
    update floda_log set watered=NOW() where id = new.id;
  else
    update floda_log
    set watered=(select watered from floda_log where nr_floda = new.nr_floda order by id desc limit 1)
    where id = new.id;
  end if;

end;

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

create table FLODA_main_database
(
  ID        int auto_increment
    primary key,
  Nazwa     varchar(255)                        null comment 'nazwa/gatunek',
  s_d_s     int                                 null comment 'srednie dobowe naslonecznienie w luxach, (dodac podzial na zacienione itd)',
  a_w_g     int                                 null comment 'alert o zbyt niskej wilgotnosci gleby (ustawiany na podstawie jaka wilgotnosc gleby powinna byc do rosliny)(oparte o podzial podstawowych ustawien)',
  c_k_p     int                                 null comment 'druga opcja do podlewania czyli czas co jaki kwiat powinno sie podlac (dni)
',
  s_d_t     int                                 null comment 'srednia dzienna temperatura (odchył o -5C do +10C) ustawiane na podstawie wlasnych badz ustalonych perferencji',
  s_d_w     int                                 null comment 'srednia dzienna wilgotnosc ustalana na podstawie wlasnych ustawien albo wczesniej ustawionych odchył +-10%',
  id_autora int                                 null comment 'id_autora',
  www       varchar(250)                        null comment 'strona internetowa ze szczególowymi informacjami',
  kiedy     timestamp default CURRENT_TIMESTAMP null,
  constraint FLODA_main_database_Nazwa_uindex
    unique (Nazwa),
  constraint autor
    foreign key (id_autora) references floda_user_detail (ID)
)
  comment 'baza dbania o rosliny';

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

create table safeway_pl
(
  ID          int auto_increment
    primary key,
  roadname    text          null,
  sidewalk    text          null,
  road        text          null,
  limroad     text          null,
  addit       text          null,
  hour        int default 0 null,
  month       int default 1 null,
  day_of_week int default 1 null
);

create table safeway_roads
(
  ID     int auto_increment
    primary key,
  road   text not null,
  weight int  not null
);

create table usr_info
(
  usr_id      int auto_increment
    primary key,
  usr_name    tinytext charset utf8     not null,
  usr_surname tinytext charset utf8     not null,
  usr_country int                       not null,
  usr_email   varchar(200) charset utf8 not null,
  usr_teach   tinyint(1) default 0      null,
  usr_admin   tinyint(1) default 0      null,
  usr_team    tinytext charset utf8     not null,
  usr_descr   text                      null,
  usr_fb_page text                      null,
  constraint usr_email_UNIQUE
    unique (usr_email)
);

create trigger usr_info_AFTER_INSERT
  after INSERT
  on usr_info
  for each row
BEGIN
  DECLARE id integer;
  SET id := (select usr_id from usr_info order by usr_id desc limit 1);
  INSERT INTO usr_log(usr_id) value (id);
END;

create table usr_log
(
  usr_id       int        default 0                 not null
    primary key,
  usr_login    varchar(200)                         not null,
  usr_password varchar(200)                         not null,
  usr_active   tinyint(1) default 1                 null,
  usr_created  datetime   default CURRENT_TIMESTAMP null,
  constraint usr_login_UNIQUE
    unique (usr_login),
  constraint usr_id
    foreign key (usr_id) references usr_info (usr_id)
      on delete cascade
);

create table web_log
(
  id        int auto_increment
    primary key,
  pitolenie text not null,
  obrazek   text not null,
  tytul     text null
);

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
       `ger`.`watered`              AS `watered`
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

create function be() returns tinyint
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
  return 1;
end;

