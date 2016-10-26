-- PBE_TRAINING -> TI_TRAININGS
ALTER INDEX APP_PBE.PBE_TRA_PK RENAME TO TI_TRA_PK;
ALTER TABLE APP_PBE.PBE_TRAINING RENAME TO TI_TRAININGS;

ALTER TABLE APP_PBE.TI_TRAININGS RENAME CONSTRAINT PBE_TRA_PK TO TI_TRA_PK;
ALTER TABLE APP_PBE.TI_TRAININGS RENAME CONSTRAINT PBE_TRA_CAT_DIC_FK TO TRA_CAT_DIC_FK;
ALTER TABLE APP_PBE.TI_TRAININGS RENAME CONSTRAINT PBE_TRIN_FK TO TRIN_FK;

-- PBE_TRAINING_CATEGORY_DICS -> TI_TRAINING_CATEGORY_DICS
ALTER INDEX APP_PBE.PBE_TRA_CAT_DIC_PK RENAME TO TRA_CAT_DIC_PK;
ALTER TABLE APP_PBE.PBE_TRAINING_CATEGORY_DICS RENAME TO TI_TRAINING_CATEGORY_DICS;

ALTER TABLE APP_PBE.TI_TRAINING_CATEGORY_DICS RENAME CONSTRAINT PBE_TRA_CAT_DIC_PKP TO TRA_CAT_DIC_PK;