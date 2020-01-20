-- dodanie procentu do rozliczen

MERGE INTO APP_PBE.E_REIMBURSEMENTS ER
     USING (SELECT ti.id, gpp.VALUE
              FROM APP_PBE.TI_TRAINING_INSTANCES  TI
                   JOIN APP_PBE.GRANT_PROGRAM_PARAMS gpp
                       ON     gpp.GRANT_PROGRAM_ID = TI.GRANT_PROGRAM_ID
                          AND gpp.PARAM_ID = 'OWN_CONT_P') tti
        ON (    tTI.ID = ER.TI_TR_INST_ID)
WHEN MATCHED
THEN
    UPDATE SET er.OWN_CONTRIBUTION_PERCENTAGE = tti.VALUE;
	
MERGE INTO APP_PBE.E_REIMBURSEMENTS ER
     USING (SELECT pip.id, gpp.VALUE
              FROM APP_PBE.PBE_PRODUCT_INSTANCE_POOLS  pip
                   JOIN APP_PBE.ORDERS o ON o.ID = pip.ORDER_ID
                   JOIN APP_PBE.GRANT_PROGRAM_PARAMS gpp
                       ON     gpp.GRANT_PROGRAM_ID = o.GRANT_PROGRAM_ID
                          AND gpp.PARAM_ID = 'OWN_CONT_P') a
        ON (a.id = ER.PRODUCT_INSTANCE_POOL_ID)
WHEN MATCHED
THEN
    UPDATE SET er.OWN_CONTRIBUTION_PERCENTAGE = a.VALUE	;