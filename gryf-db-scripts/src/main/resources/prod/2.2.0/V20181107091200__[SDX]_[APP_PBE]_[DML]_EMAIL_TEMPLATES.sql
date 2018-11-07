
--------------------------------------------------------------------------------------
-----------------------------------KK_ORDER-------------------------------------------
---------1.6	Email do Uczestnika o przydzieleniu bonów – systemowy/automatyczny-------
----------------------3.2	Email do OsFiz/MŚP o przydzieleniu bonów--------------------
--------------------------------------OK-----------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'KK_ORDER' ID ,'Sz. P. {$firstName} {$lastName},

Witamy w Systemie bonów szkoleniowych w ramach projektu {$grantProgramName} realizowanego przez Wojewódzki Urząd Pracy w Krakowie. W związku z dokonaniem wpłaty wkładu własnego przydzielono {$grantedVouchersNumber} elektronicznych bonów szkoleniowych.

Stan przydzielonej puli bonów oraz ich rezerwację na usługi można śledzić pod adresem: {$IndividualWebAppURL}.

Login: {$IndividualWebAppLogin}
Hasło: {$IndividualWebAppPass}

Szczegółowe informacje dotyczące systemu bonów szkoleniowych dostępne są w zakładce Pomoc.

W załączeniu przesyłamy dokument potwierdzający dokonanie wpłaty wkładu własnego tj. notę obciążeniowo-księgową o nr: {$noteNo}.

Z poważaniem
Zespół ds. obsługi klienta

Operator Finansowy projektu {$grantProgramName}
Telefoniczne Biuro Obsługi Klienta
Tel. {$grantProgramPhone}; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. Kłobucka 25; 02-699 Warszawa
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



---------------------------------------------------------------------------------------------------
-----------------------------------VC_SEND---------------------------------------------------------
---------1.21	Email do Uczestnika – ponowna wysyłka kodu weryfikacyjnego –systemowy/automatyczny---
-----------------------Dane do logowania dla osoby fizycznej---------------------------------------
---------------------------------------OK----------------------------------------------------------

MERGE INTO APP_PBE.EMAIL_TEMPLATES msg USING (SELECT 'VC_SEND' ID ,'Sz. P. {$firstName} {$lastName},
Witamy w Systemie bonów szkoleniowych.

Login: {$login}
Kod weryfikacyjny: {$verificationCode}

Stan przydzielonej puli bonów oraz ich rezerwację na usługi można śledzić pod adresem: {$url}.

Z poważaniem
Zespół ds. obsługi klienta

Telefoniczne Biuro Obsługi Klienta:
Operator Finansowy projektu Kierunek Kariera
Tel. 22/346-75-05; e-mail: tbok.kk@sodexo.com
Operator Finansowy projektu Kierunek Kariera Zawodowa
Tel. 22/346-75-15; e-mail: tbok.kk@sodexo.com

Sodexo Benefits and Rewards Services Polska
ul. Kłobucka 25; 02-699 Warszawa
Sodexo. World Leader in Quality of Life Services.' EMAIL_BODY_TEMPLATE,

                                                     'Przypomnienie kodu weryfikacyjnego' EMAIL_SUBJECT_TEMPLATE,
                                                     'Email do Uczestnika – ponowna wysyłka kodu weryfikacyjnego –systemowy/automatyczny' DESCRIPTION,
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


DECLARE
  large_txt CLOB;
BEGIN

  large_txt:='<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Kierunek Kariera - Wojewódzki Urząd Pracy Kraków</title>

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
          <td valign="top"><img src="http://cdn.sodexo.pl/gryf/mailing/header.gif" width="640" height="121" title="Małopolska, Urząd Pracy, Instytucja Województwa Małopolskiego Wojewódzki Urząd Pracy w Krakowie" style="margin:0;padding:0;border:0;"></td>
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
          <td valign="top" style="background-color: #304f87; color:#ffffff; font-size: 13px; line-height: 19px; padding: 0 29px; text-align: center;"><p style="font-size: 13px; line-height: 19px;">Wszystkie informacje o projektach pn. "Kierunek Kariera" i "Kierunek Kariera Zawodowa" dostępne są na stronie:<br><a style="color:#ffc720;font-size: 13px; line-height: 19px;" href="http://www.pociagdokariery.pl" title="www.pociagdokariery.pl">www.pociagdokariery.pl</a></p><p style="font-size: 13px; line-height: 19px;"><a style="color:#ffc720;font-size: 13px; line-height: 19px;" href="http://www.facebook.com/pociagdokariery" title="www.facebook.com/pociagdokariery">www.facebook.com/pociagdokariery</a></p></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td valign="top" style="background: #ffffff;"><table cellpadding="0" cellspacing="0" border="0" align="center" width="640" style="background: #ffffff;">
        <tr>
          <td valign="top"><img src="http://cdn.sodexo.pl/gryf/mailing/footer.gif" width="640" height="100" title="Fundusze Europejskie Program Regionalny, Małpolska, Unia Europejska Europejski Fundusz Socjalny" style="margin:0;padding:0;border:0;"></td>
        </tr>
      </table></td>
  </tr>
  <tr><td valign="top" style="background: #ffffff;"><table cellpadding="0" cellspacing="0" border="0" align="center" width="640" style="background: #ffffff;">
        <tr>
          <td valign="top" style="background-color: #f7f7f7; color:#696668; font-size: 13px; line-height: 19px; padding:0 29px; text-align: center;"><p style="font-size: 13px; line-height: 19px;">E-mail został wygenerowany automatycznie, prosimy na niego nie odpowiadać.</p></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td valign="top" style="background: #ffffff;"><table cellpadding="0" cellspacing="0" border="0" align="center" width="640" style="background: #ffffff;">
        <tr>
          <td valign="top" style="background-color: #ffffff; color:#696668; font-size: 11px; line-height: 15px; padding:0 29px; text-align: center;"><p style="font-size: 11px; line-height: 15px;">Wiadomość została wysłana przez Sodexo Benefits and Rewards Services Polska Sp. z o.o., ul. Kłobucka 25, 02-699 Warszawa Sąd Rejonowy dla m. st. Warszawy w Warszawie, XIII Wydział Gospodarczy Krajowego Rejestru Sądowego, Kapitał Zakładowy 3,000,000 PLN, KRS: 0000033826, NIP: 522-23-57-343, ponieważ Twój adres e-mail znajduje się na liście kontaktów.</p></td>
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
  'VC_SEND');