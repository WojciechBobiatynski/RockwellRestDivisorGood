--------------------------------------------TABELA ${eagle.schema}.adm_parameters --------------------------------------------
-- Kierunek Kariera Zawodowa - Prameter: GRYF_EXTERNAL_ORDER_ID_PATTERN

MERGE INTO ${eagle.schema}.adm_parameters ug USING
( WITH program_code AS (
    SELECT
        'WUPKKZ' AS code,
        'WZ' AS REGEXP_PREFIX
    FROM
        dual
)
SELECT
     (SELECT CODE FROM PROGRAM_CODE ) || '_' || 'GRYF_EXTERNAL_ORDER_ID_PATTERN' name,
     (SELECT REGEXP_PREFIX FROM PROGRAM_CODE ) || '/[0-9]+Z/[0-9]+' value,
     '(GRYF) Wzorzec Regexp walidacji identyfikatora zamówienia dla programu KKZ' description
 FROM
     dual
) ins ON ( ug.name = ins.name )
WHEN MATCHED THEN UPDATE SET ug.value = ins.value,
ug.description = ins.description
WHEN NOT MATCHED THEN INSERT (
    name,
    value,
    description ) VALUES (
    ins.name,
    ins.value,
    ins.description );

-- Kierunek Kariera Zawodowa - Prameter: GRYF_IMPORT_TRAINING_SEARCH_PATT
MERGE INTO ${eagle.schema}.adm_parameters ug USING
( WITH program_code AS (
    SELECT
        'WUPKKZ' AS code,
        'WZ' AS REGEXP_PREFIX
    FROM
        dual
)
SELECT
     (SELECT CODE FROM PROGRAM_CODE ) || '_' || 'GRYF_IMPORT_TRAINING_SEARCH_PATT' name,
     (SELECT REGEXP_PREFIX FROM PROGRAM_CODE ) || '/[0-9]+Z/1$' value,
     '(GRYF) Wzorzec Regexp wybierania szkoleń dla progamu KKZ' description
 FROM
     dual
) ins ON ( ug.name = ins.name )
WHEN MATCHED THEN UPDATE SET ug.value = ins.value,
ug.description = ins.description
WHEN NOT MATCHED THEN INSERT (
    name,
    value,
    description ) VALUES (
    ins.name,
    ins.value,
    ins.description );

-- Kierunek Kariera
-- Prameter: GRYF_EXTERNAL_ORDER_ID_PATTERN
MERGE INTO ${eagle.schema}.adm_parameters ug USING
( WITH program_code AS (
    SELECT
        'WUPKKK' AS code,
        'WKK' AS REGEXP_PREFIX
    FROM
        dual
)
SELECT
     (SELECT CODE FROM PROGRAM_CODE ) || '_' || 'GRYF_EXTERNAL_ORDER_ID_PATTERN' name,
     (SELECT REGEXP_PREFIX FROM PROGRAM_CODE ) || '/[0-9]+/[0-9]+' value,
     '(GRYF) Wzorzec Regexp walidacji identyfikatora zamówienia dla programu KK' description
 FROM
     dual
) ins ON ( ug.name = ins.name )
WHEN MATCHED THEN UPDATE SET ug.value = ins.value,
ug.description = ins.description
WHEN NOT MATCHED THEN INSERT (
    name,
    value,
    description ) VALUES (
    ins.name,
    ins.value,
    ins.description );

-- Kierunek Kariera --
-- Prameter: GRYF_IMPORT_TRAINING_SEARCH_PATT
MERGE INTO ${eagle.schema}.adm_parameters ug USING
( WITH program_code AS (
    SELECT
        'WUPKKK' AS code,
        'WKK' AS REGEXP_PREFIX
    FROM
        dual
)
SELECT
     (SELECT CODE FROM PROGRAM_CODE ) || '_' || 'GRYF_IMPORT_TRAINING_SEARCH_PATT' name,
     (SELECT REGEXP_PREFIX FROM PROGRAM_CODE ) || '/[0-9]+/1$' value,
     '(GRYF) Wzorzec Regexp wyszukiwania importowanych szkoleń dla programu KK' description
 FROM
     dual
) ins ON ( ug.name = ins.name )
WHEN MATCHED THEN UPDATE SET ug.value = ins.value,
ug.description = ins.description
WHEN NOT MATCHED THEN INSERT (
    name,
    value,
    description ) VALUES (
    ins.name,
    ins.value,
    ins.description );

--------------------------------------------------------------------------------------------------