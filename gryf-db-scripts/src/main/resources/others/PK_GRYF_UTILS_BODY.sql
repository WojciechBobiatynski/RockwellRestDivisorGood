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


  -- utwórz notê obci¹¿eniowo-ksiêgow¹
  PROCEDURE Create_Debit_Note( o_inv_id OUT NUMBER, o_invoice_number OUT VARCHAR2, o_invoice_type OUT VARCHAR2, o_invoice_date OUT DATE, a_order_id IN NUMBER )
  IS
    BEGIN
      PK_AUDIT.AUDIT_MODULE('PK_GRF_UTILS', 'Create_Debit_Note', ivar('A_ORDER_ID',a_order_id)||var('; USER',USER ) );

      o_inv_id := TO_NUMBER(TO_CHAR(sysdate,'YYMMDDHH24MISS'));
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
    END Create_Debit_Note;

  PROCEDURE Create_Debit_Note_Worker( o_inv_id OUT NUMBER, o_invoice_number OUT VARCHAR2, o_invoice_type OUT VARCHAR2, o_invoice_date OUT DATE, a_order_id IN NUMBER )
  is
    ao$invoice t$invoice;
    a_type  varchar2(200) := 'NPB';
    begin
      PK_AUDIT.AUDIT_MODULE('PK_GRF_UTILS', 'Create_Debit_Note_Worker', ivar('A_ORDER_ID',a_order_id)||var('; USER',USER ) );
      -- seria
      -- numeracja dokumentów
      -- produkt (kod, code_type)
      -- select * from APP_STC.PRODUCTS p where p.type in ('29','30')

      ao$invoice := t$invoice();
      --ao$invoice.

      -- w typach:
      -- * refaktor metody GEN_CUS_NOTE
      -- * refaktor metody self.invoice_number := t$invoice.GET_INVOICE_NUMBER
      -- * refaktot metody serialize (obs³uga nowego typu)

      o_invoice_number := a_order_id||'/'||a_type||'/'||to_char(SYSDATE,'MM/YY');
      o_invoice_type   := a_type;
      o_invoice_date   := sysdate;

      ao$invoice.invoice_number := o_invoice_number;
      ao$invoice.type   := o_invoice_type;
      ao$invoice.invoice_date := o_invoice_date;

      ao$invoice.serialize();
      o_inv_id := ao$invoice.id;
      ao$invoice := null;
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
  PROCEDURE Create_Pb_Cus_Note( o_inv_id OUT NUMBER, o_invoice_number OUT VARCHAR2, o_invoice_type OUT VARCHAR2, o_invoice_date OUT DATE, a_order_id IN NUMBER )
  is
    begin
      Create_Debit_Note( o_inv_id, o_invoice_number, o_invoice_type, o_invoice_date, a_order_id );
    end Create_Pb_Cus_Note;


END PK_GRF_UTILS;