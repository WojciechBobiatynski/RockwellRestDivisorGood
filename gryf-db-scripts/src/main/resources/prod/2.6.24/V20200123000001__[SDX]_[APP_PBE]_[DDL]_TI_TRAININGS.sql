-----------------------------------------------------
----- APP_PBE.TI_TRAININGS --------------------------
-----------------------------------------------------

UPDATE APP_PBE.TI_TRAININGS
SET INDIVIDUAL = NULL;

ALTER TABLE APP_PBE.TI_TRAININGS
MODIFY(INDIVIDUAL  DEFAULT NULL);

ALTER TABLE APP_PBE.TI_TRAININGS
MODIFY(INDIVIDUAL NUMBER(10));

ALTER TABLE APP_PBE.TI_TRAININGS
RENAME COLUMN INDIVIDUAL TO MAX_PARTICIPANTS;