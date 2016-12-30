DELETE FROM APP_PBE.ACCOUNT_CONTRACT_PAIRS;

DECLARE
v_code varchar(30);
v_account_payment varchar(30);
TI_ID number;
USR_ID number;

BEGIN

FOR counter in 1..1100 LOOP

  --Na razie zmienne z palca, bo skrypt jest na nasze potrzeby. W aplikacji s¹ ju¿ zdefiniowane parametry
  -- prefix to GRYF_INDIVIDUAL_CODE_PREFIX i ma wartoœæ 7
  -- d³ugoœ maski to te¿ 7
  -- docelowo trzeba bêdzie zmienne wrzuciæ do EAGLE.ADM_PARAMETERES
  SELECT 6 ||TO_CHAR(EAGLE.IND_SEQ.NEXTVAL,'FM0000000') into v_code FROM DUAL;

  SELECT eagle.t$bank_account.GET_COR_SPP(v_code) into v_account_payment FROM dual;

  INSERT INTO APP_PBE.ACCOUNT_CONTRACT_PAIRS (ID, CONTRACT_ID, ACCOUNT_PAYMENT, GRANT_PROGRAM_ID, USED, VERSION, CREATED_USER, CREATED_TIMESTAMP, MODIFIED_USER, MODIFIED_TIMESTAMP)
  VALUES (EAGLE.PK_SEQ.NEXTVAL, EAGLE.CNT_SEQ.NEXTVAL, v_account_payment, 100, '0', 1, 'GRYF', SYSDATE, 'GRYF', SYSDATE);

END LOOP;

END;
/