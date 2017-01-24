
--------------------------------------------------------------------------------------
-----------------------------------RESET_LINK-----------------------------------------
--------------------Powiadomienie o wygenerowaniu linku resetuj�cego has�o------------
--------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'RESET_LINK' ID ,'Link resetuj�cy has�o: {$resetLink}

Z powa�aniem
Zesp� ds. obs�ugi klienta' EMAIL_BODY_TEMPLATE, 'Reset has�a' EMAIL_SUBJECT_TEMPLATE, 'Powiadomienie o wygenerowaniu linku resetuj�cego has�o' DESCRIPTION from dual)
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
Poni�ej znajduj� si� dane logowania do naszego serwisu

Adres: {$url}
Login: {$login}
Has�o: {$verificationCode}

Z powa�aniem
Zesp� ds. obs�ugi klienta' EMAIL_BODY_TEMPLATE, 'Dane do logowania' EMAIL_SUBJECT_TEMPLATE, 'Dane do logowania dla osoby fizycznej' DESCRIPTION from dual)
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
-------------------------Przes�anie pinu do szkolenia---------------------------------
-----------------3.7	Email do uczestnika z numerem PIN do szkolenia------------------
--------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'PIN_SEND' ID ,'Sz. P. {$firstName} {$lastName},

Uprzejmie informujemy, i� zarezerwowano  SZKOLENIE:{$trainingName}, {$trainingPlace}, {$trainingInstitutionName}, {$trainingStartDate},
liczba zarezerwowanych bon�w: {$assignedProductNum}.

Poni�ej znajduje si� kod PIN, potwierdzaj�cy uczestnictwo w szkoleniu, kt�ry nale�y przekaza� Usługodawcy na koniec wskazanego szkolenia.
PIN: {$reimbursmentPin}

Z powa�aniem
Zesp� ds. obs�ugi klienta' EMAIL_BODY_TEMPLATE, 'Zarezerwowano szkolenie w ramach programu dofinansowania: {$grantProgramName}' EMAIL_SUBJECT_TEMPLATE,
                                                     'Przes�anie pinu do szkolenia' DESCRIPTION from dual)
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
----------------------------Przes�anie pinu do szkolenia------------------------------
--------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'PIN_RESEND' ID ,'Sz. P. {$firstName} {$lastName},

Uprzejmie informujemy, i� zarezerwowano  SZKOLENIE:{$trainingName}, {$trainingPlace}, {$trainingInstitutionName}, {$trainingStartDate},
liczba zarezerwowanych bon�w: {$assignedProductNum}.

Poni�ej znajduje si� kod PIN, potwierdzaj�cy uczestnictwo w szkoleniu, kt�ry nale�y przekaza� Usługodawcy na koniec wskazanego szkolenia.
PIN: {$reimbursmentPin}

Z powa�aniem
Zesp� ds. obs�ugi klienta' EMAIL_BODY_TEMPLATE, 'Zarezerwowano szkolenie w ramach programu dofinansowania: {$grantProgramName}' EMAIL_SUBJECT_TEMPLATE,
                                                     'Ponowne przes�anie pinu do szkolenia' DESCRIPTION from dual)
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
---------Przes�anie informacji o zrealizowaniu szkolenia na bony elektroniczne--------
--------------------3.2	Email do OsFiz/M�P o przydzieleniu bon�w----------------------
--------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'KK_ORDER' ID ,'Sz. P. {$firstName} {$lastName},

Uprzejmie informujemy, �e przydzielono dofinansowanie w ramach programu {$grantProgramName} w wysoko�ci {$grantedVouchersNumber} elektronicznych bon�w szkoleniowych.

Stan przydzielonej puli bon�w oraz zapisy na szkolenia mo�na �ledzi� pod adresem: {$IndividualWebAppURL}.

Login: {$IndividualWebAppLogin}
Has�o: {$IndividualWebAppPass}

W za��czniku znajd� Pa�stwo not� obci��eniowo ksi�gow� o numerze: {$noteNo}.

Z powa�aniem
Zesp� ds. obs�ugi klienta' EMAIL_BODY_TEMPLATE, 'Przyznanie bon�w szkoleniowych w ramach programu dofinansowania: {$grantProgramName}' EMAIL_SUBJECT_TEMPLATE,
                                                     'Przes�anie informacji o zrealizowaniu szkolenia na bony elektroniczne' DESCRIPTION from dual)
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

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'CORR_NOTIF' ID ,'Szanowni Pa�stwo

Po weryfikacji dokument�w do rozliczenia otrzymanych w dniu {$arrivalDate}, numer rozliczenia {$rmbsNumber}, rozliczenie zosta�o zwr�cone do korekty, dane szczeg�owe na temat zidentyfikowanych niezgodno�ci znajd� Pa�stwo w serwisie.
Wzywamy do uzupe�nienia brakuj�cych dokument�w w ci�gu 5 dni roboczych.' EMAIL_BODY_TEMPLATE,
                                                     'Rozliczenie zwr�cone do korekty w ramach programu dofinansowania: {$grantProgramName}' EMAIL_SUBJECT_TEMPLATE,
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
---------------Informacja o rozliczeniu bon�w szkoleniowych---------------------------
-----------------3.3	Rozliczenie bon�w szkoleniowych---------------------------------
--------------------------------------------------------------------------------------

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



--------------------------------------------------------------------------------------
-----------------------------------CNF_PYMT-------------------------------------------
-----------------------Potwierdzenie wp�aty nale�no�ci--------------------------------
---------------------3.4	Potwierdzenie wyp�aty nale�no�ci----------------------------
--------------------------------------------------------------------------------------

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