MERGE INTO APP_PBE.GRANT_PROGRAM_PARAM_TYPES ug USING (
   SELECT
     'D_FOR_CORR' ID,
     'Dni na korekte' NAME,
     'Liczba dni na wykonanie korekty' DESCRIPRTION
   FROM dual
  ) ins
ON (ug.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET ug.NAME = ins.NAME, ug.DESCRIPRTION = ins.DESCRIPRTION
WHEN NOT MATCHED THEN INSERT
  (ID,NAME,DESCRIPRTION)
VALUES (ins.ID, ins.NAME, ins.DESCRIPRTION);

MERGE INTO APP_PBE.GRANT_PROGRAM_PARAMS ug USING (
   SELECT
     8 ID,
     100 GRANT_PROGRAM_ID,
     'D_FOR_CORR' PARAM_ID,
     '5' VALUE,
     null DATE_FROM,
     null DATE_TO
   FROM dual
  ) ins
ON (ug.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET ug.VALUE = ins.VALUE, ug.DATE_FROM = ins.DATE_FROM, ug.DATE_TO = ins.DATE_TO
WHEN NOT MATCHED THEN INSERT
  (ID,GRANT_PROGRAM_ID,PARAM_ID, VALUE, DATE_FROM, DATE_TO)
VALUES (ins.ID, ins.GRANT_PROGRAM_ID, ins.PARAM_ID, ins.VALUE, ins.DATE_FROM , ins.DATE_TO);

MERGE INTO APP_PBE.GRANT_PROGRAM_PARAM_TYPES ug USING (
   SELECT
     'D_FOR_REIM' ID,
     'Dni na rozliczenie' NAME,
     'Liczba dni na wykonanie rozliczenia' DESCRIPRTION
   FROM dual
  ) ins
ON (ug.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET ug.NAME = ins.NAME, ug.DESCRIPRTION = ins.DESCRIPRTION
WHEN NOT MATCHED THEN INSERT
  (ID,NAME,DESCRIPRTION)
VALUES (ins.ID, ins.NAME, ins.DESCRIPRTION);

MERGE INTO APP_PBE.GRANT_PROGRAM_PARAMS ug USING (
   SELECT
     9 ID,
     100 GRANT_PROGRAM_ID,
     'D_FOR_REIM' PARAM_ID,
     '10' VALUE,
     null DATE_FROM,
     null DATE_TO
   FROM dual
  ) ins
ON (ug.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET ug.VALUE = ins.VALUE, ug.DATE_FROM = ins.DATE_FROM, ug.DATE_TO = ins.DATE_TO
WHEN NOT MATCHED THEN INSERT
  (ID,GRANT_PROGRAM_ID,PARAM_ID, VALUE, DATE_FROM, DATE_TO)
VALUES (ins.ID, ins.GRANT_PROGRAM_ID, ins.PARAM_ID, ins.VALUE, ins.DATE_FROM , ins.DATE_TO);