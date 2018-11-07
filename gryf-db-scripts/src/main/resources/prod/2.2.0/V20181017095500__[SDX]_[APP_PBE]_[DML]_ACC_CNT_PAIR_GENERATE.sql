DECLARE

  v_code varchar(30);
  v_code_no  NUMBER;
  v_account_payment varchar(30);
  v_contract_char VARCHAR2(1) := 'Z';
  v_contract_id VARCHAR2(100);
  v_code_prefix   VARCHAR2(100);
  v_program_id    NUMBER :=  NULL;

  FUNCTION GET_NEXT_NUM(a_parameter_name IN VARCHAR2,
                        a_program_id     IN NUMBER) RETURN NUMBER IS
    v_num NUMBER;
  BEGIN
    null;
    SELECT VALUE
    INTO   v_num
    FROM   APP_PBE.GRANT_PROGRAM_PARAMS P
    WHERE  GRANT_PROGRAM_ID = a_program_id
    AND    PARAM_ID         = a_parameter_name
    FOR UPDATE NOWAIT;
    v_num := v_num+1;
    UPDATE  APP_PBE.GRANT_PROGRAM_PARAMS P
    SET    VALUE = v_num
    WHERE  GRANT_PROGRAM_ID = a_program_id
    AND    PARAM_ID         = a_parameter_name;
    RETURN v_num;
  end;
  FUNCTION GET_PARAM_VALUE(a_parameter_name IN VARCHAR2,
                           a_program_id     IN NUMBER) RETURN VARCHAR2 IS
    v_value APP_PBE.GRANT_PROGRAM_PARAMS.VALUE%TYPE;
  BEGIN
    null;
    SELECT VALUE
    INTO   v_value
    FROM   APP_PBE.GRANT_PROGRAM_PARAMS P
    WHERE  GRANT_PROGRAM_ID = a_program_id
    AND    PARAM_ID         = a_parameter_name;
    RETURN v_value;
  end;
BEGIN

  SELECT ID INTO v_program_id FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}';

  v_code_prefix := GET_PARAM_VALUE( 'CODE_PRFX', v_program_id);
  v_contract_char := GET_PARAM_VALUE( 'CTR_PSTFIX', v_program_id);

  FOR counter in 1..15000
  LOOP

    v_code_no := GET_NEXT_NUM('CODE_LST_N', v_program_id);

    SELECT v_code_prefix ||TO_CHAR(v_code_no,'FM'||LPAD('0',8-LENGTH(v_code_prefix),'0')) into v_code FROM DUAL;

    v_account_payment := ${eagle.schema}.t$bank_account.GET_COR_SPP_WUP(a_grand_program_id => v_program_id, a_code => v_code);

    v_contract_id := GET_NEXT_NUM('CTR_LST_ID', v_program_id);

    IF counter = 1 THEN
      DBMS_OUTPUT.PUT_LINE('Id kontraktu startowe: '||v_contract_id);
    END IF;

    INSERT INTO ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS (ID, CONTRACT_ID, ACCOUNT_PAYMENT, GRANT_PROGRAM_ID, USED, VERSION, CREATED_USER, CREATED_TIMESTAMP, MODIFIED_USER, MODIFIED_TIMESTAMP)
    VALUES (${eagle.schema}.PK_SEQ.NEXTVAL, v_contract_id||v_contract_char, v_account_payment, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), '0', 1, USER, SYSDATE, USER, SYSDATE);

  END LOOP;

END;
/
