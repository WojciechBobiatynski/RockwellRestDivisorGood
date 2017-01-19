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
                                                     'Email do Uczestnika (MŒP) z potwierdzeniem rozliczenia us³ugi - systemowy/automatyczny' DESCRIPTION
                                              from dual) ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
  msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
  msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);