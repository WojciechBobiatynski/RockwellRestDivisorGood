
Insert into APP_PBE.GRANT_PROGRAM_PARAM_TYPES (ID, NAME, DESCRIPRTION) values ('ENROLL_GAP','Dni do rezewacji szkolenia',
'= 0 � mog� rezerwowa� przed rozpocz�ciem oraz w dniu szkolenia
< 0 � mog� rezerwowa� przed rozpocz�ciem i po rozpocz�ciu szkolenia np. dla warto�ci -2 a� do 2ch dni po rozpocz�ciu
> 0 � mog� rezerwowa� wy��cznie przed rozpocz�ciem szkolenia np. dla warto�ci 3 do 3ch dni przed rozpocz�ciem');

Insert into APP_PBE.GRANT_PROGRAM_PARAMS (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM, DATE_TO) values (12, 100, 'ENROLL_GAP', '0', null, null);