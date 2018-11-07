/*GENERACJA WPISÓW W TABELCE */
-- Uwaga!!!!!!!!!!!! CZY WYLACZYC "WYBRANE" TRIGGERY NA CZAS DZIALANIA SKRYPTU?????????????????????
--
--4.	Przeniesienie CODE i ACCOUNT_PAYMENT
--a.	Należy wykonać backup – prośba  o zmyślny schemat ….
--i.	APP_PBE.INDIVIDUALS
--ii.	APP_PBE.ENTERPRISES
CREATE TABLE ${gryf.schema}.INDIVIDUALS_ARCH AS   SELECT * FROM ${gryf.schema}.INDIVIDUALS;

CREATE TABLE ${gryf.schema}.INDIVIDUALS_AUDIT_ARCH AS   SELECT * FROM ${gryf.schema}.INDIVIDUALS_AUDIT;

COMMENT ON TABLE ${gryf.schema}.INDIVIDUALS_ARCH IS 'Tabela archiwalna INDIVIDUALS';
COMMENT ON TABLE ${gryf.schema}.INDIVIDUALS_AUDIT_ARCH IS 'Tabela archiwalna INDIVIDUALS_AUDIT';

CREATE TABLE ${gryf.schema}.ENTERPRISES_ARCH AS SELECT * FROM ${gryf.schema}.ENTERPRISES;

CREATE TABLE ${gryf.schema}.ENTERPRISES_AUDIT_ARCH AS SELECT * FROM ${gryf.schema}.ENTERPRISES_AUDIT;

COMMENT ON TABLE ${gryf.schema}.ENTERPRISES_ARCH IS 'Tabela archiwalna ENTERPRISES';
COMMENT ON TABLE ${gryf.schema}.ENTERPRISES_AUDIT_ARCH IS 'Tabela archiwalna ENTERPRISES_AUDIT';

--b.	Dodajemy kolumny CODE i ACCOUNT_PAYMENT do APP_PBE.CONTRACTS
--c.	Dla indywidualsów
--i.	Przenosimy  CODE I ACCOUNT z APP_PBE.INDIVIDUALS do kontraktów
ALTER TABLE ${gryf.schema}.CONTRACTS add (CODE VARCHAR2(8) default '-1' NOT NULL , ACCOUNT_PAYMENT VARCHAR2(26) default '-1' NOT NULL);

ALTER TABLE ${gryf.schema}.CONTRACTS_AUDIT add (CODE VARCHAR2(8) default '-1' NOT NULL , ACCOUNT_PAYMENT VARCHAR2(26) default '-1' NOT NULL);

COMMENT ON COLUMN ${gryf.schema}.CONTRACTS.CODE IS 'Kod uczestnika/MSP';
COMMENT ON COLUMN ${gryf.schema}.CONTRACTS.ACCOUNT_PAYMENT IS 'Subkonto uczestnika/MSP';

-- IND - dane do Umowy z Individuals
--- Uwaga: Kolejnosś jest wazna. 1st Ind 2nd Ent
MERGE INTO ${gryf.schema}.CONTRACTS  contr USING (
    SELECT
         code,
         ACCOUNT_PAYMENT,
         id
     FROM
         ${gryf.schema}.INDIVIDUALS
    ) inds ON ( contr.INDIVIDUAL_ID = inds.id AND contr.ENTERPRISE_ID IS NULL )
    WHEN MATCHED THEN
     UPDATE SET contr.code = inds.code,
                contr.ACCOUNT_PAYMENT = inds.ACCOUNT_PAYMENT;

MERGE INTO ${gryf.schema}.CONTRACTS_AUDIT  contr USING (
SELECT
     code,
     ACCOUNT_PAYMENT,
     id
 FROM
     ${gryf.schema}.INDIVIDUALS
) inds ON ( contr.INDIVIDUAL_ID = inds.id AND contr.ENTERPRISE_ID IS NULL)
WHEN MATCHED THEN
 UPDATE SET contr.code = inds.code,
            contr.ACCOUNT_PAYMENT = inds.ACCOUNT_PAYMENT;

-- Msp - dane do Umowy z Ent
MERGE INTO ${gryf.schema}.CONTRACTS  contr USING (
SELECT
     code,
     ACCOUNT_PAYMENT,
     id
 FROM
     ${gryf.schema}.ENTERPRISES
) ents ON ( contr.ENTERPRISE_ID = ents.id )
WHEN MATCHED THEN
 UPDATE SET contr.code = ents.code,
            contr.ACCOUNT_PAYMENT = ents.ACCOUNT_PAYMENT;


  MERGE INTO ${gryf.schema}.CONTRACTS_AUDIT  contr USING (
  SELECT
       code,
       ACCOUNT_PAYMENT,
       id
   FROM
       ${gryf.schema}.ENTERPRISES
  ) ents ON ( contr.ENTERPRISE_ID = ents.id )
  WHEN MATCHED THEN
   UPDATE SET contr.code = ents.code,
              contr.ACCOUNT_PAYMENT = ents.ACCOUNT_PAYMENT;




ALTER TABLE ${gryf.schema}.CONTRACTS modify (CODE VARCHAR2(8) default null , ACCOUNT_PAYMENT VARCHAR2(26) default null);
ALTER TABLE ${gryf.schema}.CONTRACTS_AUDIT modify (CODE VARCHAR2(8) default null , ACCOUNT_PAYMENT VARCHAR2(26) default null);

--ii.	Usuwamy kolumny z tabelki
--d.	Dla mśp
--i.	Do kontraktu przeniesione są wartości CODE I ACCOUNT z APP_PBE.ENTERPRISES
--ii.	Wartości CODE i ACCOUNT z APP_PBE.INDIVIDUALS są usuwane – oraz „zwalniamy” zajęte kody ACCOUNT do ponownego wykorzystania!
--iii.	Usuwamy kolumny z APP_PBE.ENTERPRISES
alter table    ${gryf.schema}.INDIVIDUALS drop (code, ACCOUNT_PAYMENT);

alter table    ${gryf.schema}.INDIVIDUALS_AUDIT drop (code, ACCOUNT_PAYMENT);

alter table    ${gryf.schema}.ENTERPRISES drop (code, ACCOUNT_PAYMENT);

alter table    ${gryf.schema}.ENTERPRISES_AUDIT drop (code, ACCOUNT_PAYMENT);

--e.	TODO MGU: CZY PRZENOSIMY RÓWNIEŻ ACCOUNT_REPAYMENT? – NIE kolumna nieużywana i niech tak pozostanie – nic z tym nie robimy
--f.	TODO KKR: Zmiana w imporcie do Navision
--
--6.	Zmiana sposobu zapisu adresu
--a.	Dodajemy do kontraktu APP_PBE.CONTRACTS nowe kolumny (kopia np. z APP_PBE.INDIVIDUALS) :
--i.	ZIP_CODE_CORR,
--ii.	ZIP_CODE_INVOICE,
--iii.	ADDRESS_INVOICE,
--iv.	ADDRESS_CORR
alter table ${gryf.schema}.CONTRACTS ADD (
     ADDRESS_INVOICE VARCHAR2(200),
     ZIP_CODE_INVOICE NUMBER,
     ADDRESS_CORR VARCHAR2(200),
     ZIP_CODE_CORR NUMBER);

alter table ${gryf.schema}.CONTRACTS_AUDIT ADD (
     ADDRESS_INVOICE VARCHAR2(200),
     ZIP_CODE_INVOICE NUMBER,
     ADDRESS_CORR VARCHAR2(200),
     ZIP_CODE_CORR NUMBER);

 ALTER TABLE ${gryf.schema}.CONTRACTS
 ADD CONSTRAINT ZC_CON_CORR_FK FOREIGN KEY
 (
   ZIP_CODE_CORR
 )
 REFERENCES ${eagle.schema}.zip_codes ( id ) enable;

 ALTER TABLE ${gryf.schema}.CONTRACTS
 ADD CONSTRAINT ZC_CON_INV_FK FOREIGN KEY
 (
   ZIP_CODE_INVOICE
 )
 REFERENCES ${eagle.schema}.zip_codes ( id ) enable;


COMMENT ON COLUMN  ${gryf.schema}.CONTRACTS.ADDRESS_INVOICE IS 'Adres do faktury';
COMMENT ON COLUMN  ${gryf.schema}.CONTRACTS.ZIP_CODE_INVOICE IS 'Kod pocztowy adresu do faktury';
COMMENT ON COLUMN  ${gryf.schema}.CONTRACTS.ADDRESS_CORR IS 'Adres korespondencyjny';
COMMENT ON COLUMN  ${gryf.schema}.CONTRACTS.ZIP_CODE_CORR IS 'Kod pocztowy adresu korespondencyjnego';

-- UZUPELNIJ DANE
--- Uwaga: Kolejnosś jest wazna. 1st Ind 2nd Ent
MERGE INTO ${gryf.schema}.CONTRACTS  contr USING (
SELECT
    ADDRESS_INVOICE ,
    ZIP_CODE_INVOICE ,
    ADDRESS_CORR,
    ZIP_CODE_CORR,
    id
 FROM
     ${gryf.schema}.INDIVIDUALS
) inds ON ( contr.INDIVIDUAL_ID = INDS.ID AND contr.ENTERPRISE_ID IS NULL)
WHEN MATCHED THEN
 UPDATE SET
     CONTR.ADDRESS_INVOICE   =   INDS.ADDRESS_INVOICE,
     CONTR.ZIP_CODE_INVOICE  =   INDS.ZIP_CODE_INVOICE,
     CONTR.ADDRESS_CORR      =   INDS.ADDRESS_CORR,
     CONTR.ZIP_CODE_CORR     =   INDS.ZIP_CODE_CORR            ;

MERGE INTO ${gryf.schema}.CONTRACTS_AUDIT  contr USING (
SELECT
    ADDRESS_INVOICE ,
    ZIP_CODE_INVOICE ,
    ADDRESS_CORR,
    ZIP_CODE_CORR,
    id
 FROM
     ${gryf.schema}.INDIVIDUALS
) inds ON ( contr.INDIVIDUAL_ID = INDS.ID AND contr.ENTERPRISE_ID IS NULL)
WHEN MATCHED THEN
 UPDATE SET
     CONTR.ADDRESS_INVOICE   =   INDS.ADDRESS_INVOICE,
     CONTR.ZIP_CODE_INVOICE  =   INDS.ZIP_CODE_INVOICE,
     CONTR.ADDRESS_CORR      =   INDS.ADDRESS_CORR,
     CONTR.ZIP_CODE_CORR     =   INDS.ZIP_CODE_CORR            ;

MERGE INTO ${gryf.schema}.CONTRACTS  contr USING (
SELECT
    ADDRESS_INVOICE ,
    ZIP_CODE_INVOICE ,
    ADDRESS_CORR,
    ZIP_CODE_CORR,
    ID
 FROM
     ${gryf.schema}.ENTERPRISES
) ents ON ( contr.ENTERPRISE_ID = ents.ID )
WHEN MATCHED THEN
 UPDATE SET
     contr.ADDRESS_INVOICE   =    ents.ADDRESS_INVOICE,
     contr.ZIP_CODE_INVOICE  =    ents.ZIP_CODE_INVOICE,
     contr.ADDRESS_CORR     =    ents.ADDRESS_CORR,
     contr.ZIP_CODE_CORR     =    ents.ZIP_CODE_CORR;

MERGE INTO ${gryf.schema}.CONTRACTS_AUDIT  contr USING (
SELECT
    ADDRESS_INVOICE ,
    ZIP_CODE_INVOICE ,
    ADDRESS_CORR,
    ZIP_CODE_CORR,
    ID
 FROM
     ${gryf.schema}.ENTERPRISES
) ents ON ( contr.ENTERPRISE_ID = ents.ID )
WHEN MATCHED THEN
 UPDATE SET
     contr.ADDRESS_INVOICE   =    ents.ADDRESS_INVOICE,
     contr.ZIP_CODE_INVOICE  =    ents.ZIP_CODE_INVOICE,
     contr.ADDRESS_CORR     =    ents.ADDRESS_CORR,
     contr.ZIP_CODE_CORR     =    ents.ZIP_CODE_CORR;


 ALTER TABLE ${gryf.schema}.CONTRACTS MODIFY (
          ADDRESS_INVOICE NOT NULL,
          ZIP_CODE_INVOICE NOT NULL,
          ADDRESS_CORR NOT NULL,
          ZIP_CODE_CORR NOT NULL
      );

--b.	TODO MGU: Pytanie: czy przenieść jakieś inne dane?
--i.	DOCUMENT_NUMBER
--ii.	DOCUMENT_TYPE
--To zostaje z osobą – bo nie wykorzystywane – zostawiamy bez zmian, chyba że starczy czasu to można przenieść z innymi
--
--c.	Zmiana w logice zapisu adresów:
--i.	Adres powinien być zapisywany tak jak dawniej w indywidualsie/MŚP
--1.	Będzie to ostatnio wczytywany adres (w przypadku gdy użytkownik posiada dwie umowy)
--ii.	Na poziomie kontraktu powinien być zapisany adres, który był podany przy wczytywaniu danej umowy
--1.	Jeśli użytkownik ma dwie umowy, to możliwe jest, że na dwóch kontraktach ma inne adresy
--d.	TODO MAPO: Należy zwrócić uwagę przy raportowaniu, z jakiego źródła pobierany jest adres
--e.	TODO KKR: Zmiana w imporcie do Navision
--f.	TODO STEFAN: Zmiana w imporcie (umów?) – zapis danych do kontraktu*/


-- Pamietac o Triggerach!!!!!!!!