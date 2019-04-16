-- Tworzenie użytkownika IS dla danego użytkownika Opertora
-- Do wprowadzenia:
  -- v_adm_user_id - id użytkownika Operatora w EAGLE
  -- v_ti_user_email - mail użytkownika, którego tworzymy

DECLARE
  -- These are required inputs: Operator login, email
  v_adm_user_id EAGLE.ADM_USERS.ID%TYPE := 'ADM_DEV1';
  v_ti_user_email APP_PBE.TRAINING_INSTITUTION_USERS.EMAIL%TYPE := 'user@sodexo.com';

  PROCEDURE add_ti_user(a_adm_user IN EAGLE.ADM_USERS.ID%TYPE,
                        a_email    IN APP_PBE.TRAINING_INSTITUTION_USERS.EMAIL%TYPE) IS
    v_default_ti_id NUMBER;
    v_ti_usr_id NUMBER;
  BEGIN
    SELECT VALUE INTO v_default_ti_id FROM EAGLE.ADM_PARAMETERS
    WHERE UPPER(NAME) = 'DEFAULT_TI';

    v_ti_usr_id := EAGLE.TI_USR_SEQ.nextval;

    -- Add TI USER:
    -- EMAIL = LOGIN = A_EMAIL
    -- PASSWORD - BCrypt hash for '123'
    INSERT INTO APP_PBE.TRAINING_INSTITUTION_USERS
      (ID,
       TRAINING_ISTITUTION_ID,
       LOGIN,
       EMAIL,
       PASSWORD,
       IS_ACTIVE,
       CREATED_USER,
       CREATED_TIMESTAMP,
       MODIFIED_USER,
       MODIFIED_TIMESTAMP)
    VALUES (v_ti_usr_id,
            DEFAULT_TI_ID,
            a_email,
            a_email,
            '$2a$10$EHF2BS3SzO406vByOd1MmeuQjpQMT9NhknfzYjqTA3PlknbX6DcAW',
            1,
            USER,
            SYSDATE,
            USER,
            SYSDATE);

    -- Add roles for TI user
    INSERT INTO APP_PBE.TI_USER_IN_ROLES (TI_USER_ID, TE_ROLE_CODE)
    VALUES (v_ti_usr_id, 'TI_TEST_ROLE');
    INSERT INTO APP_PBE.TI_USER_IN_ROLES (TI_USER_ID, TE_ROLE_CODE)
    VALUES (v_ti_usr_id, 'TI_MAIN_ROLE');

    -- Add relation to ADM_USER
    INSERT INTO EAGLE.ADM_USER_PARAMETERS (AUR_ID, CODE, VALUE)
    VALUES (a_adm_user, 'TI_USER_ID', v_ti_usr_id);
  END;
BEGIN
  add_ti_user(v_adm_user_id, v_ti_user_email);
END;


-- Sprawdzenie czy użytkownik został stworzony
-- Wprowadź v_adm_user - id użytkownika Opertora w EAGLE
DECLARE
  v_adm_user EAGLE.ADM_USERS.ID%TYPE := 'ADM_DEV1';
  v_user_param EAGLE.ADM_USER_PARAMETERS.VALUE%TYPE;
  v_ti_user_id APP_PBE.TRAINING_INSTITUTION_USERS.ID%TYPE;
  v_ti_user_email APP_PBE.TRAINING_INSTITUTION_USERS.EMAIL%TYPE;
  v_ti_name APP_PBE.TRAINING_INSTITUTIONS.NAME%TYPE;
BEGIN
  SELECT VALUE INTO v_user_param FROM EAGLE.ADM_USER_PARAMETERS
  WHERE AUR_ID = UPPER(v_adm_user)
    AND CODE = ('TI_USER_ID');
  DBMS_OUTPUT.PUT_LINE('ADM_PARAM=' || v_user_param);

  SELECT TIU.ID,
         TIU.EMAIL,
         TI.NAME
  INTO v_ti_user_id, v_ti_user_email, v_ti_name
  FROM APP_PBE.TRAINING_INSTITUTION_USERS TIU
    JOIN APP_PBE.TRAINING_INSTITUTIONS TI
      ON TIU.TRAINING_ISTITUTION_ID = TI.ID
  WHERE TIU.ID = TO_NUMBER(v_user_param);
  DBMS_OUTPUT.PUT_LINE('TI_USER_ID="' || v_ti_user_id ||
                       '", TI_USER_EMAIL="' || v_ti_user_email ||
                       '", TI_NAME="' || v_ti_name || '"');
END;
