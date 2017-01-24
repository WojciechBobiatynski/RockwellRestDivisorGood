
--------------------------------------------------------------------------------------
-----------------------------------KK_ORDER-------------------------------------------
---------1.6	Email do Uczestnika o przydzieleniu bonów – systemowy/automatyczny-------
----------------------3.2	Email do OsFiz/MŒP o przydzieleniu bonów--------------------
--------------------------------------OK-----------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'KK_ORDER' ID ,'Sz. P. {$firstName} {$lastName},

Witamy w Systemie bonów szkoleniowych w ramach projektu {$grantProgramName} realizowanego przez Wojewódzki Urz¹d Pracy w Krakowie. W zwi¹zku z dokonaniem wp³aty wk³adu w³asnego przydzielono {$grantedVouchersNumber} elektronicznych bonów szkoleniowych.

Stan przydzielonej puli bonów oraz ich rezerwacjê na us³ugi mo¿na œledziæ pod adresem: {$IndividualWebAppURL}.

Login: {$IndividualWebAppLogin}
Has³o: {$IndividualWebAppPass}

Szczegó³owe informacje dotycz¹ce systemu bonów szkoleniowych dostêpne s¹ w zak³adce Pomoc.

W za³¹czeniu przesy³amy dokument potwierdzaj¹cy dokonanie wp³aty wk³adu w³asnego tj. notê obci¹¿eniowo-ksiêgow¹ o nr: {$noteNo}.

Z powa¿aniem
Zespó³ ds. obs³ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs³ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K³obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} – przyznanie bonów szkoleniowych' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika o przydzieleniu bonów –systemowy/automatyczny' DESCRIPTION,
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
---------------1.7	Email do Uczestnika o rozliczeniu us³ugi - systemowy/automatyczny-------
-----------------3.3	Rozliczenie bonów szkoleniowych---------------------------------------
--------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'E_REIMB' ID ,'Sz. P. {$firstName} {$lastName},
Uprzejmie informujemy, ¿e rozliczono bony w ramach projektu {$grantProgramName} dotycz¹ce us³ugi {$trainingName}.

W zwi¹zku z tym, i¿ wartoœæ wp³aconego wk³adu w³asnego jest wy¿sza ni¿ wartoœæ wynikaj¹ca z rozliczenia nadp³acone œrodki zosta³y zwrócone na rachunek bankowy, z którego wp³ynê³y. W za³¹czeniu przesy³amy dokument potwierdzaj¹cy zwrot œrodków tj. notê uznaniow¹ o nr{$noteNo}.

Z powa¿aniem
Zespó³ ds. obs³ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs³ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K³obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} – zwrot nadp³aconego wk³adu w³asnego z tytu³u rozliczenia us³ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika o rozliczeniu us³ugi - systemowy/automatyczny' DESCRIPTION,
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
-------1.8	Email do Uczestnika z potwierdzeniem zap³aty nale¿noœci za us³ugê - systemowy/automatyczny---------
---------------------3.4	Potwierdzenie wyp³aty nale¿noœci-----------------------------------------------------
---------------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'CNF_PYMT' ID ,'Sz. P. {$firstName} {$lastName},
Uprzejmie informujemy, ¿e rozliczono bony w ramach projektu {$grantProgramName} dotycz¹ce szkolenia {$trainingName}.

W za³¹czniku znajdzie Pan/Pani potwierdzenie zap³aty nale¿noœci za us³ugê.

Z powa¿aniem
Zespó³ ds. obs³ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs³ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K³obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} – potwierdzenie zap³aty nale¿noœci z tytu³u rozliczenia us³ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika z potwierdzeniem zap³aty nale¿noœci za us³ugê - systemowy/automatyczny' DESCRIPTION,
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
------1.9	Email do Uczestnika (MŒP) z potwierdzeniem rozliczenia us³ugi - systemowy/automatyczny-------
-----------------Potwierdzenie dofinansowania----------------------------------------------------------
-------------------------------------------------------------------------------------------------------


MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'E_CONF_GRA' ID ,'Sz. P. {$firstName} {$lastName},
Uprzejmie informujemy, ¿e rozliczono bony w ramach projektu {$grantProgramName} dotycz¹ce szkolenia {$trainingName}.

W za³¹czniku znajdzie Pan/Pani potwierdzenie rozliczenia us³ugi, tj. dokument pn. Potwierdzenie realizacji dofinansowania oraz potwierdzemoe zap³aty nale¿noœci za us³ugê.

Z powa¿aniem
Zespó³ ds. obs³ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs³ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K³obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} – potwierdzenie rozliczenia us³ugi oraz potwierdzenie zap³aty nale¿noœci z tytu³u tej us³ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika (MŒP) z potwierdzeniem rozliczenia us³ugi - systemowy/automatyczny' DESCRIPTION,
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
----1.10	Email do Us³ugodawcy z informacj¹ o koniecznoœci korekty rozliczenia szkolenia - systemowy/automatyczny---
-----------------------------3.6	Korekta rozliczenia---------------------------------------------------------------
---------------------------------------OK---------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'CORR_NOTIF' ID ,'Szanowni Pañstwo,

Po weryfikacji dokumentów do rozliczenia otrzymanych w dniu {$arrivalDate}, numer rozliczenia {$rmbsNumber}, rozliczenie zosta³o zwrócone do korekty. Szczegó³owe dane dot. zidentyfikowanych nieprawid³owoœci znajd¹ Pañstwo w serwisie www.

Prosimy o korektê dokumentów rozliczeniowych w terminie 5 dni roboczych od otrzymania niniejszej  informacji.

Z powa¿aniem
Zespó³ ds. obs³ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs³ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K³obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} – koniecznoœæ dokonania korekty w rozliczeniu us³ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Us³ugodawcy z informacj¹ o koniecznoœci korekty rozliczenia szkolenia - systemowy/automatyczny' DESCRIPTION,
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
---1.13 Email do uczestnika z numerem PIN do us³ugi  po jej rezerwacji - systemowy/automatyczny-------
-----------------3.7	Email do uczestnika z numerem PIN do szkolenia----------------------------------
---------------------------------------OK-------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'PIN_SEND' ID ,'Sz. P. {$firstName} {$lastName},

Uprzejmie informujemy, i¿ zarezerwowano  us³ugê: {$trainingName}, {$trainingPlace}, {$trainingInstitutionName}, {$trainingStartDate}, liczba zarezerwowanych bonów: {$assignedProductNum}.

Poni¿ej znajduje siê kod PIN, s³u¿¹cy do rozliczenia us³ugi, który nale¿y przekazaæ us³ugodawcy w dniu zakoñczenia us³ugi. Przekazanie kodu PIN jest równoznaczne z potwierdzeniem uczestnictwa w us³udze.

PIN: {$reimbursmentPin}

Z powa¿aniem
Zespó³ ds. obs³ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs³ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K³obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} – kod PIN do rozliczenia us³ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do uczestnika z numerem PIN do us³ugi po jej rezerwacji - systemowy/automatyczny' DESCRIPTION,
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
----1.15	Email do uczestnika z numerem PIN do us³ugi ( ponowna wysy³ka) - systemowy/automatyczny-----
----------------------------Przes³anie pinu do szkolenia----------------------------------------------
---------------------------------------OK-------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'PIN_RESEND' ID ,'Poni¿ej znajduje siê kod PIN, s³u¿¹cy do rozliczenia us³ugi:

{$trainingName}, {$trainingPlace}, {$trainingInstitutionName}, {$trainingStartDate}, liczba zarezerwowanych bonów: {$assignedProductNum}
który nale¿y przekazaæ us³ugodawcy w dniu zakoñczenia us³ugi. Przekazanie kodu PIN jest równoznaczne z potwierdzeniem uczestnictwa w us³udze.

PIN: {$reimbursmentPin}

Z powa¿aniem
Zespó³ ds. obs³ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs³ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K³obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} – kod PIN do rozliczenia us³ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do uczestnika z numerem PIN do us³ugi ( ponowna wysy³ka) - systemowy/automatyczny' DESCRIPTION,
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
----1.16	 Email do Us³ugodawcy z linkiem do resetu has³a do logowania - systemowy/automatyczny---
--------------------Powiadomienie o wygenerowaniu linku resetuj¹cego has³o------------------------
---------------------------------------OK-----------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'RESET_LINK' ID ,'Poni¿ej znajduje siê link do resetu has³a do serwisu www

{$resetLink}

Z powa¿aniem
Zespó³ ds. obs³ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs³ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K³obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     'Reset has³a' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Us³ugodawcy z linkiem do resetu has³a do logowania - systemowy/automatyczny' DESCRIPTION,
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
----1.17 Email do uczestnika , informuj¹cy o up³ywaj¹cym terminie wa¿noœci bonów. - systemowy/automatyczny----
--------------------------------------DO PODPIECIA------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'EXPI_PROD' ID ,'Sz. P. {$firstName} {$lastName},

Przypominamy, ¿e termin wa¿noœci Bonów Szkoleniowych przydzielonych w ramach projektu pn. „Kierunek Kariera” up³ywa dnia {$contractExpiryDate}.

Z powa¿aniem
Zespó³ ds. obs³ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs³ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K³obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} – przypomnienie o up³ywaj¹cym terminie wa¿noœci bonów' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do uczestnika, informuj¹cy o up³ywaj¹cym terminie wa¿noœci bonów - systemowy/automatyczny' DESCRIPTION,
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
----1.18 Email do Uczestnika o rozliczeniu bonów po terminie wa¿noœci systemowy/automatyczny------------------
--------------------------------------DO PODPIECIA------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'EXPI_REIMB' ID ,'Sz. P. {$firstName} {$lastName},

Uprzejmie informujemy, ¿e rozliczono bony w ramach projektu {$grantProgramName}, których termin wa¿noœci siê zakoñczy³.

W za³¹czeniu przesy³amy dokument potwierdzaj¹cy rozliczenie wp³aconego wk³adu w³asnego tj. notê uznaniow¹ nr{$noteNo}.

Z powa¿aniem
Zespó³ ds. obs³ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs³ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K³obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} – zwrot wk³adu w³asnego z tytu³u up³yniêcia utraty terminu wa¿noœci bonów' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika o rozliczeniu bonów po terminie wa¿noœci systemowy/automatyczny' DESCRIPTION,
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
----1.19 Email do Us³ugodawcy o przydzieleniu dostêpu do serwisu www - systemowy/automatyczny-----------------
--------------------------------------DO PODPIECIA------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'TI_ACCESS' ID ,'Szanowni Pañstwo,

Witamy w Systemie bonów szkoleniowych w ramach projektu {$grantProgramName} realizowanego przez Wojewódzki Urz¹d Pracy w Krakowie.

Rezerwacje bonów uczestników na us³ugi oraz rozliczenia us³ug realizuje siê pod adresem: {$IndividualWebAppURL}.


Login: {$login}
Link do resetu has³a: {$resetLink}

Szczegó³owe informacje dotycz¹ce systemu bonów szkoleniowych oraz jego obs³ugi dostêpne s¹ w zak³adce Pomoc.

Z powa¿aniem
Zespó³ ds. obs³ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs³ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K³obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} – dostêp do serwisu www' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Us³ugodawcy o przydzieleniu dostêpu do serwisu www - systemowy/automatyczny' DESCRIPTION,
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
----1.20	Email do Uczestnika o rozliczeniu zwrotu bonów - systemowy/automatyczny-----------------------------
--------------------------------------DO PODPIECIA------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'RET_REIMB' ID ,'Sz. P. {$firstName} {$lastName},

Uprzejmie informujemy, ¿e rozliczono zwrot bonów w ramach projektu {$grantProgramName}.

W za³¹czeniu przesy³amy dokument potwierdzaj¹cy rozliczenie wp³aconego wk³adu w³asnego tj. notê uznaniow¹ nr{$noteNo}.

Z powa¿aniem
Zespó³ ds. obs³ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs³ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K³obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} – zwrot wk³adu w³asnego z tytu³u zwrotu bonów ' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika o rozliczeniu zwrotu bonów - systemowy/automatyczny' DESCRIPTION,
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
---------1.21	Email do Uczestnika – ponowna wysy³ka kodu weryfikacyjnego –systemowy/automatyczny---
-----------------------Dane do logowania dla osoby fizycznej---------------------------------------
---------------------------------------OK----------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'VC_SEND' ID ,'Sz. P. {$firstName} {$lastName},
Witamy w Systemie bonów szkoleniowych.

Login: {$login}
Kod weryfikacyjny: {$verificationCode}

Stan przydzielonej puli bonów oraz ich rezerwacjê na us³ugi mo¿na œledziæ pod adresem: {$url}.

Z powa¿aniem
Zespó³ ds. obs³ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs³ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K³obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     'Przypomnienie kodu weryfikacyjnego' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika – ponowna wysy³ka kodu weryfikacyjnego –systemowy/automatyczny' DESCRIPTION,
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
<title>Kierunek Kariera - Wojewódzki Urz¹d Pracy Kraków</title>

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
          <td valign="top"><img src="http://cdn.sodexo.pl/gryf/mailing/header.gif" width="640" height="121" title="Ma³opolska, Urz¹d Pracy, Instytucja Województwa Ma³opolskiego Wojewódzki Urz¹d Pracy w Krakowie" style="margin:0;padding:0;border:0;"></td>
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
          <td valign="top" style="background-color: #304f87; color:#ffffff; font-size: 13px; line-height: 19px; padding: 0 29px; text-align: center;"><p style="font-size: 13px; line-height: 19px;">Wszystkie informacje o projekcie pn. "Kierunek Kariera" dostêpne s¹ na stronie:<br><a style="color:#ffc720;font-size: 13px; line-height: 19px;" href="http://www.pociagdokariery.pl" title="www.pociagdokariery.pl">www.pociagdokariery.pl</a></p><p style="font-size: 13px; line-height: 19px;"><a style="color:#ffc720;font-size: 13px; line-height: 19px;" href="http://www.facebook.com/pociagdokariery" title="www.facebook.com/pociagdokariery">www.facebook.com/pociagdokariery</a></p></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td valign="top" style="background: #ffffff;"><table cellpadding="0" cellspacing="0" border="0" align="center" width="640" style="background: #ffffff;">
        <tr>
          <td valign="top"><img src="http://cdn.sodexo.pl/gryf/mailing/footer.gif" width="640" height="100" title="Fundusze Europejskie Program Regionalny, Ma³polska, Unia Europejska Europejski Fundusz Socjalny" style="margin:0;padding:0;border:0;"></td>
        </tr>
      </table></td>
  </tr>
  <tr><td valign="top" style="background: #ffffff;"><table cellpadding="0" cellspacing="0" border="0" align="center" width="640" style="background: #ffffff;">
        <tr>
          <td valign="top" style="background-color: #f7f7f7; color:#696668; font-size: 13px; line-height: 19px; padding:0 29px; text-align: center;"><p style="font-size: 13px; line-height: 19px;">E-mail zosta³ wygenerowany automatycznie, prosimy na niego nie odpowiadaæ.</p></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td valign="top" style="background: #ffffff;"><table cellpadding="0" cellspacing="0" border="0" align="center" width="640" style="background: #ffffff;">
        <tr>
          <td valign="top" style="background-color: #ffffff; color:#696668; font-size: 11px; line-height: 15px; padding:0 29px; text-align: center;"><p style="font-size: 11px; line-height: 15px;">Wiadomoœæ zosta³a wys³ana przez Sodexo Benefits and Rewards Services Polska Sp. z o.o., ul. K³obucka 25, 02-699 Warszawa S¹d Rejonowy dla m. st. Warszawy w Warszawie, XIII Wydzia³ Gospodarczy Krajowego Rejestru S¹dowego, Kapita³ Zak³adowy 3,000,000 PLN, KRS: 0000033826, NIP: 522-23-57-343, poniewa¿ Twój adres e-mail znajduje siê na liœcie kontaktów.</p></td>
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










