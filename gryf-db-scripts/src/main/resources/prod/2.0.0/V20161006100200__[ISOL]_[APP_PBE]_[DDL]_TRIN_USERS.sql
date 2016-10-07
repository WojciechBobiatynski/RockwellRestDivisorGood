----------------------------
--TRAINING_INSTITUTION_USERS
----------------------------

CREATE SEQUENCE EAGLE.TI_USR_SEQ 
   MINVALUE 1 
   MAXVALUE 9999999999999999999999999999 
   INCREMENT BY 1 
   START WITH 1 
   CACHE 20 
   NOORDER NOCYCLE ;

CREATE TABLE APP_PBE.TRAINING_INSTITUTION_USERS(
  ID NUMBER NOT NULL,
  TRAINING_ISTITUTION_ID NUMBER NOT NULL,
  LOGIN VARCHAR2(150) NOT NULL,
  EMAIL VARCHAR2(150) NOT NULL,
  PASSWORD varchar2 (60) NOT NULL,
  LAST_LOGIN_DATE DATE,
  PASSWORD_EXP_DATE DATE,
  IS_ACTIVE varchar2(1 BYTE) DEFAULT 1 NOT NULL,
  VERSION Number,
  CREATED_USER Varchar2(150) NOT NULL,
  CREATED_TIMESTAMP Timestamp(6) NOT NULL,
  MODIFIED_USER Varchar2(150) NOT NULL,
  MODIFIED_TIMESTAMP Timestamp(6) NOT NULL
);

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'TRAINING_INSTITUTION_USERS', 'ALL')); 
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'TRAINING_INSTITUTION_USERS', 'ALL'));   
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'TRAINING_INSTITUTION_USERS', 'SELECT'));      
END;
/
-- Indeksy
CREATE UNIQUE INDEX APP_PBE.TI_USR_PK ON APP_PBE.TRAINING_INSTITUTION_USERS (ID);
CREATE UNIQUE INDEX APP_PBE.TI_USR_LOGIN_UIDX ON APP_PBE.TRAINING_INSTITUTION_USERS (LOGIN);

-- Klucze
ALTER TABLE APP_PBE.TRAINING_INSTITUTION_USERS ADD CONSTRAINT TI_USR_PK PRIMARY KEY (ID);
--FK
ALTER TABLE APP_PBE.TRAINING_INSTITUTION_USERS ADD CONSTRAINT TI_USR_TRIN_FK FOREIGN KEY (TRAINING_ISTITUTION_ID) REFERENCES APP_PBE.TRAINING_INSTITUTIONS (ID);

-- Komentarze
COMMENT ON TABLE APP_PBE.TRAINING_INSTITUTION_USERS IS '@Author(Jakub.Bentyn); @Project(Gryf-PBE); @Date(2016-10-05) ;@Purpose(Tabela przechowująca użytkowników IS);';

COMMENT ON COLUMN APP_PBE.TRAINING_INSTITUTION_USERS.ID IS 'Unikalne ID użytkownika IS nadawane automatycznie. Sekwencja: EAGLE.TI_USR_SEQ ';
COMMENT ON COLUMN APP_PBE.TRAINING_INSTITUTION_USERS.TRAINING_ISTITUTION_ID IS 'ID instytucji szkoleniowej użytkownika.';
COMMENT ON COLUMN APP_PBE.TRAINING_INSTITUTION_USERS.LOGIN IS 'Login użytkownika IS'; 
COMMENT ON COLUMN APP_PBE.TRAINING_INSTITUTION_USERS.EMAIL IS 'Adres email użytkownika';
COMMENT ON COLUMN APP_PBE.TRAINING_INSTITUTION_USERS.PASSWORD IS 'Hash hasła'; 
COMMENT ON COLUMN APP_PBE.TRAINING_INSTITUTION_USERS.LAST_LOGIN_DATE IS 'Data ostatniego udanego logowania';
COMMENT ON COLUMN APP_PBE.TRAINING_INSTITUTION_USERS.PASSWORD_EXP_DATE IS 'Data wygaśnięcia hasła';
COMMENT ON COLUMN APP_PBE.TRAINING_INSTITUTION_USERS.IS_ACTIVE IS 'Flaga oznaczająca czy konto uzytkownika jest aktywne'; 
COMMENT ON COLUMN APP_PBE.TRAINING_INSTITUTION_USERS.VERSION IS 'Kolumna techniczna używana do mechanizmu Optimistic Lock'; 
COMMENT ON COLUMN APP_PBE.TRAINING_INSTITUTION_USERS.CREATED_USER IS 'Login użytkownika, który utworzył rekord'; 
COMMENT ON COLUMN APP_PBE.TRAINING_INSTITUTION_USERS.CREATED_TIMESTAMP IS 'Data utworzenia rekordu'; 
COMMENT ON COLUMN APP_PBE.TRAINING_INSTITUTION_USERS.MODIFIED_USER IS 'Login użytkownika, który ostatnio modyfikował rekord'; 
COMMENT ON COLUMN APP_PBE.TRAINING_INSTITUTION_USERS.MODIFIED_TIMESTAMP IS 'Data ostatniej modyfikacji rekordu'; 

----------------------------
--TE_ROLES
----------------------------

CREATE TABLE APP_PBE.TE_ROLES(
  CODE	VARCHAR2(50 BYTE)	NOT NULL,
  DESCRIPTION	VARCHAR2(300 BYTE),
  CONTEXT VARCHAR2(10) NOT NULL
);

BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'TE_ROLES', 'ALL')); 
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'TE_ROLES', 'ALL'));   
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'TE_ROLES', 'SELECT'));      
END;
/

-- Indeksy
CREATE UNIQUE INDEX APP_PBE.TE_ROLES_PK ON APP_PBE.TE_ROLES (CODE);

--klucze
ALTER TABLE APP_PBE.TE_ROLES ADD CONSTRAINT TE_ROLES_PK PRIMARY KEY (CODE);

-- Komentarze
COMMENT ON TABLE APP_PBE.TE_ROLES IS '@Author(Jakub.Bentyn); @Project(Gryf-PBE); @Date(2016-10-05) ;@Purpose(Tabela przechowująca role użytkowników IS);';

COMMENT ON COLUMN APP_PBE.TE_ROLES.CODE IS 'Nazwa roli';  
COMMENT ON COLUMN APP_PBE.TE_ROLES.DESCRIPTION IS 'Opis roli'; 
COMMENT ON COLUMN APP_PBE.TE_ROLES.CONTEXT IS 'Kontekst w jakim używana jest rola. TI - kontekst instytucji szkoleniowej'; 
----------------------------
--TE_PRIVILEGES
----------------------------

CREATE TABLE APP_PBE.TE_PRIVILEGES(
  CODE	VARCHAR2(50 BYTE)	NOT NULL,
  DESCRIPTION	VARCHAR2(300 BYTE),
  CONTEXT VARCHAR2(10) NOT NULL
);

BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'TE_PRIVILEGES', 'ALL')); 
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'TE_PRIVILEGES', 'ALL'));   
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'TE_PRIVILEGES', 'SELECT'));      
END;
/

-- indeksy
CREATE UNIQUE INDEX APP_PBE.TE_PRIVS_PK ON APP_PBE.TE_PRIVILEGES (CODE);

-- klucze
ALTER TABLE APP_PBE.TE_PRIVILEGES ADD CONSTRAINT TE_PRIVS_PK PRIMARY KEY (CODE);

--komentarze
COMMENT ON TABLE APP_PBE.TE_PRIVILEGES IS '@Author(Jakub.Bentyn); @Project(Gryf-PBE); @Date(2016-10-05) ;@Purpose(Tabela przechowująca uprawnienia bezpieczeństwa uzytkowników IS);';

COMMENT ON COLUMN APP_PBE.TE_PRIVILEGES.CODE IS 'Nazwa uprawnienia bezpieczeństwa'; 
COMMENT ON COLUMN APP_PBE.TE_PRIVILEGES.DESCRIPTION IS 'Opis uprawnienia';
COMMENT ON COLUMN APP_PBE.TE_PRIVILEGES.CONTEXT IS 'Kontekst w jakim używana jest przywilej. TI - kontekst instytucji szkoleniowej'; 


--------------------
--TI_USER_IN_ROLES
--------------------

CREATE TABLE APP_PBE.TI_USER_IN_ROLES(
  TI_USER_ID	NUMBER NOT NULL,	
  TE_ROLE_CODE	VARCHAR2(50 BYTE) NOT NULL	
);

BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'TI_USER_IN_ROLES', 'ALL')); 
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'TI_USER_IN_ROLES', 'ALL'));   
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'TI_USER_IN_ROLES', 'SELECT'));      
END;
/
-- indeksy
CREATE UNIQUE INDEX APP_PBE.TI_USR_ROLES_PK ON APP_PBE.TI_USER_IN_ROLES (TI_USER_ID, TE_ROLE_CODE);

-- Klucze
ALTER TABLE APP_PBE.TI_USER_IN_ROLES ADD CONSTRAINT TI_USR_ROLES_PK PRIMARY KEY (TI_USER_ID, TE_ROLE_CODE);
--FK
ALTER TABLE APP_PBE.TI_USER_IN_ROLES ADD CONSTRAINT TI_USR_ROLES_USER_FK FOREIGN KEY (TI_USER_ID) REFERENCES APP_PBE.TRAINING_INSTITUTION_USERS (ID);
ALTER TABLE APP_PBE.TI_USER_IN_ROLES ADD CONSTRAINT TI_USR_ROLES_ROLE_FK FOREIGN KEY (TE_ROLE_CODE) REFERENCES APP_PBE.TE_ROLES (CODE);

--komentarze
COMMENT ON TABLE APP_PBE.TI_USER_IN_ROLES IS '@Author(Jakub.Bentyn); @Project(Gryf-PBE); @Date(2016-10-05) ;@Purpose(Tabela złaczeniowa ról i  uzytkowników IS);';

COMMENT ON COLUMN APP_PBE.TI_USER_IN_ROLES.TI_USER_ID	IS 'Id uzytkownika IS';	
COMMENT ON COLUMN APP_PBE.TI_USER_IN_ROLES.TE_ROLE_CODE	IS 'Kod roli bezpieczeństwa aplikacji IS';	

--------------------
--TE_PRIV_IN_ROLES
--------------------

CREATE TABLE APP_PBE.TE_PRIV_IN_ROLES(
  TE_PRIV_CODE	VARCHAR2(50 BYTE) NOT NULL,	
  TE_ROLE_CODE	VARCHAR2(50 BYTE) NOT NULL
);

BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'TE_PRIV_IN_ROLES', 'ALL')); 
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'TE_PRIV_IN_ROLES', 'ALL'));   
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'TE_PRIV_IN_ROLES', 'SELECT'));      
END;
/

-- Indeksy
CREATE UNIQUE INDEX APP_PBE.TE_PRIV_ROLES_PK ON APP_PBE.TE_PRIV_IN_ROLES (TE_PRIV_CODE, TE_ROLE_CODE);

-- Klucze
ALTER TABLE APP_PBE.TE_PRIV_IN_ROLES ADD CONSTRAINT TE_PRIV_ROLES_PK PRIMARY KEY (TE_PRIV_CODE, TE_ROLE_CODE);
--FK
ALTER TABLE APP_PBE.TE_PRIV_IN_ROLES ADD CONSTRAINT PRIV_ROLES_PRIV_FK FOREIGN KEY (TE_PRIV_CODE) REFERENCES APP_PBE.TE_PRIVILEGES (CODE);
ALTER TABLE APP_PBE.TE_PRIV_IN_ROLES ADD CONSTRAINT PRIV_ROLES_ROLE_FK FOREIGN KEY (TE_ROLE_CODE) REFERENCES APP_PBE.TE_ROLES (CODE);

--komentarze
COMMENT ON TABLE APP_PBE.TE_PRIV_IN_ROLES IS '@Author(Jakub.Bentyn); @Project(Gryf-PBE); @Date(2016-10-05) ;@Purpose(Tabela złaczeniowa ról i uprawnień bezpieczństwa w aplikacji IS);';

COMMENT ON COLUMN APP_PBE.TE_PRIV_IN_ROLES.TE_PRIV_CODE	IS 'Kod uprawnienia';	
COMMENT ON COLUMN APP_PBE.TE_PRIV_IN_ROLES.TE_ROLE_CODE	IS 'Kod roli';
