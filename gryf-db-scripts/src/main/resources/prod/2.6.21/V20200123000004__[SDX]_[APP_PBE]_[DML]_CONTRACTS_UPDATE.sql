-- DODANIE PROCENTU DO UMOW
merge into APP_PBE.CONTRACTS c
using APP_PBE.GRANT_PROGRAM_PARAMS gpp 
on (gpp.GRANT_PROGRAM_ID=c.GRANT_PROGRAM_ID and gpp.PARAM_ID='OWN_CONT_P')
when matched then update set c.OWN_CONTRIBUTION_PERCENTAGE = gpp.value;