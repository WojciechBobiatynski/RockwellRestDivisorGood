Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('WDEPNUM_KK','Dodatkowy numer dokumentu','ATTR_V','MAX_LENGTH=50; ','Dodatkowy numer dokumentu');
--TODO: tbilski zminic labelke

Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('DOCGENKK',    'WDEPNUM_KK',41, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PROCESSDKK',  'WDEPNUM_KK',41, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('SENTKK',      'WDEPNUM_KK',41, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('EKSPORTKK',   'WDEPNUM_KK',41, null, null,null);

