MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'CORR_NOTIF' ID ,'Szanowni Pa�stwo

Po weryfikacji dokument�w do rozliczenia otrzymanych w dniu {$arrivalDate}, numer rozliczenia {$rmbsNumber}, rozliczenie zosta�o zwr�cone do korekty, dane szczeg�owe na temat zidentyfikowanych niezgodno�ci znajd� Pa�stwo w serwisie.
Wzywamy do uzupe�nienia brakuj�cych dokument�w w ci�gu 5 dni roboczych.' EMAIL_BODY_TEMPLATE,
 'Rozliczenie zwr�cone do korekty w ramach programu dofinansowania: {$grantProgramName}' EMAIL_SUBJECT_TEMPLATE,
 'Informacja o korekcie rozliczenia' DESCRIPTION
from dual) ins ON (msg.ID = ins.ID)
WHEN MATCHED THEN
  UPDATE SET msg.EMAIL_BODY_TEMPLATE = ins.EMAIL_BODY_TEMPLATE,
    msg.EMAIL_SUBJECT_TEMPLATE = ins.EMAIL_SUBJECT_TEMPLATE,
    msg.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED
  THEN INSERT (ID, EMAIL_BODY_TEMPLATE, EMAIL_SUBJECT_TEMPLATE, DESCRIPTION) VALUES
    (ins.ID, ins.EMAIL_BODY_TEMPLATE,ins.EMAIL_SUBJECT_TEMPLATE, ins.DESCRIPTION);
