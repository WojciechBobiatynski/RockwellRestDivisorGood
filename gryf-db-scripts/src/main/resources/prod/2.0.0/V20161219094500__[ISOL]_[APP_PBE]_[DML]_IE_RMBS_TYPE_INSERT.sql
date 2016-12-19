MERGE INTO APP_PBE.E_REIMBURSEMENT_TYPES ug USING (
  SELECT
      'TI_INST' CODE,
      'Rozliczenie zarezerwowanego szkolenia (instancji szkolenia)' NAME,
      1 ORDINAL
  FROM dual
) ins
ON ( ug.CODE = ins.CODE)
WHEN MATCHED THEN
  UPDATE SET ug.NAME = ins.NAME, ug.ORDINAL = ins.ORDINAL
WHEN NOT MATCHED THEN INSERT
  (CODE, NAME, ORDINAL)
  VALUES (ins.CODE, ins.NAME, ins.ORDINAL);

MERGE INTO APP_PBE.E_REIMBURSEMENT_TYPES ug USING (
  SELECT
      'URSVD_POOL' CODE,
      'Rozliczenie niewykorzystanej puli bonów' NAME,
      2 ORDINAL
  FROM dual
) ins
ON ( ug.CODE = ins.CODE)
WHEN MATCHED THEN
  UPDATE SET ug.NAME = ins.NAME, ug.ORDINAL = ins.ORDINAL
WHEN NOT MATCHED THEN INSERT
  (CODE, NAME, ORDINAL)
  VALUES (ins.CODE, ins.NAME, ins.ORDINAL);