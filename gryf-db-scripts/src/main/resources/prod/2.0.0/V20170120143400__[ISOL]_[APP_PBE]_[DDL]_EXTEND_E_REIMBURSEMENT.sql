ALTER TABLE APP_PBE.E_REIMBURSEMENTS ADD (EXPIRED_PRODUCTS_NUM NUMBER(10,0));

COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.EXPIRED_PRODUCTS_NUM IS 'Rozliczona ilo�� niewykorzystanych bon�w. Przyjmuje warto�� tylko dla rozliczenia niewykorzystanej puli bon�w';