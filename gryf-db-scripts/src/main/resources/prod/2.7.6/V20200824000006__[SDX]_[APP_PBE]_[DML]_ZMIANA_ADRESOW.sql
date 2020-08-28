update APP_PBE.EMAIL_TEMPLATES et
set et.EMAIL_BODY_TEMPLATE=replace(et.EMAIL_BODY_TEMPLATE,'Kłobucka 25','Rzymowskiego 53')
where et.EMAIL_BODY_TEMPLATE like '%Kłobucka 25%';

update APP_PBE.EMAIL_TEMPLATES et
set et.EMAIL_BODY_TEMPLATE=replace(et.EMAIL_BODY_TEMPLATE,'02-699 Warszawa','02-697 Warszawa')
where et.EMAIL_BODY_TEMPLATE like '%02-699 Warszawa%';

update APP_PBE.EMAIL_TEMPLATES et
set et.EMAIL_BODY_HTML_TEMPLATE=replace(et.EMAIL_BODY_HTML_TEMPLATE,'Kłobucka 25','Rzymowskiego 53')
where et.EMAIL_BODY_HTML_TEMPLATE like '%Kłobucka 25%';

update APP_PBE.EMAIL_TEMPLATES et
set et.EMAIL_BODY_HTML_TEMPLATE=replace(et.EMAIL_BODY_HTML_TEMPLATE,'02-699 Warszawa','02-697 Warszawa')
where et.EMAIL_BODY_HTML_TEMPLATE like '%02-699 Warszawa%';