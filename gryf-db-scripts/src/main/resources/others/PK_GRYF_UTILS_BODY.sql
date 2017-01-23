create or replace PACKAGE BODY       PK_GRF_UTILS AS


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
      return a_name ||'='||to_char(a_value,'YYYY-MM-DD');
    end var;
  function ivar( a_name varchar2, a_value number) return varchar2
  is
    begin
      return a_name ||'='||to_char(a_value,'fm99999999999990');
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



  -- utwórz notê obci¹¿eniowo-ksiêgow¹
  /*PROCEDURE Create_Debit_Note( o_inv_id OUT NUMBER, o_invoice_number OUT VARCHAR2, o_invoice_type OUT VARCHAR2, o_invoice_date OUT DATE, a_order_id IN NUMBER )
IS
BEGIN
  PK_AUDIT.AUDIT_MODULE('PK_GRF_UTILS', 'Create_Debit_Note', ivar('A_ORDER_ID',a_order_id)||var('; USER',USER ) );

  o_inv_id := TO_NUMBER(TO_CHAR(SYSDATE,'YYMMDDHH24MISS'));
  o_invoice_number := a_order_id||'/NOPB/'||to_char(SYSDATE,'MM/YY');
  o_invoice_type   := 'CNOPB';
  o_invoice_date   := sysdate;
      PK_AUDIT.AUDIT_MODULE('PK_GRF_UTILS', 'Create_Debit_Note', 'OUT: '||
                     ivar('o_inv_id', o_inv_id)||
                      var(', o_invoice_number', o_invoice_number)||
                      var(', o_invoice_type', o_invoice_type)||
                      var(', o_invoice_date',o_invoice_date)||
                     ivar(', A_ORDER_ID',a_order_id)||
                      var(', USER', USER ));
  EXCEPTION
      when others then
          pk_error.WRITE_ERROR;
          raise;
END Create_Debit_Note;*/

  PROCEDURE Create_Debit_Note_Worker( o_inv_id OUT NUMBER, o_invoice_number OUT VARCHAR2, o_invoice_type OUT VARCHAR2, o_invoice_date OUT DATE, a_order_id IN NUMBER )
  is
    v_o$invoice t$invoice;
    v_o$fake_order t$order;
    a_type       constant varchar2(200) := 'NPB';
    source_type_ constant APP_FIN.INVOICE_LINE_SOURCE_TYPES.TYPE%TYPE := 'ORDPB';
    il_code_     constant APP_FIN.INVOICE_LINE_CODES.CODE%TYPE        :=  'PB30';  -- sk¹d wiadomo co sprzedajemy..

    cursor get_order_data( p_id number )
    is
      select *
      from (
        select oe.order_id, o.external_order_id, o.status_id, CON.CONTRACT_TYPE_ID, con.enterprise_id, con.individual_id, o.vouchers_number vouchers_assigned,
          o.order_date, Con.SIGN_DATE,
          address_corr, zip_code_corr_id,
          Z.ZIP_CODE, Z.CITY_NAME,
          O.PBE_PRD_ID, pp.name pbe_name, pp.value pbe_value,
          oe.element_id, oe.value_number
        from app_pbe.order_elements oe
          join app_pbe.orders o on (o.id=OE.ORDER_ID)
          left outer join APP_PBE.PBE_PRODUCTS pp on (O.PBE_PRD_ID = PP.prd_id)
          left outer join eagle.zip_codes z on (z.active = '1' and Z.ID=o.zip_code_corr_id)
          join APP_PBE.CONTRACTS con on (o.contract_id = con.id)
        --and o.status_id = 'NEWKK'
        where 1=1
              --and O.ORDER_DATE > trunc(sysdate,'YEAR')
              and order_id = p_id
      )
        pivot ( min(value_number)
          for element_id
          in (
            --'ADRINV_KK' inv_address,
            --'PCODINV_KK' inv_postal_code,
            --'ADRCOR_KK' doc_address,
            --'PCODCOR_KK' doc_postal_code,
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
      PK_AUDIT.AUDIT_MODULE('PK_GRF_UTILS', 'Create_Debit_Note_Worker', ivar('A_ORDER_ID',a_order_id)||var('; USER',USER ) );
      -- seria
      -- numeracja dokumentów
      -- produkt (kod, code_type)
      -- select * from APP_STC.PRODUCTS p where p.type in ('29','30')

      v_o$invoice := t$invoice();

      -- w typach:
      -- * refaktor metody GEN_CUS_NOTE
      -- * refaktor metody self.invoice_number := t$invoice.GET_INVOICE_NUMBER
      -- * refaktor metody serialize (obs³uga nowego typu)


      /* nie ma proformy i nigdy jej nie by³o * /
       IF SELF.INVOICE_NUMBER IS NULL THEN

             SELF.INVOICE_NUMBER := t$invoice.get_invoice_number(
                                 a_type    => SELF.type,
                                     a_o$order => a_o$order,
                          a_tsk_id  => NULL,
                          a_rem_id  => NULL);

       END IF;
       */



      o_invoice_type   := a_type;
      o_invoice_date   := SYSDATE;

      v_o$invoice.type           := o_invoice_type;
      v_o$invoice.invoice_date   := o_invoice_date;
      v_o$invoice.hand_made      := 'N';
      v_o$invoice.CONFIRMED  := 'N';
      v_o$invoice.ELECTRONIC := 'N';
      v_o$invoice.payment_mean := 'PRZ'; -- przelew

      --o_invoice_number := a_order_id||'-'||to_char(SYSDATE,'SS')||'/'||a_type||'/'||to_char(SYSDATE,'MM/YY');
      o_invoice_number := t$invoice.get_invoice_number(
          a_type    => v_o$invoice.type,
          a_o$order => v_o$fake_order,
          a_tsk_id  => NULL,
          a_rem_id  => NULL,
          a_prd_code => '30');
      v_o$invoice.invoice_number := o_invoice_number;

      -- adres
      v_o$invoice.COR_ADDRESS := t$address;

      for rec_ in get_order_data(a_order_id)
      loop
        v_o$invoice.cor_address.ad_line_1 := rec_.address_corr;
        v_o$invoice.COR_AD_LINE_4         := rec_.zip_code|| ' '||rec_.city_name;
        v_o$invoice.payment_date := rec_.order_date +10;
        --                         albo app_pbe.contracts.sign_date + 10

        case rec_.contract_type_id
          when 'ENT' then
          select e.name, e.vat_reg_num
          into v_o$invoice.cor_name, v_o$invoice.vat_reg_num
          from APP_PBE.ENTERPRISES e
          where e.id = rec_.enterprise_id;
          when 'IND' then
          select initcap(i.first_name || ' '|| i.last_name)
          into v_o$invoice.cor_name
          from APP_PBE.INDIVIDUALS i
          where i.id = rec_.individual_id;
        else
          PK_ERROR.RAISE_ERROR(-20004, 'GRF_UTL_CNTRTYP: Nieznany typ kontraktu = '|| nvl(rec_.contract_type_id, '<NULL>') );
        end case;

        v_o$invoice.pattern  := t$invoice.get_invoice_pattern(v_o$invoice.type);
        v_o$invoice.lines     := t$$invoice_lines();
        v_o$invoice.lines.ADD_(
            A_CODE         => rec_.pbe_prd_id, --il_code_,
            A_QUANTITY     => rec_.quantity,
            A_AMOUNT       => rec_.order_amount,
            A_SOURCE       => a_order_id,
            A_TYPE         => 'ORDPB',
            A_SOURCE_TYPE  => source_type_,
            A_CUS_ID       => NULL,
            A_ROUND_MODE   => 'NET');
      end loop;
      v_o$invoice.lines.CALCULATE_AMOUNTS('NET');

      v_o$invoice.NET_AMOUNT   := v_o$invoice.LINES.GET_NET_AMOUNT;
      v_o$invoice.VAT_AMOUNT   := v_o$invoice.LINES.GET_VAT_AMOUNT;
      v_o$invoice.GROSS_AMOUNT := v_o$invoice.LINES.GET_GROSS_AMOUNT;
      v_o$invoice.CHECK_AMOUNTS();

      v_o$invoice.serialize();
      o_inv_id := v_o$invoice.id;
      v_o$invoice := null;
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
  PROCEDURE Create_Pb_Cus_Note( o_inv_id OUT NUMBER, o_invoice_number OUT VARCHAR2, o_invoice_type OUT VARCHAR2, o_invoice_date OUT DATE, o_87_invoice_number OUT VARCHAR2, a_order_id IN NUMBER, a_sign_user IN VARCHAR2 DEFAULT 'PONICHTERA BART£OMIEJ' )
  is
    begin
      --Create_Debit_Note( o_inv_id, o_invoice_number, o_invoice_type, o_invoice_date, a_order_id );
      --Create_Debit_Note_Worker( o_inv_id, o_invoice_number, o_invoice_type, o_invoice_date, a_order_id );
      o_inv_id := 814;
      o_invoice_number := a_order_id||'/NOPB/'||to_char(SYSDATE,'MM/YY');
      o_invoice_type   := 'CNOPB';
      o_invoice_date   := sysdate;
      o_87_invoice_number := a_order_id||'/NOPB/NEXT/'||to_char(SYSDATE,'MM/YY');
    end Create_Pb_Cus_Note;



  -- utwórz notê uznaniow¹ dla Uczestnika
  PROCEDURE Create_Pb_Rmb_Note( o_inv_id OUT NUMBER, o_invoice_number OUT VARCHAR2, o_invoice_type OUT VARCHAR2, o_invoice_date OUT DATE, a_ermb_id IN NUMBER, a_sign_user IN VARCHAR2 DEFAULT 'PONICHTERA BART£OMIEJ', a_type in VARCHAR2 default 'CUS' )
  IS
    BEGIN
      o_inv_id := 814;
      o_invoice_number := a_ermb_id||'/NOPB/'||to_char(SYSDATE,'MM/YY');
      o_invoice_type   := 'CNOPB';
      o_invoice_date   := sysdate;
    END Create_Pb_Rmb_Note;

  -- procedura robocza, do testów wew.
  PROCEDURE Create_Credit_Note_Worker (
    o_inv_id         OUT NUMBER,
    o_invoice_number OUT VARCHAR2,
    o_invoice_type   OUT VARCHAR2,
    o_invoice_date   OUT DATE,
    a_ermb_id        IN NUMBER,
    a_type           in varchar2 ) -- default 'CUS'
  is
    v_o$invoice    t$invoice;
    v_itype       constant varchar2(200)                               := 'NUPB';
    v_source_type constant APP_FIN.INVOICE_LINE_SOURCE_TYPES.TYPE%TYPE := 'REMPB';
    v_il_code     constant APP_FIN.INVOICE_LINE_CODES.CODE%TYPE        := '30RMB1';
    v_o$fake_order   t$order;


    cursor get_rmb_data (rmb_id number)
    is
      select er.id, count(ipu.id) over (partition by er.id) duplikaty, er.status_id rmb_status_id, er.arrival_date,
                    SXO_IND_AMOUNT_DUE_TOTAL overpaid_amount,
                    PIP.ORDER_ID pip_order_id, O.ADDRESS_CORR, z. zip_code, Z.CITY_NAME,
        O.CONTRACT_ID, C.CONTRACT_TYPE_ID, C.ENTERPRISE_ID, C.INDIVIDUAL_ID,
                    pip.prd_id pip_prd_id,
        ti.training_id,
        ti.grant_program_id, (select program_name from app_pbe.grant_programs gp where gp.id=ti.grant_program_id) program_name, ti.status_id ti_status_id
      from APP_PBE.E_REIMBURSEMENTS er
        left outer join APP_PBE.TI_TRAINING_INSTANCES ti on ER.TI_TR_INST_ID=ti.id
        --left outer join APP_PBE.PBE_PRODUCT_INSTANCE_POOLS pip on er.product_instance_pool_id=PIP.ID --<- er.product_instance_pool_id to pula niewykorzystana
        left join APP_PBE.PBE_PRODUCT_INSTANCE_POOL_USES ipu on IPU.TRAINING_INSTANCE_ID = ti.id -- <- mog¹ byæ dwie
        left join APP_PBE.PBE_PRODUCT_INSTANCE_POOLS pip on IPU.product_instance_pool_id=PIP.ID
        left join app_pbe.orders o on pip.order_id = o.id
        left join APP_PBE.CONTRACTS c on O.CONTRACT_ID = C.ID
        left outer join eagle.zip_codes z on (z.active = '1' and Z.ID=o.zip_code_corr_id)
      where er.id = rmb_id
      order by er.id, pip.id;
    begin
      PK_AUDIT.AUDIT_MODULE('PK_GRF_UTILS', 'Create_Credit_Note_Worker', ivar('A_E_RMB_ID',a_ermb_id)||';'||var('A_TYPE', a_type )||var('; USER',USER ) );
      v_o$invoice := t$invoice();
      o_invoice_type   := v_itype;
      o_invoice_date   := sysdate;

      v_o$invoice.type           := o_invoice_type;
      v_o$invoice.invoice_date   := o_invoice_date;
      v_o$invoice.hand_made      := 'N';
      v_o$invoice.CONFIRMED  := 'N';
      v_o$invoice.ELECTRONIC := 'N';

      -- adres
      v_o$invoice.COR_ADDRESS := t$address;

      for rec_ in get_rmb_data(a_ermb_id)
      loop
        v_o$invoice.cor_address.ad_line_1 := rec_.address_corr;
        v_o$invoice.COR_AD_LINE_4         := rec_.zip_code|| ' '||rec_.city_name;

        case rec_.contract_type_id
          when 'ENT' then
          select e.name, e.vat_reg_num
          into v_o$invoice.cor_name,
            v_o$invoice.vat_reg_num
          from APP_PBE.ENTERPRISES e
          where e.id = rec_.enterprise_id;
          when 'IND' then
          select initcap(i.first_name || ' '|| i.last_name)
          into v_o$invoice.cor_name
          from APP_PBE.INDIVIDUALS i
          where i.id = rec_.individual_id;
        else
          PK_ERROR.RAISE_ERROR(-20004, 'GRF_UTL_CNTRTYP: Nieznany typ kontraktu = '|| nvl(rec_.contract_type_id, '<NULL>') );
        end case;

        v_o$invoice.pattern   := t$invoice.get_invoice_pattern(v_o$invoice.type);
        v_o$invoice.lines     := t$$invoice_lines();
        v_o$invoice.lines.ADD_(
            A_CODE         => v_il_code,
            A_QUANTITY     => 1,
            A_AMOUNT       => rec_.overpaid_amount,
            A_SOURCE       => a_ermb_id,
            A_TYPE         => 'REMPB',
            A_SOURCE_TYPE  => v_source_type,
            A_CUS_ID       => NULL,
            A_ROUND_MODE   => 'NET');

        exit;
      end loop;

      /*o_invoice_number:= a_ermb_id||'-'||to_char(SYSDATE,'SS')||'/'||v_itype||'/'||to_char(SYSDATE,'MM/YY');*/
      o_invoice_number := t$invoice.get_invoice_number(
          a_type    => v_o$invoice.type,
          a_o$order => v_o$fake_order,
          a_tsk_id  => NULL,
          a_rem_id  => NULL,
          a_prd_code => '30');
      v_o$invoice.invoice_number := o_invoice_number;

      v_o$invoice.lines.CALCULATE_AMOUNTS('NET'); -- poza kalkulacj¹ metoda nadaje numeracjê linii
      v_o$invoice.NET_AMOUNT   := v_o$invoice.LINES.GET_NET_AMOUNT;
      v_o$invoice.VAT_AMOUNT   := v_o$invoice.LINES.GET_VAT_AMOUNT;
      v_o$invoice.GROSS_AMOUNT := v_o$invoice.LINES.GET_GROSS_AMOUNT;
      v_o$invoice.CHECK_AMOUNTS();
      v_o$invoice.serialize();

      o_inv_id := v_o$invoice.id;
      v_o$invoice := null;
    end Create_Credit_Note_Worker;



END PK_GRF_UTILS;