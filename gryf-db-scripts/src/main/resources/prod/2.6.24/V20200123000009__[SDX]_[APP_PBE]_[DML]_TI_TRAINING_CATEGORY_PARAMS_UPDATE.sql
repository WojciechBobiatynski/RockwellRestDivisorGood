--AKTUALIZACJA DATY DO DLA STARYCH PARAMETRÓW
UPDATE APP_PBE.TI_TRAINING_CATEGORY_PARAMS
SET APP_PBE.TI_TRAINING_CATEGORY_PARAMS.DATE_TO=TO_DATE('2020-06-30','YYYY-MM-DD')
WHERE APP_PBE.TI_TRAINING_CATEGORY_PARAMS.CATEGORY_ID NOT IN ('EGZ','SPD', 'EGZKZ', 'KFW');


UPDATE APP_PBE.TI_TRAINING_CATEGORY_PARAMS
SET APP_PBE.TI_TRAINING_CATEGORY_PARAMS.MIN_PARTICIPANTS=2
WHERE APP_PBE.TI_TRAINING_CATEGORY_PARAMS.CATEGORY_ID IN ('JAZ','KCY');