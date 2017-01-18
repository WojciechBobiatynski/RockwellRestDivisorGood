Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('WDEPNUM_KK','Nota przysz�ych nale�no�ci WUP','ATTR_V','MAX_LENGTH=50; ','Dodatkowy numer dokumentu');
Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('WDEPEXP_KK','NPNW wyeksportowana do FK','CONFCHKBOX',null,'Dodatkowy numer dokumentu wyeksportowany do NAVISION');

Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('DOCGENKK',    'WDEPNUM_KK',41, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PROCESSDKK',  'WDEPNUM_KK',41, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('SENTKK',      'WDEPNUM_KK',41, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('EKSPORTKK',   'WDEPNUM_KK',41, null, null,null);

Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('DOCGENKK',    'WDEPEXP_KK',42, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PROCESSDKK',  'WDEPEXP_KK',42, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('SENTKK',      'WDEPEXP_KK',42, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('EKSPORTKK',   'WDEPEXP_KK',42, null, null,null);

update APP_PBE.ORDER_FLOW_ELEMENTS
set ELEMENT_TYPE_PARAMS = 'SQL_EXPRESSION=select ''BRAK_WP'', ''Nie wp�acono wp�aty w�asnej'' from dual union select ''INNE'', ''Inne''  from dual'
where ELEMENT_ID = 'CANREAS_KK';