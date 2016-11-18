MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'PIN_SEND' ID ,'Witaj: {$individualName}

PIN: {$reimbursmentPin}
SZKOLENIE:{$trainingName}, {$trainingPlace}, {$trainingInstitutionName}, {$trainingStartDate}, liczba bonów: {$assignedProductNum}
Z powa�aniem
Zesp� ds. obs�ugi klienta' EMAIL_BODY_TEMPLATE, 'Pin do szkolenia' EMAIL_SUBJECT_TEMPLATE,
 'Przesłanie pinu do szkolenia' DESCRIPTION from dual) ins

ON (msg.ID = ins.ID) WHEN MATCHED THEN UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
 msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
msg.DESCRIPTION = ins.DESCRIPTION WHEN NOT MATCHED THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,
ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'PIN_RESEND' ID ,'Witaj: {$individualName}

PIN: {$reimbursmentPin}
SZKOLENIE:{$trainingName}, {$trainingPlace}, {$trainingInstitutionName}, {$trainingStartDate}, liczba bonów: {$assignedProductNum}
Z powa�aniem
Zesp� ds. obs�ugi klienta' EMAIL_BODY_TEMPLATE, 'Pin do szkolenia' EMAIL_SUBJECT_TEMPLATE,
 'Przesłanie pinu do szkolenia' DESCRIPTION from dual) ins

ON (msg.ID = ins.ID) WHEN MATCHED THEN UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
 msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
msg.DESCRIPTION = ins.DESCRIPTION WHEN NOT MATCHED THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,
ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);