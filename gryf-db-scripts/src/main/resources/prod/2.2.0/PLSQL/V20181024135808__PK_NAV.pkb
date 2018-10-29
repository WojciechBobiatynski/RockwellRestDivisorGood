CREATE OR REPLACE PACKAGE BODY EAGLE.pk_nav
IS

PROCEDURE SEND_PAY_MORE_THEN_PROF(a_order_id IN NUMBER,
                                  a_doc_number      IN VARCHAR2,
                                  a_transfer_amount IN NUMBER,
                                  a_proforma_amount IN NUMBER,
                                  a_eror_emails     IN VARCHAR2,
                                  a_error_group     IN VARCHAR2
                                  );


PROCEDURE NAV_INVOICES_PB (a_delete_prev_items VARCHAR2); -- Czy usuwaæ wiersze z APP_FIN.NAV_INVOICES (Y / N)

PROCEDURE NAV_INVOICES
IS
  v_period              NUMBER(10) := 200;
  v_inv_cnt             NUMBER(10);
  v_nota_cnt            NUMBER(10);
  v_invoice_no          app_fin.nav_invoices.INVOICE_NO%TYPE;
  v_invoice_date        app_fin.nav_invoices.INVOICE_DATE%TYPE;
  v_gross_commission    app_fin.nav_invoices.GROSS_COMMISION%TYPE;
  v_nota_no             app_fin.nav_invoices.NOTA_NO%TYPE;
  v_nota_date           app_fin.nav_invoices.NOTA_DATE%TYPE;
  v_total_fv            app_fin.nav_invoices.TOTAL_FV%TYPE;  
  v_posting_group       app_fin.nav_invoices.POSTING_GROUP%TYPE;   
BEGIN

   PK_AUDIT.AUDIT_MODULE('PK_NAV','NAV_INVOICES',NULL);

    DELETE FROM APP_FIN.NAV_INVOICES;
    
    FOR l_prof IN (
            SELECT prof_o.ord_id, prof_o.source_type, prof_o.proforma_no,
                   prof_o.proforma_date, prof_o.proforma_amount, prof_o.code,
                   prof_o.company_code_temp, prof_o.cor_name, prof_o.cor_ad_line_1,
                   prof_o.cor_ad_line_4,
                   prof_o.inv_type
              FROM (SELECT prof.ord_id, prof.source_type, prof.proforma_no,
                           prof.proforma_date, prof.proforma_amount, cny.code,
                           inv_web.code company_code_temp, prof.cor_name,
                           prof.cor_ad_line_1, prof.cor_ad_line_4, prof.inv_type,
                           (SELECT SUM (iln3.gross_amount) FROM (
                                SELECT distinct inv2.invoice_number, inv2.gross_amount,
                                       iln2.source, iln2.source_type
                                  FROM app_fin.invoices inv2, app_fin.invoice_lines iln2
                                 WHERE inv2.ID = iln2.inv_id
                                   AND inv2.TYPE NOT IN ('P', 'PV')) iln3
                                   WHERE iln3.source_type = prof.source_type
                                   AND iln3.SOURCE = prof.ord_id) sum_documents,
                           (SELECT SUM (t.amount)
                              FROM app_fin.nav_mc_transfers t
                             WHERE t.doc_number = prof.proforma_no) sum_payments
                      FROM (SELECT   il.SOURCE ord_id, il.source_type, i.ID inv_id,
                                     i.cus_id cus_id, i.invoice_number proforma_no,
                                     i.invoice_date proforma_date,
                                     i.gross_amount proforma_amount, i.cor_name cor_name,
                                     i.cor_ad_line_1 cor_ad_line_1,
                                     i.cor_ad_line_4 cor_ad_line_4,
                                     i.type inv_type
                                FROM app_fin.invoices i,
                                     app_fin.invoice_lines il,
                                     app_emi.orders o
                               WHERE i.ID = il.inv_id
                                 AND i.TYPE = 'P'
                                 AND o.ID = il.SOURCE
                                 AND il.source_type IN ('ORD')
                                 AND o.status NOT IN ('C', 'R')
                                 AND TRUNC (o.order_date) > TRUNC (SYSDATE) - v_period
                                 --AND NVL (o.payment, 'N') != 'Y'
                            GROUP BY il.SOURCE,
                                     il.source_type,
                                     i.ID,
                                     i.cus_id,
                                     i.invoice_number,
                                     i.invoice_date,
                                     i.gross_amount,
                                     i.cor_name,
                                     i.cor_ad_line_1,
                                     i.cor_ad_line_4,
                                     i.type
                            UNION
                            SELECT   il.SOURCE ord_id, il.source_type, i.ID inv_id,
                                     i.cus_id cus_id, i.invoice_number proforma_no,
                                     i.invoice_date proforma_date,
                                     i.gross_amount proforma_amount, i.cor_name cor_name,
                                     i.cor_ad_line_1 cor_ad_line_1,
                                     i.cor_ad_line_4 cor_ad_line_4,
                                     i.type inv_type
                                FROM app_fin.invoices i,
                                     app_fin.invoice_lines il,
                                     app_tsk.tasks t
                               WHERE i.ID = il.inv_id
                                 AND i.TYPE = 'P'
                                 AND t.ID = il.SOURCE
                                 AND il.source_type IN ('TSK')
                                 AND t.status NOT IN ('C', 'P')
                                 AND TRUNC (t.create_date) > TRUNC (SYSDATE) - v_period
                            GROUP BY il.SOURCE,
                                     il.source_type,
                                     i.ID,
                                     i.cus_id,
                                     i.invoice_number,
                                     i.invoice_date,
                                     i.gross_amount,
                                     i.cor_name,
                                     i.cor_ad_line_1,
                                     i.cor_ad_line_4,
                                     i.type
                            UNION
                            SELECT   il.SOURCE ord_id, il.source_type, i.ID inv_id,
                                     i.cus_id cus_id, i.invoice_number proforma_no,
                                     i.invoice_date proforma_date,
                                     i.gross_amount proforma_amount, i.cor_name cor_name,
                                     i.cor_ad_line_1 cor_ad_line_1,
                                     i.cor_ad_line_4 cor_ad_line_4,
                                     i.type inv_type
                                FROM app_fin.invoices i,
                                     app_fin.invoice_lines il,
                                     app_vemi.orders o
                               WHERE i.ID = il.inv_id
                                 AND i.TYPE = 'PV'
                                 AND o.ID = il.SOURCE
                                 AND il.source_type IN ('ORDV')
                                 AND o.status <> 'C'
                                 --AND ((o.PAYMENT_TYPE != 'D' AND o.STATUS != 'R')
                                 --     OR 
                                 --     (o.PAYMENT_TYPE = 'D' AND o.STATUS = 'R' AND nvl(o.PAYMENT,'N') != 'Y')) 
                                 AND TRUNC (o.order_date) > TRUNC (SYSDATE) - v_period
                                 --AND NVL (o.payment, 'N') != 'Y'
                            GROUP BY il.SOURCE,
                                     il.source_type,
                                     i.ID,
                                     i.cus_id,
                                     i.invoice_number,
                                     i.invoice_date,
                                     i.gross_amount,
                                     i.cor_name,
                                     i.cor_ad_line_1,
                                     i.cor_ad_line_4,
                                     i.type
                            UNION
                            SELECT   il.SOURCE ord_id, il.source_type, i.ID inv_id,
                                     i.cus_id cus_id, i.invoice_number proforma_no,
                                     i.invoice_date proforma_date,
                                     i.gross_amount proforma_amount, i.cor_name cor_name,
                                     i.cor_ad_line_1 cor_ad_line_1,
                                     i.cor_ad_line_4 cor_ad_line_4,
                                     i.type inv_type
                                FROM app_fin.invoices i,
                                     app_fin.invoice_lines il,
                                     APP_FIN.INVOICE_PROPOSALS ip
                               WHERE i.ID = il.inv_id
                                 AND i.TYPE = EAGLE.PK_MOD_INV_PROPOSALS.GC_INV_TYPE_PG
                                 AND ip.ID = il.SOURCE
                                 AND il.source_type IN ('IP')
                                 AND ip.status <> 'C'
                                 --AND ((o.PAYMENT_TYPE != 'D' AND o.STATUS != 'R')
                                 --     OR 
                                 --     (o.PAYMENT_TYPE = 'D' AND o.STATUS = 'R' AND nvl(o.PAYMENT,'N') != 'Y')) 
                                 AND TRUNC (NVL(I.INVOICE_DATE,SYSDATE) /*o.order_date*/) > TRUNC (SYSDATE) - v_period
                                 --AND NVL (o.payment, 'N') != 'Y'
                            GROUP BY il.SOURCE,
                                     il.source_type,
                                     i.ID,
                                     i.cus_id,
                                     i.invoice_number,
                                     i.invoice_date,
                                     i.gross_amount,
                                     i.cor_name,
                                     i.cor_ad_line_1,
                                     i.cor_ad_line_4,
                                     i.type                                     
                                     ) prof,
                           app_emi.customers cny,
                           (SELECT invw.invoice_number, cusw.code
                              FROM app_web.invoices invw, app_web.customers cusw
                             WHERE invw.cus_id = cusw.ID) inv_web
                     WHERE prof.cus_id = cny.ID(+)
                       AND prof.proforma_no = inv_web.invoice_number(+)) prof_o
             WHERE ( prof_o.sum_documents IS NULL
                    OR NVL (prof_o.sum_documents, -1) > NVL (prof_o.sum_payments, 0)
                                                                /* niepokryte p³atnoœci */
                   )
   )
   LOOP
      CASE l_prof.inv_type 
        WHEN 'P'  THEN 
          v_posting_group := 'KLIENT';
        WHEN 'PV' THEN 
          v_posting_group := 'VIVABOX';
        WHEN EAGLE.PK_MOD_INV_PROPOSALS.GC_INV_TYPE_PG THEN 
          DECLARE
            v_invoicing_type APP_FIN.INVOICE_PROPOSALS.INVOICING_TYPE%TYPE;
          BEGIN
            SELECT IP.INVOICING_TYPE
            INTO   v_invoicing_type    
            FROM   APP_FIN.INVOICE_PROPOSALS ip
            WHERE  IP.ID = l_prof.ord_id;
            IF v_invoicing_type = PK_MOD_INV_PROPOSALS.GC_INVOICING_TYPE_SELECT THEN
              v_posting_group := 'SELECT';          
            ELSIF v_invoicing_type = PK_MOD_INV_PROPOSALS.GC_INVOICING_TYPE_SPS THEN
              v_posting_group := 'PLATFORMA';
            ELSE
              PK_ERROR.RAISE_ERROR(-20001, 'Nie uda³o siê ustaliæ v_posting_group dla IP '||l_prof.ord_id||' invoicing_type '||v_invoicing_type);          
            END IF;
          END;
        ELSE 
          v_posting_group := 'INNA';
      END CASE;    
      v_inv_cnt          := 0;
      v_invoice_no       := NULL;
      v_invoice_date     := NULL;
      v_gross_commission := NULL;
      FOR l_inv IN (SELECT   il.SOURCE ord_id, i.cus_id,
                             i.invoice_number invoice_no,
                             i.invoice_date invoice_date,
                             nvl(i.hand_made,'N') hand_made,
                             SUM (il.gross_amount) invoice_amount
                        FROM app_fin.invoices i,
                             app_fin.invoice_lines il
                       WHERE i.ID = il.inv_id
                         AND i.TYPE IN ('IC', 'ICV',EAGLE.PK_MOD_INV_PROPOSALS.GC_INV_TYPE_ICG)
                         AND il.source_type = l_prof.SOURCE_TYPE
                         AND il.source = l_prof.ord_id
                    GROUP BY il.SOURCE, i.cus_id, i.invoice_number,
                             i.invoice_date, nvl(i.hand_made,'N')
                        ORDER BY CONVERT_INVOICE_NUMBER(i.invoice_number))
       LOOP
       
          IF v_inv_cnt = 0 AND l_inv.hand_made = 'N' THEN
          
             v_invoice_no       := l_inv.invoice_no;
             v_invoice_date     := l_inv.invoice_date;
             v_gross_commission := l_inv.invoice_amount;

          ELSE

             ADD_NAV_INVOICE(A_INVOICE_ID      => GET_INVOICE_ID(l_prof.proforma_no, l_inv.invoice_no, NULL),
                             A_ORD_ID          => l_prof.ord_id,
                             A_PROFORMA_NO     => l_prof.proforma_no, 
                             A_PROFORMA_DATE   => l_prof.proforma_date,
                             A_PROFORMA_AMOUNT => l_inv.invoice_amount,
                             A_INVOICE_NO      => l_inv.invoice_no, 
                             A_INVOICE_DATE    => l_inv.invoice_date,
                             A_GROSS_COMMISION => l_inv.invoice_amount,
                             A_NOTA_NO         => NULL, 
                             A_NOTA_DATE       => NULL,
                             A_TOTAL_FV        => NULL,
                             A_COMPANY_CODE    => l_prof.code, 
                             A_COMPANY_CODE_TEMP => l_prof.company_code_temp,
                             A_COMPANY_NAME    => l_prof.cor_name,
                             A_ADR_INV_LINE_1  => l_prof.cor_ad_line_1, 
                             A_ADR_INV_LINE_2  => l_prof.cor_ad_line_4, 
                             A_POSTING_GROUP   => v_posting_group,
                             A_SOURCE_TYPE     => l_prof.source_type,               
                             A_PAYMENT_CONF_TYPE => 'INS');          
          
          END IF;
       
          v_inv_cnt := v_inv_cnt + 1;
       END LOOP;
       
       
      v_nota_cnt      := 0;
      v_nota_no       := NULL;
      v_nota_date     := NULL;
      v_total_fv      := NULL;
      FOR l_nota IN (SELECT   il.SOURCE ord_id, i.cus_id,
                             i.invoice_number nota_no,
                             i.invoice_date nota_date,
                             nvl(i.hand_made,'N') hand_made,
                             SUM (il.gross_amount) total_fv
                        FROM app_fin.invoices i,
                             app_fin.invoice_lines il
                       WHERE i.ID = il.inv_id
                         AND i.TYPE IN ('N', 'R', EAGLE.PK_MOD_INV_PROPOSALS.GC_INV_TYPE_NG, EAGLE.PK_MOD_INV_PROPOSALS.GC_INV_TYPE_RG)
                         --AND il.source_type = 'ORD'
                         AND il.source_type = l_prof.SOURCE_TYPE
                         AND il.source = l_prof.ord_id
                    GROUP BY il.SOURCE, i.cus_id, i.invoice_number,
                             i.invoice_date,  nvl(i.hand_made,'N')
                        ORDER BY CONVERT_INVOICE_NUMBER(i.invoice_number))
       LOOP
       
          IF v_nota_cnt = 0 AND l_nota.hand_made = 'N' THEN
          
             v_nota_no       := l_nota.nota_no;
             v_nota_date     := l_nota.nota_date;
             v_total_fv      := l_nota.total_fv;

          ELSE

             ADD_NAV_INVOICE(A_INVOICE_ID      => GET_INVOICE_ID(l_prof.proforma_no, NULL, l_nota.nota_no),
                             A_ORD_ID          => l_prof.ord_id,
                             A_PROFORMA_NO     => l_prof.proforma_no, 
                             A_PROFORMA_DATE   => l_prof.proforma_date,
                             A_PROFORMA_AMOUNT => l_nota.total_fv,
                             A_INVOICE_NO      => NULL, 
                             A_INVOICE_DATE    => NULL,
                             A_GROSS_COMMISION => NULL,
                             A_NOTA_NO         => l_nota.nota_no, 
                             A_NOTA_DATE       => l_nota.nota_date,
                             A_TOTAL_FV        => l_nota.total_fv,
                             A_COMPANY_CODE    => l_prof.code, 
                             A_COMPANY_CODE_TEMP => l_prof.company_code_temp,
                             A_COMPANY_NAME    => l_prof.cor_name,
                             A_ADR_INV_LINE_1  => l_prof.cor_ad_line_1, 
                             A_ADR_INV_LINE_2  => l_prof.cor_ad_line_4,
                             A_POSTING_GROUP   => v_posting_group,
                             A_SOURCE_TYPE     => l_prof.source_type,               
                             A_PAYMENT_CONF_TYPE => 'INS');          
          
          END IF;
       
          v_nota_cnt := v_nota_cnt + 1;
       END LOOP;

         ADD_NAV_INVOICE(A_INVOICE_ID      => CONVERT_INVOICE_NUMBER(l_prof.proforma_no),
                         A_ORD_ID          => l_prof.ord_id,
                         A_PROFORMA_NO     => l_prof.proforma_no, 
                         A_PROFORMA_DATE   => l_prof.proforma_date,
                         A_PROFORMA_AMOUNT => l_prof.proforma_amount,
                         A_INVOICE_NO      => v_invoice_no, 
                         A_INVOICE_DATE    => v_invoice_date,
                         A_GROSS_COMMISION => v_gross_commission,
                         A_NOTA_NO         => v_nota_no, 
                         A_NOTA_DATE       => v_nota_date,
                         A_TOTAL_FV        => v_total_fv,
                         A_COMPANY_CODE    => l_prof.code, 
                         A_COMPANY_CODE_TEMP => l_prof.company_code_temp,
                         A_COMPANY_NAME    => l_prof.cor_name,
                         A_ADR_INV_LINE_1  => l_prof.cor_ad_line_1, 
                         A_ADR_INV_LINE_2  => l_prof.cor_ad_line_4, 
                         A_POSTING_GROUP   => v_posting_group,
                         A_SOURCE_TYPE     => l_prof.source_type,               
                         A_PAYMENT_CONF_TYPE => 'INS');

   END LOOP;
   
  NAV_INVOICES_PB ('N');-- Czy usuwaæ wiersze z APP_FIN.NAV_INVOICES (Y / N)

  COMMIT;

EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'NAV_INV');
END;

PROCEDURE ADD_NAV_INVOICE(A_INVOICE_ID      IN NUMBER,
                          A_ORD_ID          IN NUMBER,
                          A_PROFORMA_NO     IN VARCHAR2, 
                          A_PROFORMA_DATE   IN DATE,
                          A_PROFORMA_AMOUNT IN NUMBER,
                          A_INVOICE_NO      IN VARCHAR2, 
                          A_INVOICE_DATE    IN DATE,
                          A_GROSS_COMMISION IN NUMBER,
                          A_NOTA_NO         IN VARCHAR2, 
                          A_NOTA_DATE       IN DATE,
                          A_TOTAL_FV        IN NUMBER,
                          A_COMPANY_CODE    IN VARCHAR2, 
                          A_COMPANY_CODE_TEMP IN VARCHAR2,
                          A_COMPANY_NAME    IN VARCHAR2,
                          A_ADR_INV_LINE_1  IN VARCHAR2, 
                          A_ADR_INV_LINE_2  IN VARCHAR2, 
                          A_POSTING_GROUP   IN VARCHAR2,
                          A_SOURCE_TYPE     IN VARCHAR2,
                          A_PAYMENT_CONF_TYPE IN VARCHAR2)
IS
BEGIN

   PK_AUDIT.AUDIT_MODULE('PK_NAV','ADD_NAV_INVOICE',NULL);
   
    INSERT INTO APP_FIN.NAV_INVOICES I ( 
       INVOICE_ID, ORD_ID, PROFORMA_NO, 
       PROFORMA_DATE, PROFORMA_AMOUNT, INVOICE_NO, 
       INVOICE_DATE, GROSS_COMMISION, NOTA_NO, 
       NOTA_DATE, TOTAL_FV, COMPANY_CODE, 
       COMPANY_CODE_TEMP, COMPANY_NAME, ADR_INV_LINE_1, 
       ADR_INV_LINE_2, POSTING_GROUP,
       I.SOURCE_TYPE,
       I.PAYMENT_CONF_TYPE )
    VALUES(
       A_INVOICE_ID, A_ORD_ID, A_PROFORMA_NO, 
       A_PROFORMA_DATE, A_PROFORMA_AMOUNT, A_INVOICE_NO, 
       A_INVOICE_DATE, A_GROSS_COMMISION, A_NOTA_NO, 
       A_NOTA_DATE, A_TOTAL_FV, A_COMPANY_CODE, 
       A_COMPANY_CODE_TEMP, A_COMPANY_NAME, A_ADR_INV_LINE_1, 
       A_ADR_INV_LINE_2, A_POSTING_GROUP,
       A_SOURCE_TYPE,
       A_PAYMENT_CONF_TYPE);

EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'NAV_ANI');
END;

PROCEDURE ADD_NAV_INVOICE_ID(a_proforma_no IN VARCHAR2,
                             a_invoice_no   IN VARCHAR2,
                             a_nota_no      IN VARCHAR2,
                             a_extention_id IN NUMBER,
                             a_ord_pb_id    IN NUMBER DEFAULT NULL)
IS
BEGIN

  PK_AUDIT.AUDIT_MODULE('PK_NAV','ADD_NAV_INVOICE_ID',NULL);
   
  INSERT INTO APP_FIN.NAV_INVOICE_ID (
     PROFORMA_NO, INVOICE_NO, NOTA_NO, 
     EXTENTION_ID, ORD_PB_ID) 
  VALUES (a_proforma_no , a_invoice_no, a_nota_no,
          a_extention_id, a_ord_pb_id );

EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'NAV_ANID');
END;


FUNCTION NAV_IMP_PAYMENTS ( v_err OUT VARCHAR2)
      RETURN BOOLEAN IS

  v_inv_amount     APP_FIN.INVOICES.GROSS_AMOUNT%TYPE;
  v_ord_id         APP_EMI.ORDERS.ID%TYPE;
  v_payment        APP_EMI.ORDERS.PAYMENT%TYPE;
  v_ord_status     APP_EMI.ORDERS.STATUS%TYPE;
  v_crd_id         APP_FIN.CREDITS.ID%TYPE;
  v_crd_amount     APP_FIN.CREDITS.AMOUNT%TYPE;
  v_crd_cl_amount  APP_FIN.CREDITS.AMOUNT%TYPE;
  v_cus_id         APP_EMI.ORDERS.CUS_ID%TYPE;
  v_tr_amount      APP_FIN.NAV_MC_TRANSFERS.AMOUNT%TYPE;
  v_rowid          VARCHAR2(18);
  v_inv_rowid      VARCHAR2(18);
  
  v_tsk_id         APP_TSK.TASKS.ID%TYPE;
  v_tsk_status     APP_TSK.TASKS.STATUS%TYPE;

  v_vord_id        APP_VEMI.ORDERS.ID%TYPE;
  v_vord_status    APP_VEMI.ORDERS.STATUS%TYPE;  
  v_update_amount  NUMBER;

  v_update_amount_prc NUMBER;
  v_tr_amount_prc     NUMBER;
  
  TYPE tt_invoices IS TABLE OF T$INVOICE;
  v_inv_prop        T$INVOICE_PROPOSAL;
  v_inv_prop_id     APP_FIN.INVOICE_PROPOSALS.ID%TYPE;
  v_inv_prop_status APP_FIN.INVOICE_PROPOSALS.STATUS%TYPE;
  v_idx_p           NUMBER;
  v_idx_f           NUMBER;
  v_idx_n           NUMBER;
  v_o$invoices      tt_invoices;
  v_invoicing_type  APP_FIN.INVOICE_PROPOSALS.INVOICING_TYPE%TYPE;
  v_inv_id          APP_FIN.INVOICES.ID%TYPE;
  v_invoice_no      APP_FIN.INVOICES.INVOICE_NUMBER%TYPE;
  v_email           APP_FIN.INVOICE_PROPOSALS.EMAIL%type;
  v_name            APP_FIN.INVOICE_PROPOSALS.NAME%type;
  v_gcon_id         APP_COR.GEN_CONTRACTS.ID%TYPE;
  
  PROCEDURE UNLOCK_ALL_OBJECTS_NO_AUT
  IS
  BEGIN
    DELETE FROM ADM_LOCKED_OBJECTS
    WHERE sid = userenv('SESSIONID');
  EXCEPTION
    WHEN others THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'PK_NAV_UAO');
  END;
  
  
BEGIN

  PK_AUDIT.AUDIT_MODULE('PK_NAV','NAV_IMP_PAYMENTS',NULL);

  FOR t IN (
            SELECT TRS.doc_number, 
                   SUM(TRS.amount) AMOUNT, 
                   MAX(TRS.payment_date) payment_date,
                   SUM(CASE  WHEN SRC_TAB = 'TRS' THEN 1 ELSE 0 END ) SRC_TAB_QUANTITY_TRS,
                   SUM(CASE  WHEN SRC_TAB = 'TRSP' THEN 1 ELSE 0 END ) SRC_TAB_QUANTITY_TRSP
            FROM  (SELECT doc_number, amount, payment_date, 'TRS' SRC_TAB 
                  FROM   APP_FIN.NAV_MC_TRANSFERS
                  WHERE  used = 'N' 
                  AND    doc_number IS NOT NULL
                  union all
                  SELECT doc_number, amount, payment_date, 'TRSP' SRC_TAB
                  FROM   APP_FIN.NAV_MC_TRANSFERS_PRC TP
                  WHERE  used = 'N' 
                  AND    doc_number IS NOT NULL
                  AND    ERROR_TIMESTAMP IS NULL
                  AND    ERROR_MESSAGE   IS NULL
                  AND    TP.SOURCE_TYPE = 'ORD'
                  ) TRS
            GROUP BY doc_number 
            order by doc_number  
            )
  LOOP
    
    savepoint MC_TRANSFER_SAVEPOINT;  
  
    v_ord_id       := NULL;
    v_inv_prop_id  := NULL;
    v_tsk_id       := NULL;
	  v_vord_id      := NULL;
    v_update_amount:= NULL;

    BEGIN
    --dane proformy i zamówienia
      SELECT distinct o.id ord_id, o.payment, o.status, i.gross_amount, o.cus_id, rowidtochar(o.rowid) rowid#
        INTO v_ord_id, v_payment, v_ord_status, v_inv_amount, v_cus_id, v_rowid
        FROM APP_EMI.ORDERS o, APP_FIN.INVOICES i, APP_FIN.INVOICE_LINES il
       WHERE o.id = il.source
         AND i.id = il.inv_id
         AND il.SOURCE_TYPE = 'ORD'
         AND i.type = 'P'
         AND i.invoice_number = t.doc_number;
         --DBMS_OUTPUT.PUT_LINE('ORD ID '||v_ord_id);
    EXCEPTION
      WHEN no_data_found THEN null;
      WHEN too_many_rows THEN null;
    END;
    
    BEGIN
    --dane proformy i propozycji (GAFA)
      SELECT ip.id, i.id, ip.payment, ip.status, i.gross_amount-sum(IL.PAID_AMOUNT) , ip.cus_id, rowidtochar(ip.rowid) rowid#, 
             ip.gcon_id, ip.name
        INTO v_inv_prop_id, v_inv_id, v_payment, v_inv_prop_status, v_inv_amount, v_cus_id, v_inv_rowid,   
             v_gcon_id, v_name
        FROM APP_FIN.INVOICE_PROPOSALS ip, APP_FIN.INVOICES i, APP_FIN.INVOICE_LINES il
       WHERE ip.id = il.source
         AND i.id = il.inv_id
         AND il.SOURCE_TYPE = 'IP'
         AND i.type = EAGLE.PK_MOD_INV_PROPOSALS.GC_INV_TYPE_PG
         AND i.invoice_number = t.doc_number
       GROUP BY ip.id, i.id, ip.payment, ip.status, i.gross_amount, ip.cus_id, ip.rowid, 
                ip.gcon_id, ip.name;
         --DBMS_OUTPUT.PUT_LINE('PROPOSAL ID '||v_ord_id);
    EXCEPTION
      WHEN no_data_found THEN null;
      WHEN too_many_rows THEN null;
    END;
    
  --sprawdzenie czarnej listy
    BEGIN
      SELECT CUS_ID INTO v_cus_id
        FROM APP_COR.BLACK_LISTS BL, APP_EMI.CUSTOMER_BLACK_LISTS CBL
       WHERE CBL.BLL_ID=BL.ID 
	     AND CBL.CUS_ID = v_cus_id
         AND BL.TYPE = 'BP'
         AND BL.STATUS = 'A';

      -- rezygnacja, jesli jest blokada platnosci na firme
      v_ord_id := null;
      v_inv_prop_id := null;
    EXCEPTION
      WHEN no_data_found THEN null;
      WHEN too_many_rows THEN null;
    END;
    --DBMS_OUTPUT.PUT_LINE('ORD ID PO BL'||v_ord_id);

    IF v_ord_id IS NOT NULL THEN
      BEGIN
        PK_AUDIT.LOCK_ROWID('APP_EMI.ORDERS', v_rowid); 
        --null;
      EXCEPTION
        WHEN others then
           v_ord_id := null;
           --WHEN OTHERS THEN
               --PK_ERROR.RAISE_ERROR(sqlcode, sqlerrm, 'PLUM1'); 
      END;
    --DBMS_OUTPUT.PUT_LINE('ORD ID PO LOCK'||v_ord_id);
    END IF;
    
    IF v_inv_prop_id IS NOT NULL THEN
      BEGIN
        PK_AUDIT.LOCK_ROWID('APP_FIN.INVOICE_PROPOSALS', v_inv_rowid); 
      EXCEPTION
        WHEN others then
           v_inv_prop_id := null;
      END;
    END IF;
    
    IF  v_inv_prop_id IS NOT NULL 
    AND T.SRC_TAB_QUANTITY_TRS>0 THEN

      BEGIN
      -- poprzednie platnosci
        SELECT nvl(sum(amount),0)
          INTO v_tr_amount
          FROM APP_FIN.NAV_MC_TRANSFERS
         WHERE used = 'Y'
           AND doc_number = t.doc_number;
      EXCEPTION
        WHEN no_data_found THEN v_tr_amount := 0;
      END;
      
      --DBMS_OUTPUT.PUT_LINE('v_tr_amount'||v_tr_amount);
      
      --ustawienie czesciowej platnosci
       v_inv_prop := T$INVOICE_PROPOSAL(v_inv_prop_id, a_locked => 'N' );  
       v_inv_prop.PART_PAYMENT := 'Y';
       IF v_inv_amount <= (t.amount + v_tr_amount +1) THEN
          -- proforma op³acona
          v_inv_prop.PAYMENT := 'Y';
       END IF;   
       v_inv_prop.SERIALIZE();       

      UPDATE APP_FIN.NAV_MC_TRANSFERS
         SET used = 'Y',
             match_date = sysdate
       WHERE used = 'N'
         AND doc_number = t.doc_number
       RETURNING  nvl(sum(amount),0) INTO v_update_amount;
         
       IF NVL(v_update_amount,0) != t.amount THEN
         ROLLBACK TO SAVEPOINT MC_TRANSFER_SAVEPOINT;
       END IF;

      IF v_inv_prop_status = EAGLE.PK_MOD_INV_PROPOSALS.GC_INV_PROP_STATUS_CN THEN
        --Propozycja faktury w statusie wygenerowano proformê
        IF v_inv_amount <= (t.amount + v_tr_amount +1) THEN

          -- proforma op³acona
          v_inv_prop.PAYMENT := 'Y';
          IF v_inv_prop.STATUS IN (EAGLE.PK_MOD_INV_PROPOSALS.GC_INV_PROP_STATUS_CN, EAGLE.PK_MOD_INV_PROPOSALS.GC_INV_PROP_STATUS_AB)
          THEN
            IF EAGLE.T$INVOICE_PROPOSAL.GET_PAR_MAN_VAL ('ELECTR_DEL', v_inv_prop_id) IS NULL
            THEN
              T$INVOICE_PROPOSAL.GEN_PAR_ELECTR_DEL(A_INV_PROP_ID => v_inv_prop_id);
            END IF;
            v_inv_prop.STATUS := PK_MOD_INV_PROPOSALS.GC_INV_PROP_STATUS_F;
          END IF;
          v_inv_prop.SERIALIZE();       

          -- generowanie dokumentów ksiêgowych
          v_o$invoices := tt_invoices();
          v_o$invoices.extend;    
          v_idx_p := v_o$invoices.last;
          v_o$invoices(v_idx_p) :=  T$INVOICE(v_inv_id);
          v_invoicing_type := EAGLE.T$INVOICE_PROPOSAL.GET_INVOICING_TYPE(v_inv_prop_id);
          v_o$invoices.extend;    
          v_idx_f := v_o$invoices.last;
          
          v_o$invoices(v_idx_f) :=  T$INVOICE(a_o$proforma     => v_o$invoices(v_idx_p),
                                              a_type           => PK_MOD_INV_PROPOSALS.GC_INV_TYPE_ICG,
                                              a_inv_prop_id    => v_inv_prop_id,
                                              a_invoicing_type => v_invoicing_type,
                                              a_sign_user      => v_o$invoices(v_idx_p).sign_user);  

          v_o$invoices.extend;    
          v_idx_n := v_o$invoices.last;      
          v_o$invoices(v_idx_n) :=  T$INVOICE(a_o$proforma     => v_o$invoices(v_idx_p),
                                              a_type           => PK_MOD_INV_PROPOSALS.GC_INV_TYPE_NG,
                                              a_inv_prop_id    => v_inv_prop_id,
                                              a_invoicing_type => v_invoicing_type,
                                              a_sign_user      => v_o$invoices(v_idx_p).sign_user); 
                                              
          FOR K IN v_o$invoices.FIRST..v_o$invoices.LAST
          LOOP 
              IF  v_o$invoices(K).LINES.COUNT_NOT_DELETED >0  
              AND v_o$invoices(K).GROSS_AMOUNT!=0
              AND v_o$invoices(K).TYPE IN (PK_MOD_INV_PROPOSALS.GC_INV_TYPE_ICG, PK_MOD_INV_PROPOSALS.GC_INV_TYPE_NG) 
              THEN
                IF v_o$invoices(K).INVOICE_NUMBER IS NULL THEN
                  v_o$invoices(K).INVOICE_NUMBER := t$invoice.GET_INVOICE_NUMBER_IP( a_doc_type       => v_o$invoices(K).TYPE,
                                                                                     a_inv_prop_id    => v_inv_prop_id,
                                                                                     a_invoicing_type => v_invoicing_type
                                                                                    );
                 END IF;
                 
                v_o$invoices(K).SERIALIZE;

                IF NVL(EAGLE.T$INVOICE_PROPOSAL.GET_PAR_MAN_VAL ('PLATN_PROF', v_inv_prop_id), 'NULL') = 'Tak'
                AND v_o$invoices(K).TYPE IN (PK_MOD_INV_PROPOSALS.GC_INV_TYPE_ICG, PK_MOD_INV_PROPOSALS.GC_INV_TYPE_NG)
                AND EAGLE.T$INVOICE_PROPOSAL.CAN_GENERATE_ATTACHMENT (v_inv_prop_id, v_email)
                THEN
                  BEGIN
                   PK_NAV.SND_MAIL_PAYM(v_cus_id,
                                        v_inv_amount,
                                        null,
                                        null,
                                        v_o$invoices(K).INVOICE_NUMBER,
                                        v_email                          
                                        );
                  EXCEPTION
                     WHEN OTHERS THEN
                     null;
                  END;
                END IF;
                
              END IF;
          END LOOP;
        ELSE
          --proforma op³acona czêœciowo
          v_email := EAGLE.PK_MOD_INV_PROPOSALS.GET_MAIL_PM(v_gcon_id);
          SND_MAIL_PAYM ( v_cus_id,
                          v_inv_amount,
                          null,
                          null,
                          v_inv_prop_id,
                          v_email,
                          t.amount + v_tr_amount - v_inv_amount,
                          t.payment_date,
                          v_name 
                          );
          
          
          
          
        
        END IF;

      END IF;

    END IF;  
    
    IF v_ord_id IS NOT NULL THEN

      BEGIN
	    -- poprzednie platnosci
        SELECT NVL(SUM(TRS.amount),0) AMOUNT
        INTO  v_tr_amount
        FROM  (SELECT doc_number, amount, payment_date 
              FROM   APP_FIN.NAV_MC_TRANSFERS
              WHERE  used = 'Y' 
              AND    doc_number = t.doc_number
              union all
              SELECT doc_number, amount, payment_date
              FROM   APP_FIN.NAV_MC_TRANSFERS_PRC TP
              WHERE  used = 'Y' 
              AND    doc_number = t.doc_number
              AND    TP.SOURCE_TYPE = 'ORD'
              ) TRS
        GROUP BY doc_number 
        order by doc_number;
           
      EXCEPTION
        WHEN no_data_found THEN v_tr_amount := 0;
      END;
      
	  --ustawienie czesciowej platnosci
      UPDATE APP_EMI.ORDERS
         SET part_payment = 'Y',
	  	     modified = pk_utils.get_mod_string
       WHERE id = v_ord_id
	     AND nvl(part_payment, 'N') != 'Y';

      IF v_ord_status = 'P' THEN
        --zamówienia z³o¿one
        IF v_inv_amount <= (t.amount + v_tr_amount +1) THEN
          -- zamówienie op³acone
          UPDATE APP_EMI.ORDERS
             SET payment = 'Y', 
			     status = 'R',
	   	         modified = pk_utils.get_mod_string
           WHERE id = v_ord_id;
           
        --2014-07-04
        --DODANE
        --DBMS_OUTPUT.PUT_LINE('PRZED MAIL'||v_ord_id);
        BEGIN
           PK_NAV.SND_MAIL_PAYM(v_cus_id,
                          v_inv_amount,
                          v_ord_id,
                          null                          
                          );
        EXCEPTION
             WHEN OTHERS THEN
             null;
             --PK_ERROR.RAISE_ERROR(sqlcode, sqlerrm, 'PLUM');              
        END;           
        END IF;
        --DBMS_OUTPUT.PUT_LINE('PO MAIL'||v_ord_id);

        v_update_amount := 0;
        IF T.SRC_TAB_QUANTITY_TRSP>0 THEN
          UPDATE APP_FIN.NAV_MC_TRANSFERS_PRC TP
             SET used = 'Y',
                 match_date = sysdate
           WHERE used = 'N'
             AND doc_number = t.doc_number
             AND TP.SOURCE_TYPE = 'ORD'
             AND ERROR_TIMESTAMP IS NULL
             AND ERROR_MESSAGE   IS NULL
           RETURNING  nvl(sum(amount),0) INTO v_update_amount_prc;
           v_update_amount := v_update_amount + NVL(v_update_amount_prc,0);
        END IF;
        
        IF T.SRC_TAB_QUANTITY_TRS>0 THEN
          UPDATE APP_FIN.NAV_MC_TRANSFERS
             SET used = 'Y',
                 match_date = sysdate
           WHERE used = 'N'
             AND doc_number = t.doc_number
           RETURNING  nvl(sum(amount),0) INTO v_update_amount_prc;
           v_update_amount := v_update_amount + NVL(v_update_amount_prc,0);
        END IF;
         
        IF NVL(v_update_amount,0) != t.amount THEN
          ROLLBACK TO SAVEPOINT MC_TRANSFER_SAVEPOINT;
        END IF;
        
      ELSE
        -- zamowienie w trakcie realizacji -> kredyt
        BEGIN
		    --nieoplacony kredyt
          SELECT id, amount
            INTO v_crd_id, v_crd_amount
            FROM APP_FIN.CREDITS
           WHERE ord_id = v_ord_id
             AND status = 'P';

          BEGIN
		        --wczesniejsze sp³aty kredytu
            SELECT nvl(sum(amount),0)
              INTO v_crd_cl_amount
              FROM APP_FIN.CREDIT_CLEARING
             WHERE crd_id = v_crd_id;
          EXCEPTION
            WHEN no_data_found 
			      THEN v_crd_cl_amount := 0;
          END;

          IF v_crd_amount <= (t.amount + v_crd_cl_amount + 1) THEN
            -- kredyt w ca³osci op³acony
            UPDATE APP_FIN.CREDITS
               SET status = 'F',
                   clear_user = user,
                   clear_date = sysdate,
		           modified = pk_utils.get_mod_string
             WHERE id = v_crd_id;
			
			     -- pe³na platnosc za zamówienie
            UPDATE APP_EMI.ORDERS
               SET payment = 'Y',
	  	           modified = pk_utils.get_mod_string
             WHERE id = v_ord_id;

          END IF;

          INSERT
            INTO APP_FIN.CREDIT_CLEARING
                (CRD_ID, AMOUNT, CLEAR_DATE, CLEAR_USER, TRANSF_ID,
     			 CREATED, MODIFIED)
             SELECT v_crd_id, amount, sysdate, user, id,
                    eagle.pk_utils.get_mod_string, eagle.pk_utils.get_mod_string
               FROM APP_FIN.NAV_MC_TRANSFERS
              WHERE used = 'N'
                AND doc_number = t.doc_number;
		  
		  --platnosc wykorzystana
          v_update_amount := 0;
          IF T.SRC_TAB_QUANTITY_TRSP>0 THEN
            UPDATE APP_FIN.NAV_MC_TRANSFERS_PRC TP
               SET used = 'Y',
                   match_date = sysdate
             WHERE used = 'N'
               AND doc_number = t.doc_number
               AND TP.SOURCE_TYPE = 'ORD'
               AND ERROR_TIMESTAMP IS NULL
               AND ERROR_MESSAGE   IS NULL
             RETURNING  nvl(sum(amount),0) INTO v_update_amount_prc;
             v_update_amount := v_update_amount + NVL(v_update_amount_prc,0);
          END IF;
          
          IF T.SRC_TAB_QUANTITY_TRS>0 THEN
            UPDATE APP_FIN.NAV_MC_TRANSFERS
               SET used = 'Y',
                   match_date = sysdate
             WHERE used = 'N'
               AND doc_number = t.doc_number
             RETURNING  nvl(sum(amount),0) INTO v_update_amount_prc;
             v_update_amount := v_update_amount + NVL(v_update_amount_prc,0);
          END IF;
           
          IF NVL(v_update_amount,0) != t.amount THEN
            ROLLBACK TO SAVEPOINT MC_TRANSFER_SAVEPOINT;
          END IF;

        EXCEPTION
          WHEN no_data_found THEN null;
          WHEN too_many_rows THEN null;
        END;

      END IF;
      
      IF v_inv_amount < (t.amount + v_tr_amount) THEN
        null;
        -- todo: zweryfikowaæ wiszace p³atnoœci i odkomentowaæ
        /*
        SEND_PAY_MORE_THEN_PROF(a_order_id        => v_ord_id,
                                a_doc_number      => t.doc_number,
                                a_transfer_amount => t.amount + v_tr_amount,
                                a_proforma_amount => v_inv_amount,
                                a_eror_emails     =>   pk_utils.get_param_value('PAY_MATCH_ERR_EMAIL_LIST'),  
                                a_error_group     => pk_utils.get_param_value('PAY_MATCH_ERR_GROUP')
                                );
       */
      END IF;

    ELSE
      -- op³ata za proformê do zadañ mp. wymiana
      BEGIN
        SELECT distinct t.id ord_id, t.status, i.gross_amount, t.cus_id, rowidtochar(t.rowid) rowid#
          INTO v_tsk_id, v_tsk_status, v_inv_amount, v_cus_id, v_rowid
          FROM APP_TSK.TASKS t, APP_FIN.INVOICES i, APP_FIN.INVOICE_LINES il
         WHERE t.id = il.source
           AND i.id = il.inv_id
           AND il.SOURCE_TYPE = 'TSK'
           AND i.type = 'P'
           AND i.invoice_number = t.doc_number;
      EXCEPTION
        WHEN no_data_found THEN null;
        WHEN too_many_rows THEN null;
      END;
  
      BEGIN
        PK_AUDIT.LOCK_ROWID('APP_TSK.TASKS', v_rowid);
      EXCEPTION
        WHEN others then
          v_tsk_id := null;
      END;
  
      IF v_tsk_id IS NOT NULL THEN
        BEGIN
          SELECT nvl(sum(amount),0)
            INTO v_tr_amount
            FROM APP_FIN.NAV_MC_TRANSFERS
           WHERE used = 'Y'
             AND doc_number = t.doc_number;
        EXCEPTION
          WHEN no_data_found THEN 
		    v_tr_amount := 0;
        END;
   
        --UPDATE APP_EMI.ORDERS
        --   SET part_payment = 'Y'
        -- WHERE id = v_ord_id;
        
   
        IF v_tsk_status = 'AC' THEN
          IF v_inv_amount <= (t.amount + v_tr_amount +1) THEN
            -- zadanie op³acone
            UPDATE APP_TSK.TASKS
               SET PAYMENT = 'Y',
                   status = 'P',
	  	           modified = pk_utils.get_mod_string
             WHERE id = v_tsk_id;
             
          --15-07-2014
          --wysy³ka maila
            PK_NAV.SND_MAIL_PAYM(v_cus_id,
                                  v_inv_amount,
                                  NULL,
                                  v_tsk_id
                                  );
          END IF;
   
          UPDATE APP_FIN.NAV_MC_TRANSFERS
             SET used = 'Y',
                 match_date = sysdate
           WHERE used = 'N'
             AND doc_number = t.doc_number
          RETURNING  nvl(sum(amount),0) INTO v_update_amount;
         
          IF NVL(v_update_amount,0) != t.amount THEN
            ROLLBACK TO SAVEPOINT MC_TRANSFER_SAVEPOINT;
          END IF;
             
   
        ELSE
          -- zadanie w trakcie realizacji -> kredyt
          BEGIN
  
            SELECT id, amount
              INTO v_crd_id, v_crd_amount
              FROM APP_FIN.CREDITS
             WHERE tsk_id = v_tsk_id
               AND status = 'P';
  
            BEGIN
              SELECT nvl(sum(amount),0)
                INTO v_crd_cl_amount
                FROM APP_FIN.CREDIT_CLEARING
               WHERE crd_id = v_crd_id;
            EXCEPTION
              WHEN no_data_found THEN 
			          v_crd_cl_amount := 0;
            END;
  
            IF v_crd_amount <= (t.amount + v_crd_cl_amount + 1) THEN
              -- kredyt w ca³osci op³acony
              UPDATE APP_FIN.CREDITS
                 SET status = 'F',
                     clear_user = user,
                     clear_date = sysdate,
			         modified = pk_utils.get_mod_string
               WHERE id = v_crd_id;

              -- zadanie op³acone
              UPDATE APP_TSK.TASKS
                 SET PAYMENT = 'Y',
                     modified = pk_utils.get_mod_string
               WHERE id = v_tsk_id;
  
            END IF;
  
            INSERT
              INTO APP_FIN.CREDIT_CLEARING
                  (CRD_ID, AMOUNT, CLEAR_DATE, CLEAR_USER, TRANSF_ID,
                   CREATED, MODIFIED)
            SELECT v_crd_id, amount, sysdate, user, id,
                   eagle.pk_utils.get_mod_string, eagle.pk_utils.get_mod_string
              FROM APP_FIN.NAV_MC_TRANSFERS
             WHERE used = 'N'
               AND doc_number = t.doc_number;
  
            UPDATE APP_FIN.NAV_MC_TRANSFERS
               SET used = 'Y',
                   match_date = sysdate
             WHERE used = 'N'
               AND doc_number = t.doc_number
            RETURNING  nvl(sum(amount),0) INTO v_update_amount;
         
            IF NVL(v_update_amount,0) != t.amount THEN
              ROLLBACK TO SAVEPOINT MC_TRANSFER_SAVEPOINT;
            END IF;

          EXCEPTION
            WHEN no_data_found THEN null;
            WHEN too_many_rows THEN null;
          END;
  
        END IF;
      ELSE
	      /*P³atnosci VivaBoks - pocz¹tek*/
	      BEGIN
	      --dane proformy i zamówienia
          SELECT distinct o.id ord_id, o.payment, o.status, i.gross_amount, o.cus_id, rowidtochar(o.rowid) rowid#
            INTO v_vord_id, v_payment, v_vord_status, v_inv_amount, v_cus_id, v_rowid
            FROM APP_VEMI.ORDERS o, APP_FIN.INVOICES i, APP_FIN.INVOICE_LINES il
           WHERE o.id = il.source
             AND i.id = il.inv_id
             AND il.SOURCE_TYPE = 'ORDV'
             AND i.type = 'PV'
             AND i.invoice_number = t.doc_number;
        EXCEPTION
          WHEN no_data_found THEN null;
          WHEN too_many_rows THEN null;
        END;
    
	     --sprawdzenie czarnej listy - wykasowano sprawdzenie czarnej listy dla VB

        BEGIN
          PK_AUDIT.LOCK_ROWID('APP_VEMI.ORDERS', v_rowid);
        EXCEPTION
          WHEN others then
             v_vord_id := null;
        END;
        
		    IF v_vord_id IS NOT NULL THEN

          BEGIN
	        -- poprzednie platnosci
            SELECT nvl(sum(amount),0)
              INTO v_tr_amount
              FROM APP_FIN.NAV_MC_TRANSFERS
             WHERE used = 'Y'
               AND doc_number = t.doc_number;
          EXCEPTION
            WHEN no_data_found THEN v_tr_amount := 0;
          END;
      
	      --ustawienie czesciowej platnosci
    		  UPDATE APP_VEMI.ORDERS
             SET part_payment = 'Y',
	  	           modified = pk_utils.get_mod_string
           WHERE id = v_vord_id
	         AND nvl(part_payment, 'N') != 'Y';
		  
          IF v_vord_status = 'P' THEN
            --zamówienia z³o¿one
            IF v_inv_amount <= (t.amount + v_tr_amount +1) THEN
              -- zamówienie op³acone
              UPDATE APP_VEMI.ORDERS
                 SET payment = 'Y', 
			         status = 'R',
	   	             modified = pk_utils.get_mod_string
               WHERE id = v_vord_id;
            END IF;
			
          ELSIF v_vord_status IN ('R','Z','E','F') THEN
            -- zamowienie w trakcie realizacji -> kredyt
            IF v_inv_amount <= (t.amount + v_tr_amount +1) THEN
              -- zamówienie op³acone
              UPDATE APP_VEMI.ORDERS
                 SET payment = 'Y', 
	   	             modified = pk_utils.get_mod_string
               WHERE id = v_vord_id;
            END IF;
          END IF;
          
		      UPDATE APP_FIN.NAV_MC_TRANSFERS
             SET used = 'Y',
                 match_date = sysdate
           WHERE used = 'N'
             AND doc_number = t.doc_number
          RETURNING  nvl(sum(amount),0) INTO v_update_amount;
         
          IF NVL(v_update_amount,0) != t.amount THEN
            ROLLBACK TO SAVEPOINT MC_TRANSFER_SAVEPOINT;
          END IF;

        END IF;
 		   /*P³atnosci VivaBoks - koniec*/
	    END IF;

    END IF;

  END LOOP;

  UNLOCK_ALL_OBJECTS_NO_AUT;
  
  MATCH_PAYMENTS(v_err);
  
  IF v_err IS NOT NULL THEN
    RETURN FALSE;
  END IF;
  

  RETURN TRUE;

EXCEPTION
  WHEN OTHERS THEN
    v_err := 'SABA_IMP_PAYMENTS: '||SQLERRM;
    RETURN FALSE;
END;

PROCEDURE NAV_IMP_PAYMENTS
IS
  v_err VARCHAR2(2000);
BEGIN

  IF PK_NAV.NAV_IMP_PAYMENTS(v_err) THEN
    COMMIT;
  ELSE
    ROLLBACK;
    pk_error.WRITE_ERROR(-20001,'B³¹d w funkcji NAV_IMP_PAYMENTS: '||v_err);
    EAGLE.SEND_MAIL(pk_utils.get_param_value('EMAIL_EAGLE'),'es@sodexomotivation.pl','B³¹d w funkcji NAV_IMP_PAYMENTS',v_err);
  END IF;

END;


FUNCTION CONVERT_INVOICE_NUMBER(a_invoice_number IN VARCHAR2) RETURN NUMBER
IS
BEGIN

 PK_AUDIT.AUDIT_MODULE('PK_NAV','CONVERT_INVOICE_NUMBER',NULL);
 
 RETURN to_number(substr(a_invoice_number,instr(a_invoice_number,'/')+1,2)||
              lpad(substr(a_invoice_number,1,instr(a_invoice_number,'/')-1),6,'0'));

EXCEPTION
  WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'MOD_NAV_CIN');
END;

FUNCTION GET_INVOICE_ID(a_proforma_no IN VARCHAR2, a_invoice_no IN VARCHAR2, a_nota_no IN VARCHAR2) RETURN NUMBER
IS
   v_retval NUMBER(10);
   v_id app_fin.nav_invoice_id.EXTENTION_ID%TYPE;
BEGIN

    IF a_proforma_no IS NULL THEN
       PK_ERROR.RAISE_ERROR(-20002, 'proforma_no nie moze byc NULL!');
    END IF;
    
    v_id := NULL;
    IF a_invoice_no IS NULL THEN
    
       BEGIN
       
           SELECT nid.EXTENTION_ID
             INTO v_id
           FROM app_fin.nav_invoice_id nid
           WHERE nid.PROFORMA_NO = a_proforma_no
             AND nid.nota_no = a_nota_no;
             
       EXCEPTION
          WHEN no_data_found THEN
       
               SELECT nvl(max(nid.EXTENTION_ID),0)
                 INTO v_id
               FROM app_fin.nav_invoice_id nid
               WHERE nid.PROFORMA_NO = a_proforma_no;
               
               v_id := v_id + 1;
               
               add_nav_invoice_id(a_proforma_no  => a_proforma_no,
                                  a_invoice_no   => NULL,
                                  a_nota_no      => a_nota_no,
                                  a_extention_id  => v_id);
       
       END;
         
    ELSIF a_nota_no IS NULL THEN

       BEGIN
       
           SELECT nid.EXTENTION_ID
             INTO v_id
           FROM app_fin.nav_invoice_id nid
           WHERE nid.PROFORMA_NO = a_proforma_no
             AND nid.invoice_no = a_invoice_no;
             
       EXCEPTION
          WHEN no_data_found THEN
       
               SELECT nvl(max(nid.EXTENTION_ID),0)
                 INTO v_id
               FROM app_fin.nav_invoice_id nid
               WHERE nid.PROFORMA_NO = a_proforma_no;
               
               v_id := v_id + 1;
               
               add_nav_invoice_id(a_proforma_no  => a_proforma_no,
                                  a_invoice_no   => a_invoice_no,
                                  a_nota_no      => NULL,
                                  a_extention_id  => v_id);
       
       END;

    ELSE
       PK_ERROR.RAISE_ERROR(-20002, 'Wszystkie parametry nie moga byc wypelnione jednoczesnie!');
    END IF;
    
    RETURN to_number(to_char(CONVERT_INVOICE_NUMBER(a_proforma_no))||lpad(v_id,2,'0'));

EXCEPTION
  WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'MOD_NAV_GIN');
  
END;

FUNCTION GET_USER_EMAIL(a_aur_id IN VARCHAR2) RETURN VARCHAR2
IS
BEGIN

   PK_AUDIT.AUDIT_MODULE('PK_NAV','GET_USER_EMAIL',NULL);
   
   RETURN t$employee.GET_EMAIL(a_aur_id);

EXCEPTION
   WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'MOD_ORD_GUE');
END;

PROCEDURE SND_MAIL_PAYM ( a_cus_id IN NUMBER,
                          a_inv_amount IN NUMBER,
                          a_ord_id IN NUMBER,
                          a_tsk_id IN NUMBER,
                          a_invoice_no IN VARCHAR2 DEFAULT NULL,
                          a_email      IN VARCHAR2 DEFAULT NULL,
                          a_part_payment IN VARCHAR2 DEFAULT NULL,
                          a_date IN DATE DEFAULT NULL,
                          a_name_cus IN VARCHAR2 DEFAULT NULL 
                          )
IS
    v_prd_types VARCHAR2(3000);
    v_from VARCHAR2(100);
    v_subject VARCHAR2(100);
    v_footer VARCHAR2(1000);
    v_footer_eng VARCHAR2(1000);
    EOL VARCHAR2(2) := chr(13)||chr(10);
    v_body VARCHAR2(3000);
    v_body_eng VARCHAR2(3000);
    v_to VARCHAR2(900);
    v_to2_emp VARCHAR2(10);
    v_to2 VARCHAR2(900);
    v_o$email t$email;
    v_thrd_id NUMBER;
BEGIN

   PK_AUDIT.AUDIT_MODULE('PK_NAV','SND_MAIL_PAYM',NULL);
   IF a_ord_id IS NOT NULL THEN
       v_subject := 'P³atnoœæ za zamówienie '||a_ord_id;
       v_prd_types := t$order_line.get_prd_type(a_ord_id);
       v_to := EAGLE.T$ORDER.GET_EMAIL(a_ord_id);       
   ELSIF a_invoice_no IS NOT NULL THEN    
       v_subject := 'P³atnoœæ za proformê nr '||a_invoice_no;
       v_to := a_email;       
   ELSIF a_tsk_id IS NOT NULL THEN
       v_subject := 'P³atnoœæ za wymianê '||a_tsk_id ||' Payments for the exchange of coupons ';       
       v_prd_types := t$order_line.get_prd_type(a_ord_id);        
       
       BEGIN 
           SELECT GBT.THR_ID
           into
           v_thrd_id
           FROM  APP_RMB.GIV_BACK_ISSUES GBI, APP_RMB.GIV_BACK_THREADS GBT, APP_THR.THREADS THR
           WHERE GBI.TSK_ID = a_tsk_id
           AND GBT.THR_ID = THR.ID
           AND GBT.REM_ID = GBI.REM_ID
           AND THR.NET_TYPE_ID = 26;
           --AND THR.STATUS = 'F';
       EXCEPTION    
        WHEN NO_DATA_FOUND THEN
        v_thrd_id := NULL;       
       END;
       
       IF v_thrd_id IS NOT NULL THEN
            v_to := EAGLE.T$THREAD_ATTRIBUTE.GET_LATEST_VALUE(v_thrd_id, 2609, 'ADRES_MAIL');
       END IF;
       
       FOR j IN (SELECT DISTINCT PT.TYPE, PT.NAME
                   FROM APP_TSK.TASK_PRODUCTS TPR, APP_STC.PRODUCT_TYPES PT, APP_STC.PRODUCTS P
                   WHERE TPR.TSK_ID = a_tsk_id
                   AND TPR.PRD_ID = P.ID
                   AND P.TYPE = PT.TYPE
                   ) 
      LOOP
        v_prd_types := v_prd_types || ' '||j.NAME;
      END LOOP;
       
   END IF; 

   v_from := PK_UTILS.get_param_value('EMAIL_DOK');
   v_footer := '<html><body><p>Z powa¿aniem,</p>
                <p>Sodexo Benefits and Rewards Services Polska Sp.z o.o.</p>
                <p>ul. K³obucka 25</p>
                <p>02-699 Warszawa</p>
                <p>fax. 22/ 535 10 01</p>
                <p>www.sodexo.pl</p></html></body>';

  IF a_ord_id IS NOT NULL THEN            
       v_body := '<html><body><p>Szanowni Pañstwo,</p>
                 <p>Informujemy, ¿e p³atnoœæ za zamówienie '||a_ord_id||' na '||v_prd_types||' zosta³o zaksiegowane na naszym koncie</p>
                 <p><b>Zamówienie uzyska³o status Do realizacji</b></p>
                 <p>tel. (22) 535 11 11, e-mail: info.svc.pl@sodexo.com</p>'; 
      v_body_eng := '<html><body><p>Dear Sir or Madam,</p>
                 <p>Please be advised that the payment for the order number '||a_ord_id||' for the  '||v_prd_types||' has been registered on our account.</p>
                 <p><b>The order received status </b></p>
                 <p>tel. (+48 22) 535 11 11, e-mail: info.svc.pl@sodexo.com</p>'; 
      v_footer_eng:='<html><body><p>Yours Sincerely</p>
                <p>Sodexo Benefits and Rewards Services Polska Sp.z o.o.</p>
                <p>ul. K³obucka 25</p>
                <p>02-699 Warszawa</p>
                <p>fax. 22/ 535 10 01</p>
                <p>www.sodexo.pl</p></html></body>';           
  ELSIF a_invoice_no IS NOT NULL THEN
       v_body := '<html><body><p>Dzieñ dobry,</p>
                 <p>Informujemy, ¿e p³atnoœæ za proformê '||a_invoice_no||' zosta³a zaksiegowana na naszym koncie</p>
                 <p><b>Zamówienie jest w trakcie realizacji realizacji</b></p>
                 <p>tel. (22) 535 11 11, e-mail: info.svc.pl@sodexo.com</p>'; 
       v_body_eng := null;
       v_footer_eng := null;
  ELSIF a_tsk_id IS NOT NULL AND v_thrd_id IS NOT NULL THEN       
       v_body     := '<html><body><p>Szanowni Pañstwo,</p>
                 <p>Informujemy, ¿e p³atnoœæ za wymianê kuponów o numerze '||a_tsk_id||' na '||v_prd_types||' zosta³a zaksiegowana na naszym koncie</p>
                 <p><b>Wymiana kuponów uzyska³a status Do realizacji</b><p>
                 <p>tel. (22) 535 11 11, e-mail: info.svc.pl@sodexo.com<p></html></body>';   
       v_body_eng := '<html><body><p>Dear Sir or Madam,</p>
                 <p>We confirm that payment for the exchange of coupons number '||a_tsk_id||' for '||v_prd_types||' has been registered on our account</p>
                 <p><b>The exchange of coupons received status Do realizacji</b><p>
                 <p>tel. (+48 22) 535 11 11, e-mail: info.svc.pl@sodexo.com<p></html></body>';
       v_footer_eng:='<html><body><p>Yours Sincerely</p>
                <p>Sodexo Benefits and Rewards Services Polska Sp.z o.o.</p>
                <p>ul. K³obucka 25</p>
                <p>02-699 Warszawa</p>
                <p>fax. 22/ 535 10 01</p>
                <p>www.sodexo.pl</p></html></body>';
  END IF;               

  v_body := v_body||v_footer||EOL||'........................................'||EOL||v_body_eng||v_footer_eng;  
  v_to2_emp := PK_UTILS.GET_PARAM_VALUE('DOK_ORD_WP_4');
  v_to2 := GET_USER_EMAIL(v_to2_emp);

  IF a_part_payment IS NOT NULL THEN
    v_subject := 'Niepe³na wp³ata do proformy nr '||a_invoice_no;
    v_to := a_email;       
    v_body := '<html><body><p>Dzieñ dobry,</p>
              <p>W dniu '||a_date||' otrzymaliœmy niepe³n¹ wp³atê do proformy nr '||a_invoice_no||', wystawionej dla '||a_name_cus||'</p>
              <p>Nale¿y poinformowaæ klienta aby dop³aci³ brakuj¹c¹ kwotê w wysokoœci '||a_part_payment||' z³.</p>';
    v_to2 := 'Ksiegowosc.SVC.PL@sodexo.com';
  END IF;     
  v_o$email := t$email('EAGLE', v_from, v_to, v_subject, v_body, NULL, NULL, NULL, NULL, 'ORDP');        
  v_o$email.SEND_TO_DELIMITED(v_to,'text/html');
  v_o$email.SEND_TO_DELIMITED(v_to2,'text/html');
  v_o$email.SERIALIZE;
  v_o$email := null;
EXCEPTION
    WHEN OTHERS THEN
      null;
END;

----------------------------------------------------------------
-- new
----------------------------------------------------------------

PROCEDURE ACT_ORDER_PB_TRANSFER_AMOUNT
              (a_ord_pb_id           IN NUMBER,
               a_act_match_amount    IN NUMBER,
               a_act_transfer_amount IN NUMBER,
               a_act_payment_date    IN DATE,
               a_error_mess          OUT VARCHAR2
               )  IS

  a_el_id_match_amount    APP_PBE.ORDER_ELEMENTS.ID%TYPE; -- CONAMA_KK
  a_el_id_payment_date    APP_PBE.ORDER_ELEMENTS.ID%TYPE; -- PAYD_KK
  a_el_id_match_date      APP_PBE.ORDER_ELEMENTS.ID%TYPE; -- COND_KK
  a_el_id_transfer_amount APP_PBE.ORDER_ELEMENTS.ID%TYPE; -- PAYAMA_KK
  v_amount_to_pay         APP_PBE.ORDER_ELEMENTS.VALUE_NUMBER%TYPE;
  
  PROCEDURE GET_ELEMENT_ID_FOR_UPDATE
                        (a_ord_pb_id  NUMBER,
                         a_element_id VARCHAR2,
                         a_id         OUT NUMBER,
                         a_error_mess      OUT VARCHAR2) IS
    e_wait_eoutoftime EXCEPTION;
    PRAGMA EXCEPTION_INIT(e_wait_eoutoftime, -30006);
  BEGIN
      SELECT ID 
      INTO   a_id
      FROM   APP_PBE.ORDER_ELEMENTS E
      WHERE  E.ORDER_ID = a_ord_pb_id
      AND    E.ELEMENT_ID = a_element_id
      FOR UPDATE
      WAIT 2;
  EXCEPTION    
    WHEN TOO_MANY_ROWS THEN
      a_error_mess := 'Znaleziono wiele rekordów ORDER_ELEMENTS, dla ord id:'|| a_ord_pb_id||', element_id: '||a_element_id;
      RETURN;
    WHEN NO_DATA_FOUND THEN
      a_error_mess := 'Nie znaleziono rekordu ORDER_ELEMENTS, dla ord id:'|| a_ord_pb_id||', element_id: '||a_element_id;
      RETURN;
    WHEN e_wait_eoutoftime THEN
      a_error_mess := 'Nie uda³o siê zablokowaæ rekordu ORDER_ELEMENTS, dla ord id:'|| a_ord_pb_id||', element_id: '||a_element_id;
      RETURN;
    WHEN OTHERS THEN
      PK_ERROR.WRITE_ERROR(-20001,'Wyst¹pi³ nieobs³ugiwany b³¹d aktualizacji danych Zamówienia dla ord id:'|| a_ord_pb_id||': ORDER_ELEMENTS.ID'||a_element_id);
      a_error_mess := 'Wyst¹pi³ nieobs³ugiwany b³¹d aktualizacji danych Zamówienia dla ord id:'|| a_ord_pb_id||': ORDER_ELEMENTS.ID'||a_element_id;
      RETURN;
  END;

BEGIN

  savepoint s_act_bp_order_transfer_amount;
  
  SELECT E.VALUE_NUMBER 
  INTO   v_amount_to_pay
  FROM   APP_PBE.ORDER_ELEMENTS E
  WHERE  E.ORDER_ID = a_ord_pb_id
  AND    E.ELEMENT_ID = 'OWNCONA_KK';
  
  IF v_amount_to_pay< a_act_match_amount THEN
    PK_ERROR.WRITE_ERROR(-20001,'Wyst¹pi³ nieobs³ugiwany b³¹d aktualizacji danych Zamówienia dla ord id '|| a_ord_pb_id||': Podpiêta kwota '|| a_act_match_amount||' jest wiêksza ni¿ kwota do op³acenia '||v_amount_to_pay);
    a_error_mess := 'Wyst¹pi³ nieobs³ugiwany b³¹d aktualizacji danych Zamówienia dla ord id '|| a_ord_pb_id||': Podpiêta kwota '|| a_act_match_amount||' jest wiêksza ni¿ kwota do op³acenia '||v_amount_to_pay;
    RETURN;
  END IF;

--  a_el_id_match_amount APP_PBE.ORDER_ELEMENTS.ID%TYPE; -- CONAMA_KK
  GET_ELEMENT_ID_FOR_UPDATE
                        (a_ord_pb_id  => a_ord_pb_id,
                         a_element_id => 'CONAMA_KK',
                         a_id         => a_el_id_match_amount,
                         a_error_mess => a_error_mess);
  IF a_error_mess IS NOT NULL THEN
    ROLLBACK TO SAVEPOINT s_act_bp_order_transfer_amount;
    RETURN;
  END IF;
  
--  a_el_id_payment_date   APP_PBE.ORDER_ELEMENTS.ID%TYPE; -- PAYD_KK
  GET_ELEMENT_ID_FOR_UPDATE
                        (a_ord_pb_id  => a_ord_pb_id,
                         a_element_id => 'PAYD_KK',
                         a_id         => a_el_id_payment_date,
                         a_error_mess => a_error_mess);
  IF a_error_mess IS NOT NULL THEN
    ROLLBACK TO SAVEPOINT s_act_bp_order_transfer_amount;
    RETURN;
  END IF;
  
--  a_el_id_match_date     APP_PBE.ORDER_ELEMENTS.ID%TYPE; -- COND_KK
  GET_ELEMENT_ID_FOR_UPDATE
                        (a_ord_pb_id  => a_ord_pb_id,
                         a_element_id => 'COND_KK',
                         a_id         => a_el_id_match_date,
                         a_error_mess => a_error_mess);
  IF a_error_mess IS NOT NULL THEN
    ROLLBACK TO SAVEPOINT s_act_bp_order_transfer_amount;
    RETURN;
  END IF;
  
--  a_el_id_amount_payment APP_PBE.ORDER_ELEMENTS.ID%TYPE; -- PAYAMA_KK
  GET_ELEMENT_ID_FOR_UPDATE
                        (a_ord_pb_id  => a_ord_pb_id,
                         a_element_id => 'PAYAMA_KK',
                         a_id         => a_el_id_transfer_amount,
                         a_error_mess => a_error_mess);
  IF a_error_mess IS NOT NULL THEN
    ROLLBACK TO SAVEPOINT s_act_bp_order_transfer_amount;
    RETURN;
  END IF;
  
--  a_act_math_amount     IN NUMBER,
--  a_el_id_match_amount    APP_PBE.ORDER_ELEMENTS.ID%TYPE; -- CONAMA_KK
  UPDATE APP_PBE.ORDER_ELEMENTS E
  SET    E.VALUE_NUMBER       = a_act_match_amount,
         E.MODIFIED_USER      = USER,
         E.MODIFIED_TIMESTAMP = SYSTIMESTAMP
  WHERE  E.ID = a_el_id_match_amount;
  
--  a_act_payment_date    IN DATE,
--  a_el_id_payment_date    APP_PBE.ORDER_ELEMENTS.ID%TYPE; -- PAYD_KK
  UPDATE APP_PBE.ORDER_ELEMENTS E
  SET    E.VALUE_DATE         = a_act_payment_date,
         E.MODIFIED_USER      = USER,
         E.MODIFIED_TIMESTAMP = SYSTIMESTAMP
  WHERE  E.ID = a_el_id_payment_date;

--  a_el_id_match_date      APP_PBE.ORDER_ELEMENTS.ID%TYPE; -- COND_KK
  UPDATE APP_PBE.ORDER_ELEMENTS E
  SET    E.VALUE_DATE         = SYSDATE,
         E.MODIFIED_USER      = USER,
         E.MODIFIED_TIMESTAMP = SYSTIMESTAMP
  WHERE  E.ID = a_el_id_match_date;


--  a_el_id_transfer_amount APP_PBE.ORDER_ELEMENTS.ID%TYPE; -- PAYAMA_KK
--  a_act_transfer_amount IN NUMBER,
  UPDATE APP_PBE.ORDER_ELEMENTS E
  SET    E.VALUE_NUMBER       = a_act_transfer_amount,
         E.MODIFIED_USER      = USER,
         E.MODIFIED_TIMESTAMP = SYSTIMESTAMP
  WHERE  E.ID = a_el_id_transfer_amount;
  


EXCEPTION    
  WHEN OTHERS THEN
    PK_ERROR.WRITE_ERROR(-20001,'Wyst¹pi³ nieobs³ugiwany b³¹d aktualizacji danych Zamówienia dla ord id:'|| a_ord_pb_id||': '||SQLCODE||'; '||SQLERRM);
    a_error_mess := 'Wyst¹pi³ nieobs³ugiwany b³¹d aktualizacji danych Zamówienia dla ord id:'|| a_ord_pb_id;
    RETURN;
END;

PROCEDURE MATCH_PAYMENT( a_doc_number   IN VARCHAR2,
                         a_ord_pb_id    IN NUMBER,
                         a_source_type  IN VARCHAR2,
                         a_eror_emails  IN VARCHAR2,
                         a_error_group  IN VARCHAR2,
                         a_error_mess  OUT VARCHAR2,
                         a_matched_row_quantity OUT VARCHAR2
                         )
IS
  e_wait_eoutoftime EXCEPTION;
  PRAGMA EXCEPTION_INIT(e_wait_eoutoftime, -30006);  
  v_prev_version       APP_PBE.ORDERS.VERSION%TYPE;
  v_order_pb_status_id APP_PBE.ORDERS.STATUS_ID%TYPE;
  v_amount             NUMBER(38,2);      
  v_required_date      DATE;
  v_payment_date       DATE;
  v_not_used_quantity  NUMBER;
  v_transfer_amount    NUMBER;  
  v_amount_to_pay         APP_PBE.ORDER_ELEMENTS.VALUE_NUMBER%TYPE;
  v_warning            VARCHAR2(1000);
  
  
  PROCEDURE ERROR_REGISTER is
    v_o$email       T$EMAIL; 
  BEGIN 
 

    IF a_error_mess IS NOT NULL THEN
      ROLLBACK;
      UPDATE APP_FIN.NAV_MC_TRANSFERS_PRC T
      SET    T.ERROR_TIMESTAMP = SYSDATE,
             T.ERROR_MESSAGE = a_error_mess,
             T.MODIFIED_TIMESTAMP = SYSTIMESTAMP,
             T.MODIFIED_USER = USER,
             T.USED = 'E',
             T.WARNING = v_warning
      WHERE  T.USED = 'N'
      AND    T.ORDER_ID     = a_ord_pb_id                     
      AND    T.DOC_NUMBER   = a_doc_number      
      AND    T.SOURCE_TYPE  = a_source_type
      AND    T.ERROR_TIMESTAMP IS NULL
      AND    T.ERROR_MESSAGE   IS NULL ;

      PK_ERROR.WRITE_ERROR(-20001,'B³¹d w funkcji MATCH_PAYMENT dla '|| 
' ORDER_ID   : '||a_ord_pb_id    ||    
', DOC_NUMBER : '||a_doc_number   ||    
', SOURCE_TYPE: '||a_source_type  ||'; '|| a_error_mess);

    v_o$email := t$email('EAGLE', pk_utils.get_param_value('EMAIL_EAGLE'), NULL, 'B³¹d w funkcji MATCH_PAYMENT','B³¹d w funkcji MATCH_PAYMENT dla '|| 
' ORDER_ID   : '||a_ord_pb_id    ||    
', DOC_NUMBER : '||a_doc_number   ||    
', SOURCE_TYPE: '||a_source_type  ||'; '|| a_error_mess
    , NULL, NULL, NULL, NULL, 
                'PMTHE');        

      IF a_eror_emails IS NOT NULL THEN
        v_o$email.SEND_TO_DELIMITED(a_eror_emails);
      END IF;

      IF a_error_group IS NOT NULL THEN
        v_o$email.SEND_TO_GROUP(a_error_group);
      END IF;
      v_o$email.SERIALIZE;

      v_o$email := null;

      COMMIT;
    
    END IF;  
  END;

  PROCEDURE SEND_DELAYED_PAYMENT_MAIL is
    v_o$email       T$EMAIL; 
  BEGIN 
 
      PK_ERROR.WRITE_ERROR(-20001,'MATCH_PAYMENT: Rejestracja przeterminowanej p³atnoœci WUP'|| 
' ORDER_ID   : '||a_ord_pb_id    ||    
', DOC_NUMBER : '||a_doc_number   ||    
', SOURCE_TYPE: '||a_source_type  ||
', a_required_date: '||to_char(v_required_date,'YYYY-MM-DD')  ||
', v_payment_date: '||to_char(v_payment_date,'YYYY-MM-DD')
);

    v_o$email := t$email('EAGLE', pk_utils.get_param_value('EMAIL_EAGLE'), NULL, 'UWAGA: Zarejestrowano przeterminowan¹ p³atnoœæ za zamówienie WUP', 
'UWAGA: Zarejestrowano przeterminowan¹ p³atnoœæ za zamówienie WUP: 
Numer zamówienia: '||a_ord_pb_id    ||'    
Numer dokumentu: '||a_doc_number   ||'    
Typ Ÿród³a: '||a_source_type  ||'
Wymagana data wykonania p³atnoœci: '||to_char(v_required_date,'YYYY-MM-DD')  ||'
Data wykonania przelewu: '||to_char(v_payment_date,'YYYY-MM-DD')||'
'
    , NULL, NULL, NULL, NULL, 
                'PMTHE');        

      IF a_eror_emails IS NOT NULL THEN
        v_o$email.SEND_TO_DELIMITED(a_eror_emails);
      END IF;

      IF a_error_group IS NOT NULL THEN
        v_o$email.SEND_TO_GROUP(a_error_group);
      END IF;
      v_o$email.SERIALIZE;

      v_o$email := null;

      COMMIT;
    
  END;
  
  PROCEDURE SEND_WARNING_MAIL is
    v_o$email       T$EMAIL; 
  BEGIN 
    IF v_warning IS NULL THEN
      RETURN;
    END IF;
    
    PK_ERROR.WRITE_ERROR(-20001,'SEND_WARNING_MAIL: Ostrze¿enie dotycz¹ce podpiêcia p³atnoœci do zamówienie WUP: '|| v_warning ||
' ORDER_ID   : '||a_ord_pb_id    ||    
', DOC_NUMBER : '||a_doc_number   ||    
', SOURCE_TYPE: '||a_source_type  ||
', a_required_date: '||to_char(v_required_date,'YYYY-MM-DD')  ||
', v_payment_date: '||to_char(v_payment_date,'YYYY-MM-DD')
);

    v_o$email := t$email('EAGLE', pk_utils.get_param_value('EMAIL_EAGLE'), NULL, 'UWAGA: Ostrze¿enie dotycz¹ce podpiêcia p³atnoœci do zamówienie WUP', 
v_warning|| '   
Numer zamówienia: '||a_ord_pb_id    ||'    
Numer dokumentu: '||a_doc_number   ||'    
Typ Ÿród³a: '||a_source_type  ||'
Wymagana data wykonania p³atnoœci: '||to_char(v_required_date,'YYYY-MM-DD')  ||'
Data wykonania przelewu: '||to_char(v_payment_date,'YYYY-MM-DD')||'
'
    , NULL, NULL, NULL, NULL, 
                'PMTHE');        

      IF a_eror_emails IS NOT NULL THEN
        v_o$email.SEND_TO_DELIMITED(a_eror_emails);
      END IF;

      IF a_error_group IS NOT NULL THEN
        v_o$email.SEND_TO_GROUP(a_error_group);
      END IF;
      v_o$email.SERIALIZE;

      v_o$email := null;

      COMMIT;
    
  END;
  
BEGIN
  IF a_source_type NOT IN (PK_NAV.GC_TRS_SOURCE_TYPE_ORDPB) THEN
    a_error_mess := 'Nieobs³ugiwane Ÿród³o identyfikatora dokumentu: '||a_source_type;
    ERROR_REGISTER;
    RETURN;
  END IF;  
  
  -- Sprawdzenie statusu zamówienia
  BEGIN
    SELECT O.STATUS_ID, O.VERSION
    INTO   v_order_pb_status_id, v_prev_version
    FROM   APP_PBE.ORDERS O 
    WHERE  O.ID = a_ord_pb_id
    FOR  UPDATE
    WAIT 2;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN 
      a_error_mess := 'Nie znaleziono zamówienia na Vouchery Socjalne, zamówienie o id: '||a_ord_pb_id;
      ERROR_REGISTER;
      RETURN;
    WHEN e_wait_eoutoftime THEN
      a_error_mess := 'Nie powiod³a siê próba zablokowania zamówienia PB o id: '||a_ord_pb_id;
      ERROR_REGISTER;
      RETURN;
  END;
  
  IF v_order_pb_status_id != 'NEWKK' THEN
    v_warning    := 'UWAGA: Podpiêto p³atnoœæ pod zamówenie,na Vouchery Socjalne, które znajduje siê w statusie innym ni¿ NEWKK. Obecny status: : '||v_order_pb_status_id;
  END IF;
  
  IF a_doc_number      IS NULL THEN a_error_mess := 'Nie podano wartoœci dla pola a_doc_number.'; ERROR_REGISTER; RETURN; END IF;
  IF a_ord_pb_id        IS NULL THEN a_error_mess := 'Nie podano wartoœci dla pola Identyfikator zamówienia.'; ERROR_REGISTER; RETURN;  END IF;
  IF a_source_type     IS NULL THEN a_error_mess := 'Nie podano wartoœci dla pola Typ Ÿród³a identyfikatora zamówienia.'; ERROR_REGISTER; RETURN; END IF;
  
  -- Oblicz ile suma, 
  SELECT sum(T.AMOUNT) amount, 
         max(T.TRANSFER_DATE) payment_date, 
         SUM(DECODE(T.USED,'N',1,0)) NOT_USED_QUANTITY,
         SUM(T.TRANSFER_AMOUNT) TRANSFER_AMOUNT
  INTO   v_amount, 
         v_payment_date, 
         a_matched_row_quantity,
         v_transfer_amount
  FROM   APP_FIN.NAV_MC_TRANSFERS_PRC T
  WHERE  DOC_NUMBER = a_doc_number
  AND    T.ORDER_ID = a_ord_pb_id
  AND    T.ERROR_TIMESTAMP IS NULL
  AND    T.ERROR_MESSAGE   IS NULL
  AND    T.USED IN ('Y','N')
  GROUP  BY T.ORDER_ID, T.DOC_NUMBER;
    
  IF a_matched_row_quantity = 0 THEN
    a_error_mess := 'B³¹d aktualizacji przelewów: Nie znaleziono przelewów do zmodyfikowania. Zamówienie o id: '||a_ord_pb_id||', Doc Number: '||a_doc_number ;
    ERROR_REGISTER;
    RETURN;
  END IF;
  
  -- Update zamówienia
  ACT_ORDER_PB_TRANSFER_AMOUNT
                  (a_ord_pb_id           => a_ord_pb_id,
                   a_act_match_amount    => v_amount,
                   a_act_transfer_amount => v_transfer_amount,
                   a_act_payment_date    => v_payment_date,
                   a_error_mess          => a_error_mess
                   );                   

  -- ew. dodanie zadania do aktualizacji statusu zamówienia
  BEGIN
    SELECT E.VALUE_NUMBER 
    INTO   v_amount_to_pay
    FROM   APP_PBE.ORDER_ELEMENTS E
    WHERE  E.ORDER_ID = a_ord_pb_id
    AND    E.ELEMENT_ID = 'OWNCONA_KK';

  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      a_error_mess := 'Nieznaleziono wartoœci do zap³aty dla zamówienia o id: '||a_ord_pb_id||'.';
      ERROR_REGISTER;
      RETURN;
  END;

  IF v_amount_to_pay< v_amount THEN
    a_error_mess := 'Wyst¹pi³ nieobs³ugiwany b³¹d aktualizacji danych Zamówienia dla ord id '|| a_ord_pb_id||': Podpiêta kwota '|| v_amount||' jest wiêksza ni¿ kwota do op³acenia '||v_amount_to_pay;
    ERROR_REGISTER;
    RETURN;
  END IF;

  -- aktualizacja wersji zamówienia                       
  UPDATE APP_PBE.ORDERS O 
  SET    O.VERSION = O.VERSION + 1
  WHERE  O.ID = a_ord_pb_id;
  
  IF  a_error_mess IS NULL THEN

    SELECT E.REQUIRED_DATE
    INTO   v_required_date
    FROM   APP_PBE.ORDER_ELEMENTS E
    WHERE  E.ELEMENT_ID = 'PAYAMA_KK'
    AND    E.ORDER_ID   = a_ord_pb_id;


    IF trunc(v_required_date)< trunc(v_payment_date) THEN
      v_warning:= v_warning||' Uwaga: p³atnoœæ opóŸniona. Data p³atnoœci '|| to_char(v_payment_date,'YYYY-MM-DD')|| ', data koñcowa: '||to_char(v_required_date,'YYYY-MM-DD'); 
    END IF;
    
    -- Update przelewów
    UPDATE APP_FIN.NAV_MC_TRANSFERS_PRC T
    SET    T.USED = 'Y',
           T.MATCH_DATE = SYSDATE,
           T.MODIFIED_TIMESTAMP = SYSTIMESTAMP,
           T.MODIFIED_USER = USER,
           T.WARNING = v_warning
    WHERE  DOC_NUMBER = a_doc_number
    AND    T.ORDER_ID = a_ord_pb_id
    AND    T.USED = 'N'
    AND    T.ERROR_TIMESTAMP IS NULL
    AND    T.ERROR_MESSAGE   IS NULL;
      
    -- Sprawdzenie, czy odpowiednia liczba zupdateowana
    IF SQL%ROWCOUNT != a_matched_row_quantity THEN
      a_error_mess := 'B³¹d aktualizacji przelewów: Wyst¹pi³a chwilowa rozbie¿noœæ iloœci wierszy do modyfikacji.Zamówienie o id: '||a_ord_pb_id||', Doc Number: '||a_doc_number;
      ERROR_REGISTER;
      RETURN;
    END IF;

    
    IF v_amount_to_pay= v_amount
    and trunc(v_required_date)>= trunc(v_payment_date)
    AND v_order_pb_status_id = 'NEWKK' THEN
      Insert into APP_PBE.ASYNCH_JOBS
         (ID, TYPE, PARAMS, DESCRIPTION, STATUS, 
          NEXT_START_TIMESTAMP, START_TIMESTAMP, STOP_TIMESTAMP, ORDER_ID, VERSION, 
          CREATED_USER, CREATED_TIMESTAMP, MODIFIED_USER, MODIFIED_TIMESTAMP)
       Values
         (EAGLE.ASYN_JOB_SEQ.NEXTVAL, 'ORDER_TRANS','PAIDKK', NULL , 'N',
          NULL, NULL, NULL, a_ord_pb_id, 1, 
          USER, SYSTIMESTAMP, USER, SYSTIMESTAMP);
    END IF;
    
    COMMIT;
    
    /*
    IF trunc(v_required_date)< trunc(v_payment_date) THEN
      SEND_DELAYED_PAYMENT_MAIL;
    END IF;
    */
    
    IF v_warning IS NOT NULL THEN
      SEND_WARNING_MAIL;
    END IF;

  ELSE
    ERROR_REGISTER;
  END IF;

EXCEPTION  
  WHEN OTHERS THEN
    PK_ERROR.WRITE_ERROR(-20001,'Wyst¹pi³ nieobs³ugiwany b³¹d aktualizacji przelewów. Zamówienie o id: '||a_ord_pb_id||', Doc Number: '||a_doc_number||'; '||DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
    a_error_mess := 'Wyst¹pi³ nieobs³ugiwany b³¹d aktualizacji przelewów. Zamówienie o id: '||a_ord_pb_id||', Doc Number: '||a_doc_number||'; '||DBMS_UTILITY.FORMAT_ERROR_BACKTRACE;
    ERROR_REGISTER;
    RETURN;
END;                       

PROCEDURE MATCH_PAYMENTS(a_error_mess  OUT VARCHAR2)
IS
  v_error_mess VARCHAR2(30000);
  v_eror_emails   EAGLE.ADM_PARAMETERS.VALUE%TYPE;
  v_error_group   EAGLE.ADM_PARAMETERS.VALUE%TYPE;
  v_matched_row_quantity NUMBER;
BEGIN

  v_eror_emails := pk_utils.get_param_value('PAY_MATCH_ERR_EMAIL_LIST');  
  v_error_group := pk_utils.get_param_value('PAY_MATCH_ERR_GROUP');  
  
  FOR K IN (SELECT T.ORDER_ID, 
                   T.DOC_NUMBER,
                   T.SOURCE_TYPE
            FROM   APP_FIN.NAV_MC_TRANSFERS_PRC T
            WHERE  T.USED = 'N'
            AND    T.ERROR_TIMESTAMP IS NULL
            AND    T.ERROR_MESSAGE   IS NULL
            AND    T.SOURCE_TYPE NOT IN ('ORD')
            GROUP  BY T.ORDER_ID, T.DOC_NUMBER, T.SOURCE_TYPE)
  LOOP

    v_error_mess := NULL;
    MATCH_PAYMENT(a_doc_number  => K.DOC_NUMBER,
                  a_ord_pb_id   => K.ORDER_ID,
                  a_source_type => K.SOURCE_TYPE,
                  a_eror_emails => v_eror_emails,
                  a_error_group => v_error_group,
                  a_error_mess  => v_error_mess,
                  a_matched_row_quantity=> v_matched_row_quantity);

  END LOOP;

EXCEPTION  
  WHEN OTHERS THEN
      PK_ERROR.WRITE_ERROR (-20001,
      'Wyst¹pi³ nieobs³ugiwany b³¹d aktualizacji przelewów. '||DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
    a_error_mess := 'Wyst¹pi³ nieobs³ugiwany b³¹d aktualizacji przelewów. '|| DBMS_UTILITY.FORMAT_ERROR_BACKTRACE; 
    return;
END;

----------------------------------------------------
---------------------------------------------------
-----------------------------------------------------

FUNCTION CONVERT_ORD_PB_ID(a_ord_pb_id IN NUMBER) RETURN NUMBER
IS
  v_fy      VARCHAR2(4);
  v_ret     VARCHAR2(40);
BEGIN

  PK_AUDIT.AUDIT_MODULE('PK_NAV','CONVERT_ORD_PB_ID',NULL);
  IF a_ord_pb_id IS NULL THEN
    PK_ERROR.RAISE_ERROR(-20001, 'B³¹d procedury CONVERT_ORD_PB_ID. Nie podano id zamówienia'); -- mo¿na dodaæ obs³ugê jeszcze jednej cyfry => doklejaæ na pocz¹tku tylko 9
  END IF;
--  IF a_ord_pb_id>999999 THEN
--    PK_ERROR.RAISE_ERROR(-20001, 'B³¹d procedury CONVERT_ORD_PB_ID. Zbyt du¿e id zamówienia'); -- mo¿na dodaæ obs³ugê jeszcze jednej cyfry => doklejaæ na pocz¹tku tylko 9
--  END IF;
  
 BEGIN
   select CASE 
            WHEN TO_NUMBER(to_char(O.ORDER_DATE,'MM'))<9 THEN SUBSTR(to_char(O.ORDER_DATE,'YYYY'),3) 
            ELSE SUBSTR(to_char(ADD_MONTHS(O.ORDER_DATE,12),'YYYY'),3)
          END FY
   INTO   v_fy       
   from   APP_PBE.ORDERS o
   WHERE  O.ID = a_ord_pb_id;
 EXCEPTION
  WHEN NO_DATA_FOUND THEN
    v_fy := SUBSTR(PK_UTILS.Get_Param_Value('MILLESIME'),3);
 END;
  
 SELECT v_fy||500000+
       +LPAD(MOD(a_ord_pb_id, 500000),6,'0')
 INTO  v_ret
 FROM  DUAL; 
 
 RETURN v_ret;
 
EXCEPTION
  WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'MOD_NAV_COPBID');
END;

FUNCTION GET_INVOICE_ID_ORDPB(a_ord_pb_id IN VARCHAR2,
                              a_ext_ord_pb_no IN VARCHAR2, 
                              a_nota_no IN VARCHAR2) RETURN NUMBER
IS
   v_retval NUMBER(10);
   v_id app_fin.nav_invoice_id.EXTENTION_ID%TYPE;
BEGIN

  IF a_ord_pb_id IS NULL THEN
     PK_ERROR.RAISE_ERROR(-20002, 'a_ord_pb_id nie moze byc NULL!');
  END IF;
  IF a_ext_ord_pb_no IS NULL THEN
     PK_ERROR.RAISE_ERROR(-20002, 'a_ext_ord_pb_no nie moze byc NULL!');
  END IF;
    
  v_id := NULL;
  IF a_nota_no IS NULL THEN
    PK_ERROR.RAISE_ERROR(-20002, 'Nie podano numeru noty');
  END IF;
    
  BEGIN
         
     SELECT nid.EXTENTION_ID
     INTO   v_id
     FROM   app_fin.nav_invoice_id nid
     WHERE  NID.ORD_PB_ID = a_ord_pb_id
       AND  NID.NOTA_NO   = a_nota_no;
               
  EXCEPTION
    WHEN no_data_found THEN
         
         SELECT nvl(max(nid.EXTENTION_ID),0)
         INTO   v_id
         FROM   APP_FIN.NAV_INVOICE_ID NID
         WHERE  NID.ORD_PB_ID = a_ord_pb_id;
                 
         v_id := v_id + 1;
                 
         add_nav_invoice_id(a_proforma_no  => a_ext_ord_pb_no,
                            a_invoice_no   => NULL,
                            a_nota_no      => a_nota_no,
                            a_extention_id => v_id,
                            a_ord_pb_id    => a_ord_pb_id);
         
  END;
   
  RETURN to_number(to_char(CONVERT_ORD_PB_ID(a_ord_pb_id))||lpad(v_id,2,'0'));

EXCEPTION
  WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'MOD_NAV_GINOPB');
  
END;

PROCEDURE NAV_INVOICES_PB (a_delete_prev_items VARCHAR2 -- Czy usuwaæ wiersze z APP_FIN.NAV_INVOICES (Y / N)
)
IS
  v_inv_cnt             NUMBER(10);
  v_nota_cnt            NUMBER(10);
  v_invoice_no          APP_FIN.NAV_INVOICES.INVOICE_NO%TYPE;
  v_invoice_date        APP_FIN.NAV_INVOICES.INVOICE_DATE%TYPE;
  v_gross_commission    APP_FIN.NAV_INVOICES.GROSS_COMMISION%TYPE;
  v_nota_no             APP_FIN.NAV_INVOICES.NOTA_NO%TYPE;
  v_nota_date           APP_FIN.NAV_INVOICES.NOTA_DATE%TYPE;
  v_total_fv            APP_FIN.NAV_INVOICES.TOTAL_FV%TYPE;  
  v_posting_group       APP_FIN.NAV_INVOICES.POSTING_GROUP%TYPE;   
BEGIN

  PK_AUDIT.AUDIT_MODULE('PK_NAV','NAV_INVOICES_PB',NULL);

  IF a_delete_prev_items = 'Y' THEN
    DELETE FROM APP_FIN.NAV_INVOICES;
  END IF;
  
  FOR l_ord IN (
      SELECT O.ID                                        ORD_ID, -- il.SOURCE ord_id, 
             'ORDPB'                                     SOURCE_TYPE, -- il.source_type, 
             NULL                                        INV_ID,-- i.ID inv_id,
             I.ID                                        INDIVIDUAL_ID, -- i.cus_id cus_id, 
             E.ID                                        ENTERPRISE_ID,
             O.EXTERNAL_ORDER_ID                         EXTERNAL_ORDER_ID, -- i.invoice_number proforma_no,
             O.ORDER_DATE                                ORDER_DATE, -- i.invoice_date proforma_date,
             EL.VALUE_NUMBER                             PAYMENT_AMOUNT,  -- i.gross_amount proforma_amount, 
             NVL(E.NAME, I.FIRST_NAME ||' '|| I.LAST_NAME ) COR_NAME, -- i.cor_name cor_name,
             O.ADDRESS_CORR cor_ad_line_1, -- i.cor_ad_line_1 cor_ad_line_1,
             Z.ZIP_CODE||' '||Z.CITY_NAME cor_ad_line_4, -- i.cor_ad_line_4 cor_ad_line_4,
             O.GRANT_PROGRAM_ID,
             O.GRANT_APPLICATION_ID,
             C.CODE CODE
      FROM   APP_PBE.ORDERS O
             JOIN      APP_PBE.CONTRACTS   C ON C.ID = O.CONTRACT_ID
             JOIN      APP_PBE.INDIVIDUALS I ON I.ID = C.INDIVIDUAL_ID
             LEFT JOIN APP_PBE.ENTERPRISES E ON E.ID = C.ENTERPRISE_ID
             JOIN      APP_PBE.ORDER_ELEMENTS EL ON EL.ELEMENT_ID = 'OWNCONA_KK' AND EL.ORDER_ID =  O.ID 
             LEFT JOIN EAGLE.ZIP_CODES Z ON (Z.ACTIVE = '1' AND Z.ID=O.ZIP_CODE_CORR_ID)
      WHERE  O.STATUS_ID IN ('NEWKK')
      AND    O.EXTERNAL_ORDER_ID IS NOT NULL 
      )
   LOOP

  --     Czy potrzebne?
  --      IF  l_ord.GRANT_APPLICATION_ID = 
  --      AND l_ord.GRANT_PROGRAM_ID     =  THEN
      v_posting_group := 'KLIENT WUP';
  --      END IF;
        
            
      v_inv_cnt          := 0;
      v_invoice_no       := NULL;
      v_invoice_date     := NULL;
      v_gross_commission := NULL;

      v_nota_cnt      := 0;
      v_nota_no       := NULL;
      v_nota_date     := NULL;
      v_total_fv      := NULL;
        
        
      FOR l_nota IN (SELECT  il.SOURCE             ord_id,  
                             i.invoice_number      nota_no, 
                             i.invoice_date        nota_date,
                             nvl(i.hand_made,'N')  hand_made,
                             SUM (il.gross_amount) total_fv, 
                             O.EXTERNAL_ORDER_ID   EXTERNAL_ORDER_ID
                        FROM app_fin.invoices i
                             join app_fin.invoice_lines il on i.ID = il.inv_id and IL.SOURCE_TYPE = 'ORDPB'
                             join APP_PBE.ORDERS o on O.ID = IL.SOURCE 
                       WHERE i.TYPE IN ('NPB') 
                         AND il.source_type = l_ord.SOURCE_TYPE
                         AND il.source = l_ord.ord_id
                    GROUP BY il.SOURCE, i.invoice_number,
                             i.invoice_date,  nvl(i.hand_made,'N'),
                             O.EXTERNAL_ORDER_ID
                          ORDER BY CONVERT_INVOICE_NUMBER(i.invoice_number))
       LOOP
         
          IF v_nota_cnt = 0 AND l_nota.hand_made = 'N' THEN
            
             v_nota_no       := l_nota.nota_no;
             v_nota_date     := l_nota.nota_date;
             v_total_fv      := l_nota.total_fv;

          ELSE

             ADD_NAV_INVOICE(A_INVOICE_ID      => GET_INVOICE_ID_ORDPB(a_ord_pb_id => l_nota.ord_id,
                                                                       a_ext_ord_pb_no => l_nota.EXTERNAL_ORDER_ID, 
                                                                       a_nota_no => l_nota.nota_no), 
                             A_ORD_ID          => l_ord.ORD_ID,
                             A_PROFORMA_NO     => l_ord.EXTERNAL_ORDER_ID, 
                             A_PROFORMA_DATE   => l_ord.ORDER_DATE,
                             A_PROFORMA_AMOUNT => l_ord.PAYMENT_AMOUNT,
                             A_INVOICE_NO      => NULL, 
                             A_INVOICE_DATE    => NULL,
                             A_GROSS_COMMISION => NULL,
                             A_NOTA_NO         => l_nota.nota_no, 
                             A_NOTA_DATE       => l_nota.nota_date,
                             A_TOTAL_FV        => l_nota.total_fv,
                             A_COMPANY_CODE    => l_ord.code, 
                             A_COMPANY_CODE_TEMP => NULL,
                             A_COMPANY_NAME    => l_ord.cor_name,
                             A_ADR_INV_LINE_1  => l_ord.cor_ad_line_1, 
                             A_ADR_INV_LINE_2  => l_ord.cor_ad_line_4,
                             A_POSTING_GROUP   => v_posting_group,
                             A_SOURCE_TYPE     => 'ORDPB',               
                             A_PAYMENT_CONF_TYPE => 'PRC');          
            
          END IF;
         
          v_nota_cnt := v_nota_cnt + 1;
       END LOOP;


       ADD_NAV_INVOICE(A_INVOICE_ID      => to_char(CONVERT_ORD_PB_ID(l_ord.ORD_ID)),
                       A_ORD_ID          => l_ord.ord_id,
                       A_PROFORMA_NO     => l_ord.EXTERNAL_ORDER_ID, 
                       A_PROFORMA_DATE   => l_ord.ORDER_DATE,
                       A_PROFORMA_AMOUNT => l_ord.PAYMENT_AMOUNT,
                       A_INVOICE_NO      => v_invoice_no, 
                       A_INVOICE_DATE    => v_invoice_date,
                       A_GROSS_COMMISION => v_gross_commission,
                       A_NOTA_NO         => v_nota_no, 
                       A_NOTA_DATE       => v_nota_date,
                       A_TOTAL_FV        => v_total_fv,
                       A_COMPANY_CODE    => l_ord.code,
                       A_COMPANY_CODE_TEMP => NULL,
                       A_COMPANY_NAME    => l_ord.cor_name,
                       A_ADR_INV_LINE_1  => l_ord.cor_ad_line_1, 
                       A_ADR_INV_LINE_2  => l_ord.cor_ad_line_4, 
                       A_POSTING_GROUP   => v_posting_group,
                             A_SOURCE_TYPE     => 'ORDPB',               
                             A_PAYMENT_CONF_TYPE => 'PRC');  
      
   END LOOP;   
  
 COMMIT;



EXCEPTION
    WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'NAV_INVPB');
END;

PROCEDURE SEND_PAY_MORE_THEN_PROF(a_order_id IN NUMBER,
                                  a_doc_number      IN VARCHAR2,
                                  a_transfer_amount IN NUMBER,
                                  a_proforma_amount IN NUMBER,
                                  a_eror_emails     IN VARCHAR2,
                                  a_error_group     IN VARCHAR2
                                  ) is
PRAGMA AUTONOMOUS_TRANSACTION;
  v_o$email       T$EMAIL; 
BEGIN 

  PK_ERROR.WRITE_ERROR(-20001,'SEND_PAY_MORE_THEN_PROF: Ostrze¿enie dotycz¹ce podpiêcia p³atnoœci do zamówienie (wartoœæ p³atnoœci > wartoœæ proformy): '|| 
' a_transfer_amount   : '||a_transfer_amount    ||    
', a_proforma_amount : '||a_proforma_amount   
);

  v_o$email := t$email('EAGLE', pk_utils.get_param_value('EMAIL_EAGLE'), NULL, 'UWAGA: PODPIÊTO P£ATNOŒÆ WIÊKSZ¥ NI¯ WARTOŒÆ PROFORMY', 
'Numer zamówienia: '||a_order_id    ||'    
Numer dokumentu: '||a_doc_number   ||'    
Suma p³atnoœci: '||a_transfer_amount   ||'    
jest wiêksza ni¿ wartoœæ zamówienia:: '||a_proforma_amount   ||'.    

Nale¿y zweryfikowaæ p³atnoœæ zamówienie po stronie Navision.
'
  , NULL, NULL, NULL, NULL, 
              'PMTHE');        

    IF a_eror_emails IS NOT NULL THEN
      v_o$email.SEND_TO_DELIMITED(a_eror_emails);
    END IF;

    IF a_error_group IS NOT NULL THEN
      v_o$email.SEND_TO_GROUP(a_error_group);
    END IF;

    v_o$email.SERIALIZE;

    v_o$email := null;

    COMMIT;
EXCEPTION
  WHEN OTHERS THEN
    PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'NAV_SPMTP');
END;

END;
/