MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'CORR_NOTIF' ID ,'Szanowni Pañstwo

Po weryfikacji dokumentów do rozliczenia otrzymanych w dniu {$arrivalDate}, numer rozliczenia {$rmbsNumber}, rozliczenie zosta³o zwrócone do korekty, dane szczegó³owe na temat zidentyfikowanych niezgodnoœci znajd¹ Pañstwo w serwisie.
Wzywamy do uzupe³nienia brakuj¹cych dokumentów w ci¹gu 5 dni roboczych.' EMAIL_BODY_TEMPLATE,
 'Rozliczenie zwrócone do korekty w ramach programu dofinansowania: {$grantProgramName}' EMAIL_SUBJECT_TEMPLATE,
 'Informacja o korekcie rozliczenia' DESCRIPTION
from dual) ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
  UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
    msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
    msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
  THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
    (ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);
