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
     (SELECT CODE FROM PROGRAM_CODE ) || '/[0-9]+/[0-9]+' value,
     '(GRYF) Wzorzec Regexp walidacji identyfikatora zamówienia' description
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
     (SELECT CODE FROM PROGRAM_CODE ) || '/[0-9]+/1$' value,
     '(GRYF) Wzorzec Regexp walidacji identyfikatora zamówienia' description
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
        'WUPKK' AS code,
        'WZ' AS REGEXP_PREFIX
    FROM
        dual
)
SELECT
     (SELECT CODE FROM PROGRAM_CODE ) || '_' || 'GRYF_EXTERNAL_ORDER_ID_PATTERN' name,
     (SELECT CODE FROM PROGRAM_CODE ) || '/[0-9]+/[0-9]+' value,
     '(GRYF) Wzorzec Regexp walidacji identyfikatora zamówienia' description
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
        'WUPKK' AS code,
        'WKK' AS REGEXP_PREFIX
    FROM
        dual
)
SELECT
     (SELECT CODE FROM PROGRAM_CODE ) || '_' || 'GRYF_IMPORT_TRAINING_SEARCH_PATT' name,
     (SELECT CODE FROM PROGRAM_CODE ) || '/[0-9]+/1$' value,
     '(GRYF) Wzorzec Regexp wyszukiwania importowanych szkoleń' description
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