BEGIN
  -- Grant privileges
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE(upper('${eagle.schema}'), 'SRV_EE', 'ADM_USER_PARAMETERS', 'ALL'));

  -- Add relationship to between EAGLE.ADM_USERS and APP_PBE.TRAINING_INSTITUTION_USERS
  INSERT INTO EAGLE.ADM_USER_PARAMETER_CODES (CODE, NAME, ACTIVE, DESCRIPTION)
  VALUES ('TI_USER_ID', 'TI_USER_ID', 'Y', 'Training institution user');

  -- Add default Training Institution for FO users to which users can reset
  -- id is arbitrary, can be modified later
  INSERT INTO EAGLE.ADM_PARAMETERS (NAME, VALUE, DESCRIPTION)
  VALUES ('DEFAULT_TI', '201', 'Domyślna sztuczna Instytucja Szkoleniowa');
END;
/
