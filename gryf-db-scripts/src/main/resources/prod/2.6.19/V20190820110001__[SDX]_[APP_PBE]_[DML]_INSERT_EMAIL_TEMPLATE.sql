
--------------------------------------------------------------------------------------
--------------------------Email o anulowaniu rozliczenia------------------------------
--------------------------------------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'CNCL_REIMB' ID ,'Szanowni Pañstwo,

Po weryfikacji dokumentów do rozliczenia otrzymanych w dniu {$arrivalDate}, numer rozliczenia {$rmbsNumber}, rozliczenie zosta³o anulowane.

Prosimy o pilny kontakt telefoniczny.

Z powa¿aniem
Zespó³ ds. obs³ugi klienta

Operator Finansowy projektu Kierunek Kariera
Telefoniczne Biuro Obs³ugi Klienta
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. K³obucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     '{$grantProgramName} – rozliczenie anulowane' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Us³ugodawcy z informacj¹ o anulowaniu rozliczenia - systemowy/automatyczny' DESCRIPTION,
                                                     'html' EMAIL_TYPE  from dual)
                                             ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
  msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
  msg.DESCRIPTION = ins.DESCRIPTION,
  msg.EMAIL_TYPE = ins.EMAIL_TYPE
WHEN NOT MATCHED
THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION, EMAIL_TYPE) VALUES
(ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION, ins.EMAIL_TYPE);

---------------------------------------------------------------------------------------------------


MERGE INTO APP_PBE.EMAIL_TEMPLATES msg 
USING APP_PBE.EMAIL_TEMPLATES ins ON (ins.ID = 'CORR_NOTIF' AND MSG.ID='CNCL_REIMB')
WHEN MATCHED THEN
UPDATE SET msg.EMAIL_BODY_HTML_TEMPLATE = ins.EMAIL_BODY_HTML_TEMPLATE;
/

