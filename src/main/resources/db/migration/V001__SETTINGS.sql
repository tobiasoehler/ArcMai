create table AM_OPTION (
    AM_ID VARCHAR PRIMARY KEY,
    AM_VALUE VARCHAR
);

insert into AM_OPTION (AM_ID, AM_VALUE) VALUES ('eml_filename', '{date}-{subject}-{messageId}');
insert into AM_OPTION (AM_ID, AM_VALUE) VALUES ('sync_interval_in_minutes', '60');