
--------------------------------------------------------------------------------------
-----------------------------------KK_ORDER-------------------------------------------
---------1.6	Email do Uczestnika o przydzieleniu bon�w � systemowy/automatyczny-------
----------------------3.2	Email do OsFiz/M�P o przydzieleniu bon�w--------------------
--------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'KK_ORDER' ID ,'Sz. P. {$firstName} {$lastName},

Witamy w Systemie bon�w szkoleniowych w ramach projektu {$grantProgramName} realizowanego przez Wojew�dzki Urz�d Pracy w Krakowie. W zwi�zku z dokonaniem wp�aty wk�adu w�asnego przydzielono {$grantedVouchersNumber} elektronicznych bon�w szkoleniowych.

Stan przydzielonej puli bon�w oraz ich rezerwacj� na us�ugi mo�na �ledzi� pod adresem: {$IndividualWebAppURL}.

Login: {$IndividualWebAppLogin}
Has�o: {$IndividualWebAppPass}

Szczeg�owe informacje dotycz�ce systemu bon�w szkoleniowych dost�pne s� w zak�adce FAQ.

W za��czeniu przesy�amy dokument potwierdzaj�cy dokonanie wp�aty wk�adu w�asnego tj. not� obci��eniowo-ksi�gow� o nr: {$noteNo}.

Z powa�aniem
Zesp� ds. obs�ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} � przyznanie bon�w szkoleniowych' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika o przydzieleniu bon�w �systemowy/automatyczny' DESCRIPTION from dual)
                                             ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
  msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
  msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);


--------------------------------------------------------------------------------------------
-----------------------------------E_REIMB--------------------------------------------------
---------------1.7	Email do Uczestnika o rozliczeniu us�ugi - systemowy/automatyczny-------
-----------------3.3	Rozliczenie bon�w szkoleniowych---------------------------------------
--------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'E_REIMB' ID ,'Sz. P. {$firstName} {$lastName},
Uprzejmie informujemy, �e rozliczono bony w ramach projektu {$grantProgramName} dotycz�ce us�ugi {nazwa szkolenia}.

W zwi�zku z tym, i� warto�� wp�aconego wk�adu w�asnego jest wy�sza ni� warto�� wynikaj�ca z rozliczenia nadp�acone �rodki zosta�y zwr�cone na rachunek bankowy, z kt�rego wp�yn�y. W za��czeniu przesy�amy dokument potwierdzaj�cy zwrot �rodk�w tj. not� uznaniow� o nr{$noteNo}.

Z powa�aniem
Zesp� ds. obs�ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName}� zwrot nadp�aconego wk�adu w�asnego z tytu�u rozliczenia us�ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika o rozliczeniu us�ugi - systemowy/automatyczny' DESCRIPTION
                                              from dual) ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
  msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
  msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);


---------------------------------------------------------------------------------------------------------------
-----------------------------------CNF_PYMT--------------------------------------------------------------------
-------1.8	Email do Uczestnika z potwierdzeniem zap�aty nale�no�ci za us�ug� - systemowy/automatyczny---------
---------------------3.4	Potwierdzenie wyp�aty nale�no�ci-----------------------------------------------------
---------------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'CNF_PYMT' ID ,'Sz. P. {$firstName} {$lastName},
Uprzejmie informujemy, �e rozliczono bony w ramach projektu {$grantProgramName} dotycz�ce szkolenia {$nazwa szkolenia}.

W za��czniku znajdzie Pan/Pani potwierdzenie zap�aty nale�no�ci za us�ug�.

Z powa�aniem
Zesp� ds. obs�ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName}� potwierdzenie zap�aty nale�no�ci z tytu�u rozliczenia us�ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika z potwierdzeniem zap�aty nale�no�ci za us�ug� - systemowy/automatyczny' DESCRIPTION
                                              from dual) ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
  msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
  msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);

-------------------------------------------------------------------------------------------------------
-----------------------------------E_REIMB-------------------------------------------------------------
------1.9	Email do Uczestnika (M�P) z potwierdzeniem rozliczenia us�ugi - systemowy/automatyczny-------
-----------------Potwierdzenie dofinansowania----------------------------------------------------------
-------------------------------------------------------------------------------------------------------


MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'E_CONF_GRA' ID ,'Sz. P. {$firstName} {$lastName},
Uprzejmie informujemy, �e rozliczono bony w ramach projektu {$grantProgramName} dotycz�ce szkolenia {$trainingName}.

W za��czniku znajdzie Pan/Pani potwierdzenie rozliczenia us�ugi, tj. dokument pn. Potwierdzenie realizacji dofinansowania.

Z powa�aniem
Zesp� ds. obs�ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName}� potwierdzenie rozliczenia us�ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika (M�P) z potwierdzeniem rozliczenia us�ugi - systemowy/automatyczny' DESCRIPTION
                                              from dual) ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
  msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
  msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);

--------------------------------------------------------------------------------------------------------------------
-----------------------------------CORR_NOTIF-----------------------------------------------------------------------
----1.10	Email do Us�ugodawcy z informacj� o konieczno�ci korekty rozliczenia szkolenia - systemowy/automatyczny---
-----------------------------3.6	Korekta rozliczenia---------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'CORR_NOTIF' ID ,'Szanowni Pa�stwo,

Po weryfikacji dokument�w do rozliczenia otrzymanych w dniu {$arrivalDate}, numer rozliczenia {$rmbsNumber}, rozliczenie zosta�o zwr�cone do korekty. Szczeg�owe dane dot. zidentyfikowanych nieprawid�owo�ci znajd� Pa�stwo w serwisie www.

Prosimy o korekt� dokument�w rozliczeniowych w terminie 5 dni roboczych od otrzymania niniejszej  informacji.

Z powa�aniem
Zesp� ds. obs�ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName}� konieczno�� dokonania korekty w rozliczeniu us�ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Us�ugodawcy z informacj� o konieczno�ci korekty rozliczenia szkolenia - systemowy/automatyczny' DESCRIPTION from dual)
                                             ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
  msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
  msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);


------------------------------------------------------------------------------------------------------
-----------------------------------PIN_SEND-----------------------------------------------------------
---1.13 Email do uczestnika z numerem PIN do us�ugi  po jej rezerwacji - systemowy/automatyczny-------
-----------------3.7	Email do uczestnika z numerem PIN do szkolenia----------------------------------
------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'PIN_SEND' ID ,'Sz. P. {$firstName} {$lastName},

Uprzejmie informujemy, i� zarezerwowano  us�ug�:{$trainingName}, {$trainingPlace}, {$trainingInstitutionName}, {$trainingStartDate}, liczba zarezerwowanych bon�w: {$assignedProductNum}.

Poni�ej znajduje si� kod PIN, s�u��cy do rozliczenia us�ugi, kt�ry nale�y przekaza� us�ugodawcy w dniu zako�czenia us�ugi. Przekazanie kodu PIN jest r�wnoznaczne z potwierdzeniem uczestnictwa w us�udze.

PIN: {$reimbursmentPin}

Z powa�aniem
Zesp� ds. obs�ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} � kod PIN do rozliczenia us�ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do uczestnika z numerem PIN do us�ugi po jej rezerwacji - systemowy/automatyczny' DESCRIPTION from dual)
                                             ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
  msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
  msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);


------------------------------------------------------------------------------------------------------
-----------------------------------PIN_RESEND---------------------------------------------------------
----1.15	Email do uczestnika z numerem PIN do us�ugi ( ponowna wysy�ka) - systemowy/automatyczny-----
----------------------------Przes�anie pinu do szkolenia----------------------------------------------
------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'PIN_RESEND' ID ,'Poni�ej znajduje si� kod PIN, s�u��cy do rozliczenia us�ugi:

{$trainingName}, {$trainingPlace}, {$trainingInstitutionName}, {$trainingStartDate}, liczba zarezerwowanych bon�w: {$assignedProductNum}
kt�ry nale�y przekaza� us�ugodawcy w dniu zako�czenia us�ugi. Przekazanie kodu PIN jest r�wnoznaczne z potwierdzeniem uczestnictwa w us�udze.

PIN: {$reimbursmentPin}

Z powa�aniem
Zesp� ds. obs�ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} � kod PIN do rozliczenia us�ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do uczestnika z numerem PIN do us�ugi ( ponowna wysy�ka) - systemowy/automatyczny' DESCRIPTION from dual)
                                             ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
  msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
  msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);




--------------------------------------------------------------------------------------------------
-----------------------------------RESET_LINK-----------------------------------------------------
----1.16	 Email do Us�ugodawcy z linkiem do resetu has�a do logowania - systemowy/automatyczny---
--------------------Powiadomienie o wygenerowaniu linku resetuj�cego has�o------------------------
--------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'RESET_LINK' ID ,'Poni�ej znajduje si� link do resetu has�a do serwisu www

{$resetLink}

Z powa�aniem
Zesp� ds. obs�ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} � reset has�a' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Us�ugodawcy z linkiem do resetu has�a do logowania - systemowy/automatyczny' DESCRIPTION from dual)
                                             ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
  msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
  msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);


--------------------------------------------------------------------------------------------------------------
-----------------------------------EXPI_PROD------------------------------------------------------------------
----1.17 Email do uczestnika , informuj�cy o up�ywaj�cym terminie wa�no�ci bon�w. - systemowy/automatyczny----
--------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'EXPI_PROD' ID ,'Sz. P. {$firstName} {$lastName},

Przypominamy, �e termin wa�no�ci Bon�w Szkoleniowych przydzielonych w ramach projektu pn. �Kierunek Kariera� up�ywa dnia {$contractExpiryDate}.

Z powa�aniem
Zesp� ds. obs�ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} � przypomnienie o up�ywaj�cym terminie wa�no�ci bon�w' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do uczestnika, informuj�cy o up�ywaj�cym terminie wa�no�ci bon�w - systemowy/automatyczny' DESCRIPTION from dual)
                                             ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
  msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
  msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);



--------------------------------------------------------------------------------------------------------------
-----------------------------------EXPI_REIMB-----------------------------------------------------------------
----1.18 Email do Uczestnika o rozliczeniu bon�w po terminie wa�no�ci systemowy/automatyczny------------------
--------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'EXPI_REIMB' ID ,'Sz. P. {$firstName} {$lastName},

Uprzejmie informujemy, �e rozliczono bony w ramach projektu {$grantProgramName}, kt�rych termin wa�no�ci si� zako�czy�.

W za��czeniu przesy�amy dokument potwierdzaj�cy rozliczenie wp�aconego wk�adu w�asnego tj. not� uznaniow� nr{$noteNo}.

Z powa�aniem
Zesp� ds. obs�ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName}� zwrot wk�adu w�asnego z tytu�u up�yni�cia utraty terminu wa�no�ci bon�w' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika o rozliczeniu bon�w po terminie wa�no�ci systemowy/automatyczny' DESCRIPTION from dual)
                                             ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
  msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
  msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);



--------------------------------------------------------------------------------------------------------------
-----------------------------------TI_ACCESS------------------------------------------------------------------
----1.19 Email do Us�ugodawcy o przydzieleniu dost�pu do serwisu www - systemowy/automatyczny-----------------
--------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'TI_ACCESS' ID ,'Szanowni Pa�stwo,

Witamy w Systemie bon�w szkoleniowych w ramach projektu {$grantProgramName} realizowanego przez Wojew�dzki Urz�d Pracy w Krakowie.

Rezerwacje bon�w uczestnik�w na us�ugi oraz rozliczenia us�ug realizuje si� pod adresem: {$IndividualWebAppURL}.

Login: {$login}
Has�o: {$password}

Szczeg�owe informacje dotycz�ce systemu bon�w szkoleniowych oraz jego obs�ugi dost�pne s� w zak�adce FAQ.

Z powa�aniem
Zesp� ds. obs�ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} � dost�p do serwisu www' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Us�ugodawcy o przydzieleniu dost�pu do serwisu www - systemowy/automatyczny' DESCRIPTION from dual)
                                             ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
  msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
  msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);


--------------------------------------------------------------------------------------------------------------
-----------------------------------TI_ACCESS------------------------------------------------------------------
----1.20	Email do Uczestnika o rozliczeniu zwrotu bon�w - systemowy/automatyczny-----------------------------
--------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'RET_REIMB' ID ,'Sz. P. {$firstName} {$lastName},

Uprzejmie informujemy, �e rozliczono zwrot bon�w w ramach projektu {$grantProgramName}.

W za��czeniu przesy�amy dokument potwierdzaj�cy rozliczenie wp�aconego wk�adu w�asnego tj. not� uznaniow� nr{$noteNo}.

Z powa�aniem
Zesp� ds. obs�ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName}� zwrot wk�adu w�asnego z tytu�u zwrotu bon�w ' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika o rozliczeniu zwrotu bon�w - systemowy/automatyczny' DESCRIPTION from dual)
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
Zesp� ds. obs�ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     'Dane do logowania' EMAIL_SUBJECT_TEMPLATE,
                                                     'Dane do logowania dla osoby fizycznej' DESCRIPTION from dual)
                                             ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
  msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
  msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);
















