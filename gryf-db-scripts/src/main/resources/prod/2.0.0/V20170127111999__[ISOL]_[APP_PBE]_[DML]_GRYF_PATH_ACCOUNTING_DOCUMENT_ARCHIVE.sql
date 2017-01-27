MERGE INTO EAGLE.ADM_PARAMETERS ug USING (
                                           SELECT
                                             'GRYF_PATH_ACCOUNTING_DOCUMENT_ARCHIVE' NAME,
                                             'ArchiwumDokumentowKsiegowychGryf/dev/%s/WUP/' VALUE,
                                             'Scie¿ka do archiwum elektronicznego' DESCRIPTION
                                           FROM dual
                                         ) ins
ON (ug.NAME = ins.NAME)
WHEN MATCHED THEN
UPDATE SET ug.VALUE = ins.VALUE, ug.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED THEN INSERT
  (NAME,VALUE,DESCRIPTION)
VALUES (ins.NAME, ins.VALUE, ins.DESCRIPTION);