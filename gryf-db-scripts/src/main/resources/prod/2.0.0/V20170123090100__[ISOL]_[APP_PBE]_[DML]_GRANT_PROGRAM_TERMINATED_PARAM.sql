MERGE INTO APP_PBE.GRANT_PROGRAM_PARAM_TYPES ug USING (
   SELECT
     'E_DAY_LIM' ID,
     'Liczba dni przeterminowania' NAME,
     'Liczba dni liczona od ko�ca szkolenia po kt�rej dane rozliczenie bon�w elektronicznych, je�li nie zosta�o rozliczone, uznawane jest za przeterminowane' DESCRIPRTION
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
     6 ID,
     100 GRANT_PROGRAM_ID,
     'E_DAY_LIM' PARAM_ID,
     '35' VALUE,
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