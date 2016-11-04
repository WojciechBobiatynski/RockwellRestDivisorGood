MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'VC_SEND' ID ,'Szanowny Panie/Szanowna Pani
Poni¿ej znajduj¹ siê dane logowania do naszego serwisu

Adres: {$url}
Login: {$login}
Has³o: {$verificationCode}

Z powa¿aniem
Zespó³ ds. obs³ugi klienta' EMAIL_BODY_TEMPLATE, 'Dane do logowania' EMAIL_SUBJECT_TEMPLATE, 'Dane do logowania dla osoby fizycznej' DESCRIPTION from dual) ins ON (msg.ID = ins.ID) WHEN MATCHED THEN UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE, msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE, msg.DESCRIPTION = ins.DESCRIPTION WHEN NOT MATCHED THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES (ins.ID, ins.EMAIL_BODY_TEMPLATE, ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);