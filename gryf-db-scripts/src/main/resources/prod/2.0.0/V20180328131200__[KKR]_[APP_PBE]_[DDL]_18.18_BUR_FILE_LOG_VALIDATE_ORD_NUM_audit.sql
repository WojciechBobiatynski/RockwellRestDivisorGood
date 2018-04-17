--------------------- TABLE BEGIN ------------------------

CREATE TABLE APP_PBE.TI_TRAINING_INSTANCES_EXT_AUDT AS SELECT *
                                                       FROM APP_PBE.TI_TRAINING_INSTANCES_EXT
                                                       WHERE 1 = 0;
ALTER TABLE APP_PBE.TI_TRAINING_INSTANCES_EXT_AUDT
  ADD (
  AUDIT_USER VARCHAR2(30) NOT NULL,
  AUDIT_TIMESTAMP TIMESTAMP(6) NOT NULL,
  AUDIT_OPERATION VARCHAR2(1) NOT NULL );

COMMENT ON TABLE APP_PBE.TI_TRAINING_INSTANCES_EXT_AUDT IS '@Author(KKR); @Project(WUP);@Date(2018-03-28);@Purpose(Tabela audytowa dla APP_PBE.TI_TRAINING_INSTANCES_EXT';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT_AUDT.AUDIT_USER IS 'Użytkownik wprowadzający zmiany (tworzący wiersz audytowy)';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT_AUDT.AUDIT_TIMESTAMP IS 'Timestamp wprowadzenia zmian (utworzenia wpisu w audycie)';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT_AUDT.AUDIT_OPERATION IS 'Rodzaj operacji w audycie, zmiany danych: U - Update, D - Delete';

