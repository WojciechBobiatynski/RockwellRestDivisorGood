shutdown immediate;
startup mount;
flashback database to restore point zero;
alter database open resetlogs;