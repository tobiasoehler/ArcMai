create table AM_OPTION (
    AM_ID VARCHAR PRIMARY KEY,
    AM_VALUE VARCHAR
);

create table IMAP_SETTING (
    ID UUID default random_uuid() PRIMARY KEY,
    USERNAME varchar,
    PASSWORD varchar,
    HOST varchar,
    PORT integer
);

insert into AM_OPTION (AM_ID, AM_VALUE) VALUES ('eml_filename', '{date}-{subject}-{messageId}');
insert into AM_OPTION (AM_ID, AM_VALUE) VALUES ('sync_interval_in_minutes', '60');