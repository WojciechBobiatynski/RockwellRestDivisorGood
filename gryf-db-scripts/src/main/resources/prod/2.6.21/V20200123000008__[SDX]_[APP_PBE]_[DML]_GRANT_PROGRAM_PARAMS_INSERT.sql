INSERT INTO APP_PBE.GRANT_PROGRAM_PARAMS (ID,
                                          GRANT_PROGRAM_ID,
                                          PARAM_ID,
                                          VALUE,
                                          DATE_FROM,
                                          DATE_TO)
     VALUES (eagle.GRANT_PROGRAM_PARAMS_SEQ.nextval
             ,             100
             ,             'OWN_CONT_A'
             ,             '13;15'
             ,             null
             ,             null
             );
             
INSERT INTO APP_PBE.GRANT_PROGRAM_PARAMS (ID,
                                          GRANT_PROGRAM_ID,
                                          PARAM_ID,
                                          VALUE,
                                          DATE_FROM,
                                          DATE_TO)
     VALUES (eagle.GRANT_PROGRAM_PARAMS_SEQ.nextval
             ,             101
             ,             'OWN_CONT_A'
             ,             '15'
             ,             null
             ,             null
             );
  
INSERT INTO APP_PBE.GRANT_PROGRAM_PARAMS (ID,
                                          GRANT_PROGRAM_ID,
                                          PARAM_ID,
                                          VALUE,
                                          DATE_FROM,
                                          DATE_TO)
     VALUES (eagle.GRANT_PROGRAM_PARAMS_SEQ.nextval
             ,             100
             ,             'OWN_CONT_I'
             ,             '13'
             ,             null
             ,             to_date('2020-04-14','yyyy-mm-dd')
             );
             
INSERT INTO APP_PBE.GRANT_PROGRAM_PARAMS (ID,
                                          GRANT_PROGRAM_ID,
                                          PARAM_ID,
                                          VALUE,
                                          DATE_FROM,
                                          DATE_TO)
     VALUES (eagle.GRANT_PROGRAM_PARAMS_SEQ.nextval
             ,             100
             ,             'OWN_CONT_I'
             ,             '15'
             ,             to_date('2020-01-02','yyyy-mm-dd')
             ,             null
             );           
             