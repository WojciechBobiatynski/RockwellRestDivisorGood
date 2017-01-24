
--------------------------------------------------------------------------------------
-----------------------------------RESET_LINK-----------------------------------------
--------------------Powiadomienie o wygenerowaniu linku resetuj¹cego has³o------------
--------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'RESET_LINK' ID ,'Link resetuj¹cy has³o: {$resetLink}

Z powa¿aniem
Zespó³ ds. obs³ugi klienta' EMAIL_BODY_TEMPLATE, 'Reset has³a' EMAIL_SUBJECT_TEMPLATE, 'Powiadomienie o wygenerowaniu linku resetuj¹cego has³o' DESCRIPTION from dual)
ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
  msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
  msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);



--------------------------------------------------------------------------------------
-----------------------------------VC_SEND--------------------------------------------
-----------------------Dane do logowania dla osoby fizycznej--------------------------
--------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'VC_SEND' ID ,'Szanowny Panie/Szanowna Pani
Poni¿ej znajduj¹ siê dane logowania do naszego serwisu

Adres: {$url}
Login: {$login}
Has³o: {$verificationCode}

Z powa¿aniem
Zespó³ ds. obs³ugi klienta' EMAIL_BODY_TEMPLATE, 'Dane do logowania' EMAIL_SUBJECT_TEMPLATE, 'Dane do logowania dla osoby fizycznej' DESCRIPTION from dual)
ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
  msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
  msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);



--------------------------------------------------------------------------------------
-----------------------------------PIN_SEND-------------------------------------------
-------------------------Przes³anie pinu do szkolenia---------------------------------
-----------------3.7	Email do uczestnika z numerem PIN do szkolenia------------------
--------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'PIN_SEND' ID ,'Sz. P. {$firstName} {$lastName},

Uprzejmie informujemy, i¿ zarezerwowano  SZKOLENIE:{$trainingName}, {$trainingPlace}, {$trainingInstitutionName}, {$trainingStartDate},
liczba zarezerwowanych bonów: {$assignedProductNum}.

PoniŸej znajduje siê kod PIN, potwierdzaj¹cy uczestnictwo w szkoleniu, który nale¿y przekazaæ Instytucji Szkoleniowej na koniec wskazanego szkolenia.
PIN: {$reimbursmentPin}

Z powa¿aniem
Zespó³ ds. obs³ugi klienta' EMAIL_BODY_TEMPLATE, 'Zarezerwowano szkolenie w ramach programu dofinansowania: {$grantProgramName}' EMAIL_SUBJECT_TEMPLATE,
                                                     'Przes³anie pinu do szkolenia' DESCRIPTION from dual)
ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
  msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
  msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);

--------------------------------------------------------------------------------------
-----------------------------------PIN_RESEND-----------------------------------------
----------------------------Przes³anie pinu do szkolenia------------------------------
--------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'PIN_RESEND' ID ,'Sz. P. {$firstName} {$lastName},

Uprzejmie informujemy, i¿ zarezerwowano  SZKOLENIE:{$trainingName}, {$trainingPlace}, {$trainingInstitutionName}, {$trainingStartDate},
liczba zarezerwowanych bonów: {$assignedProductNum}.

Poni¿ej znajduje siê kod PIN, potwierdzaj¹cy uczestnictwo w szkoleniu, który nale¿y przekazaæ Instytucji Szkoleniowej na koniec wskazanego szkolenia.
PIN: {$reimbursmentPin}

Z powa¿aniem
Zespó³ ds. obs³ugi klienta' EMAIL_BODY_TEMPLATE, 'Zarezerwowano szkolenie w ramach programu dofinansowania: {$grantProgramName}' EMAIL_SUBJECT_TEMPLATE,
                                                     'Ponowne przes³anie pinu do szkolenia' DESCRIPTION from dual)
ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
  msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
  msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);

--------------------------------------------------------------------------------------
-----------------------------------KK_ORDER-------------------------------------------
---------Przes³anie informacji o zrealizowaniu szkolenia na bony elektroniczne--------
--------------------3.2	Email do OsFiz/MŒP o przydzieleniu bonów----------------------
--------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'KK_ORDER' ID ,'Sz. P. {$firstName} {$lastName},

Uprzejmie informujemy, ¿e przydzielono dofinansowanie w ramach programu {$grantProgramName} w wysokoœci {$grantedVouchersNumber} elektronicznych bonów szkoleniowych.

Stan przydzielonej puli bonów oraz zapisy na szkolenia mo¿na œledziæ pod adresem: {$IndividualWebAppURL}.

Login: {$IndividualWebAppLogin}
Has³o: {$IndividualWebAppPass}

W za³¹czniku znajd¹ Pañstwo notê obci¹¿eniowo ksiêgow¹ o numerze: {$noteNo}.

Z powa¿aniem
Zespó³ ds. obs³ugi klienta' EMAIL_BODY_TEMPLATE, 'Przyznanie bonów szkoleniowych w ramach programu dofinansowania: {$grantProgramName}' EMAIL_SUBJECT_TEMPLATE,
                                                     'Przes³anie informacji o zrealizowaniu szkolenia na bony elektroniczne' DESCRIPTION from dual)
ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
  msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
  msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);

--------------------------------------------------------------------------------------
-----------------------------------CORR_NOTIF-----------------------------------------
---------------------------Informacja o korekcie rozliczenia--------------------------
-------------------------------3.6	Korekta rozliczenia-------------------------------
--------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'CORR_NOTIF' ID ,'Szanowni Pañstwo

Po weryfikacji dokumentów do rozliczenia otrzymanych w dniu {$arrivalDate}, numer rozliczenia {$rmbsNumber}, rozliczenie zosta³o zwrócone do korekty, dane szczeg³owe na temat zidentyfikowanych niezgodnoœci znajd¹ Pañstwo w serwisie.
Wzywamy do uzupe³nienia brakuj¹cych dokumentów w ci¹gu 5 dni roboczych.' EMAIL_BODY_TEMPLATE,
                                                     'Rozliczenie zwrócone do korekty w ramach programu dofinansowania: {$grantProgramName}' EMAIL_SUBJECT_TEMPLATE,
                                                     'Informacja o korekcie rozliczenia' DESCRIPTION from dual)
                                             ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
  msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
  msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);

--------------------------------------------------------------------------------------
-----------------------------------E_REIMB--------------------------------------------
---------------Informacja o rozliczeniu bonów szkoleniowych---------------------------
-----------------3.3	Rozliczenie bonów szkoleniowych---------------------------------
--------------------------------------------------------------------------------------

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



--------------------------------------------------------------------------------------
-----------------------------------CNF_PYMT-------------------------------------------
-----------------------Potwierdzenie wp³aty nale¿noœci--------------------------------
---------------------3.4	Potwierdzenie wyp³aty naleŸnoœci----------------------------
--------------------------------------------------------------------------------------

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