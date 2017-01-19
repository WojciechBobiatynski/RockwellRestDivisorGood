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
                                                     'Email do Uczestnika (M�P) z potwierdzeniem rozliczenia us�ugi - systemowy/automatyczny' DESCRIPTION
                                              from dual) ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
  msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
  msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);