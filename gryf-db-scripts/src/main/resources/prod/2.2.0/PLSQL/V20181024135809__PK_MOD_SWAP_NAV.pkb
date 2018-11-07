CREATE OR REPLACE PACKAGE BODY EAGLE.pk_mod_swap_nav
AS

  TYPE t_emails 
   IS TABLE OF VARCHAR2(50)
      INDEX BY PLS_INTEGER;

  FUNCTION TBL_NVL(a_emails t_emails, a_idx NUMBER, a_null_val VARCHAR2 DEFAULT NULL) RETURN VARCHAR2
  IS
  BEGIN
    IF a_emails.exists(a_idx) THEN 
      RETURN a_emails(a_idx);  
    END IF;
    RETURN a_null_val;
  END TBL_NVL;
  
PROCEDURE EXPORT_DOCUMENTS(a_input_date      IN DATE,
                           a_cus_tsk_id      IN NUMBER,
                           a_cus_tsk_all_id  IN NUMBER)
IS
BEGIN

  PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','EXPORT_DOCUMENTS',NULL);

  PK_AUDIT.LOCK_OBJECT('APP_SWP.NAV_CUST');
       
  DELETE FROM APP_SWP.NAV_CUST;
  
  CUST_CLIENT(a_input_date, a_cus_tsk_id, a_cus_tsk_all_id);
  CUST_AFF(a_input_date, a_cus_tsk_id, a_cus_tsk_all_id);
  External_Customers(a_input_date, a_cus_tsk_id, a_cus_tsk_all_id);
  Pbe_Individuals_Enterprises(a_input_date, a_cus_tsk_id, a_cus_tsk_all_id);
  Pbe_Training_Institutions(a_input_date, a_cus_tsk_id, a_cus_tsk_all_id);
       
  PK_AUDIT.UNLOCK_OBJECT('APP_SWP.NAV_CUST');
       
  PK_AUDIT.LOCK_OBJECT('APP_SWP.NAV_INV');
       
  DELETE FROM APP_SWP.NAV_INV;

  CLIENTS_INV(a_input_date, 'C', 'ORD');
  CLIENTS_INV(a_input_date, 'C', 'TSK');
  CLIENTS_INV(a_input_date, 'C', PK_MOD_INV_PROPOSALS.GC_INV_SRC_TYPE_IP);
  CLIENTS_INV(a_input_date, 'K', 'ORD');
  CLIENTS_INV(a_input_date, 'K', 'TSK');
  CLIENTS_INV(a_input_date, 'K', PK_MOD_INV_PROPOSALS.GC_INV_SRC_TYPE_IP);
     
  CLIENTS_INV_VB(a_input_date, 'C', 'ORDV');
  CLIENTS_INV_VB(a_input_date, 'K', 'ORDV');
     
  CLIENTS_NOTES(a_input_date, 'N', 'ORD');
  CLIENTS_NOTES(a_input_date, 'N', 'TSK');
  CLIENTS_NOTES(a_input_date, 'N', PK_MOD_INV_PROPOSALS.GC_INV_SRC_TYPE_IP);
  CLIENTS_NOTES(a_input_date, 'NU', 'REM');
  CLIENTS_NOTES(a_input_date, 'M', 'ORD');
  CLIENTS_NOTES(a_input_date, 'NPB', 'ORDPB');
          
  AFF_INV(a_input_date, 'A');
  AFF_INV(a_input_date, 'B');

  Acc_Reck_Inv(a_input_date);
  Acc_Reck_Ev(a_input_date);   
          
  External_Customer_Invoices(a_input_date);
          
  AFF_REIMB(a_input_date, 'R');
  AFF_REIMB(a_input_date, 'S');
          
  TNT_REINV(a_input_date, 'T');
  TNT_REINV(a_input_date, 'U');

  Pbe_Reimb (a_input_date);
  Pbe_Rmb_Note (a_input_date);

  PK_AUDIT.UNLOCK_OBJECT('APP_SWP.NAV_INV');

EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_EXD');
END;

FUNCTION GET_ACCOUNT(a_acc_name VARCHAR2) RETURN r_acc
IS
   v_retval r_acc;
BEGIN

   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','GET_ACCOUNT',a_acc_name);
   
   SELECT acc_no, PROJECT
     INTO v_retval.ACC_NO, v_retval.PROJECT
     FROM app_fin.scala_accounts
    WHERE UPPER (acc_name) = a_acc_name;
    
    RETURN v_retval;

EXCEPTION
    WHEN no_data_found THEN
      PK_ERROR.RAISE_ERROR(-20001, 'NAV_GAC: Nieznany account='||a_acc_name);
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,a_acc_name||':'||sqlerrm,'SWP_NAV_GAC');
END;

FUNCTION GET_PRD_ACCOUNT(a_acc_name VARCHAR2, a_prd_type IN VARCHAR2) RETURN r_acc
IS
   v_retval r_acc;
BEGIN

   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','GET_PRD_ACCOUNT',NULL);
   
   SELECT acc_no, project
     INTO v_retval.acc_no, v_retval.project
     FROM app_fin.scala_accounts
    WHERE UPPER (acc_name) = a_acc_name
      AND prd_type = a_prd_type;
    
    RETURN v_retval;

EXCEPTION
    WHEN no_data_found THEN
      PK_ERROR.RAISE_ERROR(-20001, 'NAV_GPAC: Nieznany account='||a_acc_name||' pty='||a_prd_type);
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_GPAC');
END;

FUNCTION GET_PRD_ACCOUNT(a_acc_name VARCHAR2, a_prd_type IN VARCHAR2, a_exp_date IN DATE) RETURN r_acc
IS
   v_retval r_acc;
BEGIN

   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','GET_PRD_ACCOUNT2',NULL);
   
   SELECT acc_no, project
     INTO v_retval.acc_no, v_retval.project
     FROM app_fin.scala_accounts a
    WHERE UPPER (acc_name) = a_acc_name
      AND prd_type = a_prd_type
      AND a.prd_exp_date = a_exp_date;
    
    RETURN v_retval;

EXCEPTION
    WHEN no_data_found THEN
      PK_ERROR.RAISE_ERROR(-20001, 'NAV_GPAC2: Nieznany account='||a_acc_name||' pty='||a_prd_type||
      ' d='||to_char(a_exp_date,'YYYY-MM-DD'));
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_GPAC2');
END;

PROCEDURE CUST_CLIENT(a_input_date DATE,
                         a_task_id IN NUMBER,
                         a_task_all_id IN NUMBER)
IS
   v_acc_no r_acc;
   vt_efv   t_emails;
BEGIN

   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','EXP_CUST_CLIENT',NULL);
   
   v_acc_no := GET_ACCOUNT('CLIENT_ACCOUNT');
   
    /* Wszystkie */
    FOR i IN (SELECT DISTINCT RTRIM (c.code) code,
                    c.act_name, C.COMMERCIAL_NAME, c.ad_line_1,
                    m.ID CITY_CODE, m.NAME city_name,
                    c.vat_reg_num,
                    (SELECT MAX (con1.DATA)
                       FROM app_emi.customers c1,
                            app_emi.customer_contacts cc1,
                            app_cor.contacts con1
                      WHERE c1.ID = cc1.cus_id
                        AND cc1.con_id = con1.ID
                        AND con1.TYPE = 'P'
                        AND c1.ID = c.ID) phone,
                    DECODE (c.customer_type, 'C', 'G', 'C') customer_type,
                    (SELECT MAX (ba1.num)
                       FROM app_cor.bank_accounts ba1,
                            app_emi.customer_bank_accounts cba1
                      WHERE cba1.bac_id = ba1.ID
                        AND ba1.TYPE <> 'ISP'
                        AND cba1.cus_id = c.ID) bank_account,
                    DECODE(c.einv_permission_active, 'Y' , 'T', 'N') e_inv_permission,
                    c.corr_ad_line_1,
                    c.corr_city_code,
                    (SELECT name FROM EAGLE.CITIES WHERE id = c.corr_city_code) corr_city_name,
                    c.id
               FROM app_emi.customers c,
                    eagle.cities m
              WHERE c.city = m.ID(+)
                AND c.active = 'Y')
              LOOP   
                
                --pobierz emaile efaktury  
                SELECT SUBSTR(DATA,1,50)
                BULK COLLECT INTO vt_efv 
                      FROM APP_EMI.CUSTOMER_CONTACTS cc,
                           APP_COR.CONTACTS c 
                           WHERE cc.con_id = c.id
                           AND   c.type = 'M' -- e-mail efaktury  
                           AND   cc.cus_id =  i.id 
                           ORDER BY cc.con_id;
              
                 ADD_NAV_CUST(
                    A_CODE              => i.CODE,
                    A_NAME              => i.COMMERCIAL_NAME,
                    A_AD_LINE_1         => i.AD_LINE_1, 
                    A_CITY_CODE         => i.CITY_CODE,
                    A_CITY_NAME         => i.CITY_NAME,
                    A_VAT_REG_NUM       => i.VAT_REG_NUM,
                    A_PHONE             => i.PHONE, 
                    A_TYPE              => i.CUSTOMER_TYPE,
                    A_ACCOUNT           => v_acc_no.acc_no,
                    A_CLEAR_TYPE        => '00', 
                    A_BANK_ACCOUNT_NUM  => i.BANK_ACCOUNT,
                    A_TRANSFER_DESC     => NULL,
                    A_ACC_CODE          => NULL, 
                    A_ACC_NAME          => NULL,
                    A_ACC_AD_LINE_1     => NULL,
                    A_ACC_CITY_CODE     => NULL,
                    A_ACC_CITY_NAME     => NULL, 
                    A_ACC_VAT_REG_NUM   => NULL,
                    A_ACC_PHONE         => NULL,
                    A_ACC_TYPE          => NULL, 
                    A_ACC_ACCOUNT       => NULL,
                    A_ACC_CLEAR_TYPE    => NULL,
                    A_ACC_BANK_ACCOUNT_NUM  => NULL, 
                    A_ACC_TRANSFER_DESC => NULL,
                    A_TASK_ID           => a_task_all_id, 
                    A_E_INV_PERMISSION  => i.e_inv_permission, 
                    A_SENSITIVE_PARTNER => NULL,  
                    A_COR_AD_LINE_1     => i.corr_ad_line_1,      
                    A_COR_CITY_CODE     => i.corr_city_code,      
                    A_COR_CITY_NAME     => i.corr_city_name,      
                    A_EMAIL_FV_1        => TBL_NVL(vt_efv,1),                   
                    A_EMAIL_FV_2        => TBL_NVL(vt_efv,2),   
                    A_EMAIL_FV_3        => TBL_NVL(vt_efv,3),            
                    A_EMAIL_FV_4        => TBL_NVL(vt_efv,4),            
                    A_EMAIL_FV_5        => TBL_NVL(vt_efv,5),           
                    A_EMAIL_MID_1       => NULL,          
                    A_EMAIL_MID_2       => NULL,   
                    A_EMAIL_SXO         => NULL  
                    );
              
              END LOOP;
              
     /* Z danego dnia */
    FOR i IN (
                 SELECT DISTINCT 
                      code,
                      act_name,
                      commercial_name,
                      ad_line_1,
                      CITY_CODE, 
                      city_name,
                      vat_reg_num,
                      phone,
                      customer_type,
                      bank_account,
                      e_inv_permission,
                      corr_ad_line_1,
                      corr_city_code,
                      corr_city_name,
                      id    
               FROM   (  -- Klienci dla faktur z danego dnia
                         SELECT DISTINCT RTRIM (c.code) code,
                                c.act_name,
                                c.commercial_name,
                                c.ad_line_1,
                                m.ID CITY_CODE, 
                                m.NAME city_name,
                                c.vat_reg_num,
                                (SELECT MAX (con1.DATA)
                                   FROM app_emi.customers c1,
                                        app_emi.customer_contacts cc1,
                                        app_cor.contacts con1
                                  WHERE c1.ID = cc1.cus_id
                                    AND cc1.con_id = con1.ID
                                    AND con1.TYPE = 'P'
                                    AND c1.ID = c.ID) phone,
                                DECODE (c.customer_type, 'C', 'G', 'C') customer_type,
                                (SELECT MAX (ba1.num)
                                   FROM app_cor.bank_accounts ba1,
                                        app_emi.customer_bank_accounts cba1
                                  WHERE cba1.bac_id = ba1.ID
                                    AND ba1.TYPE <> 'ISP'
                                    AND cba1.cus_id = c.ID) bank_account,
                                DECODE(c.einv_permission_active, 'Y' , 'T', 'N') e_inv_permission,
                                c.corr_ad_line_1,
                                c.corr_city_code,
                                (SELECT name FROM EAGLE.CITIES WHERE id = c.corr_city_code) corr_city_name,
                                c.id                        
                           FROM app_emi.customers c,
                                eagle.cities m,
                                app_fin.invoices inv
                          WHERE c.city = m.ID(+)
                            AND c.active = 'Y'
                            AND inv.cus_id = c.id
                            AND trunc(inv.invoice_date) = trunc(a_input_date)                
                          UNION 
                          -- Klienci zmodyfikowani danego dnia
                          SELECT DISTINCT RTRIM (c.code) code,
                                c.act_name,
                                c.commercial_name, c.ad_line_1,
                                m.ID CITY_CODE, m.NAME city_name,
                                c.vat_reg_num,
                                (SELECT MAX (con1.DATA)
                                   FROM app_emi.customers c1,
                                        app_emi.customer_contacts cc1,
                                        app_cor.contacts con1
                                  WHERE c1.ID = cc1.cus_id
                                    AND cc1.con_id = con1.ID
                                    AND con1.TYPE = 'P'
                                    AND c1.ID = c.ID) phone,
                                DECODE (c.customer_type, 'C', 'G', 'C') customer_type,
                                (SELECT MAX (ba1.num)
                                   FROM app_cor.bank_accounts ba1,
                                        app_emi.customer_bank_accounts cba1
                                  WHERE cba1.bac_id = ba1.ID
                                    AND ba1.TYPE <> 'ISP'
                                    AND cba1.cus_id = c.ID) bank_account,
                                DECODE(c.einv_permission_active, 'Y' , 'T', 'N') e_inv_permission,
                                c.corr_ad_line_1,
                                c.corr_city_code,
                                (SELECT name FROM EAGLE.CITIES WHERE id = c.corr_city_code) corr_city_name,
                                c.id                        
                           FROM app_emi.customers c,
                                eagle.cities m
                          WHERE c.city = m.ID(+)
                            AND c.active = 'Y'
                            AND SUBSTR(c.modified, 1, 10) = TO_CHAR(a_input_date,'YYYY-MM-DD')                     
                      )          
               ) -- by³ wystawiony dokument lub klient by³ modyfikowany
              LOOP    
              
                --pobierz emaile efaktury  
                SELECT SUBSTR(DATA,1,50)
                BULK COLLECT INTO vt_efv 
                      FROM APP_EMI.CUSTOMER_CONTACTS cc,
                           APP_COR.CONTACTS c 
                           WHERE cc.con_id = c.id
                           AND   c.type = 'M' -- e-mail efaktury  
                           AND   cc.cus_id =  i.id 
                           ORDER BY cc.con_id;              
              
                 ADD_NAV_CUST(
                    A_CODE              => i.CODE,
                    A_NAME              => i.COMMERCIAL_NAME,
                    A_AD_LINE_1         => i.AD_LINE_1, 
                    A_CITY_CODE         => i.CITY_CODE,
                    A_CITY_NAME         => i.CITY_NAME,
                    A_VAT_REG_NUM       => i.VAT_REG_NUM,
                    A_PHONE             => i.PHONE, 
                    A_TYPE              => i.CUSTOMER_TYPE,
                    A_ACCOUNT           => v_acc_no.acc_no,
                    A_CLEAR_TYPE        => '00', 
                    A_BANK_ACCOUNT_NUM  => i.BANK_ACCOUNT,
                    A_TRANSFER_DESC     => NULL,
                    A_ACC_CODE          => NULL, 
                    A_ACC_NAME          => NULL,
                    A_ACC_AD_LINE_1     => NULL,
                    A_ACC_CITY_CODE     => NULL,
                    A_ACC_CITY_NAME     => NULL, 
                    A_ACC_VAT_REG_NUM   => NULL,
                    A_ACC_PHONE         => NULL,
                    A_ACC_TYPE          => NULL, 
                    A_ACC_ACCOUNT       => NULL,
                    A_ACC_CLEAR_TYPE    => NULL,
                    A_ACC_BANK_ACCOUNT_NUM  => NULL, 
                    A_ACC_TRANSFER_DESC => NULL,
                    A_TASK_ID           => a_task_id,
                    A_E_INV_PERMISSION  => i.e_inv_permission, 
                    A_SENSITIVE_PARTNER => NULL,  
                    A_COR_AD_LINE_1     => i.corr_ad_line_1,      
                    A_COR_CITY_CODE     => i.corr_city_code,      
                    A_COR_CITY_NAME     => i.corr_city_name,      
                    A_EMAIL_FV_1        => TBL_NVL(vt_efv,1),                   
                    A_EMAIL_FV_2        => TBL_NVL(vt_efv,2),   
                    A_EMAIL_FV_3        => TBL_NVL(vt_efv,3),            
                    A_EMAIL_FV_4        => TBL_NVL(vt_efv,4),            
                    A_EMAIL_FV_5        => TBL_NVL(vt_efv,5),           
                    A_EMAIL_MID_1       => NULL,          
                    A_EMAIL_MID_2       => NULL,   
                    A_EMAIL_SXO         => NULL                      
                    );
              END LOOP;
EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_INV');
END;

PROCEDURE External_Customers(a_input_date DATE,
                             a_task_id IN NUMBER,
                             a_task_all_id IN NUMBER)
IS
   v_acc_no r_acc;
   vt_efv   t_emails;
BEGIN
   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','External_Customers',NULL);
   v_acc_no := GET_ACCOUNT('EXTERNAL_CUSTOMER_ACCOUNT');


    /* Wszyscy */
    FOR i IN (SELECT c.code,
                    c.name, 
                    c.address,
                    c.zip_code, 
                    c.city_name,
                    c.vat_reg_num,
                    c.phone_number,
                    c.account_number,
                    c.id
               FROM APP_WEB.EXTERNAL_SYSTEM_CUSTOMERS c
               WHERE c.external_program_type in ( EAGLE.PK_MOD_EXTERNAL_INVOICES.gc_prog_type_clickepass, EAGLE.PK_MOD_EXTERNAL_INVOICES.gc_prog_type_vouchermodel))
              LOOP   
                 ADD_NAV_CUST(
                    A_CODE              => i.CODE,
                    A_NAME              => i.NAME,
                    A_AD_LINE_1         => i.address, 
                    A_CITY_CODE         => i.zip_code,
                    A_CITY_NAME         => i.CITY_NAME,
                    A_VAT_REG_NUM       => NVL(i.VAT_REG_NUM,'0000000000'),
                    A_PHONE             => i.phone_number, 
                    A_TYPE              => 'C',
                    A_ACCOUNT           => v_acc_no.acc_no,
                    A_CLEAR_TYPE        => 'PD', 
                    A_BANK_ACCOUNT_NUM  => i.account_number,
                    A_TRANSFER_DESC     => NULL,
                    A_ACC_CODE          => NULL, 
                    A_ACC_NAME          => NULL,
                    A_ACC_AD_LINE_1     => NULL,
                    A_ACC_CITY_CODE     => NULL,
                    A_ACC_CITY_NAME     => NULL, 
                    A_ACC_VAT_REG_NUM   => NULL,
                    A_ACC_PHONE         => NULL,
                    A_ACC_TYPE          => NULL, 
                    A_ACC_ACCOUNT       => NULL,
                    A_ACC_CLEAR_TYPE    => NULL,
                    A_ACC_BANK_ACCOUNT_NUM  => NULL, 
                    A_ACC_TRANSFER_DESC => NULL,
                    A_TASK_ID           => a_task_all_id, 
                    A_E_INV_PERMISSION  => 'T', 
                    A_SENSITIVE_PARTNER => NULL,  
                    A_COR_AD_LINE_1     => i.address,      
                    A_COR_CITY_CODE     => i.zip_code,      
                    A_COR_CITY_NAME     => i.CITY_NAME,      
                    A_EMAIL_FV_1        => NULL,                   
                    A_EMAIL_FV_2        => NULL,   
                    A_EMAIL_FV_3        => NULL,            
                    A_EMAIL_FV_4        => NULL,            
                    A_EMAIL_FV_5        => NULL,           
                    A_EMAIL_MID_1       => NULL,          
                    A_EMAIL_MID_2       => NULL,   
                    A_EMAIL_SXO         => NULL);
              END LOOP;
   
    /* Niewyeksportowane dokumenty z ostatniego tygodnia */
    FOR i IN (SELECT c.code,
                    c.name, 
                    c.address,
                    c.zip_code, 
                    c.city_name,
                    c.vat_reg_num,
                    c.phone_number,
                    c.account_number,
                    c.id
               FROM APP_WEB.EXTERNAL_SYSTEM_CUSTOMERS c
               JOIN APP_FIN.EXTERNAL_INVOICES inv ON inv.external_system_customer_id = c.id
              WHERE inv.STATUS_ID  = EAGLE.PK_MOD_EXTERNAL_INVOICES.gc_status_invoiced
                AND c.external_program_type in (EAGLE.PK_MOD_EXTERNAL_INVOICES.gc_prog_type_clickepass, EAGLE.PK_MOD_EXTERNAL_INVOICES.gc_prog_type_vouchermodel)
                AND TRUNC(inv.INVOICE_DATE) BETWEEN TRUNC(a_input_date) - 7 AND TRUNC(a_input_date))
              LOOP   
                 ADD_NAV_CUST(
                    A_CODE              => i.CODE,
                    A_NAME              => i.NAME,
                    A_AD_LINE_1         => i.address, 
                    A_CITY_CODE         => i.zip_code,
                    A_CITY_NAME         => i.CITY_NAME,
                    A_VAT_REG_NUM       => NVL(i.VAT_REG_NUM,'0000000000'),
                    A_PHONE             => i.phone_number, 
                    A_TYPE              => 'C',
                    A_ACCOUNT           => v_acc_no.acc_no,
                    A_CLEAR_TYPE        => 'PD', 
                    A_BANK_ACCOUNT_NUM  => i.account_number,
                    A_TRANSFER_DESC     => NULL,
                    A_ACC_CODE          => NULL, 
                    A_ACC_NAME          => NULL,
                    A_ACC_AD_LINE_1     => NULL,
                    A_ACC_CITY_CODE     => NULL,
                    A_ACC_CITY_NAME     => NULL, 
                    A_ACC_VAT_REG_NUM   => NULL,
                    A_ACC_PHONE         => NULL,
                    A_ACC_TYPE          => NULL, 
                    A_ACC_ACCOUNT       => NULL,
                    A_ACC_CLEAR_TYPE    => NULL,
                    A_ACC_BANK_ACCOUNT_NUM  => NULL, 
                    A_ACC_TRANSFER_DESC => NULL,
                    A_TASK_ID           => a_task_id, 
                    A_E_INV_PERMISSION  => 'T', 
                    A_SENSITIVE_PARTNER => NULL,  
                    A_COR_AD_LINE_1     => i.address,      
                    A_COR_CITY_CODE     => i.zip_code,      
                    A_COR_CITY_NAME     => i.CITY_NAME,      
                    A_EMAIL_FV_1        => NULL,                   
                    A_EMAIL_FV_2        => NULL,   
                    A_EMAIL_FV_3        => NULL,            
                    A_EMAIL_FV_4        => NULL,            
                    A_EMAIL_FV_5        => NULL,           
                    A_EMAIL_MID_1       => NULL,          
                    A_EMAIL_MID_2       => NULL,   
                    A_EMAIL_SXO         => NULL);
              END LOOP;
EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_EC');
END External_Customers;

PROCEDURE CUST_AFF(a_input_date DATE,
                      a_task_id IN NUMBER,
                      a_task_all_id IN NUMBER)
IS
   v_account         r_acc;
   v_acc_account     r_acc;
   v_acc_clear_type  app_swp.nav_cust.clear_type%TYPE;   
   vt_efv            t_emails;
   vt_mid            t_emails;
      
BEGIN

   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','EXP_CUST_CLIENT',NULL);
   
   v_account := GET_ACCOUNT('AFFILIANT_ACCOUNT');
   
    /* Wszystkie */
    FOR i IN (SELECT DISTINCT RTRIM (mp.code) code, 
                mp.NAME, mp.ad_line_1,
                c.ID CITY_CODE, c.NAME city_name,
                mp.vat_reg_num,
                (SELECT max(c1.DATA) 
                   FROM app_cor.contacts c1,
                        app_rmb.market_point_contacts mpc1
                  WHERE mpc1.mkp_id = mp.id
                    AND mpc1.con_id = c1.ID
                    AND c1.TYPE = 'P') phone,
                    'A' customer_type,
                    mp.BANK_ACCOUNT_NUM bank_account,
                    mp.transfer_remarks,
                mp.ID mkp_id,
                mp.ACC_ID,
                acc.code acc_code,
                acc.name acc_name,
                acc.ad_line_1 acc_ad_line_1,
                acc.city_code acc_city_code,
                acc.city_name acc_city_name,
                acc.vat_reg_num acc_vat_reg_num,
                acc.phone acc_phone,
                acc.customer_type acc_customer_type,
                acc.bank_account acc_bank_account,
                acc.transfer_remarks acc_transfer_remarks,
                DECODE(mp.einv_permission_active, 'Y' , 'T', DECODE(a.einv_permission_active, 'Y' , 'T', 'N')) e_inv_permission,
                mp.corr_ad_line_1,
                mp.corr_city_id,
                (SELECT name FROM EAGLE.CITIES WHERE id = mp.corr_city_id) corr_city_name                
           FROM app_rmb.acceptors a,
                app_rmb.market_points mp,
                eagle.cities c,
                (SELECT DISTINCT RTRIM (mp.code) code, 
                                mp.NAME, mp.ad_line_1,
                                c.ID city_code, c.NAME city_name,
                                mp.vat_reg_num,
                                (SELECT max(c1.DATA) 
                                   FROM app_cor.contacts c1,
                                        app_rmb.market_point_contacts mpc1
                                  WHERE mpc1.mkp_id = mp.id
                                    AND mpc1.con_id = c1.ID
                                    AND c1.TYPE = 'P') phone,
                                    'A' customer_type,
                                    mp.BANK_ACCOUNT_NUM bank_account,
                                    mp.transfer_remarks,
                                mp.ID mkp_id,
                                mp.ACC_ID
                           FROM app_rmb.market_points mp,
                                eagle.cities c
                          WHERE mp.city_id = c.ID(+)
                            AND nvl(mp.ORIGINAL,'N') = 'Y') acc              
              WHERE mp.acc_id = a.id
                AND mp.city_id = c.ID(+)
                AND mp.ACC_ID = acc.acc_id (+)
            AND EXISTS (
                   SELECT mkp
                     FROM (SELECT DISTINCT mkp_id mkp
                                      FROM app_fin.invoices
                                     WHERE mkp_id IS NOT NULL
                           UNION
                           (SELECT DISTINCT mkp_id mkp
                                       FROM app_rmb.remittances
                                      WHERE mkp_id IS NOT NULL)
                           UNION
                           (SELECT DISTINCT mkp_id mkp
                                       FROM app_vrmb.remittances
                                      WHERE mkp_id IS NOT NULL)
                           UNION
                           (SELECT DISTINCT mkp_id mkp
                                       FROM APP_RMB.RECKONINGS R
                                      WHERE mkp_id IS NOT NULL)
                                      ) mk
                    WHERE mk.mkp = mp.ID))
              LOOP
              
                 v_acc_account := NULL;
                 v_acc_clear_type := NULL;
                 IF i.ACC_CODE IS NOT NULL THEN
                    v_acc_account    := v_account;
                    v_acc_clear_type := 'PD';
                 END IF;      
                 
                 -- pobierz email efaktury
                 SELECT DATA BULK COLLECT INTO vt_efv 
                 FROM (
                   SELECT SUBSTR(DATA,1,50) DATA 
                   FROM APP_RMB.MARKET_POINT_CONTACTS mc,
                        APP_COR.CONTACTS c
                   WHERE mc.con_id = c.id
                   AND   c.type = 'M' -- e-mail efaktury   
                   AND   mc.mkp_id =  i.mkp_id   
                   UNION
                   SELECT SUBSTR(DATA,1,50) DATA 
                   FROM APP_RMB.ACCEPTOR_CONTACTS ac,
                        APP_COR.CONTACTS c
                   WHERE ac.con_id = c.id
                   AND   c.type = 'M' -- e-mail efaktury   
                   AND   ac.acc_id = i.acc_id  
                   );  

                 -- pobierz email mid
                 SELECT DATA BULK COLLECT INTO vt_mid 
                 FROM (
                   SELECT SUBSTR(DATA,1,50) DATA 
                   FROM APP_RMB.MARKET_POINT_CONTACTS mc,
                        APP_COR.CONTACTS c
                   WHERE mc.con_id = c.id
                   AND   c.type = 'I' -- e-mail efaktury   
                   AND   mc.mkp_id =  i.mkp_id   
                   UNION
                   SELECT SUBSTR(DATA,1,50) DATA 
                   FROM APP_RMB.ACCEPTOR_CONTACTS ac,
                        APP_COR.CONTACTS c
                   WHERE ac.con_id = c.id
                   AND   c.type = 'I' -- e-mail efaktury   
                   AND   ac.acc_id = i.acc_id  
                   );  
              
                 ADD_NAV_CUST(
                    A_CODE              => i.CODE,
                    A_NAME              => i.NAME,
                    A_AD_LINE_1         => i.AD_LINE_1, 
                    A_CITY_CODE         => i.CITY_CODE,
                    A_CITY_NAME         => i.CITY_NAME,
                    A_VAT_REG_NUM       => i.VAT_REG_NUM,
                    A_PHONE             => i.PHONE, 
                    A_TYPE              => i.CUSTOMER_TYPE,
                    A_ACCOUNT           => v_account.acc_no,
                    A_CLEAR_TYPE        => 'PD', 
                    A_BANK_ACCOUNT_NUM  => i.BANK_ACCOUNT,
                    A_TRANSFER_DESC     => i.TRANSFER_REMARKS,
                    A_ACC_CODE          => i.ACC_CODE, 
                    A_ACC_NAME          => i.ACC_NAME,
                    A_ACC_AD_LINE_1     => i.ACC_AD_LINE_1,
                    A_ACC_CITY_CODE     => i.ACC_CITY_CODE,
                    A_ACC_CITY_NAME     => i.ACC_CITY_NAME, 
                    A_ACC_VAT_REG_NUM   => i.ACC_VAT_REG_NUM,
                    A_ACC_PHONE         => i.ACC_PHONE,
                    A_ACC_TYPE          => i.ACC_CUSTOMER_TYPE,
                    A_ACC_ACCOUNT       => v_acc_account.acc_no,
                    A_ACC_CLEAR_TYPE    => v_acc_clear_type,
                    A_ACC_BANK_ACCOUNT_NUM  => i.ACC_BANK_ACCOUNT, 
                    A_ACC_TRANSFER_DESC => i.ACC_TRANSFER_REMARKS,
                    A_TASK_ID           => a_task_all_id,
                    A_E_INV_PERMISSION  => i.e_inv_permission, 
                    A_SENSITIVE_PARTNER => NULL,  
                    A_COR_AD_LINE_1     => i.corr_ad_line_1,      
                    A_COR_CITY_CODE     => i.corr_city_id,      
                    A_COR_CITY_NAME     => i.corr_city_name,      
                    A_EMAIL_FV_1        => TBL_NVL(vt_efv,1),                   
                    A_EMAIL_FV_2        => TBL_NVL(vt_efv,2),   
                    A_EMAIL_FV_3        => TBL_NVL(vt_efv,3),            
                    A_EMAIL_FV_4        => TBL_NVL(vt_efv,4),            
                    A_EMAIL_FV_5        => TBL_NVL(vt_efv,5),           
                    A_EMAIL_MID_1       => TBL_NVL(vt_mid,1),          
                    A_EMAIL_MID_2       => TBL_NVL(vt_mid,2),   
                    A_EMAIL_SXO         => NULL  
                    );
              
              END LOOP;
              
     /* Z danego dnia */
    FOR i IN (SELECT DISTINCT RTRIM (mp.code) code, 
                mp.NAME, mp.ad_line_1,
                c.ID CITY_CODE, c.NAME city_name,
                mp.vat_reg_num,
                (SELECT max(c1.DATA) 
                   FROM app_cor.contacts c1,
                        app_rmb.market_point_contacts mpc1
                  WHERE mpc1.mkp_id = mp.id
                    AND mpc1.con_id = c1.ID
                    AND c1.TYPE = 'P') phone,
                    'A' customer_type,
                    mp.BANK_ACCOUNT_NUM bank_account,
                    mp.transfer_remarks,
                mp.ID mkp_id,
                mp.ACC_ID,
                acc.code acc_code,
                acc.name acc_name,
                acc.ad_line_1 acc_ad_line_1,
                acc.city_code acc_city_code,
                acc.city_name acc_city_name,
                acc.vat_reg_num acc_vat_reg_num,
                acc.phone acc_phone,
                acc.customer_type acc_customer_type,
                acc.bank_account acc_bank_account,
                acc.transfer_remarks acc_transfer_remarks,
                DECODE(mp.einv_permission_active, 'Y' , 'T', DECODE(a.einv_permission_active, 'Y' , 'T', 'N')) e_inv_permission,
                mp.corr_ad_line_1,
                mp.corr_city_id,
                (SELECT name FROM EAGLE.CITIES WHERE id = mp.corr_city_id) corr_city_name                   
           FROM app_rmb.market_points mp,
                app_rmb.acceptors a,
                eagle.cities c,
                app_fin.invoices inv,
                (SELECT DISTINCT RTRIM (mp.code) code, 
                                mp.NAME, mp.ad_line_1,
                                c.ID city_code, c.NAME city_name,
                                mp.vat_reg_num,
                                (SELECT max(c1.DATA) 
                                   FROM app_cor.contacts c1,
                                        app_rmb.market_point_contacts mpc1
                                  WHERE mpc1.mkp_id = mp.id
                                    AND mpc1.con_id = c1.ID
                                    AND c1.TYPE = 'P') phone,
                                    'A' customer_type,
                                    mp.BANK_ACCOUNT_NUM bank_account,
                                    mp.transfer_remarks,
                                mp.ID mkp_id,
                                mp.ACC_ID
                           FROM app_rmb.market_points mp,
                                eagle.cities c
                          WHERE mp.city_id = c.ID(+)
                            AND nvl(mp.ORIGINAL,'N') = 'Y') acc              
              WHERE mp.acc_id = a.id
                AND mp.city_id = c.ID(+)
                AND mp.ACC_ID = acc.acc_id (+)
                AND mp.id = inv.mkp_id
                AND (trunc(inv.invoice_date) = trunc(a_input_date) 
                    OR SUBSTR(mp.modified, 1, 10) = TO_CHAR(a_input_date,'YYYY-MM-DD')
                    AND mp.name IS NOT NULL))  -- by³ wystawiony dokument lub punkt (posiadaj¹cy jakiekolwiek faktury) by³ modyfikowany 
              LOOP
              
                 v_acc_account    := NULL;
                 v_acc_clear_type := NULL;
                 IF i.ACC_CODE IS NOT NULL THEN
                    v_acc_account    := v_account;
                    v_acc_clear_type := 'PD';
                 END IF;              

                                  -- pobierz email efaktury
                 SELECT DATA BULK COLLECT INTO vt_efv 
                 FROM (
                   SELECT SUBSTR(DATA,1,50) DATA 
                   FROM APP_RMB.MARKET_POINT_CONTACTS mc,
                        APP_COR.CONTACTS c
                   WHERE mc.con_id = c.id
                   AND   c.type = 'M' -- e-mail efaktury   
                   AND   mc.mkp_id =  i.mkp_id   
                   UNION
                   SELECT SUBSTR(DATA,1,50) DATA 
                   FROM APP_RMB.ACCEPTOR_CONTACTS ac,
                        APP_COR.CONTACTS c
                   WHERE ac.con_id = c.id
                   AND   c.type = 'M' -- e-mail efaktury   
                   AND   ac.acc_id = i.acc_id  
                   );  

                 -- pobierz email mid
                 SELECT DATA BULK COLLECT INTO vt_mid 
                 FROM (
                   SELECT SUBSTR(DATA,1,50) DATA 
                   FROM APP_RMB.MARKET_POINT_CONTACTS mc,
                        APP_COR.CONTACTS c
                   WHERE mc.con_id = c.id
                   AND   c.type = 'I' -- e-mail efaktury   
                   AND   mc.mkp_id =  i.mkp_id   
                   UNION
                   SELECT SUBSTR(DATA,1,50) DATA 
                   FROM APP_RMB.ACCEPTOR_CONTACTS ac,
                        APP_COR.CONTACTS c
                   WHERE ac.con_id = c.id
                   AND   c.type = 'I' -- e-mail efaktury   
                   AND   ac.acc_id = i.acc_id  
                   );  
              
                 ADD_NAV_CUST(
                    A_CODE              => i.CODE,
                    A_NAME              => i.NAME,
                    A_AD_LINE_1         => i.AD_LINE_1, 
                    A_CITY_CODE         => i.CITY_CODE,
                    A_CITY_NAME         => i.CITY_NAME,
                    A_VAT_REG_NUM       => i.VAT_REG_NUM,
                    A_PHONE             => i.PHONE, 
                    A_TYPE              => i.CUSTOMER_TYPE,
                    A_ACCOUNT           => v_account.acc_no,
                    A_CLEAR_TYPE        => 'PD',
                    A_BANK_ACCOUNT_NUM  => i.BANK_ACCOUNT,
                    A_TRANSFER_DESC     => i.TRANSFER_REMARKS,
                    A_ACC_CODE          => i.ACC_CODE, 
                    A_ACC_NAME          => i.ACC_NAME,
                    A_ACC_AD_LINE_1     => i.ACC_AD_LINE_1,
                    A_ACC_CITY_CODE     => i.ACC_CITY_CODE,
                    A_ACC_CITY_NAME     => i.ACC_CITY_NAME,
                    A_ACC_VAT_REG_NUM   => i.ACC_VAT_REG_NUM,
                    A_ACC_PHONE         => i.ACC_PHONE,
                    A_ACC_TYPE          => i.ACC_CUSTOMER_TYPE,
                    A_ACC_ACCOUNT       => v_acc_account.acc_no,
                    A_ACC_CLEAR_TYPE    => v_acc_clear_type,
                    A_ACC_BANK_ACCOUNT_NUM  => i.ACC_BANK_ACCOUNT, 
                    A_ACC_TRANSFER_DESC => i.ACC_TRANSFER_REMARKS,
                    A_TASK_ID           => a_task_id,
                    A_E_INV_PERMISSION  => i.e_inv_permission, 
                    A_SENSITIVE_PARTNER => NULL,  
                    A_COR_AD_LINE_1     => i.corr_ad_line_1,      
                    A_COR_CITY_CODE     => i.corr_city_id,      
                    A_COR_CITY_NAME     => i.corr_city_name,      
                    A_EMAIL_FV_1        => TBL_NVL(vt_efv,1),                   
                    A_EMAIL_FV_2        => TBL_NVL(vt_efv,2),   
                    A_EMAIL_FV_3        => TBL_NVL(vt_efv,3),            
                    A_EMAIL_FV_4        => TBL_NVL(vt_efv,4),            
                    A_EMAIL_FV_5        => TBL_NVL(vt_efv,5),           
                    A_EMAIL_MID_1       => TBL_NVL(vt_mid,1),          
                    A_EMAIL_MID_2       => TBL_NVL(vt_mid,2),   
                    A_EMAIL_SXO         => NULL                      
                    );
              END LOOP;

EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_INV');
END;


PROCEDURE ADD_NAV_CUST(A_CODE                    IN VARCHAR2,
                      A_NAME                    IN VARCHAR2,
                      A_AD_LINE_1               IN VARCHAR2, 
                      A_CITY_CODE               IN VARCHAR2,
                      A_CITY_NAME               IN VARCHAR2,
                      A_VAT_REG_NUM             IN VARCHAR2,
                      A_PHONE                   IN VARCHAR2, 
                      A_TYPE                    IN VARCHAR2,
                      A_ACCOUNT                 IN VARCHAR2,
                      A_CLEAR_TYPE              IN VARCHAR2, 
                      A_BANK_ACCOUNT_NUM        IN VARCHAR2,
                      A_TRANSFER_DESC           IN VARCHAR2,
                      A_ACC_CODE                IN VARCHAR2, 
                      A_ACC_NAME                IN VARCHAR2,
                      A_ACC_AD_LINE_1           IN VARCHAR2,
                      A_ACC_CITY_CODE           IN VARCHAR2,
                      A_ACC_CITY_NAME           IN VARCHAR2,
                      A_ACC_VAT_REG_NUM         IN VARCHAR2,
                      A_ACC_PHONE               IN VARCHAR2,
                      A_ACC_TYPE                IN VARCHAR2, 
                      A_ACC_ACCOUNT             IN VARCHAR2,
                      A_ACC_CLEAR_TYPE          IN VARCHAR2,
                      A_ACC_BANK_ACCOUNT_NUM    IN VARCHAR2, 
                      A_ACC_TRANSFER_DESC       IN VARCHAR2,
                      A_TASK_ID                 IN NUMBER,
                      A_E_INV_PERMISSION        IN VARCHAR2,
                      A_SENSITIVE_PARTNER       IN VARCHAR2,
                      A_COR_AD_LINE_1           IN VARCHAR2,
                      A_COR_CITY_CODE           IN VARCHAR2,
                      A_COR_CITY_NAME           IN VARCHAR2,
                      A_EMAIL_FV_1              IN VARCHAR2,          
                      A_EMAIL_FV_2              IN VARCHAR2,  
                      A_EMAIL_FV_3              IN VARCHAR2,  
                      A_EMAIL_FV_4              IN VARCHAR2,  
                      A_EMAIL_FV_5              IN VARCHAR2,  
                      A_EMAIL_MID_1             IN VARCHAR2,  
                      A_EMAIL_MID_2             IN VARCHAR2,  
                      A_EMAIL_SXO               IN VARCHAR2)          
IS
BEGIN

   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','ADD_NAV_CUST',NULL);

   BEGIN
        INSERT INTO app_swp.nav_cust
                    (code,
                    NAME,
                     ad_line_1,
                     city_code,
                     city_name,
                     vat_reg_num,
                     phone,
                     TYPE,
                     ACCOUNT,
                     clear_type,
                     bank_account_num,
                     transfer_desc,
                     acc_code,
                     acc_name,
                     acc_ad_line_1,
                     acc_city_code,
                     acc_city_name,
                     acc_vat_reg_num,
                     acc_phone,
                     acc_type,
                     acc_account,
                     acc_clear_type,
                     acc_bank_account_num,
                     acc_transfer_desc,
                     task_id,
                     e_inv_permission,
                     sensitive_partner, 
                     cor_ad_line_1,     
                     cor_city_code,     
                     cor_city_name,     
                     email_fv_1,                
                     email_fv_2,        
                     email_fv_3,        
                     email_fv_4,        
                     email_fv_5,        
                     email_mid_1,       
                     email_mid_2,       
                     email_sxo         
                    )
             VALUES (pk_utils.remove_dangerous_white_chars(a_code),
                     SUBSTR (pk_utils.remove_dangerous_white_chars(a_name), 1, 50),
                     SUBSTR (pk_utils.remove_dangerous_white_chars(a_ad_line_1), 1, 25), 
                     SUBSTR (pk_utils.remove_dangerous_white_chars(a_city_code), 1, 10),
                     SUBSTR (pk_utils.remove_dangerous_white_chars(a_city_name), 1, 30),
                     SUBSTR (pk_utils.remove_dangerous_white_chars(a_vat_reg_num), 1, 10),
                     SUBSTR (pk_utils.remove_dangerous_white_chars(a_phone), 1, 20),
                     pk_utils.remove_dangerous_white_chars(a_type),
                     pk_utils.remove_dangerous_white_chars(a_account),
                     pk_utils.remove_dangerous_white_chars(a_clear_type),
                     pk_utils.remove_dangerous_white_chars(decode(a_bank_account_num,'0',NULL,a_bank_account_num)),
                     SUBSTR (pk_utils.remove_dangerous_white_chars(a_transfer_desc), 1, 15),
                     pk_utils.remove_dangerous_white_chars(a_acc_code),
                     SUBSTR (pk_utils.remove_dangerous_white_chars(a_acc_name), 1, 50),
                     SUBSTR (pk_utils.remove_dangerous_white_chars(a_acc_ad_line_1), 1, 25),
                     SUBSTR (pk_utils.remove_dangerous_white_chars(a_acc_city_code), 1, 10),
                     SUBSTR (pk_utils.remove_dangerous_white_chars(a_acc_city_name), 1, 30),
                     SUBSTR (pk_utils.remove_dangerous_white_chars(a_acc_vat_reg_num), 1, 10),
                     SUBSTR (pk_utils.remove_dangerous_white_chars(a_acc_phone), 1, 20),
                     pk_utils.remove_dangerous_white_chars(a_acc_type),
                     pk_utils.remove_dangerous_white_chars(a_acc_account),
                     pk_utils.remove_dangerous_white_chars(a_acc_clear_type),
                     pk_utils.remove_dangerous_white_chars(decode(a_acc_bank_account_num,'0',NULL,a_acc_bank_account_num)),
                     SUBSTR (pk_utils.remove_dangerous_white_chars(a_acc_transfer_desc), 1, 15),
                     a_task_id,
                     SUBSTR(pk_utils.remove_dangerous_white_chars(a_e_inv_permission), 1, 1),
                     SUBSTR(pk_utils.remove_dangerous_white_chars(a_sensitive_partner), 1, 1),
                     SUBSTR(pk_utils.remove_dangerous_white_chars(a_cor_ad_line_1), 1, 50),
                     SUBSTR(pk_utils.remove_dangerous_white_chars(a_cor_city_code), 1, 10),
                     SUBSTR(pk_utils.remove_dangerous_white_chars(a_cor_city_name), 1, 30),
                     SUBSTR(pk_utils.remove_dangerous_white_chars(a_email_fv_1), 1, 50),
                     SUBSTR(pk_utils.remove_dangerous_white_chars(a_email_fv_2), 1, 50),
                     SUBSTR(pk_utils.remove_dangerous_white_chars(a_email_fv_3), 1, 50),
                     SUBSTR(pk_utils.remove_dangerous_white_chars(a_email_fv_4), 1, 50),
                     SUBSTR(pk_utils.remove_dangerous_white_chars(a_email_fv_5), 1, 50),
                     SUBSTR(pk_utils.remove_dangerous_white_chars(a_email_mid_1), 1, 50),
                     SUBSTR(pk_utils.remove_dangerous_white_chars(a_email_mid_2), 1, 50),
                     SUBSTR(pk_utils.remove_dangerous_white_chars(a_email_sxo), 1, 50)                     
                    );
   EXCEPTION
      WHEN others THEN
         PK_ERROR.RAISE_ERROR(-20001, 'cus_code='||a_code||' errm='||sqlerrm);
   END;

EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_ANC');
END;

PROCEDURE CUST_AFF_VB(a_export_id    IN NUMBER,
                          a_task_id      IN NUMBER,
                          a_task_all_id  IN NUMBER)
IS
   v_account         r_acc;
   v_acc_account     r_acc;
   v_acc_clear_type app_swp.nav_cust.clear_type%TYPE;
   vt_efv            t_emails;
   vt_mid            t_emails;      
BEGIN

    PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','EXP_CUST_AFF_VB',NULL);
    
   v_account := GET_ACCOUNT('AFFILIANT_ACCOUNT_VB');
   
    /* Wszystkie */
    FOR i IN (SELECT DISTINCT RTRIM (mp.code) code, 
                mp.NAME, mp.ad_line_1,
                c.ID CITY_CODE, c.NAME city_name,
                mp.vat_reg_num,
                (SELECT max(c1.DATA) 
                   FROM app_cor.contacts c1,
                        app_rmb.market_point_contacts mpc1
                  WHERE mpc1.mkp_id = mp.id
                    AND mpc1.con_id = c1.ID
                    AND c1.TYPE = 'P') phone,
                    'D' customer_type,
                    mp.BANK_ACCOUNT_NUM bank_account,
                    mp.transfer_remarks,
                mp.ID mkp_id,
                mp.ACC_ID,
                acc.code acc_code,
                acc.name acc_name,
                acc.ad_line_1 acc_ad_line_1,
                acc.city_code acc_city_code,
                acc.city_name acc_city_name,
                acc.vat_reg_num acc_vat_reg_num,
                acc.phone acc_phone,
                acc.customer_type acc_customer_type,
                acc.bank_account acc_bank_account,
                acc.transfer_remarks acc_transfer_remarks,
                DECODE(mp.einv_permission_active, 'Y' , 'T', DECODE(a.einv_permission_active, 'Y' , 'T', 'N')) e_inv_permission,
                mp.corr_ad_line_1,
                mp.corr_city_id,
                (SELECT name FROM EAGLE.CITIES WHERE id = mp.corr_city_id) corr_city_name 
           FROM app_rmb.market_points mp,
                app_rmb.acceptors a,
                eagle.cities c,
                (SELECT DISTINCT RTRIM (mp.code) code, 
                                mp.NAME, mp.ad_line_1,
                                c.ID city_code, c.NAME city_name,
                                mp.vat_reg_num,
                                (SELECT max(c1.DATA) 
                                   FROM app_cor.contacts c1,
                                        app_rmb.market_point_contacts mpc1
                                  WHERE mpc1.mkp_id = mp.id
                                    AND mpc1.con_id = c1.ID
                                    AND c1.TYPE = 'P') phone,
                                    'D' customer_type,
                                    mp.BANK_ACCOUNT_NUM bank_account,
                                    mp.transfer_remarks,
                                mp.ID mkp_id,
                                mp.ACC_ID
                           FROM app_rmb.market_points mp,
                                eagle.cities c
                          WHERE mp.city_id = c.ID(+)
                            AND nvl(mp.ORIGINAL,'N') = 'Y') acc              
              WHERE mp.acc_id = a.id
                AND mp.city_id = c.ID(+)
                AND mp.ACC_ID = acc.acc_id (+)
            AND EXISTS (
                   SELECT mkp
                     FROM (SELECT DISTINCT mkp_id mkp
                                      FROM app_fin.invoices
                                     WHERE mkp_id IS NOT NULL
                           UNION
                           (SELECT DISTINCT mkp_id mkp
                                       FROM app_rmb.remittances
                                      WHERE mkp_id IS NOT NULL)) mk
                    WHERE mk.mkp = mp.ID))
              LOOP
              
                 v_acc_account    := NULL;
                 v_acc_clear_type := NULL;
                 IF i.ACC_CODE IS NOT NULL THEN
                    v_acc_account    := v_account;
                    v_acc_clear_type := 'PD';
                 END IF; 
                 
                 -- pobierz email efaktury
                 SELECT DATA BULK COLLECT INTO vt_efv 
                 FROM (
                   SELECT SUBSTR(DATA,1,50) DATA 
                   FROM APP_RMB.MARKET_POINT_CONTACTS mc,
                        APP_COR.CONTACTS c
                   WHERE mc.con_id = c.id
                   AND   c.type = 'M' -- e-mail efaktury   
                   AND   mc.mkp_id =  i.mkp_id   
                   UNION
                   SELECT SUBSTR(DATA,1,50) DATA 
                   FROM APP_RMB.ACCEPTOR_CONTACTS ac,
                        APP_COR.CONTACTS c
                   WHERE ac.con_id = c.id
                   AND   c.type = 'M' -- e-mail efaktury   
                   AND   ac.acc_id = i.acc_id  
                   );  

                 -- pobierz email mid
                 SELECT DATA BULK COLLECT INTO vt_mid 
                 FROM (
                   SELECT SUBSTR(DATA,1,50) DATA 
                   FROM APP_RMB.MARKET_POINT_CONTACTS mc,
                        APP_COR.CONTACTS c
                   WHERE mc.con_id = c.id
                   AND   c.type = 'I' -- e-mail efaktury   
                   AND   mc.mkp_id =  i.mkp_id   
                   UNION
                   SELECT SUBSTR(DATA,1,50) DATA 
                   FROM APP_RMB.ACCEPTOR_CONTACTS ac,
                        APP_COR.CONTACTS c
                   WHERE ac.con_id = c.id
                   AND   c.type = 'I' -- e-mail efaktury   
                   AND   ac.acc_id = i.acc_id  
                   );                                
              
                 ADD_NAV_CUST(
                    A_CODE              => i.CODE,
                    A_NAME              => i.NAME,
                    A_AD_LINE_1         => i.AD_LINE_1, 
                    A_CITY_CODE         => i.CITY_CODE,
                    A_CITY_NAME         => i.CITY_NAME,
                    A_VAT_REG_NUM       => i.VAT_REG_NUM,
                    A_PHONE             => i.PHONE, 
                    A_TYPE              => i.CUSTOMER_TYPE,
                    A_ACCOUNT           => v_account.acc_no,
                    A_CLEAR_TYPE        => 'PD', 
                    A_BANK_ACCOUNT_NUM  => i.BANK_ACCOUNT,
                    A_TRANSFER_DESC     => i.TRANSFER_REMARKS,
                    A_ACC_CODE          => i.ACC_CODE, 
                    A_ACC_NAME          => i.ACC_NAME,
                    A_ACC_AD_LINE_1     => i.ACC_AD_LINE_1,
                    A_ACC_CITY_CODE     => i.ACC_CITY_CODE,
                    A_ACC_CITY_NAME     => i.ACC_CITY_NAME,
                    A_ACC_VAT_REG_NUM   => i.ACC_VAT_REG_NUM,
                    A_ACC_PHONE         => i.ACC_PHONE,
                    A_ACC_TYPE          => i.ACC_CUSTOMER_TYPE,
                    A_ACC_ACCOUNT       => v_acc_account.acc_no,
                    A_ACC_CLEAR_TYPE    => v_acc_clear_type,
                    A_ACC_BANK_ACCOUNT_NUM  => i.ACC_BANK_ACCOUNT, 
                    A_ACC_TRANSFER_DESC => i.ACC_TRANSFER_REMARKS,
                    A_TASK_ID           => a_task_all_id,
                    A_E_INV_PERMISSION  => i.e_inv_permission, 
                    A_SENSITIVE_PARTNER => NULL,  
                    A_COR_AD_LINE_1     => i.corr_ad_line_1,      
                    A_COR_CITY_CODE     => i.corr_city_id,      
                    A_COR_CITY_NAME     => i.corr_city_name,      
                    A_EMAIL_FV_1        => TBL_NVL(vt_efv,1),                   
                    A_EMAIL_FV_2        => TBL_NVL(vt_efv,2),   
                    A_EMAIL_FV_3        => TBL_NVL(vt_efv,3),            
                    A_EMAIL_FV_4        => TBL_NVL(vt_efv,4),            
                    A_EMAIL_FV_5        => TBL_NVL(vt_efv,5),           
                    A_EMAIL_MID_1       => TBL_NVL(vt_mid,1),          
                    A_EMAIL_MID_2       => TBL_NVL(vt_mid,2),   
                    A_EMAIL_SXO         => NULL  
                    );
              
              END LOOP;
              
     /* Z danego export_id */
    FOR i IN (SELECT DISTINCT RTRIM (mp.code) code, 
                mp.NAME, mp.ad_line_1,
                c.ID CITY_CODE, c.NAME city_name,
                mp.vat_reg_num,
                (SELECT max(c1.DATA) 
                   FROM app_cor.contacts c1,
                        app_rmb.market_point_contacts mpc1
                  WHERE mpc1.mkp_id = mp.id
                    AND mpc1.con_id = c1.ID
                    AND c1.TYPE = 'P') phone,
                    'D' customer_type,
                    mp.BANK_ACCOUNT_NUM bank_account,
                    mp.transfer_remarks,
                mp.ID mkp_id,
                mp.ACC_ID,
                acc.code acc_code,
                acc.name acc_name,
                acc.ad_line_1 acc_ad_line_1,
                acc.city_code acc_city_code,
                acc.city_name acc_city_name,
                acc.vat_reg_num acc_vat_reg_num,
                acc.phone acc_phone,
                acc.customer_type acc_customer_type,
                acc.bank_account acc_bank_account,
                acc.transfer_remarks acc_transfer_remarks,
                DECODE(mp.einv_permission_active, 'Y' , 'T', DECODE(a.einv_permission_active, 'Y' , 'T', 'N')) e_inv_permission,
                mp.corr_ad_line_1,
                mp.corr_city_id,
                (SELECT name FROM EAGLE.CITIES WHERE id = mp.corr_city_id) corr_city_name                 
           FROM app_rmb.market_points mp,
                app_rmb.acceptors a,  
                eagle.cities c,
                app_vfin.invoices inv,
                app_vfin.inv_session_invoices ISI,
                app_vfin.invoice_sessions SI,
                (SELECT DISTINCT RTRIM (mp.code) code, 
                                mp.NAME, mp.ad_line_1,
                                c.ID city_code, c.NAME city_name,
                                mp.vat_reg_num,
                                (SELECT max(c1.DATA) 
                                   FROM app_cor.contacts c1,
                                        app_rmb.market_point_contacts mpc1
                                  WHERE mpc1.mkp_id = mp.id
                                    AND mpc1.con_id = c1.ID
                                    AND c1.TYPE = 'P') phone,
                                    'D' customer_type,
                                    mp.BANK_ACCOUNT_NUM bank_account,
                                    mp.transfer_remarks,
                                mp.ID mkp_id,
                                mp.ACC_ID
                           FROM app_rmb.market_points mp,
                                eagle.cities c
                          WHERE mp.city_id = c.ID(+)
                            AND nvl(mp.ORIGINAL,'N') = 'Y') acc              
              WHERE mp.acc_id = a.id
                AND mp.city_id = c.ID(+)
                AND mp.ACC_ID = acc.acc_id (+)
                AND mp.id = inv.mkp_id
                AND isi.VINV_ID = inv.ID
				AND si.ID = isi.SESSION_ID
				AND si.EXPORT_ID = a_export_id)
              LOOP
              
                 v_acc_account    := NULL;
                 v_acc_clear_type := NULL;
                 IF i.ACC_CODE IS NOT NULL THEN
                    v_acc_account    := v_account;
                    v_acc_clear_type := 'PD';
                 END IF;   
                 
                 -- pobierz email efaktury
                 SELECT DATA BULK COLLECT INTO vt_efv 
                 FROM (
                   SELECT SUBSTR(DATA,1,50) DATA 
                   FROM APP_RMB.MARKET_POINT_CONTACTS mc,
                        APP_COR.CONTACTS c
                   WHERE mc.con_id = c.id
                   AND   c.type = 'M' -- e-mail efaktury   
                   AND   mc.mkp_id =  i.mkp_id   
                   UNION
                   SELECT SUBSTR(DATA,1,50) DATA 
                   FROM APP_RMB.ACCEPTOR_CONTACTS ac,
                        APP_COR.CONTACTS c
                   WHERE ac.con_id = c.id
                   AND   c.type = 'M' -- e-mail efaktury   
                   AND   ac.acc_id = i.acc_id  
                   );  

                 -- pobierz email mid
                 SELECT DATA BULK COLLECT INTO vt_mid 
                 FROM (
                   SELECT SUBSTR(DATA,1,50) DATA 
                   FROM APP_RMB.MARKET_POINT_CONTACTS mc,
                        APP_COR.CONTACTS c
                   WHERE mc.con_id = c.id
                   AND   c.type = 'I' -- e-mail efaktury   
                   AND   mc.mkp_id =  i.mkp_id   
                   UNION
                   SELECT SUBSTR(DATA,1,50) DATA 
                   FROM APP_RMB.ACCEPTOR_CONTACTS ac,
                        APP_COR.CONTACTS c
                   WHERE ac.con_id = c.id
                   AND   c.type = 'I' -- e-mail efaktury   
                   AND   ac.acc_id = i.acc_id  
                   );                              
              
                 ADD_NAV_CUST(
                    A_CODE              => i.CODE,
                    A_NAME              => i.NAME,
                    A_AD_LINE_1         => i.AD_LINE_1, 
                    A_CITY_CODE         => i.CITY_CODE,
                    A_CITY_NAME         => i.CITY_NAME,
                    A_VAT_REG_NUM       => i.VAT_REG_NUM,
                    A_PHONE             => i.PHONE, 
                    A_TYPE              => i.CUSTOMER_TYPE,
                    A_ACCOUNT           => v_account.acc_no,
                    A_CLEAR_TYPE        => 'PD',
                    A_BANK_ACCOUNT_NUM  => i.BANK_ACCOUNT,
                    A_TRANSFER_DESC     => i.TRANSFER_REMARKS,
                    A_ACC_CODE          => i.ACC_CODE, 
                    A_ACC_NAME          => i.ACC_NAME,
                    A_ACC_AD_LINE_1     => i.ACC_AD_LINE_1,
                    A_ACC_CITY_CODE     => i.ACC_CITY_CODE,
                    A_ACC_CITY_NAME     => i.ACC_CITY_NAME,
                    A_ACC_VAT_REG_NUM   => i.ACC_VAT_REG_NUM,
                    A_ACC_PHONE         => i.ACC_PHONE,
                    A_ACC_TYPE          => i.ACC_CUSTOMER_TYPE,
                    A_ACC_ACCOUNT       => v_acc_account.acc_no,
                    A_ACC_CLEAR_TYPE    => v_acc_clear_type,
                    A_ACC_BANK_ACCOUNT_NUM  => i.ACC_BANK_ACCOUNT,
                    A_ACC_TRANSFER_DESC => i.ACC_TRANSFER_REMARKS,
                    A_TASK_ID           => a_task_id,
                    A_E_INV_PERMISSION  => i.e_inv_permission, 
                    A_SENSITIVE_PARTNER => NULL,  
                    A_COR_AD_LINE_1     => i.corr_ad_line_1,      
                    A_COR_CITY_CODE     => i.corr_city_id,      
                    A_COR_CITY_NAME     => i.corr_city_name,      
                    A_EMAIL_FV_1        => TBL_NVL(vt_efv,1),                   
                    A_EMAIL_FV_2        => TBL_NVL(vt_efv,2),   
                    A_EMAIL_FV_3        => TBL_NVL(vt_efv,3),            
                    A_EMAIL_FV_4        => TBL_NVL(vt_efv,4),            
                    A_EMAIL_FV_5        => TBL_NVL(vt_efv,5),           
                    A_EMAIL_MID_1       => TBL_NVL(vt_mid,1),          
                    A_EMAIL_MID_2       => TBL_NVL(vt_mid,2),   
                    A_EMAIL_SXO         => NULL                      
                    );
              END LOOP;


EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_CAV');
END;

PROCEDURE exp_tnt_einv (
  pc_nr_fakur    VARCHAR2,                          /*pn_task_id number,*/
  pn_cust_id     NUMBER
)
IS
    vct_faktury     varchars2_type;
    vct_faktury_data varchars2_type;
    vn_index        NUMBER;
    v_inc_account   r_acc;
    v_comp_code     VARCHAR2(8) := '104944'; /* TNT */
    v_book_date     DATE;
BEGIN

   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','EXP_TNT_EINV',NULL);
    -- pobierz ID dostawcy TNT w nav NAV
    BEGIN
      v_comp_code := PK_UTILS.GET_PARAM_VALUE('NAV_TNT_PROVIDER_ID');
    EXCEPTION
    WHEN OTHERS THEN
      v_comp_code:= '104944';  
    END;   
   
      v_inc_account := GET_ACCOUNT('TNT_CONS_INCOME_ACC');
      

      PK_AUDIT.LOCK_OBJECT('APP_SWP.NAV_INV');
   
          DELETE FROM APP_SWP.NAV_INV;
          
          vct_faktury_data := pk_mod_swap_nav.SPLIT (pc_nr_fakur, ';');

          v_book_date := to_date(vct_faktury_data(0), 'YYYY-MM-DD');
          vct_faktury := pk_mod_swap_nav.SPLIT (vct_faktury_data(1), ',');
          vn_index := vct_faktury.FIRST;

          WHILE (vn_index <= vct_faktury.LAST)
          LOOP

             der_einv (TRIM (BOTH ' ' FROM vct_faktury (vn_index)),
                       v_book_date,
                       v_inc_account,
                       GET_ACCOUNT('TNT_CONS_VAT_ACC'),
                       'TNT_CONS',
                       v_comp_code);

                vn_index := vct_faktury.NEXT (vn_index);
          END LOOP;
     
     PK_AUDIT.UNLOCK_OBJECT('APP_SWP.NAV_INV');
      
     PK_AUDIT.LOCK_OBJECT('APP_SWP.NAV_CUST');
     
         DELETE FROM APP_SWP.NAV_CUST;      
       
         ADD_NAV_CUST(
            A_CODE              => v_comp_code,
            A_NAME              => 'TNT EXPRESS WORLDWIDE',
            A_AD_LINE_1         => 'UL ¯WIRKI I WIGURY 1', 
            A_CITY_CODE         => '02-143',
            A_CITY_NAME         => 'WARSZAWA',            
            A_VAT_REG_NUM       => '1180029298',
            A_PHONE             => NULL, 
            A_TYPE              => 'D',
            A_ACCOUNT           => v_inc_account.acc_no,
            A_CLEAR_TYPE        => '00',
            A_BANK_ACCOUNT_NUM  => NULL,
            A_TRANSFER_DESC     => NULL,
            A_ACC_CODE          => NULL,
            A_ACC_NAME          => NULL,
            A_ACC_AD_LINE_1     => NULL,
            A_ACC_CITY_CODE     => NULL,
            A_ACC_CITY_NAME     => NULL,  
            A_ACC_VAT_REG_NUM   => NULL,
            A_ACC_PHONE         => NULL,
            A_ACC_TYPE          => NULL,
            A_ACC_ACCOUNT       => NULL,
            A_ACC_CLEAR_TYPE    => NULL,
            A_ACC_BANK_ACCOUNT_NUM  => NULL, 
            A_ACC_TRANSFER_DESC => NULL,
            A_TASK_ID           => pn_cust_id, 
            A_E_INV_PERMISSION  => NULL, 
            A_SENSITIVE_PARTNER => NULL,  
            A_COR_AD_LINE_1     => NULL,      
            A_COR_CITY_CODE     => NULL,      
            A_COR_CITY_NAME     => NULL,      
            A_EMAIL_FV_1        => NULL,                   
            A_EMAIL_FV_2        => NULL,   
            A_EMAIL_FV_3        => NULL,            
            A_EMAIL_FV_4        => NULL,            
            A_EMAIL_FV_5        => NULL,           
            A_EMAIL_MID_1       => NULL,          
            A_EMAIL_MID_2       => NULL,   
            A_EMAIL_SXO         => NULL              
            );
        
   PK_AUDIT.UNLOCK_OBJECT('APP_SWP.NAV_CUST');

EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_ETE');
END;

PROCEDURE exp_aff_inv_vb (
  pn_transf_id   NUMBER,
  pn_cust_id     NUMBER,
  pn_cust_all_id NUMBER,
  transf_date    DATE)
IS
  vct_faktury     varchars2_type;
  vn_index        NUMBER;
  v_export_id     NUMBER;
BEGIN

   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','EXP_AFF_INV_VB',NULL);
   
      SELECT VISE_SEQ.NEXTVAL
	  INTO v_export_id
	  FROM DUAL;
      
      PK_AUDIT.LOCK_OBJECT('APP_SWP.NAV_INV');
      DELETE FROM APP_SWP.NAV_INV;      
      
          /*wybranie wszystkich wskazanych sesji*/
          FOR K IN (SELECT S.ID
                    FROM   APP_VFIN.INVOICE_SESSIONS S
                    WHERE  S.STATUS = 'A')
          LOOP
               pk_mod_swap_nav.aff_inv_vb (K.ID,
                                           transf_date,
                                           v_export_id);

            vn_index := vct_faktury.NEXT (vn_index);
          END LOOP;  

   PK_AUDIT.UNLOCK_OBJECT('APP_SWP.NAV_INV');

   PK_AUDIT.LOCK_OBJECT('APP_SWP.NAV_CUST');
   
      DELETE FROM APP_SWP.NAV_CUST;
      /*tworzenie pliku klientow tnt*/
        cust_aff_vb (a_export_id    => v_export_id,
                     a_task_id      => pn_cust_id,
                     a_task_all_id  => pn_cust_all_id);
                     
   PK_AUDIT.UNLOCK_OBJECT('APP_SWP.NAV_CUST');                     

EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_EAI');
END;

PROCEDURE exp_ups_einv (pc_nr_fakur    VARCHAR2, /*pn_task_id number,*/
                       pn_cust_id     NUMBER)
IS
    vct_faktury     varchars2_type;
    vct_faktury_data varchars2_type;
    vn_index        NUMBER;
    v_inc_account   r_acc;
    v_comp_code     VARCHAR2(8) := '100086'; /* UPS */
    v_book_date     DATE;
BEGIN

   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','EXP_UPS_EINV',NULL);
   
      v_inc_account := GET_ACCOUNT('UPS_CONS_INCOME_ACC');
      
      PK_AUDIT.LOCK_OBJECT('APP_SWP.NAV_INV');
   
          DELETE FROM APP_SWP.NAV_INV;

          vct_faktury_data := pk_mod_swap_nav.SPLIT (pc_nr_fakur, ';');

          v_book_date := to_date(vct_faktury_data(0), 'YYYY-MM-DD');
          vct_faktury := pk_mod_swap_nav.SPLIT (vct_faktury_data(1), ',');
          vn_index := vct_faktury.FIRST;

          WHILE (vn_index <= vct_faktury.LAST)
          LOOP

             der_einv (TRIM (BOTH ' ' FROM vct_faktury (vn_index)),
                       v_book_date,
                       v_inc_account,
                       GET_ACCOUNT('UPS_CONS_VAT_ACC'),
                       'UPS_CONS',
                       v_comp_code);

                vn_index := vct_faktury.NEXT (vn_index);
          END LOOP;
      
     PK_AUDIT.UNLOCK_OBJECT('APP_SWP.NAV_INV');
      
     PK_AUDIT.LOCK_OBJECT('APP_SWP.NAV_CUST');
     
         DELETE FROM APP_SWP.NAV_CUST;      
   
         ADD_NAV_CUST(
            A_CODE              => v_comp_code,
            A_NAME              => 'UPS POLSKA SP. Z O.O.',
            A_AD_LINE_1         => 'UL  PRADZYNSKIEGO 1/3',
            A_CITY_CODE         => '01-222',
            A_CITY_NAME         => 'WARSZAWA',
            A_VAT_REG_NUM       => '5221004200',
            A_PHONE             => NULL,
            A_TYPE              => 'D',
            A_ACCOUNT           => v_inc_account.acc_no,
            A_CLEAR_TYPE        => '00',
            A_BANK_ACCOUNT_NUM  => NULL,
            A_TRANSFER_DESC     => NULL,
            A_ACC_CODE          => NULL,
            A_ACC_NAME          => NULL,
            A_ACC_AD_LINE_1     => NULL,
            A_ACC_CITY_CODE     => NULL,
            A_ACC_CITY_NAME     => NULL,
            A_ACC_VAT_REG_NUM   => NULL,
            A_ACC_PHONE         => NULL,
            A_ACC_TYPE          => NULL,
            A_ACC_ACCOUNT       => NULL,
            A_ACC_CLEAR_TYPE    => NULL,
            A_ACC_BANK_ACCOUNT_NUM  => NULL, 
            A_ACC_TRANSFER_DESC => NULL,
            A_TASK_ID           => pn_cust_id,
            A_E_INV_PERMISSION  => NULL, 
            A_SENSITIVE_PARTNER => NULL,  
            A_COR_AD_LINE_1     => NULL,      
            A_COR_CITY_CODE     => NULL,      
            A_COR_CITY_NAME     => NULL,      
            A_EMAIL_FV_1        => NULL,                   
            A_EMAIL_FV_2        => NULL,   
            A_EMAIL_FV_3        => NULL,            
            A_EMAIL_FV_4        => NULL,            
            A_EMAIL_FV_5        => NULL,           
            A_EMAIL_MID_1       => NULL,          
            A_EMAIL_MID_2       => NULL,   
            A_EMAIL_SXO         => NULL            
            );
        
   PK_AUDIT.UNLOCK_OBJECT('APP_SWP.NAV_CUST');        

EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_EUE');
END;

PROCEDURE clients_inv (
  a_input_date      IN       DATE,
  a_doc_type        IN       VARCHAR2,
  a_source_type     IN       VARCHAR2  
)
IS
   v_prd_type       app_stc.product_types.TYPE%TYPE;

   v_c_account      r_acc;
   v_vat_account    r_acc;
   v_cli_inc_account r_acc;
   v_cli_add_account r_acc;
   v_account        r_acc;
   
   v_cli_ex_account r_acc;

   v_change         BOOLEAN := FALSE;
   v_debug_string   eagle.adm_audit_modules.arg_string%TYPE;
   v_proforma_no    app_fin.invoices.invoice_number%TYPE;
   v_proforma_id    app_fin.invoices.ID%TYPE;
   
   v_account_declared BOOLEAN := FALSE;
   v_payment_type VARCHAR2(32); 
   
   v_project          APP_SWP.NAV_INV.PROJECT%TYPE;
   v_cpack_par_count  NUMBER;  
   
   v_line_name_std    APP_FIN.INVOICE_LINES.NAME%TYPE;
   v_line_name_2      APP_FIN.INVOICE_LINES.NAME%TYPE;

   v_unit_price  NUMBER;
   v_quantity    NUMBER;
BEGIN

   v_debug_string := 'doc='||a_doc_type||' src='||a_source_type||' date='||to_char(a_input_date);
   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','CLIENTS_INV',v_debug_string);
   
   v_c_account      := GET_ACCOUNT('CLIENT_ACCOUNT');
   v_vat_account    := GET_ACCOUNT('VAT_ACCOUNT');
   v_cli_ex_account := GET_ACCOUNT('CLI_DC_ACCOUNT_EX');
   
   IF a_doc_type = 'C' THEN
      v_cli_inc_account := GET_ACCOUNT('CLI_INCOME_ACCOUNT');
      v_cli_add_account := GET_ACCOUNT('CLI_DC_ACCOUNT');
   ELSIF a_doc_type = 'K' THEN
      v_cli_inc_account := GET_ACCOUNT('CLI_INCOME_ACCOUNT_C');
      v_cli_add_account := GET_ACCOUNT('CLI_DC_ACCOUNT_C');
   ELSE
      PK_ERROR.RAISE_ERROR(-20008, 'Nieznany parametr doc_type='||a_doc_type);
   END IF;
   
   FOR cinv IN (SELECT com.code comp_code,
                       inv.invoice_number invoice_number,
                  DECODE (inv.TYPE, 'IC', 'C', 'CIC', 'K', inv.TYPE) dok,
                  inv.TYPE inv_type,
                  SUM(NVL (inv_lin.gross_amount, 0)) * (-1) netto,
                  SUM(NVL (inv_lin.vat_amount, 0)) * (-1) vat_val,
                  inv.invoice_date,
                  nvl(inv.PAYMENT_DATE, inv.INVOICE_DATE) pay_date,
                  inv.ID inv_id,
                  inv_lin.source, 
                  inv.vat_date,
                  null invoicing_type,
                  CR.COR_REASON_NAV_CODE  COR_REASON_NAV_CODE,
                      inv.sale_date
             FROM app_emi.customers com,
                  app_fin.invoices inv,
                  app_fin.invoice_lines inv_lin,
                  APP_FIN.COR_REASON CR 
            WHERE inv.cus_id = com.ID
              AND inv.ID = inv_lin.inv_id
              AND CR.ID (+)= inv.COR_REASON
              AND inv.invoice_number IS NOT NULL
              AND inv_lin.gross_amount != 0
              AND ((    (   (inv.TYPE = 'IC' AND a_doc_type = 'C')
                         OR (    inv.TYPE = 'CIC'
                             AND inv_lin.pos_type = 'D'
                             AND a_doc_type = 'K'
                            )
                        )
                    AND inv_lin.source_type = 'ORD'
                    AND a_source_type = 'ORD'
                   )
                  )
              AND TRUNC (invoice_date) = a_input_date
              AND inv.status = 'P'
            GROUP BY com.code,
                     inv.invoice_number,
                     DECODE (inv.TYPE, 'IC', 'C', 'CIC', 'K', inv.TYPE),
                     inv.TYPE,        
                     inv.invoice_date,
                     nvl(inv.PAYMENT_DATE, inv.INVOICE_DATE),
                     inv.ID,
                     inv_lin.source,
                     inv.vat_date ,
                     CR.COR_REASON_NAV_CODE,
                      inv.sale_date
         UNION
             SELECT   com.code comp_code,
                      inv.invoice_number invoice_number,
                      DECODE (inv.TYPE, 'IC', 'C', 'CIC', 'K', inv.TYPE) dok,
                      inv.type inv_type,
                      SUM (NVL (inv_lin.gross_amount, 0) * (-1)) netto,
                      SUM (NVL (inv_lin.vat_amount, 0) * (-1)) vat_val,
                      inv.invoice_date,
                      nvl(inv.PAYMENT_DATE, inv.INVOICE_DATE) pay_date,
                      inv.ID inv_id,
                      inv_lin.source,
                      inv.vat_date,
                      null invoicing_type,
                      CR.COR_REASON_NAV_CODE  COR_REASON_NAV_CODE,
                      inv.sale_date
                 FROM app_emi.customers com,
                      app_fin.invoices inv,
                      app_fin.invoice_lines inv_lin,
                      APP_FIN.COR_REASON CR 
                WHERE inv.cus_id = com.ID
                  AND inv.ID = inv_lin.inv_id
                  AND CR.ID (+) = inv.COR_REASON
                  AND inv.invoice_number IS NOT NULL
                  AND inv_lin.gross_amount != 0
                  AND (    (   (inv.TYPE = 'IC' AND a_doc_type = 'C')
                            OR (    inv.TYPE = 'CIC'
                                AND inv_lin.pos_type = 'D'
                                AND a_doc_type = 'K'
                               )
                           )
                       AND inv_lin.source_type = 'TSK'
                       AND a_source_type = 'TSK'
                      )
                  AND TRUNC (invoice_date) = a_input_date
                  AND inv.status = 'P'
             GROUP BY com.code,
                      inv.invoice_number,
                      DECODE (inv.TYPE, 'IC', 'C', 'CIC', 'K', inv.TYPE),
                      inv.type,
                      inv.invoice_date,
                      nvl(inv.PAYMENT_DATE, inv.INVOICE_DATE),
                      inv.ID,
                      inv_lin.source,
                      inv.vat_date,
                      CR.COR_REASON_NAV_CODE,
                      inv.sale_date
         UNION -- GAFA a_source_type = 'IP'
             SELECT   com.code comp_code,
                      inv.invoice_number invoice_number,
                      DECODE (inv.TYPE, PK_MOD_INV_PROPOSALS.GC_INV_TYPE_ICG, 'C',PK_MOD_INV_PROPOSALS.GC_INV_TYPE_CICG, 'K', inv.TYPE) dok,
                      inv.type inv_type,
                      SUM (NVL (inv_lin.gross_amount, 0) * (-1)) netto,
                      SUM (NVL (inv_lin.vat_amount, 0) * (-1)) vat_val,
                      inv.invoice_date,
                      nvl(inv.PAYMENT_DATE, inv.INVOICE_DATE) pay_date,
                      inv.ID inv_id,
                      inv_lin.source,
                      inv.vat_date,
                      ip.invoicing_type,
                      CR.COR_REASON_NAV_CODE  COR_REASON_NAV_CODE,
                      inv.sale_date
                 FROM app_emi.customers com,
                      app_fin.invoices inv,
                      app_fin.invoice_lines inv_lin,
                      app_fin.invoice_proposals ip,
                      APP_FIN.COR_REASON CR 
                WHERE inv.cus_id = com.ID
                  AND inv.ID = inv_lin.inv_id
                  AND inv_lin.source = ip.id
                  AND CR.ID (+) = inv.COR_REASON
                  AND inv.invoice_number IS NOT NULL
                  AND inv_lin.gross_amount != 0
                  AND (   (inv.TYPE = PK_MOD_INV_PROPOSALS.GC_INV_TYPE_ICG AND a_doc_type = 'C')
                        OR (    inv.TYPE = PK_MOD_INV_PROPOSALS.GC_INV_TYPE_CICG
                            AND inv_lin.pos_type = 'D'
                            AND a_doc_type = 'K'
                           )
                       )
                  AND inv_lin.source_type = a_source_type
                  AND a_source_type = PK_MOD_INV_PROPOSALS.GC_INV_SRC_TYPE_IP
                  AND TRUNC (invoice_date) = a_input_date
                  AND inv.status = 'P'
             GROUP BY com.code,
                      inv.invoice_number,
                      DECODE (inv.TYPE, PK_MOD_INV_PROPOSALS.GC_INV_TYPE_ICG, 'C',PK_MOD_INV_PROPOSALS.GC_INV_TYPE_CICG, 'K', inv.TYPE),
                      inv.type,
                      inv.invoice_date,
                      nvl(inv.PAYMENT_DATE, inv.INVOICE_DATE),
                      inv.ID,
                      inv_lin.source,
                      inv.vat_date,
                      ip.invoicing_type,
                      CR.COR_REASON_NAV_CODE,
                      inv.sale_date
          ORDER BY invoice_number
          )
   LOOP
   
     IF cinv.invoicing_type = PK_MOD_INV_PROPOSALS.GC_INVOICING_TYPE_SPS THEN
       v_line_name_std := NVL(PK_UTL_REPORTS.GET_DOC_LINE_NAME_SPS_IP (cinv.inv_id),'Us³ugi reklamowe pozosta³e');
     ELSE 
       v_line_name_std := NULL;
     END IF;
     
     t$invoice.set_status (cinv.inv_id, 'E');
     
     IF a_source_type <> PK_MOD_INV_PROPOSALS.GC_INV_SRC_TYPE_IP OR
       (a_source_type = PK_MOD_INV_PROPOSALS.GC_INV_SRC_TYPE_IP AND
        NVL(EAGLE.T$INVOICE_PROPOSAL.GET_PAR_MAN_VAL ('PLATN_PROF', cinv.source), 'NULL') = 'Tak')
     THEN
       v_proforma_no := GET_PROFORMA_NUMBER(cinv.inv_id);
     END IF;
     
     IF T$INVOICE.EXISTS_CRD_EX_INVOICE(cinv.inv_id) = 'Y' THEN
        v_payment_type := 'PD';
     ELSE
        v_payment_type := '00';
     END IF;
     
     IF a_source_type = PK_MOD_INV_PROPOSALS.GC_INV_SRC_TYPE_IP THEN
       v_payment_type := '00';
       IF cinv.invoicing_type = PK_MOD_INV_PROPOSALS.GC_INVOICING_TYPE_SELECT THEN
         v_c_account   := GET_ACCOUNT('CLIENT_ACCOUNT_SSEL');
         v_vat_account := GET_ACCOUNT('VAT_ACCOUNT');
       ELSIF cinv.invoicing_type = PK_MOD_INV_PROPOSALS.GC_INVOICING_TYPE_SPS THEN
         v_c_account   := GET_ACCOUNT('EXTERNAL_CUSTOMER_ACCOUNT');
         v_vat_account := GET_ACCOUNT('VAT_ACCOUNT');
       ELSE
        PK_ERROR.RAISE_ERROR(-20008, 'Nieznany typ fakturowania dla NAVISION='||cinv.invoicing_type);
       END IF;
     END IF;


     IF a_source_type = PK_MOD_INV_PROPOSALS.GC_INV_SRC_TYPE_IP THEN

       ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(cinv.INV_TYPE),
                    A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.INV_TYPE), -- ???
                    A_DOC_TYPE       => GET_DOC_TYPE(cinv.INV_TYPE), 
                    A_PAYMENT_TYPE   => v_payment_type,
                    A_ACCOUNT        => v_c_account.acc_no,
                    A_MPK            => NULL,                  
                    A_TAX            => NULL,
                    A_PRODUCT        => NULL,
                    A_BOOK_DATE      => cinv.invoice_date,
                    A_AMOUNT         => cinv.netto * (-1),
                    A_CODE           => cinv.comp_code,
                    A_DOC_NUMBER     => cinv.invoice_number,
                    A_VAT_RATE       => NULL,
                    A_ORD_ID         => cinv.source,
                    A_PROFORMA_NO    => v_proforma_no,
                    A_INVOICE_DATE   => cinv.invoice_date,
                    A_PAYMENT_DATE   => cinv.pay_date,
                    A_PROJECT        => v_c_account.project,
                    A_VAT_DATE       => cinv.vat_date, 
                    A_SALE_DATE      => cinv.vat_date,
                    A_COR_REASON_NAV_CODE => cinv.COR_REASON_NAV_CODE);
                    
         ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(cinv.INV_TYPE),
                      A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.INV_TYPE), -- ???
                      A_DOC_TYPE       => GET_DOC_TYPE(cinv.INV_TYPE), 
                      A_PAYMENT_TYPE   => v_payment_type,
                      A_ACCOUNT        => v_vat_account.acc_no,
                      A_MPK            => NULL,
                      A_TAX            => NULL, 
                      A_PRODUCT        => NULL,
                      A_BOOK_DATE      => cinv.invoice_date,
                      A_AMOUNT         => cinv.vat_val,
                      A_CODE           => cinv.comp_code,
                      A_DOC_NUMBER     => cinv.invoice_number,
                      A_VAT_RATE       => NULL,
                      A_ORD_ID         => cinv.source,
                      A_PROFORMA_NO    => v_proforma_no,
                      A_INVOICE_DATE   => cinv.invoice_date,
                      A_PAYMENT_DATE   => cinv.pay_date,
                      A_PROJECT        => v_vat_account.project,
                      A_VAT_DATE       => cinv.vat_date, 
                      A_SALE_DATE      => cinv.vat_date,
                      A_COR_REASON_NAV_CODE =>null);
        ELSE

          ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(cinv.INV_TYPE),
                      A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.INV_TYPE), -- ???
                      A_DOC_TYPE       => GET_DOC_TYPE(cinv.INV_TYPE), 
                      A_PAYMENT_TYPE   => v_payment_type,
                      A_ACCOUNT        => v_c_account.acc_no,
                      A_MPK            => NULL,                  
                      A_TAX            => NULL,
                      A_PRODUCT        => NULL,
                      A_BOOK_DATE      => cinv.invoice_date,
                      A_AMOUNT         => cinv.netto * (-1),
                      A_CODE           => cinv.comp_code,
                      A_DOC_NUMBER     => cinv.invoice_number,
                      A_VAT_RATE       => NULL,
                      A_ORD_ID         => cinv.source,
                      A_PROFORMA_NO    => v_proforma_no,
                      A_INVOICE_DATE   => cinv.invoice_date,
                      A_PAYMENT_DATE   => cinv.pay_date,
                      A_PROJECT        => v_c_account.project,
                      A_VAT_DATE       => cinv.vat_date,
                      A_SALE_DATE      => cinv.sale_date,
                      A_COR_REASON_NAV_CODE => cinv.COR_REASON_NAV_CODE);
                      
           ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(cinv.INV_TYPE),
                        A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.INV_TYPE), -- ???
                        A_DOC_TYPE       => GET_DOC_TYPE(cinv.INV_TYPE), 
                        A_PAYMENT_TYPE   => v_payment_type,
                        A_ACCOUNT        => v_vat_account.acc_no,
                        A_MPK            => NULL,
                        A_TAX            => NULL, 
                        A_PRODUCT        => NULL,
                        A_BOOK_DATE      => cinv.invoice_date,
                        A_AMOUNT         => cinv.vat_val,
                        A_CODE           => cinv.comp_code,
                        A_DOC_NUMBER     => cinv.invoice_number,
                        A_VAT_RATE       => NULL,
                        A_ORD_ID         => cinv.source,
                        A_PROFORMA_NO    => v_proforma_no,
                        A_INVOICE_DATE   => cinv.invoice_date,
                        A_PAYMENT_DATE   => cinv.pay_date,
                        A_PROJECT        => v_vat_account.project,
                        A_SALE_DATE      => CINV.SALE_DATE,
                        A_VAT_DATE       => cinv.vat_date);

        END IF;                      
             

    FOR cinv_in IN (SELECT NVL (il.gross_amount, 0) * (-1) netto,
                           NVL (il.vat_amount, 0) * (-1) vat_val,
                           decode(a_source_type, 'TSK', 'TSKP', il.code) prd_id,
                           il.code,
                           il.code_type,
                           il.VAT,
                           il.source_type,
                           il.source,
                           pr.product_nav_code,
                           SUBSTR(IL.NAME,1,50)    NAME,
                           SUBSTR(IL.NAME,51,50)   NAME_2,
                           IL.UNIT_PRICE           UNIT_PRICE,
                           IL.NET_AMOUNT           NET_AMOUNT,
                           IL.QUANTITY_PRINTED     QUANTITY   ,         
                           IL.UNIT_PRINTED         UNIT_PRINTED,       
                           IL.UNIT                 UNIT,       
                           CR.COR_REASON_NAV_CODE  COR_REASON_NAV_CODE,
                           IL.POS_NUM              POS_NUM
                    FROM      APP_FIN.INVOICES I
                         JOIN APP_FIN.INVOICE_LINES IL ON I.ID = IL.INV_ID
                         LEFT JOIN APP_FIN.INVOICE_PROPOSALS ip ON (IP.ID = IL.SOURCE AND IL.SOURCE_TYPE = PK_MOD_INV_PROPOSALS.GC_INV_SRC_TYPE_IP)
                         LEFT JOIN APP_COR.PROGRAMS pr on PR.CODE = IP.PROGRAM_CODE
                         LEFT JOIN APP_FIN.COR_REASON CR ON CR.ID = I.COR_REASON
                    WHERE --il.vat_amount != 0 AND
                        il.GROSS_AMOUNT != 0
                    AND i.id = cinv.inv_id
                    AND ((i.TYPE IN ('IC', PK_MOD_INV_PROPOSALS.GC_INV_TYPE_ICG) AND il.pos_type = 'I' AND a_doc_type = 'C')
                          OR (i.TYPE IN ('CIC', PK_MOD_INV_PROPOSALS.GC_INV_TYPE_CICG) AND il.pos_type = 'D' AND a_doc_type = 'K')
                         )                                                          
                    ORDER BY prd_id)
    LOOP

          v_account_declared := FALSE;
          v_change := FALSE;
          
          IF a_source_type = PK_MOD_INV_PROPOSALS.GC_INV_SRC_TYPE_IP THEN
            v_prd_type := cinv_in.product_nav_code;
                        
            IF cinv.invoicing_type = PK_MOD_INV_PROPOSALS.GC_INVOICING_TYPE_SELECT THEN
              IF cinv_in.code_type = 'D' AND cinv_in.code = 'SRV_ONE' THEN 
                v_account   := GET_ACCOUNT('SELECT_CUST_REVE_SETUP_ACCOUNT');
              ELSIF cinv_in.code_type IN ('F') OR 
                   (cinv_in.code_type = 'D' AND cinv_in.code = 'SRV') OR 
                   (cinv_in.code_type = 'D' AND cinv_in.code = 'DF') OR
--                   (cinv_in.code_type = 'D' AND cinv_in.code = 'SRV_ONE') OR 
                   (cinv_in.code_type = 'D' AND cinv_in.code = 'CI') THEN 
                v_account   := GET_ACCOUNT('SELECT_CUST_REVE_ACCOUNT');
              ELSIF cinv_in.code_type = 'PE' THEN 
                v_account   := GET_ACCOUNT('SELECT_CUST_TURN_3_PARTY_ACCOUNT');
              ELSIF cinv_in.code_type = '?' AND cinv_in.code = '?' THEN 
                v_account   := GET_ACCOUNT('SELECT_CUST_TURN_GOODS_ACCOUNT');
              ELSIF cinv_in.code_type = '?' AND cinv_in.code = '?' THEN 
                v_account   := GET_ACCOUNT('SELECT_CUST_REVE_DISTR_ACCOUNT');
              ELSE
                PK_ERROR.RAISE_ERROR(-20008, 'Nieznana kombinacja warunków code_type: '||cinv_in.code_type||' i code: '||cinv_in.code||' dla typu fakturowania: '||cinv.invoicing_type);
              END IF;
            ELSIF cinv.invoicing_type = PK_MOD_INV_PROPOSALS.GC_INVOICING_TYPE_SPS THEN
              IF cinv_in.code_type = 'IP' THEN
                v_account   := GET_ACCOUNT('EXT_CUS_TURN_ACCOUNT');
              ELSIF cinv_in.code_type = 'D' AND cinv_in.code = 'SRV' THEN 
                v_account   := GET_ACCOUNT('EXT_CUS_REVE_ACCOUNT');
              ELSIF cinv_in.code_type = 'D' AND cinv_in.code = 'SRV_ONE' THEN 
                v_account   := GET_ACCOUNT('EXT_CUS_REVE_SETUP_ACCOUNT');
              ELSE
                PK_ERROR.RAISE_ERROR(-20008, 'Nieznana kombinacja warunków code_type: '||cinv_in.code_type||' i code: '||cinv_in.code||' dla typu fakturowania: '||cinv.invoicing_type);
              END IF;
            ELSE
              PK_ERROR.RAISE_ERROR(-20008, 'Nieznany typ fakturowania dla NAVISION: '||cinv.invoicing_type);
            END IF;
            v_account_declared := TRUE;
          ELSE
            v_prd_type := cinv_in.PRD_ID;
            IF cinv_in.prd_id LIKE '%DC%' OR cinv_in.prd_id IN ('C', 'DOP')  THEN
               v_prd_type := get_min_inv_product(cinv.inv_id);
               v_change := TRUE;
            END IF;
            
            IF cinv_in.prd_id IN ('CI','CR') THEN -- OP£ATA MANIPULACYJNA ZA WYDANIE / PRZEKSIÊGOWANIE KARTY
               v_proforma_id := get_proforma_id(cinv.inv_id);
               v_prd_type := get_min_inv_product(v_proforma_id);
               -- jeœli zwrócone jest id produktu, to 
               DECLARE
                 v_cnt NUMBER;
               BEGIN
                 SELECT NVL(COUNT(*),0)
                 INTO   v_cnt
                 FROM   APP_STC.PRODUCT_TYPES PT
                 WHERE  PT.TYPE = v_prd_type;
                 IF v_cnt =0 THEN 
                   SELECT TYPE
                   INTO   v_prd_type 
                   FROM   APP_STC.PRODUCTS P
                   WHERE  P.ID = v_prd_type;
                 END IF;
               EXCEPTION
                 WHEN NO_DATA_FOUND THEN
                   NULL;
               END;
               v_account := v_cli_ex_account;
               v_account_declared := TRUE;
            END IF;

            IF cinv_in.prd_id LIKE '%TSKP%'
            THEN --SUMA WSZYSTKICH PÓL NA FAKTURZE DLA WYMIAN UKRYTA POD T¥ KSYW¥
                SELECT prd_id
                  INTO v_prd_type
                  --produkt zamieniony na taki, którego w tasku by³a najwiêksza iloœæ
                FROM   (SELECT   t$product.get_type (tp.prd_id) prd_id,
                                 SUM (tp.quantity
                                      * t$product.get_value (tp.prd_id)
                                     )
                            FROM app_tsk.task_products tp
                           WHERE tsk_id = (SELECT DISTINCT SOURCE
                                                      FROM app_fin.invoice_lines il
                                                     WHERE il.inv_id = cinv.inv_id
                                                                                  --tu stawiæ numer
                                         )
                        GROUP BY t$product.get_type (tp.prd_id)
                        ORDER BY 2 DESC)
                 WHERE ROWNUM <= 1;

            END IF;
             
            IF v_prd_type LIKE '%DF%' THEN
               v_prd_type := '1';
               v_change := TRUE;
            END IF;

            -- na proœbê Macka Fi 20120601:
            IF v_prd_type LIKE '%DC%' THEN
               v_prd_type := '1';
               v_change := TRUE;
            END IF;

            
            IF v_prd_type LIKE 'COM%' OR
               v_prd_type LIKE '%TSKP%'
            THEN
               v_prd_type := '1';
            END IF;            
          END IF;    

          IF v_account_declared THEN

            NULL; -- ustawione wczeœniej

          ELSIF cinv_in.prd_id IN ('DOP') THEN 
          
            v_account := GET_ACCOUNT('CLI_DC_ACCOUNT_ONLINE_PMT');
          
          ELSIF cinv.COMP_CODE LIKE 'COM%' THEN
          
                IF a_doc_type = 'C' THEN
                   v_account := GET_PRD_ACCOUNT('CLI_PRD_TURN_ACCOUNT', 'COM');
                ELSIF a_doc_type = 'K' THEN
                   v_account := GET_PRD_ACCOUNT('CLI_PRD_TURN_ACCOUNT_COR', 'COM');
                END IF;
          
          ELSIF a_source_type = 'TSK' THEN
          
             v_account := GET_ACCOUNT('EXCHANGE_INVOICES');
             
          ELSIF (v_prd_type IN ('CC')) OR (v_change = TRUE) THEN
          
             v_change := FALSE;
             v_account := v_cli_add_account;
             IF v_prd_type IN ('25', '26','27','28', '32') THEN
                v_account := GET_ACCOUNT('CLI_DC_ACCOUNT_CRD');
             END IF;
          
          ELSIF v_prd_type IN ('12', '21') THEN
          
             v_account := GET_ACCOUNT('E_CARD_INVOICES');
             
          ELSIF v_prd_type IN ('25', '26', '27', '28', '32') THEN

             v_account := GET_ACCOUNT('CLI_INCOME_ACCOUNT_CRD');
             
          ELSIF v_prd_type = '14' THEN
          
             v_prd_type := '1';
             IF a_doc_type = 'C' THEN
               v_account := GET_PRD_ACCOUNT('CLI_PRD_TURN_ACCOUNT', '14');
             ELSIF a_doc_type = 'K' THEN
               v_account := GET_PRD_ACCOUNT('CLI_PRD_TURN_ACCOUNT_COR', '14');
             END IF;

          ELSE
             v_account := v_cli_inc_account;
          END IF;  
             
          -- wymiar CPACK na projekcie
          -- dla nie CPACKow pozostaje domyœlny projekt z SCALA_ACCOUNTS - bo tak by³o ;)
          
          v_project := v_account.project;
          -- tylko dla zrodla typu zamowienie                       
          IF cinv_in.source_type = 'ORD' THEN
            -- czy mamy na zam. parametr CARD_PACK
            SELECT COUNT(*) 
            INTO v_cpack_par_count
            FROM APP_EMI.ORDER_PARAMETERS op 
            WHERE op.ord_id = cinv_in.source 
            AND  op.par_code = 'CARD_PACK';
            IF v_cpack_par_count > 0 THEN
              v_project := 'CPACK';
            END IF;         
          END IF;     
          
          v_unit_price := NULL;
          v_quantity   := NULL;
          GET_EXP_NAV_UP_AND_Q  (a_inv_id => cinv.inv_id,
                                 a_doc_type => a_doc_type, -- C -orygina³, K - korekta
                                 a_pos_num  => cinv_in.pos_num,
                                 a_line_quantity_printed => cinv_in.quantity,
                                 a_line_unit => cinv_in.unit,
                                 a_line_netto_amount => cinv_in.net_amount,
                                 a_line_unit_price => cinv_in.unit_price,
                                 p_unit_price => v_unit_price,
                                 p_quantity   => v_quantity);
                  
          
          IF a_source_type = PK_MOD_INV_PROPOSALS.GC_INV_SRC_TYPE_IP THEN
          
             IF v_line_name_std IS NOT NULL THEN
               v_line_name_2 := substr(v_line_name_std,51,50);
             ELSE
               v_line_name_2 := cinv_in.NAME_2;
             END IF;
             
             ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(cinv.INV_TYPE),                                                                
                          A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.INV_TYPE), -- ???                                                          
                          A_DOC_TYPE       => GET_DOC_TYPE(cinv.INV_TYPE),                                                                     
                          A_PAYMENT_TYPE   => v_payment_type,                                                                                  
                          A_ACCOUNT        => v_account.acc_no,                                                                                
                          A_MPK            => NULL,                                                                                            
                          A_TAX            => 'PDO',                                                                                           
                          A_PRODUCT        => v_prd_type,                                                                                      
                          A_BOOK_DATE      => cinv.invoice_date,                                                                               
                          A_AMOUNT         => cinv_in.netto - cinv_in.vat_val,                                                                 
                          A_CODE           => cinv.comp_code,                                                                                  
                          A_DOC_NUMBER     => cinv.invoice_number,                                                                             
                          A_VAT_RATE       => GET_VAT_RATE(cinv_in.vat, cinv.inv_type),                                                        
                          A_ORD_ID         => cinv.source,                                                                                     
                          A_PROFORMA_NO    => v_proforma_no,                                                                                   
                          A_INVOICE_DATE   => cinv.invoice_date,                                                                               
                          A_PAYMENT_DATE   => cinv.pay_date,                                                                                   
                          A_PROJECT        => v_project,                                                                                       
                          a_tgkv_code       => GET_TGKV_CODE_SELL_IP(cinv_in.vat, cinv.invoicing_type, v_account.acc_no,cinv.INV_TYPE  ),      
                          A_VAT_DATE       => cinv.vat_date,                                                                                   
                          A_SALE_DATE      => cinv.vat_date,                                                                                   
                          A_INVOICE_LINE_NAME => NVL(substr(v_line_name_std,1,50),cinv_in.NAME),                                               
                          A_INVOICE_LINE_NAME_2 => v_line_name_2,                                                                              
                          A_UNIT_PRICE          => v_unit_price,
                          A_QUANTITY            => v_quantity,                                                                          
                          A_UNIT_NAV_CODE       => GET_UNIT_NAV_CODE(cinv.invoice_number, cinv_in.UNIT_PRINTED ),                                                           
                          A_COR_REASON_NAV_CODE => null                                                                
                          );                                                                                                                   
                                                                                                                                               
          ELSE                                                                                                                                 

             ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(cinv.INV_TYPE),
                          A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.INV_TYPE), -- ???
                          A_DOC_TYPE       => GET_DOC_TYPE(cinv.INV_TYPE), 
                          A_PAYMENT_TYPE   => v_payment_type,
                          A_ACCOUNT        => v_account.acc_no,
                          A_MPK            => NULL,
                          A_TAX            => 'PDO',
                          A_PRODUCT        => v_prd_type,
                          A_BOOK_DATE      => cinv.invoice_date,
                          A_AMOUNT         => cinv_in.netto - cinv_in.vat_val,
                          A_CODE           => cinv.comp_code,
                          A_DOC_NUMBER     => cinv.invoice_number,
                          A_VAT_RATE       => GET_VAT_RATE(cinv_in.vat, cinv.inv_type),
                          A_ORD_ID         => cinv.source,
                          A_PROFORMA_NO    => v_proforma_no,
                          A_INVOICE_DATE   => cinv.invoice_date,
                          A_PAYMENT_DATE   => cinv.pay_date,
                          A_PROJECT        => v_project,
                          a_tgkv_code      => get_tgkv_code_sell(cinv_in.vat,'N'),
                          A_VAT_DATE       => cinv.vat_date,
                          A_INVOICE_LINE_NAME   => cinv_in.NAME,
                          A_INVOICE_LINE_NAME_2 => cinv_in.NAME_2,
                          A_UNIT_PRICE          => v_unit_price,
                          A_QUANTITY            => v_quantity,                                                                          
                          A_UNIT_NAV_CODE       => GET_UNIT_NAV_CODE(cinv.invoice_number, cinv_in.UNIT_PRINTED ),
                          A_COR_REASON_NAV_CODE => null);

          END IF;                          
   
      END LOOP;
   END LOOP;   


EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,v_debug_string||sqlerrm,'SWP_NAV_CIN');
END;
PROCEDURE External_Customer_Invoices (
  a_input_date      IN       DATE
)
IS
  v_ext_cus_account        r_acc;
  v_vat_account            r_acc;
  v_ext_cus_turn_account   r_acc;
  v_ext_cus_settle_account r_acc; 
  v_ext_cus_settle_rmb_account r_acc;
  v_ext_cus_voucher_sr_account r_acc;
  v_ext_cus_vo_rmb_account r_acc;
  v_rmb_account r_acc;
  v_unit_price  NUMBER;
  v_quantity    NUMBER;
  
  FUNCTION Get_Tgkv_Code__(a_vat_rate VARCHAR2) RETURN VARCHAR2
  IS
    v_number NUMBER;
    v_tgkv_code VARCHAR2(10);
  BEGIN
    BEGIN
      v_number := TO_NUMBER(TRIM(a_vat_rate));
      IF v_number = 0 THEN
        v_tgkv_code := 'VAT' || v_number || ' T'; 
      ELSE
        v_tgkv_code := 'VAT' || LPAD(v_number, 2, '0') || ' T';
      END IF; 
    EXCEPTION
      WHEN OTHERS THEN
        v_tgkv_code := a_vat_rate || ' T';  
    END;
    RETURN v_tgkv_code;
  END;  
  
BEGIN
  PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','External_Customer_Invoices',to_char(a_input_date,'YYYY-MM-DD'));
     
  v_ext_cus_account        := GET_ACCOUNT('EXTERNAL_CUSTOMER_ACCOUNT');
  v_vat_account            := GET_ACCOUNT('VAT_ACCOUNT');
  v_ext_cus_turn_account   := GET_ACCOUNT('EXT_CUS_TURN_ACCOUNT');
  v_ext_cus_settle_account := GET_ACCOUNT('EXT_CUS_SETTLE_ACCOUNT');
  
  v_ext_cus_settle_rmb_account := GET_ACCOUNT('EXT_CUS_SETTLE_ACCOUNT_RMB');   -- 256010
  v_ext_cus_vo_rmb_account := GET_ACCOUNT('EXT_CUS_VOUCHER_MOD_ACCOUNT_SR_RMB');
 
  v_rmb_account:=NULL;    
  FOR c_ext_inv IN (SELECT esc.code external_customer_code,
                           ei.invoice_number, 
                           ei.reimbursement_number,
                           ei.type invoice_type,
                           SUM(NVL(eil.gross_amount,0)) gross_amount,
                           SUM(NVL(eil.vat_amount,0)) vat_amount,
                           ei.invoice_date,
                           ei.external_order_date,
                           esc.external_program_id,
                           ei.id external_invoice_id,
                           ESC.EXTERNAL_PROGRAM_TYPE,
                          NULL                    COR_REASON_NAV_CODE
                      FROM APP_FIN.EXTERNAL_INVOICES ei
                      JOIN APP_FIN.EXTERNAL_INVOICE_LINES eil ON ei.id = eil.external_invoice_id
                      JOIN APP_WEB.EXTERNAL_SYSTEM_CUSTOMERS esc ON ei.external_system_customer_id = esc.id
                      WHERE eil.gross_amount != 0
                        AND esc.external_program_type in (EAGLE.PK_MOD_EXTERNAL_INVOICES.gc_prog_type_clickepass, EAGLE.PK_MOD_EXTERNAL_INVOICES.gc_prog_type_vouchermodel) 
                        AND TRUNC(ei.INVOICE_DATE) BETWEEN TRUNC(a_input_date) - 7 AND TRUNC(a_input_date) 
                        AND ei.status_id = EAGLE.PK_MOD_EXTERNAL_INVOICES.gc_status_invoiced  
                      GROUP BY esc.code,
                               ei.invoice_number,
                               ei.reimbursement_number, 
                               ei.type, 
                               ei.invoice_date,
                               ei.external_order_date,
                               esc.external_program_id,
                               ei.id,
                               ESC.EXTERNAL_PROGRAM_TYPE     
                      ORDER BY ei.invoice_number)
  LOOP
    -- faktura 
    ADD_NAV_INV(A_NUM_SEQUENCE    => GET_INV_SEQUENCE(c_ext_inv.invoice_type),
                A_DOC_TYPE_NAV    => NULL,
                A_DOC_TYPE        => GET_DOC_TYPE(c_ext_inv.invoice_type),
                A_PAYMENT_TYPE    => 'PD',
                A_ACCOUNT         => v_ext_cus_account.acc_no,
                A_MPK             => NULL,                  
                A_TAX             => NULL,
                A_PRODUCT         => NULL,
                A_BOOK_DATE       => c_ext_inv.invoice_date,
                A_AMOUNT          => c_ext_inv.gross_amount,
                A_CODE            => c_ext_inv.external_customer_code,
                A_DOC_NUMBER      => c_ext_inv.invoice_number, 
                A_VAT_RATE        => NULL,  
                A_ORD_ID          => NULL, 
                A_PROFORMA_NO     => NULL, 
                A_INVOICE_DATE    => c_ext_inv.invoice_date,
                A_PAYMENT_DATE    => c_ext_inv.invoice_date,
                A_PROJECT         => NULL,
                A_VAT_DATE        => c_ext_inv.invoice_date,
                A_COR_REASON_NAV_CODE => c_ext_inv.COR_REASON_NAV_CODE );
              
    IF c_ext_inv.invoice_type=EAGLE.PK_MOD_EXTERNAL_INVOICES.gc_inv_type_ext_customer THEN     
      ADD_NAV_INV(A_NUM_SEQUENCE   => GET_INV_SEQUENCE(c_ext_inv.invoice_type),
                  A_DOC_TYPE_NAV   => NULL,
                  A_DOC_TYPE       => GET_DOC_TYPE(c_ext_inv.invoice_type), 
                  A_PAYMENT_TYPE   => 'PD',
                  A_ACCOUNT        => v_vat_account.acc_no,
                  A_MPK            => NULL,
                  A_TAX            => NULL, 
                  A_PRODUCT        => NULL,
                  A_BOOK_DATE      => c_ext_inv.invoice_date,
                  A_AMOUNT         => c_ext_inv.vat_amount * (-1),
                  A_CODE           => c_ext_inv.external_customer_code,
                  A_DOC_NUMBER     => c_ext_inv.invoice_number,
                  A_VAT_RATE       => NULL,
                  A_ORD_ID         => NULL, 
                  A_PROFORMA_NO    => NULL,
                  A_INVOICE_DATE   => c_ext_inv.invoice_date,
                  A_PAYMENT_DATE   => c_ext_inv.invoice_date,
                  A_PROJECT        => NULL,
                  A_VAT_DATE       => c_ext_inv.invoice_date,
                  A_COR_REASON_NAV_CODE => NULL);
    END IF;
    FOR c_ext_inv_vat_rate IN (SELECT  eil.vat_rate,
                                       NVL(eil.gross_amount,0) gross_amount,
                                       NVL(eil.vat_amount,0) vat_amount,
                                       SUBSTR(EIL.ITEM_NAME,1,50)    NAME,
                                       SUBSTR(EIL.ITEM_NAME,51,50)   NAME_2,
                                       ROUND(EIL.NET_AMOUNT/ EIL.QUANTITY,2) UNIT_PRICE,
                                       EIL.NET_AMOUNT          NET_AMOUNT , 
                                       EIL.QUANTITY            QUANTITY   ,         
                                       UPPER(EIL.UNIT_MEASURE) UNIT,
                                       EIL.EXTERNAL_ORDER_LINE_ID       
                                FROM APP_FIN.EXTERNAL_INVOICE_LINES eil
                                WHERE eil.external_invoice_id = c_ext_inv.external_invoice_id
                                AND  eil.gross_amount != 0
                                ORDER BY eil.vat_rate)
    LOOP
      
      IF c_ext_inv_vat_rate.UNIT NOT IN ('SZT') THEN
        PK_ERROR.RAISE_ERROR(-20008, 'Nieobs³ugiwana jednostka miary: '||c_ext_inv_vat_rate.UNIT||'. Faktura o nr: '||c_ext_inv.invoice_number );
      END IF;
      
      v_unit_price := NULL;
      v_quantity   := NULL;
      GET_EXP_NAV_UP_AND_Q  (a_inv_id => c_ext_inv.external_invoice_id,
                             a_doc_type => 'C', -- C -orygina³, K - korekta: jeœli bêd¹ korekty, to nale¿y obs³u¿yæ
                             a_pos_num  => c_ext_inv_vat_rate.EXTERNAL_ORDER_LINE_ID ,
                             a_line_quantity_printed => c_ext_inv_vat_rate.quantity,
                             a_line_unit => c_ext_inv_vat_rate.unit,
                             a_line_netto_amount => c_ext_inv_vat_rate.net_amount,
                             a_line_unit_price => c_ext_inv_vat_rate.unit_price,
                             p_unit_price => v_unit_price,
                             p_quantity   => v_quantity);      
      
      
      ADD_NAV_INV(A_NUM_SEQUENCE   => GET_INV_SEQUENCE(c_ext_inv.invoice_type),
                  A_DOC_TYPE_NAV   => NULL,
                  A_DOC_TYPE       => GET_DOC_TYPE(c_ext_inv.invoice_type), 
                  A_PAYMENT_TYPE   => 'PD',
                  A_ACCOUNT        => v_ext_cus_turn_account.acc_no,
                  A_MPK            => NULL,
                  A_TAX            => 'PDO',
                  A_PRODUCT        => c_ext_inv.external_program_id,
                  A_BOOK_DATE      => c_ext_inv.invoice_date,
                  A_AMOUNT         => (c_ext_inv_vat_rate.gross_amount - c_ext_inv_vat_rate.vat_amount) * (-1),
                  A_CODE           => c_ext_inv.external_customer_code,
                  A_DOC_NUMBER     => c_ext_inv.invoice_number,
                  A_VAT_RATE       => c_ext_inv_vat_rate.vat_rate,
                  A_ORD_ID         => NULL,
                  A_PROFORMA_NO    => NULL,
                  A_INVOICE_DATE   => c_ext_inv.invoice_date,
                  A_PAYMENT_DATE   => c_ext_inv.invoice_date,
                  A_PROJECT        => NULL,
                  A_TGKV_CODE      => Get_Tgkv_Code__(c_ext_inv_vat_rate.vat_rate), 
                  A_VAT_DATE       => c_ext_inv.invoice_date,
                  A_INVOICE_LINE_NAME => c_ext_inv_vat_rate.NAME,
                  A_INVOICE_LINE_NAME_2 => c_ext_inv_vat_rate.NAME_2,
                  A_UNIT_PRICE          => v_unit_price,
                  A_QUANTITY            => v_quantity,                                                                          
                  A_UNIT_NAV_CODE       => GET_UNIT_NAV_CODE(c_ext_inv.invoice_number, c_ext_inv_vat_rate.UNIT ),
                  A_COR_REASON_NAV_CODE => NULL
                  );
    END LOOP; 
        
    --  rozliczenie  
    ADD_NAV_INV(A_NUM_SEQUENCE   => GET_INV_SEQUENCE(c_ext_inv.invoice_type|| '_RMB'),
                A_DOC_TYPE_NAV   => NULL,
                A_DOC_TYPE       => GET_DOC_TYPE(c_ext_inv.invoice_type || '_RMB'), 
                A_PAYMENT_TYPE   => 'PD',
                A_ACCOUNT        => v_ext_cus_account.acc_no,
                A_MPK            => NULL,
                A_TAX            => NULL, 
                A_PRODUCT        => NULL,
                A_BOOK_DATE      => c_ext_inv.invoice_date,
                A_AMOUNT         => c_ext_inv.gross_amount * (-1),
                A_CODE           => c_ext_inv.external_customer_code,
                A_DOC_NUMBER     => c_ext_inv.reimbursement_number,
                A_VAT_RATE       => NULL,
                A_ORD_ID         => NULL, 
                A_PROFORMA_NO    => NULL,
                A_INVOICE_DATE   => c_ext_inv.invoice_date,
                A_PAYMENT_DATE   => c_ext_inv.invoice_date,
                A_PROJECT        => NULL,  
                A_TGKV_CODE      => 'NP U',
                A_VAT_DATE       => NULL);


  IF c_ext_inv.EXTERNAL_PROGRAM_TYPE=EAGLE.PK_MOD_EXTERNAL_INVOICES.gc_prog_type_clickepass THEN
    v_rmb_account:=v_ext_cus_settle_rmb_account;
  ELSE
    v_rmb_account:=v_ext_cus_vo_rmb_account;
  END IF;

    ADD_NAV_INV(A_NUM_SEQUENCE    => GET_INV_SEQUENCE(c_ext_inv.invoice_type || '_RMB'),
                A_DOC_TYPE_NAV    => NULL,
                A_DOC_TYPE        => GET_DOC_TYPE(c_ext_inv.invoice_type || '_RMB'),
                A_PAYMENT_TYPE    => 'PD',
                A_ACCOUNT         => v_rmb_account.acc_no,
                A_MPK             => NULL,                  
                A_TAX             => NULL,
                A_PRODUCT         => c_ext_inv.external_program_id,
                A_BOOK_DATE       => c_ext_inv.invoice_date,
                A_AMOUNT          => c_ext_inv.gross_amount,
                A_CODE            => c_ext_inv.external_customer_code,
                A_DOC_NUMBER      => c_ext_inv.reimbursement_number, 
                A_VAT_RATE        => NULL,  
                A_ORD_ID          => NULL, 
                A_PROFORMA_NO     => NULL, 
                A_INVOICE_DATE    => c_ext_inv.invoice_date,
                A_PAYMENT_DATE    => c_ext_inv.invoice_date,
                A_PROJECT         => NULL,
                A_TGKV_CODE      => 'NP U',
                A_VAT_DATE        => NULL);      
    -- zmiana statusu faktury     
    EAGLE.PK_MOD_EXTERNAL_INVOICES.Set_Invoice_Exported(c_ext_inv.external_invoice_id);
   END LOOP;   


EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,to_char(a_input_date,'YYYY-MM-DD')||sqlerrm,'SWP_NAV_ECI');
END External_Customer_Invoices;


PROCEDURE clients_inv_vb (
  a_input_date      IN       DATE,
  a_doc_type        IN       VARCHAR2,
  a_source_type     IN       VARCHAR2  
)
IS
   v_prd_type       app_stc.product_types.TYPE%TYPE;
   
   v_c_account      r_acc;
   v_vat_account    r_acc;
   v_cli_inc_account r_acc;
   v_cli_add_account r_acc;
   v_cli_inc_account_c2c r_acc;
   v_cli_add_account_c2c r_acc;
   v_account        r_acc;
   
   v_change         BOOLEAN := FALSE;
   v_debug_string   eagle.adm_audit_modules.arg_string%TYPE;
   v_proforma_no    app_fin.invoices.invoice_number%TYPE;
BEGIN

   v_debug_string := 'doc='||a_doc_type||' src='||a_source_type||' date='||to_char(a_input_date);
   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','CLIENTS_INV_VB',v_debug_string);
   
   v_c_account      := GET_ACCOUNT('CLIENT_ACCOUNT_VB');
   v_vat_account    := GET_ACCOUNT('VAT_ACCOUNT_VB');
   
   IF a_doc_type = 'C' THEN
      v_cli_inc_account := GET_ACCOUNT('CLI_INCOME_ACCOUNT_VB');
      v_cli_add_account := GET_ACCOUNT('CLI_DC_ACCOUNT_VB');
      v_cli_inc_account_c2c := GET_ACCOUNT('CLI_INCOME_ACCOUNT_VB_C2C');
      v_cli_add_account_c2c := GET_ACCOUNT('CLI_DC_ACCOUNT_VB_C2C');
   ELSIF a_doc_type = 'K' THEN
      v_cli_inc_account := GET_ACCOUNT('CLI_INCOME_ACCOUNT_C_VB');
      v_cli_add_account := GET_ACCOUNT('CLI_DC_ACCOUNT_C_VB');
      v_cli_inc_account_c2c := GET_ACCOUNT('CLI_INCOME_ACCOUNT_C_VB_C2C');
      v_cli_add_account_c2c := GET_ACCOUNT('CLI_DC_ACCOUNT_C_VB_C2C');
   ELSE
      PK_ERROR.RAISE_ERROR(-20008, 'Nieznany parametr doc_type='||a_doc_type);
   END IF;   
   
   FOR cinv IN (SELECT   com.code comp_code,
                  inv.invoice_number,
                  DECODE (inv.TYPE, 'ICV', 'C', 'CICV', 'K', inv.TYPE) dok,
                  SUM(NVL (inv_lin.gross_amount, 0) * (-1)) netto,
                  SUM(NVL (inv_lin.vat_amount, 0) * (-1)) vat_val,
                  inv.invoice_date,
                  nvl(inv.PAYMENT_DATE, inv.INVOICE_DATE) pay_date,
                  inv.ID inv_id,
                  inv.TYPE inv_type,
                  inv_lin.source,
                  inv.vat_date
             FROM app_emi.customers com,
                  app_fin.invoices inv,
                  app_fin.invoice_lines inv_lin
            WHERE inv.cus_id = com.ID
              AND inv.ID = inv_lin.inv_id
              AND inv.invoice_number IS NOT NULL
              AND inv_lin.vat_amount != 0
              AND ((    (   (inv.TYPE = 'ICV' AND a_doc_type = 'C')
                         OR (    inv.TYPE = 'CICV'
                             AND inv_lin.pos_type = 'D'
                             AND a_doc_type = 'K'
                            )
                        )
                    AND inv_lin.source_type = 'ORDV'
                    AND a_source_type = 'ORDV'
                   )
                  )
              AND TRUNC (invoice_date) = a_input_date
              AND inv.status = 'P'
           GROUP BY
              com.code ,
              inv.invoice_number,
              DECODE (inv.TYPE, 'ICV', 'C', 'CICV', 'K', inv.TYPE),
              inv.invoice_date,
              nvl(inv.PAYMENT_DATE, inv.INVOICE_DATE),
              inv.ID,
              inv.TYPE,
              inv_lin.source,
              inv.vat_date
           )
      LOOP
         
         t$invoice.set_status (cinv.inv_id, 'E');
         v_proforma_no := GET_PROFORMA_NUMBER(cinv.inv_id);

         ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(cinv.INV_TYPE),
                      A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.INV_TYPE), -- ???
                      A_DOC_TYPE       => GET_DOC_TYPE(cinv.INV_TYPE), 
                      A_PAYMENT_TYPE   => '00',
                      A_ACCOUNT        => v_c_account.acc_no,
                      A_MPK            => NULL,
                      A_TAX            => NULL, 
                      A_PRODUCT        => NULL,
                      A_BOOK_DATE      => cinv.invoice_date,
                      A_AMOUNT         => cinv.netto * (-1),
                      A_CODE           => cinv.comp_code,
                      A_DOC_NUMBER     => cinv.invoice_number,
                      A_VAT_RATE       => NULL,
                      A_ORD_ID         => cinv.source,
                      A_PROFORMA_NO    => v_proforma_no,
                      A_INVOICE_DATE   => cinv.invoice_date,
                      A_PAYMENT_DATE   => cinv.pay_date,
                      A_PROJECT        => v_c_account.project,
                      A_VAT_DATE       => cinv.vat_date);

         ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(cinv.INV_TYPE),
                      A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.INV_TYPE), -- ???
                      A_DOC_TYPE       => GET_DOC_TYPE(cinv.INV_TYPE), 
                      A_PAYMENT_TYPE   => '00',
                      A_ACCOUNT        => v_vat_account.acc_no,
                      A_MPK            => NULL,
                      A_TAX            => NULL, 
                      A_PRODUCT        => NULL,
                      A_BOOK_DATE      => cinv.invoice_date,
                      A_AMOUNT         => cinv.vat_val,
                      A_CODE           => cinv.comp_code,
                      A_DOC_NUMBER     => cinv.invoice_number,
                      A_VAT_RATE       => NULL,
                      A_ORD_ID         => cinv.source,
                      A_PROFORMA_NO    => v_proforma_no,
                      A_INVOICE_DATE   => cinv.invoice_date,
                      A_PAYMENT_DATE   => cinv.pay_date,
                      A_PROJECT        => v_vat_account.project,
                      A_VAT_DATE       => cinv.vat_date);
        
        FOR cinv_in IN (SELECT NVL (il.gross_amount, 0) * (-1) netto,
                               NVL (il.vat_amount, 0) * (-1) vat_val,
                                   il.code prd_id,
                                       il.VAT
                                FROM app_fin.invoices i,
                                     app_fin.invoice_lines il
                               WHERE i.ID = il.inv_id
                                 AND il.vat_amount != 0
                                 AND i.id = cinv.inv_id
                                 AND ((i.TYPE = 'ICV' AND il.pos_type = 'I' AND a_doc_type = 'C')
                                   OR (i.TYPE = 'CICV' AND il.pos_type = 'D' AND a_doc_type = 'K'))
                            ORDER BY prd_id)
        LOOP        
         
              v_prd_type := cinv_in.PRD_ID;
              IF cinv_in.prd_id LIKE '%DC%' THEN
                 v_prd_type := get_min_inv_product(cinv.inv_id);
                 v_change := TRUE;
              END IF;
              
              IF v_prd_type LIKE '%DF%' THEN
                 v_prd_type := '1';
                 v_change := TRUE;
              END IF;
              
             /* Proteza Pierwszych produktow Viva Box */
             IF PK_UTILS.Is_number(v_prd_type) THEN
               v_prd_type := to_number(v_prd_type) + 100;
             END IF;              
              
             /* Viva Box C2C */
              IF cinv.comp_code like '3%' /* osoba fizyczna */
                OR
                tv$order.get_retailer(cinv.source) = 'Y' /* detalista */
                OR
                tv$order_parameter.exists_parameter(cinv.source, 'SHP_ORD_ID') /* sklep internetowy */
              THEN

                 v_account := v_cli_inc_account_c2c;
                 IF (v_prd_type IN ('CC')) OR (v_change = TRUE) THEN
                    v_change := FALSE;
                    v_account := v_cli_add_account_c2c;
                 END IF;                 
                 
              ELSE
              
                 v_account := v_cli_inc_account;
                 IF (v_prd_type IN ('CC')) OR (v_change = TRUE) THEN
                    v_change := FALSE;
                    v_account := v_cli_add_account;
                 END IF;

              END IF;             
             
             
             ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(cinv.INV_TYPE),
                          A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.INV_TYPE), -- ???
                          A_DOC_TYPE       => GET_DOC_TYPE(cinv.INV_TYPE), 
                          A_PAYMENT_TYPE   => '00',
                          A_ACCOUNT        => v_account.acc_no,
                          A_MPK            => NULL,
                          A_TAX            => 'PDO',
                          A_PRODUCT        => v_prd_type,
                          A_BOOK_DATE      => cinv.invoice_date,
                          A_AMOUNT         => cinv_in.netto - cinv_in.vat_val,
                          A_CODE           => cinv.comp_code,
                          A_DOC_NUMBER     => cinv.invoice_number,
                          A_VAT_RATE       => GET_VAT_RATE(cinv_in.vat, cinv.inv_type),
                          A_ORD_ID         => cinv.source,
                          A_PROFORMA_NO    => v_proforma_no,
                          A_INVOICE_DATE   => cinv.invoice_date,
                          A_PAYMENT_DATE   => cinv.pay_date,
                          A_PROJECT        => v_account.project,
                          a_tgkv_code			 => get_tgkv_code_sell(cinv_in.vat,'Y'), 
                          A_VAT_DATE       => cinv.vat_date);
         
        END LOOP;
      
      END LOOP;
   
EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,v_debug_string||sqlerrm,'SWP_NAV_CIV');
END;

PROCEDURE clients_notes (
  a_input_date      IN       DATE,
  a_doc_type        IN       VARCHAR2,
  a_source_type     IN       VARCHAR2)
IS
   e_wait_eoutoftime EXCEPTION;
   PRAGMA EXCEPTION_INIT(e_wait_eoutoftime, -30006);
   v_c_account_Pb_Usr  r_acc;
   v_c_account_Pb_Ord  r_acc;
   v_c_account_Pb_Turn r_acc;
   v_c_account         r_acc;
   v_meal_account      r_acc;
   
   v_debug_string   eagle.adm_audit_modules.arg_string%TYPE;
   v_proforma_no    app_fin.invoices.invoice_number%TYPE;
   v_payment_type VARCHAR2(30);  
   
   v_project          APP_SWP.NAV_INV.PROJECT%TYPE;
   v_cpack_par_count  NUMBER;  

   v_np                 VARCHAR2(10):='NP';
   v_npu                VARCHAR2(10):='NP U';
   
   v_invoice_line_name APP_FIN.INVOICE_LINES.NAME%TYPE;
   v_version           APP_PBE.ORDERS.VERSION%TYPE;
   v_nav_inv           APP_SWP.NAV_INV%ROWTYPE;

   v_unit_price  NUMBER;
   v_quantity    NUMBER;

BEGIN

   v_debug_string := 'doc='||a_doc_type||' src='||a_source_type||' date='||to_char(a_input_date);
   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','CLIENTS_NOTES',v_debug_string);
   
   v_c_account             := GET_ACCOUNT('CLIENT_ACCOUNT');
   v_c_account_Pb_Usr      := GET_ACCOUNT('PBE_USER_ACCOUNT');
   v_c_account_Pb_Ord      := GET_ACCOUNT('PBE_ORDER_ACCOUNT');
   
   v_c_account_Pb_Turn     := GET_ACCOUNT('PBE_TURN_ACCOUNT');

  IF a_doc_type = 'NPB' THEN
   
      FOR cinv IN (
               SELECT 
                      CON.CODE                                  COMP_CODE,
                      I.INVOICE_NUMBER                          INVOICE_NUMBER,
                      SUM (NVL (IL.NET_AMOUNT, 0) * (-1))       NETTO,
                      SUM (NVL (IL.VAT_AMOUNT, 0) * (-1))       VAT_VAL,
                      I.INVOICE_DATE                            INVOICE_DATE,
                      NVL(I.PAYMENT_DATE, I.INVOICE_DATE)       PAY_DATE,
                      I.ID                                      INV_ID,
                      I.TYPE                                    INV_TYPE,
                      IL.SOURCE                                 ORDER_ID,
                      CON.ID                                    CONTRACT_ID,
                      I.VAT_DATE                                VAT_DATE,
                      PRG.PROGRAM_CODE                          GRANT_PROGRAM_CODE,
                      O.EXTERNAL_ORDER_ID                       EXTERNAL_ORDER_ID,
                      PRG.PROGRAM_CODE_TURN                     PROGRAM_CODE_TURN                                          
               FROM   APP_FIN.INVOICES I
                      JOIN APP_FIN.INVOICE_LINES IL     ON IL.INV_ID = I.ID AND IL.SOURCE_TYPE = 'ORDPB'
                      JOIN APP_PBE.ORDERS O             ON O.ID = IL.SOURCE
                      JOIN APP_PBE.ORDER_INVOICES OI    ON OI.INVOICE_ID = I.ID AND OI.ORDER_ID = O.ID
                      LEFT JOIN APP_PBE.ENTERPRISES ENT ON ENT.ID = OI.ENTERPRISE_ID
                      LEFT JOIN APP_PBE.INDIVIDUALS IND ON IND.ID = OI.INDIVIDUAL_ID
                      JOIN APP_PBE.CONTRACTS CON        ON CON.ID = O.CONTRACT_ID
                      JOIN APP_PBE.GRANT_PROGRAMS   PRG ON PRG.ID = CON.GRANT_PROGRAM_ID
               WHERE  I.STATUS = 'P'
                  AND TRUNC (I.INVOICE_DATE) = a_input_date
                  AND I.NET_AMOUNT <> 0 
             GROUP BY I.INVOICE_NUMBER,
                      I.INVOICE_DATE,
                      NVL(I.PAYMENT_DATE, I.INVOICE_DATE),
                      I.ID,
                      I.TYPE,
                      IL.SOURCE,
                      I.VAT_DATE,
                      CON.CODE,
                      CON.ID,
                      PRG.PROGRAM_CODE,
                      O.EXTERNAL_ORDER_ID,
                      PRG.PROGRAM_CODE_TURN    
             )
    LOOP
      t$invoice.set_status (cinv.inv_id, 'E');
      
      v_proforma_no := CINV.ORDER_ID;
             
      v_payment_type := '00';
      
      BEGIN
        SELECT DISTINCT NAME
        INTO   v_invoice_line_name
        FROM   APP_FIN.INVOICE_LINES IL
        WHERE  IL.INV_ID = CINV.INV_ID;
      EXCEPTION
        WHEN NO_DATA_FOUND 
        OR   TOO_MANY_ROWS THEN
          v_invoice_line_name:='Wk³ad w³asny z tytu³u zakupu bonów szkoleniowych';          
      END;
      
      ADD_NAV_INV(A_NUM_SEQUENCE           => GET_INV_SEQUENCE(cinv.INV_TYPE),
                  A_DOC_TYPE_NAV           => GET_DOC_TYPE_NAV(cinv.INV_TYPE), -- NULL
                  A_DOC_TYPE               => GET_DOC_TYPE(cinv.INV_TYPE), -- NULL, dla korekt C
                  A_PAYMENT_TYPE           => v_payment_type,
                  A_ACCOUNT                => v_c_account_Pb_Usr.acc_no,
                  A_MPK                    => NULL,
                  A_TAX                    => NULL, 
                  A_PRODUCT                => NULL,
                  A_BOOK_DATE              => cinv.invoice_date,
                  A_AMOUNT                 => cinv.netto * (-1),
                  A_CODE                   => cinv.comp_code,
                  A_DOC_NUMBER             => cinv.invoice_number,
                  A_VAT_RATE               => NULL,
                  A_ORD_ID                 => cinv.ORDER_ID,
                  A_PROFORMA_NO            => cinv.EXTERNAL_ORDER_ID,
                  A_INVOICE_DATE           => cinv.invoice_date,
                  A_PAYMENT_DATE           => cinv.pay_date,
                  A_PROJECT                => NULL,
                  A_TGKV_CODE              => NULL,
                  A_VAT_DATE               => cinv.vat_date, 
                  A_SALE_DATE              => cinv.invoice_date,
                  A_ACCOUNT_NUMBER         => NULL,
                  A_REFERENCE_NUMBER       => NULL,
                  A_OUT_PAYMENT_CONF_CODE  => NULL,
                  A_INVOICE_LINE_NAME      => v_invoice_line_name
                  );      


      FOR cinv_in IN (
                 SELECT 
                       SUM (NVL (IL.NET_AMOUNT, 0) * (-1))       NETTO,
                       SUM (NVL (IL.VAT_AMOUNT, 0) * (-1))       VAT_VAL,
                       PBPRD.PRD_TYPE                             PRD_CODE, 
                       IL.SOURCE                                  SOURCE,
                       IL.NAME                                    LINE_NAME,
                       IL.VAT                                     VAT  
                 FROM APP_FIN.INVOICES I
                      JOIN APP_FIN.INVOICE_LINES IL     ON IL.INV_ID = I.ID AND IL.SOURCE_TYPE = 'ORDPB'
                      JOIN APP_PBE.ORDERS O             ON O.ID = IL.SOURCE
                      JOIN APP_PBE.ORDER_INVOICES OI    ON OI.INVOICE_ID = I.ID AND OI.ORDER_ID = O.ID
                      LEFT JOIN APP_PBE.ENTERPRISES ENT ON ENT.ID = OI.ENTERPRISE_ID
                      LEFT JOIN APP_PBE.INDIVIDUALS IND ON IND.ID = OI.INDIVIDUAL_ID
                      JOIN APP_PBE.CONTRACTS CON        ON CON.ID = O.CONTRACT_ID
                      JOIN APP_PBE.GRANT_PROGRAMS   PRG ON PRG.ID = CON.GRANT_PROGRAM_ID
                      LEFT JOIN APP_PBE.PBE_PRODUCTS PBPRD ON PBPRD.PRD_ID = IL.CODE 
               WHERE  i.net_amount <> 0       
               AND    il.source_type = a_source_type   
               AND    IL.INV_ID = CINV.INV_ID            
               GROUP BY  IL.CODE,
                         PBPRD.PRD_TYPE,
                         IL.SOURCE,
                         IL.NAME,
                         IL.VAT                
               )
      LOOP

        ADD_NAV_INV(A_NUM_SEQUENCE   => GET_INV_SEQUENCE(cinv.INV_TYPE),
                    A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.INV_TYPE), -- ???
                    A_DOC_TYPE       => GET_DOC_TYPE(cinv.INV_TYPE), 
                    A_PAYMENT_TYPE   => v_payment_type,
                    A_ACCOUNT        => v_c_account_Pb_Turn.acc_no,
                    A_MPK            => NULL,
                    A_TAX            => NULL, 
                    A_PRODUCT        => cinv_in.PRD_CODE,
                    A_BOOK_DATE      => cinv.invoice_date,
                    A_AMOUNT         => cinv_in.netto,
                    A_CODE           => cinv.comp_code,
                    A_DOC_NUMBER     => cinv.invoice_number, 
                    A_VAT_RATE       => GET_VAT_RATE(cinv_in.vat, cinv.inv_type),
                    A_ORD_ID         => cinv_in.source,
                    A_PROFORMA_NO    => cinv.EXTERNAL_ORDER_ID,
                    A_INVOICE_DATE   => cinv.invoice_date,
                    A_PAYMENT_DATE   => cinv.pay_date,
                    A_PROJECT        => cinv.GRANT_PROGRAM_CODE,
                    a_tgkv_code       => 'NP U',
                    A_VAT_DATE       => cinv.vat_date, 
                    A_SALE_DATE      => cinv.invoice_date,
                    A_ACCOUNT_NUMBER         => NULL,
                    A_REFERENCE_NUMBER       => NULL,
                    A_OUT_PAYMENT_CONF_CODE  => NULL,
                    A_INVOICE_LINE_NAME      => CINV_IN.LINE_NAME
                    );

      END LOOP;
      
      v_nav_inv := NULL;

      -- update zamówienia
      BEGIN
        SELECT VERSION
        INTO   v_version
        FROM   APP_PBE.ORDERS O
        WHERE  O.ID = CINV.ORDER_ID
        FOR UPDATE
        WAIT 2;
      EXCEPTION    
        WHEN NO_DATA_FOUND THEN
          PK_ERROR.RAISE_ERROR(-200001,'Wersja zamówienia zosta³a zmieniona:'|| CINV.ORDER_ID);
        WHEN e_wait_eoutoftime THEN
          PK_ERROR.RAISE_ERROR(-200001,'Nie uda³o siê zablokowaæ rekordu APP_PBE.ORDERS, dla ord id:'|| CINV.ORDER_ID);
      END;
      UPDATE APP_PBE.ORDERS O
      SET    O.VERSION   = O.VERSION+1 
      WHERE  O.ID = CINV.ORDER_ID;

      -- update order elements - znacznik, z info o wyeksportowaniu
      UPDATE APP_PBE.ORDER_ELEMENTS E
      SET    E.VALUE_VARCHAR = 'true' -- Wyeksportowane
      WHERE  E.ORDER_ID = CINV.ORDER_ID
      and    E.ELEMENT_ID = 'WDEPEXP_KK';
      
      IF SQL%ROWCOUNT != 1 THEN
        PK_ERROR.RAISE_ERROR(-200001,'Znaleziono wiele wpisów w ORDER_ELEMENTS, dla ord id:'|| CINV.ORDER_ID||', element_id: WDEPEXP_KK');
      END IF;

      BEGIN
        SELECT  E.VALUE_NUMBER
        INTO    v_nav_inv.AMOUNT
        FROM    APP_PBE.ORDER_ELEMENTS E
        WHERE   E.ORDER_ID = CINV.ORDER_ID
        AND     E.ELEMENT_ID = 'GRAAMO_KK';
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          PK_ERROR.RAISE_ERROR(-200001,'Nie znaleziono wpisu w ORDER_ELEMENTS, dla ord id:'|| CINV.ORDER_ID||', element_id: GRAAMO_KK');
        WHEN TOO_MANY_ROWS THEN
          PK_ERROR.RAISE_ERROR(-200001,'Znaleziono wiele wpisów w ORDER_ELEMENTS, dla ord id:'|| CINV.ORDER_ID||', element_id: GRAAMO_KK');
      END;
      
      v_invoice_line_name:='Kwota do refundacji bonów szkoleniowych';          

      BEGIN
        SELECT  E.VALUE_VARCHAR
        INTO    v_nav_inv.DOC_NUMBER
        FROM    APP_PBE.ORDER_ELEMENTS E
        WHERE   E.ORDER_ID = CINV.ORDER_ID
        AND     E.ELEMENT_ID = 'WDEPNUM_KK';
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          PK_ERROR.RAISE_ERROR(-200001,'Nie znaleziono wpisu z nr. dok. P³atnoœæ odroczona w ORDER_ELEMENTS, dla ord id:'|| CINV.ORDER_ID||', element_id: WDEPNUM_KK');
        WHEN TOO_MANY_ROWS THEN
          PK_ERROR.RAISE_ERROR(-200001,'Znaleziono wiele wpisów z nr. dok. P³atnoœæ odroczona w ORDER_ELEMENTS, dla ord id:'|| CINV.ORDER_ID||', element_id: WDEPNUM_KK');
      END;

      ADD_NAV_INV(A_NUM_SEQUENCE           => GET_INV_SEQUENCE('NPB_ORD'),
                  A_DOC_TYPE_NAV           => GET_DOC_TYPE_NAV('NPB_ORD'), -- NULL
                  A_DOC_TYPE               => GET_DOC_TYPE('NPB_ORD'), -- NULL, dla korekt C
                  A_PAYMENT_TYPE           => '00',
                  A_ACCOUNT                => v_c_account_Pb_Ord.acc_no,
                  A_MPK                    => NULL,
                  A_TAX                    => NULL, 
                  A_PRODUCT                => NULL,
                  A_BOOK_DATE              => cinv.invoice_date,
                  A_AMOUNT                 => v_nav_inv.AMOUNT ,
                  A_CODE                   => cinv.PROGRAM_CODE_TURN, 
                  A_DOC_NUMBER             => v_nav_inv.DOC_NUMBER,
                  A_VAT_RATE               => NULL,
                  A_ORD_ID                 => cinv.ORDER_ID,
                  A_PROFORMA_NO            => cinv.EXTERNAL_ORDER_ID,
                  A_INVOICE_DATE           => cinv.invoice_date,
                  A_PAYMENT_DATE           => cinv.pay_date,
                  A_PROJECT                => NULL,
                  A_TGKV_CODE              => NULL,
                  A_VAT_DATE               => cinv.vat_date, 
                  A_SALE_DATE              => cinv.invoice_date,
                  A_ACCOUNT_NUMBER         => NULL,
                  A_REFERENCE_NUMBER       => NULL,
                  A_OUT_PAYMENT_CONF_CODE  => NULL,
                  A_INVOICE_LINE_NAME      => v_invoice_line_name
                  );      


      
      BEGIN
        SELECT DISTINCT  PBPRD.PRD_TYPE, GET_VAT_RATE(IL.VAT, cinv.inv_type) 
        INTO   v_nav_inv.PRODUCT,  v_nav_inv.VAT_RATE
        FROM   APP_FIN.INVOICE_LINES IL     
               LEFT JOIN APP_PBE.PBE_PRODUCTS PBPRD ON PBPRD.PRD_ID = IL.CODE 
        WHERE  IL.INV_ID = CINV.INV_ID ;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          PK_ERROR.RAISE_ERROR(-200001,'Nie znaleziono linii Noty PB dla zamówienia'|| CINV.ORDER_ID);
        WHEN TOO_MANY_ROWS THEN
          PK_ERROR.RAISE_ERROR(-200001,'Znaleziono wiele produktów w liniach faktury, dla ord id:'|| CINV.ORDER_ID);
      END;      

      ADD_NAV_INV(A_NUM_SEQUENCE           => GET_INV_SEQUENCE('NPB_ORD'),
                  A_DOC_TYPE_NAV           => GET_DOC_TYPE_NAV('NPB_ORD'), -- NULL
                  A_DOC_TYPE               => GET_DOC_TYPE('NPB_ORD'), -- NULL, dla korekt C
                  A_PAYMENT_TYPE           => '00',
                  A_ACCOUNT                => v_c_account_Pb_Turn.acc_no,
                  A_MPK                    => NULL,
                  A_TAX                    => NULL, 
                  A_PRODUCT                => v_nav_inv.PRODUCT,
                  A_BOOK_DATE              => cinv.invoice_date,
                  A_AMOUNT                 => - v_nav_inv.AMOUNT,
                  A_CODE                   => cinv.PROGRAM_CODE_TURN, 
                  A_DOC_NUMBER             => v_nav_inv.DOC_NUMBER, 
                  A_VAT_RATE               => v_nav_inv.VAT_RATE, --GET_VAT_RATE(cinv_in.vat, cinv.inv_type),
                  A_ORD_ID                 => cinv.ORDER_ID,
                  A_PROFORMA_NO            => cinv.EXTERNAL_ORDER_ID,
                  A_INVOICE_DATE           => cinv.invoice_date,
                  A_PAYMENT_DATE           => cinv.pay_date,
                  A_PROJECT                => cinv.GRANT_PROGRAM_CODE,
                  a_tgkv_code              => 'NP U',
                  A_VAT_DATE               => cinv.vat_date, 
                  A_SALE_DATE              => cinv.invoice_date,
                  A_ACCOUNT_NUMBER         => NULL,
                  A_REFERENCE_NUMBER       => NULL,
                  A_OUT_PAYMENT_CONF_CODE  => NULL,
                  A_INVOICE_LINE_NAME      => v_invoice_line_name
                  );

    END LOOP;
  
    RETURN;   
  
  END IF ;
   
   
  FOR cinv IN ( SELECT cny.code comp_code,
                        inv.invoice_number,
                        SUM (NVL (inv_lin.net_amount, 0) * (-1)) netto,
                        SUM (NVL (inv_lin.vat_amount, 0) * (-1)) vat_val,
                        inv.invoice_date,
                        nvl(inv.PAYMENT_DATE, inv.INVOICE_DATE) pay_date,
                        inv.ID inv_id,
                        inv.TYPE inv_type,
                        inv_lin.source,
                        inv.vat_date,
                        null INVOICING_TYPE,
                        CR.COR_REASON_NAV_CODE,
                        inv.sale_date
                   FROM app_emi.customers cny,
                        app_fin.invoices inv,
                        app_fin.invoice_lines inv_lin,
                        APP_FIN.COR_REASON cr
                  WHERE inv.ID = inv_lin.inv_id
                    AND inv.cus_id = cny.ID
                    AND CR.ID (+) = INV.COR_REASON
                    AND inv_lin.code <> '12'
                    AND inv_lin.code <> '21'
                    AND inv_lin.code <> '25'
                    AND inv_lin.code <> '26'
                    AND inv_lin.code <> '27'
                    AND inv_lin.code <> '28'
                    AND inv_lin.code <> '32'
                    AND (   (    (   (    (    (inv.TYPE = 'N' OR inv.TYPE = 'R')
                                           AND inv_lin.pos_type = 'I'
                                          )
                                      AND a_doc_type = 'N'
                                     )
                                  OR (    (    (inv.TYPE = 'CN' OR inv.TYPE = 'CR'
                                               )
                                           AND inv_lin.pos_type = 'D'
                                          )
                                      AND a_doc_type = 'M'
                                     )
                                 )
                             AND inv_lin.source_type = 'ORD'
                             AND a_source_type = 'ORD'
                            )
                         OR (    inv.TYPE = 'N'
                             AND inv_lin.pos_type = 'I'
                             AND a_doc_type = 'N'
                             AND inv_lin.source_type = 'TSK'
                             AND a_source_type = 'TSK'
                            )
                         OR (    inv.TYPE = 'NU'
                             AND inv_lin.pos_type = 'I'
                             AND a_doc_type = 'NU'
                             AND inv_lin.source_type = 'REM'
                             AND a_source_type = 'REM'
                            )
                        )
                    AND inv.status = 'P'
                    AND TRUNC (invoice_date) = a_input_date
                    AND inv.net_amount <> 0          --nie eksportuj zerowych faktur 
               GROUP BY cny.code,
                        inv.invoice_number,
                        inv.invoice_date,
                        nvl(inv.PAYMENT_DATE, inv.INVOICE_DATE),
                        inv.ID,
                        inv.TYPE,
                        inv_lin.source,
                        inv.vat_date,
                        CR.COR_REASON_NAV_CODE,
                        inv.sale_date
           -- GAFA
           UNION ALL
                   SELECT cny.code comp_code,
                        inv.invoice_number,
                        SUM (NVL (inv_lin.net_amount, 0) * (-1)) netto,
                        SUM (NVL (inv_lin.vat_amount, 0) * (-1)) vat_val,
                        inv.invoice_date,
                        nvl(inv.PAYMENT_DATE, inv.INVOICE_DATE) pay_date,
                        inv.ID inv_id,
                        inv.TYPE inv_type,
                        inv_lin.source,
                        inv.vat_date,
                        IP.INVOICING_TYPE,
                        CR.COR_REASON_NAV_CODE,
                        inv.sale_date
                   FROM app_fin.invoices inv                     
                        JOIN app_fin.invoice_lines inv_lin ON inv.ID = inv_lin.inv_id
                        JOIN app_emi.customers cny ON inv.cus_id = cny.ID
                        JOIN APP_FIN.INVOICE_PROPOSALS ip ON (IP.ID = INV_LIN.SOURCE AND INV_LIN.SOURCE_TYPE = PK_MOD_INV_PROPOSALS.GC_INV_SRC_TYPE_IP)
                        LEFT JOIN APP_FIN.COR_REASON cr ON CR.ID = INV.COR_REASON
                  WHERE (inv.TYPE = PK_MOD_INV_PROPOSALS.GC_INV_TYPE_NG  OR inv.TYPE = PK_MOD_INV_PROPOSALS.GC_INV_TYPE_RG) -- rachunek i nota
                    AND inv_lin.pos_type = 'I'
                    AND a_doc_type = 'N'
                    AND inv.status = 'P'
                    AND TRUNC (invoice_date) = a_input_date
                    AND inv.net_amount <> 0          --nie eksportuj zerowych faktur
                    AND inv_lin.source_type = a_source_type
                    AND a_source_type = PK_MOD_INV_PROPOSALS.GC_INV_SRC_TYPE_IP                      
               GROUP BY cny.code,
                        inv.invoice_number,
                        inv.invoice_date,
                        nvl(inv.PAYMENT_DATE, inv.INVOICE_DATE),
                        inv.ID,
                        inv.TYPE,
                        inv_lin.source,
                        inv.vat_date,
                        ip.INVOICING_TYPE,
                        CR.COR_REASON_NAV_CODE,
                        inv.sale_date
               ORDER BY invoice_number)
        LOOP
             
             t$invoice.set_status (cinv.inv_id, 'E');
             
             IF a_source_type <> PK_MOD_INV_PROPOSALS.GC_INV_SRC_TYPE_IP OR
               (a_source_type = PK_MOD_INV_PROPOSALS.GC_INV_SRC_TYPE_IP AND
                NVL(EAGLE.T$INVOICE_PROPOSAL.GET_PAR_MAN_VAL ('PLATN_PROF', cinv.source), 'NULL') = 'Tak')
             THEN
               v_proforma_no := GET_PROFORMA_NUMBER(cinv.inv_id);
             END IF;
     
             IF T$INVOICE.EXISTS_CRD_EX_INVOICE(cinv.inv_id) = 'Y' THEN
                v_payment_type := 'PD';
             ELSE
                v_payment_type := '00';
             END IF;

             IF a_source_type = PK_MOD_INV_PROPOSALS.GC_INV_SRC_TYPE_IP THEN
               v_payment_type := '00';
               v_np := NULL;
               v_npu := NULL;
               IF cinv.invoicing_type = PK_MOD_INV_PROPOSALS.GC_INVOICING_TYPE_SELECT THEN
                 v_c_account   := GET_ACCOUNT('CLIENT_ACCOUNT_SSEL');
               ELSIF cinv.invoicing_type = PK_MOD_INV_PROPOSALS.GC_INVOICING_TYPE_SPS THEN
                 v_c_account   := GET_ACCOUNT('EXTERNAL_CUSTOMER_ACCOUNT');
               ELSE
                PK_ERROR.RAISE_ERROR(-20008, 'Nieznany typ fakturowania dla NAVISION='||cinv.invoicing_type);
               END IF;
             END IF;

             /* na samymy pocz¹tku wprowadŸ wyj¹tek dla noty uznaniowej */
             IF a_doc_type = 'NU' AND a_source_type = 'REM'
             THEN
                cinv.netto := (-1) * cinv.netto;
             END IF;

             IF a_source_type = PK_MOD_INV_PROPOSALS.GC_INV_SRC_TYPE_IP THEN -- GAFA
                 ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(cinv.INV_TYPE),
                              A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.INV_TYPE), -- ???
                              A_DOC_TYPE       => GET_DOC_TYPE(cinv.INV_TYPE), 
                              A_PAYMENT_TYPE   => v_payment_type,
                              A_ACCOUNT        => v_c_account.acc_no,
                              A_MPK            => NULL,
                              A_TAX            => NULL, 
                              A_PRODUCT        => NULL,
                              A_BOOK_DATE      => cinv.invoice_date,
                              A_AMOUNT         => cinv.netto * (-1),
                              A_CODE           => cinv.comp_code,
                              A_DOC_NUMBER     => cinv.invoice_number,
                              A_VAT_RATE       => v_np,
                              A_ORD_ID         => cinv.source,
                              A_PROFORMA_NO    => v_proforma_no,
                              A_INVOICE_DATE   => cinv.invoice_date,
                              A_PAYMENT_DATE   => cinv.pay_date,
                              A_PROJECT        => v_c_account.project,
                              A_TGKV_CODE			 => v_npu,
                              A_VAT_DATE       => cinv.vat_date, 
                              A_SALE_DATE      => cinv.vat_date,
                              A_COR_REASON_NAV_CODE => cinv.COR_REASON_NAV_CODE                 );
              ELSE
                 ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(cinv.INV_TYPE),
                              A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.INV_TYPE), -- ???
                              A_DOC_TYPE       => GET_DOC_TYPE(cinv.INV_TYPE), 
                              A_PAYMENT_TYPE   => v_payment_type,
                              A_ACCOUNT        => v_c_account.acc_no,
                              A_MPK            => NULL,
                              A_TAX            => NULL, 
                              A_PRODUCT        => NULL,
                              A_BOOK_DATE      => cinv.invoice_date,
                              A_AMOUNT         => cinv.netto * (-1),
                              A_CODE           => cinv.comp_code,
                              A_DOC_NUMBER     => cinv.invoice_number,
                              A_VAT_RATE       => v_np,
                              A_ORD_ID         => cinv.source,
                              A_PROFORMA_NO    => v_proforma_no,
                              A_INVOICE_DATE   => cinv.invoice_date,
                              A_PAYMENT_DATE   => cinv.pay_date,
                              A_PROJECT        => v_c_account.project,
                              A_TGKV_CODE       => v_npu,
                              A_VAT_DATE       => cinv.vat_date,
                              A_COR_REASON_NAV_CODE => cinv.COR_REASON_NAV_CODE,
                              A_SALE_DATE      => cinv.sale_date);
              END IF;                            


           FOR cinv_in IN (  SELECT prd.TYPE prd_type,
                                    nvl(prd.expired_date,to_date('2099-12-31', 'YYYY-MM-DD')) prd_exp_date,
                                    prd.TYPE prd_id, cny.code comp_code,
                                    SUM (NVL (inv_lin.net_amount, 0) * (-1)) netto,
                                    SUM (NVL (inv_lin.vat_amount, 0) * (-1)) vat_val,
                                    inv_lin.vat,
                                    inv_lin.source,
                                    inv_lin.source_type,
                                    NULL CODE_TYPE,
                                    NULL PRD_GROUP,
                                    CAST(NULL AS VARCHAR2(500)) NAME,
                                    NULL                        UNIT_PRICE,
                                    NULL                        NET_AMOUNT,
                                    NULL                        QUANTITY   ,         
                                    NULL                        UNIT_PRINTED,       
                                    NULL                        UNIT        ,
                                    NULL                        POS_NUM                                                                                 
                               FROM app_emi.customers cny,
                                    app_fin.invoices inv,
                                    app_fin.invoice_lines inv_lin,
                                    app_stc.products prd
                              WHERE inv.ID = inv_lin.inv_id
                                AND inv.cus_id = cny.ID
                                AND inv_lin.code <> '12'
                                AND inv_lin.code <> '21'
                                AND inv_lin.code <> '25'
                                AND inv_lin.code <> '26'
                                AND inv_lin.code <> '27'
                                AND inv_lin.code <> '28'
                                AND inv_lin.code <> '32'
                                AND (   (    (   (    (    (inv.TYPE = 'N' OR inv.TYPE = 'R')
                                                       AND inv_lin.pos_type = 'I'
                                                      )
                                                  AND a_doc_type = 'N'
                                                 )
                                              OR (    (    (inv.TYPE = 'CN' OR inv.TYPE = 'CR'
                                                           )
                                                       AND inv_lin.pos_type = 'D'
                                                      )
                                                  AND a_doc_type = 'M'
                                                 )
                                             )
                                         AND inv_lin.source_type = 'ORD'
                                         AND a_source_type = 'ORD'
                                        )
                                     OR (    inv.TYPE = 'N'
                                         AND inv_lin.pos_type = 'I'
                                         AND a_doc_type = 'N'
                                         AND inv_lin.source_type = 'TSK'
                                         AND a_source_type = 'TSK'
                                        )
                                     OR (    inv.TYPE = 'NU'
                                         AND inv_lin.pos_type = 'I'
                                         AND a_doc_type = 'NU'
                                         AND inv_lin.source_type = 'REM'
                                         AND a_source_type = 'REM'
                                        )
                                    )
                                AND inv_lin.code = prd.ID
                                AND inv.id = cinv.inv_id
                                AND inv.net_amount <> 0          --nie eksportuj zerowych faktur
                           GROUP BY prd.TYPE,
                                    nvl(prd.expired_date,to_date('2099-12-31', 'YYYY-MM-DD')),
                                    prd.TYPE,
                                    cny.code,
                                    inv_lin.VAT,
                                    inv_lin.source,
                                    inv_lin.source_type
                       -- GAFA
                       UNION ALL
                       SELECT PR.PRODUCT_NAV_CODE prd_type,
                                    NULL prd_exp_date,
                                    PR.PRODUCT_NAV_CODE prd_id, 
                                    cny.code comp_code,
                                    NVL (inv_lin.net_amount, 0) * (-1) netto,
                                    NVL (inv_lin.vat_amount, 0) * (-1) vat_val,
                                    inv_lin.vat,
                                    inv_lin.source,
                                    inv_lin.source_type, 
                                    INV_LIN.CODE_TYPE,
                                    PT.PRD_GROUP  ,
                                    INV_LIN.NAME  ,
                                    inv_lin.UNIT_PRICE           UNIT_PRICE,
                                    inv_lin.NET_AMOUNT           NET_AMOUNT,
                                    inv_lin.QUANTITY_PRINTED     QUANTITY   ,         
                                    inv_lin.UNIT_PRINTED         UNIT_PRINTED,       
                                    inv_lin.UNIT                 UNIT   ,    
                                    inv_lin.POS_NUM              POS_NUM               
                               FROM app_fin.invoices inv 
                               JOIN app_fin.invoice_lines inv_lin ON inv.ID = inv_lin.inv_id
                               JOIN app_emi.customers cny ON inv.cus_id = cny.ID
                               LEFT JOIN APP_FIN.INVOICE_PROPOSALS ip ON (IP.ID = inv_lin.SOURCE AND inv_lin.SOURCE_TYPE = PK_MOD_INV_PROPOSALS.GC_INV_SRC_TYPE_IP)
                               LEFT JOIN APP_COR.PROGRAMS pr on PR.CODE = IP.PROGRAM_CODE
                               LEFT JOIN app_stc.products prd ON INV_LIN.CODE_TYPE = 'P' AND prd.ID = inv_lin.code
                               LEFT JOIN app_stc.PRODUCT_TYPES pt ON PT.TYPE = PRD.TYPE
                              WHERE 
                              inv.id = cinv.inv_id
                                AND inv.net_amount <> 0          --nie eksportuj zerowych faktur
                                AND inv_lin.source_type = a_source_type
                                AND a_source_type = PK_MOD_INV_PROPOSALS.GC_INV_SRC_TYPE_IP                                                                                      
                           ORDER BY prd_id  )
             LOOP
             
                 /* na samymy pocz¹tku wprowadŸ wyj¹tek dla noty uznaniowej */
                 IF a_doc_type = 'NU' AND a_source_type = 'REM'
                 THEN
                    cinv_in.netto := (-1) * cinv_in.netto;
                 END IF;             
             
             
                 IF cinv_in.prd_id IN ('1', '4', '5', '6', '8', '11', '2', '3', 
                                       '0', '12','21','13','16','17','18', 
                                       '22', '23', '24', '25', '26','27','28','32')
                 AND a_source_type <> PK_MOD_INV_PROPOSALS.GC_INV_SRC_TYPE_IP
                 THEN
                 
                    IF a_doc_type = 'N' AND a_source_type = 'ORD'
                    THEN                                        --DLA ORGINALNYCH not
                       v_meal_account :=
                          get_prd_account ('CLI_PRD_TURN_ACCOUNT',
                                           cinv_in.prd_type,
                                           cinv_in.prd_exp_date);
                    END IF;

                    IF a_doc_type = 'M' AND a_source_type = 'ORD'
                    THEN                                                  --dla korekt
                       v_meal_account :=
                          get_prd_account ('CLI_PRD_TURN_ACCOUNT_COR',
                                           cinv_in.prd_type,
                                           cinv_in.prd_exp_date);
                    END IF;

                    IF a_doc_type = 'N' AND a_source_type = 'TSK'
                    THEN                             --DLA NOT OBCI¥¯ENIOWYCH Z WYMIAN
                       v_meal_account :=
                          get_prd_account ('CLI_PRD_TURN_CHARGE_EXCH_ACCOUNT',
                                           cinv_in.prd_type,
                                           cinv_in.prd_exp_date);
                    END IF;

                    IF a_doc_type = 'NU' AND a_source_type = 'REM'
                    THEN                                --DLA NOT UZNANIOWYCH Z WYMIAN
                       v_meal_account :=
                          get_prd_account ('CLI_PRD_TURN_ACCEPT_EXCH_ACCOUNT',
                                           cinv_in.prd_type,
                                           cinv_in.prd_exp_date);
                    END IF;
                    
                 ELSIF a_source_type = PK_MOD_INV_PROPOSALS.GC_INV_SRC_TYPE_IP THEN -- GAFA
                 
                    IF a_source_type = PK_MOD_INV_PROPOSALS.GC_INV_SRC_TYPE_IP THEN                                  
                      IF cinv.invoicing_type = PK_MOD_INV_PROPOSALS.GC_INVOICING_TYPE_SELECT THEN
                        IF cinv_in.code_type = 'P' AND cinv_in.PRD_GROUP = 'V' THEN 
                          v_meal_account   := GET_ACCOUNT('SELECT_CUST_TURN_VOU_ACCOUNT');
                        ELSIF cinv_in.code_type = 'P' AND cinv_in.PRD_GROUP = 'EC' THEN
                          v_meal_account   := GET_ACCOUNT('SELECT_CUST_TURN_CARD_ACCOUNT');
                        ELSIF cinv_in.code_type = 'PE' THEN 
                          v_meal_account   := GET_ACCOUNT('SELECT_CUST_TURN_3_PARTY_ACCOUNT');
                        ELSIF cinv_in.code_type = 'PEFLX' THEN 
                          v_meal_account   := GET_ACCOUNT('AFF_EV_PRD_ACCOUNT');
                        ELSE
                          PK_ERROR.RAISE_ERROR(-20008, 'Nieznana kombinacja warunków code_type: '||cinv_in.code_type||' i PRD_GROUP: '||cinv_in.PRD_GROUP||' dla typu fakturowania: '||cinv.invoicing_type);
                        END IF;
                      ELSIF cinv.invoicing_type = PK_MOD_INV_PROPOSALS.GC_INVOICING_TYPE_SPS THEN
                        IF cinv_in.code_type = 'IP' THEN
                          v_meal_account   := GET_ACCOUNT('EXT_CUS_SETTLE_ACCOUNT');
                        END IF;
                      ELSE
                        PK_ERROR.RAISE_ERROR(-20008, 'Nieznany typ fakturowania dla NAVISION: '||cinv.invoicing_type);
                      END IF;                      
                    END IF; 
                      
                 END IF;
                   
                 -- wymiar CPACK na projekcie
                 -- dla nie CPACKow pozostaje domyœlny projekt z SCALA_ACCOUNTS - bo tak by³o ;)
                 v_project := v_meal_account.project;
                 -- tylko dla zrodla typu zamowienie                       
                 IF cinv_in.source_type = 'ORD' THEN
                   -- czy mamy na zam. parametr CARD_PACK
                   SELECT COUNT(*) 
                   INTO v_cpack_par_count
                   FROM APP_EMI.ORDER_PARAMETERS op 
                   WHERE op.ord_id = cinv_in.source 
                   AND  op.par_code = 'CARD_PACK';
                   IF v_cpack_par_count > 0 THEN
                     v_project := 'CPACK';
                   END IF;         
                 END IF;
                            
                 IF a_source_type = PK_MOD_INV_PROPOSALS.GC_INV_SRC_TYPE_IP THEN -- GAFA
                   DECLARE
                     v_product VARCHAR2(1000);
                     v_tax     VARCHAR2(1000);
                   BEGIN
                     IF cinv_in.code_type = 'PEFLX' THEN
                       v_product := 'VU1';
                       v_tax     := NULL;
                     ELSE
                       v_product := cinv_in.prd_id;
                       v_tax     := 'PDO';
                     END IF;    

                     IF cinv.inv_type not in (PK_MOD_INV_PROPOSALS.GC_INV_TYPE_NG, PK_MOD_INV_PROPOSALS.GC_INV_TYPE_RG) THEN
                     -- Ze wzglêdu na GET_EXP_NAV_UP_AND_Q (tam a_doc_type to orygina³), przy dodawaniu korekt obs³u¿yæ
                       PK_ERROR.RAISE_ERROR(-20008, 'Nieobs³ugiwany typ dokumentu Gafa : '||cinv.inv_type||', inv_id: '||cinv.inv_id);
                     END IF;

                     v_unit_price := NULL;
                     v_quantity   := NULL;
                     GET_EXP_NAV_UP_AND_Q  (a_inv_id => cinv.inv_id,
                                            a_doc_type => 'C', -- C -orygina³, K - korekta: Jeœli bêd¹ korekty to obs³u¿yæ
                                            a_pos_num  => cinv_in.pos_num,
                                            a_line_quantity_printed => cinv_in.quantity,
                                            a_line_unit => cinv_in.unit,
                                            a_line_netto_amount => cinv_in.net_amount,
                                            a_line_unit_price => cinv_in.unit_price,
                                            p_unit_price => v_unit_price,
                                            p_quantity   => v_quantity);

                     ADD_NAV_INV( A_NUM_SEQUENCE   => GET_INV_SEQUENCE(cinv.INV_TYPE),
                                  A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.INV_TYPE), -- ???
                                  A_DOC_TYPE       => GET_DOC_TYPE(cinv.INV_TYPE), 
                                  A_PAYMENT_TYPE   => v_payment_type,
                                  A_ACCOUNT        => v_meal_account.acc_no,
                                  A_MPK            => NULL,
                                  A_TAX            => v_tax, 
                                  A_PRODUCT        => v_product,
                                  A_BOOK_DATE      => cinv.invoice_date,
                                  A_AMOUNT         => cinv_in.netto,
                                  A_CODE           => cinv.comp_code,
                                  A_DOC_NUMBER     => cinv.invoice_number,
                                  A_VAT_RATE       => GET_VAT_RATE(cinv_in.vat, cinv.inv_type),
                                  A_ORD_ID         => cinv_in.source,
                                  A_PROFORMA_NO    => v_proforma_no,
                                  A_INVOICE_DATE   => cinv.invoice_date,
                                  A_PAYMENT_DATE   => cinv.pay_date,
                                  A_PROJECT        => v_project,
                                  a_tgkv_code			 => 'NP U',
                                  A_VAT_DATE       => cinv.vat_date, 
                                  A_SALE_DATE      => cinv.vat_date,
                                  A_INVOICE_LINE_NAME   => NULL, -- TODO: ZMIENIC NA WLASCIWY cinv_in.NAME);
                                  A_INVOICE_LINE_NAME_2 => NULL,
                                  A_UNIT_PRICE          => v_unit_price,
                                  A_QUANTITY            => v_quantity,                                                                          
                                  A_UNIT_NAV_CODE       => GET_UNIT_NAV_CODE(cinv.invoice_number, cinv_in.UNIT_PRINTED ),
                                  A_COR_REASON_NAV_CODE => NULL);
                                  
                   END;                                  
                  ELSE

                     ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(cinv.INV_TYPE),
                                  A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.INV_TYPE), -- ???
                                  A_DOC_TYPE       => GET_DOC_TYPE(cinv.INV_TYPE), 
                                  A_PAYMENT_TYPE   => v_payment_type,
                                  A_ACCOUNT        => v_meal_account.acc_no,
                                  A_MPK            => NULL,
                                  A_TAX            => NULL, 
                                  A_PRODUCT        => cinv_in.prd_id,
                                  A_BOOK_DATE      => cinv.invoice_date,
                                  A_AMOUNT         => cinv_in.netto,
                                  A_CODE           => cinv.comp_code,
                                  A_DOC_NUMBER     => cinv.invoice_number,
                                  A_VAT_RATE       => GET_VAT_RATE(cinv_in.vat, cinv.inv_type),
                                  A_ORD_ID         => cinv_in.source,
                                  A_PROFORMA_NO    => v_proforma_no,
                                  A_INVOICE_DATE   => cinv.invoice_date,
                                  A_PAYMENT_DATE   => cinv.pay_date,
                                  A_PROJECT        => v_project,
                                  a_tgkv_code       => 'NP U',
                                  A_VAT_DATE       => cinv.vat_date,
                                  A_COR_REASON_NAV_CODE => NULL,
                                  A_SALE_DATE     => cinv.sale_date);
                  END IF;
                                                
                          
             END LOOP;
        
        END LOOP;

EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.WRITE_ERROR(sqlcode,v_debug_string||sqlerrm||'; '||DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
      PK_ERROR.RAISE_ERROR(sqlcode,v_debug_string||sqlerrm,'SWP_NAV_CNO');
      
END;

PROCEDURE aff_inv (
  a_input_date      IN       DATE,
  a_doc_type        IN       VARCHAR2)
IS
   v_a_account        r_acc;
   v_vat_account      r_acc;
   v_aff_inc_account  r_acc;
   
   v_debug_string   eagle.adm_audit_modules.arg_string%TYPE;
   v_proforma_no    app_fin.invoices.invoice_number%TYPE;

   v_unit_price  NUMBER;
   v_quantity    NUMBER;

BEGIN

   v_debug_string := 'doc='||a_doc_type||' date='||to_char(a_input_date);
   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','AFF_INV',v_debug_string);
   
   v_a_account   := GET_ACCOUNT('AFFILIANT_ACCOUNT');
   v_vat_account := GET_ACCOUNT('VAT_ACCOUNT');
   
   IF a_doc_type = 'A' THEN
      v_aff_inc_account := GET_ACCOUNT('AFF_INCOME_ACCOUNT');
   ELSIF a_doc_type = 'B' THEN
      v_aff_inc_account := GET_ACCOUNT('AFF_INCOME_ACCOUNT_C');
   ELSE
      PK_ERROR.RAISE_ERROR(-20008, 'Nieznany parametr doc_type='||a_doc_type);
   END IF;   
   
   FOR cinv IN (SELECT com.code comp_code,
                 i.invoice_number,
                 SUM (il.gross_amount) * (-1) pay_fv,
                 SUM (il.vat_amount) * (-1) vat_val,
                 i.ID inv_id,
                 decode(T$INVOICE.GET_NO_COMP_PAYMENT_DELAY(I.ID),'-1','PD','-2','PD','00') PAYMENT_TYPE, 
                 i.invoice_date,
                 DECODE (t$invoice.get_no_comp_payment_delay (i.ID),
                           -1, r.reimb_date,
                           -2, r.reimb_date,
                          r.reimb_date+ t$invoice.get_no_comp_payment_delay (i.ID)
                          ) pay_date, 
                 f_g_final_account ( com.VAT_REG_NUM, a_doc_type,v_a_account.acc_no) final_account,
                 f_g_final_account ( com.VAT_REG_NUM, a_doc_type,v_aff_inc_account.acc_no) final_aff_inc_account,
                 i.TYPE INV_TYPE,
                 i.vat_date,
                 CR.COR_REASON_NAV_CODE  COR_REASON_NAV_CODE
            FROM app_rmb.remittances r,
                 app_fin.invoices i,
                 app_fin.invoice_lines il,
                 app_rmb.market_points com,
                 APP_FIN.COR_REASON CR 
           WHERE il.SOURCE = r.ID
             AND i.ID = il.inv_id
             AND com.ID = i.mkp_id
             AND CR.ID (+) = I.COR_REASON
             AND i.gross_amount != 0
             AND i.status = 'P'
             AND (   (i.TYPE = 'IA' AND il.pos_type = 'I'
                               AND a_doc_type = 'A'
                     )
                  OR (i.TYPE = 'CIA' AND il.pos_type = 'D'
                               AND a_doc_type = 'B'
                     )
                 )
                      AND TRUNC (i.invoice_date) = a_input_date
        GROUP BY com.code,
                 i.invoice_number,
                 i.ID,
                 decode(T$INVOICE.GET_NO_COMP_PAYMENT_DELAY(I.ID),'-1','PD','-2','PD','00'),
                 i.invoice_date,
                 DECODE (t$invoice.get_no_comp_payment_delay (i.ID),
                           -1, r.reimb_date,
                           -2, r.reimb_date,
                          r.reimb_date+ t$invoice.get_no_comp_payment_delay (i.ID)
                          ),
                 f_g_final_account ( com.VAT_REG_NUM, a_doc_type,v_a_account.acc_no),
                 f_g_final_account ( com.VAT_REG_NUM, a_doc_type,v_aff_inc_account.acc_no),
                 i.TYPE,
                 i.vat_date,
                 CR.COR_REASON_NAV_CODE 
        ORDER BY invoice_number)
   LOOP
   
        t$invoice.set_status (cinv.inv_id, 'E');
        
         ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(cinv.INV_TYPE),
                      A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.INV_TYPE), -- ???
                      A_DOC_TYPE       => GET_DOC_TYPE(cinv.INV_TYPE), 
                      A_PAYMENT_TYPE   => cinv.PAYMENT_TYPE,
                      A_ACCOUNT        => cinv.final_account,
                      A_MPK            => NULL,
                      A_TAX            => NULL, 
                      A_PRODUCT        => NULL,
                      A_BOOK_DATE      => cinv.invoice_date,
                      A_AMOUNT         => cinv.pay_fv * (-1),
                      A_CODE           => cinv.comp_code,
                      A_DOC_NUMBER     => cinv.invoice_number,
                      A_VAT_RATE       => NULL,
                      A_ORD_ID         => NULL,
                      A_PROFORMA_NO    => NULL,
                      A_INVOICE_DATE   => cinv.invoice_date,
                      A_PAYMENT_DATE   => cinv.pay_date,
                      A_PROJECT        => v_a_account.project,
                      A_VAT_DATE       => cinv.vat_date,
                      A_COR_REASON_NAV_CODE => cinv.COR_REASON_NAV_CODE );


         ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(cinv.INV_TYPE),
                      A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.INV_TYPE), -- ???
                      A_DOC_TYPE       => GET_DOC_TYPE(cinv.INV_TYPE), 
                      A_PAYMENT_TYPE   => cinv.PAYMENT_TYPE,
                      A_ACCOUNT        => v_vat_account.acc_no,
                      A_MPK            => NULL,
                      A_TAX            => NULL, 
                      A_PRODUCT        => NULL,
                      A_BOOK_DATE      => cinv.invoice_date,
                      A_AMOUNT         => cinv.vat_val,
                      A_CODE           => cinv.comp_code,
                      A_DOC_NUMBER     => cinv.invoice_number,
                      A_VAT_RATE       => NULL,
                      A_ORD_ID         => NULL,
                      A_PROFORMA_NO    => NULL,
                      A_INVOICE_DATE   => cinv.invoice_date,
                      A_PAYMENT_DATE   => cinv.pay_date,
                      A_PROJECT        => v_vat_account.project,
                      A_VAT_DATE       => cinv.vat_date,
                      A_COR_REASON_NAV_CODE => NULL);
     
        FOR cinv_in IN (SELECT (il.gross_amount) * (-1) pay_fv,
                               (il.vat_amount) * (-1) vat_val,
                               il.code prd,
                               il.VAT,
                               SUBSTR(IL.NAME,1,50) NAME,
                               SUBSTR(IL.NAME,51,50)   NAME_2,
                               IL.UNIT_PRICE           UNIT_PRICE,
                               IL.NET_AMOUNT           NET_AMOUNT,
                               IL.QUANTITY_PRINTED     QUANTITY   ,         
                               IL.UNIT                 UNIT,       
                               IL.UNIT_PRINTED         UNIT_PRINTED,       
                               CR.COR_REASON_NAV_CODE  COR_REASON_NAV_CODE,
                               IL.POS_NUM              POS_NUM
                        FROM app_fin.invoices i,
                             app_fin.invoice_lines il,
                             APP_FIN.COR_REASON CR 
                       WHERE i.ID = il.inv_id
                         AND CR.ID (+) = I.COR_REASON
                         AND il.gross_amount != 0
                         AND i.id = cinv.inv_id
                         AND ((i.TYPE = 'IA' AND il.pos_type = 'I'
                                             AND a_doc_type = 'A'
                              )
                           OR (i.TYPE = 'CIA' AND il.pos_type = 'D'
                                              AND a_doc_type = 'B'
                              ))                  
                    ORDER BY prd, IL.POS_NUM)
        LOOP

            v_unit_price := NULL;
            v_quantity   := NULL;
            
            DECLARE
              v_doc_type VARCHAR2(10);
            BEGIN
              IF a_doc_type = 'A' THEN 
                v_doc_type := 'C';
              ELSE
                v_doc_type := 'K';
              END IF;
              GET_EXP_NAV_UP_AND_Q  (a_inv_id => cinv.inv_id,
                                     a_doc_type => v_doc_type, -- C -orygina³, K - korekta
                                     a_pos_num  => cinv_in.pos_num,
                                     a_line_quantity_printed => cinv_in.quantity,
                                     a_line_unit => cinv_in.unit,
                                     a_line_netto_amount => cinv_in.net_amount,
                                     a_line_unit_price => cinv_in.unit_price,
                                     p_unit_price => v_unit_price,
                                     p_quantity   => v_quantity);
            END;                                     

             ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(cinv.INV_TYPE),
                          A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.INV_TYPE), -- ???
                          A_DOC_TYPE       => GET_DOC_TYPE(cinv.INV_TYPE), 
                          A_PAYMENT_TYPE   => cinv.PAYMENT_TYPE,
                          A_ACCOUNT        => cinv.final_aff_inc_account,
                          A_MPK            => NULL,
                          A_TAX            => 'PDO', 
                          A_PRODUCT        => cinv_in.prd,
                          A_BOOK_DATE      => cinv.invoice_date,
                          A_AMOUNT         => cinv_in.pay_fv - cinv_in.vat_val,
                          A_CODE           => cinv.comp_code,
                          A_DOC_NUMBER     => cinv.invoice_number,
                          A_VAT_RATE       => GET_VAT_RATE(cinv_in.vat, cinv.inv_type),
                          A_ORD_ID         => NULL,
                          A_PROFORMA_NO    => NULL,
                          A_INVOICE_DATE   => cinv.invoice_date,
                          A_PAYMENT_DATE   => cinv.pay_date,
                          A_PROJECT        => v_aff_inc_account.project,
                          a_tgkv_code			 => get_tgkv_code_sell(cinv_in.vat,'N'),
                          A_VAT_DATE       => cinv.vat_date,
                          A_INVOICE_LINE_NAME => cinv_in.NAME,
                          A_INVOICE_LINE_NAME_2 => cinv_in.NAME_2,                                                                              
                          A_UNIT_PRICE          => v_unit_price,
                          A_QUANTITY            => v_quantity,                                                                          
                          A_UNIT_NAV_CODE       => GET_UNIT_NAV_CODE(cinv.invoice_number, cinv_in.UNIT_PRINTED ),                                                           
                          A_COR_REASON_NAV_CODE => NULL                                                                
                          );
        
        END LOOP;
      
   END LOOP;   

EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,v_debug_string||sqlerrm,'SWP_NAV_AIN');
END;

PROCEDURE aff_inv_vb (
  a_sess_id         IN       NUMBER,
  a_transf_date     IN       DATE,
  a_export_id       IN       NUMBER)
IS
   v_inc_account      r_acc;
   v_vat_account      r_acc;
   
   v_debug_string   eagle.adm_audit_modules.arg_string%TYPE;
   v_session TV$INVOICE_SESSION;
   v_i NUMBER;
   v_comp_code app_rmb.market_points.code%TYPE;
   v_inv_type VARCHAR2(5);
BEGIN

   v_debug_string := 'sess='||a_sess_id;
   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','AFF_INV_VB',v_debug_string);
   
      v_inc_account     := GET_ACCOUNT('AFFILIANT_ACCOUNT_VB');
      v_vat_account     := GET_ACCOUNT('VAT_ACCOUNT_INP_VB');
   
      v_session :=EAGLE.TV$INVOICE_SESSION(a_sess_id);
	   
	  v_i:=v_session.INVOICES.ITEMS.FIRST;
	  LOOP
	     
         -- nie eksportowane sa faktury oznaczone jako rêcznie wyeksportowane
         IF NVL(v_session.INVOICES.ITEMS(v_i).INV.HAND_MADE,'N') = 'N' THEN  	  
          
              -- Konto MP
              select com.code
              into   v_comp_code 
              from app_rmb.market_points com
              where com.ID = v_session.INVOICES.ITEMS(v_i).INV.MKP_ID;
    	
    		  
              -- 2. poziom podsumowania
              -- linie podsumowuj¹ce per produkt
              FOR K IN   (SELECT I.INVOICE_NUMBER,
                                 I.ID,
                                 TRUNC(I.INVOICE_DATE) INVOICE_DATE,
                                 TRUNC(I.PAYMENT_DATE) PAYMENT_DATE,
                                 --'REDEM.VB-' LINE_NAME,
                                 SUM(IL.NET_AMOUNT) NET_AMOUNT,
                                 SUM(IL.VAT_AMOUNT) VAT_AMOUNT,
                                 i.TYPE INV_TYPE
                          FROM   APP_VFIN.INVOICES I,
                                 APP_VFIN.INVOICE_LINES IL
                          WHERE  IL.VINV_ID = I.ID
                          AND    I.ID = v_session.INVOICES.ITEMS(v_i).INV.ID
                          AND    IL.POS_TYPE = DECODE(i.TYPE,'I','I','CI','D',NULL)
                          --AND    IL.CRF_ID IS NOT NULL
                          GROUP BY I.INVOICE_NUMBER,
                                   I.ID,
                                   TRUNC(I.INVOICE_DATE),
                                   TRUNC(I.PAYMENT_DATE),
                                   i.TYPE
--                          UNION
--                          SELECT I.INVOICE_NUMBER,
--                                 I.ID,
--                                 TRUNC(I.INVOICE_DATE),
--                                 TRUNC(I.PAYMENT_DATE),
--                                 --IL.NAME LINE_NAME,
--                                 IL.NET_AMOUNT NET_AMOUNT,
--                                 IL.VAT_AMOUNT VAT_AMOUNT,
--                                 i.TYPE INV_TYPE
--                          FROM   APP_VFIN.INVOICES I,
--                                 APP_VFIN.INVOICE_LINES IL
--                          WHERE  IL.VINV_ID = I.ID
--                          AND    I.ID = v_session.INVOICES.ITEMS(v_i).INV.ID
--                          AND    IL.POS_TYPE = DECODE(i.TYPE,'I','I','CI','D',NULL)
--                          AND    IL.CRF_ID IS NULL
                          )
              LOOP
                IF k.PAYMENT_DATE IS NULL THEN
                  eagle.pk_error.write_error (SQLCODE,SQLERRM|| ' f_aff_inv_vb puste PAYMENT_DATE dla faktrury o id '||v_session.INVOICES.ITEMS(v_i).INV.ID);
                  pk_error.raise_error (SQLCODE,SQLERRM,' f_aff_inv_vb puste PAYMENT_DATE dla faktrury o id '||v_session.INVOICES.ITEMS(v_i).INV.ID);
                END IF;
    						
                IF k.inv_type = 'CI' AND (k.net_amount+k.vat_amount) > 0 THEN
                	v_inv_type := 'I';
                ELSE
                	v_inv_type := k.inv_type;
                END IF;
                
                 ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(v_inv_type),
                              A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(v_inv_type), -- ???
                              A_DOC_TYPE       => GET_DOC_TYPE(v_inv_type), 
                              A_PAYMENT_TYPE   => 'PD',
                              A_ACCOUNT        => v_inc_account.acc_no,
                              A_MPK            => NULL,
                              A_TAX            => NULL, 
                              A_PRODUCT        => NULL,
                              A_BOOK_DATE      => a_transf_date,
                              A_AMOUNT         => abs(K.net_amount + K.vat_amount) *  (-1), 
                              A_CODE           => v_comp_code,
                              A_DOC_NUMBER     => K.invoice_number,
                              A_VAT_RATE       => NULL,
                              A_ORD_ID         => NULL,
                              A_PROFORMA_NO    => NULL,
                              A_INVOICE_DATE   => k.invoice_date,
                              A_PAYMENT_DATE   => k.payment_date,
                              A_PROJECT        => v_inc_account.project);


                 --IF K.VAT_AMOUNT <> 0 THEN
                 
                     ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(v_inv_type),
                                  A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(v_inv_type), -- ???
                                  A_DOC_TYPE       => GET_DOC_TYPE(v_inv_type), 
                                  A_PAYMENT_TYPE   => 'PD',
                                  A_ACCOUNT        => v_vat_account.acc_no,
                                  A_MPK            => NULL,
                                  A_TAX            => NULL, 
                                  A_PRODUCT        => NULL,
                                  A_BOOK_DATE      => a_transf_date,
                                  A_AMOUNT         => abs(K.VAT_AMOUNT),
                                  A_CODE           => v_comp_code,
                                  A_DOC_NUMBER     => K.invoice_number,
                                  A_VAT_RATE       => NULL,
                                  A_ORD_ID         => NULL,
                                  A_PROFORMA_NO    => NULL,
                                  A_INVOICE_DATE   => k.invoice_date,
                                  A_PAYMENT_DATE   => k.payment_date,
                                  A_PROJECT        => v_vat_account.project);

                 --END IF;
                 
              END LOOP;
    		  
              -- 3. poziom podsumowania
              -- linie per produkt (sumowanie), ew. per linia faktury nie zw. z rozliczeniem (brak sumowania) 
              FOR K IN       (SELECT I.INVOICE_NUMBER,
                                     I.ID,
                                     TRUNC(I.INVOICE_DATE) invoice_date,
                                     TRUNC(I.PAYMENT_DATE) payment_date,
                                     IL.VAT,
                                     IL.NAME LINE_NAME,
                                     IL.CRF_ID,
                                     NVL(TO_CHAR(SA.ACC_NO),' ') ACCOUNT_NBR,
                                     NVL(SA.MPK,' ') COST_CENTER,
                                     NVL(SA.SERVICE,' ') PRODUCT_NUMBER,
                                     NVL(SA.TAX,' ') TAX,
                                     SUM(IL.NET_AMOUNT) NET_AMOUNT,
                                     SUM(IL.VAT_AMOUNT) VAT_AMOUNT,
                                     i.TYPE INV_TYPE,
                                     sa.PROJECT
                              FROM   APP_VFIN.INVOICES I,
                                     APP_VFIN.INVOICE_LINES IL,
                                     APP_FIN.SCALA_ACCOUNTS SA
                              WHERE  IL.VINV_ID = I.ID
                              AND    I.ID = v_session.INVOICES.ITEMS(v_i).INV.ID
                              AND    IL.POS_TYPE = DECODE(i.TYPE,'I','I','CI','D',NULL)
                              AND    IL.CRF_ID IS NOT NULL
                              AND    UPPER (SA.ACC_NAME (+)) = 'AFF_PRD_TURN_ACCOUNT_VB'
                              AND    SA.PRD_TYPE (+) = TO_CHAR(IL.CRF_ID +100)
                              GROUP BY I.INVOICE_NUMBER,
                                       I.ID,
                                       TRUNC(I.INVOICE_DATE),
                                       TRUNC(I.PAYMENT_DATE),
                                       IL.VAT,
                                       IL.NAME,
                                       IL.CRF_ID,
                                       SA.ACC_NO,
                                       SA.MPK,
                                       SA.SERVICE,
                                       SA.TAX,
                                       i.TYPE,
                                       sa.PROJECT
                              UNION
                              SELECT I.INVOICE_NUMBER,
                                     I.ID,
                                     TRUNC(I.INVOICE_DATE),
                                     TRUNC(I.PAYMENT_DATE),
                                     IL.VAT,
                                     IL.NAME LINE_NAME,
                                     IL.CRF_ID,
                                     NVL(TO_CHAR(IL.ACC_ACCOUNT),' ') ACCOUNT_NBR,
                                     NVL(IL.ACC_COST_CENTER,' ') COST_CENTER,
                                     NVL(TO_CHAR(IL.ACC_PRODUCT_NUMBER),' ') PRODUCT_NUMBER,
                                     NVL(IL.ACC_TAX,' ') TAX,
                                     IL.NET_AMOUNT NET_AMOUNT,
                                     IL.VAT_AMOUNT VAT_AMOUNT,
                                     I.TYPE INV_TYPE,
                                     NULL
                              FROM   APP_VFIN.INVOICES I,
                                     APP_VFIN.INVOICE_LINES IL
                              WHERE  IL.VINV_ID = I.ID
                              AND    I.ID = v_session.INVOICES.ITEMS(v_i).INV.ID
                              AND    IL.POS_TYPE = DECODE(i.TYPE,'I','I','CI','D',NULL)
                              AND    IL.CRF_ID IS NULL
                              ORDER BY ACCOUNT_NBR, COST_CENTER, TAX, PRODUCT_NUMBER
                              )
               LOOP
                               
                 IF K.CRF_ID  IS NULL THEN
                   IF K.ACCOUNT_NBR IS NULL THEN
                      eagle.pk_error.write_error (SQLCODE,SQLERRM|| ' f_aff_inv_vb puste ACC_ACCOUNT dla faktrury o id '||v_session.INVOICES.ITEMS(v_i).INV.ID);
                      pk_error.raise_error (SQLCODE,SQLERRM,' f_aff_inv_vb puste ACC_ACCOUNT dla faktrury o id '||v_session.INVOICES.ITEMS(v_i).INV.ID);
                   END IF;
                 END IF;
                 
                 IF k.inv_type = 'CI' AND (k.net_amount + k.vat_amount) > 0 THEN
                		v_inv_type := 'I';
                 ELSE
                	v_inv_type := k.inv_type;
                 END IF;
                   
                 ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(v_inv_type),
                              A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(v_inv_type), -- ???
                              A_DOC_TYPE       => GET_DOC_TYPE(v_inv_type), 
                              A_PAYMENT_TYPE   => 'PD',
                              A_ACCOUNT        => K.ACCOUNT_NBR,
                              A_MPK            => K.COST_CENTER,
                              A_TAX            => k.TAX,
                              A_PRODUCT        => K.PRODUCT_NUMBER,
                              A_BOOK_DATE      => a_transf_date,
                              A_AMOUNT         => abs(K.NET_AMOUNT),
                              A_CODE           => v_comp_code,
                              A_DOC_NUMBER     => K.invoice_number,
                              A_VAT_RATE       => GET_VAT_RATE(k.VAT, k.inv_type),
                              A_ORD_ID         => NULL,
                              A_PROFORMA_NO    => NULL,
                              A_INVOICE_DATE   => k.invoice_date,
                              A_PAYMENT_DATE   => k.payment_date,
                              A_PROJECT        => k.project,
                              A_TGKV_CODE			 => GET_TGKV_CODE_BUY(k.vat,'Y',null, null));

               END LOOP;
         END IF;
		  
	  EXIT WHEN v_i=v_session.INVOICES.ITEMS.LAST;
	    v_i:=v_session.INVOICES.ITEMS.NEXT(v_i);
	  END LOOP;	  

	  -- zmiana statusów sesji, faktur i rozliczeñ
	  v_session.set_exported;
	  -- dane o exporcie zapisane w sesji
      v_session.EXPORT_USER := USER;
	  v_session.EXPORT_DATE := SYSDATE;
	  v_session.EXPORT_ID := a_export_id;
	  v_session.SERIALIZE;
	  v_session.UNLOCK_;

EXCEPTION
    WHEN OTHERS THEN
         IF v_session IS NOT NULL THEN
            v_session.UNLOCK_AUT;
            v_session:=NULL;
         END IF;    
      PK_ERROR.RAISE_ERROR(sqlcode,v_debug_string||sqlerrm,'SWP_NAV_AIV');
END;


PROCEDURE Acc_Reck_Inv (
    a_input_date IN DATE )
IS
    v_a_account           r_acc;
    v_vat_account         r_acc;
    v_aff_inc_account     r_acc;
    v_aff_inc_account_ev  r_acc;    
    a_effective_inc_account APP_FIN.SCALA_ACCOUNTS.ACC_NO%TYPE;
    v_debug_string   eagle.adm_audit_modules.arg_string%TYPE;
    v_reck_id             APP_FIN.INVOICE_LINES.SOURCE%TYPE;   
    v_o$reckoning         t$reckoning;
    v_payment_type        VARCHAR2(2); 
    v_payment_date        DATE;
    v_sale_date           DATE;                    
    v_product             VARCHAR2(10);
    v_net_amount          NUMBER (12,2);
    v_net_amount_part     NUMBER (12,2);
    v_net_amount_sum      NUMBER (12,2);
    v_prd_type_count      NUMBER;
    v_prd_type_counter    NUMBER;
    
    CURSOR get_IAC_invoices IS
        SELECT   com.code comp_code,
                 i.invoice_number,
                 SUM (il.gross_amount) pay_fv,
                 SUM (il.vat_amount) * (-1) vat_val,
                 i.id inv_id,
                 i.invoice_date,
                 i.payment_date pay_date, 
                 i.type inv_type,
                 i.vat_date
            FROM app_fin.invoices i,
                 app_fin.invoice_lines il,
                 app_rmb.market_points com
            WHERE i.id = il.inv_id
            AND   com.id = i.mkp_id
            AND   i.gross_amount != 0
            AND   i.status = 'P'
            AND   i.type = 'IAC' -- UWAGA: PRZY DODANIU KOREKTY OBS£U¯YÆ A_UNIT_PRICE, A_QUANTITY (powinno byæ ustalane z procedury GET_EXP_NAV_UP_AND_Q)  
            AND   il.pos_type = 'I'
            AND   TRUNC (i.invoice_date) = a_input_date
            GROUP BY com.code,
                     i.invoice_number,
                     i.id,
                     i.invoice_date,
                     i.payment_date,
                     i.type,
                     i.vat_date
            ORDER BY invoice_number; 
            
    CURSOR get_IAC_inv_products(a_inv_id NUMBER) IS 
        SELECT SUM (il.net_amount) * (-1) net_fv,
               il.code code,
               il.vat,
               SUBSTR(MIN(IL.NAME),1 ,50) NAME,
               SUBSTR(MIN(IL.NAME),51,50) NAME_2,
               IL.UNIT,
               IL.UNIT_PRINTED,
               IL.QUANTITY_PRINTED
        FROM app_fin.invoices i,
             app_fin.invoice_lines il
        WHERE i.id = il.inv_id
        AND   il.gross_amount != 0
        AND   i.id = a_inv_id
        AND   i.TYPE = 'IAC' 
        AND   il.pos_type = 'I'
        GROUP BY il.code,
                 il.vat,
                 IL.UNIT,
                 IL.UNIT_PRINTED,
                 IL.QUANTITY_PRINTED
        ORDER BY code;    
                   
BEGIN
    v_debug_string := 'date='||to_char(a_input_date);
    PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','ACC_RECK_INV',v_debug_string);
       
    v_a_account         := GET_ACCOUNT('AFFILIANT_MB_ACCOUNT');
    v_vat_account       := GET_ACCOUNT('VAT_ACCOUNT');
    v_aff_inc_account   := GET_ACCOUNT('AFF_MB_INCOME_ACC'); 
    v_aff_inc_account_ev:= GET_ACCOUNT('AFF_EV_INCOME_ACC');
       
    FOR cinv IN get_IAC_invoices LOOP
        t$invoice.set_status (cinv.inv_id, 'E');     
        
        BEGIN
          SELECT DISTINCT source
          INTO v_reck_id
          FROM APP_FIN.INVOICE_LINES
          WHERE inv_id = cinv.inv_id;
        EXCEPTION
          WHEN OTHERS THEN
             -- AKTUALNIE FAKTURY dla ROZLICZEÑ KART i EVOUCHEROW powinny dotyczyæ tylko jednego rozliczenia tym samym na liniach w SOURCE powinna byæ tylko jedna unikalna wartoœæ. 
             PK_ERROR.RAISE_ERROR(-20621, 'B³¹d wyznaczania Ÿród³a faktury IAC o id: ' || cinv.inv_id);
        END; 

        -- pobierz datê sprzeda¿y faktury IAC
        v_sale_date := PK_MOD_RECKONINGS.Get_Sale_Date_For_Invoice(cinv.invoice_number);
        
        -- TODOMGU poni¿sze wyznaczanie nale¿y w razie mo¿liwoœci przenieœæ do typu T$RECKONING - to samo dotyczy metody ACC_RECK_EV 
        -- nie lockujemy rozliczenia bo nie bêdzie na nim robionych zmian w tym fragmencie kodu.
        v_o$reckoning := t$reckoning(v_reck_id, 'Y');
        IF v_o$reckoning.type = PK_MOD_RECKONINGS.GC_E_VOUCHER_RECKONING_TYPE THEN
          v_payment_type := 'PD';
          v_payment_date := cinv.invoice_date + EAGLE.T$RECKONING_EV_HELPER.Get_Ev_Payment_Delay(v_o$reckoning);
        ELSE
          IF v_o$reckoning.invoice_payment_mean = EAGLE.PK_MOD_RECKONINGS.gc_payment_mean_direct_debit THEN
            v_payment_type := 'PD';
          ELSE
            v_payment_type := '00';
          END IF;
          v_payment_date := cinv.pay_date;
        END IF;          
        
        ADD_NAV_INV( A_NUM_SEQUENCE   => GET_INV_SEQUENCE(cinv.inv_type),
                     A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.inv_type), -- ??? MGU - te¿ mnie to ciekawi, zostawiam jak by³o
                     A_DOC_TYPE       => GET_DOC_TYPE(cinv.inv_type), 
                     A_PAYMENT_TYPE   => v_payment_type,
                     A_ACCOUNT        => v_a_account.acc_no,
                     A_MPK            => NULL,
                     A_TAX            => NULL, 
                     A_PRODUCT        => NULL,
                     A_BOOK_DATE      => cinv.invoice_date,
                     A_AMOUNT         => cinv.pay_fv,
                     A_CODE           => cinv.comp_code,
                     A_DOC_NUMBER     => cinv.invoice_number,
                     A_VAT_RATE       => NULL,
                     A_ORD_ID         => NULL,
                     A_PROFORMA_NO    => NULL,
                     A_INVOICE_DATE   => cinv.invoice_date,
                     A_PAYMENT_DATE   => v_payment_date, 
                     A_PROJECT        => v_a_account.project, 
                     A_VAT_DATE       => cinv.vat_date, 
                     A_SALE_DATE      => v_sale_date,                                                           
                     A_COR_REASON_NAV_CODE => NULL);


        ADD_NAV_INV( A_NUM_SEQUENCE   => GET_INV_SEQUENCE(cinv.inv_type),
                     A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.inv_type), -- ??? MGU - te¿ mnie to ciekawi, zostawiam jak by³o
                     A_DOC_TYPE       => GET_DOC_TYPE(cinv.inv_type), 
                     A_PAYMENT_TYPE   => v_payment_type,
                     A_ACCOUNT        => v_vat_account.acc_no,
                     A_MPK            => NULL,
                     A_TAX            => NULL, 
                     A_PRODUCT        => NULL,
                     A_BOOK_DATE      => cinv.invoice_date,
                     A_AMOUNT         => cinv.vat_val,
                     A_CODE           => cinv.comp_code,
                     A_DOC_NUMBER     => cinv.invoice_number,
                     A_VAT_RATE       => NULL,
                     A_ORD_ID         => NULL,
                     A_PROFORMA_NO    => NULL,
                     A_INVOICE_DATE   => cinv.invoice_date,
                     A_PAYMENT_DATE   => v_payment_date,
                     A_PROJECT        => v_vat_account.project, 
                     A_VAT_DATE       => cinv.vat_date, 
                     A_SALE_DATE      => v_sale_date,                                                           
                     A_COR_REASON_NAV_CODE => NULL);
         
        FOR cinv_in IN get_IAC_inv_products (cinv.inv_id) LOOP 
        
            IF   NVL(cinv_in.UNIT,'A') != '%'
            OR   NVL(cinv_in.UNIT_PRINTED,'A') != 'SZT'
            OR   NVL(cinv_in.QUANTITY_PRINTED,2) != 1 THEN
              PK_ERROR.RAISE_ERROR(-20621, 'B³¹d ustawieñ sposoby wyliczania dla faktury o id: ' || cinv.inv_id
                                           ||'. Parametry: '||cinv_in.UNIT
                                           ||','||cinv_in.UNIT_PRINTED
                                           ||','||cinv_in.QUANTITY_PRINTED);
            END IF;
        
            -- MGU, konto przychodów dla Akc jest inne dla eVoucherow, pozosta³e faktury IAC na dzieñ 20140217 maj¹ to samo konto przychodów.
            IF cinv_in.code = 'CC_FP_EV' THEN  
              a_effective_inc_account := v_aff_inc_account_ev.acc_no;
            ELSE
              a_effective_inc_account := v_aff_inc_account.acc_no;
            END IF;               
            v_product := EAGLE.T$RECKONING.Get_Nav_Prd_Code(cinv_in.code);
            v_net_amount := cinv_in.net_fv;

            IF v_product IS NOT NULL THEN
                ADD_NAV_INV( A_NUM_SEQUENCE   => GET_INV_SEQUENCE(cinv.inv_type),
                         A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.inv_type), -- ??? MGU - te¿ mnie to ciekawi, zostawiam jak by³o
                         A_DOC_TYPE       => GET_DOC_TYPE(cinv.inv_type), 
                         A_PAYMENT_TYPE   => v_payment_type,
                         A_ACCOUNT        => a_effective_inc_account,
                         A_MPK            => NULL,
                         A_TAX            => 'PDO', 
                         A_PRODUCT        => v_product,
                         A_BOOK_DATE      => cinv.invoice_date,
                         A_AMOUNT         => v_net_amount,
                         A_CODE           => cinv.comp_code,
                         A_DOC_NUMBER     => cinv.invoice_number,
                         A_VAT_RATE       => GET_VAT_RATE(cinv_in.vat, cinv.inv_type),
                         A_ORD_ID         => NULL,
                         A_PROFORMA_NO    => NULL,
                         A_INVOICE_DATE   => cinv.invoice_date,
                         A_PAYMENT_DATE   => v_payment_date,
                         A_PROJECT        => v_aff_inc_account.project,
                         A_TGKV_CODE      => get_tgkv_code_sell(cinv_in.vat,'N'), 
                         A_VAT_DATE       => cinv.vat_date, 
                         A_SALE_DATE      => v_sale_date,
                         A_INVOICE_LINE_NAME   => cinv_in.NAME,
                         A_INVOICE_LINE_NAME_2 => cinv_in.NAME_2,                                                                              
                         A_UNIT_PRICE          => abs(v_net_amount) , -- UWAGA: PRZY DODANIU KOREKTY OBS£U¯YÆ A_UNIT_PRICE, A_QUANTITY (powinno byæ ustalane z procedury GET_EXP_NAV_UP_AND_Q)
                         A_QUANTITY            => cinv_in.QUANTITY_PRINTED ,   -- Zawsze 1 (walidacja wy¿ej)                                                                       
                         A_UNIT_NAV_CODE       => GET_UNIT_NAV_CODE(cinv.invoice_number, cinv_in.UNIT_PRINTED ),                                                           
                         A_COR_REASON_NAV_CODE => NULL
                         );
            ELSE
                --dodajemy dodatkowe wpisy dla z³o¿onych produktów
                SELECT COUNT(*) 
                INTO v_prd_type_count 
                FROM (
                    SELECT DISTINCT P.TYPE FROM APP_CRD.ACC_COM_CRD_GR_PRD_ID A
                    JOIN APP_CRD.ACC_COMMISION_CRD_GROUP G ON G.ID = A.COM_CRD_GROUP_ID
                    JOIN APP_STC.PRODUCTS P ON P.ID = A.PRD_ID
                    -- 20161006 MAPO (START)
                    join APP_RMB.RECKONING_CC_LINES rcl on rcl.reck_id = v_reck_id and P.ID = RCL.PRD_ID  
                    -- 20161006 MAPO (FINISH)
                    WHERE G.INV_LINE_CODE = cinv_in.code
                );
                v_prd_type_counter := 0;
                v_net_amount_sum := 0;
                FOR product_types IN 
                    (SELECT DISTINCT P.TYPE FROM APP_CRD.ACC_COM_CRD_GR_PRD_ID A
                    JOIN APP_CRD.ACC_COMMISION_CRD_GROUP G ON G.ID = A.COM_CRD_GROUP_ID
                    JOIN APP_STC.PRODUCTS P ON P.ID = A.PRD_ID
                    -- 20161006 MAPO (START)
                    join APP_RMB.RECKONING_CC_LINES rcl on rcl.reck_id = v_reck_id and P.ID = RCL.PRD_ID  
                    -- 20161006 MAPO (FINISH)
                    WHERE G.INV_LINE_CODE = cinv_in.code)
                LOOP
                    v_prd_type_counter := v_prd_type_counter + 1;
                    v_product := product_types.TYPE;
                    IF v_prd_type_counter = v_prd_type_count THEN
                        v_net_amount_part := v_net_amount - v_net_amount_sum;
                    ELSE
                        v_net_amount_part := (-1) * EAGLE.T$RECKONING_CC_HELPER.Get_Net_Amount_For_Product(v_o$reckoning, v_product);
                        v_net_amount_sum := v_net_amount_sum + v_net_amount_part;
                    END IF;
                    ADD_NAV_INV( A_NUM_SEQUENCE   => GET_INV_SEQUENCE(cinv.inv_type),
                               A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.inv_type),
                               A_DOC_TYPE       => GET_DOC_TYPE(cinv.inv_type), 
                               A_PAYMENT_TYPE   => v_payment_type,
                               A_ACCOUNT        => a_effective_inc_account,
                               A_MPK            => NULL,
                               A_TAX            => 'PDO', 
                               A_PRODUCT        => v_product,
                               A_BOOK_DATE      => cinv.invoice_date,
                               A_AMOUNT         => v_net_amount_part,
                               A_CODE           => cinv.comp_code,
                               A_DOC_NUMBER     => cinv.invoice_number,
                               A_VAT_RATE       => GET_VAT_RATE(cinv_in.vat, cinv.inv_type),
                               A_ORD_ID         => NULL,
                               A_PROFORMA_NO    => NULL,
                               A_INVOICE_DATE   => cinv.invoice_date,
                               A_PAYMENT_DATE   => v_payment_date,
                               A_PROJECT        => v_aff_inc_account.project,
                               A_TGKV_CODE      => get_tgkv_code_sell(cinv_in.vat,'N'), 
                               A_VAT_DATE       => cinv.vat_date, 
                               A_SALE_DATE      => v_sale_date,
                               A_INVOICE_LINE_NAME   => cinv_in.NAME,
                               A_INVOICE_LINE_NAME_2 => cinv_in.NAME_2,                                                                              
                               A_UNIT_PRICE          => abs(v_net_amount_part),-- UWAGA: PRZY DODANIU KOREKTY OBS£U¯YÆ A_UNIT_PRICE, A_QUANTITY (powinno byæ ustalane z procedury GET_EXP_NAV_UP_AND_Q)
                               A_QUANTITY            => cinv_in.QUANTITY_PRINTED ,  -- Zawsze 1 (walidacja wy¿ej)                                                                        
                               A_UNIT_NAV_CODE       => GET_UNIT_NAV_CODE(cinv.invoice_number, cinv_in.UNIT_PRINTED ),                                                           
                               A_COR_REASON_NAV_CODE => NULL
                               );
                END LOOP;
            END IF;
        END LOOP;
        v_o$reckoning := NULL;
    END LOOP;   
EXCEPTION
    WHEN OTHERS THEN
        PK_ERROR.RAISE_ERROR(sqlcode,v_debug_string||sqlerrm,'SWP_NAV_ARI');
END Acc_Reck_Inv;

PROCEDURE aff_reimb (
  a_input_date      IN       DATE,
  a_doc_type        IN       VARCHAR2)
IS
   v_a_account        r_acc;
   v_meal_account     r_acc;
   v_prd_turnover_account_name app_fin.scala_accounts.acc_name%TYPE;
   
   v_debug_string   eagle.adm_audit_modules.arg_string%TYPE;
   v_inv_type       VARCHAR2(4);
BEGIN

   v_debug_string := 'doc='||a_doc_type||' date='||to_char(a_input_date);
   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','AFF_REIMB',v_debug_string);
   
   v_a_account   := GET_ACCOUNT('AFFILIANT_ACCOUNT');
   
   IF a_doc_type = 'R' THEN
      v_prd_turnover_account_name := 'AFF_PRD_TURN_ACCOUNT';
      v_inv_type                  := 'REM';
   ELSIF a_doc_type = 'S' THEN
      v_prd_turnover_account_name := 'AFF_PRD_TURN_ACCOUNT_COR';
      v_inv_type                  := 'CREM';      
   ELSE
      PK_ERROR.RAISE_ERROR(-20009, 'Nieznany parametr doc_type='||a_doc_type);
   END IF;   
   
   FOR crem IN (SELECT   1 stype, com.code comp_code,
                  to_char(rl.rem_id) invoice_num,
                  SUM (rl.fv_reimb_ident) + SUM (rl.fv_reimb_error) pay_fv,
                  i.invoice_date pay_date,
                  r.reimb_date,
                  i.ID inv_id
             FROM app_rmb.market_points com,
                  app_rmb.remittance_lines rl,
                  app_rmb.remittances r,
                  app_fin.invoices i,
                  app_stc.products prd,
                  (SELECT DISTINCT invl.inv_id, invl.SOURCE rem_id
                              FROM app_fin.invoices inv,
                                   app_fin.invoice_lines invl
                             WHERE inv.ID = invl.inv_id
                               AND (inv.TYPE = 'IA' AND a_doc_type = 'R')
                               AND TRUNC (inv.invoice_date) = a_input_date) ri
            WHERE i.ID = ri.inv_id
              AND ri.rem_id = r.ID
              AND com.ID = i.mkp_id
              AND rl.prd_id = prd.ID
              AND r.ID = rl.rem_id
              AND ((i.TYPE = 'IA' AND a_doc_type = 'R'))
              AND TRUNC (i.invoice_date) = a_input_date
              AND (   rl.fv_reimb_ident IS NOT NULL
                   OR rl.fv_reimb_error IS NOT NULL
                  )
              AND (rl.fv_reimb_ident != 0 OR rl.fv_reimb_error != 0)
         GROUP BY com.code,
                  to_char(rl.rem_id),
                  i.invoice_date,
                  r.reimb_date,
                  i.ID
         UNION
         SELECT   2 stype, com.code comp_code,
                  il.SOURCE || 'K0' invoice_num,
                  SUM (prd.VALUE) pay_fv, 
                  i.invoice_date pay_date,
                  r.reimb_date,
                  i.ID inv_id
             FROM app_rmb.market_points com,
                  app_rmb.remittances r,
                  app_fin.invoices i,
                  app_fin.invoice_lines il,
                  (SELECT rowid#, remark, TO_DATE (exp_date, 'YYYY-MM-DD') exp_date,
                          TO_NUMBER (VALUE) VALUE
                     FROM (SELECT ROWIDTOCHAR (invl.ROWID) rowid#,
                                  invl.remark,
                                  SUBSTR (invl.remark, 1, 10) exp_date,
                                  SUBSTR (invl.remark,
                                          INSTR (invl.remark, ':', 1, 1) + 1,
                                            INSTR (invl.remark, '#', 1, 1)
                                          - INSTR (invl.remark, ':', 1, 1)
                                          - 1
                                         ) VALUE
                             FROM app_fin.invoice_lines invl,
                                  app_fin.invoices inv
                            WHERE pos_type = 'D'
                              AND invl.remark IS NOT NULL
                              AND INSTR (invl.remark, '#', 1, 1) <> 0
                              AND invl.inv_id = inv.ID
                              AND inv.TYPE = 'CIA'
                              AND TRUNC (inv.invoice_date) = a_input_date
                           UNION ALL
                           SELECT ROWIDTOCHAR (invl.ROWID) rowid#,
                                  invl.remark,
                                  SUBSTR (invl.remark,
                                          INSTR (invl.remark, '#', 1, 1) + 1,
                                          10
                                         ) exp_date,
                                  SUBSTR (invl.remark,
                                          INSTR (invl.remark, ':', 1, 2) + 1,
                                            INSTR (invl.remark, '#', 1, 2)
                                          - INSTR (invl.remark, ':', 1, 2)
                                          - 1
                                         ) VALUE
                             FROM app_fin.invoice_lines invl,
                                  app_fin.invoices inv
                            WHERE pos_type = 'D'
                              AND invl.remark IS NOT NULL
                              AND INSTR (invl.remark, '#', 1, 2) <> 0
                              AND invl.inv_id = inv.ID
                              AND inv.TYPE = 'CIA'
                              AND TRUNC (inv.invoice_date) = a_input_date
                           UNION ALL
                           SELECT ROWIDTOCHAR (invl.ROWID) rowid#,
                                  invl.remark,
                                  SUBSTR (invl.remark,
                                          INSTR (invl.remark, '#', 1, 2) + 1,
                                          10
                                         ) exp_date,
                                  SUBSTR (invl.remark,
                                          INSTR (invl.remark, ':', 1, 3) + 1,
                                            INSTR (invl.remark, '#', 1, 3)
                                          - INSTR (invl.remark, ':', 1, 3)
                                          - 1
                                         ) VALUE
                             FROM app_fin.invoice_lines invl,
                                  app_fin.invoices inv
                            WHERE pos_type = 'D'
                              AND invl.remark IS NOT NULL
                              AND INSTR (invl.remark, '#', 1, 3) <> 0
                              AND invl.inv_id = inv.ID
                              AND inv.TYPE = 'CIA'
                              AND TRUNC (inv.invoice_date) = a_input_date
                           UNION ALL
                           SELECT ROWIDTOCHAR (invl.ROWID) rowid#,
                                  invl.remark,
                                  SUBSTR (invl.remark,
                                          INSTR (invl.remark, '#', 1, 3) + 1,
                                          10
                                         ) exp_date,
                                  SUBSTR (invl.remark,
                                          INSTR (invl.remark, ':', 1, 4) + 1,
                                            INSTR (invl.remark, '#', 1, 4)
                                          - INSTR (invl.remark, ':', 1, 4)
                                          - 1
                                         ) VALUE
                             FROM app_fin.invoice_lines invl,
                                  app_fin.invoices inv
                            WHERE pos_type = 'D'
                              AND invl.remark IS NOT NULL
                              AND INSTR (invl.remark, '#', 1, 4) <> 0
                              AND invl.inv_id = inv.ID
                              AND inv.TYPE = 'CIA'
                              AND TRUNC (inv.invoice_date) = a_input_date
                           UNION ALL
                           SELECT ROWIDTOCHAR (invl.ROWID) rowid#,
                                  invl.remark,
                                  SUBSTR (invl.remark,
                                          INSTR (invl.remark, '#', 1, 4) + 1,
                                          10
                                         ) exp_date,
                                  SUBSTR (invl.remark,
                                          INSTR (invl.remark, ':', 1, 5) + 1,
                                            INSTR (invl.remark, '#', 1, 5)
                                          - INSTR (invl.remark, ':', 1, 5)
                                          - 1
                                         ) VALUE
                             FROM app_fin.invoice_lines invl,
                                  app_fin.invoices inv
                            WHERE pos_type = 'D'
                              AND invl.remark IS NOT NULL
                              AND INSTR (invl.remark, '#', 1, 5) <> 0
                              AND invl.inv_id = inv.ID
                              AND inv.TYPE = 'CIA'
                              AND TRUNC (inv.invoice_date) = a_input_date)) prd
            WHERE i.ID = il.inv_id
              AND il.SOURCE = r.ID
              AND com.ID = i.mkp_id
              AND ROWIDTOCHAR (il.ROWID) = prd.rowid#
              AND i.TYPE = 'CIA'
              AND a_doc_type = 'S'
              AND il.remark IS NOT NULL
              AND il.pos_type = 'D'
              AND TRUNC (i.invoice_date) = a_input_date
         GROUP BY com.code,
                  il.SOURCE || 'K0',
                  i.invoice_date,
                  r.reimb_date,
                  i.ID
         ORDER BY stype, invoice_num)
      LOOP
      
          ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(v_inv_type),
                          A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(v_inv_type), -- ???
                          A_DOC_TYPE       => GET_DOC_TYPE(v_inv_type), 
                          A_PAYMENT_TYPE   => 'PD',
                          A_ACCOUNT        => v_a_account.acc_no,
                          A_MPK            => NULL,
                          A_TAX            => NULL, 
                          A_PRODUCT        => NULL,
                          A_BOOK_DATE      => crem.pay_date,
                          A_AMOUNT         => nvl(crem.pay_fv,0) * (-1),
                          A_CODE           => crem.comp_code,
                          A_DOC_NUMBER     => crem.invoice_num,
                          A_VAT_RATE       => 'NP',
                          A_ORD_ID         => NULL,
                          A_PROFORMA_NO    => NULL,
                          A_INVOICE_DATE   => crem.pay_date,
                          A_PAYMENT_DATE   => crem.reimb_date,
                          A_PROJECT        => v_a_account.project,
                          A_TGKV_CODE			=> 'NP U');
      
           FOR crem_in IN (SELECT   1 stype, com.code comp_code,
                          to_char(rl.rem_id) invoice_num,
                          SUM (rl.fv_reimb_ident) + SUM (rl.fv_reimb_error) pay_fv,
                          i.invoice_date pay_date,
                          prd.TYPE prd_type, prd.expired_date prd_exp_date,
                          i.ID inv_id
                     FROM app_rmb.market_points com,
                          app_rmb.remittance_lines rl,
                          app_rmb.remittances r,
                          app_fin.invoices i,
                          app_stc.products prd,
                          (SELECT DISTINCT invl.inv_id, invl.SOURCE rem_id
                                      FROM app_fin.invoices inv,
                                           app_fin.invoice_lines invl
                                     WHERE inv.ID = invl.inv_id
                                       AND (inv.TYPE = 'IA' AND a_doc_type = 'R')
                                       AND TRUNC (inv.invoice_date) = a_input_date) ri
                    WHERE i.ID = ri.inv_id
                      AND ri.rem_id = r.ID
                      AND com.ID = i.mkp_id
                      AND rl.prd_id = prd.ID
                      AND r.ID = rl.rem_id
                      AND ((i.TYPE = 'IA' AND a_doc_type = 'R'))
                      AND TRUNC (i.invoice_date) = a_input_date
                      AND (   rl.fv_reimb_ident IS NOT NULL
                           OR rl.fv_reimb_error IS NOT NULL
                          )
                      AND (rl.fv_reimb_ident != 0 OR rl.fv_reimb_error != 0)
                      AND to_char(r.ID) = crem.invoice_num 
                 GROUP BY com.code,
                          rl.rem_id,
                          i.invoice_date,
                          prd.TYPE,
                          prd.expired_date,
                          i.ID
                 UNION
                 SELECT   2 stype, com.code comp_code,
                          il.SOURCE || 'K0' invoice_num,
                          SUM (prd.VALUE) pay_fv, 
                          i.invoice_date pay_date,
                          il.code prd_type, prd.exp_date prd_exp_date,
                          i.ID inv_id
                     FROM app_rmb.market_points com,
                          app_rmb.remittances r,
                          app_fin.invoices i,
                          app_fin.invoice_lines il,
                          (SELECT rowid#, remark, TO_DATE (exp_date, 'YYYY-MM-DD') exp_date,
                                  TO_NUMBER (VALUE) VALUE
                             FROM (SELECT ROWIDTOCHAR (invl.ROWID) rowid#,
                                          invl.remark,
                                          SUBSTR (invl.remark, 1, 10) exp_date,
                                          SUBSTR (invl.remark,
                                                  INSTR (invl.remark, ':', 1, 1) + 1,
                                                    INSTR (invl.remark, '#', 1, 1)
                                                  - INSTR (invl.remark, ':', 1, 1)
                                                  - 1
                                                 ) VALUE
                                     FROM app_fin.invoice_lines invl,
                                          app_fin.invoices inv
                                    WHERE pos_type = 'D'
                                      AND invl.remark IS NOT NULL
                                      AND INSTR (invl.remark, '#', 1, 1) <> 0
                                      AND invl.inv_id = inv.ID
                                      AND inv.TYPE = 'CIA'
                                      AND TRUNC (inv.invoice_date) = a_input_date
                                   UNION ALL
                                   SELECT ROWIDTOCHAR (invl.ROWID) rowid#,
                                          invl.remark,
                                          SUBSTR (invl.remark,
                                                  INSTR (invl.remark, '#', 1, 1) + 1,
                                                  10
                                                 ) exp_date,
                                          SUBSTR (invl.remark,
                                                  INSTR (invl.remark, ':', 1, 2) + 1,
                                                    INSTR (invl.remark, '#', 1, 2)
                                                  - INSTR (invl.remark, ':', 1, 2)
                                                  - 1
                                                 ) VALUE
                                     FROM app_fin.invoice_lines invl,
                                          app_fin.invoices inv
                                    WHERE pos_type = 'D'
                                      AND invl.remark IS NOT NULL
                                      AND INSTR (invl.remark, '#', 1, 2) <> 0
                                      AND invl.inv_id = inv.ID
                                      AND inv.TYPE = 'CIA'
                                      AND TRUNC (inv.invoice_date) = a_input_date
                                   UNION ALL
                                   SELECT ROWIDTOCHAR (invl.ROWID) rowid#,
                                          invl.remark,
                                          SUBSTR (invl.remark,
                                                  INSTR (invl.remark, '#', 1, 2) + 1,
                                                  10
                                                 ) exp_date,
                                          SUBSTR (invl.remark,
                                                  INSTR (invl.remark, ':', 1, 3) + 1,
                                                    INSTR (invl.remark, '#', 1, 3)
                                                  - INSTR (invl.remark, ':', 1, 3)
                                                  - 1
                                                 ) VALUE
                                     FROM app_fin.invoice_lines invl,
                                          app_fin.invoices inv
                                    WHERE pos_type = 'D'
                                      AND invl.remark IS NOT NULL
                                      AND INSTR (invl.remark, '#', 1, 3) <> 0
                                      AND invl.inv_id = inv.ID
                                      AND inv.TYPE = 'CIA'
                                      AND TRUNC (inv.invoice_date) = a_input_date
                                   UNION ALL
                                   SELECT ROWIDTOCHAR (invl.ROWID) rowid#,
                                          invl.remark,
                                          SUBSTR (invl.remark,
                                                  INSTR (invl.remark, '#', 1, 3) + 1,
                                                  10
                                                 ) exp_date,
                                          SUBSTR (invl.remark,
                                                  INSTR (invl.remark, ':', 1, 4) + 1,
                                                    INSTR (invl.remark, '#', 1, 4)
                                                  - INSTR (invl.remark, ':', 1, 4)
                                                  - 1
                                                 ) VALUE
                                     FROM app_fin.invoice_lines invl,
                                          app_fin.invoices inv
                                    WHERE pos_type = 'D'
                                      AND invl.remark IS NOT NULL
                                      AND INSTR (invl.remark, '#', 1, 4) <> 0
                                      AND invl.inv_id = inv.ID
                                      AND inv.TYPE = 'CIA'
                                      AND TRUNC (inv.invoice_date) = a_input_date
                                   UNION ALL
                                   SELECT ROWIDTOCHAR (invl.ROWID) rowid#,
                                          invl.remark,
                                          SUBSTR (invl.remark,
                                                  INSTR (invl.remark, '#', 1, 4) + 1,
                                                  10
                                                 ) exp_date,
                                          SUBSTR (invl.remark,
                                                  INSTR (invl.remark, ':', 1, 5) + 1,
                                                    INSTR (invl.remark, '#', 1, 5)
                                                  - INSTR (invl.remark, ':', 1, 5)
                                                  - 1
                                                 ) VALUE
                                     FROM app_fin.invoice_lines invl,
                                          app_fin.invoices inv
                                    WHERE pos_type = 'D'
                                      AND invl.remark IS NOT NULL
                                      AND INSTR (invl.remark, '#', 1, 5) <> 0
                                      AND invl.inv_id = inv.ID
                                      AND inv.TYPE = 'CIA'
                                      AND TRUNC (inv.invoice_date) = a_input_date)) prd
                    WHERE i.ID = il.inv_id
                      AND il.SOURCE = r.ID
                      AND com.ID = i.mkp_id
                      AND ROWIDTOCHAR (il.ROWID) = prd.rowid#
                      AND i.TYPE = 'CIA'
                      AND a_doc_type = 'S'
                      AND il.remark IS NOT NULL
                      AND il.pos_type = 'D'
                      AND TRUNC (i.invoice_date) = a_input_date
                      AND il.SOURCE || 'K0' = crem.invoice_num
                 GROUP BY com.code,
                          il.SOURCE || 'K0',
                          i.invoice_date,
                          il.code,
                          prd.exp_date,
                          i.ID
                 ORDER BY stype, invoice_num, prd_type, prd_exp_date)
            LOOP

                v_meal_account :=
                                  get_prd_account (v_prd_turnover_account_name,
                                                   crem_in.prd_type,
                                                   crem_in.prd_exp_date);

                ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(v_inv_type),
                                          A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(v_inv_type), -- ???
                                          A_DOC_TYPE       => GET_DOC_TYPE(v_inv_type),
                                          A_PAYMENT_TYPE   => 'PD',
                                          A_ACCOUNT        => v_meal_account.acc_no,
                                          A_MPK            => NULL,
                                          A_TAX            => NULL, 
                                          A_PRODUCT        => crem_in.PRD_TYPE,
                                          A_BOOK_DATE      => crem.pay_date,
                                          A_AMOUNT         => nvl(crem_in.pay_fv,0),
                                          A_CODE           => crem.comp_code,
                                          A_DOC_NUMBER     => crem.invoice_num,
                                          A_VAT_RATE       => 'NP',
                                          A_ORD_ID         => NULL,
                                          A_PROFORMA_NO    => NULL,
                                          A_INVOICE_DATE   => crem.pay_date,
                                          A_PAYMENT_DATE   => crem.reimb_date,
                                          A_PROJECT        => v_meal_account.project,
                                          A_TGKV_CODE			=> 'NP U');
            
            END LOOP;

      END LOOP;


EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,v_debug_string||sqlerrm,'SWP_NAV_ARM');
END;

-- rozliczenia eVoucherów
PROCEDURE Acc_Reck_Ev (
  a_input_date      IN       DATE)
IS
   v_a_account        r_acc;
   v_ev_prd_account   r_acc;
   v_inv_type         VARCHAR2(4);
   v_o$reckoning      t$reckoning;  
   v_pay_delay        NUMBER;
   v_prd_type         APP_SWP.NAV_INV.PRODUCT%TYPE;           
BEGIN
   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','Acc_Reck_Ev', a_input_date);
   v_a_account      := GET_ACCOUNT('AFFILIANT_MB_ACCOUNT'); 
   v_ev_prd_account := GET_ACCOUNT('AFF_EV_PRD_ACCOUNT');
   v_inv_type       := 'RECK';
   FOR creck IN (SELECT NVL(i.invoice_date, r.create_date) invoice_date, 
                        r.reckoning_base_value, 
                        m.code, 
                        r.id 
                 FROM  APP_RMB.RECKONINGS r
                 JOIN  APP_RMB.MARKET_POINTS m ON m.id = r.mkp_id 
                 LEFT JOIN APP_RMB.RECKONING_INVOICES ri ON r.id = ri.reck_id
                 LEFT JOIN APP_FIN.INVOICES i ON i.id = ri.invoice_id
                   WHERE r.type = EAGLE.PK_MOD_RECKONINGS.gc_e_voucher_reckoning_type
                    AND trunc(NVL(i.invoice_date, r.create_date)) = trunc(a_input_date) 
                    AND r.status IN (EAGLE.PK_MOD_RECKONINGS.gc_status_invoiced, EAGLE.PK_MOD_RECKONINGS.gc_status_to_reimb)     
                    ORDER BY r.id)
      LOOP
        v_o$reckoning := t$reckoning(creck.id);
        v_o$reckoning.Process_Reckoning(EAGLE.PK_MOD_RECKONINGS.gc_action_export);
        v_pay_delay := EAGLE.T$RECKONING_EV_HELPER.Get_Ev_Payment_Delay(v_o$reckoning);
        v_prd_type  := EAGLE.T$RECKONING_EV_HELPER.Get_Ev_Reck_Nav_Prd_Code(v_o$reckoning);  
      
        ADD_NAV_INV(A_NUM_SEQUENCE   => GET_INV_SEQUENCE(v_inv_type),
                    A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(v_inv_type), -- ???
                    A_DOC_TYPE       => GET_DOC_TYPE(v_inv_type), 
                    A_PAYMENT_TYPE   => 'PD',
                    A_ACCOUNT        => v_a_account.acc_no,
                    A_MPK            => NULL,
                    A_TAX            => NULL, 
                    A_PRODUCT        => NULL,
                    A_BOOK_DATE      => creck.invoice_date,
                    A_AMOUNT         => nvl(creck.reckoning_base_value,0) * (-1),
                    A_CODE           => creck.code,
                    A_DOC_NUMBER     => creck.id,
                    A_VAT_RATE       => 'NP',
                    A_ORD_ID         => NULL,
                    A_PROFORMA_NO    => NULL,
                    A_INVOICE_DATE   => creck.invoice_date,
                    A_PAYMENT_DATE   => creck.invoice_date+v_pay_delay,
                    A_PROJECT        => v_a_account.project,
                    A_TGKV_CODE      => 'NP U');
        
        -- MGU na dzieñ 20140217 jest tylko jeden rodzaj produktu i dla kazdego rozliczenia jest tylko jedna linia! 
        ADD_NAV_INV(A_NUM_SEQUENCE   => GET_INV_SEQUENCE(v_inv_type),
                    A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(v_inv_type), -- ???
                    A_DOC_TYPE       => GET_DOC_TYPE(v_inv_type),
                    A_PAYMENT_TYPE   => 'PD',
                    A_ACCOUNT        => v_ev_prd_account.acc_no,
                    A_MPK            => NULL,
                    A_TAX            => NULL, 
                    A_PRODUCT        => v_prd_type,
                    A_BOOK_DATE      => creck.invoice_date,
                    A_AMOUNT         => nvl(creck.reckoning_base_value,0),
                    A_CODE           => creck.code,
                    A_DOC_NUMBER     => creck.id,
                    A_VAT_RATE       => 'NP',
                    A_ORD_ID         => NULL,
                    A_PROFORMA_NO    => NULL,
                    A_INVOICE_DATE   => creck.invoice_date,
                    A_PAYMENT_DATE   => creck.invoice_date+v_pay_delay,
                    A_PROJECT        => v_ev_prd_account.project,
                    A_TGKV_CODE      => 'NP U');   
        v_o$reckoning.serialize;
        v_o$reckoning := NULL;      
      END LOOP;
EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_ARE');
END Acc_Reck_Ev;



PROCEDURE tnt_reinv (
  a_input_date      IN       DATE,
  a_doc_type        IN       VARCHAR2)
IS
   v_prd_type       app_stc.product_types.TYPE%TYPE;

   v_a_account      r_acc;
   v_vat_account    r_acc;
   v_tnt_account    r_acc;
   v_tnt_inc_account r_acc;
   v_tnt_inc_account_vb r_acc;

   v_debug_string   eagle.adm_audit_modules.arg_string%TYPE;
BEGIN

   v_debug_string := 'doc='||a_doc_type||' date='||to_char(a_input_date);
   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','TNT_REINV',v_debug_string);

   v_a_account      := GET_ACCOUNT('AFFILIANT_ACCOUNT');
   v_vat_account    := GET_ACCOUNT('VAT_ACCOUNT');

   IF a_doc_type = 'T' THEN
      v_tnt_inc_account     := GET_ACCOUNT('TNT_INCOME_ACCOUNT');
      v_tnt_inc_account_vb  := GET_ACCOUNT('TNT_INCOME_ACCOUNT_VB');
   ELSIF a_doc_type = 'U' THEN
     v_tnt_inc_account      := GET_ACCOUNT('TNT_INCOME_ACCOUNT_C');
     v_tnt_inc_account_vb   := GET_ACCOUNT('TNT_INCOME_ACCOUNT_C_VB');
   ELSE
      PK_ERROR.RAISE_ERROR(-20010, 'Nieznany parametr doc_type='||a_doc_type);
   END IF;

   FOR cinv IN (SELECT   com.code comp_code,
                  i.invoice_number,
                  SUM (il.gross_amount) pay_fv, SUM (il.vat_amount) vat,
                  i.invoice_date,
                  i.payment_date,
                  decode(i.scala_pay_type,'0','00', i.scala_pay_type) pay_mode,
                  i.ID inv_id,
                  i.TYPE INV_TYPE, 
                  i.vat_date,
                  CR.COR_REASON_NAV_CODE
             FROM app_fin.invoices i,
                  app_fin.invoice_lines il,
                  app_rmb.market_points com,
                  APP_FIN.COR_REASON cr
            WHERE i.ID = il.inv_id
              AND com.ID = i.mkp_id
              AND CR.ID (+) = I.COR_REASON
              AND i.gross_amount != 0
              AND il.gross_amount != 0
              AND ((i.TYPE = 'T' AND a_doc_type = 'T')
                  OR (i.TYPE = 'CT' AND il.pos_type = 'D' AND a_doc_type = 'U'))
              AND TRUNC (i.invoice_date) = a_input_date
              AND i.status = 'P'
         GROUP BY com.code,
                  i.invoice_number,
                  i.invoice_date,
                  i.payment_date,
                  i.scala_pay_type,
                  i.ID,
                  i.TYPE,
                  i.vat_date,
                  CR.COR_REASON_NAV_CODE
         ORDER BY i.ID)
      LOOP
          
         t$invoice.set_status (cinv.inv_id, 'E');
         
         IF cinv.INV_TYPE in ('CT') THEN
           -- ze wzglêdu na obs³ugê A_UNIT_PRICE A_QUANTITY, ostatnie takie korekty wystawione w 2012
           PK_ERROR.RAISE_ERROR(-20001, 'NAV_GAC: Nieobs³ugiwany typ dokumentu CT. Faktura o id: '||cinv.inv_id);
         END IF;

         ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(cinv.INV_TYPE),
                      A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.INV_TYPE), -- ???
                      A_DOC_TYPE       => GET_DOC_TYPE(cinv.INV_TYPE), 
                      A_PAYMENT_TYPE   => cinv.PAY_MODE,
                      A_ACCOUNT        => v_a_account.acc_no,
                      A_MPK            => NULL,
                      A_TAX            => NULL, 
                      A_PRODUCT        => NULL,
                      A_BOOK_DATE      => cinv.invoice_date,
                      A_AMOUNT         => cinv.pay_fv,
                      A_CODE           => cinv.comp_code,
                      A_DOC_NUMBER     => cinv.invoice_number,
                      A_VAT_RATE       => NULL,
                      A_ORD_ID         => NULL,
                      A_PROFORMA_NO    => NULL,
                      A_INVOICE_DATE   => cinv.invoice_date,
                      A_PAYMENT_DATE   => cinv.payment_date,
                      A_PROJECT        => v_a_account.project, 
                      A_VAT_DATE       => cinv.vat_date,
                      A_COR_REASON_NAV_CODE => cinv.COR_REASON_NAV_CODE );
                      
         ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(cinv.INV_TYPE),
                      A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.INV_TYPE), -- ???
                      A_DOC_TYPE       => GET_DOC_TYPE(cinv.INV_TYPE), 
                      A_PAYMENT_TYPE   => cinv.PAY_MODE,
                      A_ACCOUNT        => v_vat_account.acc_no,
                      A_MPK            => NULL,
                      A_TAX            => NULL, 
                      A_PRODUCT        => NULL,
                      A_BOOK_DATE      => cinv.invoice_date,
                      A_AMOUNT         => cinv.vat * (-1),
                      A_CODE           => cinv.comp_code,
                      A_DOC_NUMBER     => cinv.invoice_number,
                      A_VAT_RATE       => NULL,
                      A_ORD_ID         => NULL,
                      A_PROFORMA_NO    => NULL,
                      A_INVOICE_DATE   => cinv.invoice_date,
                      A_PAYMENT_DATE   => cinv.payment_date,
                      A_PROJECT        => v_vat_account.project, 
                      A_VAT_DATE       => cinv.vat_date,
                      A_COR_REASON_NAV_CODE => NULL);                     
         
         
         FOR cinv_in IN (SELECT SUM (il.gross_amount) pay_fv, SUM (il.vat_amount) vat,
                                CASE WHEN il.code_type = 'DV' THEN /* proteza VivaBox */
                                        to_char(to_number(il.code)+100)
                                     ELSE
                                        il.code
                                END prd,
                                i.ID inv_id,
                                il.VAT VAT_RATE,
                                il.CODE_TYPE,
                                SUBSTR('Us³uga Kurierska',1,50) NAME,
                                SUBSTR('Us³uga Kurierska',51,50) NAME_2,
                                IL.UNIT,
                                IL.UNIT_PRINTED,
                                IL.QUANTITY_PRINTED
                           FROM app_fin.invoices i,
                                app_fin.invoice_lines il
                          WHERE i.ID = il.inv_id
                            AND i.gross_amount != 0
                            AND il.gross_amount != 0
                            AND (   (i.TYPE = 'T' AND a_doc_type = 'T')
                                 OR (i.TYPE = 'CT' AND il.pos_type = 'D'
                                     AND a_doc_type = 'U'
                                    )
                                )
                            AND i.ID = cinv.inv_id
                           GROUP BY CASE WHEN il.code_type = 'DV' THEN /* proteza VivaBox */
                                            to_char(to_number(il.code)+100)
                                         ELSE
                                            il.code
                                    END,
                                    i.ID,
                                    il.VAT,
                                    il.CODE_TYPE,
                                    IL.UNIT,
                                    IL.UNIT_PRINTED,
                                    IL.QUANTITY_PRINTED
                           ORDER BY prd)
            LOOP
            
                IF   NVL(cinv_in.UNIT,'A') != 'SZT'
                OR   NVL(cinv_in.UNIT_PRINTED,'A') != 'SZT'
                OR   NVL(cinv_in.QUANTITY_PRINTED,2) != 1 THEN
                  PK_ERROR.RAISE_ERROR(-20621, 'B³¹d ustawieñ sposoby wyliczania dla faktury o id: ' || cinv.inv_id
                                               ||'. Parametry: '||cinv_in.UNIT
                                               ||','||cinv_in.UNIT_PRINTED
                                               ||','||cinv_in.QUANTITY_PRINTED);
                END IF;


                 v_tnt_account := v_tnt_inc_account;
                 IF cinv_in.code_type = 'DV' THEN
                    v_tnt_account := v_tnt_inc_account_vb;
                 END IF;            
            
                 ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(cinv.INV_TYPE),
                              A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(cinv.INV_TYPE), -- ???
                              A_DOC_TYPE       => GET_DOC_TYPE(cinv.INV_TYPE), 
                              A_PAYMENT_TYPE   => cinv.PAY_MODE,
                              A_ACCOUNT        => v_tnt_account.acc_no,
                              A_MPK            => NULL,
                              A_TAX            => 'PDO', 
                              A_PRODUCT        => cinv_in.prd,
                              A_BOOK_DATE      => cinv.invoice_date,
                              A_AMOUNT         => (cinv_in.pay_fv - cinv_in.vat) * (-1),
                              A_CODE           => cinv.comp_code,
                              A_DOC_NUMBER     => cinv.invoice_number,
                              A_VAT_RATE       => GET_VAT_RATE(cinv_in.vat_rate, cinv.inv_type),
                              A_ORD_ID         => NULL,
                              A_PROFORMA_NO    => NULL,
                              A_INVOICE_DATE   => cinv.invoice_date,
                              A_PAYMENT_DATE   => cinv.payment_date,
                              A_PROJECT        => v_tnt_account.project,
                              a_tgkv_code			 => get_tgkv_code_sell(cinv_in.vat_rate,'N'), 
                              A_VAT_DATE       => cinv.vat_date,
                              A_INVOICE_LINE_NAME => cinv_in.NAME,
                              A_INVOICE_LINE_NAME_2 => cinv_in.NAME_2,                                                                              
                              A_UNIT_PRICE          => cinv_in.pay_fv - cinv_in.vat, -- Przy dodaniu korekt: obs³u¿yæ
                              A_QUANTITY            => cinv_in.QUANTITY_PRINTED ,    -- Przy dodaniu korekt: obs³u¿yæ                                                                       
                              A_UNIT_NAV_CODE       => GET_UNIT_NAV_CODE(cinv.invoice_number, cinv_in.UNIT_PRINTED ),                                                           
                              A_COR_REASON_NAV_CODE => NULL
                              
                              
                              
                              );
               
            END LOOP;
      
      
      END LOOP;   
  
EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,v_debug_string||sqlerrm,'SWP_NAV_TRE');
END;


PROCEDURE der_einv (
  a_inv             IN       NUMBER,
  a_book_date       IN       DATE,
  a_inc_account     IN       r_acc,
  a_vat_account     IN       r_acc,
  a_nav_account     IN       VARCHAR2,
  a_comp_code       IN       VARCHAR2)
IS
   --v_ks_date        DATE := trunc(sysdate);
   v_debug_string   eagle.adm_audit_modules.arg_string%TYPE;
   v_inv_type VARCHAR2(20);
   v_nav_account VARCHAR2(20);
BEGIN

   v_debug_string := 'acc='||a_nav_account||' inv='||a_inv;
   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','DER_EINV',v_debug_string);
         
      t$der_invoice.set_status (a_inv);   
   
      FOR n IN (SELECT   di.invoice_no, TRUNC (di.invoice_date) inv_date,
                         TRUNC (di.payment_date) pay_date,
                         SUM (dis.net_amount) net_amount,
                         SUM (dis.vat_amount) vat_amount
                    FROM app_fin.der_invoices di,
                         app_fin.der_invoice_scala dis
                   WHERE di.ID = dis.inv_id AND di.ID = a_inv
                GROUP BY di.invoice_no,
                         TRUNC (di.invoice_date),
                         TRUNC (di.payment_date)
                         )
      LOOP
				
        -- dla faktur koryguj¹cyh jest inna seria numeracji (NUM_SEQUENCE)  
        IF  n.net_amount + n.vat_amount < 0 
        and a_nav_account = 'TNT_CONS' THEN
            v_nav_account := 'CTNT_CONS';
        ELSE
          v_nav_account := a_nav_account;
        END IF;

        IF n.net_amount + n.vat_amount < 0 THEN
        	v_inv_type := 'CI';
        ELSE
        	v_inv_type := a_nav_account;
        END IF;
      
         ADD_NAV_INV( A_NUM_SEQUENCE   => GET_INV_SEQUENCE(v_nav_account),
                      A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(v_nav_account), -- ???
                      A_DOC_TYPE       => GET_DOC_TYPE(v_inv_type),
                      A_PAYMENT_TYPE   => '00',
                      A_ACCOUNT        => a_inc_account.acc_no,
                      A_MPK            => NULL,
                      A_TAX            => NULL,
                      A_PRODUCT        => NULL,
                      A_BOOK_DATE      => a_book_date,
                      A_AMOUNT         => abs(n.net_amount + n.vat_amount) * (-1),
                      A_CODE           => a_comp_code,
                      A_DOC_NUMBER     => n.invoice_no,
                      A_VAT_RATE       => NULL,
                      A_ORD_ID         => NULL,
                      A_PROFORMA_NO    => NULL,
                      A_INVOICE_DATE   => n.inv_date,
                      A_PAYMENT_DATE   => n.pay_date,
                      A_PROJECT        => a_inc_account.project);

         IF n.vat_amount <> 0 THEN
         
             ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(v_nav_account),
                          A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(v_nav_account), -- ???
                          A_DOC_TYPE       => GET_DOC_TYPE(v_inv_type),
                          A_PAYMENT_TYPE   => '00',
                          A_ACCOUNT        => a_vat_account.acc_no,
                          A_MPK            => NULL,
                          A_TAX            => NULL,
                          A_PRODUCT        => NULL,
                          A_BOOK_DATE      => a_book_date,
                          A_AMOUNT         => abs(n.vat_amount),
                          A_CODE           => a_comp_code,
                          A_DOC_NUMBER     => n.invoice_no,
                          A_VAT_RATE       => NULL,
                          A_ORD_ID         => NULL,
                          A_PROFORMA_NO    => NULL,
                          A_INVOICE_DATE   => n.inv_date,
                          A_PAYMENT_DATE   => n.pay_date,
                          A_PROJECT        => a_vat_account.project);

         END IF;         

      END LOOP;

      FOR n IN (SELECT   di.invoice_no, di.invoice_date inv_date,
                         di.payment_date pay_date,
                         dis.account_no,
                         dis.mpk, dis.tax,
                         dis.service,
                         decode(dis.vat_code,'99','NP','13','0','11','22',dis.vat_code) vat_code,
                         dis.net_amount, dis.vat_amount
                         --NVL (sa.cons_code, '#') cons_code,
                         --sa.project
                    FROM app_fin.der_invoices di,
                         app_fin.der_invoice_scala dis
                         --app_fin.scala_accounts sa
                   WHERE di.ID = dis.inv_id
                     --AND sa.acc_no = dis.account_no
                     --AND sa.acc_name = a_nav_account
                     --AND NVL (sa.mpk, '#') = NVL (dis.mpk, '#')
                     --AND NVL (sa.service, '#') = NVL (dis.service, '#')
                     --AND NVL (sa.tax, '#') = NVL (dis.tax, '#')
                     AND di.ID = a_inv
                ORDER BY dis.account_no, dis.mpk, dis.tax, dis.service)
      LOOP
				
        -- dla faktur koryguj¹cyh jest inna seria numeracji (NUM_SEQUENCE)  
        IF  n.net_amount + n.vat_amount < 0 
        and a_nav_account = 'TNT_CONS' THEN
            v_nav_account := 'CTNT_CONS';
        ELSE
          v_nav_account := a_nav_account;
        END IF;
              	IF n.net_amount + n.vat_amount < 0 THEN
        	v_inv_type := 'CI';
        ELSE
        	v_inv_type := a_nav_account;
        END IF;
      
         ADD_NAV_INV( A_NUM_SEQUENCE    => GET_INV_SEQUENCE(v_nav_account),
                      A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(v_nav_account), -- ???
                      A_DOC_TYPE       => GET_DOC_TYPE(v_inv_type),
                      A_PAYMENT_TYPE   => '00',
                      A_ACCOUNT        => n.account_no,
                      A_MPK            => n.mpk,
                      A_TAX            => n.tax,
                      A_PRODUCT        => n.service,
                      A_BOOK_DATE      => a_book_date,
                      A_AMOUNT         => abs(n.net_amount),
                      A_CODE           => a_comp_code,
                      A_DOC_NUMBER     => n.invoice_no,
                      A_VAT_RATE       => n.VAT_CODE,
                      A_ORD_ID         => NULL,
                      A_PROFORMA_NO    => NULL,
                      A_INVOICE_DATE   => n.inv_date,
                      A_PAYMENT_DATE   => n.pay_date,
                      A_PROJECT        => NULL,
                      A_TGKV_CODE			 => GET_TGKV_CODE_BUY(n.vat_code, 'N', n.mpk, n.service));

      END LOOP;
  
EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,v_debug_string||sqlerrm,'SWP_NAV_TRE');
END;  

FUNCTION GET_MIN_INV_PRODUCT(a_inv_id IN NUMBER) RETURN VARCHAR2
IS
   v_retval app_fin.invoice_lines.code%TYPE;
   v_inv_type   app_fin.invoices.type%TYPE;
BEGIN

   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','GET_MIN_INV_PRODUCT',NULL);

   SELECT MIN (iln.code), inv.type
     INTO v_retval, v_inv_type
     FROM app_fin.invoice_lines iln,
          app_fin.invoices inv
    WHERE inv_id = a_inv_id
      AND inv.id = iln.inv_id
    GROUP BY inv.type;

    IF v_inv_type IN ('CIC', 'CICV') THEN

       IF v_retval IN ('C', 'DC', 'DF', 'FEE', 'DCV') THEN
          /* szukanie dokumentu dla korekty */
           SELECT MIN (iln.code)
             INTO v_retval
             FROM app_fin.invoice_lines iln,
                  app_fin.invoices inv,
                  (SELECT DISTINCT iln.source, iln.source_type
                     FROM app_fin.invoice_lines iln
                    WHERE iln.inv_id = a_inv_id) inv_in
            WHERE inv.id = iln.inv_id
              AND iln.source = inv_in.source
              AND iln.source_type = inv_in.source_type
              AND inv.type IN ('IC', 'ICV');

       END IF;

    END IF;

    RETURN v_retval;

EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_GIP');
END;

FUNCTION GET_INV_SEQUENCE(a_invoice_type IN VARCHAR2) RETURN VARCHAR2
IS
   v_retval VARCHAR2(10);
BEGIN

   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','GET_INV_SEQUENCE',NULL);

   
   SELECT decode(a_invoice_type, 'IA',  'S_FAAKCEP',
                                 'IAC', 'S_FAAKCEP',
                                 'ECI', 'S_FAKLI', -- APP_FIN.EXTERNAL_INVOICES
                                 'ECN', 'S_NOTAOBC', 
                                 'IC',  'S_FAKLI',
                                 PK_MOD_INV_PROPOSALS.GC_INV_TYPE_ICG, 'S_FAKLI',
                                 'CIA', 'S_KORAKCEP',
                                 'CIC', 'S_KORKLI',
                                 PK_MOD_INV_PROPOSALS.GC_INV_TYPE_CICG, 'S_KORKLI',
                                 'CT',  'S_KORTNT',
                                 'N',   'S_NOTAOBC',
                                 'R',   'S_NOTAOBC',
                                 PK_MOD_INV_PROPOSALS.GC_INV_TYPE_NG, 'S_NOTAOBC',
                                 PK_MOD_INV_PROPOSALS.GC_INV_TYPE_RG, 'S_NOTAOBC',
                                 'CN',  'S_NOTOBCKO',
                                 'CR',  'S_NOTOBCKO',
                                 PK_MOD_INV_PROPOSALS.GC_INV_TYPE_CNG, 'S_NOTOBCKO',
                                 PK_MOD_INV_PROPOSALS.GC_INV_TYPE_CRG, 'S_NOTOBCKO',
                                 'NU',  'S_NOTUZN',
                                 'CNU', 'S_NOTUZNKO',
                                 'T',   'S_REFTNT',
                                 'ICV', 'S_VIVABOX',
                                 'CICV','S_VIVABOXK',
                                 'REM', 'S_RMBAKC',
                                 'RECK','S_RMBAKC',
                                 'ECI_RMB','S_RMBAKC', -- sztuczne rozliczenie dla APP_FIN.EXTERNAL_INVOICES
                                 'ECN_RMB','S_RMBAKC', -- sztuczne rozliczenie dla APP_FIN.EXTERNAL_INVOICES
                                 'CREM', 'S_RMBAKCKO',
                                 'I',   'Z_AKCVBX',
                                 'CI',  'Z_AKCVBXKO',
                                 'CTNT_CONS', 'Z_TNTK',
                                 'TNT_CONS', 'Z_TNT_UPS',
                                 'UPS_CONS', 'Z_TNT_UPS',
                                 'NPB' ,'S_NOTAWUP', -- Nota obci¹¿eniowa WUP
                                 'NPB_ORD' ,'S_BVWUP', -- Sztuczne zamóienie dla APP_PBE.ORDERS
                                 'NUPB','S_NOTAUWUP', -- Nota uznaniowa WUP
                                 'NUPB_RMB','S_RMBWUP' -- Sztuczne rozliczenie dla APP_PBE.E_REIMBURSEMENTS
                                 )
    INTO v_retval
    FROM dual;

    
    IF v_retval IS NULL THEN
       pk_error.raise_error(-20002,'Nie moge znalezc definicji dokumentu: '||a_invoice_type);
    END IF;
   
   RETURN v_retval;

EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_GIS');
END;

FUNCTION GET_DOC_TYPE(a_invoice_type IN VARCHAR2) RETURN VARCHAR2
IS
   v_retval VARCHAR2(10);
BEGIN

   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','GET_DOC_TYPE',NULL);
   
   SELECT decode(a_invoice_type, 'CIA', 'C',
                                 'CIC', 'C',
                                 PK_MOD_INV_PROPOSALS.GC_INV_TYPE_CICG, 'C',
                                 'CT',  'C',
                                 'CN',  'C',
                                 'CR',  'C',
                                 'CNU', 'C',
                                 'CICV', 'C',
                                 'CI',   'C',
                                 'CREM', 'C',
                                 PK_MOD_INV_PROPOSALS.GC_INV_TYPE_CICG, 'C')
    INTO v_retval
    FROM dual;
   
   RETURN v_retval;

EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_GDT');
END;

FUNCTION GET_DOC_TYPE_NAV(a_invoice_type IN VARCHAR2) RETURN VARCHAR2
IS
BEGIN

   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','GET_DOC_TYPE_NAV',NULL);
   
   /*
   IF a_invoice_type IS NULL THEN
      PK_ERROR.RAISE_ERROR(-20003, 'Typ dokumentu nie mo¿e byc NULL!');
   END IF;
   
   RETURN substr(a_invoice_type,1,5);
   */
   
   RETURN NULL;

EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_GDTN');
END;

FUNCTION GET_VAT_RATE(a_vat IN VARCHAR2, A_INV_TYPE IN VARCHAR2) RETURN VARCHAR2
IS
BEGIN

   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','GET_VAT_RATE',NULL);
   
   IF a_vat = 'ZW' THEN
   	
   	IF A_INV_TYPE IN ('IC','CIC',PK_MOD_INV_PROPOSALS.GC_INV_TYPE_ICG,PK_MOD_INV_PROPOSALS.GC_INV_TYPE_CICG) THEN
    	RETURN 'ZW';
    ELSE
    	RETURN 'NP';
    END IF;
   
   END IF;
   
   RETURN a_vat;

EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_GVR');
END;

PROCEDURE ADD_NAV_INV(A_NUM_SEQUENCE          IN VARCHAR2,
                      A_DOC_TYPE_NAV          IN VARCHAR2,
                      A_DOC_TYPE              IN VARCHAR2, 
                      A_PAYMENT_TYPE          IN VARCHAR2,
                      A_ACCOUNT               IN VARCHAR2,
                      A_MPK                   IN VARCHAR2,
                      A_TAX                   IN VARCHAR2, 
                      A_PRODUCT               IN VARCHAR2,
                      A_BOOK_DATE             IN DATE,
                      A_AMOUNT                IN NUMBER,
                      A_CODE                  IN VARCHAR2,
                      A_DOC_NUMBER            IN VARCHAR2,
                      A_VAT_RATE              IN VARCHAR2, 
                      A_ORD_ID                IN NUMBER,
                      A_PROFORMA_NO           IN VARCHAR2,
                      A_INVOICE_DATE          IN DATE,
                      A_PAYMENT_DATE          IN DATE,
                      A_PROJECT               IN VARCHAR2,
                      A_TGKV_CODE             IN VARCHAR2 DEFAULT NULL, 
                      A_VAT_DATE              IN DATE DEFAULT NULL,
                      A_SALE_DATE             IN DATE DEFAULT NULL,
                      A_ACCOUNT_NUMBER        IN VARCHAR2 DEFAULT NULL,
                      A_REFERENCE_NUMBER      IN VARCHAR2 DEFAULT NULL,
                      A_OUT_PAYMENT_CONF_CODE IN VARCHAR2 DEFAULT NULL,
                      A_INVOICE_LINE_NAME     IN VARCHAR2 DEFAULT NULL,
                      A_INVOICE_LINE_NAME_2   IN VARCHAR2 DEFAULT NULL,
                      A_UNIT_PRICE            IN NUMBER   DEFAULT NULL,
                      A_QUANTITY              IN NUMBER   DEFAULT NULL,
                      A_UNIT_NAV_CODE         IN VARCHAR2 DEFAULT NULL,
                      A_COR_REASON_NAV_CODE   IN VARCHAR2 DEFAULT NULL
                      )                      
IS
BEGIN

   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','ADD_NAV_INV',NULL);
    
   BEGIN

        INSERT INTO app_swp.nav_inv
                    (id, num_sequence, doc_type_nav, doc_type, payment_type,
                     ACCOUNT, mpk, tax, product, book_date,
                     amount, 
                     code, doc_number, vat_rate, ord_id,
                     proforma_no,
                     invoice_date, payment_date,
                     project,
                     tgkv_code,
                     vat_date,
                     sale_date,
                     ACCOUNT_NUMBER        ,
                     REFERENCE_NUMBER      ,
                     OUT_PAYMENT_CONF_CODE ,
                     INVOICE_LINE_NAME     ,
                     INVOICE_LINE_NAME_2   ,
                     UNIT_PRICE            ,
                     QUANTITY              ,
                     UNIT_NAV_CODE         ,
                     COR_REASON_NAV_CODE   
                    )
             VALUES (v_glob_nav_inv_id, a_num_sequence, a_doc_type_nav, a_doc_type, a_payment_type,
                     a_account, a_mpk, a_tax, a_product, to_char(a_book_date,'DDMMYYYY'),
                     replace(trim(to_char(a_amount,'999999999990.99')),'.',','),
                     a_code, substr(a_doc_number,1,20), a_vat_rate, substr(to_char(a_ord_id),1,8),
                     substr(a_proforma_no,1,15),
                     to_char(a_invoice_date,'DDMMYYYY'), to_char(a_payment_date,'DDMMYYYY'),
                     a_project, a_tgkv_code,
                     to_char(a_vat_date,'DDMMYYYY'),
                     to_char(a_sale_date,'DDMMYYYY'),
                     A_ACCOUNT_NUMBER        ,
                     A_REFERENCE_NUMBER      ,
                     A_OUT_PAYMENT_CONF_CODE ,
                     SUBSTR(A_INVOICE_LINE_NAME,1,50),
                     SUBSTR(A_INVOICE_LINE_NAME_2,1,50)  ,
                     replace(trim(to_char(A_UNIT_PRICE,'999999999990.99')),'.',','),
                     replace(trim(to_char(A_QUANTITY,'999999999990.99')),'.',','),
                     A_UNIT_NAV_CODE   ,
                     A_COR_REASON_NAV_CODE   
                     );
                    
   EXCEPTION
      WHEN others THEN
         PK_ERROR.RAISE_ERROR(-20005, 'doc_num='||a_doc_number||' errm='||sqlerrm);
   END;             
                
   v_glob_nav_inv_id := v_glob_nav_inv_id + 1;

EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_EUE');
END;

FUNCTION GET_PROFORMA_NUMBER(a_inv_id IN NUMBER) RETURN VARCHAR2
IS
   v_retval app_fin.invoices.invoice_number%TYPE;
   v_source app_fin.invoice_lines.source%TYPE;
   v_source_type app_fin.invoice_lines.source_type%TYPE;
BEGIN

   BEGIN
   
       SELECT DISTINCT iln.source, iln.source_type
       INTO v_source, v_source_type
       FROM app_fin.invoices inv,
            app_fin.invoice_lines iln
       WHERE inv.id = iln.inv_id
         AND inv.id = a_inv_id;
         
   EXCEPTION
      WHEN others THEN
         PK_ERROR.RAISE_ERROR(-20007, 'Brak dokumentu='||a_inv_id||sqlerrm);
   END;

   SELECT MAX(inv.invoice_number)
     INTO v_retval
     FROM app_fin.invoices inv,
          app_fin.invoice_lines iln
    WHERE inv.id = iln.inv_id
      AND inv.type IN ('P', 'PV', PK_MOD_INV_PROPOSALS.GC_INV_TYPE_PG)
      AND iln.SOURCE_TYPE = v_source_type
      AND iln.source = v_source;
      
      RETURN v_retval;
      
      
EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_EUE');
END;

/*---------------------------------------------------------------------------------------*/
--pobiera ostatecznie dobre konto dla faktur akceptanckich, wprowadzone dla SodexhoPolska
/* Formatted on 2007/09/07 10:11 (Formatter Plus v4.8.8) */
FUNCTION f_g_final_account (a_vat_reg_num VARCHAR2, a_doc_type VARCHAR2, a_org_account varchar2)
   RETURN VARCHAR2
AS
--numery kont, które maj¹ ulec zmianie
v_aff_inv_org_1 varchar2(20):='310020';
v_aff_inv_corr_org_1 varchar2(20):='310021';

--   v_default_return   VARCHAR2 (30) DEFAULT NULL;
BEGIN
   IF a_vat_reg_num IS NULL
   THEN
--        pk_error.WRITE_ERROR('-20003','jeden'||a_vat_reg_num);
      RETURN a_org_account;
   --konto sodexhoPolska
   ELSIF '1180038498' = a_vat_reg_num
   THEN
      IF a_doc_type IS NULL
      THEN
   --     pk_error.WRITE_ERROR('dwa');
         RETURN a_org_account;
      --faktury akceptanckie
      ELSIF 'A' = a_doc_type and v_aff_inv_org_1=a_org_account
      THEN
--              pk_error.WRITE_ERROR('-20003','trzy'||a_vat_reg_num);
         RETURN '310026';
      ELSIF 'B' = a_doc_type and v_aff_inv_corr_org_1=a_org_account
      THEN
--              pk_error.WRITE_ERROR('-20003','cztery'||a_vat_reg_num);      
         RETURN '310027';
      ELSE
--                  pk_error.WRITE_ERROR('-20003','piec'||a_vat_reg_num);
         RETURN a_org_account;
      END IF;
   ELSE
--                pk_error.WRITE_ERROR('-20003','szesc'||a_vat_reg_num);
      RETURN a_org_account;
   END IF;
END;

FUNCTION SPLIT (pc_pozostalosc VARCHAR2, pc_separator VARCHAR2 := ',')
  RETURN varchars2_type
AS
  vc_do_listy      VARCHAR2 (4000);
  vc_pozostalosc   VARCHAR2 (4000) := SUBSTR (pc_pozostalosc, 0, 4000);
  vct_lista        varchars2_type;
  vn_index         INTEGER         := 0;
BEGIN
  LOOP
     IF (INSTR (vc_pozostalosc, pc_separator) = 0)
     THEN  -- nie ma juz zadnego separatora, wiec zwracaj pozostaly wynik
        vct_lista (vn_index) := vc_pozostalosc;
        RETURN vct_lista;
     END IF;

     vct_lista (vn_index) :=
        SUBSTR (vc_pozostalosc,
                0,
                (INSTR (vc_pozostalosc, pc_separator)) - 1
               );                                 --wycina kolejny kawalek
     vc_pozostalosc :=
        SUBSTR (vc_pozostalosc,
                (INSTR (vc_pozostalosc, pc_separator)) + 1);
     --to co zostanie
     vn_index := vn_index + 1;
  END LOOP;
END;


FUNCTION GET_TGKV_CODE_SELL_IP(a_vat_rate       IN VARCHAR2, 
                               a_invoicing_type IN VARCHAR2,
                               a_account_no     IN VARCHAR2,
                               a_invoice_type   IN VARCHAR2) RETURN VARCHAR2
IS
  v_ret VARCHAR2(100);
BEGIN

  IF a_invoicing_type = PK_MOD_INV_PROPOSALS.GC_INVOICING_TYPE_SELECT THEN
    
    IF a_vat_rate = '23' THEN
      v_ret := 'VAT23 ';
    ELSIF a_vat_rate = '22' THEN
      v_ret := 'VAT22 ';
    ELSIF a_vat_rate = '8' THEN
      v_ret := 'VAT08 ';
    ELSIF a_vat_rate = '7' THEN
      v_ret := 'VAT07 ';
    ELSIF a_vat_rate = 'NP' THEN
      v_ret := 'NP ';
    ELSE
      RETURN 'ERR';
    END IF;
    
    IF    a_account_no = '310220' THEN v_ret := v_ret||'U';
    ELSIF a_account_no = '310221' THEN v_ret := v_ret||'U';
    ELSIF a_account_no = '310225' THEN v_ret := v_ret||'T';
    ELSIF a_account_no = '310227' THEN v_ret := v_ret||'T';
    ELSIF a_account_no = '310226' THEN v_ret := v_ret||'U';
    ELSIF a_account_no = '310222' THEN v_ret := v_ret||'T';
    ELSIF a_account_no = '310223' THEN v_ret := v_ret||'T';
    ELSIF a_account_no = '310225' THEN v_ret := v_ret||'T';
    ELSIF a_account_no = '255100' THEN v_ret := v_ret||'T';
    ELSE
      RETURN 'ERR';
    END IF;
    
    RETURN v_ret;
      
  
  ELSIF A_INVOICING_TYPE = PK_MOD_INV_PROPOSALS.GC_INVOICING_TYPE_SPS THEN
    
    IF a_vat_rate = '23' THEN
      v_ret := 'VAT23 U';
    ELSIF a_vat_rate = '22' THEN
      v_ret := 'VAT22 U';
    ELSIF a_vat_rate = '8' THEN
      v_ret := 'VAT8 U';
    ELSIF a_vat_rate = '7' THEN
      v_ret := 'VAT7 U';
    ELSIF a_vat_rate = 'NP' THEN
      v_ret := 'NP U';
    ELSE
      RETURN 'ERR';
    END IF;

    RETURN v_ret;
  

  ELSE

    RETURN 'ERR';
  
  END IF;

EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_GTCSI');
END;



FUNCTION GET_TGKV_CODE_SELL(a_vat_rate IN VARCHAR2, A_VB IN VARCHAR2) RETURN VARCHAR2
IS
BEGIN

	IF A_VB = 'Y' THEN
  
  	IF a_vat_rate = '23' THEN
    	RETURN 'VAT23 T';
    ELSIF a_vat_rate = '22' THEN
    	RETURN 'VAT22 T';
    ELSE
    	RETURN 'ERR';
    END IF;
  
  ELSE
  
  	IF a_vat_rate = 'ZW' THEN
    	RETURN 'ZW U';
    ELSIF a_vat_rate = '23' THEN
    	RETURN 'VAT23 U';
    ELSIF a_vat_rate = '22' THEN
    	RETURN 'VAT22 U';
    ELSIF a_vat_rate = 'NP' THEN
        RETURN 'NP U';
    ELSE
    	RETURN 'ERR';
    END IF;
  
  END IF;

EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_GTCS');
END;


FUNCTION GET_TGKV_CODE_BUY(a_vat_rate IN VARCHAR2, a_rem_vb IN VARCHAR2, a_mpk IN VARCHAR2, a_service IN VARCHAR2) RETURN VARCHAR2
IS
BEGIN
	
	IF a_rem_vb = 'Y' THEN
  
  	IF a_vat_rate = '22' THEN
    	RETURN 'VAT22 U';
    ELSIF a_vat_rate = '23' THEN
    	RETURN 'VAT23 U';
    ELSIF a_vat_rate = '7' THEN
    	RETURN 'VAT07 U';
    ELSIF a_vat_rate = '8' THEN
    	RETURN 'VAT08 U';
    ELSIF a_vat_rate = 'ZW' THEN
    	RETURN 'ZW U';
    ELSIF a_vat_rate = '0' THEN
    	RETURN 'VAT0 U';
    ELSIF a_vat_rate = '5' THEN
        RETURN 'VAT05 U';
    ELSIF a_vat_rate = 'NP' THEN
        RETURN 'NP U';
    ELSE
    	RETURN 'ERR';
    END IF;
    
  
  ELSE 
  
  	IF a_mpk in ('VB-OVH', 'PR-KUR', 'PM-OVH')
    OR (a_mpk = 'PR-DEL' AND a_service NOT IN ('25','26','27','28','32')) 
    OR (a_mpk = 'PR-PRO' AND a_service NOT IN ('25','26','27','28','32')) THEN
    
    	IF a_vat_rate = 'NP' THEN 
      	RETURN 'NP U';
      ELSIF a_vat_rate = '22' THEN
      	RETURN 'VAT22 U ';
      ELSIF a_vat_rate = '23' THEN
      	RETURN 'VAT23 U';
      ELSIF a_vat_rate = '0' THEN
      	RETURN 'VAT0 U';
      ELSE
      	RETURN 'ERR';
      END IF; 
    
    ELSIF (a_mpk = 'PR-DEL' AND a_service in ('25','26','27','28','32'))
    or    (a_mpk = 'PR-PRO' AND a_service in ('25','26','27','28','32')) THEN
    	
    	IF a_vat_rate = 'NP' THEN 
      	RETURN 'NP U';
      ELSIF a_vat_rate = '22' THEN
      	RETURN 'VAT22 U';
      ELSIF a_vat_rate = '23' THEN
      	RETURN 'VAT23 U';
      ELSIF a_vat_rate = '0' THEN
      	RETURN 'VAT0 U';
      ELSE
      	RETURN 'ERR';
      END IF;
    
    ELSIF a_mpk not in ('VB-OVH','PR-KUR','PR-DEL','PM-OVH') THEN
    	
    	IF a_vat_rate = 'NP' THEN 
      	RETURN 'NP U';
      ELSIF a_vat_rate = '22' THEN
      	RETURN 'VAT22 U';
      ELSIF a_vat_rate = '23' THEN
      	RETURN 'VAT23 U';
      ELSIF a_vat_rate = '0' THEN
      	RETURN 'VAT0 U';
      ELSE
      	RETURN 'ERR';
      END IF;
      
    ELSE
    	RETURN 'ERR';
    END IF;
    
  END IF;

EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_GTCB');
END;

FUNCTION GET_PROFORMA_ID(a_inv_id IN NUMBER) RETURN NUMBER
IS
   v_retval app_fin.invoices.ID%TYPE;
   v_source app_fin.invoice_lines.source%TYPE;
   v_source_type app_fin.invoice_lines.source_type%TYPE;
BEGIN

   BEGIN
   
       SELECT DISTINCT iln.source, iln.source_type
       INTO v_source, v_source_type
       FROM app_fin.invoices inv,
            app_fin.invoice_lines iln
       WHERE inv.id = iln.inv_id
         AND inv.id = a_inv_id;
         
   EXCEPTION
      WHEN others THEN
         PK_ERROR.RAISE_ERROR(-20007, 'Brak dokumentu='||a_inv_id||sqlerrm);
   END;

   SELECT MAX(inv.ID)
     INTO v_retval
     FROM app_fin.invoices inv,
          app_fin.invoice_lines iln
    WHERE inv.id = iln.inv_id
      AND inv.type IN ('P', 'PV', PK_MOD_INV_PROPOSALS.GC_INV_TYPE_PG)
      AND iln.SOURCE_TYPE = v_source_type
      AND iln.source = v_source;
      
   RETURN v_retval;
      
EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_GPI');
END;

PROCEDURE Pbe_Individuals_Enterprises(a_input_date DATE,
                                      a_task_id IN NUMBER,
                                      a_task_all_id IN NUMBER)
IS
   TYPE t_cus_type_data IS RECORD (NAV_TYPE    VARCHAR2(10),
                                   CLEAR_TYPE  VARCHAR2(10),
                                   ACC_NO      r_acc,
                                   E_INV_PERMISSION VARCHAR2(1) -- T/N
                                  );
   TYPE t_cus_types_data IS TABLE OF t_cus_type_data INDEX BY VARCHAR2(1);                             
   v_cus_types_data t_cus_types_data;                             
   vt_efv   t_emails;
BEGIN
   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','External_Customers',NULL);
   
  v_cus_types_data('I').NAV_TYPE        := 'C';                               
  v_cus_types_data('I').ACC_NO          := GET_ACCOUNT('PBE_USER_ACCOUNT');
  v_cus_types_data('I').CLEAR_TYPE      := 'PD';
  v_cus_types_data('I').E_INV_PERMISSION:= 'T';
  
   
  v_cus_types_data('E').NAV_TYPE        := 'C';
  v_cus_types_data('E').ACC_NO          := GET_ACCOUNT('PBE_USER_ACCOUNT'); 
  v_cus_types_data('E').CLEAR_TYPE      := 'PD';
  v_cus_types_data('E').E_INV_PERMISSION:= 'T';

  v_cus_types_data('T').NAV_TYPE        := 'A';
  v_cus_types_data('T').ACC_NO          := GET_ACCOUNT('PBE_TRAINING_INSTITUTION_ACCOUNT'); 
  v_cus_types_data('T').CLEAR_TYPE      := 'PD';
  v_cus_types_data('T').E_INV_PERMISSION:= 'N';

   FOR I IN (
             SELECT  I.CODE                          CODE,       
                    I.FIRST_NAME ||' '||I.LAST_NAME NAME,  
                    I.ADDRESS_INVOICE               ADDRESS,
                    CI.ZIP_CODE                     ZIP_CODE, 
                    CI.CITY_NAME                    CITY_NAME,
                    '0000000000'                    VAT_REG_NUM, 
                    IC.PHONE                        PHONE_NUMBER,
                    I.ACCOUNT_PAYMENT               ACCOUNT_NUMBER,
                    I.ID                            ID,
                    'I'                             SOURCE_TABLE,
                    I.ADDRESS_CORR                  ADDRESS_CORR,     
                    CC.ZIP_CODE                     ZIP_CODE_CORR, 
                    CC.CITY_NAME                    CITY_NAME_CORR,
                    CASE
                      WHEN (TRUNC(I.MODIFIED_TIMESTAMP) =  TRUNC(a_input_date) -- :v_date
                            OR    CRT_INV.INDIVIDUAL_ID IS NOT NULL
                            OR    MOD_ORD.INDIVIDUAL_ID IS NOT NULL
                            OR    MOD_CON.INDIVIDUAL_ID IS NOT NULL
                            ) 
                      THEN  'Y'
                      ELSE  'N'
                    END                             IN_MODIFIED_FILE,
                    ICM.EMAIL                       E_INV_EMAIL
             FROM   (SELECT I.ID                        ID,
                             C.CODE                      CODE,
                             I.FIRST_NAME                FIRST_NAME,
                             I.LAST_NAME                 LAST_NAME,
                             C.ADDRESS_INVOICE           ADDRESS_INVOICE,
                             I.ADDRESS_CORR              ADDRESS_CORR,
                             C.ZIP_CODE_CORR             ZIP_CODE_CORR,
                             C.ZIP_CODE_INVOICE          ZIP_CODE_INVOICE,
                             C.ACCOUNT_PAYMENT           ACCOUNT_PAYMENT,
                             GREATEST(I.MODIFIED_TIMESTAMP, C.MODIFIED_TIMESTAMP) MODIFIED_TIMESTAMP
                      FROM   APP_PBE.INDIVIDUALS I 
                             JOIN APP_PBE.CONTRACTS C ON C.INDIVIDUAL_ID = I.ID
                      WHERE  C.ENTERPRISE_ID IS NULL) I
                    LEFT JOIN EAGLE.ZIP_CODES CI ON CI.ID = I.ZIP_CODE_INVOICE 
                    LEFT JOIN EAGLE.ZIP_CODES CC ON CC.ID = I.ZIP_CODE_CORR 
                    LEFT JOIN (SELECT IC.IND_ID, MAX(IC.CONTACT_DATA) PHONE
                               FROM   APP_PBE.INDIVIDUAL_CONTACTS IC
                               WHERE  IC.CONTACT_TYPE = 'PHONE' 
                               GROUP BY IC.IND_ID) IC 
                         ON IC.IND_ID = I.ID
                    LEFT JOIN (SELECT IC.IND_ID, MAX(IC.CONTACT_DATA) EMAIL
                               FROM   APP_PBE.INDIVIDUAL_CONTACTS IC
                               WHERE  IC.CONTACT_TYPE = 'EMAIL' 
                               GROUP BY IC.IND_ID) ICM 
                         ON ICM.IND_ID = I.ID
                    LEFT JOIN (SELECT DISTINCT CON.INDIVIDUAL_ID
                               FROM   APP_PBE.ORDERS O
                                      JOIN APP_PBE.CONTRACTS CON ON CON.ID = O.CONTRACT_ID AND CON.ENTERPRISE_ID IS NULL
                               WHERE  TRUNC(O.MODIFIED_TIMESTAMP) =  TRUNC(a_input_date)) MOD_ORD 
                         ON MOD_ORD.INDIVIDUAL_ID = I.ID -- :v_date
                    LEFT JOIN (SELECT DISTINCT CON.INDIVIDUAL_ID
                               FROM   APP_PBE.ORDERS O
                                      JOIN APP_PBE.CONTRACTS CON ON CON.ID = O.CONTRACT_ID AND CON.ENTERPRISE_ID IS NULL
                                      JOIN APP_PBE.ORDER_INVOICES OI ON OI.ORDER_ID = O.ID
                                      JOIN APP_FIN.INVOICES I ON I.ID = OI.ID 
                               WHERE  TRUNC(O.MODIFIED_TIMESTAMP) =  TRUNC(a_input_date)) CRT_INV 
                         ON CRT_INV.INDIVIDUAL_ID = I.ID -- :v_date
                    LEFT JOIN (SELECT DISTINCT CON.INDIVIDUAL_ID
                               FROM   APP_PBE.CONTRACTS CON 
                               WHERE  CON.ENTERPRISE_ID IS NULL
                               AND    TRUNC(CON.MODIFIED_TIMESTAMP) =  TRUNC(a_input_date)) MOD_CON 
                         ON MOD_CON.INDIVIDUAL_ID = I.ID -- :v_date
             UNION      
             SELECT E.CODE                          CODE,       
                    E.NAME                          NAME,  
                    E.ADDRESS_INVOICE               ADDRESS,
                    CI.ZIP_CODE                     ZIP_CODE, 
                    CI.CITY_NAME                    CITY_NAME,
                    E.VAT_REG_NUM                   VAT_REG_NUM, 
                    EC.PHONE                        PHONE_NUMBER,
                    E.ACCOUNT_PAYMENT               ACCOUNT_NUMBER,
                    E.ID                            ID,
                    'E'                             SOURCE_TABLE,
                    E.ADDRESS_CORR                  ADDRESS_CORR,     
                    CC.ZIP_CODE                     ZIP_CODE_CORR, 
                    CC.CITY_NAME                    CITY_NAME_CORR, 
                    CASE
                      WHEN (TRUNC(E.MODIFIED_TIMESTAMP) =  TRUNC(a_input_date) -- :v_date
                            OR    CRT_INV.ENTERPRISE_ID IS NOT NULL
                            OR    MOD_ORD.ENTERPRISE_ID IS NOT NULL
                            OR    MOD_CON.ENTERPRISE_ID IS NOT NULL) 
                      THEN  'Y'
                      ELSE  'N'
                    END                             IN_MODIFIED_FILE,
                    ECM.EMAIL                       E_INV_EMAIL
             FROM   (SELECT E.ID                        ID,
                             C.CODE                      CODE,
                             E.NAME                      NAME,
                             C.ADDRESS_INVOICE           ADDRESS_INVOICE,
                             E.ADDRESS_CORR              ADDRESS_CORR,
                             C.ZIP_CODE_CORR             ZIP_CODE_CORR,
                             C.ZIP_CODE_INVOICE          ZIP_CODE_INVOICE,
                             C.ACCOUNT_PAYMENT           ACCOUNT_PAYMENT,
                             E.VAT_REG_NUM               VAT_REG_NUM,
                             GREATEST(E.MODIFIED_TIMESTAMP, C.MODIFIED_TIMESTAMP) MODIFIED_TIMESTAMP
                      FROM   APP_PBE.ENTERPRISES E 
                             JOIN APP_PBE.CONTRACTS C ON C.ENTERPRISE_ID = E.ID) E
                    LEFT JOIN EAGLE.ZIP_CODES CI ON CI.ID = E.ZIP_CODE_INVOICE 
                    LEFT JOIN EAGLE.ZIP_CODES CC ON CC.ID = E.ZIP_CODE_CORR 
                    LEFT JOIN (SELECT EC.ENT_ID, MAX(EC.CONTACT_DATA) PHONE
                               FROM   APP_PBE.ENTERPRISE_CONTACTS EC
                               WHERE  EC.CONTACT_TYPE = 'PHONE' 
                               GROUP BY EC.ENT_ID) EC 
                         ON EC.ENT_ID = E.ID
                    LEFT JOIN (SELECT EC.ENT_ID, MAX(EC.CONTACT_DATA) EMAIL
                               FROM   APP_PBE.ENTERPRISE_CONTACTS EC
                               WHERE  EC.CONTACT_TYPE = 'EMAIL' 
                               GROUP BY EC.ENT_ID) ECM 
                         ON ECM.ENT_ID = E.ID
                    LEFT JOIN (SELECT DISTINCT CON.ENTERPRISE_ID
                               FROM   APP_PBE.ORDERS O
                                      JOIN APP_PBE.CONTRACTS CON ON CON.ID = O.CONTRACT_ID AND CON.ENTERPRISE_ID IS NOT NULL
                               WHERE  TRUNC(O.MODIFIED_TIMESTAMP) =  TRUNC(a_input_date)) MOD_ORD 
                         ON MOD_ORD.ENTERPRISE_ID = E.ID -- :v_date
                    LEFT JOIN (SELECT DISTINCT CON.ENTERPRISE_ID
                               FROM   APP_PBE.ORDERS O
                                      JOIN APP_PBE.CONTRACTS CON ON CON.ID = O.CONTRACT_ID AND CON.ENTERPRISE_ID IS NOT NULL
                                      JOIN APP_PBE.ORDER_INVOICES OI ON OI.ORDER_ID = O.ID
                                      JOIN APP_FIN.INVOICES I ON I.ID = OI.ID 
                               WHERE  TRUNC(O.MODIFIED_TIMESTAMP) =  TRUNC(a_input_date)) CRT_INV 
                         ON CRT_INV.ENTERPRISE_ID = E.ID -- :v_date
                    LEFT JOIN (SELECT DISTINCT CON.ENTERPRISE_ID
                               FROM   APP_PBE.CONTRACTS CON 
                               WHERE  CON.ENTERPRISE_ID IS NOT NULL
                               AND    TRUNC(CON.MODIFIED_TIMESTAMP) =  TRUNC(a_input_date)
                               ) MOD_CON 
                         ON MOD_CON.ENTERPRISE_ID = E.ID -- :v_date
        )             
    LOOP   
       ADD_NAV_CUST(
          A_CODE              => i.CODE,
          A_NAME              => i.NAME,
          A_AD_LINE_1         => i.address, 
          A_CITY_CODE         => i.zip_code,
          A_CITY_NAME         => i.CITY_NAME,
          A_VAT_REG_NUM       => NVL(i.VAT_REG_NUM,'0000000000'),
          A_PHONE             => i.phone_number, 
          A_TYPE              => v_cus_types_data(I.SOURCE_TABLE).NAV_TYPE,
          A_ACCOUNT           => v_cus_types_data(I.SOURCE_TABLE).ACC_NO.ACC_NO,
          A_CLEAR_TYPE        => v_cus_types_data(I.SOURCE_TABLE).CLEAR_TYPE, 
          A_BANK_ACCOUNT_NUM  => NULL, --i.account_number,
          A_TRANSFER_DESC     => NULL,
          A_ACC_CODE          => NULL, 
          A_ACC_NAME          => NULL,
          A_ACC_AD_LINE_1     => NULL,
          A_ACC_CITY_CODE     => NULL,
          A_ACC_CITY_NAME     => NULL, 
          A_ACC_VAT_REG_NUM   => NULL,
          A_ACC_PHONE         => NULL,
          A_ACC_TYPE          => NULL, 
          A_ACC_ACCOUNT       => NULL,
          A_ACC_CLEAR_TYPE    => NULL,
          A_ACC_BANK_ACCOUNT_NUM  => NULL, 
          A_ACC_TRANSFER_DESC => NULL,
          A_TASK_ID           => a_task_all_id, 
          A_E_INV_PERMISSION  => v_cus_types_data(I.SOURCE_TABLE).E_INV_PERMISSION, 
          A_SENSITIVE_PARTNER => NULL,  
          A_COR_AD_LINE_1     => i.ADDRESS_CORR,      
          A_COR_CITY_CODE     => i.ZIP_CODE_CORR,      
          A_COR_CITY_NAME     => i.CITY_NAME_CORR,      
          A_EMAIL_FV_1        => NULL,                   
          A_EMAIL_FV_2        => NULL,   
          A_EMAIL_FV_3        => NULL,            
          A_EMAIL_FV_4        => NULL,            
          A_EMAIL_FV_5        => NULL,           
          A_EMAIL_MID_1       => NULL,          
          A_EMAIL_MID_2       => NULL,   
          A_EMAIL_SXO         => NULL);
      IF I.IN_MODIFIED_FILE = 'Y' THEN
          ADD_NAV_CUST(
              A_CODE              => i.CODE,
              A_NAME              => i.NAME,
              A_AD_LINE_1         => i.address, 
              A_CITY_CODE         => i.zip_code,
              A_CITY_NAME         => i.CITY_NAME,
              A_VAT_REG_NUM       => NVL(i.VAT_REG_NUM,'0000000000'),
              A_PHONE             => i.phone_number, 
              A_TYPE              => v_cus_types_data(I.SOURCE_TABLE).NAV_TYPE,
              A_ACCOUNT           => v_cus_types_data(I.SOURCE_TABLE).ACC_NO.ACC_NO,
              A_CLEAR_TYPE        => v_cus_types_data(I.SOURCE_TABLE).CLEAR_TYPE, 
              A_BANK_ACCOUNT_NUM  => NULL, -- i.account_number,
              A_TRANSFER_DESC     => NULL,
              A_ACC_CODE          => NULL, 
              A_ACC_NAME          => NULL,
              A_ACC_AD_LINE_1     => NULL,
              A_ACC_CITY_CODE     => NULL,
              A_ACC_CITY_NAME     => NULL, 
              A_ACC_VAT_REG_NUM   => NULL,
              A_ACC_PHONE         => NULL,
              A_ACC_TYPE          => NULL, 
              A_ACC_ACCOUNT       => NULL,
              A_ACC_CLEAR_TYPE    => NULL,
              A_ACC_BANK_ACCOUNT_NUM  => NULL, 
              A_ACC_TRANSFER_DESC => NULL,
              A_TASK_ID           => a_task_id, 
              A_E_INV_PERMISSION  => v_cus_types_data(I.SOURCE_TABLE).E_INV_PERMISSION, 
              A_SENSITIVE_PARTNER => NULL,  
              A_COR_AD_LINE_1     => i.ADDRESS_CORR,      
              A_COR_CITY_CODE     => i.ZIP_CODE_CORR,      
              A_COR_CITY_NAME     => i.CITY_NAME_CORR,      
              A_EMAIL_FV_1        => NULL,                   
              A_EMAIL_FV_2        => NULL,   
              A_EMAIL_FV_3        => NULL,            
              A_EMAIL_FV_4        => NULL,            
              A_EMAIL_FV_5        => NULL,           
              A_EMAIL_MID_1       => NULL,          
              A_EMAIL_MID_2       => NULL,   
              A_EMAIL_SXO         => NULL);      
      END IF;          
    END LOOP;                
   
EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_PIE');
END Pbe_Individuals_Enterprises;


PROCEDURE Pbe_Training_Institutions(  a_input_date  IN DATE,
                                      a_task_id     IN NUMBER,
                                      a_task_all_id IN NUMBER)
IS
   TYPE t_cus_type_data IS RECORD (NAV_TYPE         VARCHAR2(10),
                                   CLEAR_TYPE       VARCHAR2(10),
                                   ACC_NO           r_acc,
                                   E_INV_PERMISSION VARCHAR2(1) -- T/N
                                  );
   TYPE t_cus_types_data IS TABLE OF t_cus_type_data INDEX BY VARCHAR2(1);                             
   v_cus_types_data t_cus_types_data;                             
   vt_efv   t_emails;
BEGIN
   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','Pbe_Training_Institutions',NULL);
   
  v_cus_types_data('I').NAV_TYPE        := 'C';                               
  v_cus_types_data('I').ACC_NO          := GET_ACCOUNT('PBE_USER_ACCOUNT');
  v_cus_types_data('I').CLEAR_TYPE      := 'PD';
  v_cus_types_data('I').E_INV_PERMISSION:= 'T';
  
   
  v_cus_types_data('E').NAV_TYPE        := 'C';
  v_cus_types_data('E').ACC_NO          := GET_ACCOUNT('PBE_USER_ACCOUNT'); 
  v_cus_types_data('E').CLEAR_TYPE      := 'PD';
  v_cus_types_data('E').E_INV_PERMISSION:= 'T';

  v_cus_types_data('T').NAV_TYPE        := 'A';
  v_cus_types_data('T').ACC_NO          := GET_ACCOUNT('PBE_TRAINING_INSTITUTION_ACCOUNT'); 
  v_cus_types_data('T').CLEAR_TYPE      := 'PD';
  v_cus_types_data('T').E_INV_PERMISSION:= 'N';

   FOR I IN (
             SELECT TI.CODE                         CODE,       
                    TI.NAME                         NAME,  
                    TI.ADDRESS_INVOICE              ADDRESS,
                    CI.ZIP_CODE                     ZIP_CODE, 
                    CI.CITY_NAME                    CITY_NAME,
                    TI.VAT_REG_NUM                  VAT_REG_NUM, 
                    TIC.PHONE                       PHONE_NUMBER,
                    NULL                            ACCOUNT_NUMBER,
                    TI.ID                           ID,
                    'T'                             SOURCE_TABLE,
                    TI.ADDRESS_CORR                 ADDRESS_CORR,     
                    CC.ZIP_CODE                     ZIP_CODE_CORR, 
                    CC.CITY_NAME                    CITY_NAME_CORR, 
                    CASE
                      WHEN (TRUNC(TI.MODIFIED_TIMESTAMP) =  TRUNC(a_input_date) -- :v_date
                            OR    CRT_INV.TRAINING_INSTITUTION_ID IS NOT NULL
                            OR    MOD_RMB.TRAINING_INSTITUTION_ID IS NOT NULL )  -- zmodyfikowane rozliczenie
                      THEN  'Y'
                      ELSE  'N'
                    END IN_MODIFIED_FILE
             FROM   APP_PBE.TRAINING_INSTITUTIONS TI
                    LEFT JOIN EAGLE.ZIP_CODES CI ON CI.ID = TI.ZIP_CODE_INVOICE 
                    LEFT JOIN EAGLE.ZIP_CODES CC ON CC.ID = TI.ZIP_CODE_CORR 
                    LEFT JOIN (SELECT TIC.TRAINING_INSTITUTION_ID, MAX(TIC.CONTACT_DATA) PHONE
                               FROM   APP_PBE.TRAINING_INSTITUTION_CONTACTS TIC
                               WHERE  TIC.CONTACT_TYPE = 'PHONE' 
                               GROUP BY TIC.TRAINING_INSTITUTION_ID) TIC 
                         ON TIC.TRAINING_INSTITUTION_ID = TI.ID
                    LEFT JOIN (SELECT DISTINCT TI.TRAINING_INSTITUTION_ID
                               FROM   APP_PBE.E_REIMBURSEMENTS R
                                      JOIN APP_PBE.TI_TRAINING_INSTANCES TII ON TII.ID = R.TI_TR_INST_ID
                                      JOIN APP_PBE.TI_TRAININGS          TI  ON TI.ID = TII.TRAINING_ID                                      
                               WHERE  TRUNC(R.MODIFIED_TIMESTAMP ) =  TRUNC(a_input_date)) MOD_RMB 
                         ON MOD_RMB.TRAINING_INSTITUTION_ID = TI.ID -- :v_date
                    LEFT JOIN (SELECT DISTINCT TI.TRAINING_INSTITUTION_ID
                               FROM   APP_PBE.E_REIMBURSEMENTS R
                                      JOIN APP_PBE.TI_TRAINING_INSTANCES TII ON TII.ID = R.TI_TR_INST_ID
                                      JOIN APP_PBE.TI_TRAININGS          TI  ON TI.ID = TII.TRAINING_ID
                                      JOIN APP_PBE.E_REIMBURSEMENT_INVOICES RI ON RI.E_REIMBURSEMENT_ID = R.ID
                                      JOIN APP_FIN.INVOICES              I   ON I.ID = RI.INVOICE_ID                                      
                               WHERE  TRUNC(I.INVOICE_DATE ) =  TRUNC(a_input_date)) CRT_INV 
                         ON CRT_INV.TRAINING_INSTITUTION_ID = TI.ID -- :v_date
             )
    LOOP   
       ADD_NAV_CUST(
          A_CODE              => i.CODE,
          A_NAME              => i.NAME,
          A_AD_LINE_1         => i.address, 
          A_CITY_CODE         => i.zip_code,
          A_CITY_NAME         => i.CITY_NAME,
          A_VAT_REG_NUM       => NVL(i.VAT_REG_NUM,'0000000000'),
          A_PHONE             => i.phone_number, 
          A_TYPE              => v_cus_types_data(I.SOURCE_TABLE).NAV_TYPE,
          A_ACCOUNT           => v_cus_types_data(I.SOURCE_TABLE).ACC_NO.ACC_NO,
          A_CLEAR_TYPE        => v_cus_types_data(I.SOURCE_TABLE).CLEAR_TYPE, 
          A_BANK_ACCOUNT_NUM  => i.account_number,
          A_TRANSFER_DESC     => NULL,
          A_ACC_CODE          => NULL, 
          A_ACC_NAME          => NULL,
          A_ACC_AD_LINE_1     => NULL,
          A_ACC_CITY_CODE     => NULL,
          A_ACC_CITY_NAME     => NULL, 
          A_ACC_VAT_REG_NUM   => NULL,
          A_ACC_PHONE         => NULL,
          A_ACC_TYPE          => NULL, 
          A_ACC_ACCOUNT       => NULL,
          A_ACC_CLEAR_TYPE    => NULL,
          A_ACC_BANK_ACCOUNT_NUM  => NULL, 
          A_ACC_TRANSFER_DESC => NULL,
          A_TASK_ID           => a_task_all_id, 
          A_E_INV_PERMISSION  => v_cus_types_data(I.SOURCE_TABLE).E_INV_PERMISSION, 
          A_SENSITIVE_PARTNER => NULL,  
          A_COR_AD_LINE_1     => i.ADDRESS_CORR,      
          A_COR_CITY_CODE     => i.ZIP_CODE_CORR,      
          A_COR_CITY_NAME     => i.CITY_NAME_CORR,      
          A_EMAIL_FV_1        => NULL,                   
          A_EMAIL_FV_2        => NULL,   
          A_EMAIL_FV_3        => NULL,            
          A_EMAIL_FV_4        => NULL,            
          A_EMAIL_FV_5        => NULL,           
          A_EMAIL_MID_1       => NULL,          
          A_EMAIL_MID_2       => NULL,   
          A_EMAIL_SXO         => NULL);
      IF I.IN_MODIFIED_FILE = 'Y' THEN
          ADD_NAV_CUST(
              A_CODE              => i.CODE,
              A_NAME              => i.NAME,
              A_AD_LINE_1         => i.address, 
              A_CITY_CODE         => i.zip_code,
              A_CITY_NAME         => i.CITY_NAME,
              A_VAT_REG_NUM       => NVL(i.VAT_REG_NUM,'0000000000'),
              A_PHONE             => i.phone_number, 
              A_TYPE              => v_cus_types_data(I.SOURCE_TABLE).NAV_TYPE,
              A_ACCOUNT           => v_cus_types_data(I.SOURCE_TABLE).ACC_NO.ACC_NO,
              A_CLEAR_TYPE        => v_cus_types_data(I.SOURCE_TABLE).CLEAR_TYPE, 
              A_BANK_ACCOUNT_NUM  => i.account_number,
              A_TRANSFER_DESC     => NULL,
              A_ACC_CODE          => NULL, 
              A_ACC_NAME          => NULL,
              A_ACC_AD_LINE_1     => NULL,
              A_ACC_CITY_CODE     => NULL,
              A_ACC_CITY_NAME     => NULL, 
              A_ACC_VAT_REG_NUM   => NULL,
              A_ACC_PHONE         => NULL,
              A_ACC_TYPE          => NULL, 
              A_ACC_ACCOUNT       => NULL,
              A_ACC_CLEAR_TYPE    => NULL,
              A_ACC_BANK_ACCOUNT_NUM  => NULL, 
              A_ACC_TRANSFER_DESC => NULL,
              A_TASK_ID           => a_task_id, 
              A_E_INV_PERMISSION  => v_cus_types_data(I.SOURCE_TABLE).E_INV_PERMISSION, 
              A_SENSITIVE_PARTNER => NULL,  
              A_COR_AD_LINE_1     => i.ADDRESS_CORR,      
              A_COR_CITY_CODE     => i.ZIP_CODE_CORR,      
              A_COR_CITY_NAME     => i.CITY_NAME_CORR,      
              A_EMAIL_FV_1        => NULL,                   
              A_EMAIL_FV_2        => NULL,   
              A_EMAIL_FV_3        => NULL,            
              A_EMAIL_FV_4        => NULL,            
              A_EMAIL_FV_5        => NULL,           
              A_EMAIL_MID_1       => NULL,          
              A_EMAIL_MID_2       => NULL,   
              A_EMAIL_SXO         => NULL);      
      END IF;          
    END LOOP;                
   
EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_PTI');
END Pbe_Training_Institutions;


-- eksport Pustaków
PROCEDURE Pbe_Account_Contract_Pairs( a_input_date DATE,
                                      a_task_id IN NUMBER,
                                      a_task_all_id IN NUMBER)
IS
   TYPE t_cus_type_data IS RECORD (NAV_TYPE    VARCHAR2(10),
                                   CLEAR_TYPE  VARCHAR2(10),
                                   ACC_NO      r_acc,
                                   E_INV_PERMISSION VARCHAR2(1) -- T/N
                                  );
   TYPE t_cus_types_data IS TABLE OF t_cus_type_data INDEX BY VARCHAR2(1);                             
   v_cus_types_data t_cus_types_data;                             
   vt_efv   t_emails;
BEGIN
   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','External_Customers',NULL);
   
  v_cus_types_data('I').NAV_TYPE        := 'C';                               
  v_cus_types_data('I').ACC_NO          := GET_ACCOUNT('PBE_USER_ACCOUNT');
  v_cus_types_data('I').CLEAR_TYPE      := 'PD';
  v_cus_types_data('I').E_INV_PERMISSION:= 'T';
  
   
  v_cus_types_data('E').NAV_TYPE        := 'C';
  v_cus_types_data('E').ACC_NO          := GET_ACCOUNT('PBE_USER_ACCOUNT'); 
  v_cus_types_data('E').CLEAR_TYPE      := 'PD';
  v_cus_types_data('E').E_INV_PERMISSION:= 'T';

  v_cus_types_data('T').NAV_TYPE        := 'A';
  v_cus_types_data('T').ACC_NO          := GET_ACCOUNT('PBE_TRAINING_INSTITUTION_ACCOUNT'); 
  v_cus_types_data('T').CLEAR_TYPE      := 'PD';
  v_cus_types_data('T').E_INV_PERMISSION:= 'N';

   FOR I IN (SELECT SUBSTR(P.ACCOUNT_PAYMENT,19)    CODE,       
                    'PB starter'                    NAME,  
                    NULL                            ADDRESS,
                    NULL                            ZIP_CODE, 
                    NULL                            CITY_NAME,
                    '0000000000'                    VAT_REG_NUM, 
                    NULL                            PHONE_NUMBER,
                    P.ACCOUNT_PAYMENT               ACCOUNT_NUMBER,
                    1                               ID,
                    'I'                             SOURCE_TABLE,
                    NULL                            ADDRESS_CORR,     
                    NULL                            ZIP_CODE_CORR, 
                    NULL                            CITY_NAME_CORR,
                    'Y'                             IN_MODIFIED_FILE,
                    NULL                            E_INV_EMAIL
             FROM   APP_PBE.ACCOUNT_CONTRACT_PAIRS P
            )             
    LOOP   
       ADD_NAV_CUST(
          A_CODE              => i.CODE,
          A_NAME              => i.NAME,
          A_AD_LINE_1         => i.address, 
          A_CITY_CODE         => i.zip_code,
          A_CITY_NAME         => i.CITY_NAME,
          A_VAT_REG_NUM       => NVL(i.VAT_REG_NUM,'0000000000'),
          A_PHONE             => i.phone_number, 
          A_TYPE              => v_cus_types_data(I.SOURCE_TABLE).NAV_TYPE,
          A_ACCOUNT           => v_cus_types_data(I.SOURCE_TABLE).ACC_NO.ACC_NO,
          A_CLEAR_TYPE        => v_cus_types_data(I.SOURCE_TABLE).CLEAR_TYPE, 
          A_BANK_ACCOUNT_NUM  => NULL,
          A_TRANSFER_DESC     => NULL,
          A_ACC_CODE          => NULL, 
          A_ACC_NAME          => NULL,
          A_ACC_AD_LINE_1     => NULL,
          A_ACC_CITY_CODE     => NULL,
          A_ACC_CITY_NAME     => NULL, 
          A_ACC_VAT_REG_NUM   => NULL,
          A_ACC_PHONE         => NULL,
          A_ACC_TYPE          => NULL, 
          A_ACC_ACCOUNT       => NULL,
          A_ACC_CLEAR_TYPE    => NULL,
          A_ACC_BANK_ACCOUNT_NUM  => NULL, 
          A_ACC_TRANSFER_DESC => NULL,
          A_TASK_ID           => a_task_all_id, 
          A_E_INV_PERMISSION  => v_cus_types_data(I.SOURCE_TABLE).E_INV_PERMISSION, 
          A_SENSITIVE_PARTNER => NULL,  
          A_COR_AD_LINE_1     => i.ADDRESS_CORR,      
          A_COR_CITY_CODE     => i.ZIP_CODE_CORR,      
          A_COR_CITY_NAME     => i.CITY_NAME_CORR,      
          A_EMAIL_FV_1        => NULL,                   
          A_EMAIL_FV_2        => NULL,   
          A_EMAIL_FV_3        => NULL,            
          A_EMAIL_FV_4        => NULL,            
          A_EMAIL_FV_5        => NULL,           
          A_EMAIL_MID_1       => NULL,          
          A_EMAIL_MID_2       => NULL,   
          A_EMAIL_SXO         => NULL);
      IF I.IN_MODIFIED_FILE = 'Y' THEN
          ADD_NAV_CUST(
              A_CODE              => i.CODE,
              A_NAME              => i.NAME,
              A_AD_LINE_1         => i.address, 
              A_CITY_CODE         => i.zip_code,
              A_CITY_NAME         => i.CITY_NAME,
              A_VAT_REG_NUM       => NVL(i.VAT_REG_NUM,'0000000000'),
              A_PHONE             => i.phone_number, 
              A_TYPE              => v_cus_types_data(I.SOURCE_TABLE).NAV_TYPE,
              A_ACCOUNT           => v_cus_types_data(I.SOURCE_TABLE).ACC_NO.ACC_NO,
              A_CLEAR_TYPE        => v_cus_types_data(I.SOURCE_TABLE).CLEAR_TYPE, 
              A_BANK_ACCOUNT_NUM  => i.account_number,
              A_TRANSFER_DESC     => NULL,
              A_ACC_CODE          => NULL, 
              A_ACC_NAME          => NULL,
              A_ACC_AD_LINE_1     => NULL,
              A_ACC_CITY_CODE     => NULL,
              A_ACC_CITY_NAME     => NULL, 
              A_ACC_VAT_REG_NUM   => NULL,
              A_ACC_PHONE         => NULL,
              A_ACC_TYPE          => NULL, 
              A_ACC_ACCOUNT       => NULL,
              A_ACC_CLEAR_TYPE    => NULL,
              A_ACC_BANK_ACCOUNT_NUM  => NULL, 
              A_ACC_TRANSFER_DESC => NULL,
              A_TASK_ID           => a_task_id, 
              A_E_INV_PERMISSION  => v_cus_types_data(I.SOURCE_TABLE).E_INV_PERMISSION, 
              A_SENSITIVE_PARTNER => NULL,  
              A_COR_AD_LINE_1     => i.ADDRESS_CORR,      
              A_COR_CITY_CODE     => i.ZIP_CODE_CORR,      
              A_COR_CITY_NAME     => i.CITY_NAME_CORR,      
              A_EMAIL_FV_1        => NULL,                   
              A_EMAIL_FV_2        => NULL,   
              A_EMAIL_FV_3        => NULL,            
              A_EMAIL_FV_4        => NULL,            
              A_EMAIL_FV_5        => NULL,           
              A_EMAIL_MID_1       => NULL,          
              A_EMAIL_MID_2       => NULL,   
              A_EMAIL_SXO         => NULL);      
      END IF;          
    END LOOP;                
   
EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_PACP');
END Pbe_Account_Contract_Pairs;

-- rozliczenia Gryf
PROCEDURE Pbe_Reimb ( a_input_date      IN       DATE)
IS
e_wait_eoutoftime EXCEPTION;
PRAGMA EXCEPTION_INIT(e_wait_eoutoftime, -30006);
  v_account_head     r_acc;
  v_account_line     r_acc;
  v_inv_type         VARCHAR2(10);
  v_prd_type         APP_SWP.NAV_INV.PRODUCT%TYPE;           
  v_id               APP_PBE.E_REIMBURSEMENTS.ID%TYPE;
BEGIN
   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','Pbe_Reimb', a_input_date);
   v_account_head      := GET_ACCOUNT('PBE_TRAINING_INSTITUTION_ACCOUNT'); 
   v_account_line      := GET_ACCOUNT('PBE_RMB_ACCOUNT'); 
   v_inv_type       := 'NUPB_RMB';
   
   FOR K IN (select 
                     TIT.CODE                    TRAINING_INSTITUTION_CODE,
                     R.ID                        RMB_ID,
                     R.SXO_TI_AMOUNT_DUE_TOTAL   AMOUNT,  
                     R.RECON_DATE                RECON_DATE,
                     PPRD.PRD_TYPE               PRD_TYPE,
                     R.REIMBURSEMENT_DATE        REIMBURSEMENT_DATE,
                     PRG.PROGRAM_CODE            GRANT_PROGRAM_CODE ,
                     R.TI_REIMB_ACCOUNT_NUMBER   TI_REIMB_ACCOUNT_NUMBER,
                     INV.DOC_NUM                 DOC_NUM 
              from   APP_PBE.E_REIMBURSEMENTS R
                     LEFT JOIN APP_PBE.TI_TRAINING_INSTANCES TI ON TI.ID =  R.TI_TR_INST_ID 
                     JOIN APP_PBE.GRANT_PROGRAMS   PRG ON PRG.ID = TI.GRANT_PROGRAM_ID
                     LEFT JOIN APP_PBE.TI_TRAININGS T ON T.ID = TI.TRAINING_ID
                     LEFT JOIN APP_PBE.TRAINING_INSTITUTIONS TIT ON TIT.ID = T.TRAINING_INSTITUTION_ID
                     LEFT JOIN APP_PBE.PBE_PRODUCT_INSTANCE_POOL_USES INSTU ON INSTU.TRAINING_INSTANCE_ID = TI.ID
                     LEFT JOIN APP_PBE.PBE_PRODUCT_INSTANCE_POOLS     INSTP ON INSTP.ID = INSTU.PRODUCT_INSTANCE_POOL_ID 
                     LEFT JOIN APP_PBE.PBE_PRODUCTS                   PPRD  ON PPRD.PRD_ID = INSTP.PRD_ID
                     LEFT JOIN (  SELECT A.E_RMBS_ID, MAX(A.DOCUMENT_NUMBER) DOC_NUM
                                  FROM   APP_PBE.E_RMBS_ATTACHMENTS A
                                  WHERE  A.ATTACH_TYPE = 'INVOICE'
                                  GROUP BY A.E_RMBS_ID) INV            ON INV.E_RMBS_ID= R.ID
              WHERE  R.NAV_EXPORTED = 'N'
              AND    TRUNC(R.RECON_DATE) = a_input_date
              AND    R.STATUS_ID = 'REIMB' 
              GROUP BY  TIT.CODE                    ,
                        R.ID                        ,
                        R.SXO_TI_AMOUNT_DUE_TOTAL   ,
                        R.RECON_DATE                ,
                        PPRD.PRD_TYPE               ,
                        R.REIMBURSEMENT_DATE        ,
                        PRG.PROGRAM_CODE            ,
                        R.TI_REIMB_ACCOUNT_NUMBER   ,
                        INV.DOC_NUM                 )
  LOOP
  
    BEGIN
      BEGIN
        SELECT ID
        INTO   v_id
        FROM   APP_PBE.E_REIMBURSEMENTS R
        WHERE  R.ID = K.RMB_ID
        FOR UPDATE WAIT 2;
      EXCEPTION
        WHEN e_wait_eoutoftime THEN
          PK_ERROR.RAISE_ERROR(-20001, 'B³ad eksportu rozliczeñ PB: Nie uda³o siê zablokowaæ rozliczenia o id '|| K.RMB_ID||'.');
      END;

      -- Jeœli wartoœæ rozliczenia to 0, to zmieniamy flagê na S (Skipped) 
      IF K.AMOUNT = 0 THEN
        UPDATE APP_PBE.E_REIMBURSEMENTS R
        SET    R.NAV_EXPORT_DATE = SYSDATE,
               R.NAV_EXPORTED    = 'S'
        WHERE  R.ID = K.RMB_ID;
      
      ELSE 
          
          IF K.DOC_NUM IS NULL THEN
            PK_ERROR.RAISE_ERROR(-20001, 'B³ad eksportu rozliczeñ PB: Dla rozliczenia o id '|| K.RMB_ID||' nie znaleziono numeru faktury instytucji szkoleniowej');
          END IF; 
          
          IF K.TRAINING_INSTITUTION_CODE IS NULL THEN  PK_ERROR.RAISE_ERROR(-20001, 'B³ad eksportu rozliczeñ PB: Dla rozliczenia o id '|| K.RMB_ID||' brak ustalonej wartoœci w polu TRAINING_INSTITUTION_CODE'); END IF; 
          IF K.AMOUNT                    IS NULL THEN  PK_ERROR.RAISE_ERROR(-20001, 'B³ad eksportu rozliczeñ PB: Dla rozliczenia o id '|| K.RMB_ID||' brak ustalonej wartoœci w polu AMOUNT                   '); END IF; 
          IF K.RECON_DATE                IS NULL THEN  PK_ERROR.RAISE_ERROR(-20001, 'B³ad eksportu rozliczeñ PB: Dla rozliczenia o id '|| K.RMB_ID||' brak ustalonej wartoœci w polu RECON_DATE               '); END IF; 
          IF K.PRD_TYPE                  IS NULL THEN  PK_ERROR.RAISE_ERROR(-20001, 'B³ad eksportu rozliczeñ PB: Dla rozliczenia o id '|| K.RMB_ID||' brak ustalonej wartoœci w polu PRD_TYPE                 '); END IF; 
          IF K.REIMBURSEMENT_DATE        IS NULL THEN  PK_ERROR.RAISE_ERROR(-20001, 'B³ad eksportu rozliczeñ PB: Dla rozliczenia o id '|| K.RMB_ID||' brak ustalonej wartoœci w polu REIMBURSEMENT_DATE       '); END IF; 
          IF K.GRANT_PROGRAM_CODE        IS NULL THEN  PK_ERROR.RAISE_ERROR(-20001, 'B³ad eksportu rozliczeñ PB: Dla rozliczenia o id '|| K.RMB_ID||' brak ustalonej wartoœci w polu GRANT_PROGRAM_CODE       '); END IF; 
          IF K.TI_REIMB_ACCOUNT_NUMBER   IS NULL THEN  PK_ERROR.RAISE_ERROR(-20001, 'B³ad eksportu rozliczeñ PB: Dla rozliczenia o id '|| K.RMB_ID||' brak ustalonej wartoœci w polu TI_REIMB_ACCOUNT_NUMBER  '); END IF; 
          
          ADD_NAV_INV(A_NUM_SEQUENCE   => GET_INV_SEQUENCE(v_inv_type),
                      A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(v_inv_type), -- NULL
                      A_DOC_TYPE       => GET_DOC_TYPE(v_inv_type), -- NULL
                      A_PAYMENT_TYPE   => 'PD',
                      A_ACCOUNT        => v_account_head.acc_no,
                      A_MPK            => NULL,
                      A_TAX            => NULL, 
                      A_PRODUCT        => NULL,
                      A_BOOK_DATE      => K.RECON_DATE,
                      A_AMOUNT         => nvl(K.AMOUNT,0) * (-1),
                      A_CODE           => K.TRAINING_INSTITUTION_CODE,
                      A_DOC_NUMBER     => K.RMB_ID,
                      A_VAT_RATE       => NULL,
                      A_ORD_ID         => NULL,
                      A_PROFORMA_NO    => NULL,
                      A_INVOICE_DATE   => K.RECON_DATE,
                      A_PAYMENT_DATE   => K.REIMBURSEMENT_DATE,
                      A_PROJECT        => NULL,
                      A_TGKV_CODE      => NULL, -- 'NP U'
                      A_VAT_DATE               => NULL, 
                      A_SALE_DATE              => K.RECON_DATE,
                      A_ACCOUNT_NUMBER         => K.TI_REIMB_ACCOUNT_NUMBER,
                      A_REFERENCE_NUMBER       => K.DOC_NUM,
                      A_OUT_PAYMENT_CONF_CODE  => 'DOCUMENT',
                      A_INVOICE_LINE_NAME      => NULL
                      );
          
          ADD_NAV_INV(A_NUM_SEQUENCE   => GET_INV_SEQUENCE(v_inv_type),
                      A_DOC_TYPE_NAV   => GET_DOC_TYPE_NAV(v_inv_type), -- NULL
                      A_DOC_TYPE       => GET_DOC_TYPE(v_inv_type), -- NULL
                      A_PAYMENT_TYPE   => 'PD',
                      A_ACCOUNT        => v_account_line.acc_no,
                      A_MPK            => NULL,
                      A_TAX            => NULL, 
                      A_PRODUCT        => K.PRD_TYPE,
                      A_BOOK_DATE      => K.RECON_DATE,
                      A_AMOUNT         => nvl(K.AMOUNT,0),
                      A_CODE           => K.TRAINING_INSTITUTION_CODE,
                      A_DOC_NUMBER     => K.RMB_ID,
                      A_VAT_RATE       => 'NP',
                      A_ORD_ID         => NULL,
                      A_PROFORMA_NO    => NULL,
                      A_INVOICE_DATE   => K.RECON_DATE,
                      A_PAYMENT_DATE   => K.REIMBURSEMENT_DATE,
                      A_PROJECT        => K.GRANT_PROGRAM_CODE,
                      A_TGKV_CODE      =>  'NP U',
                      A_VAT_DATE               => NULL, 
                      A_SALE_DATE              => K.RECON_DATE,
                      A_ACCOUNT_NUMBER         => K.TI_REIMB_ACCOUNT_NUMBER,
                      A_REFERENCE_NUMBER       => K.DOC_NUM,
                      A_OUT_PAYMENT_CONF_CODE  => 'DOCUMENT',
                      A_INVOICE_LINE_NAME      => NULL
                      );

        UPDATE APP_PBE.E_REIMBURSEMENTS R
        SET    R.NAV_EXPORT_DATE = SYSDATE,
               R.NAV_EXPORTED    = 'Y'
        WHERE  R.ID = K.RMB_ID;

      END IF;
    
    EXCEPTION
      WHEN OTHERS THEN
        PK_ERROR.RAISE_ERROR(sqlcode, 'Pbe_Reimb (RMB:'|| K.RMB_ID ||')'||sqlerrm );
    END;
                          
  END LOOP;                        

EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'Pbe_Reimb');
END Pbe_Reimb;

-- Noty uznaniowe Gryf
PROCEDURE Pbe_Rmb_Note (a_input_date  IN DATE)
IS
e_wait_eoutoftime EXCEPTION;
PRAGMA EXCEPTION_INIT(e_wait_eoutoftime, -30006);
  v_account_head     r_acc;
  v_account_line     r_acc;
  v_inv_type         VARCHAR2(4);
  v_prev_inv_id      APP_FIN.INVOICES.ID%TYPE;
BEGIN
   PK_AUDIT.AUDIT_MODULE('PK_MOD_SWAP_NAV','Pbe_Rmb_Note', a_input_date);
   v_account_head      := GET_ACCOUNT('PBE_USER_ACCOUNT'); 
   v_account_line      := GET_ACCOUNT('PBE_RMB_ACCOUNT'); 
   v_inv_type       := 'NUPB';
   
   FOR K IN (SELECT  I.INVOICE_DATE           INVOICE_DATE,
                     PP.PRD_TYPE              PRD_TYPE,
                     I.GROSS_AMOUNT           GROSS_AMOUNT,
                     C.CODE                   IND_ENT_CODE,
                     I.INVOICE_NUMBER         INVOICE_NUMBER,
                     ER.REIMBURSEMENT_DATE    REIMBURSEMENT_DATE, 
                     PRG.PROGRAM_CODE         GRANT_PROGRAM_CODE ,
                     IL.NAME                  INVOICE_LINE_NAME,
                     ER.TYPE_ID               RMB_TYPE_ID, 
                     I.ID                     INV_ID      
              FROM   APP_FIN.INVOICES I
                     JOIN APP_FIN.INVOICE_LINES IL ON IL.INV_ID = I.ID AND IL.SOURCE_TYPE = 'REMPB'
                     JOIN APP_PBE.E_REIMBURSEMENTS er ON ER.ID = IL.SOURCE AND ER.TYPE_ID = 'TI_INST'
                     LEFT JOIN APP_PBE.TI_TRAINING_INSTANCES TI ON (ER.TI_TR_INST_ID=TI.ID )
                     LEFT JOIN APP_PBE.PBE_PRODUCT_INSTANCE_POOL_USES IPU ON IPU.TRAINING_INSTANCE_ID = TI.ID -- <- mog¹ istnieæ dwie
                     LEFT JOIN APP_PBE.PBE_PRODUCT_INSTANCE_POOLS PIP ON IPU.PRODUCT_INSTANCE_POOL_ID=PIP.ID 
                     LEFT JOIN APP_PBE.PBE_PRODUCTS PP ON (PIP.PRD_ID = PP.PRD_ID)  
                     LEFT JOIN APP_PBE.ORDERS O ON PIP.ORDER_ID = O.ID
                     LEFT JOIN APP_PBE.GRANT_PROGRAMS PRG ON PRG.ID = O.GRANT_PROGRAM_ID
                     LEFT JOIN APP_PBE.CONTRACTS C ON O.CONTRACT_ID = C.ID
                     LEFT JOIN APP_PBE.ENTERPRISES ENT ON ENT.ID = C.ENTERPRISE_ID
                     LEFT JOIN APP_PBE.INDIVIDUALS IND ON IND.ID = C.INDIVIDUAL_ID
              WHERE  I.TYPE = 'NUPB'
              AND    TRUNC(I.INVOICE_DATE) = TRUNC(a_input_date)
              GROUP BY I.INVOICE_DATE           ,   
                       PP.PRD_TYPE              ,
                       I.GROSS_AMOUNT           ,
                       C.CODE, --NVL(IND.CODE, ENT.CODE)  ,
                       I.INVOICE_NUMBER         ,
                       ER.REIMBURSEMENT_DATE    ,
                       PRG.PROGRAM_CODE         ,
                       IL.NAME                  ,
                       ER.TYPE_ID               ,
                       I.ID                     
              UNION
              SELECT I.INVOICE_DATE           INVOICE_DATE,
                     PP.PRD_TYPE              PRD_TYPE,
                     I.GROSS_AMOUNT           GROSS_AMOUNT,
                     C.CODE                   IND_ENT_CODE,
                     I.INVOICE_NUMBER         INVOICE_NUMBER,
                     ER.REIMBURSEMENT_DATE    REIMBURSEMENT_DATE, 
                     PRG.PROGRAM_CODE         GRANT_PROGRAM_CODE ,
                     IL.NAME                  INVOICE_LINE_NAME,
                     ER.TYPE_ID               RMB_TYPE_ID, 
                     I.ID                     INV_ID                       
              FROM   APP_FIN.INVOICES I
                     JOIN APP_FIN.INVOICE_LINES IL ON IL.INV_ID = I.ID AND IL.SOURCE_TYPE = 'REMPB'
                     JOIN APP_PBE.E_REIMBURSEMENTS ER ON ER.ID = IL.SOURCE AND ER.TYPE_ID NOT IN ('TI_INST')
                     LEFT JOIN APP_PBE.PBE_PRODUCT_INSTANCE_POOLS PIP ON ER.PRODUCT_INSTANCE_POOL_ID = PIP.ID 
                     LEFT JOIN APP_PBE.PBE_PRODUCTS PP ON (PIP.PRD_ID = PP.PRD_ID)  
                     LEFT JOIN APP_PBE.ORDERS O ON PIP.ORDER_ID = O.ID
                     LEFT JOIN APP_PBE.GRANT_PROGRAMS PRG ON PRG.ID = O.GRANT_PROGRAM_ID
                     LEFT JOIN APP_PBE.CONTRACTS C ON O.CONTRACT_ID = C.ID
                     LEFT JOIN APP_PBE.ENTERPRISES ENT ON ENT.ID = C.ENTERPRISE_ID
                     LEFT JOIN APP_PBE.INDIVIDUALS IND ON IND.ID = C.INDIVIDUAL_ID
              WHERE  I.TYPE = 'NUPB'
              AND    TRUNC(I.INVOICE_DATE) = TRUNC(a_input_date)
              GROUP BY  I.INVOICE_DATE           ,      
                        PP.PRD_TYPE              ,
                        I.GROSS_AMOUNT           ,
                        C.CODE,
                        I.INVOICE_NUMBER         ,
                        ER.REIMBURSEMENT_DATE    ,
                        PRG.PROGRAM_CODE         ,
                        IL.NAME                  ,
                        ER.TYPE_ID               ,
                        I.ID
              ORDER BY INV_ID
   )
  LOOP
  
    BEGIN
      
      IF v_prev_inv_id = K.INV_ID THEN
        PK_ERROR.RAISE_ERROR(-20001, 'B³ad eksportu Not uznaniowych PB: Faktura o id :  '|| K.INV_ID ||' zawiera dwie linie' ); 
      END IF;
      
      v_prev_inv_id := K.INV_ID;
    
      t$invoice.set_status (K.INV_ID, 'E');

      IF K.INVOICE_DATE       IS NULL THEN  PK_ERROR.RAISE_ERROR(-20001, 'B³ad eksportu Not uznaniowych PB: Dla faktury o id  '|| K.INV_ID||' brak ustalonej wartoœci w polu INVOICE_DATE      '); END IF; 
      IF K.PRD_TYPE           IS NULL THEN  PK_ERROR.RAISE_ERROR(-20001, 'B³ad eksportu Not uznaniowych PB: Dla faktury o id  '|| K.INV_ID||' brak ustalonej wartoœci w polu PRD_TYPE          '); END IF; 
      IF K.GROSS_AMOUNT       IS NULL THEN  PK_ERROR.RAISE_ERROR(-20001, 'B³ad eksportu Not uznaniowych PB: Dla faktury o id  '|| K.INV_ID||' brak ustalonej wartoœci w polu GROSS_AMOUNT      '); END IF; 
      IF K.IND_ENT_CODE       IS NULL THEN  PK_ERROR.RAISE_ERROR(-20001, 'B³ad eksportu Not uznaniowych PB: Dla faktury o id  '|| K.INV_ID||' brak ustalonej wartoœci w polu IND_ENT_CODE      '); END IF; 
      IF K.INVOICE_NUMBER     IS NULL THEN  PK_ERROR.RAISE_ERROR(-20001, 'B³ad eksportu Not uznaniowych PB: Dla faktury o id  '|| K.INV_ID||' brak ustalonej wartoœci w polu INVOICE_NUMBER    '); END IF; 
      IF K.REIMBURSEMENT_DATE IS NULL THEN  PK_ERROR.RAISE_ERROR(-20001, 'B³ad eksportu Not uznaniowych PB: Dla faktury o id  '|| K.INV_ID||' brak ustalonej wartoœci w polu REIMBURSEMENT_DATE'); END IF; 
      IF K.GRANT_PROGRAM_CODE IS NULL THEN  PK_ERROR.RAISE_ERROR(-20001, 'B³ad eksportu Not uznaniowych PB: Dla faktury o id  '|| K.INV_ID||' brak ustalonej wartoœci w polu GRANT_PROGRAM_CODE'); END IF; 
      IF K.INVOICE_LINE_NAME  IS NULL THEN  PK_ERROR.RAISE_ERROR(-20001, 'B³ad eksportu Not uznaniowych PB: Dla faktury o id  '|| K.INV_ID||' brak ustalonej wartoœci w polu INVOICE_LINE_NAME '); END IF; 
      IF K.RMB_TYPE_ID        IS NULL THEN  PK_ERROR.RAISE_ERROR(-20001, 'B³ad eksportu Not uznaniowych PB: Dla faktury o id  '|| K.INV_ID||' brak ustalonej wartoœci w polu RMB_TYPE_ID       '); END IF; 

      IF K.RMB_TYPE_ID NOT IN  ('URSVD_POOL', 'RET_POOL' , 'TI_INST')       THEN  
        PK_ERROR.RAISE_ERROR(-20001, 'B³ad eksportu Not uznaniowych PB: Nieobs³ugiwany typ rozliczenia:  '|| K.RMB_TYPE_ID ); 
      END IF;
       
      ADD_NAV_INV(A_NUM_SEQUENCE           => GET_INV_SEQUENCE(v_inv_type),
                  A_DOC_TYPE_NAV           => GET_DOC_TYPE_NAV(v_inv_type), -- NULL
                  A_DOC_TYPE               => GET_DOC_TYPE(v_inv_type), -- NULL
                  A_PAYMENT_TYPE           => 'PD',
                  A_ACCOUNT                => v_account_head.acc_no,
                  A_MPK                    => NULL,
                  A_TAX                    => NULL, 
                  A_PRODUCT                => NULL,
                  A_BOOK_DATE              => K.INVOICE_DATE,
                  A_AMOUNT                 => nvl(K.GROSS_AMOUNT,0) * (-1),
                  A_CODE                   => K.IND_ENT_CODE,
                  A_DOC_NUMBER             => K.INVOICE_NUMBER,
                  A_VAT_RATE               => NULL,
                  A_ORD_ID                 => NULL,
                  A_PROFORMA_NO            => NULL,
                  A_INVOICE_DATE           => K.INVOICE_DATE,
                  A_PAYMENT_DATE           => K.REIMBURSEMENT_DATE,
                  A_PROJECT                => NULL,
                  A_TGKV_CODE              => NULL, -- 'NP U'
                  A_VAT_DATE               => NULL, 
                  A_SALE_DATE              => K.INVOICE_DATE,
                  A_ACCOUNT_NUMBER         => NULL,
                  A_REFERENCE_NUMBER       => NULL,
                  A_OUT_PAYMENT_CONF_CODE  => 'USER',
                  A_INVOICE_LINE_NAME      => K.INVOICE_LINE_NAME
                  );
          
      ADD_NAV_INV(A_NUM_SEQUENCE           => GET_INV_SEQUENCE(v_inv_type),
                  A_DOC_TYPE_NAV           => GET_DOC_TYPE_NAV(v_inv_type), -- NULL
                  A_DOC_TYPE               => GET_DOC_TYPE(v_inv_type), -- NULL
                  A_PAYMENT_TYPE           => 'PD',
                  A_ACCOUNT                => v_account_line.acc_no,
                  A_MPK                    => NULL,
                  A_TAX                    => NULL, 
                  A_PRODUCT                => K.PRD_TYPE,
                  A_BOOK_DATE              => K.INVOICE_DATE,
                  A_AMOUNT                 => nvl(K.GROSS_AMOUNT,0),
                  A_CODE                   => K.IND_ENT_CODE,
                  A_DOC_NUMBER             => K.INVOICE_NUMBER,
                  A_VAT_RATE               => 'NP',
                  A_ORD_ID                 => NULL,
                  A_PROFORMA_NO            => NULL,
                  A_INVOICE_DATE           => K.INVOICE_DATE,
                  A_PAYMENT_DATE           => K.REIMBURSEMENT_DATE,
                  A_PROJECT                => K.GRANT_PROGRAM_CODE,
                  A_TGKV_CODE              => 'NP U',
                  A_VAT_DATE               => NULL, 
                  A_SALE_DATE              => K.INVOICE_DATE,
                  A_ACCOUNT_NUMBER         => NULL,
                  A_REFERENCE_NUMBER       => NULL,
                  A_OUT_PAYMENT_CONF_CODE  => 'USER',
                  A_INVOICE_LINE_NAME      => K.INVOICE_LINE_NAME
                  );
     EXCEPTION
       WHEN OTHERS THEN
         PK_ERROR.RAISE_ERROR(sqlcode, 'Pbe_Rmb_Note (inv:'|| K.INV_ID ||')'||sqlerrm );
     END;

  END LOOP;                        

EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SNW_PBRMBNOTE');
END Pbe_Rmb_Note;

FUNCTION GET_UNIT_NAV_CODE(a_invoice_number IN VARCHAR2,
                           a_unit_eagle_code IN VARCHAR2) RETURN VARCHAR2 IS
BEGIN
  IF a_unit_eagle_code = 'SZT' THEN 
    RETURN 'SZT';
  ELSE
    PK_ERROR.RAISE_ERROR(-20008, 'Nie uda³o siê ustaliæ kodu jednostki miary w Navision. Kod jednostki w Eagle: '||a_unit_eagle_code||'. Faktura o nr: '||a_invoice_number);
  END IF;  
EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'SWP_NAV_GUNC');
END;

-- zwraca wartoœæ jednostkow¹ oraz iloœæ, która bêdzie eksportowana do Navision
PROCEDURE GET_EXP_NAV_UP_AND_Q  (a_inv_id IN NUMBER,
                                 a_doc_type IN VARCHAR2, -- C -orygina³, K - korekta
                                 a_pos_num  IN NUMBER,
                                 a_line_quantity_printed NUMBER , -- Linia D dla korekt, dla orygina³ow - jedyna linia
                                 a_line_unit VARCHAR2, -- Linia D dla korekt, dla orygina³ow - jedyna linia
                                 a_line_netto_amount NUMBER, -- Linia D dla korekt, dla orygina³ow - jedyna linia
                                 a_line_unit_price NUMBER, -- Linia D dla korekt, dla orygina³ow - jedyna linia
                                 p_unit_price OUT NUMBER,
                                 p_quantity   OUT NUMBER) IS
  v_line_unit_price_prev APP_FIN.INVOICE_LINES.UNIT_PRICE%TYPE;       
  v_line_quantity_prev   APP_FIN.INVOICE_LINES.QUANTITY%TYPE;
BEGIN       
  -- orygina³y
  IF a_doc_type = 'C' THEN
    p_quantity := a_line_quantity_printed;
    IF a_line_unit = '%' THEN
      p_unit_price := a_line_netto_amount;
    ELSE
      if a_line_unit_price is not null then
        p_unit_price := a_line_unit_price;
      -- je¿eli puste (dla not)
      else
        p_unit_price := a_line_netto_amount;
        p_quantity   := 1;
      end if;
    END IF; 
  -- korekty
  ELSE
    IF a_line_unit = '%' THEN
      p_quantity   := a_line_quantity_printed;
      p_unit_price := -a_line_netto_amount;
    ELSE
      SELECT IL.UNIT_PRICE,
             IL.QUANTITY
      INTO   v_line_unit_price_prev,
             v_line_quantity_prev
      FROM   APP_FIN.INVOICE_LINES IL
      WHERE  IL.INV_ID = a_inv_id
      AND    IL.POS_NUM = a_pos_num
      AND    IL.POS_TYPE = 'I';
      -- korygowana iloœæ siê iloœæ
      IF a_line_quantity_printed!=0 THEN
        -- korygowana iloœæ i cena jednostkowa => ERROR
        IF NVL(v_line_unit_price_prev,0) != NVL(a_line_unit_price,0) THEN
          PK_ERROR.RAISE_ERROR(-20001, 'NAV_GAC: Faktura id='||a_inv_id||' pozycja:'||a_pos_num||', Jednoczesna korekta ceny i iloœci');
        END IF;
        -- korekty not gafa - dla do³adowañ kart (ró¿ne kwoty daj¹ sumaryczn¹ wartoœæ)
        IF v_line_unit_price_prev IS NULL THEN
          p_unit_price := -a_line_netto_amount;
          p_quantity   := 1;
        ELSE
          p_unit_price := -v_line_unit_price_prev * (a_line_quantity_printed/abs(a_line_quantity_printed));
          p_quantity   := abs(a_line_quantity_printed);
        END IF;
      -- korygowana wy³¹cznie cena jednostkowa
      ELSE
        IF v_line_unit_price_prev IS NULL THEN
          p_unit_price := -a_line_netto_amount;
          p_quantity   := 1;
        ELSE
          p_quantity   := v_line_quantity_prev;
          p_unit_price := (v_line_unit_price_prev - a_line_unit_price);
        END IF;
      END IF;
    END IF; 
  END IF;

END;

END pk_mod_swap_nav;
