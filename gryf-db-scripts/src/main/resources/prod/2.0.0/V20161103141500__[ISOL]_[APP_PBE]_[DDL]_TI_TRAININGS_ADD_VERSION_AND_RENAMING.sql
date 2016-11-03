ALTER TABLE APP_PBE.TI_TRAININGS RENAME COLUMN TRA_ID TO ID;
ALTER TABLE APP_PBE.TI_TRAININGS RENAME COLUMN TRIN_ID TO TRAINING_INSTITUTION_ID;
ALTER TABLE APP_PBE.TI_TRAININGS RENAME COLUMN TRC_CODE TO TRAINING_CATEGORY_ID;

ALTER TABLE APP_PBE.TI_TRAININGS ADD VERSION NUMBER;
COMMENT ON COLUMN APP_PBE.TI_TRAININGS.VERSION IS 'Wersja. Kolumna techniczna u�ywana do mechanizmu Optimistic Lock';