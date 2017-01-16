
--------------------------------------------------------------------------------------
-----------------------------------KK_ORDER-------------------------------------------
---------1.6	Email do Uczestnika o przydzieleniu bonów – systemowy/automatyczny-------
----------------------3.2	Email do OsFiz/MŒP o przydzieleniu bonów--------------------
--------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'KK_ORDER' ID ,'Sz. P. {$firstName} {$lastName},

Witamy w Systemie bonów szkoleniowych w ramach projektu {$grantProgramName} realizowanego przez Wojewódzki Urz¹d Pracy w Krakowie. W zwi¹zku z dokonaniem wp³aty wk³adu w³asnego przydzielono {$grantedVouchersNumber} elektronicznych bonów szkoleniowych.

Stan przydzielonej puli bonów oraz ich rezerwacjê na us³ugi mo¿na œledziæ pod adresem: {$IndividualWebAppURL}.

Login: {$IndividualWebAppLogin}
Has³o: {$IndividualWebAppPass}

Szczegó³owe informacje dotycz¹ce systemu bonów szkoleniowych dostêpne s¹ w zak³adce FAQ.

W za³¹czeniu przesy³amy dokument potwierdzaj¹cy dokonanie wp³aty wk³adu w³asnego tj. notê obci¹¿eniowo-ksiêgow¹ o nr: {$noteNo}.

Z powa¿aniem
Zespó³ ds. obs³ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} – przyznanie bonów szkoleniowych' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika o przydzieleniu bonów –systemowy/automatyczny' DESCRIPTION from dual)
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
Uprzejmie informujemy, ¿e rozliczono bony w ramach projektu {$grantProgramName} dotycz¹ce us³ugi {nazwa szkolenia}.

W zwi¹zku z tym, i¿ wartoœæ wp³aconego wk³adu w³asnego jest wy¿sza ni¿ wartoœæ wynikaj¹ca z rozliczenia nadp³acone œrodki zosta³y zwrócone na rachunek bankowy, z którego wp³ynê³y. W za³¹czeniu przesy³amy dokument potwierdzaj¹cy zwrot œrodków tj. notê uznaniow¹ o nr{$noteNo}.

Z powa¿aniem
Zespó³ ds. obs³ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName}– zwrot nadp³aconego wk³adu w³asnego z tytu³u rozliczenia us³ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika o rozliczeniu us³ugi - systemowy/automatyczny' DESCRIPTION
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
Uprzejmie informujemy, ¿e rozliczono bony w ramach projektu {$grantProgramName} dotycz¹ce szkolenia {$nazwa szkolenia}.

W za³¹czniku znajdzie Pan/Pani potwierdzenie zap³aty nale¿noœci za us³ugê.

Z powa¿aniem
Zespó³ ds. obs³ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName}– potwierdzenie zap³aty nale¿noœci z tytu³u rozliczenia us³ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika z potwierdzeniem zap³aty nale¿noœci za us³ugê - systemowy/automatyczny' DESCRIPTION
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

W za³¹czniku znajdzie Pan/Pani potwierdzenie rozliczenia us³ugi, tj. dokument pn. Potwierdzenie realizacji dofinansowania.

Z powa¿aniem
Zespó³ ds. obs³ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName}– potwierdzenie rozliczenia us³ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika (MŒP) z potwierdzeniem rozliczenia us³ugi - systemowy/automatyczny' DESCRIPTION
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
----1.10	Email do Us³ugodawcy z informacj¹ o koniecznoœci korekty rozliczenia szkolenia - systemowy/automatyczny---
-----------------------------3.6	Korekta rozliczenia---------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'CORR_NOTIF' ID ,'Szanowni Pañstwo,

Po weryfikacji dokumentów do rozliczenia otrzymanych w dniu {$arrivalDate}, numer rozliczenia {$rmbsNumber}, rozliczenie zosta³o zwrócone do korekty. Szczegó³owe dane dot. zidentyfikowanych nieprawid³owoœci znajd¹ Pañstwo w serwisie www.

Prosimy o korektê dokumentów rozliczeniowych w terminie 5 dni roboczych od otrzymania niniejszej  informacji.

Z powa¿aniem
Zespó³ ds. obs³ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName}– koniecznoœæ dokonania korekty w rozliczeniu us³ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Us³ugodawcy z informacj¹ o koniecznoœci korekty rozliczenia szkolenia - systemowy/automatyczny' DESCRIPTION from dual)
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
------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'PIN_SEND' ID ,'Sz. P. {$firstName} {$lastName},

Uprzejmie informujemy, i¿ zarezerwowano  us³ugê:{$trainingName}, {$trainingPlace}, {$trainingInstitutionName}, {$trainingStartDate}, liczba zarezerwowanych bonów: {$assignedProductNum}.

Poni¿ej znajduje siê kod PIN, s³u¿¹cy do rozliczenia us³ugi, który nale¿y przekazaæ us³ugodawcy w dniu zakoñczenia us³ugi. Przekazanie kodu PIN jest równoznaczne z potwierdzeniem uczestnictwa w us³udze.

PIN: {$reimbursmentPin}

Z powa¿aniem
Zespó³ ds. obs³ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} – kod PIN do rozliczenia us³ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do uczestnika z numerem PIN do us³ugi po jej rezerwacji - systemowy/automatyczny' DESCRIPTION from dual)
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
------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'PIN_RESEND' ID ,'Poni¿ej znajduje siê kod PIN, s³u¿¹cy do rozliczenia us³ugi:

{$trainingName}, {$trainingPlace}, {$trainingInstitutionName}, {$trainingStartDate}, liczba zarezerwowanych bonów: {$assignedProductNum}
który nale¿y przekazaæ us³ugodawcy w dniu zakoñczenia us³ugi. Przekazanie kodu PIN jest równoznaczne z potwierdzeniem uczestnictwa w us³udze.

PIN: {$reimbursmentPin}

Z powa¿aniem
Zespó³ ds. obs³ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} – kod PIN do rozliczenia us³ugi' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do uczestnika z numerem PIN do us³ugi ( ponowna wysy³ka) - systemowy/automatyczny' DESCRIPTION from dual)
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
--------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'RESET_LINK' ID ,'Poni¿ej znajduje siê link do resetu has³a do serwisu www

{$resetLink}

Z powa¿aniem
Zespó³ ds. obs³ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} – reset has³a' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Us³ugodawcy z linkiem do resetu has³a do logowania - systemowy/automatyczny' DESCRIPTION from dual)
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
--------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'EXPI_PROD' ID ,'Sz. P. {$firstName} {$lastName},

Przypominamy, ¿e termin wa¿noœci Bonów Szkoleniowych przydzielonych w ramach projektu pn. „Kierunek Kariera” up³ywa dnia {$contractExpiryDate}.

Z powa¿aniem
Zespó³ ds. obs³ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} – przypomnienie o up³ywaj¹cym terminie wa¿noœci bonów' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do uczestnika, informuj¹cy o up³ywaj¹cym terminie wa¿noœci bonów - systemowy/automatyczny' DESCRIPTION from dual)
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
--------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'EXPI_REIMB' ID ,'Sz. P. {$firstName} {$lastName},

Uprzejmie informujemy, ¿e rozliczono bony w ramach projektu {$grantProgramName}, których termin wa¿noœci siê zakoñczy³.

W za³¹czeniu przesy³amy dokument potwierdzaj¹cy rozliczenie wp³aconego wk³adu w³asnego tj. notê uznaniow¹ nr{$noteNo}.

Z powa¿aniem
Zespó³ ds. obs³ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName}– zwrot wk³adu w³asnego z tytu³u up³yniêcia utraty terminu wa¿noœci bonów' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika o rozliczeniu bonów po terminie wa¿noœci systemowy/automatyczny' DESCRIPTION from dual)
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
--------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'TI_ACCESS' ID ,'Szanowni Pañstwo,

Witamy w Systemie bonów szkoleniowych w ramach projektu {$grantProgramName} realizowanego przez Wojewódzki Urz¹d Pracy w Krakowie.

Rezerwacje bonów uczestników na us³ugi oraz rozliczenia us³ug realizuje siê pod adresem: {$IndividualWebAppURL}.

Login: {$login}
Has³o: {$password}

Szczegó³owe informacje dotycz¹ce systemu bonów szkoleniowych oraz jego obs³ugi dostêpne s¹ w zak³adce FAQ.

Z powa¿aniem
Zespó³ ds. obs³ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} – dostêp do serwisu www' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Us³ugodawcy o przydzieleniu dostêpu do serwisu www - systemowy/automatyczny' DESCRIPTION from dual)
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
--------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'RET_REIMB' ID ,'Sz. P. {$firstName} {$lastName},

Uprzejmie informujemy, ¿e rozliczono zwrot bonów w ramach projektu {$grantProgramName}.

W za³¹czeniu przesy³amy dokument potwierdzaj¹cy rozliczenie wp³aconego wk³adu w³asnego tj. notê uznaniow¹ nr{$noteNo}.

Z powa¿aniem
Zespó³ ds. obs³ugi klienta' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName}– zwrot wk³adu w³asnego z tytu³u zwrotu bonów ' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika o rozliczeniu zwrotu bonów - systemowy/automatyczny' DESCRIPTION from dual)
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
Zespó³ ds. obs³ugi klienta' EMAIL_BODY_TEMPLATE,

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
















