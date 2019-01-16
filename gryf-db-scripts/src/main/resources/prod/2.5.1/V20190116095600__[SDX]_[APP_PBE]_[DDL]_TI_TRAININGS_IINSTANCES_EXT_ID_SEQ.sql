
CREATE SEQUENCE ${eagle.schema}.TI_TRA_INS_EXT_SEQ
  START WITH 1100000
  MAXVALUE 9999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;

declare
  v_x varchar2(100);
begin
   v_x := SYS.PK_ADMIN.F_GRANT_TO_EAGLE('${eagle.schema}', 'SRV_EE', 'TI_TRA_INS_EXT_SEQ', 'SELECT');
   dbms_output.put_line(v_x);
end;
