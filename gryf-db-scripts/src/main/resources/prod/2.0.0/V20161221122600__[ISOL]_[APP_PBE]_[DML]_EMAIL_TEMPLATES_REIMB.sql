MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'E_REIMB' ID ,'Sz. P. {$firstName} {$lastName},
Uprzejmie informujemy, ¿e rozliczono bony w ramach programu {$grantProgramName} dotycz¹ce szkolenia {$trainingName}

W za³¹czniku znajdzie Pan/Pani notê uznaniow¹ o numerze: {$noteNo} rozliczaj¹c¹ wp³acony przez Pana/Pani¹ wk³ad w³asny.' EMAIL_BODY_TEMPLATE,
 'Rozliczenie bonów szkoleniowych w ramach programu dofinansowania: {$grantProgramName}' EMAIL_SUBJECT_TEMPLATE,
 'Informacja o rozliczeniu bonów szkoleniowych' DESCRIPTION
from dual) ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
  UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
    msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
    msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
  THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
    (ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'CNF_PYMT' ID ,'Sz. P. {$firstName} {$lastName},
Uprzejmie informujemy, ¿e rozliczono bony w ramach programu {$grantProgramName} dotycz¹ce szkolenia {$trainingName}

W za³¹czniku znajdzie Pan/Pani potwierdzenie wyp³aty nale¿noœci za szkolenie.' EMAIL_BODY_TEMPLATE,
 'Potwierdzenie wyp³aty nale¿noœci z tytu³u rozliczenia bonów szkoleniowych w ramach programu dofinansowania: {$grantProgramName}' EMAIL_SUBJECT_TEMPLATE,
 'Potwierdzenie wp³aty nale¿noœci' DESCRIPTION
from dual) ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
  UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
    msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
    msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
  THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
    (ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);