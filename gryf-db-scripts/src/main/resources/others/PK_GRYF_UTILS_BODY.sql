create or replace PACKAGE BODY             eagle.PK_GRF_UTILS AS


  FUNCTION Amount_Verbally(a_amount NUMBER) RETURN VARCHAR2 IS
    BEGIN
      RETURN EAGLE.Amount_Verbally(a_amount);
    END Amount_Verbally;


  FUNCTION Next_Business_Day(a_date DATE) RETURN DATE IS
    BEGIN
      RETURN EAGLE.F_NEXT_WORK_DAYS(a_date);
    END;


  FUNCTION Get_Nth_Business_Day(a_date DATE, a_nth_day NUMBER) RETURN DATE
  IS
    v_next_business_day DATE;
    BEGIN
      IF a_nth_day < 0 THEN
        PK_ERROR.RAISE_ERROR(-20001, 'GRF_UTL_GNBD: niew³aœciwa liczba dni ='|| a_nth_day);
      END IF;
      v_next_business_day :=  a_date;
      FOR i IN 1 .. a_nth_day LOOP
        v_next_business_day := Next_Business_Day(v_next_business_day);
      END LOOP;
      RETURN v_next_business_day;
    END Get_Nth_Business_Day;


  FUNCTION Get_Nth_Callendar_Day(a_date DATE, a_nth_day NUMBER) RETURN DATE
  IS
    BEGIN
      IF a_nth_day < 0 THEN
        PK_ERROR.RAISE_ERROR(-20002, 'GRF_UTL_GNCD: niew³aœciwa liczba dni ='|| a_nth_day);
      END IF;
      RETURN a_date + a_nth_day;
    END Get_Nth_Callendar_Day;


  FUNCTION Get_Nth_Day(a_date DATE, a_nth_day NUMBER, a_day_type VARCHAR2) RETURN DATE
  IS
    BEGIN
      IF UPPER(a_day_type) = 'B' THEN
        RETURN Get_Nth_Business_Day(a_date, a_nth_day);
      ELSIF UPPER(a_day_type) = 'C' THEN
        RETURN  Get_Nth_Callendar_Day(a_date, a_nth_day);
      ELSE
        PK_ERROR.RAISE_ERROR(-20003, 'GRF_UTL_GND: niew³aœciwy rodzaj dnia ='|| a_day_type);
      END IF;
    END Get_Nth_Day;

  function var( a_name varchar2, a_value varchar2) return varchar2
  is
    begin
      return a_name ||'='||nvl(a_value,'<null>');
    end var;
  function var( a_name varchar2, a_value date) return varchar2
  is
    begin
        return a_name ||'='||nvl(to_char(a_value,'YYYY-MM-DD'),'<null>');
    end var;
  function ivar( a_name varchar2, a_value number) return varchar2
  is
    begin
        return a_name ||'='||NVL(to_char(a_value,'fm99999999999990'), '<null>');
    end ivar;
    function ivar( a_value number) return varchar2
    is
    begin
        return NVL(to_char(a_value,'fm99999999999990'), '<null>');
    end ivar;


  --
  FUNCTION GET_INVOICE_NUMBER(a_type    IN VARCHAR2,
                              a_o$order IN OUT NOCOPY t$order,
                              a_tsk_id  IN NUMBER,
                              a_rem_id  IN NUMBER,
                              a_prd_code IN VARCHAR2 DEFAULT NULL) RETURN VARCHAR2
  IS
    v_retval VARCHAR2(50);
    v_num NUMBER;
    v_millesime VARCHAR2(300);
    v_code VARCHAR2(10);
    v_g    VARCHAR2(1);
    BEGIN

      v_millesime := PK_UTILS.Get_Param_Value('MILLESIME');

      IF a_type = 'NOPB' THEN
        v_code := 'NOTE_PB';
      ELSIF a_type IN ('N','R') THEN
        v_code := 'NOTE';
      ELSIF a_type IN ('NU') THEN
        v_code := 'NOTE_UZ';
      ELSIF a_type = 'NUPB' THEN
        v_code := 'NOTE_UZ_PB';
      END IF;

      v_num := PK_UTILS.Get_Document_Number(v_code);

      IF a_type = 'P' THEN

        v_retval := to_char(v_num)||'/'||substr(v_millesime,3,2)||'P';

        IF a_o$order IS NOT NULL THEN
          v_retval := v_retval||a_o$order.ord_lines.GET_PRODUCTS_TYPES_LIST;
        ELSIF a_tsk_id IS NOT NULL THEN
          v_retval := v_retval||'U'||t$task_product.GET_PRODUCT_TYPES_LIST(a_tsk_id);
        END IF;

      ELSIF a_type = 'IC' THEN

        v_retval := to_char(v_num)||'/'||substr(v_millesime,3,2)||'K';

        IF a_o$order IS NOT NULL THEN
          IF a_o$order.customer.customer_type = 'C' THEN
            v_g:='G';
          END IF;
          v_retval := v_retval
                      ||v_g
                      ||a_o$order.ord_lines.GET_PRODUCTS_TYPES_LIST;
        ELSIF a_tsk_id IS NOT NULL THEN
          IF T$TASK.GET_CUSTOMER_TYPE(a_tsk_id)='C' THEN
            v_g:='G';
          END IF;
          v_retval := v_retval
                      ||v_g
                      ||'U'
                      ||NVL(t$task_product.GET_PRODUCT_TYPES_LIST(a_tsk_id),a_prd_code);
        END IF;

      ELSIF a_type = 'IA' THEN

        v_retval := to_char(v_num)||'/'||substr(v_millesime,3,2)||'A';

      ELSIF a_type = 'T' THEN

        v_retval := to_char(v_num)||'/'||substr(v_millesime,3,2)||'/TNT';

      ELSIF a_type IN ('N', 'R') THEN

        v_retval := to_char(v_num)||'/'||substr(v_millesime,3,2);

        IF a_o$order IS NOT NULL THEN
          IF a_o$order.customer.customer_type = 'C' THEN
            v_g:='G';
          ELSE
            v_g:='/';
          END IF;
          v_retval := v_retval||v_g||a_o$order.ord_lines.GET_PRODUCTS_TYPES_LIST;
        ELSIF a_tsk_id IS NOT NULL THEN
          IF T$TASK.GET_CUSTOMER_TYPE(a_tsk_id)='C' THEN
            v_g:='G';
          END IF;
          v_retval := v_retval||v_g||'O'||t$task_product.GET_PRODUCT_TYPES_LIST(a_tsk_id);
        END IF;

      ELSIF a_type = 'NU' THEN

        v_retval := to_char(v_num)||'/'||substr(v_millesime,3,2);

        IF a_rem_id IS NOT NULL THEN
          IF T$REMITTANCE.GET_CUSTOMER_TYPE(a_rem_id) = 'C' THEN
            v_g:='G';
          END IF;
          v_retval := v_retval||v_g||'U'||t$remittance_line.GET_PRODUCT_TYPES_LIST(a_rem_id);
        ELSE
          PK_ERROR.RAISE_ERROR(-20007, 'Parametr a_rem_id nie moze byc rowny NULL');
        END IF;
      ELSE
        PK_ERROR.RAISE_ERROR(-20002, 'INV_GIN: Zly kod a_type'||a_type);
      END IF;

      RETURN v_retval;

      EXCEPTION
      WHEN OTHERS THEN
      PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'INV_GPN');
    END Get_Invoice_Number;


    Function Get_Req_Payment_Date( a_order_id in number ) return date
    is
        a_ret DATE;
        
        cursor get_val (p_order_id number)
        is
        select oe.required_date
        from app_pbe.order_elements oe
        where OE.ELEMENT_ID in ('PAYAMA_KK' /*,'COND_KK'*/) -- wymagalnoœæ pola kwota wp³aty
        and OE.ORDER_ID = p_order_id;
        
    begin
        open get_val(a_order_id);
        fetch get_val into a_ret;
        close get_val;
        return a_ret;
    end;

  -- utwórz notê obci¹¿eniowo-ksiêgow¹
    PROCEDURE Create_Debit_Note_Worker( o_inv_id OUT NUMBER, o_invoice_number OUT VARCHAR2, o_invoice_type OUT VARCHAR2, o_invoice_date OUT DATE, a_order_id IN NUMBER, a_sign_user IN VARCHAR2 )
  is
    v_o$invoice t$invoice;
    v_o$fake_order t$order;
        a_line_created  BOOLEAN := FALSE;
        a_type       CONSTANT VARCHAR2(200) := 'NPB';
        source_type_ CONSTANT APP_FIN.INVOICE_LINE_SOURCE_TYPES.TYPE%TYPE := 'ORDPB';

    cursor get_order_data( p_id number )
    is
      select *
      from (
        select oe.order_id, o.external_order_id, o.status_id, CON.CONTRACT_TYPE_ID, con.enterprise_id, con.individual_id, o.vouchers_number vouchers_assigned,
          o.order_date, Con.SIGN_DATE,
          O.PBE_PRD_ID, pp.name pbe_name, pp.value pbe_value,
          oe.element_id, oe.value_number
        from app_pbe.order_elements oe
          join app_pbe.orders o on (o.id=OE.ORDER_ID)
          left outer join APP_PBE.PBE_PRODUCTS pp on (O.PBE_PRD_ID = PP.prd_id)
          join APP_PBE.CONTRACTS con on (o.contract_id = con.id)
        --and o.status_id = 'NEWKK'
             where oe.order_id = p_id
      )
        pivot ( min(value_number)
          for element_id
          in (
            'PRDAMO_KK' unit_price,
            'PRDNUM_KK' quantity,
            'ORDAMO_KK' order_amount,
            'GRAAMO_KK'  granted_amount,
            'OWNCONP_KK' payment_share,
            'OWNCONA_KK' payment_amount,
            'PAYAMA_KK' paid_amount
          )
        )
      order by 1;
    begin
        PK_AUDIT.AUDIT_MODULE('PK_GRF_UTILS', 
                                'Create_Debit_Note_Worker', 
                                ivar('A_ORDER_ID',a_order_id)||var('; A_SIGN_USER', a_sign_user)||var('; USER',USER ) );

      v_o$invoice := t$invoice();

      -- w typach:
      -- * refaktor metody GEN_CUS_NOTE
      -- * refaktor metody self.invoice_number := t$invoice.GET_INVOICE_NUMBER
      -- * refaktor metody serialize (obs³uga nowego typu)

      o_invoice_type   := a_type;
      o_invoice_date   := SYSDATE;

      v_o$invoice.type           := o_invoice_type;
      v_o$invoice.invoice_date   := o_invoice_date;
      v_o$invoice.hand_made      := 'N';
      v_o$invoice.CONFIRMED  := 'N';
      v_o$invoice.ELECTRONIC := 'N';
      v_o$invoice.payment_mean := 'PRZ'; -- przelew
            v_o$invoice.payment_date := Get_Req_payment_Date(a_order_id);
            v_o$invoice.sign_user    := a_sign_user;

      o_invoice_number := t$invoice.get_invoice_number(
          a_type    => v_o$invoice.type,
          a_o$order => v_o$fake_order,
          a_tsk_id  => NULL,
          a_rem_id  => NULL,
          a_prd_code => '30');
      v_o$invoice.invoice_number := o_invoice_number;

      -- adres
      v_o$invoice.COR_ADDRESS := t$address;

            FOR rec_ IN get_order_data(a_order_id) 
            LOOP
                
                CASE rec_.contract_type_id 
                WHEN 'ENT' THEN
                    SELECT e.name, 
                           e.vat_reg_num,
                           E.ACCOUNT_PAYMENT, -- rachunek do wp³aty
                           e.address_invoice,
                           z.zip_code ||' '|| z.city_name
                      INTO v_o$invoice.cor_name, 
                           v_o$invoice.vat_reg_num,
                           v_o$invoice.account,
                           v_o$invoice.cor_address.ad_line_1, 
                           v_o$invoice.cor_ad_line_4
                      FROM APP_PBE.ENTERPRISES e
                           LEFT OUTER JOIN EAGLE.ZIP_CODES z ON (z.active = '1' AND z.id=e.zip_code_invoice)
                      WHERE e.id = rec_.enterprise_id;
                WHEN 'IND' THEN
                    SELECT INITCAP(i.first_name || ' '|| i.last_name),
                           I.ACCOUNT_PAYMENT, -- rachunek do wp³aty
                           i.address_invoice,
                           z.zip_code ||' '|| z.city_name
                      INTO v_o$invoice.cor_name,
                           v_o$invoice.account,
                           v_o$invoice.cor_address.ad_line_1,
                           v_o$invoice.cor_ad_line_4
                      FROM APP_PBE.INDIVIDUALS i
                           LEFT OUTER JOIN EAGLE.ZIP_CODES Z ON (z.active = '1' AND z.id=i.zip_code_invoice)
                      WHERE i.id = rec_.individual_id;
                ELSE
                    PK_ERROR.RAISE_ERROR(-20004, 'GRF_UTL_CNTRTYP: Nieznany typ kontraktu = '|| NVL(rec_.contract_type_id, '<NULL>') );                
                END CASE;

        v_o$invoice.pattern  := t$invoice.get_invoice_pattern(v_o$invoice.type);
        v_o$invoice.lines     := t$$invoice_lines();
        v_o$invoice.lines.ADD_(
                        A_CODE         => rec_.pbe_prd_id,  
            A_QUANTITY     => rec_.quantity,
                        A_AMOUNT       => rec_.payment_amount,
            A_SOURCE       => a_order_id,
                        A_TYPE         => 'CP',             -- INVOICE_LINE_CODES.TYPE
            A_SOURCE_TYPE  => source_type_,
            A_CUS_ID       => NULL,
            A_ROUND_MODE   => 'NET');
                        
                a_line_created := TRUE;
            END LOOP;
            
            IF NOT a_line_created THEN
                pk_error.raise_error(-20005, 'GRF_UTL_NODEBLINE: Brak linii dla noty obci¹¿eniowej - zamówienie '|| ivar(a_order_id) ||' niekompletne?' );
            END IF;
            
      v_o$invoice.lines.CALCULATE_AMOUNTS('NET');

      v_o$invoice.NET_AMOUNT   := v_o$invoice.LINES.GET_NET_AMOUNT;
      v_o$invoice.VAT_AMOUNT   := v_o$invoice.LINES.GET_VAT_AMOUNT;
      v_o$invoice.GROSS_AMOUNT := v_o$invoice.LINES.GET_GROSS_AMOUNT;
      v_o$invoice.CHECK_AMOUNTS();

      v_o$invoice.serialize();
      o_inv_id := v_o$invoice.id;
            v_o$invoice := NULL;
      PK_AUDIT.AUDIT_MODULE('PK_GRF_UTILS', 'Create_Debit_Note_Worker', 'OUT: '||
                                                                        ivar('o_inv_id', o_inv_id)||
                                                                        var(', o_invoice_number', o_invoice_number)||
                                                                        var(', o_invoice_type', o_invoice_type)||
                                                                        var(', o_invoice_date',o_invoice_date)||
                                                                        ivar(', A_ORDER_ID',a_order_id)||
                                                                        var(', USER', USER ));
      EXCEPTION
      WHEN OTHERS THEN
      PK_ERROR.WRITE_ERROR;
      raise;
    END Create_Debit_Note_Worker;


  -- utwórz notê obci¹¿eniowo-ksiêgow¹
    PROCEDURE Create_Pb_Cus_Note( o_inv_id OUT NUMBER, o_invoice_number OUT VARCHAR2, o_invoice_type OUT VARCHAR2, o_invoice_date OUT DATE, o_87_invoice_number OUT VARCHAR2, a_order_id IN NUMBER, a_sign_user IN VARCHAR2 DEFAULT USER)
  is
        v_suffix VARCHAR2(255); 
    begin
        Create_Debit_Note_Worker( o_inv_id, o_invoice_number, o_invoice_type, o_invoice_date, a_order_id, a_sign_user );
        v_suffix := PK_UTILS.GET_PARAM_VALUE( 'PB_NOTE87_SUFFIX' );
        o_87_invoice_number := o_invoice_number||v_suffix;
    end Create_Pb_Cus_Note;


    -- procedura robocza - nota uznaniowa dla Uczestnika
  PROCEDURE Create_Credit_Note_Worker (
    o_inv_id         OUT NUMBER,
    o_invoice_number OUT VARCHAR2,
    o_invoice_type   OUT VARCHAR2,
    o_invoice_date   OUT DATE,
    a_ermb_id        IN NUMBER,
        a_sign_user      IN VARCHAR2,
        a_type           IN varchar2 ) -- default 'CUS'    
  is
        a_line_created BOOLEAN := FALSE;
        v_itype       CONSTANT VARCHAR2(200)                               := 'NUPB';
        v_source_type CONSTANT APP_FIN.INVOICE_LINE_SOURCE_TYPES.TYPE%TYPE := 'REMPB';
        v_il_code     CONSTANT APP_FIN.INVOICE_LINE_CODES.CODE%TYPE        := '30RMB1';
        v_o$fake_order  t$order;
    v_o$invoice    t$invoice;


        cursor get_rmb_data (p_rmb_id number)
    is
           select      
               er.id er_id,
               pip.id pip_id,
               count(ipu.id) OVER (PARTITION BY er.id) duplicate_counter,
               er.status_id                            rmb_status_id, 
               er.arrival_date            arrival_date,  
                    SXO_IND_AMOUNT_DUE_TOTAL overpaid_amount,
               PIP.ORDER_ID               pip_order_id, 
        O.CONTRACT_ID, C.CONTRACT_TYPE_ID, C.ENTERPRISE_ID, C.INDIVIDUAL_ID,
                    pip.prd_id pip_prd_id,
        ti.training_id,
               ti.grant_program_id, 
               (select program_name from app_pbe.grant_programs gp where gp.id=ti.grant_program_id) program_name, 
               ti.status_id ti_status_id
      from APP_PBE.E_REIMBURSEMENTS er
          join APP_PBE.TI_TRAINING_INSTANCES ti on (ER.TI_TR_INST_ID=ti.id and er.type_id = 'TI_INST')
          left join APP_PBE.PBE_PRODUCT_INSTANCE_POOL_USES ipu on IPU.TRAINING_INSTANCE_ID = ti.id -- <- mog¹ istnieæ dwie
        left join APP_PBE.PBE_PRODUCT_INSTANCE_POOLS pip on IPU.product_instance_pool_id=PIP.ID
        left join app_pbe.orders o on pip.order_id = o.id
        left join APP_PBE.CONTRACTS c on O.CONTRACT_ID = C.ID
          where er.id = p_rmb_id
          UNION ALL
          select
               er.id  er_id,
               pip.id pip_id,
               count(PIP.ID) OVER (PARTITION BY er.id) duplicate_counter,
               er.status_id                            rmb_status_id, 
               er.arrival_date            arrival_date,  
               SXO_IND_AMOUNT_DUE_TOTAL   overpaid_amount,
               PIP.ORDER_ID               pip_order_id, 
               O.CONTRACT_ID, C.CONTRACT_TYPE_ID, C.ENTERPRISE_ID, C.INDIVIDUAL_ID,
               pip.prd_id pip_prd_id,  
               null training_id,   
               c.grant_program_id grant_program_id, 
               (select program_name from app_pbe.grant_programs gp where gp.id=c.grant_program_id) program_name, 
               null ti_status_id
            from APP_PBE.E_REIMBURSEMENTS er
                 join APP_PBE.PBE_PRODUCT_INSTANCE_POOLS pip on er.PRODUCT_INSTANCE_POOL_ID = pip.ID and er.type_id = 'URSVD_POOL'
                 join APP_PBE.ORDERS o on pip.ORDER_ID = o.ID
                 join APP_PBE.CONTRACTS c on O.CONTRACT_ID = C.ID
           where er.id = p_rmb_id
        order by er_id, pip_id;

    BEGIN
        PK_AUDIT.AUDIT_MODULE('PK_GRF_UTILS', 'Create_Credit_Note_Worker', ivar('A_E_RMB_ID',a_ermb_id)||var('; A_SIGN_USER', a_sign_user)||var('; A_TYPE', a_type )||var('; USER',USER ) );

      v_o$invoice := t$invoice();
      o_invoice_type   := v_itype;
        o_invoice_date   := SYSDATE;

      v_o$invoice.type           := o_invoice_type;
      v_o$invoice.invoice_date   := o_invoice_date;
      v_o$invoice.hand_made      := 'N';
      v_o$invoice.CONFIRMED  := 'N';
      v_o$invoice.ELECTRONIC := 'N';
        v_o$invoice.SIGN_USER  := a_sign_user;

      -- adres
        v_o$invoice.COR_ADDRESS := t$address();
                    
            FOR rec_ IN get_rmb_data(a_ermb_id) 
            LOOP

                CASE rec_.contract_type_id 
                WHEN 'ENT' THEN
                    SELECT e.name, 
                           e.vat_reg_num,
                           e.address_invoice,
                           z.zip_code ||' '|| z.city_name
                      INTO v_o$invoice.cor_name, 
                           v_o$invoice.vat_reg_num,
                           v_o$invoice.cor_address.ad_line_1, 
                           v_o$invoice.cor_ad_line_4
                      FROM APP_PBE.ENTERPRISES e
                           LEFT OUTER JOIN EAGLE.ZIP_CODES z ON (z.active = '1' AND z.id=e.zip_code_invoice)
                     WHERE e.id = rec_.enterprise_id;
                WHEN 'IND' THEN
                    SELECT INITCAP(i.first_name || ' '|| i.last_name),
                           i.address_invoice,
                           z.zip_code ||' '|| z.city_name
                      INTO v_o$invoice.cor_name,
                           v_o$invoice.cor_address.ad_line_1,
                           v_o$invoice.cor_ad_line_4
                      FROM APP_PBE.INDIVIDUALS i
                           LEFT OUTER JOIN EAGLE.ZIP_CODES Z ON (z.active = '1' AND z.id=i.zip_code_invoice)
                      WHERE i.id = rec_.individual_id;
                ELSE
          PK_ERROR.RAISE_ERROR(-20004, 'GRF_UTL_CNTRTYP: Nieznany typ kontraktu = '|| nvl(rec_.contract_type_id, '<NULL>') );
                END CASE;

        v_o$invoice.pattern   := t$invoice.get_invoice_pattern(v_o$invoice.type);
        v_o$invoice.lines     := t$$invoice_lines();
        v_o$invoice.lines.ADD_(
            A_CODE         => v_il_code,
            A_QUANTITY     => 1,
                        A_AMOUNT       => rec_.overpaid_amount, -- kwota nadp³acona => do zwrotu
            A_SOURCE       => a_ermb_id,
                        A_TYPE         => 'CP', 
            A_SOURCE_TYPE  => v_source_type,
            A_CUS_ID       => NULL,
            A_ROUND_MODE   => 'NET');

                a_line_created := TRUE;
                EXIT;
            END LOOP;
            
            IF NOT a_line_created THEN
                pk_error.raise_error(-20006, 'GRF_UTL_NODEBLINE2: Brak linii dla noty uznaniowej - rozliczenie '|| ivar(a_ermb_id) ||' niekompletne?' );
            END IF;

      o_invoice_number := t$invoice.get_invoice_number(
          a_type    => v_o$invoice.type,
          a_o$order => v_o$fake_order,
          a_tsk_id  => NULL,
          a_rem_id  => NULL,
          a_prd_code => '30');
      v_o$invoice.invoice_number := o_invoice_number;

      v_o$invoice.lines.CALCULATE_AMOUNTS('NET'); -- poza kalkulacj¹ metoda nadaje numeracjê linii
            v_o$invoice.NET_AMOUNT   := v_o$invoice.LINES.GET_NET_AMOUNT();
            v_o$invoice.VAT_AMOUNT   := v_o$invoice.LINES.GET_VAT_AMOUNT();
            v_o$invoice.GROSS_AMOUNT := v_o$invoice.LINES.GET_GROSS_AMOUNT();
      v_o$invoice.CHECK_AMOUNTS();
      v_o$invoice.serialize();

      o_inv_id := v_o$invoice.id;
      v_o$invoice := null;
    end Create_Credit_Note_Worker;


    -- utwórz notê uznaniow¹ dla Uczestnika
    PROCEDURE Create_Pb_Rmb_Note( o_inv_id OUT NUMBER, o_invoice_number OUT VARCHAR2, o_invoice_type OUT VARCHAR2, o_invoice_date OUT DATE, a_ermb_id IN NUMBER, a_sign_user IN VARCHAR2 DEFAULT USER, a_type in VARCHAR2 default 'CUS' )
    IS
    BEGIN
        PK_AUDIT.AUDIT_MODULE('PK_GRF_UTILS', 'Create_Pb_Rmb_Note', ivar('A_E_RMB_ID',a_ermb_id)||';'||var('A_TYPE', a_type )||var('; USER',USER ) );
        
        Create_Credit_Note_Worker( o_inv_id, o_invoice_number, o_invoice_type, o_invoice_date, a_ermb_id, a_sign_user, a_type);
        
        -- nota mo¿e powstawaæ z dwóch (rozbie¿nych) powodów:
        -- * rozliczenie szkolenia po jego odbyciu i zosta³a nadwy¿ka
        -- * rozliczenie puli bonów (niewykorzystanych/rezygnacja/albo przeterminowanych) <- ten tryb wymaga wyjaœnienia z Justyn¹

        PK_AUDIT.AUDIT_MODULE('PK_GRF_UTILS', 'Create_Pb_Rmb_Note', 'OUT: '||
                       ivar('o_inv_id',  o_inv_id)||
                        var(', o_invoice_number', o_invoice_number)||
                        var(', o_invoice_type',   o_invoice_type)||
                        var(', o_invoice_date',   o_invoice_date)||
                       ivar(', A_E-RMB_ID', a_ermb_id)||
                        var(', USER', USER ));
    EXCEPTION
        WHEN others THEN
            pk_error.WRITE_ERROR;
            RAISE;
    END Create_Pb_Rmb_Note;    

       


END PK_GRF_UTILS;