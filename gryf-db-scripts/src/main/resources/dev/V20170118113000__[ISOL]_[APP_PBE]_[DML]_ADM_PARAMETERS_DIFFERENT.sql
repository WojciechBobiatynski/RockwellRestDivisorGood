
MERGE INTO EAGLE.ADM_PARAMETERS ug USING (
                                           SELECT
                                             'GRYF_TI_USER_URL' NAME,
                                             'http://10.48.0.38:28080/gryf-ti-web/' VALUE,
                                             'URL aplikacji TI' DESCRIPTION
                                           FROM dual
                                         ) ins
ON (ug.NAME = ins.NAME)
WHEN MATCHED THEN
UPDATE SET ug.VALUE = ins.VALUE, ug.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED THEN INSERT
  (NAME,VALUE,DESCRIPTION)
VALUES (ins.NAME, ins.VALUE, ins.DESCRIPTION);



MERGE INTO EAGLE.ADM_PARAMETERS ug USING (
                                           SELECT
                                             'GRYF_IND_USER_URL' NAME,
                                             'http://10.48.0.38:28080/gryf-ind-web/' VALUE,
                                             'URL aplikacji IND' DESCRIPTION
                                           FROM dual
                                         ) ins
ON (ug.NAME = ins.NAME)
WHEN MATCHED THEN
UPDATE SET ug.VALUE = ins.VALUE, ug.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED THEN INSERT
  (NAME,VALUE,DESCRIPTION)
VALUES (ins.NAME, ins.VALUE, ins.DESCRIPTION);