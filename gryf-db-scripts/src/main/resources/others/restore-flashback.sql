shutdown immediate;
startup mount;
flashback database to restore point zero;
alter database open resetlogs;

drop restore point zero;
create restore point zero GUARANTEE FLASHBACK DATABASE;