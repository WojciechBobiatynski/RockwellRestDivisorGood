DECLARE

  v_code varchar(30);
  v_account_payment varchar(30);
  v_contract_char VARCHAR2(1) := 'Z';
  v_contract_id VARCHAR2(100);

  --   NP konto kk:    
    --   08160010718018111160000208
  --   Konto kkz:
    --   05160010710003011205015003
  --   Struktura nr. konta:
    --   00111111112222222233333333
    --   00       - CRC
    --   11111111 - KOD BANKU
    --   22222222 - NR KONTA
    --   33333333 - NR SUBKONTA
  
  -- Składanie całego numeru konta (wraz z cyfrą kontrolną) 
  -- na motywach "${eagle.schema}.t$bank_account.GET_COR_SPP(v_code)"
  FUNCTION GET_COR_KKZ(a_code VARCHAR2) RETURN VARCHAR2
  IS
     bank VARCHAR2(8)       := '16001071';
     account VARCHAR2(16)   := '00030112';
  BEGIN
     IF length(a_code) != 8 THEN
         PK_ERROR.RAISE_ERROR(-20002, 'Zly parametr a_code='||a_code);
     END IF;
     account := account || a_code;
     RETURN t$bank_account.crc(bank, account) || bank || account;
  EXCEPTION
      WHEN OTHERS THEN
        PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'BAC_GCS');
  END;

BEGIN

  FOR counter in 1..15000 
  LOOP

    -- KKR: STARE UWAGI:
        --Na razie zmienne z palca, bo skrypt jest na nasze potrzeby. W aplikacji są już zdefiniowane parametry
        -- prefix to GRYF_INDIVIDUAL_CODE_PREFIX i ma wartość 7
        -- długoś maski to też 7
        -- docelowo trzeba będzie zmienne wrzucić do ${eagle.schema}.ADM_PARAMETERES
    
    -- TODO: USTALIĆ OSTATECZNY FORMAT
    -- TODO: Zmienić counter-1 na wartość pobieraną z ${gryf.schema}.GRANT_PROGRAM_PARAMS
    
    SELECT 601 ||TO_CHAR(counter-1,'FM00000') into v_code FROM DUAL;

    -- TODO: Po potwierdzeniu numeru konta przepisać do typu, analogicznie do: ${eagle.schema}.t$bank_account.GET_COR_SPP(v_code)
    
    v_account_payment := GET_COR_KKZ(v_code);
    
    -- TODO: Ustalić czy dodajemy nową sekwencję do ustalania id kontraktu (pewnie w ${gryf.schema}.GRANT_PROGRAM_PARAMS)
    v_contract_id := counter;
    /*
    SELECT ${eagle.schema}.CNT_SEQ.NEXTVAL
    INTO   v_contract_id
    FROM   DUAL;
    */ 
    IF counter = 1 THEN
      DBMS_OUTPUT.PUT_LINE('Id kontraktu startowe: '||v_contract_id); 
    END IF;

    INSERT INTO ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS (ID, CONTRACT_ID, ACCOUNT_PAYMENT, GRANT_PROGRAM_ID, USED, VERSION, CREATED_USER, CREATED_TIMESTAMP, MODIFIED_USER, MODIFIED_TIMESTAMP)
    VALUES (${eagle.schema}.PK_SEQ.NEXTVAL, v_contract_id||v_contract_char, v_account_payment, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), '0', 1, USER, SYSDATE, USER, SYSDATE);

  END LOOP;

END;
/