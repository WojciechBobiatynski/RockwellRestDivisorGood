MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'E_REIMB' ID ,'Sz. P. {$firstName} {$lastName},
Uprzejmie informujemy, �e rozliczono bony w ramach programu {$grantProgramName} dotycz�ce szkolenia {$trainingName}

W za��czniku znajdzie Pan/Pani not� uznaniow� o numerze: {$noteNo} rozliczaj�c� wp�acony przez Pana/Pani� wk�ad w�asny.' EMAIL_BODY_TEMPLATE,
 'Rozliczenie bon�w szkoleniowych w ramach programu dofinansowania: {$grantProgramName}' EMAIL_SUBJECT_TEMPLATE,
 'Informacja o rozliczeniu bon�w szkoleniowych' DESCRIPTION
from dual) ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
  UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
    msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
    msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
  THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
    (ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'CNF_PYMT' ID ,'Sz. P. {$firstName} {$lastName},
Uprzejmie informujemy, �e rozliczono bony w ramach programu {$grantProgramName} dotycz�ce szkolenia {$trainingName}

W za��czniku znajdzie Pan/Pani potwierdzenie wyp�aty nale�no�ci za szkolenie.' EMAIL_BODY_TEMPLATE,
 'Potwierdzenie wyp�aty nale�no�ci z tytu�u rozliczenia bon�w szkoleniowych w ramach programu dofinansowania: {$grantProgramName}' EMAIL_SUBJECT_TEMPLATE,
 'Potwierdzenie wp�aty nale�no�ci' DESCRIPTION
from dual) ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
  UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
    msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
    msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
  THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
    (ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);