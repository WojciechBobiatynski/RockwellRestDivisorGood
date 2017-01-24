
--------------------------------------------------------------------------------------
-----------------------------------KK_ORDER-------------------------------------------
---------1.6	Email do Uczestnika o przydzieleniu bon�w � systemowy/automatyczny-------
----------------------3.2	Email do OsFiz/M�P o przydzieleniu bon�w--------------------
--------------------------------------OK-----------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'KK_ORDER' ID ,'Sz. P. {$firstName} {$lastName},

Witamy w Systemie bon�w szkoleniowych w ramach projektu {$grantProgramName} realizowanego przez Wojew�dzki Urz�d Pracy w Krakowie. W zwi�zku z dokonaniem wp�aty wk�adu w�asnego przydzielono {$grantedVouchersNumber} elektronicznych bon�w szkoleniowych.

Stan przydzielonej puli bon�w oraz ich rezerwacj� na us�ugi mo�na �ledzi� pod adresem: {$IndividualWebAppURL}.

Login: {$IndividualWebAppLogin}
Has�o: {$IndividualWebAppPass}

Szczeg�owe informacje dotycz�ce systemu bon�w szkoleniowych dost�pne s� w zak�adce Pomoc.

W za��czeniu przesy�amy dokument potwierdzaj�cy dokonanie wp�aty wk�adu w�asnego tj. not� obci��eniowo-ksi�gow� o nr: {$noteNo}.

Z powa�aniem
Zesp� ds. obs�ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs�ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K�obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} � przyznanie bon�w szkoleniowych' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika o przydzieleniu bon�w �systemowy/automatyczny' DESCRIPTION,
                                                     'html' EMAIL_TYPE  from dual)
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
Uprzejmie informujemy, �e rozliczono bony w ramach projektu {$grantProgramName} dotycz�ce us�ugi {$trainingName}.

W zwi�zku z tym, i� warto�� wp�aconego wk�adu w�asnego jest wy�sza ni� warto�� wynikaj�ca z rozliczenia nadp�acone �rodki zosta�y zwr�cone na rachunek bankowy, z kt�rego wp�yn�y. W za��czeniu przesy�amy dokument potwierdzaj�cy zwrot �rodk�w tj. not� uznaniow� o nr{$noteNo}.

Z powa�aniem
Zesp� ds. obs�ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs�ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K�obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} � zwrot nadp�aconego wk�adu w�asnego z tytu�u rozliczenia us�ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika o rozliczeniu us�ugi - systemowy/automatyczny' DESCRIPTION,
                                                     'html' EMAIL_TYPE
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
Uprzejmie informujemy, �e rozliczono bony w ramach projektu {$grantProgramName} dotycz�ce szkolenia {$trainingName}.

W za��czniku znajdzie Pan/Pani potwierdzenie zap�aty nale�no�ci za us�ug�.

Z powa�aniem
Zesp� ds. obs�ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs�ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K�obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} � potwierdzenie zap�aty nale�no�ci z tytu�u rozliczenia us�ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika z potwierdzeniem zap�aty nale�no�ci za us�ug� - systemowy/automatyczny' DESCRIPTION,
                                                     'html' EMAIL_TYPE
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

W za��czniku znajdzie Pan/Pani potwierdzenie rozliczenia us�ugi, tj. dokument pn. Potwierdzenie realizacji dofinansowania oraz potwierdzemoe zap�aty nale�no�ci za us�ug�.

Z powa�aniem
Zesp� ds. obs�ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs�ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K�obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} � potwierdzenie rozliczenia us�ugi oraz potwierdzenie zap�aty nale�no�ci z tytu�u tej us�ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika (M�P) z potwierdzeniem rozliczenia us�ugi - systemowy/automatyczny' DESCRIPTION,
                                                     'html' EMAIL_TYPE from dual) ins ON (msg.ID = ins.ID)
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
---------------------------------------OK---------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'CORR_NOTIF' ID ,'Szanowni Pa�stwo,

Po weryfikacji dokument�w do rozliczenia otrzymanych w dniu {$arrivalDate}, numer rozliczenia {$rmbsNumber}, rozliczenie zosta�o zwr�cone do korekty. Szczeg�owe dane dot. zidentyfikowanych nieprawid�owo�ci znajd� Pa�stwo w serwisie www.

Prosimy o korekt� dokument�w rozliczeniowych w terminie 5 dni roboczych od otrzymania niniejszej  informacji.

Z powa�aniem
Zesp� ds. obs�ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs�ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K�obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} � konieczno�� dokonania korekty w rozliczeniu us�ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Us�ugodawcy z informacj� o konieczno�ci korekty rozliczenia szkolenia - systemowy/automatyczny' DESCRIPTION,
                                                     'html' EMAIL_TYPE  from dual)
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
---------------------------------------OK-------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'PIN_SEND' ID ,'Sz. P. {$firstName} {$lastName},

Uprzejmie informujemy, i� zarezerwowano  us�ug�: {$trainingName}, {$trainingPlace}, {$trainingInstitutionName}, {$trainingStartDate}, liczba zarezerwowanych bon�w: {$assignedProductNum}.

Poni�ej znajduje si� kod PIN, s�u��cy do rozliczenia us�ugi, kt�ry nale�y przekaza� us�ugodawcy w dniu zako�czenia us�ugi. Przekazanie kodu PIN jest r�wnoznaczne z potwierdzeniem uczestnictwa w us�udze.

PIN: {$reimbursmentPin}

Z powa�aniem
Zesp� ds. obs�ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs�ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K�obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} � kod PIN do rozliczenia us�ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do uczestnika z numerem PIN do us�ugi po jej rezerwacji - systemowy/automatyczny' DESCRIPTION,
                                                     'html' EMAIL_TYPE  from dual)
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
---------------------------------------OK-------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'PIN_RESEND' ID ,'Poni�ej znajduje si� kod PIN, s�u��cy do rozliczenia us�ugi:

{$trainingName}, {$trainingPlace}, {$trainingInstitutionName}, {$trainingStartDate}, liczba zarezerwowanych bon�w: {$assignedProductNum}
kt�ry nale�y przekaza� us�ugodawcy w dniu zako�czenia us�ugi. Przekazanie kodu PIN jest r�wnoznaczne z potwierdzeniem uczestnictwa w us�udze.

PIN: {$reimbursmentPin}

Z powa�aniem
Zesp� ds. obs�ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs�ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K�obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} � kod PIN do rozliczenia us�ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do uczestnika z numerem PIN do us�ugi ( ponowna wysy�ka) - systemowy/automatyczny' DESCRIPTION,
                                                     'html' EMAIL_TYPE  from dual)
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
---------------------------------------OK-----------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'RESET_LINK' ID ,'Poni�ej znajduje si� link do resetu has�a do serwisu www

{$resetLink}

Z powa�aniem
Zesp� ds. obs�ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs�ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K�obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     'Reset has�a' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Us�ugodawcy z linkiem do resetu has�a do logowania - systemowy/automatyczny' DESCRIPTION,
                                                     'html' EMAIL_TYPE  from dual)
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
--------------------------------------DO PODPIECIA------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'EXPI_PROD' ID ,'Sz. P. {$firstName} {$lastName},

Przypominamy, �e termin wa�no�ci Bon�w Szkoleniowych przydzielonych w ramach projektu pn. �Kierunek Kariera� up�ywa dnia {$contractExpiryDate}.

Z powa�aniem
Zesp� ds. obs�ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs�ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K�obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} � przypomnienie o up�ywaj�cym terminie wa�no�ci bon�w' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do uczestnika, informuj�cy o up�ywaj�cym terminie wa�no�ci bon�w - systemowy/automatyczny' DESCRIPTION,
                                                     'html' EMAIL_TYPE  from dual)
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
--------------------------------------DO PODPIECIA------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'EXPI_REIMB' ID ,'Sz. P. {$firstName} {$lastName},

Uprzejmie informujemy, �e rozliczono bony w ramach projektu {$grantProgramName}, kt�rych termin wa�no�ci si� zako�czy�.

W za��czeniu przesy�amy dokument potwierdzaj�cy rozliczenie wp�aconego wk�adu w�asnego tj. not� uznaniow� nr{$noteNo}.

Z powa�aniem
Zesp� ds. obs�ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs�ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K�obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} � zwrot wk�adu w�asnego z tytu�u up�yni�cia utraty terminu wa�no�ci bon�w' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika o rozliczeniu bon�w po terminie wa�no�ci systemowy/automatyczny' DESCRIPTION,
                                                     'html' EMAIL_TYPE  from dual)
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
--------------------------------------DO PODPIECIA------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'TI_ACCESS' ID ,'Szanowni Pa�stwo,

Witamy w Systemie bon�w szkoleniowych w ramach projektu {$grantProgramName} realizowanego przez Wojew�dzki Urz�d Pracy w Krakowie.

Rezerwacje bon�w uczestnik�w na us�ugi oraz rozliczenia us�ug realizuje si� pod adresem: {$IndividualWebAppURL}.


Login: {$login}
Link do resetu has�a: {$resetLink}

Szczeg�owe informacje dotycz�ce systemu bon�w szkoleniowych oraz jego obs�ugi dost�pne s� w zak�adce Pomoc.

Z powa�aniem
Zesp� ds. obs�ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs�ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K�obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} � dost�p do serwisu www' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Us�ugodawcy o przydzieleniu dost�pu do serwisu www - systemowy/automatyczny' DESCRIPTION,
                                                     'html' EMAIL_TYPE  from dual)
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
--------------------------------------DO PODPIECIA------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'RET_REIMB' ID ,'Sz. P. {$firstName} {$lastName},

Uprzejmie informujemy, �e rozliczono zwrot bon�w w ramach projektu {$grantProgramName}.

W za��czeniu przesy�amy dokument potwierdzaj�cy rozliczenie wp�aconego wk�adu w�asnego tj. not� uznaniow� nr{$noteNo}.

Z powa�aniem
Zesp� ds. obs�ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs�ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K�obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} � zwrot wk�adu w�asnego z tytu�u zwrotu bon�w ' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika o rozliczeniu zwrotu bon�w - systemowy/automatyczny' DESCRIPTION,
                                                     'html' EMAIL_TYPE  from dual)
                                             ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
  msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
  msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);




---------------------------------------------------------------------------------------------------
-----------------------------------VC_SEND---------------------------------------------------------
---------1.21	Email do Uczestnika � ponowna wysy�ka kodu weryfikacyjnego �systemowy/automatyczny---
-----------------------Dane do logowania dla osoby fizycznej---------------------------------------
---------------------------------------OK----------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'VC_SEND' ID ,'Sz. P. {$firstName} {$lastName},
Witamy w Systemie bon�w szkoleniowych.

Login: {$login}
Kod weryfikacyjny: {$verificationCode}

Stan przydzielonej puli bon�w oraz ich rezerwacj� na us�ugi mo�na �ledzi� pod adresem: {$url}.

Z powa�aniem
Zesp� ds. obs�ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs�ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K�obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     'Przypomnienie kodu weryfikacyjnego' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika � ponowna wysy�ka kodu weryfikacyjnego �systemowy/automatyczny' DESCRIPTION,
                                                     'html' EMAIL_TYPE from dual)
                                             ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
  msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
  msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);



---------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------


DECLARE
  large_txt CLOB;
BEGIN

  large_txt:='<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Kierunek Kariera - Wojew�dzki Urz�d Pracy Krak�w</title>

<style type="text/css">
  #outlook a {padding:0;}
  p, span, strong, big, a {font-family: Arial, sans-serif; font-size: 300; }
  p {margin: 19px 0; padding: 0;font-size: 13px; line-height: 19px;}
  table td {border-collapse: collapse;}
  table {border-collapse: collapse; mso-table-lspace:0pt; mso-table-rspace:0pt; }
  img {display: block; outline: none; text-decoration: none; -ms-interpolation-mode: bicubic;}
  a img {border: none;}a, a:link, a:visited {text-decoration: none; color: #00788a}
  a:hover {text-decoration: underline;}
  h2,h2 a,h2 a:visited,h3,h3 a,h3 a:visited,h4,h5,h6,.t_cht {color:#000 !important}
  .ExternalClass p, .ExternalClass span, .ExternalClass font, .ExternalClass td {line-height: 100%}
  .ExternalClass {width: 100%;}
</style>
<!--[if gte mso 9]>
  <style>
  /* Target Outlook 2007 and 2010 */
  </style>
<![endif]-->
</head><body style="width:100%; margin:0; padding:0; -webkit-text-size-adjust:100%; -ms-text-size-adjust:100%; background: #ffffff;"><table cellpadding="0" cellspacing="0" border="0" style="margin:0; padding:0; width:100%; line-height: 100% !important;">
  <tr>
    <td valign="top" style="background: #ffffff;"><table cellpadding="0" cellspacing="0" border="0" align="center" width="640">
        <tr>
          <td valign="top"><img src="http://cdn.sodexo.pl/gryf/mailing/header.gif" width="640" height="121" title="Ma�opolska, Urz�d Pracy, Instytucja Wojew�dztwa Ma�opolskiego Wojew�dzki Urz�d Pracy w Krakowie" style="margin:0;padding:0;border:0;"></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td valign="top" height="64" style="background-color:#304f87;text-align: center;vertical-align:middle;"><span style="font-size: 13px; line-height: 50px;color:#ffffff; font-weight: 700;">{$emailPlainSubjectTemplates}</span></td>
  </tr>
  <tr><td valign="top" style="height: 29px;background: #ffffff;"><br></td>
  </tr>
  <tr>
    <td valign="top" style="background: #ffffff;"><table cellpadding="0" cellspacing="0" border="0" align="center" width="640" style="background: #ffffff;">
        <tr>
          <td valign="top" style="background-color: #ffffff; color:#696668; font-size: 13px; line-height: 19px; padding:0 29px;">
			  <p style="font-size: 13px; line-height: 19px;">{$emailPlainBodyTemplates}</p>
		  </td>
		</tr>
      </table></td>
  </tr>
  <tr>
    <td valign="top" style="height: 29px;background: #ffffff;"><br></td>
  </tr>
  <tr><td valign="top" style="background: #ffffff;"><table cellpadding="0" cellspacing="0" border="0" align="center" width="640" style="background: #ffffff;">
        <tr>
          <td valign="top" style="background-color: #304f87; color:#ffffff; font-size: 13px; line-height: 19px; padding: 0 29px; text-align: center;"><p style="font-size: 13px; line-height: 19px;">Wszystkie informacje o projekcie pn. "Kierunek Kariera" dost�pne s� na stronie:<br><a style="color:#ffc720;font-size: 13px; line-height: 19px;" href="http://www.pociagdokariery.pl" title="www.pociagdokariery.pl">www.pociagdokariery.pl</a></p><p style="font-size: 13px; line-height: 19px;"><a style="color:#ffc720;font-size: 13px; line-height: 19px;" href="http://www.facebook.com/pociagdokariery" title="www.facebook.com/pociagdokariery">www.facebook.com/pociagdokariery</a></p></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td valign="top" style="background: #ffffff;"><table cellpadding="0" cellspacing="0" border="0" align="center" width="640" style="background: #ffffff;">
        <tr>
          <td valign="top"><img src="http://cdn.sodexo.pl/gryf/mailing/footer.gif" width="640" height="100" title="Fundusze Europejskie Program Regionalny, Ma�polska, Unia Europejska Europejski Fundusz Socjalny" style="margin:0;padding:0;border:0;"></td>
        </tr>
      </table></td>
  </tr>
  <tr><td valign="top" style="background: #ffffff;"><table cellpadding="0" cellspacing="0" border="0" align="center" width="640" style="background: #ffffff;">
        <tr>
          <td valign="top" style="background-color: #f7f7f7; color:#696668; font-size: 13px; line-height: 19px; padding:0 29px; text-align: center;"><p style="font-size: 13px; line-height: 19px;">E-mail zosta� wygenerowany automatycznie, prosimy na niego nie odpowiada�.</p></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td valign="top" style="background: #ffffff;"><table cellpadding="0" cellspacing="0" border="0" align="center" width="640" style="background: #ffffff;">
        <tr>
          <td valign="top" style="background-color: #ffffff; color:#696668; font-size: 11px; line-height: 15px; padding:0 29px; text-align: center;"><p style="font-size: 11px; line-height: 15px;">Wiadomo�� zosta�a wys�ana przez Sodexo Benefits and Rewards Services Polska Sp. z o.o., ul. K�obucka 25, 02-699 Warszawa S�d Rejonowy dla m. st. Warszawy w Warszawie, XIII Wydzia� Gospodarczy Krajowego Rejestru S�dowego, Kapita� Zak�adowy 3,000,000 PLN, KRS: 0000033826, NIP: 522-23-57-343, poniewa� Tw�j adres e-mail znajduje si� na li�cie kontakt�w.</p></td>
        </tr>
      </table></td>
  </tr>
  <tr><td valign="top" style="background: #ffffff;"><table cellpadding="0" cellspacing="0" border="0" align="center" width="640" style="background: #ffffff;">
        <tr>
          <td valign="top" style="background-color: #f7f7f7; color:#696668; font-size: 13px; line-height: 19px; padding:29px 29px 0 29px; text-align: center;"><a href="http://www.sodexo.pl/" title="Sodexo Benefits and Rewards Services Polska Sp. z o.o." style="margin: 0 auto;"><img width="110" height="36" src="http://cdn.sodexo.pl/gryf/mailing/sodexo.gif" title="Sodexo Benefits and Rewards Services Polska Sp. z o.o." alt="Sodexo Benefits and Rewards Services Polska Sp. z o.o."></a></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td valign="top" style="background: #ffffff;"><table cellpadding="0" cellspacing="0" border="0" align="center" width="640" style="background: #ffffff;">
        <tr>
          <td valign="top" style="background-color: #f7f7f7; color:#696668; font-size: 13px; line-height: 19px; padding:0 29px; text-align: center;"><p style="font-size: 13px; line-height: 19px;">&copy; 2017 <a style="color:#696668;font-size: 13px; line-height: 19px;" href="http://www.sodexo.pl/" title="Sodexo Benefits and Rewards Services Polska Sp. z o.o.">Sodexo Benefits and Rewards Services Polska Sp. z o.o.</a></p></td>
        </tr>
      </table></td>
  </tr>
</table></body></html>';


update APP_PBE.EMAIL_TEMPLATES
set EMAIL_BODY_HTML_TEMPLATE = large_txt
where ID in (
  'KK_ORDER',
  'E_REIMB',
  'CNF_PYMT',
  'E_CONF_GRA',
  'CORR_NOTIF',
  'PIN_SEND',
  'PIN_RESEND',
  'RESET_LINK',
  'EXPI_PROD',
  'EXPI_REIMB',
  'TI_ACCESS',
  'RET_REIMB',
  'VC_SEND');

update APP_PBE.EMAIL_TEMPLATES
set EMAIL_TYPE = 'html'
where ID in (
  'KK_ORDER',
  'E_REIMB',
  'CNF_PYMT',
  'E_CONF_GRA',
  'CORR_NOTIF',
  'PIN_SEND',
  'PIN_RESEND',
  'RESET_LINK',
  'EXPI_PROD',
  'EXPI_REIMB',
  'TI_ACCESS',
  'RET_REIMB',
  'VC_SEND');

END;
/










