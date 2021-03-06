MERGE INTO EAGLE.ADM_PARAMETERS ug USING (
  SELECT
    'GRYF_PATH_ACCOUNTING_DOCUMENT' NAME,
    'accounting_documents/dev/' VALUE,
    'Podfolder do zapisu plik?w/raport?w/za??cznik?w dla e-rozlicze? w ramach WUP (Gryf)' DESCRIPTION
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
                                             'GRYF_CDN_URL' NAME,
                                             '//cdn.sodexo.pl/gryf/' VALUE,
                                             'Url do CDN' DESCRIPTION
                                           FROM dual
                                         ) ins
ON (ug.NAME = ins.NAME)
WHEN MATCHED THEN
UPDATE SET ug.VALUE = ins.VALUE, ug.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED THEN INSERT
  (NAME,VALUE,DESCRIPTION)
VALUES (ins.NAME, ins.VALUE, ins.DESCRIPTION);



