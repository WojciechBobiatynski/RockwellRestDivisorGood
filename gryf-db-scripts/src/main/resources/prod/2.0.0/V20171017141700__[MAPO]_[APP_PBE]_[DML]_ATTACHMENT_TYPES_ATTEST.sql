SET DEFINE OFF;
MERGE INTO APP_PBE.ATTACHMENT_TYPES A USING
 (SELECT
  'ATTEST' as CODE,
  'Za�wiadczenie o uko�czeniu szkolenia' as NAME,
  2 as ORDINAL,
  4194304 as MAX_BYTES_PER_FILE,
  NULL as MAX_FILES_PER_TYPE
  FROM DUAL) B
ON (A.CODE = B.CODE)
WHEN NOT MATCHED THEN 
INSERT (
  CODE, NAME, ORDINAL, MAX_BYTES_PER_FILE, MAX_FILES_PER_TYPE)
VALUES (
  B.CODE, B.NAME, B.ORDINAL, B.MAX_BYTES_PER_FILE, B.MAX_FILES_PER_TYPE)
WHEN MATCHED THEN
UPDATE SET 
  A.NAME = B.NAME;
  A.NAME = B.NAME;

COMMIT;