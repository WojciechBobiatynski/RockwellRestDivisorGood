CREATE OR REPLACE TYPE BODY eagle.T$BANK_ACCOUNT IS

CONSTRUCTOR FUNCTION T$BANK_ACCOUNT RETURN SELF AS RESULT
IS
BEGIN

   RETURN;
   
EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'BAC_CON1');
END;

MEMBER PROCEDURE SERIALIZE(a_id number, a_source IN VARCHAR2)
 IS
BEGIN
  if self.deleted# = 'Y' then
    if length(self.rowid#) > 5 then
	   
	   IF a_source = 'CUSTOMER' THEN
	   
	      DELETE FROM app_emi.customer_bank_accounts cba
		  WHERE cba.BAC_ID = a_id;
	   
	   ELSIF a_source = 'ACCEPTOR' THEN
	   
	      DELETE FROM app_rmb.acceptor_bank_accounts aba
		  WHERE aba.BAC_ID = a_id;
		  	   
	   ELSE
	      PK_ERROR.RAISE_ERROR(-200010,'Nie istnieje taki obiekt: '||a_source);	   
	   END IF;
	   
       delete from app_cor.bank_accounts
	    where rowid = chartorowid(self.rowid#);
		
	end if;
  else
    if length(self.rowid#) > 5 then
	    update app_cor.bank_accounts set
			  NUM        = self.num,
			  TYPE       = self.type,
			  NAME       = self.name,
			  BANK_NAME  = self.bank_name,
			  REMARK     = self.remark,
			  MODIFIED   = pk_utils.get_mod_string
		  where rowid = chartorowid(self.rowid#);
		if sql%notfound then
	    	insert into app_cor.bank_accounts (ID, NUM, TYPE, NAME, BANK_NAME, REMARK, CREATED, MODIFIED)
			     values(bac_seq.nextval, self.num, self.type, self.name,
				        self.bank_name, self.remark,
						pk_utils.get_mod_string, pk_utils.get_mod_string);
						
	       IF a_source = 'CUSTOMER' THEN
	       
		      INSERT INTO app_emi.CUSTOMER_BANK_ACCOUNTS (CUS_ID, BAC_ID) 
			  VALUES (a_id, bac_seq.currval);
	   
	       ELSIF a_source = 'ACCEPTOR' THEN
	   
		      INSERT INTO app_rmb.ACCEPTOR_BANK_ACCOUNTS (ACC_ID, BAC_ID) 
			  VALUES (a_id, bac_seq.currval);
		  	   
	       ELSE
	          PK_ERROR.RAISE_ERROR(-200010,'Nie istnieje taki obiekt: '||a_source);	   
	       END IF;

		end if;
	else
	    	insert into app_cor.bank_accounts (ID, NUM, TYPE, NAME, BANK_NAME, REMARK, CREATED, MODIFIED)
			     values(bac_seq.nextval, self.num, self.type, self.name,
				        self.bank_name, self.remark,
						pk_utils.get_mod_string, pk_utils.get_mod_string);
						
	       IF a_source = 'CUSTOMER' THEN
	       
		      INSERT INTO app_emi.CUSTOMER_BANK_ACCOUNTS (CUS_ID, BAC_ID) 
			  VALUES (a_id, bac_seq.currval);
	   
	       ELSIF a_source = 'ACCEPTOR' THEN
	   
		      INSERT INTO app_rmb.ACCEPTOR_BANK_ACCOUNTS (ACC_ID, BAC_ID) 
			  VALUES (a_id, bac_seq.currval);
		  	   
	       ELSE
	          PK_ERROR.RAISE_ERROR(-200010,'Nie istnieje taki obiekt: '||a_source);	   
	       END IF;
						
    end if;
  end if;
  
  PK_AUDIT.UNLOCK_ROWID('APP_COR.BANK_ACCOUNT', self.rowid#);
  
EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'BAC_SER');
END;


STATIC FUNCTION GET_NRB(a_num VARCHAR2)
 RETURN VARCHAR2
 IS
BEGIN
   RETURN (substr(a_num, 1,2)||' '||
           substr(a_num, 3,4)||' '||
		   substr(a_num, 7,4)||' '||
		   substr(a_num,11,4)||' '||
		   substr(a_num,15,4)||' '||
		   substr(a_num,19,4)||' '||
		   substr(a_num,23,4));
EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'BAC_GET_NRB');
END;


STATIC FUNCTION GET_TYPE(A_TYPE VARCHAR2)
 RETURN VARCHAR2
 IS
  v_type   varchar2(100);
BEGIN
	   select name into v_type
	     from app_cor.bank_account_types
		where type = a_type;
   RETURN v_type;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
	  RETURN NULL;
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'BAC_GET_TYPE');
END;

STATIC PROCEDURE GET_BANK_ACCOUNT_TYPES_LIST(A_LIST IN OUT TR$STRINGS) IS
    i NUMBER := 1;
BEGIN
  FOR r IN (SELECT * FROM APP_COR.BANK_ACCOUNT_TYPES)
  LOOP
	a_list.extend;
	a_list(a_list.last) := R$STRINGS(r.type, r.name,null,null,null);
  END LOOP;
EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'BAC_GET_TYPES');
END;

STATIC FUNCTION CRC(bank_no VARCHAR2, account_no VARCHAR2) RETURN VARCHAR2
IS
   crc VARCHAR2(2);
   str VARCHAR2(30);
BEGIN
  
  str := bank_no || account_no || '252100';
  
  crc := 98 - mod(str, 97);
  crc := lpad(crc, 2,'0');
  
  RETURN crc;
  
EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'BAC_CRC');
END;

STATIC FUNCTION GET_COR_SPP(a_code VARCHAR2) RETURN VARCHAR2
IS
   bank VARCHAR2(8)      := '16001071';
   account VARCHAR2(16)	 := '80181111';
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

STATIC FUNCTION CHECK_NRB(A_NUM VARCHAR2) RETURN BOOLEAN
IS
   TYPE t_arr IS TABLE OF NUMBER(2); 
   v_nrb VARCHAR2(30);
   v_z   NUMBER;
   v_w  t_arr;
BEGIN
   
   IF a_num IS NULL THEN
      RETURN TRUE;
   END IF;

  PK_UTILS.Is_number(a_num, 'Numer konta');
  
  IF (length(a_num) = 1) AND
     (a_num = '0') THEN
	 
	 RETURN TRUE;
	 
  END IF;
  
  IF length(a_num) != 26 THEN
     PK_ERROR.RAISE_ERROR(-20001,'B³êdny NRB: '||a_num,'BAC_NRB');
  END IF;
  
  v_w := t_arr(1,10,3,30,9,90,27,76,81,34,49,5,50,15,53,45,62,38,89,17,
               73,51,25,56,75,71,31,19,93,57);

  v_nrb := a_num || '2521';
  v_nrb := substr(v_nrb,3) || substr(v_nrb,1,2);
  
    v_z := 0;    
    FOR i IN 1..30
    LOOP
       v_z := v_z + to_number(substr(v_nrb,30-i+1,1)) * v_w(i);
    END LOOP;
        
    IF mod(v_z, 97) != 1 THEN
       PK_ERROR.RAISE_ERROR(-20002,'B³êdny NRB: '||a_num,'BAC_NRB');
    END IF;
  
  
  RETURN TRUE;
EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'BAC_NRB');
END;

MEMBER PROCEDURE UNLOCK_
IS
BEGIN

   PK_AUDIT.UNLOCK_ROWID('APP_COR.BANK_ACCOUNT', SELF.rowid#);

EXCEPTION
   WHEN OTHERS THEN
	 PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'BAC_ULO');
END;

STATIC FUNCTION GET_COR_SPP_WUP(a_grand_program_id IN VARCHAR2, a_code VARCHAR2) RETURN VARCHAR2
IS
   bank VARCHAR2(8)       := '16001071';
   account VARCHAR2(16)   := '80181111';
   
  FUNCTION GET_PARAM_VALUE(a_parameter_name IN VARCHAR2,
                           a_grand_program_id     IN NUMBER) RETURN VARCHAR2 IS
    v_value APP_PBE.GRANT_PROGRAM_PARAMS.VALUE%TYPE;
  BEGIN
    null;
    SELECT VALUE
    INTO   v_value
    FROM   APP_PBE.GRANT_PROGRAM_PARAMS P
    WHERE  GRANT_PROGRAM_ID = a_grand_program_id
    AND    PARAM_ID         = a_parameter_name;
    RETURN v_value;
  end;
   
   
BEGIN

   account := GET_PARAM_VALUE('AC_PAY_PRT',a_grand_program_id);
   
   IF length(a_code) != 8 THEN
       PK_ERROR.RAISE_ERROR(-20002, 'Zly parametr a_code='||a_code);
   END IF;

   account := account || a_code;

   RETURN t$bank_account.crc(bank, account) || bank || account;
   
EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'BAC_GCS');
END;

END;
/
