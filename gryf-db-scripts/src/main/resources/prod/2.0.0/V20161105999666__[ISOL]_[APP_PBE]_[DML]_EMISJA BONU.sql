
--TRAINING_INSTANCE_STATUSES
insert into APP_PBE.TI_TRAINING_INSTANCE_STATUSES (ID, NAME, ORDINAL) values ('RES', 'Zarezerwowane', 1);
insert into APP_PBE.TI_TRAINING_INSTANCE_STATUSES (ID, NAME, ORDINAL) values ('DONE', 'Odbyte', 2);
insert into APP_PBE.TI_TRAINING_INSTANCE_STATUSES (ID, NAME, ORDINAL) values ('REIMB', 'Rozliczone', 3);
insert into APP_PBE.TI_TRAINING_INSTANCE_STATUSES (ID, NAME, ORDINAL) values ('CANCEL', 'Anulowane', 4);



--PBE_PRODUCT_INSTANCE_STATUSES
insert into APP_PBE.PBE_PRODUCT_INSTANCE_STATUSES (ID, NAME, ORDINAL) values ('NEW', 'Nowy', 1);
insert into APP_PBE.PBE_PRODUCT_INSTANCE_STATUSES (ID, NAME, ORDINAL) values ('ASSIGN', 'Przypisane', 2);
insert into APP_PBE.PBE_PRODUCT_INSTANCE_STATUSES (ID, NAME, ORDINAL) values ('RES', 'Zarezerwowany', 3);
insert into APP_PBE.PBE_PRODUCT_INSTANCE_STATUSES (ID, NAME, ORDINAL) values ('USE', 'Wykorzystany', 4);
insert into APP_PBE.PBE_PRODUCT_INSTANCE_STATUSES (ID, NAME, ORDINAL) values ('REIMB', 'Rozliczony', 5);

--PBE_PRODUCT_INSTANCE_POOL_STAS
insert into APP_PBE.PBE_PRODUCT_INSTANCE_POOL_STAS (ID, NAME, ORDINAL) values ('ACTIVE', 'Aktywna', 1);
insert into APP_PBE.PBE_PRODUCT_INSTANCE_POOL_STAS (ID, NAME, ORDINAL) values ('USE', 'Wykorzystany', 2);
insert into APP_PBE.PBE_PRODUCT_INSTANCE_POOL_STAS (ID, NAME, ORDINAL) values ('REIMB', 'Rozliczony', 3);

--PBE_PRODUCT_INSTANCE_E_T
insert into APP_PBE.PBE_PRODUCT_INSTANCE_E_T (ID, NAME, ORDINAL) values ('EMMITED', 'Wemitowany', 1);
insert into APP_PBE.PBE_PRODUCT_INSTANCE_E_T (ID, NAME, ORDINAL) values ('RES', 'Zarezerwowanie', 2);
insert into APP_PBE.PBE_PRODUCT_INSTANCE_E_T (ID, NAME, ORDINAL) values ('USE', 'Wykorzystanie', 3);
insert into APP_PBE.PBE_PRODUCT_INSTANCE_E_T (ID, NAME, ORDINAL) values ('REIMB', 'Rozliczenie', 4);

--PBE_PRODUCT_INSTANCE_POOL_E_T
insert into APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E_T (ID, NAME, ORDINAL) values ('ACTIVE', 'Aktywna (Utworzenie)', 1);
insert into APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E_T (ID, NAME, ORDINAL) values ('USE', 'Wykorzystanie', 2);
insert into APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E_T (ID, NAME, ORDINAL) values ('REIMB', 'Rozliczonie', 3);



