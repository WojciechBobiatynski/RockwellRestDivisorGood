
-- DODANIE PROCENTU DO ZAMOWIEN
merge into APP_PBE.orders o
using APP_PBE.GRANT_PROGRAM_PARAMS gpp 
on (gpp.GRANT_PROGRAM_ID=o.GRANT_PROGRAM_ID and gpp.PARAM_ID='OWN_CONT_P')
when matched then update set o.OWN_CONTRIBUTION_PERCENTAGE = gpp.value;
