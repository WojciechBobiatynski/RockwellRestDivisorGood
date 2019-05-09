--PBE_PRODUCT_INSTANCE_POOL_E_T

INSERT INTO APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E_T (ID,
                                                   NAME,
                                                   ORDINAL,
                                                   SOURCE_TYPE)
     VALUES ('LWUSED',
             'Zmniejszenie liczby użytych bonów dla odbytego szkolenia',
             51,
             'APP_PBE.TRAINING_INSTANCES');

INSERT INTO APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E_T (ID,
                                                   NAME,
                                                   ORDINAL,
                                                   SOURCE_TYPE)
     VALUES ('CNCLREIMB',
             'Anulowanie rozliczenia - zwolnienie bonów',
             61,
             'APP_PBE.TRAINING_INSTANCES');