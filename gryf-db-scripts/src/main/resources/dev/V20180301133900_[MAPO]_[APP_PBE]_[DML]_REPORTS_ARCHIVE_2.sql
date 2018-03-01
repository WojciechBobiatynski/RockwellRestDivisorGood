--APP_PBE.INDIVIDUALS;
--APP_PBE.ENTERPRISES

drop table APP_PBE.REP_WUP_ORDERS_ARCHIVES;

CREATE TABLE APP_PBE.REP_WUP_ORDERS_ARCHIVES
(
  ID                             NUMBER not null,
  ROW_NO                         NUMBER not null,
--  REP_TYPE                       VARCHAR2(60) NOT NULL,
  ON_DAY                         DATE not null,
  CREATED_USER                   VARCHAR2(30 BYTE) not null,
  CREATED                        TIMESTAMP(6) WITH TIME ZONE,
  MODIFIED_USER                  VARCHAR2(30 BYTE) not null,
  MODIFIED                       TIMESTAMP(6) WITH TIME ZONE,
    --DATE_FROM   DATE,
    --DATE_TO     DATE,
    GRANT_PROGRAM_ID               NUMBER         NOT NULL,
    LP          NUMBER    not null, -- A; LP
    IND_ID      NUMBER , -- B; Id uczestnika
    IND_NAME    VARCHAR2(500 Byte),   -- C; Imie i Nazwisko
PESEL           VARCHAR2 (11 Byte), -- D; PESEL
ID_MSP          NUMBER    null, -- E; Id MSP    
ENT_NAME      VARCHAR2 (500 Byte), -- F; Pe³na nazwa MSP    
NIP           VARCHAR2 (20 Byte) , -- G; NIP    
EXTERNAL_ORDER_ID        VARCHAR2 (20 Byte), -- H; Numer Umowy    
EXTERNAL_ORDER_SEQ_NO    VARCHAR2 (20 Byte), -- I;Która umowa    
SIGN_DATE        VARCHAR2 (10 Byte), -- J;Data zawarcia umowy    
FULLY_PAID       VARCHAR2 (1 Byte),  -- K;Wp³. wk³ad w³asny T/N    
OWN_SHARE        NUMBER, -- L;Wp³acony wk³ad w³asny    
PAYMENT_DATE     DATE,   -- M;Data zaksiêgowania wp³aty wk³adu w³asnego    
VALID            VARCHAR2 (1 Byte), -- N;Umowa Wa¿na T/N    
EXPIRY_DATE      DATE, -- O;Data wygaœniêcia umowy/zamówienia    
DATA_WAZNOSCI       VARCHAR2 (10 Byte), -- P;Data wa¿noœci bonów    
WYDANO              NUMBER,    -- R;Iloœæ wydanych bonów    
DATA_WYDANIA        VARCHAR2 (10 Byte), -- S;Data wydania bonów    
CREATED_TIMESTAMP    DATE, -- T;Data utworzenia w Gryf
-- niewyswietlane:
ORDER_ID         NUMBER,    
VOUCHERS_ORDERED    NUMBER,    
REQ_OWN_SHARE    NUMBER,  
VALID_REAL       VARCHAR2 (1 Byte),   
EXPIRY_DATE_REAL    DATE, 
CREATED_TIMESTAMP2   TIMESTAMP(6),
STATUS_ID    VARCHAR2 (10 Byte)
);

comment on table APP_PBE.REP_WUP_ORDERS_ARCHIVES is 'GRYF; Archiwum raportów 1.114 WUP Umowy ';


comment on column APP_PBE.REP_WUP_ORDERS_ARCHIVES.DATE_FROM   is 'Parametr; Umowy zawarte od';
comment on column APP_PBE.REP_WUP_ORDERS_ARCHIVES.DATE_TO           is 'Parametr; Umowy zawarte do';     
comment on column APP_PBE.REP_WUP_ORDERS_ARCHIVES.GRANT_PROGRAM_ID  is 'Parametr; Id programu dofinansowania; WUP=100';              
comment on column APP_PBE.REP_WUP_ORDERS_ARCHIVES.LP                is 'A; LP';
comment on column APP_PBE.REP_WUP_ORDERS_ARCHIVES.IND_ID      is 'B; Id uczestnika';
comment on column APP_PBE.REP_WUP_ORDERS_ARCHIVES.IND_NAME    is 'C; Imie i Nazwisko';
comment on column APP_PBE.REP_WUP_ORDERS_ARCHIVES.PESEL       is 'D; PESEL';
comment on column APP_PBE.REP_WUP_ORDERS_ARCHIVES.ID_MSP      is 'E; Id MSP    ';
comment on column APP_PBE.REP_WUP_ORDERS_ARCHIVES.ENT_NAME    is 'F; Pe³na nazwa MSP    ';
comment on column APP_PBE.REP_WUP_ORDERS_ARCHIVES.NIP                    is 'G; NIP    ';
comment on column APP_PBE.REP_WUP_ORDERS_ARCHIVES.EXTERNAL_ORDER_ID      is 'H; Numer Umowy    ';
comment on column APP_PBE.REP_WUP_ORDERS_ARCHIVES.EXTERNAL_ORDER_SEQ_NO  is 'I;Która umowa    ';
comment on column APP_PBE.REP_WUP_ORDERS_ARCHIVES.SIGN_DATE              is 'J;Data zawarcia umowy    ';
comment on column APP_PBE.REP_WUP_ORDERS_ARCHIVES.FULLY_PAID        is 'K;Wp³. wk³ad w³asny T/N    ';
comment on column APP_PBE.REP_WUP_ORDERS_ARCHIVES.OWN_SHARE         is 'L;Wp³acony wk³ad w³asny    ';
comment on column APP_PBE.REP_WUP_ORDERS_ARCHIVES.PAYMENT_DATE      is 'M;Data zaksiêgowania wp³aty wk³adu w³asnego    ';
comment on column APP_PBE.REP_WUP_ORDERS_ARCHIVES.VALID             is 'N;Umowa Wa¿na T/N    ';
comment on column APP_PBE.REP_WUP_ORDERS_ARCHIVES.EXPIRY_DATE       is 'O;Data wygaœniêcia umowy/zamówienia    ';
comment on column APP_PBE.REP_WUP_ORDERS_ARCHIVES.DATA_WAZNOSCI     is 'P;Data wa¿noœci bonów    ';
comment on column APP_PBE.REP_WUP_ORDERS_ARCHIVES.WYDANO            is 'R;Iloœæ wydanych bonów    ';
comment on column APP_PBE.REP_WUP_ORDERS_ARCHIVES.DATA_WYDANIA      is 'S;Data wydania bonów    ';
comment on column APP_PBE.REP_WUP_ORDERS_ARCHIVES.CREATED_TIMESTAMP is 'T;Data utworzenia w Gryf';






ALTER TABLE APP_PBE.REP_WUP_ORDERS_ARCHIVES ADD (
  CONSTRAINT REP_WUP_ORDERS_ARCHIVES_PK
  PRIMARY KEY
  (ID, ROW_NO)
  ENABLE VALIDATE);


ALTER TABLE APP_PBE.REP_WUP_ORDERS_ARCHIVES ADD 
CONSTRAINT REP_WUP_ORDERS_ARCHIVES_ID_FK
 FOREIGN KEY (ID)
 REFERENCES APP_PBE.REPORTS_WUP (ID)
 ENABLE
 VALIDATE
;

create or replace view app_pbe.rep_wup_orders
as 
select       
    GRANT_PROGRAM_ID,  row_number() OVER (ORDER BY sign_date) as LP, 
    IND_ID, IND_NAME, PESEL, 
    Id_MSP, ENT_NAME, NIP, EXTERNAL_ORDER_ID,  EXTERNAL_ORDER_SEQ_NO,
    SIGN_DATE, 
    FULLY_PAID, 
    /*case when valid ='T' and nvl(expiry_Date, sysdate) >= trunc(SYSDATE) 
                                       then nvl(OWN_SHARE , 0)
                                       else to_number(null) 
    end       as       OWN_SHARE,*/
    nullif(own_share,0) as own_share, 
    case when own_share > 0 /*and ( nvl(expiry_Date, SYSDATE+1) < trunc(SYSDATE) )*/ 
             then PAYMENT_DATE else null end                                        as payment_date,
    case when valid ='T' and expiry_Date < trunc(sysdate) then 'N' else valid end   as VALID,
    CASE WHEN expiry_date >= SYSDATE then null else expiry_date               end   as EXPIRY_DATE,
    --expiry_date2,
    DATA_WAZNOSCI,
    WYDANO, 
    DATA_WYDANIA,
    created_timestamp,
-- niewyswietlane:
    id as ORDER_ID,
    VOUCHERS_ORDERED,    
    REQ_OWN_SHARE, 
    valid           as valid_real, 
    expiry_date     as expiry_date_real, 
    CREATED_TIMESTAMP2,  
    STATUS_ID
from (
select c.grant_program_id, 
        ind.id IND_ID, 
        IND.FIRST_NAME||' '|| IND.LAST_NAME as ind_name, 
        IND.PESEL,
        ENT.ID as Id_MSP, 
        Ent.NAME as ENT_NAME, 
        ENT.VAT_REG_NUM as NIP,
        O.EXTERNAL_ORDER_ID as EXTERNAL_ORDER_ID, 
       substr(O.EXTERNAL_ORDER_ID, instr(O.EXTERNAL_ORDER_ID,'/',-1)+1 ) as EXTERNAL_ORDER_SEQ_NO,
        o.id, 
        to_char(O.ORDER_DATE /*C.SIGN_DATE*/, 'YYYY-MM-DD') AS SIGN_DATE, -- 2017-07-31 zamiast daty podpisania umowy (kontraktu), data zamówienia (ma znaczenie przy drugiej transzy)
        OE.VALUE_NUMBER       as OWN_SHARE,
        oe_req.VALUE_NUMBER   as REQ_OWN_SHARE,
        case when OE.VALUE_NUMBER = oe_req.VALUE_NUMBER /*and O.EXTERNAL_ORDER_ID != 'WKK/516/1'*/ then 'T' else 'N' end   as FULLY_PAID,   
        (select OE2.VALUE_DATE from APP_PBE.ORDER_ELEMENTS oe2 where (O.id=OE2.ORDER_ID and oe2.element_id = 'COND_KK') ) as PAYMENT_DATE, -- data p³atnoœci
        NVL((select 'T' from dual where SYSDATE between O.ORDER_DATE and least( nvl(OE_CANDATE.VALUE_DATE,SYSDATE+1) , C.EXPIRY_DATE )  ) , 'N') as VALID, 
        --case when O.EXTERNAL_ORDER_ID = 'WKK/516/1' then to_date('2017-07-11','YYYY-MM-DD')
        --else
        NVL( OE_CANDATE.VALUE_DATE, 
            (select /*oexp.required_date*/ 
                   --OEXP.VALUE_DATE  
                   trunc(OEXP.COMPLETED_DATE)
                    from APP_PBE.ORDER_ELEMENTS oexp 
                   where oexp.element_id ='COND_KK'  and OEXP.ORDER_ID = o.id -- data podpiêcia p³atnoœci
                     and exists (select 1 from APP_PBE.ORDER_ELEMENTS oexp2 
                                         where oexp2.element_id ='CONAMA_KK' -- kwota podpiêcia ma byæ pusta
                                           and oexp2.order_id=oexp.order_id  and ( nvl(oexp2.value_number,0) = 0  or oexp.value_date > /*(oexp.completed_date+1/24/60)*/ oexp.required_date ) ) 
            ) 
        --end
        )       AS  expiry_date , --data wygasania umowy 
        (select /*oexp.required_date*/ OEXP.VALUE_DATE  
                    from APP_PBE.ORDER_ELEMENTS oexp 
                   where oexp.element_id ='COND_KK'  and OEXP.ORDER_ID = o.id -- data podpiêcia p³atnoœci
                     and exists (select 1 from APP_PBE.ORDER_ELEMENTS oexp2 
                                         where oexp2.element_id ='CONAMA_KK' -- kwota podpiêcia ma byæ pusta
                                           and oexp2.order_id=oexp.order_id  and ( nvl(oexp2.value_number,0) != 0 ) ) ) expiry_date2,
        O.VOUCHERS_NUMBER as VOUCHERS_ORDERED,
        ppip.suma as WYDANO,
        ppip.data_wydania,
        ppip.data_waznosci,
        to_date(trunc(o.created_timestamp)) as created_timestamp,
        o.created_timestamp as created_timestamp2,
        o.status_id
 from APP_PBE.CONTRACTS c 
      join APP_PBE.ORDERS o on (O.CONTRACT_ID=c.id and o.status_id != 'VOIDKK' and o.external_order_id != 'WKK/187/1' ) -- zahardkodowana jedna umowa, która nigdy do Gryfa wpaœæ nie powinna
      LEFT OUTER JOIN APP_PBE.ENTERPRISES ent on (C.ENTERPRISE_ID=ent.ID AND C.CONTRACT_TYPE_ID = 'ENT')
      LEFT OUTER JOIN APP_PBE.INDIVIDUALS ind on (C.INDIVIDUAL_ID=ind.ID AND C.CONTRACT_TYPE_ID = 'IND')
      left outer join APP_PBE.ORDER_ELEMENTS oe on (O.id=OE.ORDER_ID and oe.element_id = 'CONAMA_KK') -- 'PAYD_KK'   -- kwota podpiêcia, mo¿e byæ inna ni¿ kwota wp³aty (np. gdy wp³aci za du¿o)
      left outer join APP_PBE.ORDER_ELEMENTS oe_req on (O.id=OE_req.ORDER_ID and oe_req.element_id = 'OWNCONA_KK') -- 'PAYD_KK'  -- OWNCONA_KK Element przetrzynuj¹cy kwotê wk³adu w³asnego, jaki u¿ytkownik musi wp³aciæ aby realizowac bony.
      left outer join app_pbe.order_elements oe_candate on (o.id=OE_CANDATE.ORDER_ID and OE_CANDATE.ELEMENT_ID = 'CANDATE_KK') -- data anulowania 
      left outer join (select sum(all_num) suma, TO_CHAR(min(PIP.START_DATE),'YYYY-MM-DD') data_wydania, pip.order_id, to_char(max(PIP.EXPIRY_DATE),'YYYY-MM-DD') data_waznosci
                         from APP_PBE.PBE_PRODUCT_INSTANCE_POOLS pip 
                        group by pip.order_id ) ppip on ppip.order_id = o.id 
      --where c.grant_program_id = {?grant_program}
      --and o.created_timestamp between  {?signed_from} and ({?signed_to}+(24*60*60-1)/(24*60*60) )
     )
order by created_timestamp
with read only;


comment on table APP_PBE.rep_wup_orders is 'GRYF; Raport 1.114 WUP Umowy - zapytanie online';


comment on column APP_PBE.rep_wup_orders.GRANT_PROGRAM_ID  is 'Parametr; Id programu dofinansowania; WUP=100';              
comment on column APP_PBE.rep_wup_orders.LP                is 'A; LP';
comment on column APP_PBE.rep_wup_orders.IND_ID      is 'B; Id uczestnika';
comment on column APP_PBE.rep_wup_orders.IND_NAME    is 'C; Imie i Nazwisko';
comment on column APP_PBE.rep_wup_orders.PESEL       is 'D; PESEL';
comment on column APP_PBE.rep_wup_orders.ID_MSP      is 'E; Id MSP    ';
comment on column APP_PBE.rep_wup_orders.ENT_NAME    is 'F; Pe³na nazwa MSP    ';
comment on column APP_PBE.rep_wup_orders.NIP                    is 'G; NIP    ';
comment on column APP_PBE.rep_wup_orders.EXTERNAL_ORDER_ID      is 'H; Numer Umowy    ';
comment on column APP_PBE.rep_wup_orders.EXTERNAL_ORDER_SEQ_NO  is 'I;Która umowa    ';
comment on column APP_PBE.rep_wup_orders.SIGN_DATE              is 'J;Data zawarcia umowy    ';
comment on column APP_PBE.rep_wup_orders.FULLY_PAID        is 'K;Wp³. wk³ad w³asny T/N    ';
comment on column APP_PBE.rep_wup_orders.OWN_SHARE         is 'L;Wp³acony wk³ad w³asny    ';
comment on column APP_PBE.rep_wup_orders.PAYMENT_DATE      is 'M;Data zaksiêgowania wp³aty wk³adu w³asnego    ';
comment on column APP_PBE.rep_wup_orders.VALID             is 'N;Umowa Wa¿na T/N    ';
comment on column APP_PBE.rep_wup_orders.EXPIRY_DATE       is 'O;Data wygaœniêcia umowy/zamówienia    ';
comment on column APP_PBE.rep_wup_orders.DATA_WAZNOSCI     is 'P;Data wa¿noœci bonów    ';
comment on column APP_PBE.rep_wup_orders.WYDANO            is 'R;Iloœæ wydanych bonów    ';
comment on column APP_PBE.rep_wup_orders.DATA_WYDANIA      is 'S;Data wydania bonów    ';
comment on column APP_PBE.rep_wup_orders.CREATED_TIMESTAMP is 'T;Data utworzenia w Gryf';




declare
    v_table_name_ varchar2(100);
begin
    -- zapytanie online
    v_table_name_ :=  'REP_WUP_ORDERS';
    dbms_output.put_line('Grant SELECT on '|| v_table_name_ ||':'|| SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'REPORTS_USER', v_table_name_, 'SELECT' ));
    dbms_output.put_line('Grant SELECT on '|| v_table_name_ ||':'|| SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'REP_USR', v_table_name_, 'SELECT' ));
    -- dla procedur EAGLE:
    dbms_output.put_line('Grant SELECT on '|| v_table_name_ ||':'|| SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', v_table_name_, 'SELECT' ));
    
    
    -- zapytanie o archiwaln¹ instancjê raportu
    v_table_name_ :=  'REP_WUP_ORDERS_ARCHIVES';
    dbms_output.put_line('Grant SELECT on '|| v_table_name_ ||':'|| SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'REPORTS_USER', v_table_name_, 'SELECT' ));
    dbms_output.put_line('Grant SELECT on '|| v_table_name_ ||':'|| SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'REP_USR', v_table_name_, 'SELECT' ));
    -- dla procedur EAGLE:
    dbms_output.put_line('Grant INSERT on '|| v_table_name_ ||':'|| SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', v_table_name_, 'INSERT' ));
end;


select * from APP_PBE.REP_WUP_ORDERS          where lp = 1;
select * from app_pbe.rep_wup_orders_archives;

--alter table app_pbe.rep_wup_orders_archives drop column rep_type;


create or replace procedure eagle.run_pbe_reports_wup( a_grant_program_id in number, a_report_type in varchar2, a_rep_view in varchar2, a_rep_arch_table in varchar2 )
is
    -- Uruchamia generacjê dowolnego raportu WUP
    v_id number;
    v_rep_type varchar2(100) :=  a_report_type; --'1.115_WUP_ROZLICZENIA';
    
    v_stmt varchar2(8000);
    v_cols varchar2(4000);

    PROCEDURE dbg(msg_ in varchar2)
    is
    begin
        dbms_output.put_line( substr(msg_,1,200) );
        if length(msg_) > 200 then
            dbms_output.put_line( substr(msg_,201,200) );
        end if;
        if length(msg_) > 400 then
            dbms_output.put_line( substr(msg_,401,200) );
        end if;
        if length(msg_) > 600 then
            dbms_output.put_line( substr(msg_,601,200) );
        end if;
        if length(msg_) > 800 then
            dbms_output.put_line( substr(msg_,801,200) );
        end if;
    end;

    procedure aud_( a_proc in varchar2, a_params in varchar2, a_params2 in varchar2 default null,a_params3 in varchar2 default null, a_params4 in varchar2 default null )  
    is
    begin
        dbg( 'PROC: '||a_proc || '('||a_params||','||a_params2||','||a_params3||','||a_params4||')');
        PK_AUDIT.AUDIT_MODULE('STORED PROC', a_proc, a_params);    
    end;
    
    
    FUNCTION var(p_name in varchar2, p_value IN BOOLEAN) RETURN VARCHAR2 
    IS
    BEGIN
      RETURN p_name||'='||
       CASE p_value
         WHEN TRUE THEN 'TRUE'
         WHEN FALSE THEN 'FALSE'
         ELSE 'NULL'
       END;
    END;    

    FUNCTION ivar(p_name in varchar2, p_value IN number) RETURN VARCHAR2 
    IS
    BEGIN
      RETURN p_name||'='||nvl(to_char(trunc(p_value),'fm999999999990'),'<null>');
    END;

    FUNCTION var(p_name in varchar2, p_value IN varchar2) RETURN VARCHAR2 
    IS
    BEGIN
      RETURN p_name||'='||nvl(p_value,'<null>');
    END;

begin
    aud_('run_pbe_reports_wup', ivar( 'a_grant_program_id', a_grant_program_id ), var('a_report_type', a_report_type), var('a_rep_view', a_rep_view), var('a_rep_arch_table',a_rep_arch_table ) );

    select EAGLE.PK_SEQ.nextval
          into v_id 
         from dual;
         
select listagg(column_name, ',') within group (order by atc.column_id)
into v_cols
from ALL_TAB_COLS atc where ATC.TABLE_NAME =  a_rep_arch_table and owner = 'APP_PBE';         
         
    insert into app_pbe.reports_wup(id, rep_type, on_day, created_user, created )  
    values (v_id,  v_rep_type, trunc(sysdate) , user   ,      systimestamp );

    v_stmt := 'insert into app_pbe.'||a_rep_arch_table||' ';
    v_stmt := v_stmt || '('|| v_cols ||')';
    v_stmt := v_stmt||q'[
    select :v_id id, 
            rownum, 
            trunc(sysdate,'MONTH') on_day,  
            user   created_user,
            systimestamp created,
            user   modified_user,
            systimestamp modified,
            online_view.*
       from app_pbe.]'||a_rep_view||q'[ online_view 
      where grant_program_id = :p_grant_program_id]';

    dbg(var('stmt',v_stmt));
    execute immediate v_stmt 
       using in v_id,
             --in v_rep_type,
             in a_grant_program_id;
             
    aud_('run_pbe_reports_wup', ivar('Rekordów wstawionych:',SQL%ROWCOUNT) );                                 
end run_pbe_reports_wup;


declare
begin
    eagle.run_pbe_reports_wup( 100, '1.114_WUP_UMOWY', 'REP_WUP_ORDERS', 'REP_WUP_ORDERS_ARCHIVES' );
    --commit;                                             
end;

select count(*) from app_pbe.rep_wup_orders;
select * from app_pbe.rep_wup_orders_archives;
select * from APP_PBE.REPORTS_WUP 