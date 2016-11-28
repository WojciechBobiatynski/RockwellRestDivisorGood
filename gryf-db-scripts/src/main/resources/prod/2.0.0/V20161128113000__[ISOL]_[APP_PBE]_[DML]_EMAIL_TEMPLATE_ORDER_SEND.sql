MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'KK_ORDER' ID ,'Witaj: {$individualName}

IMIE: {$individualName},
NAZWISKO: {$individualLastName},
LICZBA BON�W: {$productInstanceNum},
DATA WA�NO�CI: {$expiryDate},
URL APLIKACJI: {$indUserUrl}
LOGIN: {$indLogin}
HAS�O: {$indPassword}
PROGRAM DOFINANSOWANIA: {$grantProgramName},
DATA PODPISANIA UMOWY: {$signDate},
NUMER NOTY OBCIA�ENIOWO-KSIEGOWEJ: {$noteNumber},

Z powa�aniem
Zesp� ds. obs�ugi klienta' EMAIL_BODY_TEMPLATE, 'Zrealizowano zam�wienie na bony elektroniczne' EMAIL_SUBJECT_TEMPLATE,
 'Przes�anie informacji o zrealizowaniu szkolenia na bony elektroniczne' DESCRIPTION from dual) ins

ON (msg.ID = ins.ID) WHEN MATCHED THEN UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
 msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
msg.DESCRIPTION = ins.DESCRIPTION WHEN NOT MATCHED THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,
ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);
