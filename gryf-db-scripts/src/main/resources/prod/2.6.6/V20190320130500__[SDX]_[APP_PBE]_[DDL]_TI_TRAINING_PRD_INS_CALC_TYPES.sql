BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE(upper('${gryf.schema}'), 'SRV_EE', 'TI_TRAINING_PRD_INS_CALC_TYPES', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE(upper('${gryf.schema}'), 'EAGLE', 'TI_TRAINING_PRD_INS_CALC_TYPES', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE(upper('${gryf.schema}'), 'SRV_EE_IND', 'TI_TRAINING_PRD_INS_CALC_TYPES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE(upper('${gryf.schema}'), 'SRV_EE_TI', 'TI_TRAINING_PRD_INS_CALC_TYPES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE(upper('${gryf.schema}'), 'DEVELOPER', 'TI_TRAINING_PRD_INS_CALC_TYPES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE(upper('${gryf.schema}'), 'ISOLUTION', 'TI_TRAINING_PRD_INS_CALC_TYPES', 'SELECT'));
END;