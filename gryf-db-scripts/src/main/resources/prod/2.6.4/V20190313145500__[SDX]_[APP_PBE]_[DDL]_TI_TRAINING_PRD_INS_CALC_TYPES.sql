
CREATE TABLE ${gryf.schema}.TI_TRAINING_PRD_INS_CALC_TYPES
(
  ID       VARCHAR2(10)                         NOT NULL,
  NAME     VARCHAR2(500)                        NOT NULL,
  SERVICE  VARCHAR2(500)                        NOT NULL
)
RESULT_CACHE (MODE DEFAULT)
STORAGE    (
            BUFFER_POOL      DEFAULT
            FLASH_CACHE      DEFAULT
            CELL_FLASH_CACHE DEFAULT
           )
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
NOMONITORING;

COMMENT ON COLUMN ${gryf.schema}.TI_TRAINING_PRD_INS_CALC_TYPES.ID IS 'Klucz sposobu rozliczania godziny szkolenia';

COMMENT ON COLUMN ${gryf.schema}.TI_TRAINING_PRD_INS_CALC_TYPES.NAME IS 'Nazwa sposobu rozliczania godziny szkolenia';

COMMENT ON COLUMN ${gryf.schema}.TI_TRAINING_PRD_INS_CALC_TYPES.SERVICE IS 'Nazwa serwisu z logika rozliczania';

ALTER TABLE ${gryf.schema}.TI_TRAINING_PRD_INS_CALC_TYPES ADD (
  CONSTRAINT TI_TRAINING_CATEGORY_PROD_I_PK
  PRIMARY KEY
  (ID)
  ENABLE VALIDATE);