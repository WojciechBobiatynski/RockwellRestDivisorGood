
Insert into APP_PBE.GRANT_PROGRAM_PARAM_TYPES (ID, NAME, DESCRIPRTION) values ('ENROLL_GAP','Dni do rezewacji szkolenia',
'= 0 – mogê rezerwowaæ przed rozpoczêciem oraz w dniu szkolenia
< 0 – mogê rezerwowaæ przed rozpoczêciem i po rozpoczêciu szkolenia np. dla wartoœci -2 a¿ do 2ch dni po rozpoczêciu
> 0 – mogê rezerwowaæ wy³¹cznie przed rozpoczêciem szkolenia np. dla wartoœci 3 do 3ch dni przed rozpoczêciem');

Insert into APP_PBE.GRANT_PROGRAM_PARAMS (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM, DATE_TO) values (12, 100, 'ENROLL_GAP', '0', null, null);