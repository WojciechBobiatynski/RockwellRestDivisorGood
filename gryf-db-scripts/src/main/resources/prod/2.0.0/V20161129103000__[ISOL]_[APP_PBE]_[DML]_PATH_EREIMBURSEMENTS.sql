MERGE INTO EAGLE.ADM_PARAMETERS ug USING (
  SELECT
    'GRYF_PATH_EREIMBURSEMENTS' NAME,
    'ereimbursements/dev/' VALUE,
    'Podfolder do zapisu plików/raportów/za³¹czników dla e-rozliczeñ w ramach WUP (Gryf)' DESCRIPTION
  FROM dual
) ins
ON (ug.NAME = ins.NAME)
WHEN MATCHED THEN
  UPDATE SET ug.VALUE = ins.VALUE, ug.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED THEN INSERT
  (NAME,VALUE,DESCRIPTION)
  VALUES (ins.NAME, ins.VALUE, ins.DESCRIPTION);